package com.example.demo.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.example.demo.entity.Post;

// @DataJpaTestは永続化層(Repository/Entity)だけを起動するテストスライス。
// PostControllerTestの@WebMvcTestがWeb層だけを起動したのと対になる存在で、
// こちらはController/Serviceを一切起動せず、PostRepositoryが実際にDBへ
// 保存・取得できているかだけを検証する。
//
// デフォルトで組み込みDB(H2)に差し替わり、各テストは1件ずつ独立したトランザクションで実行され、
// テスト終了時に自動でロールバックされる(テスト同士がDBの状態を汚し合わない)
@DataJpaTest
class PostRepositoryTest {

	@Autowired
	private PostRepository postRepository;

	@Test
	@DisplayName("保存すると、DBが採番したidが振られた状態でPostが返る")
	void save_assignsGeneratedId() {
		Post post = new Post();
		post.setTitle("test");
		post.setBody("hello");

		// 保存前はidを自分でセットしていない(GenerationType.IDENTITYのため)
		Post saved = postRepository.save(post);

		assertThat(saved.getId()).isNotNull();
		assertThat(saved.getTitle()).isEqualTo("test");
		assertThat(saved.getBody()).isEqualTo("hello");
	}

	@Test
	@DisplayName("保存済みのidでfindByIdすると、同じ内容のPostが取得できる")
	void findById_returnsPostWhenExists() {
		Post post = new Post();
		post.setTitle("test");
		post.setBody("hello");
		Post saved = postRepository.save(post);

		Optional<Post> found = postRepository.findById(saved.getId());

		assertThat(found).isPresent();
		assertThat(found.get().getTitle()).isEqualTo("test");
		assertThat(found.get().getBody()).isEqualTo("hello");
	}

	@Test
	@DisplayName("存在しないidでfindByIdすると、空のOptionalが返る")
	void findById_returnsEmptyWhenNotExists() {
		Optional<Post> found = postRepository.findById(999L);

		assertThat(found).isEmpty();
	}

	@Test
	@DisplayName("findAllすると、保存した件数分のPostが返る")
	void findAll_returnsAllSavedPosts() {
		Post post1 = new Post();
		post1.setTitle("first");
		post1.setBody("first body");
		Post post2 = new Post();
		post2.setTitle("second");
		post2.setBody("second body");

		postRepository.save(post1);
		postRepository.save(post2);

		List<Post> all = postRepository.findAll();

		assertThat(all).hasSize(2);
	}

	@Test
	@DisplayName("deleteByIdすると、以降existsByIdがfalseになる")
	void deleteById_removesPost() {
		Post post = new Post();
		post.setTitle("test");
		post.setBody("hello");
		Post saved = postRepository.save(post);

		postRepository.deleteById(saved.getId());

		assertThat(postRepository.existsById(saved.getId())).isFalse();
	}

}