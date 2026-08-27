package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.PostCreateRequest;
import com.example.demo.dto.PostResponse;
import com.example.demo.entity.Post;
import com.example.demo.exception.PostNotFoundException;
import com.example.demo.repository.PostRepository;

// @Serviceを付けることでDIコンテナに登録され、
// ControllerからPostServiceをコンストラクタ引数として受け取れるようになる
@Service
public class PostService {

	// コンストラクタインジェクション
	// (フィールドに直接@Autowiredするより、不変(final)にできる・テストで差し替えやすいという利点がある)
	private final PostRepository postRepository;

	public PostService(PostRepository postRepository) {
		this.postRepository = postRepository;
	}

	// 投稿を1件作成する処理
	// 「リクエストDTO(入力)」→「entity(DB保存用の形)」→「保存」→「レスポンスDTO(出力)」という
	// 3段階の詰め替えを行っているのがこのメソッドの役割
	public PostResponse create(PostCreateRequest request) {
		// (1) entityを新規に組み立てる。
		// この時点ではまだDBには何も反映されておらず、idもnullのただのJavaオブジェクト
		Post post = new Post();

		// (2) リクエストDTOに入っている値(クライアントが送ってきたtitle/body)を
		// entityのフィールドにコピーしている
		post.setTitle(request.getTitle());
		post.setBody(request.getBody());

		// (3) repository.save()を呼んだ瞬間にDBへのINSERTが発行される。
		// 戻り値の saved は、DBが払い出したid(自動採番)が入った状態のPostになっている
		Post saved = postRepository.save(post);

		// (4) DB保存後のentityを、レスポンス用のDTOに変換して返す
		// (Controllerにはentityではなくこの変換後のDTOだけを渡す)
		return PostResponse.from(saved);
	}

	// 投稿の一覧を取得する処理
	public List<PostResponse> findAll() {
		// postRepository.findAll() で全件のPostエンティティ(List<Post>)を取得し、
		// stream().map(...) で1件ずつ PostResponse.from() に通してDTOへ変換している
		// (Controllerに返すのはentityのリストではなく、DTOのリストにするため)
		return postRepository.findAll().stream()
				.map(PostResponse::from)
				.toList();
	}

	// idを指定して投稿を1件取得する処理
	public PostResponse findById(Long id) {
		// findById()の戻り値はOptional<Post>(値があるかどうか分からないことを表す型)。
		// 値が存在すればその中身のPostを取り出し、
		// 存在しなければorElseThrow()で例外を投げて処理を中断する
		//
		// 見つからない場合はPostNotFoundExceptionを投げる。
		// GlobalExceptionHandlerがこれを捕まえて404に変換する
		Post post = postRepository.findById(id)
				.orElseThrow(() -> new PostNotFoundException(id));

		// 見つかったentityをDTOに変換して返す
		return PostResponse.from(post);
	}

	// 投稿を1件更新する処理
	public PostResponse update(Long id, PostCreateRequest request) {
		// 更新対象が存在しなければPostNotFoundException(findByIdと同様)
		Post post = postRepository.findById(id)
				.orElseThrow(() -> new PostNotFoundException(id));

		// findByIdの戻り値はこのメソッドを抜けると永続化コンテキストの外に出る(detached)ため、
		// フィールドを書き換えるだけでは自動的にUPDATEされない。
		// 明示的にsave()を呼ぶことで、idが既存なのでINSERTではなくUPDATEが発行される
		post.setTitle(request.getTitle());
		post.setBody(request.getBody());
		Post updated = postRepository.save(post);

		return PostResponse.from(updated);
	}

	// 投稿を1件削除する処理
	public void delete(Long id) {
		// deleteById()は対象が存在しなくても例外を投げないため、
		// 事前にexistsById()で存在確認をしてから削除している
		if (!postRepository.existsById(id)) {
			throw new PostNotFoundException(id);
		}
		postRepository.deleteById(id);
	}

}
