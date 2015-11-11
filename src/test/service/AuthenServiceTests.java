package test.service;

import me.instory.model.User;
import me.instory.service.AuthenService;
import me.instory.util.Convert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import static org.junit.Assert.*;

public class AuthenServiceTests extends _BaseServiceTest {	
	@Autowired 
	MongoTemplate db;
	
	@Autowired 
	AuthenService authenService;
	
//	boolean isDuplicateEmail(String email);
//	boolean isDuplicateUsername(String username);
//	void signUp(User user);
//	User signIn(String loginName, String password);

	@Test
	public void signUp_withFullInfo() throws Exception {
		User user = new User();
		user.setEmail("holydino@hotmail.com");
		user.setDisplay_name("Holy Dino");
		user.setUsername("holydino");
		user.setPassword("whitedog");
		
		authenService.signUp(user);
		
		User newUser = db.findOne(new Query(Criteria.where("id").is(4)), User.class);
		assertNotNull("Signup complete", newUser);
		assertEquals("Password valid", Convert.toMD5Password("whitedog"), newUser.getPassword());
	}
}
