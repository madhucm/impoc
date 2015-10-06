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
	
	 public static void main(String[] args) {
	        post("/register/:username",(req,res)-> {
	        	String username = req.params(":username");
	        	//call DB API for registration
	        	
	        	IndicationManagerDAO im = IndicationManagerDAOImpl.getDAO();
	        	ResponseCode resp = im.registerUsername(username);
	        	
	        	return resp.getResponseCode();
	        });
	        
	        delete("/deregister/:username",(req,res)-> {
	        	String username = req.params(":username");
	        	//call DB API for de-regstration
	        	return "Removed "+username+" for registaring events";
	        });
	        
	        post("/subscribe/:event",(req,res)-> {
	        	String event = req.params(":event");
	        	//call DB API for registration
	        	return "Event "+event+"+subscription successfull ";
	        });
	        
	        delete("/unsubscribe/:event",(req,res)-> {
	        	String event = req.params(":event");
	        	//call DB API for de-regstration
	        	return "Event "+event+" unsubscribed";
	        });
	        
	        get("/listevents/:username",(req,res)-> {
	        	String username = req.params(":username");
	        	//Call DB to get all the events for the username
	        	return new ArrayList();
	        });
	        
	        before("/", (request, response) -> {
	            // ... check if authenticated
	            halt(401, "Go Away!");
	        });
	    }
	 
	
}
