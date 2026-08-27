package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// 投稿作成APIの入力(リクエストボディ)用DTO。
// POST /posts のリクエストJSON({"title": "...", "body": "..."})を
// そのまま受け取るための入れ物として使われる。
// PUT /posts/{id}(更新)でも同じ形(title/body)を受け取るため、そのまま使い回している
//
// クライアントから受け取ってよい項目(title, body)だけを持たせ、
// entityをそのままリクエストの受け皿にしないことで、
// 意図しない項目(idなど)を外部から書き換えられないようにしている
//
// @NotBlank/@Sizeはフィールドに値を入れるだけでは何も起きず、
// Controller側で@Validを付けたときに初めてSpringがこれらの制約を検証する
public class PostCreateRequest {

	@NotBlank(message = "title is required")
	@Size(max = 200, message = "title must be 200 characters or fewer")
	private String title;

	@NotBlank(message = "body is required")
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
