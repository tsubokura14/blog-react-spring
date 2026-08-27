package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

// @Entityを付けることで、このクラスがJPAの管理対象(=DBのテーブルと対応するクラス)になる。
// クラス名"Post"がそのままテーブル名"post"に対応し、
// 各フィールドがテーブルの各カラムに対応する
@Entity
public class Post {

	// idはDBの主キーに対応する。
	// @Idは主キーであることを示し、
	// @GeneratedValue(strategy = IDENTITY)は「値を自分でセットせず、
	// DB側(PostgreSQLのSERIAL/IDENTITY列)に採番を任せる」という設定
	// → 新規作成時はidをnullのままにしておけば、保存後にDBが払い出した値が自動で入る
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// 投稿のタイトル
	private String title;

	// 投稿の本文
	private String body;

	// JPAはgetter/setter経由でフィールドを読み書きするため用意している
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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
