package com.example.demo.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import com.example.demo.entity.Post;

// JpaRepository<Post, Long>を継承するだけで、
// save() / findAll() / findById() / deleteById() などのCRUDメソッドを
// 自前で実装しなくても使えるようになる(Spring Data JPAが実行時にプロキシ実装を自動生成する)。
//
// <Post, Long> の意味:
//   Post ... このリポジトリが扱うentityの型
//   Long ... そのentityの主キー(id)の型
//
// メソッドの中身を1行も書いていないのに動くのは、
// Spring Data JPAが「メソッド名やインターフェースの型情報」から
// 実行すべきSQL/JPA操作を推測して自動生成しているため
public interface PostRepository extends JpaRepository<Post, Long> {

	// JpaRepositoryが元々持つfindAll()を、同じシグネチャのまま上書きしている。
	// @EntityGraph(attributePaths = "comments")を付けると、
	// postを取得するSELECTの時点でcommentsもLEFT JOIN FETCHするようになり、
	// 「後からcomments.size()を呼んで追加SELECTが飛ぶ」というN+1の原因を1回のSQLに潰せる
	@Override
	@EntityGraph(attributePaths = "comments")
	List<Post> findAll();

}
