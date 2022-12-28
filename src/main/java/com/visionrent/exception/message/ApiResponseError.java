package com.visionrent.exception.message;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ApiResponseError {
	// aim: to hold customised error messages
	private HttpStatus status;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
	private LocalDateTime timeStamp;

	// exception message
	private String message;

	// requested corresponding end-point
	private String requestURI;

	// private constructor
	private ApiResponseError() {
		timeStamp = LocalDateTime.now();
	}

	public ApiResponseError(HttpStatus status) {
		this();
		this.message = "Unexpected Error";
		this.status = status;
	}

	public ApiResponseError(HttpStatus status, String message, String requestURI) {
		this(status);// calls 1 parameter constructor
		this.message = message;
		this.requestURI = requestURI;
	}

	// won't use lombok, because we don't want to create set method for timeStamp

	// Getter-Setter
	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getRequestURI() {
		return requestURI;
	}

	public void setRequestURI(String requestURI) {
		this.requestURI = requestURI;
	}

	public LocalDateTime getTimeStamp() {
		return timeStamp;
	}

}
