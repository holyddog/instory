package test.integration;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import me.instory.model.User;
import me.instory.util.Convert;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.*;

public class UserControllerTest extends _BaseControllerTest {
	private static final String BASE_URI = "/user";

	@Test
	public void findMe() {
		ResponseEntity<User> response = restTemplate.exchange(getBaseUri(BASE_URI + "/me"), HttpMethod.GET, null, User.class);
		
		User user = response.getBody();
		
		assertNotNull(user);
		assertEquals(new Long(1), user.getId());
		assertEquals("holydog", user.getUsername());
		assertEquals("Holy D Dog", user.getDisplay_name());
		assertEquals("holyddog@gmail.com", user.getEmail());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void findUserById_existingUser() throws IllegalAccessException, InvocationTargetException {
		ResponseEntity<Map> response = restTemplate.exchange(getBaseUri(BASE_URI + "/1"), HttpMethod.GET, null, Map.class);
		
		Map<String, Object> data = (Map<String, Object>) response.getBody();
		
		User user = new User();		
		BeanUtils.populate(user, (Map<String, Object>) data.get("user"));
		
		boolean following = (boolean) data.get("following");
		
		assertNotNull(user);
		assertEquals(new Long(1), user.getId());
		assertEquals("holydog", user.getUsername());
		assertEquals("Holy D Dog", user.getDisplay_name());
		assertEquals("holyddog@gmail.com", user.getEmail());
		
		assertFalse(following);
	}
}
