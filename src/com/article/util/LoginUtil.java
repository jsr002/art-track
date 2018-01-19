package com.article.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginUtil {
	
	public static String logoutUser(HttpServletRequest request,HttpServletResponse response){
		JSONObject responseStr = new JSONObject();

		HttpSession session = request.getSession();
		session.setAttribute("user", "");
		try{
		responseStr.put("message", "user logout successful");
		}catch(Exception e){
			
		}
		return responseStr.toString();
	}

	public static String authenticateUser(String userString,HttpServletRequest httpRequest, HttpServletResponse httpResponse){
		String response = null;
		Connection con= null;
		JSONObject userJson = new JSONObject();
		try {
			userJson = new JSONObject(userString);
		} catch (JSONException e2) {
		}
		JSONObject jsonResponse = new JSONObject();
		JSONObject user = new JSONObject();
		JSONArray jArray = new JSONArray();
		String pass = null;
		String userName = null;
		Long status = 0L;
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			con = DriverManager.getConnection(  
			"jdbc:mysql://localhost:3306/ats","root","");  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement();
			 userName =((JSONObject) userJson.get("user")).getString("userName");
			ResultSet rs=stmt.executeQuery("select * from users where email = '"+userName+"'");  
			while(rs.next()){
				
				 pass = rs.getString("PASSWORD");
				// status =  rs.getLong("STATUS");
				 status =1L;
				 user.put("userName",rs.getString("USERNAME")); 
				 user.put("email",rs.getString("EMAIL")); 
				 user.put("mobileNumber",rs.getString("MOBILENUMBER")); 
				 user.put("country",rs.getString("country")); 
				 user.put("userId",rs.getString("userId")); 
				 user.put("address",rs.getString("address"));
				 user.put("aboutMe",rs.getString("aboutMe"));
				 user.put("roles", getUserRoles(rs.getString("userId")));
			}
			String password = ((JSONObject) userJson.get("user")).getString("password");
			if(password.equals(pass) && status == 1){
				jsonResponse.put("isAuthenticated", true);
				jsonResponse.put("user", user);
				HttpSession session = httpRequest.getSession();
				session.setAttribute("user", user.toString());
			}else{
				jsonResponse.put("isAuthenticated", false);
				jsonResponse.put("message", "Authentication failed");
				
			}
			}catch(Exception e){ 
				
				try {
					jsonResponse.put("isAuthenticated", false);
					jsonResponse.put("message", "Error while login");
				} catch (JSONException e1) {
					
				}
				} 
			finally{
				try {
					if(con != null)
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				} 
			}
		
		
		return jsonResponse.toString();
		
	}
	
	public static String registerUser(String userString){
		
		String response = null;
		JSONObject userJson = new JSONObject();
		String userName = null;
		String email = 	null;
		String password =null;
		String address = null;
		String phoneNumber =null;
		String country = null;
		Connection con= null;
		try {
			userJson = (new JSONObject(userString)).getJSONObject("user");
			 userName = userJson.getString("userName");
			 email = userJson.getString("email");
			 password = userJson.getString("password");
			 address = userJson.getString("address");
			 phoneNumber = userJson.getString("mobileNumber");
			 country = userJson.getString("country");
			 long userId = 0;
			//String userName = userJson.getString("userName");
			 
			 Class.forName("com.mysql.jdbc.Driver");  
				con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ats","root","");  
				//here sonoo is database name, root is username and password  
				//Statement stmt=con.createStatement();
				 String getQuery = " select max(userId) from users";
				 PreparedStatement pstmt = con.prepareStatement(getQuery);
				 ResultSet rs = pstmt.executeQuery();
				 
				 while(rs.next()){
					 userId = rs.getLong(1) + 1; 
				 }
				
				 String query = " insert into users (userid,userName, password, email, address, mobilenumber,country)"
					        + " values (?, ?, ?, ?, ?,?,?)";
				 PreparedStatement preparedStmt = con.prepareStatement(query);
				  preparedStmt.setLong(1, userId);
			      preparedStmt.setString (2, userName);
			      preparedStmt.setString   (3, password);
			      preparedStmt.setString(4, email);
			      preparedStmt.setString (5, address);
			      preparedStmt.setString (6, phoneNumber);
			      preparedStmt.setString (7, country);
			      
			     preparedStmt.execute();
			    
			    	userJson.put("isRegistered", true);
			    	userJson.put("message", "User registered successfully");
			
			    	
			    
			 
		} catch (Exception e) {
			try {
				userJson.put("isRegistered", false);
				userJson.put("message", "User registration failed");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    	
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		response = userJson.toString();
		
		return response;
		
	}
	
	public static String getUserRoles(String userId){
		
		StringBuilder responseStr = new StringBuilder();
		String resp = "";
		Connection con= null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String rolesQuery = "select * from ats.user_role_journal,ats.roles where user_role_journal.roleid = roles.roleID and user_role_journal.userId = ? and user_role_journal.status = 'APPROVED'";
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			con = DriverManager.getConnection(  
			"jdbc:mysql://localhost:3306/ats","root",""); 
			
			pstmt = con.prepareStatement(rolesQuery);
			pstmt.setString(1,userId);
			rs = pstmt.executeQuery();
			while(rs.next()){
				//responseStr.append(rs.getString("roleName")).append(",");
				resp = resp + rs.getString("roleName")+",";
			}
			
		}catch(Exception e){ 
			System.out.println("Exception Occurred in getUserRoles :: "+e);
		} 
		finally{
			try {
				if(con != null)
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} 
		}
		//System.out.println(" here :: "+responseStr.toString());
				
		//resp = responseStr.toString();
		
		if(resp != null && resp != ""){
		resp = resp.substring(0, resp.length()-1);
		System.out.println("responseStr.toString() :::: "+resp);
		}
		return resp;
		
		
	}
	
public static String updateUserProfile(String userString){

JSONObject response = new JSONObject();
JSONObject userJson = new JSONObject();
JSONObject rolesJson = new JSONObject();
JSONObject roleJson = new JSONObject();
JSONArray interestsJArray = null;
JSONArray rolesJArray = new JSONArray();
String userName = null;
String email = 	null;
String password =null;
String address = null;
String phoneNumber =null;
String country = null;
String aboutMe = null;
Connection con= null;
String interests = "";
long userId = 0;
String errorStr = "";
try {
	//{"user":{"name":"Prathap B","email":"bnprathap39@gmail.com","mobileNumber":"9538362426","country":"India","address":"Bangalore - 36","aboutMe":"This is Prathap B N","roles":[""],
	//"interests":[{"subject":{"name":"Computer Science","id":1},"journal":{"id":2,"name":"ComputerScience Journal"},"roles":[{"name":"Editor","id":"111"},{"name":"Reviewer","id":"112"}],"action":""}]}}
	try{
	userJson = (new JSONObject(userString)).getJSONObject("user");
	 userName =(String) CommonUtil.getKeyValue(userJson,"name");
	 email = (String) CommonUtil.getKeyValue(userJson,"email");
	 address = (String) CommonUtil.getKeyValue(userJson,"address");
	 phoneNumber = (String) CommonUtil.getKeyValue(userJson,"mobileNumber");
	 country = (String) CommonUtil.getKeyValue(userJson,"country");
	 aboutMe = (String) CommonUtil.getKeyValue(userJson,"aboutMe");
	 userId = Integer.valueOf((String)(CommonUtil.getKeyValue(userJson,"userId")));
	 
	
	 Class.forName("com.mysql.jdbc.Driver");  
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ats","root","");  
		con.setAutoCommit(false);
		 String userQuery = " update users set userName = ? , address = ? ,mobileNumber = ? ,aboutMe = ? , country = ? where userId = ?";
		 PreparedStatement preparedStmt = con.prepareStatement(userQuery);
		  preparedStmt.setString(1, userName);
	      preparedStmt.setString (2, address);
	      preparedStmt.setString   (3, phoneNumber);
	      preparedStmt.setString(4, aboutMe);
	      preparedStmt.setString (5, country);
	      preparedStmt.setLong (6, userId);
	      
	      preparedStmt.execute();
	}catch(Exception e){
		System.out.println("Exception"+e);
		errorStr = " Error while updating user";
	}
	

	interestsJArray = (JSONArray) CommonUtil.getKeyValue(userJson,"interests"); 
// interestsJArray = new JSONArray(interests);
 if(null != interestsJArray){
	try{ 
	 for(int i=0;i < interestsJArray.length(); i++){
		 String roleQuery = "insert into user_role_journal(id,userid,roleid,journalid,status) values(?,?,?,?,?)";
		 rolesJson =(JSONObject) interestsJArray.get(i);
		 long journalId = (Integer)((JSONObject)rolesJson.get("journal")).get("id");
		 rolesJArray = (JSONArray)rolesJson.get("roles");
		 long id = CommonUtil.getNextUserRoleId();
		 for(int j=0;j < rolesJArray.length(); j++){
			 roleJson = (JSONObject) rolesJArray.get(j);
			long roleId = Long.valueOf((String)roleJson.get("id"));
			id = id + 1;
			PreparedStatement preparedStmt = con.prepareStatement(roleQuery);
			preparedStmt.setLong(1, id);
			preparedStmt.setLong(2, userId);
			preparedStmt.setLong(3,roleId);
			preparedStmt.setLong(4,journalId);
			preparedStmt.setString(5, "PENDING");
			preparedStmt.execute();
		 }
	 }
	}catch(Exception e){
		System.out.println("Exception Occured"+e);
		errorStr = errorStr + ":: Exception Occured While updating roles";
	}
	 
 }
 if(!(errorStr.length() > 0)){
	 con.commit();
	 response.put("success", true);
	 response.put("message", "User profile updated successfully");	 
 }else{
	 con.rollback();
	 response.put("success", false);
	 response.put("message", "User profile updation failed");	
 }
	    
	 
} catch (Exception e) {
	try {
		if(null != con)
		con.rollback();
		response.put("success", false);
		response.put("message", "User profile updation failed");
	} catch (Exception e1) {
		System.out.println("Exception "+e1);
	}
	
	// TODO Auto-generated catch block
	e.printStackTrace();
}
return response.toString();
}

public static String getUserNameById(long id){
	String name = "";
	Connection con= null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String query = "select userName from users where userId = ?";
	try{
		con = ConnectionUtil.getDBConnection();
		pstmt = con.prepareStatement(query);
		pstmt.setLong(1,id);
		rs = pstmt.executeQuery();
		while(rs.next()){
			name = rs.getString(1);
		}
	}catch(Exception e){
		System.out.println("Exception ::"+e);
	}
	
	return name;
}
	
public static void main(String[] args) {
		
	System.out.println(getUserNameById(123));
		
		
	}
}
