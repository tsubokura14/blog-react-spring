package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

	// メソッド名(findBy + Post + Id)から、Spring Data JPAが
	// 「CommentのpostフィールドのidがpostIdと一致するもの」というクエリを自動生成する。
	// SQLに直すと WHERE post_id = ? に相当する
	List<Comment> findByPostId(Long postId);

}