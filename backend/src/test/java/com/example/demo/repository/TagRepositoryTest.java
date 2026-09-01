package com.example.demo.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import com.example.demo.entity.Tag;

@DataJpaTest
class TagRepositoryTest {

	@Autowired
	private TagRepository tagRepository;

	@Test
	@DisplayName("保存すると、DBが採番したidが振られた状態でTagが返る")
	void save_assignsGeneratedId() {
		Tag tag = new Tag();
		tag.setName("tech");

		Tag saved = tagRepository.save(tag);

		assertThat(saved.getId()).isNotNull();
		assertThat(saved.getName()).isEqualTo("tech");
	}

	@Test
	@DisplayName("同じ名前のTagを重ねて保存しようとすると、unique制約違反で例外が発生する")
	void save_throwsWhenNameAlreadyExists() {
		Tag tag1 = new Tag();
		tag1.setName("tech");
		tagRepository.saveAndFlush(tag1);

		Tag tag2 = new Tag();
		tag2.setName("tech");

		// saveAndFlush()で即座にDBへ反映させることで、unique制約違反をこの時点で検知させる
		// (flushしないと、テストメソッドが終わるまで実際のINSERTが発行されず違反に気づけない)
		assertThatThrownBy(() -> tagRepository.saveAndFlush(tag2))
				.isInstanceOf(DataIntegrityViolationException.class);
	}

}
