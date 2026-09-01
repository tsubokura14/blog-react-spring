package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.TagCreateRequest;
import com.example.demo.dto.TagResponse;
import com.example.demo.entity.Tag;
import com.example.demo.repository.TagRepository;

@Service
public class TagService {

	private final TagRepository tagRepository;

	public TagService(TagRepository tagRepository) {
		this.tagRepository = tagRepository;
	}

	// タグを1件作成する処理。同名タグが既に存在する場合はDBのunique制約違反となり、
	// DataIntegrityViolationExceptionがGlobalExceptionHandlerで400に変換される
	public TagResponse create(TagCreateRequest request) {
		Tag tag = new Tag();
		tag.setName(request.getName());
		Tag saved = tagRepository.save(tag);
		return TagResponse.from(saved);
	}

	// タグの一覧を取得する処理
	public List<TagResponse> findAll() {
		return tagRepository.findAll().stream()
				.map(TagResponse::from)
				.toList();
	}

}
