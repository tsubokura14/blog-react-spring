package com.example.demo.exception;

// 指定されたidのCommentが存在しない、または指定されたPostに属していないことを表す専用の例外
public class CommentNotFoundException extends RuntimeException {

	public CommentNotFoundException(Long id) {
		super("Comment not found: id=" + id);
	}

}
