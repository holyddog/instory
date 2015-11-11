package me.instory.util;

import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class Convert {
	public static String toString(Object obj) {
		if (obj != null) {
			try {
				return new String(String.valueOf(obj).getBytes("ISO-8859-1"), "UTF-8");
			} 
			catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static long generateId(Class<?> cls, MongoTemplate db) {
		Annotation a = cls.getAnnotations()[0];
		String collName = (String) AnnotationUtils.getAnnotationAttributes(a).get("collection");
		
		DBCollection coll = db.getCollection("counters");
		
		BasicDBObject find = new BasicDBObject();
		find.put("_id", collName);
		
		if (coll.findOne(find) == null) {
			BasicDBObject gen = new BasicDBObject();
	        gen.put("_id", collName);
	        gen.put("c", 1);
	        
	        coll.insert(gen);
		}		
		
		DBObject update = new BasicDBObject("$inc", new BasicDBObject("c", 1));
		
		Object val = coll.findAndModify(find, update).get("c");
		if (val instanceof Double) {
			val = (double) val;
		}
		return new Long(val.toString());
	}
	
	public static String toMD5Password(String inputPwd) {
	    MessageDigest algorithm = null;
	    
	    try {
	    	algorithm = MessageDigest.getInstance("MD5");
	    }
	    catch (NoSuchAlgorithmException ex) {
	        return null;
	    }
	    
        byte[] defaultBytes = inputPwd.getBytes();
        algorithm.reset();
        algorithm.update(defaultBytes);
        
        byte messageDigest[] = algorithm.digest();        
        StringBuffer hexString = new StringBuffer();
        
        for (int i = 0; i < messageDigest.length; i++) {
            String hex = Integer.toHexString(0xFF & messageDigest[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
		return hexString.toString();
	}
}
