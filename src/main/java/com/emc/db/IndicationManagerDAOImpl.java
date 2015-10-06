package com.emc.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.emc.constants.ResponseCode;

public class IndicationManagerDAOImpl implements IndicationManagerDAO {

	private final static IndicationManagerDAOImpl im = new IndicationManagerDAOImpl();

	/**
	 *  Client Registration
	 */
	@Override
	public ResponseCode registerUsername(String username) {
		Connection con = null;
		try {			
			con = DBFactory.getConnection();
			final String usr = username;
			//STEP 1 check if user already existing in DB;
			boolean isUserExists = false;
			isUserExists = checkUserInDB(usr,con);
			if(isUserExists) {
				String registerClientSQL = "INSERT INTO CLIENT_REGISTRATION(USERNAME,IP_ADDRESS,IS_ACTIVE) VALUES(?,?,?)";
				PreparedStatement p = con.prepareStatement(registerClientSQL);
				p.setString(1, usr);
				p.setString(2, "0.0.0.0");
				p.setBoolean(3, true);
				p.executeUpdate();
			} else {
				return ResponseCode.USER_PRESENT;
			}
		} catch(Exception e) {
			e.printStackTrace();
			return ResponseCode.REG_FAILED;
		}

		DBFactory.closeDB();
		return ResponseCode.REG_SUCCESS;
	}

	/**
	 * Find if user already exists
	 * @param usr Username
	 * @param con Connection Object
	 * @return
	 */
	private boolean checkUserInDB(String usr,Connection con) {
		String checkUserSQl = "select * from CLIENT_REGISTRATION where USERVNAME = '"+usr+"'";
		try {
			Statement stm = con.createStatement();
			ResultSet rs = stm.executeQuery(checkUserSQl);
			return rs.next();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 */
	@Override
	public ResponseCode unRegisterUsername(String username) {
		Connection con = null;
		try {
			con = DBFactory.getConnection();
			
		}catch(Exception e) {
			
		}
		
		
		return null;
	}

	@Override
	public ResponseCode subscribeEvent(String event) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseCode unSubscribeEvent(String event) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> listEvents(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	private IndicationManagerDAOImpl() {

	}

	public static IndicationManagerDAO getDAO(){
		return im;
	}
}
