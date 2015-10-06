package com.emc.im;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.emc.db.DBFactory;

public class Testings {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Connection con = DBFactory.getConnection();
		System.out.println(con);
		/*String registerClientSQL = "INSERT INTO CLIENT_REGISTRATION (USERNAME,IP_ADDRESS,IS_ACTIVE) VALUES(?,?,?)";
		PreparedStatement p = con.prepareStatement(registerClientSQL);
		p.setString(1, "sadf");
		p.setString(2, "0.0.0.0");
		p.setBoolean(3, true);
		p.executeUpdate();
		p.close();*/
		Statement s = con.createStatement();
		ResultSet r = s.executeQuery("select * from CLIENT_REGISTRATION");
		System.out.println(r);
	}

}
