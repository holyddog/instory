package me.instory.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "users")
public class User implements Serializable {
	private static final long serialVersionUID = -7575332924034737202L;
	
	public User() { }
	
	@Id
	private Long id;
	@Field("un")
	@Indexed(unique = true)
	private String username;
	@Field("em")
	@Indexed(unique = true)
	private String email;
	@Field("dn")
	private String display_name;
	@Field("pwd")
	private String password;
	@Field("pic")
	private String picture;

	@Field("rdt")
	private Long regis_date;
	@Field("stc")
	private Integer stories_count;
	@Field("fwc")
	private Integer followers_count;
	@Field("fic")
	private Integer following_count;
	@Field("iat")
	private Boolean inactive;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDisplay_name() {
		return display_name;
	}

	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public Long getRegis_date() {
		return regis_date;
	}

	public void setRegis_date(Long regis_date) {
		this.regis_date = regis_date;
	}

	public Boolean getInactive() {
		return inactive;
	}

	public void setInactive(Boolean inactive) {
		this.inactive = inactive;
	}

	public Integer getFollowers_count() {
		return followers_count;
	}

	public void setFollowers_count(Integer followers_count) {
		this.followers_count = followers_count;
	}

	public Integer getFollowing_count() {
		return following_count;
	}

	public void setFollowing_count(Integer following_count) {
		this.following_count = following_count;
	}

	public Integer getStories_count() {
		return stories_count;
	}

	public void setStories_count(Integer stories_count) {
		this.stories_count = stories_count;
	}
}
