package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

// コメント作成APIの入力(リクエストボディ)用DTO。
// どの投稿へのコメントかはURLパスの{postId}から分かるため、
// リクエストボディにはpostIdを含めない(PostCreateRequestと同じ考え方)
public class CommentCreateRequest {

	@NotBlank(message = "author is required")
	private String author;

	@NotBlank(message = "body is required")
	private String body;

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

}