package com.emc.constants;

public enum ResponseCode {
	
	REG_SUCCESS("Registration Successfull"),
	REG_FAILED("Something went bad. Registration unsucessfull"),
	SUB_SUCCESS("Event Subscribed"),
	SUB_FAILED("Event subscribtion failed"),
	UNSUB_SUCCESS("Event unsubscribed"),
	UNSUB_FAILURE("Event unsubscribtion failed"),
	LIST_FAILED("List Event Failed"),
	LIST_SUCCESS("List Event"),
	USER_PRESENT("User Already Present.");
	
	private String statusCode = null;
	private ResponseCode(String s){
		statusCode = s;
	}
	
	public String getResponseCode() {
		return statusCode;
	}
	
}
