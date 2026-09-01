package com.example.demo.exception;

import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// @RestControllerAdviceは「全Controllerに共通で適用される@ExceptionHandlerの集まり」を表す。
// 各Controllerが例外をtry-catchしなくても、ここで一元的に例外→HTTPレスポンスの変換ができる
@RestControllerAdvice
public class GlobalExceptionHandler {

	// PostServiceが投げるPostNotFoundExceptionをここで捕まえ、404として返す。
	// これが無かった場合、Spring Bootのデフォルト挙動では
	// 「ハンドラの無い例外」として500 Internal Server Errorになってしまう
	@ExceptionHandler(PostNotFoundException.class)
	public ResponseEntity<ErrorResponse> handlePostNotFound(PostNotFoundException ex) {
		ErrorResponse body = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
	}

	// CommentServiceが投げるCommentNotFoundExceptionをここで捕まえ、404として返す
	@ExceptionHandler(CommentNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleCommentNotFound(CommentNotFoundException ex) {
		ErrorResponse body = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
	}

	// @Valid付きの引数でBean Validation(@NotBlank等)違反があると、
	// Spring MVCがMethodArgumentNotValidExceptionを投げる。
	// ここで各フィールドの違反内容(ex.getBindingResult())を取り出し、
	// 「どの項目が」「なぜ」違反したのかをmessageとしてまとめて返す
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
		String message = ex.getBindingResult().getFieldErrors().stream()
				.map(error -> error.getField() + ": " + error.getDefaultMessage())
				.collect(Collectors.joining(", "));
		ErrorResponse body = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), message);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
	}

	// 同名タグの登録などDB制約(unique等)違反時にSpringが投げる例外。
	// DB由来の詳細なメッセージ(SQL文の一部等)をそのまま返さず、汎用的なメッセージに変換する
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
		ErrorResponse body = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "constraint violation: duplicate or invalid value");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
	}

}