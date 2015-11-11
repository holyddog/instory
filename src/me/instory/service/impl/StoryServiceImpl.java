package me.instory.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import me.instory.bean.Picture;
import me.instory.model.Follow;
import me.instory.model.Post;
import me.instory.model.Story;
import me.instory.model.Tag;
import me.instory.model.User;
import me.instory.service.StoryService;
import me.instory.util.Convert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.mongodb.WriteConcern;

@Service
public class StoryServiceImpl implements StoryService {

	@Autowired
	private MongoTemplate db;
	
	private void fetchUsers(List<Story> list) {
		HashMap<Long, User> map = new HashMap<Long, User>();	
		for (int i = 0; i < list.size(); i++) {
			User user = list.get(i).getAuthor();
						
			if (map.containsKey(user.getId())) {
				list.get(i).setAuthor(map.get(user.getId()));
			}
			else {
				Query query = new Query(Criteria.where("id").is(user.getId()));
				query.fields().include("picture").include("username");
				User info = db.findOne(query, User.class);				
				list.get(i).setAuthor(info);
				map.put(user.getId(), info);
			}
		}
	}

	@Override
	public List<Story> findStories(long userId) {
		Query q = new Query(Criteria.where("author.id").is(userId));
		q.with(new Sort(Direction.DESC, "cdt"));
		q.fields().exclude("likes");
		return db.find(q, Story.class);
	}

	@Override
	public long addStory(long userId, Story story) {
		db.setWriteConcern(WriteConcern.ACKNOWLEDGED);
		
		Long id = Convert.generateId(Story.class, db);
		story.setId(id);
		story.setCreate_date(System.currentTimeMillis());
		
		User author = new User();
		author.setId(userId);
		story.setAuthor(author);
		
		db.insert(story);
		
		Update update = new Update();
		update.inc("stc", 1);
		db.updateFirst(new Query(Criteria.where("id").is(userId)), update, User.class);
		
		return id;
	}

	@Override
	public void editStory(long storyId, long userId, Story story) {
		Criteria criteria = Criteria.where("id").is(storyId).and("author.id").is(userId);
		Query query = new Query(criteria);
		
		Story s = db.findOne(query, Story.class);
		String[] oldTags = s.getTags();
		
		Update update = new Update();
		update.set("title", story.getTitle());
		update.set("desc", story.getDesc());
		update.set("tags", story.getTags());
		
		db.updateFirst(query, update, Story.class);
		
		if (oldTags != null) {
			for (int i = 0; i < oldTags.length; i++) {
				Criteria c = Criteria.where("tag").regex(Pattern.compile("^" + oldTags[i] + "$", Pattern.CASE_INSENSITIVE));
				db.updateFirst(new Query(c), new Update().inc("count", -1), Tag.class);
			}			
		}
		
		String[] newTags = story.getTags();
		if (newTags != null) {
			for (int i = 0; i < newTags.length; i++) {
				Criteria c = Criteria.where("tag").regex(Pattern.compile("^" + newTags[i] + "$", Pattern.CASE_INSENSITIVE));
				long count = db.count(new Query(c), Tag.class);
				if (count > 0) {
					db.updateFirst(new Query(c), new Update().inc("count", 1), Tag.class);				
				}
				else {
					db.insert(new Tag(newTags[i], 1));					
				}			
			}			
		}
	}

	@Override
	public void deleteStory(long storyId, long userId) {
		Criteria criteria = Criteria.where("id").is(storyId).and("author.id").is(userId);
		db.remove(new Query(criteria), Story.class);
	}

//	@Override
//	public List<Story> findAll() {
//		Query q = new Query();
//		q.with(new Sort(Direction.DESC, "cdt"));	
//		q.fields().exclude("likes");
//		
//		List<Story> list = db.find(q, Story.class);
//		fetchUsers(list);		
//		
//		return list;
//	}
//
//	@Override
//	public List<Story> findFollowStories(long id) {
//		List<Follow> follows = db.find(new Query(Criteria.where("id").is(id)), Follow.class);
//		List<Long> ids = new ArrayList<Long>();
//		for (int i = 0; i < follows.size(); i++) {
//			ids.add(follows.get(i).getWho().getId());
//		}
//		
//		Query q = new Query(Criteria.where("author.id").in(ids));
//		q.with(new Sort(Direction.DESC, "cdt"));	
//		q.fields().exclude("likes");			
//		
//		List<Story> list = db.find(q, Story.class);
//		fetchUsers(list);	
//		
//		return list;
//	}
//
//	@Override
//	public Story getStory(long id) {
//		Criteria criteria = Criteria.where("id").is(id);
//		Query query = new Query(criteria);	
//		query.fields().exclude("likes");	
//		Story story = db.findOne(query, Story.class);
//		
//		if (story != null) {
//			Query q = new Query(Criteria.where("story_id").is(id));
//			q.with(new Sort(Direction.ASC, "_id"));
//			List<Post> posts = db.find(q, Post.class);
//			story.setPosts(posts);
//		}
//		
//		return story;
//	}
//
//	@Override
//	public void setCover(long id, String cover) {
//		Criteria criteria = Criteria.where("id").is(id);
//		Query query = new Query(criteria);
//		Update update = new Update();
//		update.set("cover", cover);
//		db.updateFirst(query, update, Story.class);
//	}
//
//	@Override
//	public long addPost(Post post) {
//		db.setWriteConcern(WriteConcern.ACKNOWLEDGED);		
//		
//		Long id = Convert.generateId(Post.class, db);
//		post.setId(id);
//		post.setCreate_date(System.currentTimeMillis());
//		
//		db.insert(post);
//		
//		return id;
//	}
//
//	@Override
//	public void editPost(long id, Post post) {
//		Criteria criteria = Criteria.where("id").is(id);
//		Query query = new Query(criteria);
//		
//		Update update = new Update();
//		update.set("text", post.getText());
//		update.set("pictures", post.getPictures());
//		
//		db.updateFirst(query, update, Post.class);
//	}
//
//	@Override
//	public void removePost(long id) {
//		Criteria criteria = Criteria.where("id").is(id);
//		Query query = new Query(criteria);
//		
//		db.remove(query, Post.class);
//	}
//
//	@Override
//	public long addPostPicture(long id, Picture pic) {
//		db.setWriteConcern(WriteConcern.ACKNOWLEDGED);		
//
//		Post post = null;
//		if (id == -1) {
//			id = Convert.generateId(Post.class, db);
//			post = new Post();
//			post.setId(id);
//			post.setCreate_date(System.currentTimeMillis());
//			
//			List<Picture> pics = new ArrayList<Picture>();
//			pics.add(pic);
//			post.setPictures(pics);
//			
//			db.insert(post);
//		}
//		else {
//			Update update = new Update();
//			update.push("pictures", pic);
//			
//			Criteria criteria = Criteria.where("id").is(id);
//			Query query = new Query(criteria);
//			db.updateFirst(query, update, Post.class);
//		}
//		
//		
//		return id;
//	}
//
//	@Override
//	public List<Tag> findTags(String tag) {
//		Criteria c = Criteria.where("tag").regex("^" + tag, "i");
//		return db.find(new Query(c), Tag.class);
//	}
//
//	@Override
//	public void addTag(long id, String tag) {
//		Criteria criteria = Criteria.where("id").is(id);
//		Query query = new Query(criteria);
//		Update update = new Update();
//		update.push("tags", tag);
//		db.updateFirst(query, update, Story.class);
//		
//		Criteria c = Criteria.where("tag").regex(tag, "i");
//		db.upsert(new Query(c), new Update().inc("count", 1), Tag.class);
//	}
//
//	@Override
//	public void removeTag(long id, String tag) {
//		Criteria criteria = Criteria.where("id").is(id);
//		Query query = new Query(criteria);
//		Update update = new Update();
//		update.pull("tags", tag);
//		db.updateFirst(query, update, Story.class);
//		
//		Criteria c = Criteria.where("tag").regex(tag, "i");
//		db.upsert(new Query(c), new Update().inc("count", -1), Tag.class);
//	}
//
//	@Override
//	public void likeStory(long id, Story story) {
//		Query query = new Query(Criteria.where("id").is(story.getId()).and("likes").is(id));
//		if (db.count(query, Story.class) == 0) {
//			Update update = new Update();
//			update.inc("lc", 1);
//			update.push("likes", id);
//			db.updateFirst(new Query(Criteria.where("id").is(story.getId())), update, Story.class);
//		}
//	}
//
//	@Override
//	public void unlikeStory(long id, Story story) {
//		Query query = new Query(Criteria.where("id").is(story.getId()).and("likes").is(id));
//		if (db.count(query, Story.class) > 0) {
//			Update update = new Update();
//			update.inc("lc", -1);
//			update.pull("likes", id);
//			db.updateFirst(new Query(Criteria.where("id").is(story.getId())), update, Story.class);
//		}
//	}
//
//	@Override
//	public void likePost(long id, Post post) {
//		Query query = new Query(Criteria.where("id").is(post.getId()).and("likes").is(id));
//		if (db.count(query, Post.class) == 0) {
//			Update update = new Update();
//			update.inc("lc", 1);
//			update.push("likes", id);
//			db.updateFirst(new Query(Criteria.where("id").is(post.getId())), update, Post.class);
//		}
//	}
//
//	@Override
//	public void unlikePost(long id, Post post) {
//		Query query = new Query(Criteria.where("id").is(post.getId()).and("likes").is(id));
//		if (db.count(query, Post.class) > 0) {
//			Update update = new Update();
//			update.inc("lc", -1);
//			update.pull("likes", id);
//			db.updateFirst(new Query(Criteria.where("id").is(post.getId())), update, Post.class);
//		}
//	}
}
