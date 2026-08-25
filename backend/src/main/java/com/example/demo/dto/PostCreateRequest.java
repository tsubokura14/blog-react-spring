package com.example.demo.dto;

// 投稿作成APIの入力(リクエストボディ)用DTO。
// POST /posts のリクエストJSON({"title": "...", "body": "..."})を
// そのまま受け取るための入れ物として使われる
//
// クライアントから受け取ってよい項目(title, body)だけを持たせ、
// entityをそのままリクエストの受け皿にしないことで、
// 意図しない項目(idなど)を外部から書き換えられないようにしている
public class PostCreateRequest {

	private String title;

	private String body;

	// JacksonがJSONのキーとこのgetter/setterの名前(title/body)を対応付けて、
	// JSON文字列 → PostCreateRequestインスタンスへの変換に使う
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

}
