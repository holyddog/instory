package me.instory.service.impl;

import java.util.HashMap;
import java.util.List;

import me.instory.model.Comment;
import me.instory.model.Post;
import me.instory.model.Story;
import me.instory.model.User;
import me.instory.service.CommentService;
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
public class CommentServiceImpl implements CommentService {

	@Autowired
	private MongoTemplate db;
	
	private void fetchUsers(List<Comment> list) {
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
	public List<Comment> getStoryComments(long id) {
		Query q = new Query(Criteria.where("story.id").is(id).and("post").exists(false));
		q.with(new Sort(Direction.DESC, "cdt"));
		
		List<Comment> list = db.find(q, Comment.class);
		fetchUsers(list);
		return list;
	}

	@Override
	public List<Comment> getPostComments(long id) {
		Query q = new Query(Criteria.where("post.id").is(id));
		q.with(new Sort(Direction.DESC, "cdt"));
		
		List<Comment> list = db.find(q, Comment.class);
		fetchUsers(list);
		return list;
	}

	@Override
	public long add(Comment comment) {
		db.setWriteConcern(WriteConcern.ACKNOWLEDGED);		
		
		Long id = Convert.generateId(Comment.class, db);
		comment.setId(id);
		comment.setCreate_date(System.currentTimeMillis());
		
		if (comment.getPost() != null) {
			Query q = new Query(Criteria.where("id").is(comment.getPost().getId()));
			db.updateFirst(q, new Update().inc("cc", 1), Post.class);
		}
		else {
			Query q = new Query(Criteria.where("id").is(comment.getStory().getId()));
			db.updateFirst(q, new Update().inc("cc", 1), Story.class);			
		}
		
		db.insert(comment);
		
		return id;
	}

	@Override
	public void remove(long id) {	
		Criteria criteria = Criteria.where("id").is(id);
		Query query = new Query(criteria);
		
		db.remove(query, Comment.class);
	}
}
