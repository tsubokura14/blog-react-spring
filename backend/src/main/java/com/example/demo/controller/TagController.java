package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import com.example.demo.dto.TagCreateRequest;
import com.example.demo.dto.TagResponse;
import com.example.demo.service.TagService;

// APIを/api配下に分離している理由はPostControllerのコメントを参照
@RestController
@RequestMapping("/api/tags")
public class TagController {

	private final TagService tagService;

	public TagController(TagService tagService) {
		this.tagService = tagService;
	}

	// POST /tags
	@PostMapping
	public TagResponse create(@Valid @RequestBody TagCreateRequest request) {
		return tagService.create(request);
	}

	// GET /tags (一覧取得)
	@GetMapping
	public List<TagResponse> findAll() {
		return tagService.findAll();
	}

}
