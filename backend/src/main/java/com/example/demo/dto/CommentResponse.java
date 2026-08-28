package com.example.demo.dto;

import java.time.LocalDateTime;

import com.example.demo.entity.Comment;

// コメント取得APIの出力用DTO。PostResponseと同じく、
// entityをそのまま返さずgetterのみの出力専用DTOに詰め替える
public class CommentResponse {

	private Long id;

	private String author;

	private String body;

	private LocalDateTime createdAt;

	public CommentResponse(Long id, String author, String body, LocalDateTime createdAt) {
		this.id = id;
		this.author = author;
		this.body = body;
		this.createdAt = createdAt;
	}

	public static CommentResponse from(Comment comment) {
		return new CommentResponse(comment.getId(), comment.getAuthor(), comment.getBody(), comment.getCreatedAt());
	}

	public Long getId() {
		return id;
	}

	public String getAuthor() {
		return author;
	}

	public String getBody() {
		return body;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

}