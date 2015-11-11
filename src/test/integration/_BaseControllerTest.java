package test.integration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import me.instory.model.User;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
	"classpath:test/configuration/root-context.xml",
	"classpath:test/configuration/db-context.xml"
})
public abstract class _BaseControllerTest {
	@Autowired 
	MongoTemplate db;

	private static final String BASE_URI = "http://localhost:8080/instory%s";
	public String ACCESS_TOKEN = "8db67959-ec3a-4b1f-9076-5bfb2656f7ce";
	public RestTemplate restTemplate = new RestTemplate();
	
	@Before
	public void setup() {
		List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
		converters.add(new StringHttpMessageConverter());
		converters.add(new Jaxb2RootElementHttpMessageConverter());
		converters.add(new MappingJackson2HttpMessageConverter());
		restTemplate.setMessageConverters(converters);
		
		initDatabase();
	}
	
	public String getBaseUri(String path) {
		return String.format(BASE_URI, path) + "?access_token=" + ACCESS_TOKEN;
	}
	
	public long getDate() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DATE, 1);
		c.set(Calendar.MONTH, 0);
		c.set(Calendar.YEAR, 2015);
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		
		return c.getTimeInMillis();
	}
	
	private void initDatabase() {
		db.remove(new Query(), User.class);
		
		DBCollection counters = db.getCollection("counters");
        counters.remove(new BasicDBObject());
		
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Bangkok"));
		Locale.setDefault(Locale.US);
		
		long date = getDate();
		
		User u1 = new User();
		u1.setId(1L);
		u1.setEmail("holyddog@gmail.com");
		u1.setDisplay_name("Holy D Dog");
		u1.setUsername("holydog");
		u1.setPassword("a5aae1756e3d94286302de4b50e765bb");
		u1.setRegis_date(date);		
		db.insert(u1);
		
		User u2 = new User();
		u2.setId(2L);
		u2.setEmail("holyccat@gmail.com");
		u2.setDisplay_name("Holy C Cat");
		u2.setUsername("holycat");
		u2.setPassword("a5aae1756e3d94286302de4b50e765bb");
		u2.setRegis_date(date);		
		db.insert(u2);
		
		User u3 = new User();
		u3.setId(3L);
		u3.setEmail("maivet69@gmail.com");
		u3.setDisplay_name("Mai");
		u3.setUsername("maivet69");
		u3.setPassword("a5aae1756e3d94286302de4b50e765bb");
		u3.setRegis_date(date);		
		db.insert(u3);		

		BasicDBObject gen = new BasicDBObject();
        gen.put("_id", "users");	        

		DBObject update = new BasicDBObject("$set", new BasicDBObject("c", 4));
        counters.update(gen, update, true, false);    
	}
}