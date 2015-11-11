package me.instory.service;

import java.util.List;

import me.instory.model.Comment;

public interface CommentService {
	List<Comment> getStoryComments(long id);
	List<Comment> getPostComments(long id);
		
	long add(Comment comment);
	void remove(long id);
}
