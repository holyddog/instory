package me.instory.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import me.instory.bean.ErrorResponse;
import me.instory.model.User;
import me.instory.service.UserService;
import me.instory.util.FileManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/user")
public class UserController extends _BaseController {
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/me", method = RequestMethod.GET)
	public ResponseEntity<?> me() {
		return new ResponseEntity<>(userService.getInfo(getAuthenUserId()), HttpStatus.OK);
	}

	@RequestMapping(value = "/{userId}", method = RequestMethod.GET)
	public ResponseEntity<?> getUser(@PathVariable("userId") long userId) {
		User user = userService.getInfo(userId);
		if (user != null) {
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("user", user);
			data.put("following", userService.isFollow(getAuthenUserId(), userId));	
			return new ResponseEntity<>(data, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<ErrorResponse>(new ErrorResponse(
					"User not found"), HttpStatus.OK);
			
		}
	}
	
	@RequestMapping(value = "/picture", method = RequestMethod.POST)
	public ResponseEntity<?> changePicture(@RequestParam("file") MultipartFile[] files) throws IOException {
		long userId = getAuthenUserId();
		
		MultipartFile f = files[0];	
		String path = FileManager.uploadImage("u" + userId + "/_", f.getBytes(), f.getOriginalFilename(), FileManager.ImageType.CROP);
		userService.changePicture(userId, path);
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("path", path);
		return new ResponseEntity<>(data, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{who}/follow", method = RequestMethod.POST)
	public ResponseEntity<?> follow(@PathVariable long who) {
		long userId = getAuthenUserId();
		if (who == userId) {
			return new ResponseEntity<ErrorResponse>(new ErrorResponse(
					"Cannot follow your self"), HttpStatus.OK);
			
		}
		
		userService.follow(userId, who);	
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{who}/unfollow", method = RequestMethod.POST)
	public ResponseEntity<?> unfollow(@PathVariable long who) {
		userService.unfollow(getAuthenUserId(), who);	
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
