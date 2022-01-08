package com.example.updateservice.model;

public class Constants 
{
	private Constants() {}
	
	public static final String ENDPOINT_VERSION = "/v1";
	public static final String UPDATE_ENDPOINT = "/books/update";
	public static final String UNPROCESSALE_EXCEPTION_MESSAGE = "No book exists for: ";
	public static final String INTERNAL_SERVER_ERROR_MESSAGE = "Internal Server Error";
	public static final String CORRELATION_ID = "X-Correlation-Id";
	public static final String BAD_REQUEST_MESSAGE = "Invalid Mandatory Parameter";
}
