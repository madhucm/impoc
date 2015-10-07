package com.emc.im;

import static spark.Spark.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;

import com.emc.constants.ResponseCode;
import com.emc.db.DBFactory;
import com.emc.db.IndicationManagerDAO;
import com.emc.db.IndicationManagerDAOImpl;
public class Main {
	static IndicationManagerDAO im = IndicationManagerDAOImpl.getDAO();
	final static String SESSION_NAME = "username";
	public static void main(String[] args) {
		
		get("/login/:username",(req,res)-> {
			String username = req.params(":username");
			if (username != null) {
				req.session().attribute(SESSION_NAME, username);
			}
			return ResponseCode.LOGIN_SUCCESS;
		});
		
		delete("/logout",(req,res)-> {
			 String name = req.session().attribute(SESSION_NAME);
			 if(name == null) {
				 return ResponseCode.USER_NOT_LOGGED;
			 }
			 req.session().removeAttribute(SESSION_NAME);
			 return ResponseCode.LOGOUT_SUCCESS;
		});

		post("/register/:username",(req,res)-> {
			String username = req.params(":username");
			//call DB API for registration
			ResponseCode resp = im.registerUsername(username);
			res.status(201);
			return resp.getResponseCode();
		});

		delete("/deregister/:username",(req,res)-> {
			String username = req.params(":username");
			//call DB API for de-regstration
			ResponseCode resp = im.unRegisterUsername(username);
			return resp.getResponseCode();
		});

		post("/subscribe/:event",(req,res)-> {
			 String username = req.session().attribute(SESSION_NAME);
			 System.out.println("Logged user "+username);
	            if (username == null) {
	            	return ResponseCode.LOGIN_REQUIRED;
	            }
			String event = req.params(":event");
			//call DB API for register Subscription
			ResponseCode resp = im.subscribeEvent(event, username);
			return resp.getResponseCode();
		});

		delete("/unsubscribe/:event",(req,res)-> {
			String event = req.params(":event");
			 String username = req.session().attribute(SESSION_NAME);
			 System.out.println("Logged user "+username);
	            if (username == null) {
	            	System.out.println(username);
	            	return "Login required";
	            }
			//call DB API for unsubscribe
	            ResponseCode resp = im.unSubscribeEvent(event, username);
				return resp.getResponseCode();
		});

		get("/listevents",(req,res)-> {
			String name = req.session().attribute(SESSION_NAME);
			 System.out.println("Logged user "+name);
	            if (name == null) {
	            	System.out.println(name);
	            	return "Login required";
	            }
			//Call DB to get all the events for the username
			return im.listEvents(name);
		});

		before("/", (request, response) -> {
			// ... check if authenticated
			halt(401, "Go Away!");
		});


	}


}
