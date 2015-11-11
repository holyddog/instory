package me.instory.service;

import me.instory.model.User;

public interface AuthenService {
	boolean isDuplicateEmail(String email);
	boolean isDuplicateUsername(String username);
	void signUp(User user);
	User signIn(String loginName, String password);
}
