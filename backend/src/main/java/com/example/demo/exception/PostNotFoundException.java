package com.example.demo.exception;

// 指定されたidのPostが存在しないことを表す専用の例外。
// IllegalArgumentException(汎用的な「不正な引数」の例外)だと
// 「値が不正」なのか「対象が見つからない」なのかを型で区別できず、
// GlobalExceptionHandler側で「見つからない場合だけ404にする」という
// 狙った処理がしづらいため、意味に応じた専用の例外クラスを用意している
public class PostNotFoundException extends RuntimeException {

	public PostNotFoundException(Long id) {
		super("Post not found: id=" + id);
	}

}