package me.instory.service;

import java.util.List;

import me.instory.bean.Picture;
import me.instory.model.Post;
import me.instory.model.Story;
import me.instory.model.Tag;

public interface StoryService {
	List<Story> findStories(long userId);
	long addStory(long userId, Story story);
	void editStory(long storyId, long userId, Story story);
	void deleteStory(long storyId, long userId);
	
//	List<Story> findAll();
//	List<Story> findFollowStories(long id);
//	Story getStory(long id);
//	
//	void setCover(long id, String cover);
//	
//	long addPost(Post post);
//	void editPost(long id, Post post);
//	void removePost(long id);
//	long addPostPicture(long id, Picture pic);	
//	
//	List<Tag> findTags(String tag);
//	void addTag(long id, String tag);
//	void removeTag(long id, String tag);
//
//	void likeStory(long id, Story story);
//	void unlikeStory(long id, Story story);
//
//	void likePost(long id, Post post);
//	void unlikePost(long id, Post post);
}
