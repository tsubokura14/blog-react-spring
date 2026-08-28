package com.example.demo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.dto.PostCreateRequest;
import com.example.demo.dto.PostResponse;
import com.example.demo.exception.PostNotFoundException;
import com.example.demo.service.PostService;

import tools.jackson.databind.ObjectMapper;

// @WebMvcTest(PostController.class)は、PostControllerとその周辺のWeb層
// (HandlerMapping、HttpMessageConverter、@RestControllerAdviceなど)だけを起動する軽量なテスト。
// @Serviceである実際のPostService(DB接続込み)は対象外になるため、
// @MockitoBeanで差し替えて「Serviceがこう返したら/こう例外を投げたら」を固定して検証する
@WebMvcTest(PostController.class)
class PostControllerTest {

	@Autowired
	private MockMvc mockMvc;

	// リクエストボディのJSON文字列を組み立てるのに使う。
	@Autowired
	private ObjectMapper objectMapper;

	// Serviceはモック化してDBアクセスを防ぐ
	@MockitoBean
	private PostService postService;

	@Test
	@DisplayName("投稿一覧を取得すると、Serviceが返したPostResponseの内容がそのままJSONで返る")
	void findAll_returnsAllPosts() throws Exception {
		// 準備: postService.findAll()が呼ばれたら、PostResponseを1件持つリストを返すよう固定する
		when(postService.findAll()).thenReturn(List.of(new PostResponse(1L, "test", "hello", 0)));

		// 実行: GET /postsを擬似的に送信し、その結果を検証する
		mockMvc.perform(get("/posts"))
				.andExpect(status().isOk())
				// jsonPathでレスポンスJSONの中身をピンポイントに検証(配列の0番目)
				.andExpect(jsonPath("$[0].id").value(1))
				.andExpect(jsonPath("$[0].title").value("test"));
	}

	@Test
	@DisplayName("存在するidを指定すると、該当する投稿が200で返る")
	void findById_returnsPostWhenExists() throws Exception {
		// 準備: id=1で呼ばれたときだけ、対応するPostResponseを返すよう固定する
		when(postService.findById(1L)).thenReturn(new PostResponse(1L, "test", "hello", 0));

		mockMvc.perform(get("/posts/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.title").value("test"));
	}

	@Test
	@DisplayName("存在しないidを指定すると、GlobalExceptionHandlerによって404とエラーメッセージが返る")
	void findById_returns404WhenNotFound() throws Exception {
		// 準備: 戻り値ではなく、id=999で呼ばれたら例外を投げるよう固定する(thenThrow)
		when(postService.findById(999L)).thenThrow(new PostNotFoundException(999L));

		// PostServiceが投げたPostNotFoundExceptionをGlobalExceptionHandlerが捕まえ、
		// 404 + ErrorResponseに変換されることを確認する
		mockMvc.perform(get("/posts/999"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404))
				.andExpect(jsonPath("$.message").value("Post not found: id=999"));
	}

	@Test
	@DisplayName("title/bodyが入った正常なリクエストなら、作成された投稿が200で返る")
	void create_returnsCreatedPostWhenValid() throws Exception {
		// リクエストボディとして送るDTOを組み立てる
		PostCreateRequest request = new PostCreateRequest();
		request.setTitle("validation test");
		request.setBody("hello");

		// 準備: create()にどんなPostCreateRequestが渡されても(any)、固定のPostResponseを返す
		when(postService.create(any(PostCreateRequest.class)))
				.thenReturn(new PostResponse(2L, "validation test", "hello", 0));

		// 実行: POST /postsにJSON化したrequestをボディとして送信する
		mockMvc.perform(post("/posts")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(2));
	}

	@Test
	@DisplayName("titleが空のリクエストは、@NotBlankの検証に引っかかり400とメッセージが返る")
	void create_returns400WhenTitleIsBlank() throws Exception {
		// curl.exeでの手動確認時にPowerShellの引数渡しでハマった検証(バリデーション違反→400)を、
		// 自動テストとして固定している。これで以後は手動でのcurl確認が不要になる
		PostCreateRequest request = new PostCreateRequest();
		request.setTitle("");
		request.setBody("test");

		// このテストではpostService.create()をwhen(...)で設定していない点に注意。
		// title未入力は@NotBlankでコントローラに届く前(バリデーション)で弾かれるため、
		// Service(モック)の呼び出しまで到達しない
		mockMvc.perform(post("/posts")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("title: title is required"));
	}

	@Test
	@DisplayName("存在するidを指定して更新すると、更新後の内容が200で返る")
	void update_returnsUpdatedPostWhenExists() throws Exception {
		PostCreateRequest request = new PostCreateRequest();
		request.setTitle("updated");
		request.setBody("updated body");

		// 準備: 第1引数はeq(1L)でid=1のときだけに限定し、第2引数はany(...)で内容を問わず一致させる。
		// このように引数マッチャー(eq/any)を1つでも使ったら、他の引数も全てマッチャーで書く必要がある
		when(postService.update(eq(1L), any(PostCreateRequest.class)))
				.thenReturn(new PostResponse(1L, "updated", "updated body", 0));

		mockMvc.perform(put("/posts/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value("updated"));
	}

	@Test
	@DisplayName("存在するidを指定して削除すると、200が返りServiceのdelete()が呼ばれる")
	void delete_returns200AndCallsServiceWhenExists() throws Exception {
		// delete()は戻り値がvoidなのでwhen(...)での準備は不要。実行だけ先に行う
		mockMvc.perform(delete("/posts/1"))
				.andExpect(status().isOk());

		// 戻り値で検証できない代わりに、postService.delete(1L)が実際に呼ばれたことを確認する
		verify(postService).delete(1L);
	}

	@Test
	@DisplayName("存在しないidを指定して削除すると、GlobalExceptionHandlerによって404が返る")
	void delete_returns404WhenNotFound() throws Exception {
		// 準備: delete()は戻り値がvoidのため、when(postService.delete(999L))とは書けない。
		// 代わりにdoThrow(...).when(モック).メソッド(...)という順番で「例外を投げる」ことを設定する
		doThrow(new PostNotFoundException(999L)).when(postService).delete(999L);

		mockMvc.perform(delete("/posts/999"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value("Post not found: id=999"));
	}

}