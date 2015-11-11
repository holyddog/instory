package me.instory.bean;

public class FileInfo {
	private String name;
	private String contentType;
	private int size;

	public FileInfo(String name, String contentType, int size) {
		super();
		this.name = name;
		this.contentType = contentType;
		this.size = size;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
}
