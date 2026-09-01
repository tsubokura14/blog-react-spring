package com.example.demo.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.example.demo.entity.Comment;
import com.example.demo.entity.Post;

// PostRepositoryTestと同じく永続化層だけを起動し、findByPostId()が
// 「指定したPostに紐づくCommentだけ」を返すことを検証する
@DataJpaTest
class CommentRepositoryTest {

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private PostRepository postRepository;

	@Test
	@DisplayName("findByPostIdすると、指定したPostに紐づくCommentだけが返る")
	void findByPostId_returnsOnlyMatchingComments() {
		Post post1 = new Post();
		post1.setTitle("post1");
		post1.setBody("body1");
		Post savedPost1 = postRepository.save(post1);

		Post post2 = new Post();
		post2.setTitle("post2");
		post2.setBody("body2");
		Post savedPost2 = postRepository.save(post2);

		Comment commentForPost1 = new Comment();
		commentForPost1.setPost(savedPost1);
		commentForPost1.setAuthor("tsubo");
		commentForPost1.setBody("comment for post1");
		commentForPost1.setCreatedAt(LocalDateTime.now());
		commentRepository.save(commentForPost1);

		Comment commentForPost2 = new Comment();
		commentForPost2.setPost(savedPost2);
		commentForPost2.setAuthor("tsubo");
		commentForPost2.setBody("comment for post2");
		commentForPost2.setCreatedAt(LocalDateTime.now());
		commentRepository.save(commentForPost2);

		List<Comment> found = commentRepository.findByPostId(savedPost1.getId());

		assertThat(found).hasSize(1);
		assertThat(found.get(0).getBody()).isEqualTo("comment for post1");
	}

	@Test
	@DisplayName("紐づくCommentが無いPostを指定すると、空リストが返る")
	void findByPostId_returnsEmptyWhenNoComments() {
		Post post = new Post();
		post.setTitle("post");
		post.setBody("body");
		Post saved = postRepository.save(post);

		List<Comment> found = commentRepository.findByPostId(saved.getId());

		assertThat(found).isEmpty();
	}

}
