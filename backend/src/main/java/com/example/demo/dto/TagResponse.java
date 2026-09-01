package com.example.demo.dto;

import com.example.demo.entity.Tag;

// タグ取得APIの出力用DTO
public class TagResponse {

	private Long id;

	private String name;

	public TagResponse(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public static TagResponse from(Tag tag) {
		return new TagResponse(tag.getId(), tag.getName());
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

}
