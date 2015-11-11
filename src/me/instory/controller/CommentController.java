package me.instory.controller;

import java.util.List;

import me.instory.bean.ErrorResponse;
import me.instory.model.Comment;
import me.instory.service.CommentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.util.JSON;

@RestController
@RequestMapping(value = "/comment")
public class CommentController {
	private CommentService commentService;

	@Autowired
	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}

	@RequestMapping(value = "/story/{id}", method = RequestMethod.GET)
	public List<Comment> getStoryComments(@PathVariable("id") long id) {
		return commentService.getStoryComments(id);
	}

	@RequestMapping(value = "/post/{id}", method = RequestMethod.GET)
	public List<Comment> getPostComments(@PathVariable("id") long id) {
		return commentService.getPostComments(id);
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<?> add(@RequestBody Comment comment) {
		try {
			long id = commentService.add(comment);	
			return new ResponseEntity<>(JSON.parse("{\"comment_id\":\"" + id + "\"}"), HttpStatus.OK);
		}
		catch (Exception ex) {
			return new ResponseEntity<ErrorResponse>(new ErrorResponse(
					ex.getMessage()), HttpStatus.BAD_REQUEST);			
		}
	}
	
	@RequestMapping(value = "/{id}/remove", method = RequestMethod.POST)
	public ResponseEntity<?> remove(@PathVariable("id") long id) {
		try {
			commentService.remove(id);
			return new ResponseEntity<>(HttpStatus.OK);	
		}
		catch (Exception ex) {
			return new ResponseEntity<ErrorResponse>(new ErrorResponse(
					ex.getMessage()), HttpStatus.BAD_REQUEST);			
		}	
	}
}
