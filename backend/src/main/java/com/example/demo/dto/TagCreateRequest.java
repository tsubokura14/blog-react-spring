package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

// タグ作成APIの入力(リクエストボディ)用DTO
public class TagCreateRequest {

	@NotBlank(message = "name is required")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
