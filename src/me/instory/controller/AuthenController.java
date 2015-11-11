package me.instory.controller;

import me.instory.bean.ErrorResponse;
import me.instory.model.User;
import me.instory.service.AuthenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/authen")
public class AuthenController {
	@Autowired
	private AuthenService authenService;

	@RequestMapping(value = "/signIn", method = RequestMethod.POST)
	public ResponseEntity<?> signIn(@RequestBody User user) {
		User u = authenService.signIn(user.getUsername(), user.getPassword());
		if (u != null) {
			return new ResponseEntity<User>(u, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<ErrorResponse>(new ErrorResponse(
					"Invalid username/email or password"), HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/signUp", method = RequestMethod.POST)
	public ResponseEntity<?> signUp(@RequestBody User user) {
		if (authenService.isDuplicateEmail(user.getEmail())) {
			return new ResponseEntity<ErrorResponse>(new ErrorResponse(
					"This email is already registered"), HttpStatus.OK);
		}
		if (authenService.isDuplicateUsername(user.getUsername())) {
			return new ResponseEntity<ErrorResponse>(new ErrorResponse(
					"This username is already taken"), HttpStatus.OK);
		}
		
		authenService.signUp(user);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
