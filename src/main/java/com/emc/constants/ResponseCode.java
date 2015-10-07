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
	USER_PRESENT("User Already Present."),
	UNREG_SUCCESS("Deregistration Successfull"),
	UNREG_FAILURE("Deregistration failure"),
	UNKNOW_USER("Unknow user"),
	LOGIN_SUCCESS("Login Successfull"),
	LOGOUT_SUCCESS("Logged out"),
	LOGIN_FAILED("Login Failed"),
	USER_NOT_LOGGED("User Not Logged In."),
	LOGIN_REQUIRED("Login Requiered"),
	USER_ALREADY_RESGISTERED("User Already Registered for the event");
	
	private String statusCode = null;
	private ResponseCode(String s){
		statusCode = s;
	}
	
	public String getResponseCode() {
		return statusCode;
	}
	
}
