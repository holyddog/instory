package me.instory.bean;

public class ErrorResponse {
	ErrorEntity error;
	
	public ErrorResponse() {
		
	}

	public ErrorResponse(String message) {
		super();

		this.error = new ErrorEntity(message);
	}

	public ErrorResponse(String message, String type, int code) {
		super();

		this.error = new ErrorEntity(message, type, code);
	}

	public ErrorEntity getError() {
		return error;
	}

	public void setError(ErrorEntity error) {
		this.error = error;
	}
}
