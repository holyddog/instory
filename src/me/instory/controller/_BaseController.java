package me.instory.controller;

import me.instory.model.User;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class _BaseController {
	public long getAuthenUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User authUser = (User) authentication.getPrincipal();
		return authUser.getId();
	}
}
