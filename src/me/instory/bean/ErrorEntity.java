package me.instory.bean;

public class ErrorEntity {
	String message;
	String type;
	Integer code;
	
	public ErrorEntity() {
		
	}

	public ErrorEntity(String message) {
		super();
		this.message = message;
	}

	public ErrorEntity(String message, String type, int code) {
		super();
		this.message = message;
		this.type = type;
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
}
