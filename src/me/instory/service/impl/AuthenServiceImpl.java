package me.instory.service.impl;

import me.instory.model.User;
import me.instory.service.AuthenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.mongodb.WriteConcern;

import me.instory.util.Convert;

@Service
public class AuthenServiceImpl implements AuthenService {
	
	@Autowired
	private MongoTemplate db;

	@Override
	public void signUp(User user) {
		db.setWriteConcern(WriteConcern.ACKNOWLEDGED);
		
		user.setId(Convert.generateId(User.class, db));
		user.setPassword(Convert.toMD5Password(user.getPassword()));
		user.setInactive(false);
		user.setRegis_date(System.currentTimeMillis());
		db.insert(user);
	}

	@Override
	public User signIn(String loginName, String password) {
		Criteria loginCriteria = new Criteria().orOperator(
			Criteria.where("username").is(loginName), 
			Criteria.where("email").is(loginName)
		);
		
		password = Convert.toMD5Password(password);
		Criteria criteria = Criteria.where("inactive").ne(true).andOperator(loginCriteria).and("password").is(password);
			
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
	public boolean isDuplicateEmail(String email) {
		Criteria criteria = Criteria.where("email").is(email);
		return db.count(new Query(criteria), User.class) > 0;
	}

	@Override
	public boolean isDuplicateUsername(String username) {
		Criteria criteria = Criteria.where("username").is(username);
		return db.count(new Query(criteria), User.class) > 0;
	}	
}
