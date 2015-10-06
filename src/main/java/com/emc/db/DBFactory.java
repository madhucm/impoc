package com.emc.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBFactory {
	private static DBFactory factory = new DBFactory();
	private String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    private String dbName = "IMDB";
    private String connectionURL = "jdbc:derby:" + dbName + "";
    static Connection conn;
    
    private DBFactory() {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    private Connection createConnection() {
    	try {
			conn = DriverManager.getConnection("jdbc:derby:C:/Program Files (x86)/Java/jdk1.8.0_45/db/bin/IMDB");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return conn;
    }
    
    public static Connection getConnection() {
    	return factory.createConnection();
    }
    
    public static void closeDB() {
    	try {
    		if(conn !=null) {
    			conn.close();
    		}
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }
}
