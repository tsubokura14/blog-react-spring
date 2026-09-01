package com.example.demo.dto;

import java.util.List;

import com.example.demo.entity.Post;

// 投稿取得APIの出力(レスポンスボディ)用DTO。
// entityをAPIレスポンスにそのまま使わず、返してよい項目だけをここで選んで詰め替える
// (将来Postに内部管理用のフィールドが増えても、レスポンスに漏れ出さないようにするため)
public class PostResponse {

	private Long id;

	private String title;

	private String body;

	private int commentCount;

	private List<TagResponse> tags;

	// id・title・body・commentCount・tagsの5つの値だけを受け取って組み立てるコンストラクタ。
	// このクラスは「表示・返却専用」なので、Postのように後から値を書き換えるsetterは用意していない
	public PostResponse(Long id, String title, String body, int commentCount, List<TagResponse> tags) {
		this.id = id;
		this.title = title;
		this.body = body;
		this.commentCount = commentCount;
		this.tags = tags;
	}

	// entity → DTOへの変換ロジックをここに閉じ込めておくことで、
	// service側で毎回変換処理を書かずに済むようにしている。
	// 呼び出し側は PostResponse.from(post) と書くだけで、
	// Postエンティティの中身(id/title/body)を取り出してPostResponseを組み立ててくれる。
	//
	// post.getComments().size() / post.getTags() は、遅延ロード(LAZY)のコレクションに
	// 初めてアクセスする瞬間。この行が、Post 1件ごとに追加のSELECTを発行させる
	// (=N+1問題)の引き金になる(tags側は現時点で対策していない)
	public static PostResponse from(Post post) {
		List<TagResponse> tags = post.getTags().stream()
				.map(TagResponse::from)
				.toList();
		return new PostResponse(post.getId(), post.getTitle(), post.getBody(), post.getComments().size(), tags);
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

	public List<TagResponse> getTags() {
		return tags;
	}

}
