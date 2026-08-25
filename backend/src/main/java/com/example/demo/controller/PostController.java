package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.PostCreateRequest;
import com.example.demo.dto.PostResponse;
import com.example.demo.service.PostService;

// @RestControllerは@Controller + @ResponseBodyを兼ねており、
// 戻り値を自動的にJSONへ変換してレスポンスボディに書き込む
// @RequestMapping("/posts")でこのクラス配下のURLのベースパスを指定している
@RestController
@RequestMapping("/posts")
public class PostController {

	private final PostService postService;

	public PostController(PostService postService) {
		this.postService = postService;
	}

	// POST /posts
	// リクエストボディのJSONを受け取り、投稿を1件作成してそのまま結果を返すだけのメソッド。
	// バリデーションやDB保存の中身はPostService側の責務なので、ここでは処理を委譲するだけにしている
	//
	// @RequestBodyでリクエストのJSONをPostCreateRequestにバインドする
	@PostMapping
	public PostResponse create(@RequestBody PostCreateRequest request) {
		return postService.create(request);
	}

	// GET /posts (一覧取得)
	// 現時点では絞り込みやページングは無く、常に全件を返す
	@GetMapping
	public List<PostResponse> findAll() {
		return postService.findAll();
	}

	// GET /posts/{id} (単体取得)
	// URLに含まれるidを使って1件だけ取得する。該当が無い場合の挙動はPostService側に任せている
	//
	// @PathVariableでURLパスの{id}部分を引数として受け取る
	@GetMapping("/{id}")
	public PostResponse findById(@PathVariable Long id) {
		return postService.findById(id);
	}

}
