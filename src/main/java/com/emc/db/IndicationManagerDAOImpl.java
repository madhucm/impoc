package com.emc.db;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.emc.constants.ResponseCode;

public class IndicationManagerDAOImpl implements IndicationManagerDAO {

	private final static IndicationManagerDAOImpl im = new IndicationManagerDAOImpl();
	static boolean isSubscriptionTableRowExists = false;

	/**
	 *  Client Registration
	 */
	@Override
	public ResponseCode registerUsername(String username) {
		Connection con = null;
		try {			
			con = DBFactory.getConnection();
			final String usr = username;
			boolean isUserExists = false;
			isUserExists = checkUserInDB(usr,con);
			if(!isUserExists) {
				String registerClientSQL = "INSERT INTO CLIENT_REGISTRATION(USERNAME,IP_ADDRESS,IS_ACTIVE) VALUES(?,?,?)";
				PreparedStatement p = con.prepareStatement(registerClientSQL);
				p.setString(1, usr);
				p.setString(2, "0.0.0.0");
				p.setBoolean(3, true);
				p.executeUpdate();
				p.close();
			} else {
				return ResponseCode.USER_PRESENT;
			}
		} catch(Exception e) {
			e.printStackTrace();
			return ResponseCode.REG_FAILED;
		}
		finally {
			DBFactory.closeDB();
		}
		return ResponseCode.REG_SUCCESS;
	}

	/**
	 * Find if user already exists
	 * @param usr Username
	 * @param con Connection Object
	 * @return
	 */
	private boolean checkUserInDB(String usr,Connection con) {
		String checkUserSQl = "select * from CLIENT_REGISTRATION where USERNAME = '"+usr+"'";
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
	 *  Make soft delete username
	 */
	@Override
	public ResponseCode unRegisterUsername(String username) {
		Connection con = null;
		try {
			con = DBFactory.getConnection();
			String unRegisterUsrSQL = "UPDATE CLIENT_REGISTRATION set IS_ACTIVE=false where USERNAME=?";
			PreparedStatement p = con.prepareStatement(unRegisterUsrSQL);
			p.setString(1, username);
			int result = p.executeUpdate();
			System.out.println("udpated status "+result);
			p.close();
			ResponseCode res = (result == 1)? ResponseCode.UNREG_SUCCESS:ResponseCode.UNREG_FAILURE;
			return res;

		}catch(Exception e) {
			e.printStackTrace();
		}		

		finally {
			DBFactory.closeDB();
		}
		return ResponseCode.UNREG_FAILURE;
	}

	@Override
	public ResponseCode subscribeEvent(String event,String username) {
		Connection con =null;
		try {
			con = DBFactory.getConnection();
			boolean clientEventFound = isEventExistsForClient(event,username,con);
			//STEP 1 : if no event existing in DB then insert new record
			if (!clientEventFound) {
				String selectClientIDSQL = "select UID from CLIENT_REGISTRATION where USERNAME='"+username+"'";
				Statement stm = con.createStatement();
				ResultSet rs = stm.executeQuery(selectClientIDSQL);
				rs.next();
				int client_id = rs.getInt("UID");
				String selectSubIdSQL = "select ID from SUBSCRIPTION_LIST where EVENT_NAME='"+event+"'";
				stm = con.createStatement();
				rs = stm.executeQuery(selectSubIdSQL);
				rs.next();
				int event_id = rs.getInt("ID");
				String insertSubcriptionSQL = "insert into USER_SUBSCRIPTION(EVENT_ID,USER_ID) values(?,?)";
				PreparedStatement p = con.prepareStatement(insertSubcriptionSQL);
				p.setInt(1, event_id);
				p.setInt(2, client_id);
				int result = p.executeUpdate();
				p.close();
				return (result == 1)?ResponseCode.SUB_SUCCESS:ResponseCode.SUB_FAILED;			
			}else {
				return ResponseCode.USER_ALREADY_RESGISTERED;
			}

		}catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			DBFactory.closeDB();
		}
		return ResponseCode.SUB_FAILED;
	}

	@Override
	public ResponseCode unSubscribeEvent(String event,String username) {
		Connection con =null;
		try {
			con = DBFactory.getConnection();
			String deleteEventForClientSQl = "delete from USER_SUBSCRIPTION where EVENT_ID in (select ID from SUBSCRIPTION_LIST where EVENT_NAME='"+event+"')"
					+ " and USER_ID in (select UID from CLIENT_REGISTRATION where USERNAME='"+username+"')";
			Statement stm = con.createStatement();
			int result = stm.executeUpdate(deleteEventForClientSQl);
			return (result == 1)?ResponseCode.UNSUB_SUCCESS:ResponseCode.UNSUB_FAILURE;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		finally {
			DBFactory.closeDB();
		}
		return ResponseCode.UNSUB_FAILURE;
	}

	@Override
	public List<String> listEvents(String username) {
		ResultSet rs = null;
		Connection con = null;
		List<String> list = new ArrayList<String>();
		try {
			con =DBFactory.getConnection();
			String listAllClientEventsSQL = "select EVENT_NAME from SUBSCRIPTION_LIST SL,USER_SUBSCRIPTION US,CLIENT_REGISTRATION CR"
					+ " where US.EVENT_ID=SL.ID and US.USER_ID=CR.UID and CR.USERNAME='"+username+"'";
			Statement stm = con.createStatement();
			rs = stm.executeQuery(listAllClientEventsSQL);
			while(rs.next()){
				list.add(rs.getString("EVENT_NAME"));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	private IndicationManagerDAOImpl() {

	}

	public static IndicationManagerDAO getDAO(){
		return im;
	}

	/**
	 * Get Subscription record.
	 */

	private boolean isEventExistsForClient(String event,String username,Connection con) {
		ResultSet rs = null;
		try {
			con =DBFactory.getConnection();
			String getSubcriptionListSQL = "select * from CLIENT_REGISTRATION C, SUBSCRIPTION_LIST S, USER_SUBSCRIPTION U"
					+ " where U.event_id=S.id and U.user_id=C.uid and C.USERNAME='"+username+"' and S.event_name='"+event+"'";
			Statement stm = con.createStatement();
			rs = stm.executeQuery(getSubcriptionListSQL);			
			//stm.close();
			return rs.next();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
