package me.instory.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.instory.bean.ErrorResponse;
import me.instory.model.Post;
import me.instory.model.Story;
import me.instory.model.Tag;
import me.instory.service.StoryService;
import me.instory.util.FileManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.util.JSON;

@RestController
@RequestMapping(value = "/story")
public class StoryController extends _BaseController {
	@Autowired
	private StoryService storyService;
	
	@RequestMapping(value = "/{userId}/stories", method = RequestMethod.GET)
	public ResponseEntity<?> findMyStories(@PathVariable("userId") long userId) {
		return new ResponseEntity<>(storyService.findStories(userId), HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addStory(@RequestBody Story story) {
		long storyId = storyService.addStory(getAuthenUserId(), story);		
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("story_id", storyId);
		return new ResponseEntity<>(data, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{storyId}", method = RequestMethod.POST)
	public ResponseEntity<?> editStory(@PathVariable("storyId") long storyId, @RequestBody Story story) {
		storyService.editStory(storyId, getAuthenUserId(), story);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{storyId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteStory(@PathVariable("storyId") long storyId) {
		storyService.deleteStory(storyId, getAuthenUserId());
		return new ResponseEntity<>(HttpStatus.OK);
	}

//	@RequestMapping(value = "/explore", method = RequestMethod.GET)
//	public List<Story> findAll() {
//		return storyService.findAll();
//	}
//
//	@RequestMapping(value = "/follow/{id}", method = RequestMethod.GET)
//	public List<Story> findFollowStories(@PathVariable("id") long id) {
//		return storyService.findFollowStories(id);
//	}
//
//	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
//	public ResponseEntity<?> getStory(@PathVariable("id") long id) {
//		Story story = storyService.getStory(id);
//		if (story != null) {
//			return new ResponseEntity<Story>(story, HttpStatus.OK);
//		}
//		else {
//			return new ResponseEntity<ErrorResponse>(new ErrorResponse(
//					"Story not found"), HttpStatus.OK);
//			
//		}
//	}
//	
//	@RequestMapping(value = "/addPost", method = RequestMethod.POST)
//	public ResponseEntity<?> addPost(@RequestBody Post post) {
//		try {
//			long id = storyService.addPost(post);		
//			return new ResponseEntity<>(JSON.parse("{\"post_id\":\"" + id + "\"}"), HttpStatus.OK);
//		}
//		catch (Exception ex) {
//			return new ResponseEntity<ErrorResponse>(new ErrorResponse(
//					ex.getMessage()), HttpStatus.BAD_REQUEST);			
//		}
//
//	}
//	
//	@RequestMapping(value = "/{id}/addPicture", method = RequestMethod.POST)
//	public ResponseEntity<?> addPicture(@PathVariable("id") long id, @RequestParam("id") long userId, @RequestParam("file") MultipartFile[] files) {
//		try {
//			MultipartFile f = files[0];	
//			String path = FileManager.uploadImage("u" + userId + "/s" + id, f.getBytes(), f.getOriginalFilename(), FileManager.ImageType.RESIZE);
//			return new ResponseEntity<>(JSON.parse("{\"path\":\"" + path + "\"}"), HttpStatus.OK);
//		}
//		catch (Exception ex) {
//			return new ResponseEntity<ErrorResponse>(new ErrorResponse(
//					ex.getMessage()), HttpStatus.BAD_REQUEST);			
//		}
//
//	}
//	
//	@RequestMapping(value = "/{id}/editTitle", method = RequestMethod.POST)
//	public ResponseEntity<?> editTitle(@PathVariable("id") long id, @RequestBody Story story) {
//		try {
//			storyService.editTitle(id, story);		
//			return new ResponseEntity<>(HttpStatus.OK);	
//		}
//		catch (Exception ex) {
//			return new ResponseEntity<ErrorResponse>(new ErrorResponse(
//					ex.getMessage()), HttpStatus.BAD_REQUEST);			
//		}	
//	}
//	
//	@RequestMapping(value = "/{id}/editPost", method = RequestMethod.POST)
//	public ResponseEntity<?> editPost(@PathVariable("id") long id, @RequestBody Post post) {
//		try {
//			storyService.editPost(id, post);
//			return new ResponseEntity<>(HttpStatus.OK);	
//		}
//		catch (Exception ex) {
//			return new ResponseEntity<ErrorResponse>(new ErrorResponse(
//					ex.getMessage()), HttpStatus.BAD_REQUEST);			
//		}	
//	}
//	
//	@RequestMapping(value = "/{id}/removePost", method = RequestMethod.POST)
//	public ResponseEntity<?> removePost(@PathVariable("id") long id) {
//		try {
//			storyService.removePost(id);
//			return new ResponseEntity<>(HttpStatus.OK);	
//		}
//		catch (Exception ex) {
//			return new ResponseEntity<ErrorResponse>(new ErrorResponse(
//					ex.getMessage()), HttpStatus.BAD_REQUEST);			
//		}	
//	}
//	
//	@RequestMapping(value = "/findTags", method = RequestMethod.GET)
//	public List<Tag> findTags(@RequestParam(defaultValue = "", required = false) String tag) {
//		return storyService.findTags(tag);	
//	}
//	
//	@RequestMapping(value = "/{id}/addTag", method = RequestMethod.POST)
//	public ResponseEntity<?> addTag(@PathVariable("id") long id,  @RequestParam("tag") String tag) {
//		try {
//			storyService.addTag(id, tag);
//			return new ResponseEntity<>(HttpStatus.OK);	
//		}
//		catch (Exception ex) {
//			return new ResponseEntity<ErrorResponse>(new ErrorResponse(
//					ex.getMessage()), HttpStatus.BAD_REQUEST);			
//		}	
//	}
//	
//	@RequestMapping(value = "/{id}/removeTag", method = RequestMethod.POST)
//	public ResponseEntity<?> removeTag(@PathVariable("id") long id,  @RequestParam("tag") String tag) {
//		try {
//			storyService.removeTag(id, tag);
//			return new ResponseEntity<>(HttpStatus.OK);	
//		}
//		catch (Exception ex) {
//			return new ResponseEntity<ErrorResponse>(new ErrorResponse(
//					ex.getMessage()), HttpStatus.BAD_REQUEST);			
//		}	
//	}
//	
//	@RequestMapping(value = "/{id}/setCover", method = RequestMethod.POST)
//	public ResponseEntity<?> setCover(@PathVariable("id") long id, @RequestParam("id") long userId, @RequestParam("file") MultipartFile[] files) {
//		try {
//			MultipartFile f = files[0];	
//			String cover = FileManager.uploadImage("u" + userId + "/s" + id + "/_", f.getBytes(), f.getOriginalFilename(), FileManager.ImageType.COVER);
//			storyService.setCover(id, cover);
//			return new ResponseEntity<>(JSON.parse("{\"cover\":\"" + cover + "\"}"), HttpStatus.OK);
//		} catch (IOException ex) {
//			return new ResponseEntity<ErrorResponse>(new ErrorResponse(
//					ex.getMessage()), HttpStatus.BAD_REQUEST);
//		}		
//	}
//	
//	@RequestMapping(value = "/likeStory", method = RequestMethod.POST)
//	public ResponseEntity<?> likeStory(@RequestParam("id") long userId, @RequestBody Story story) {
//		try {
//			storyService.likeStory(userId, story);
//			return new ResponseEntity<>(HttpStatus.OK);
//		}
//		catch (Exception ex) {
//			return new ResponseEntity<ErrorResponse>(new ErrorResponse(
//					ex.getMessage()), HttpStatus.BAD_REQUEST);			
//		}
//	}
//	
//	@RequestMapping(value = "/unlikeStory", method = RequestMethod.POST)
//	public ResponseEntity<?> unlikeStory(@RequestParam("id") long userId, @RequestBody Story story) {
//		try {
//			storyService.unlikeStory(userId, story);
//			return new ResponseEntity<>(HttpStatus.OK);	
//		}
//		catch (Exception ex) {
//			return new ResponseEntity<ErrorResponse>(new ErrorResponse(
//					ex.getMessage()), HttpStatus.BAD_REQUEST);			
//		}	
//	}
}
