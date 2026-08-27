package com.example.demo.dto;

import com.example.demo.entity.Post;

// 投稿取得APIの出力(レスポンスボディ)用DTO。
// entityをAPIレスポンスにそのまま使わず、返してよい項目だけをここで選んで詰め替える
// (将来Postに内部管理用のフィールドが増えても、レスポンスに漏れ出さないようにするため)
public class PostResponse {

	private Long id;

	private String title;

	private String body;

	// id・title・bodyの3つの値だけを受け取って組み立てるコンストラクタ。
	// このクラスは「表示・返却専用」なので、Postのように後から値を書き換えるsetterは用意していない
	public PostResponse(Long id, String title, String body) {
		this.id = id;
		this.title = title;
		this.body = body;
	}

	// entity → DTOへの変換ロジックをここに閉じ込めておくことで、
	// service側で毎回変換処理を書かずに済むようにしている。
	// 呼び出し側は PostResponse.from(post) と書くだけで、
	// Postエンティティの中身(id/title/body)を取り出してPostResponseを組み立ててくれる
	public static PostResponse from(Post post) {
		return new PostResponse(post.getId(), post.getTitle(), post.getBody());
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

}
