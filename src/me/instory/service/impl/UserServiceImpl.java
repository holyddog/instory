package me.instory.service.impl;

import me.instory.model.Follow;
import me.instory.model.User;
import me.instory.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private MongoTemplate db;

	@Override
	public User getInfo(long userId) {
		Criteria criteria = Criteria.where("inactive").ne(true).and("id").is(userId);
		Query query = new Query(criteria);
		query.fields()
				.include("username")
				.include("dn")
				.include("picture")
				.include("email")
				.include("stc")
				.include("fwc")
				.include("fic");
		return db.findOne(query, User.class);
	}

	@Override
	public void changePicture(long id, String picture) {
		Criteria criteria = Criteria.where("id").is(id);
		Query query = new Query(criteria);
		Update update = new Update();
		update.set("picture", picture);
		db.updateFirst(query, update, User.class);
	}

	@Override
	public void follow(long userId, long who) {
		Query query = new Query(Criteria.where("id").is(userId).and("who.id").is(who));
		long count = db.count(query, Follow.class);
		if (count == 0) {
			Follow follow = new Follow();
			follow.setId(userId);
			
			User user = new User();
			user.setId(who);
			
			follow.setWho(user);
			follow.setCreate_date(System.currentTimeMillis());
			db.insert(follow);
			
			db.updateFirst(new Query(Criteria.where("id").is(follow.getId())), new Update().inc("fic", 1), User.class);
			db.updateFirst(new Query(Criteria.where("id").is(follow.getWho().getId())), new Update().set("fwc", count + 1), User.class);
		}	
	}

	@Override
	public void unfollow(long userId, long who) {
		Query query = new Query(Criteria.where("id").is(userId).and("who.id").is(who));
		long count = db.count(query, Follow.class);
		if (count > 0) {
			db.remove(query, Follow.class);
			
			db.updateFirst(new Query(Criteria.where("id").is(userId)), new Update().inc("fic", -1), User.class);
			db.updateFirst(new Query(Criteria.where("id").is(who)), new Update().set("fwc", count - 1), User.class);			
		}
	}

	@Override
	public boolean isFollow(long userId, long who) {
		Query query = new Query(Criteria.where("id").is(userId).and("who.id").is(who));
		return db.count(query, Follow.class) > 0;
	}
}
