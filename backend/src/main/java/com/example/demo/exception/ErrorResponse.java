package com.example.demo.exception;

import java.time.LocalDateTime;

// エラー発生時にクライアントへ返すレスポンスボディ用DTO。
// PostResponse等と同じく、Jacksonがgetterを見てJSONに変換する
public class ErrorResponse {

	private final LocalDateTime timestamp;

	private final int status;

	private final String message;

	public ErrorResponse(int status, String message) {
		this.timestamp = LocalDateTime.now();
		this.status = status;
		this.message = message;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public int getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

}