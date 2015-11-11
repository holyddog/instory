package test.integration;

import me.instory.bean.ErrorResponse;
import me.instory.model.User;

import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.*;

public class AuthenControllerTest extends _BaseControllerTest {
	private static final String BASE_URI = "/authen";
	
	@Test
	public void signIn_completeWithEmail() {
		User params = new User();
		params.setUsername("holyddog@gmail.com");
		params.setPassword("whitedog");

		HttpEntity<User> entity = new HttpEntity<User>(params);		
		ResponseEntity<User> response = restTemplate.postForEntity(getBaseUri(BASE_URI + "/signIn"), entity, User.class);
		
		User user = response.getBody();
		
		assertNotNull(user);
		assertEquals(new Long(1), user.getId());
		assertEquals("holydog", user.getUsername());
		assertEquals("Holy D Dog", user.getDisplay_name());
		assertEquals("holyddog@gmail.com", user.getEmail());
	}

	@Test
	public void signIn_completeWithUsername() {
		User params = new User();
		params.setUsername("holydog");
		params.setPassword("whitedog");

		HttpEntity<User> entity = new HttpEntity<User>(params);		
		ResponseEntity<User> response = restTemplate.postForEntity(getBaseUri(BASE_URI + "/signIn"), entity, User.class);
		
		User user = response.getBody();
		
		assertNotNull(user);
		assertEquals(new Long(1), user.getId());
		assertEquals("holydog", user.getUsername());
		assertEquals("Holy D Dog", user.getDisplay_name());
		assertEquals("holyddog@gmail.com", user.getEmail());
	}

	@Test
	public void signIn_invalidPassword() {
		User params = new User();
		params.setUsername("holydog");
		params.setPassword("xxxxxxxx");

		HttpEntity<User> entity = new HttpEntity<User>(params);		
		ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(getBaseUri(BASE_URI + "/signIn"), entity, ErrorResponse.class);
		
		ErrorResponse error = response.getBody();
		
		assertNotNull(error);
		assertEquals("Invalid username/email or password", error.getError().getMessage());
	}

	@Test
	public void signIn_invalidLoginName() {
		User params = new User();
		params.setUsername("xxxxxx");
		params.setPassword("whitedog");

		HttpEntity<User> entity = new HttpEntity<User>(params);		
		ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(getBaseUri(BASE_URI + "/signIn"), entity, ErrorResponse.class);
		
		ErrorResponse error = response.getBody();
		
		assertNotNull(error);
		assertEquals("Invalid username/email or password", error.getError().getMessage());
	}
}
