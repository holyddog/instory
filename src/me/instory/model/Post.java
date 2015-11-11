package me.instory.model;

import java.util.List;

import me.instory.bean.Picture;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "posts")
public class Post {
	@Id
	private Long id;
	@Indexed
	@Field("au")
	private User author;
	@Indexed
	@Field("st")
	private Story story;
	@Field("txt")
	private String text;
	@Field("pics")
	private List<Picture> pictures;
	@Field("lc")
	private Integer likes_count = 0;
	@Field("cc")
	private Integer comments_count = 0;
	@Field("cdt")
	private Long create_date;
	@Field("ls")
	private List<Integer> likes;

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

	public Story getStory() {
		return story;
	}

	public void setStory(Story story) {
		this.story = story;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<Picture> getPictures() {
		return pictures;
	}

	public void setPictures(List<Picture> pictures) {
		this.pictures = pictures;
	}

	public Long getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Long create_date) {
		this.create_date = create_date;
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
}
