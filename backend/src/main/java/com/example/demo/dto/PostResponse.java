package com.example.demo.dto;

import com.example.demo.entity.Post;

// 投稿取得APIの出力(レスポンスボディ)用DTO。
// entityをAPIレスポンスにそのまま使わず、返してよい項目だけをここで選んで詰め替える
// (将来Postに内部管理用のフィールドが増えても、レスポンスに漏れ出さないようにするため)
public class PostResponse {

	private Long id;

	private String title;

	private String body;

	private int commentCount;

	// id・title・body・commentCountの4つの値だけを受け取って組み立てるコンストラクタ。
	// このクラスは「表示・返却専用」なので、Postのように後から値を書き換えるsetterは用意していない
	public PostResponse(Long id, String title, String body, int commentCount) {
		this.id = id;
		this.title = title;
		this.body = body;
		this.commentCount = commentCount;
	}

	// entity → DTOへの変換ロジックをここに閉じ込めておくことで、
	// service側で毎回変換処理を書かずに済むようにしている。
	// 呼び出し側は PostResponse.from(post) と書くだけで、
	// Postエンティティの中身(id/title/body)を取り出してPostResponseを組み立ててくれる。
	//
	// post.getComments().size() は、遅延ロード(LAZY)のcommentsコレクションに
	// 初めてアクセスする瞬間。この1行が、Post 1件ごとに追加のSELECTを発行させる
	// (=N+1問題)の引き金になる
	public static PostResponse from(Post post) {
		return new PostResponse(post.getId(), post.getTitle(), post.getBody(), post.getComments().size());
	}

	// JacksonがこのgetterをJSONのキー(id/title/body)に変換して出力する
	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getBody() {
		return body;
	}

	public int getCommentCount() {
		return commentCount;
	}

}
