package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.CommentCreateRequest;
import com.example.demo.dto.CommentResponse;
import com.example.demo.entity.Comment;
import com.example.demo.entity.Post;
import com.example.demo.exception.PostNotFoundException;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;

@Service
public class CommentService {

	private final CommentRepository commentRepository;

	// コメントは必ずどこかのPostに属するため、
	// 「指定されたpostIdのPostが実在するか」を確認する目的でPostRepositoryも直接使う
	private final PostRepository postRepository;

	public CommentService(CommentRepository commentRepository, PostRepository postRepository) {
		this.commentRepository = commentRepository;
		this.postRepository = postRepository;
	}

	// 投稿へのコメントを1件作成する処理
	// Post実在確認(SELECT)とコメント保存(INSERT)の2つのDB操作を1つのトランザクションにまとめるため@Transactionalを付与
	@Transactional
	public CommentResponse create(Long postId, CommentCreateRequest request) {
		// 存在しないpostIdへのコメント作成を防ぐため、先にPostの実在確認をする。
		// 見つかった場合、そのPostをCommentのpostフィールドにそのまま紐づける
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new PostNotFoundException(postId));

		Comment comment = new Comment();
		comment.setPost(post);
		comment.setAuthor(request.getAuthor());
		comment.setBody(request.getBody());
		comment.setCreatedAt(LocalDateTime.now());

		Comment saved = commentRepository.save(comment);
		return CommentResponse.from(saved);
	}

	// 指定した投稿に紐づくコメント一覧を取得する処理
	public List<CommentResponse> findAllByPostId(Long postId) {
		// 存在しないpostIdを指定された場合は404にしたいので、
		// 空リストを黙って返すのではなく明示的にPostNotFoundExceptionを投げる
		if (!postRepository.existsById(postId)) {
			throw new PostNotFoundException(postId);
		}

		return commentRepository.findByPostId(postId).stream()
				.map(CommentResponse::from)
				.toList();
	}

}
