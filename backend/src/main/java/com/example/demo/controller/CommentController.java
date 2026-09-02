package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import com.example.demo.dto.CommentCreateRequest;
import com.example.demo.dto.CommentResponse;
import com.example.demo.service.CommentService;

// "/api/posts/{postId}/comments" というURLで、
// 「どの投稿に対するコメントか」をパスの時点で明確にしている。
// APIを/api配下に分離している理由はPostControllerのコメントを参照
@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

	private final CommentService commentService;

	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}

	// POST /posts/{postId}/comments
	@PostMapping
	public CommentResponse create(@PathVariable Long postId, @Valid @RequestBody CommentCreateRequest request) {
		return commentService.create(postId, request);
	}

	// GET /posts/{postId}/comments (一覧取得)
	@GetMapping
	public List<CommentResponse> findAll(@PathVariable Long postId) {
		return commentService.findAllByPostId(postId);
	}

	// DELETE /posts/{postId}/comments/{commentId}
	@DeleteMapping("/{commentId}")
	public void delete(@PathVariable Long postId, @PathVariable Long commentId) {
		commentService.delete(postId, commentId);
	}

}
