package me.instory.util;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;

//import com.amazonaws.auth.AWSCredentialsProvider;
//import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.AmazonS3Client;
//import com.amazonaws.services.s3.model.PutObjectRequest;

public class FileManager {	
	public static class ImageSize {
		public static final int ORIGINAL = 0;
		public static final int SMALL = 200;
		public static final int MEDIUM = 640;
		public static final int LARGE = 720;
	}
	
	public static class ImageType {
		public static final int CROP = 1;
		public static final int RESIZE = 2;
		public static final int COVER = 3;
	}
	
	public static void sendToS3(String path, File file) {
//		AWSCredentialsProvider credentialsProvider = new ClasspathPropertiesFileCredentialsProvider();
//		AmazonS3 s3 = new AmazonS3Client(credentialsProvider);
//		String bucketName = getProp("buckets");
//		s3.putObject(new PutObjectRequest(bucketName, "public/" + path, file));
		
		String sysPath = getProp("dir") + "/" + path;
		File dir = new File(sysPath.substring(0, sysPath.lastIndexOf('/')));
		if (!dir.exists()) {
			dir.mkdirs();
		}

		File newFile = new File(sysPath);
		try {
			IOUtils.copy(new FileInputStream(file), new FileOutputStream(newFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String uploadImage(String prefix, byte[] bytes, String fileName, int imageType) {			
		File file = new File(fileName);			
		FileOutputStream output;
		try {
			output = new FileOutputStream(file);
			output.write(bytes);  
			output.flush();  
			output.close();			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}  

		long time = System.currentTimeMillis();
		String rand = randomString(6);

		String oriName = time + "_" + rand + "." + getFileExt(fileName);
		if (imageType == ImageType.CROP) {
			cropImage(file);
			sendToS3(prefix + "/" + oriName, file);			
		}
		else if (imageType == ImageType.RESIZE) {	
			String sysName = time + "_" + rand + "_o." + getFileExt(fileName);	
			sendToS3(prefix + "/" + sysName, file);
			
			resize(file, ImageSize.MEDIUM);
			sysName = time + "_" + rand + "." + getFileExt(fileName);	
			sendToS3(prefix + "/" + sysName, file);
		}
		else if (imageType == ImageType.COVER) {	
			String sysName = time + "_" + rand + "_o." + getFileExt(fileName);	
			sendToS3(prefix + "/" + sysName, file);
			
			resize(file, ImageSize.LARGE);
			sendToS3(prefix + "/" + oriName, file);
		}
		
		return "/" + prefix + "/" + oriName;
	}
	
	public static void resize(File input, int size) {
		try {
			String fileExt = getFileExt(input.getName());
			
			BufferedImage src = ImageIO.read(input);
			int imageWidth = ((Image)src).getWidth(null);
			int imageHeight = ((Image)src).getHeight(null);
				
			int maxHeight = size;
			int maxWidth = size;
			double imageRatio = (double) imageWidth / (double) imageHeight;	
			if (imageWidth > imageHeight) {
				maxWidth = (int) (maxHeight * imageRatio);
			}
			else if (imageWidth < imageHeight) {
				maxHeight = (int) (maxWidth / imageRatio);
			}			
			
			Image image = src.getScaledInstance(maxWidth, maxHeight, Image.SCALE_SMOOTH);
			BufferedImage newBuff = new BufferedImage(maxWidth, maxHeight, BufferedImage.TYPE_INT_RGB);
			Graphics g = newBuff.getGraphics();
			g.drawImage(image, 0, 0, null);
			g.dispose();	
		    
			ImageIO.write(newBuff, fileExt, input);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void cropImage(File input) {
		try {
			String fileExt = getFileExt(input.getName());
			
			BufferedImage src = ImageIO.read(input);
			int imageWidth = ((Image)src).getWidth(null);
			int imageHeight = ((Image)src).getHeight(null);
				
		    int imageDir = 0;
			int maxHeight = ImageSize.SMALL;
			int maxWidth = ImageSize.SMALL;
			double imageRatio = (double) imageWidth / (double) imageHeight;	
			if (imageWidth > imageHeight) {
				maxWidth = (int) (maxHeight * imageRatio);
			}
			else if (imageWidth < imageHeight) {
				maxHeight = (int) (maxWidth / imageRatio);
				imageDir = 1;
			}			
			
			Image image = src.getScaledInstance(maxWidth, maxHeight, Image.SCALE_SMOOTH);
			BufferedImage newBuff = new BufferedImage(maxWidth, maxHeight, BufferedImage.TYPE_INT_RGB);
			Graphics g = newBuff.getGraphics();
			g.drawImage(image, 0, 0, null);
			g.dispose();		    
			
			int size = ImageSize.SMALL;
			int x = 0;
			int y = 0;
			
			if (imageDir == 0) {
				x = (int) ((((imageWidth-imageHeight)/2) * size) / imageHeight);
			}
			else {
				y =  (int) ((((imageHeight-imageWidth)/2) * size) / imageWidth);
			}
			
			BufferedImage dest = new BufferedImage(size, size, src.getType());
			g = dest.getGraphics();
			g.drawImage(newBuff, 0, 0, size, size, x, y, x + size, y + size, null);
			g.dispose();
		    
			ImageIO.write(dest, fileExt, input);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getFileExt(String fileName) {
		return fileName.substring(fileName.lastIndexOf('.') + 1, fileName.length());
	}

//	public static String generateFileName(long id, String fileName) {
//		long time = System.currentTimeMillis();
//		String rand = randomString(6);
//		String ext = fileName.substring(fileName.lastIndexOf('.'), fileName.length());
//		return "u" + id + "/" + time + "_" + rand + ext;
//	}
	
	public static String getProp(String name) {
		Properties prop = new Properties();
		try {
			prop.load(Convert.class.getClassLoader().getResourceAsStream("config.properties"));

			return prop.getProperty(name);

		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public static String randomString(int length) {
		String randChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIGKLMNOPQRSTUVWXYZ012345678901234567890123456789";
		Random rand = new Random();
	    char[] text = new char[length];
	    for (int i = 0; i < length; i++) {
	        text[i] = randChars.charAt(rand.nextInt(randChars.length()));
	    }
	    return new String(text);
	}
}
