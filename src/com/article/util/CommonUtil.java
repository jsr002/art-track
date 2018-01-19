package com.article.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONException;
import org.json.JSONObject;

public class CommonUtil {

	public static Object getKeyValue(JSONObject obj, String key){
		Object value = null;
		try {
			value = obj.get(key);
		} catch (JSONException e) {
		
		}
		return value;
	}
	
	public static long getNextUserRoleId(){
		long userRoleId = 0;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String query = "select max(id) from user_role_journal";
		try{
			con = ConnectionUtil.getDBConnection();
			stmt = con.prepareStatement(query);
			rs = stmt.executeQuery();
			while(rs.next()){
				userRoleId = rs.getLong(1);
			}
		}catch(Exception e){
			System.out.println("Exception ::"+e);
		}
		return userRoleId;
	}
	
}
