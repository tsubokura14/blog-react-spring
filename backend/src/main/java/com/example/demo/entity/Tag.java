package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

// Post:Tag = 多対多の「多」側のentity。Tag自身はPostを持たず、
// 関連の管理(中間テーブルpost_tagの読み書き)はPost側の@ManyToManyフィールドに任せている(単方向関連)
@Entity
public class Tag {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// 同じ名前のタグが複数登録されるのを防ぐため、DB制約でユニークにする
	@Column(unique = true, nullable = false)
	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
