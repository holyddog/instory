package test.service;

import me.instory.service.UserService;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import static org.junit.Assert.*;

public class UserServiceTests extends _BaseServiceTest {	
	@Autowired 
	MongoTemplate db;
	
	@Autowired 
	UserService userService;

	@Test
	public void getInfo_noUser() throws Exception {
		assertNull("User is null", userService.getInfo(0));
	}

	@Test
	public void getInfo_hasUser() throws Exception {
		assertNotNull("User is not null", userService.getInfo(1));
	}
}
