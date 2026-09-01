package com.example.demo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.dto.CommentCreateRequest;
import com.example.demo.dto.CommentResponse;
import com.example.demo.exception.CommentNotFoundException;
import com.example.demo.exception.PostNotFoundException;
import com.example.demo.service.CommentService;

import tools.jackson.databind.ObjectMapper;

// PostControllerTestと同じ考え方で、CommentServiceをモック化してWeb層だけを検証する
@WebMvcTest(CommentController.class)
class CommentControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private CommentService commentService;

	@Test
	@DisplayName("author/bodyが入った正常なリクエストなら、作成されたコメントが200で返る")
	void create_returnsCreatedCommentWhenValid() throws Exception {
		CommentCreateRequest request = new CommentCreateRequest();
		request.setAuthor("tsubo");
		request.setBody("hello");

		when(commentService.create(eq(1L), any(CommentCreateRequest.class)))
				.thenReturn(new CommentResponse(1L, "tsubo", "hello", LocalDateTime.now()));

		mockMvc.perform(post("/posts/1/comments")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.author").value("tsubo"))
				.andExpect(jsonPath("$.body").value("hello"));
	}

	@Test
	@DisplayName("authorが空のリクエストは、@NotBlankの検証に引っかかり400とメッセージが返る")
	void create_returns400WhenAuthorIsBlank() throws Exception {
		CommentCreateRequest request = new CommentCreateRequest();
		request.setAuthor("");
		request.setBody("hello");

		mockMvc.perform(post("/posts/1/comments")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("author: author is required"));
	}

	@Test
	@DisplayName("存在しないpostIdにコメント作成しようとすると、GlobalExceptionHandlerによって404が返る")
	void create_returns404WhenPostNotFound() throws Exception {
		CommentCreateRequest request = new CommentCreateRequest();
		request.setAuthor("tsubo");
		request.setBody("hello");

		when(commentService.create(eq(999L), any(CommentCreateRequest.class)))
				.thenThrow(new PostNotFoundException(999L));

		mockMvc.perform(post("/posts/999/comments")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value("Post not found: id=999"));
	}

	@Test
	@DisplayName("コメント一覧を取得すると、Serviceが返したCommentResponseの内容がそのままJSONで返る")
	void findAll_returnsAllComments() throws Exception {
		when(commentService.findAllByPostId(1L))
				.thenReturn(List.of(new CommentResponse(1L, "tsubo", "hello", LocalDateTime.now())));

		mockMvc.perform(get("/posts/1/comments"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(1))
				.andExpect(jsonPath("$[0].author").value("tsubo"));
	}

	@Test
	@DisplayName("存在しないpostIdのコメント一覧を取得すると、GlobalExceptionHandlerによって404が返る")
	void findAll_returns404WhenPostNotFound() throws Exception {
		when(commentService.findAllByPostId(999L)).thenThrow(new PostNotFoundException(999L));

		mockMvc.perform(get("/posts/999/comments"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value("Post not found: id=999"));
	}

	@Test
	@DisplayName("存在するcommentIdを指定して削除すると、200が返りServiceのdelete()が呼ばれる")
	void delete_returns200AndCallsServiceWhenExists() throws Exception {
		mockMvc.perform(delete("/posts/1/comments/1"))
				.andExpect(status().isOk());

		verify(commentService).delete(1L, 1L);
	}

	@Test
	@DisplayName("存在しないcommentIdを指定して削除すると、GlobalExceptionHandlerによって404が返る")
	void delete_returns404WhenNotFound() throws Exception {
		doThrow(new CommentNotFoundException(999L)).when(commentService).delete(1L, 999L);

		mockMvc.perform(delete("/posts/1/comments/999"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value("Comment not found: id=999"));
	}

}
