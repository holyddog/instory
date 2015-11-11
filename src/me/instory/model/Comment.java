package me.instory.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "comments")
public class Comment {
	@Id
	private Long id;
	@Field("au")
	private User author;
	@Indexed
	@Field("st")
	private Story story;
	@Indexed
	@Field("pt")
	private Story post;
	@Field("txt")
	private String text;
	@Field("cdt")
	private Long create_date;

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

	public Story getPost() {
		return post;
	}

	public void setPost(Story post) {
		this.post = post;
	}

	public Long getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Long create_date) {
		this.create_date = create_date;
	}
}
