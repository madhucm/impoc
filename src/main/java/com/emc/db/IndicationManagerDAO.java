package com.emc.db;

import java.util.List;

import com.emc.constants.ResponseCode;

public interface IndicationManagerDAO {
	public ResponseCode registerUsername(String username) throws Exception;
	public ResponseCode unRegisterUsername(String username) throws Exception;
	public ResponseCode subscribeEvent(String event) throws Exception;
	public ResponseCode unSubscribeEvent(String event) throws Exception;
	public List<String> listEvents(String username) throws Exception;
}
