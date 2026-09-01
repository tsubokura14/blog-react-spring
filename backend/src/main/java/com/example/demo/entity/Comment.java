package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

// Comment:Post = 多対1(1つのPostに複数のCommentがぶら下がる)を表すentity
@Entity
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// @ManyToOneのデフォルトはFetchType.EAGERだが、
	// Commentを取得するたびに紐づくPostまで毎回JOINで取得されるのを避けるため、
	// 明示的にLAZY(実際にpost.getXxx()を呼んだ時点で初めて取得)にしている
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	private Post post;

	// 投稿者名。認証実装後はUserとの関連に置き換える予定(Postのauthorと同様の扱い)
	private String author;

	private String body;

	private LocalDateTime createdAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

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

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

}