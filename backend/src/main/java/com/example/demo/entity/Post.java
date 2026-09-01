package com.example.demo.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

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

	// mappedBy = "post" は、外部キー(post_id)の管理を
	// Comment側の@ManyToOne(post)フィールドに任せている、という意味。
	// @OneToManyのデフォルトのfetchはLAZYなので明示していないが、
	// これがN+1問題を引き起こす原因になる(comments.size()等を呼んだ瞬間に
	// このPost 1件分だけのSELECTが追加で発行される)
	@OneToMany(mappedBy = "post")
	private List<Comment> comments = new ArrayList<>();

	// Post:Tag = 多対多。中間テーブルpost_tagの管理をPost側(このフィールド)に持たせている単方向関連
	// (Tag側からPostを辿る必要が無いため、Tag側にmappedByの@ManyToManyは用意していない)
	@ManyToMany
	@JoinTable(
			name = "post_tag",
			joinColumns = @JoinColumn(name = "post_id"),
			inverseJoinColumns = @JoinColumn(name = "tag_id")
	)
	private Set<Tag> tags = new HashSet<>();

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

	public List<Comment> getComments() {
		return comments;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

}
