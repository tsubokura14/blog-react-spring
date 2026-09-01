package com.example.demo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.dto.TagCreateRequest;
import com.example.demo.dto.TagResponse;
import com.example.demo.service.TagService;

import tools.jackson.databind.ObjectMapper;

// PostControllerTestと同じ考え方で、TagServiceをモック化してWeb層だけを検証する
@WebMvcTest(TagController.class)
class TagControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private TagService tagService;

	@Test
	@DisplayName("nameが入った正常なリクエストなら、作成されたタグが200で返る")
	void create_returnsCreatedTagWhenValid() throws Exception {
		TagCreateRequest request = new TagCreateRequest();
		request.setName("tech");

		when(tagService.create(any(TagCreateRequest.class))).thenReturn(new TagResponse(1L, "tech"));

		mockMvc.perform(post("/tags")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.name").value("tech"));
	}

	@Test
	@DisplayName("nameが空のリクエストは、@NotBlankの検証に引っかかり400とメッセージが返る")
	void create_returns400WhenNameIsBlank() throws Exception {
		TagCreateRequest request = new TagCreateRequest();
		request.setName("");

		mockMvc.perform(post("/tags")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("name: name is required"));
	}

	@Test
	@DisplayName("既存の名前と重複するリクエストは、GlobalExceptionHandlerによって400が返る")
	void create_returns400WhenNameAlreadyExists() throws Exception {
		TagCreateRequest request = new TagCreateRequest();
		request.setName("tech");

		when(tagService.create(any(TagCreateRequest.class)))
				.thenThrow(new DataIntegrityViolationException("duplicate key value violates unique constraint"));

		mockMvc.perform(post("/tags")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("constraint violation: duplicate or invalid value"));
	}

	@Test
	@DisplayName("タグ一覧を取得すると、Serviceが返したTagResponseの内容がそのままJSONで返る")
	void findAll_returnsAllTags() throws Exception {
		when(tagService.findAll()).thenReturn(List.of(new TagResponse(1L, "tech"), new TagResponse(2L, "diary")));

		mockMvc.perform(get("/tags"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name").value("tech"))
				.andExpect(jsonPath("$[1].name").value("diary"));
	}

}
