package me.instory.service;

import me.instory.model.User;

public interface UserService {
	User getInfo(long userId);
	void changePicture(long userId, String picture);
	void follow(long userId, long who);
	void unfollow(long userId, long who);
	boolean isFollow(long userId, long who);
}
