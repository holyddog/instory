package me.instory.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "stories")
public class Story {
	@Id
	private Long id;
	@Field("au")
	private User author;
	@Field("ttl")
	private String title;
	private String desc;
	@Field("cv")
	private String cover;
	@Field("tgs")
	private String[] tags;
	@Field("lc")
	private Integer likes_count = 0;
	@Field("cc")
	private Integer comments_count = 0;
	@Field("cdt")
	private Long create_date;
	@Field("pts")
	private List<Post> posts;
	@Field("ls")
	private List<Integer> likes;
	private Boolean liked;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public Long getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Long create_date) {
		this.create_date = create_date;
	}

	public String[] getTags() {
		return tags;
	}

	public void setTags(String[] tags) {
		this.tags = tags;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	public Integer getLikes_count() {
		return likes_count;
	}

	public void setLikes_count(Integer likes_count) {
		this.likes_count = likes_count;
	}

	public Integer getComments_count() {
		return comments_count;
	}

	public void setComments_count(Integer comments_count) {
		this.comments_count = comments_count;
	}

	public List<Integer> getLikes() {
		return likes;
	}

	public void setLikes(List<Integer> likes) {
		this.likes = likes;
	}

	public Boolean getLiked() {
		return liked;
	}

	public void setLiked(Boolean liked) {
		this.liked = liked;
	}
}
