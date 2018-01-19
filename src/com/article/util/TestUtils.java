package com.article.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONObject;

public class TestUtils {
	
	
	
	
	
	public static String getData(){
		String response = null;
		Connection con= null;
		JSONObject jsonResponse = new JSONObject();
		JSONArray jArray = new JSONArray();
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			con = DriverManager.getConnection(  
			"jdbc:mysql://localhost:3306/ats","root","");  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement();  
			ResultSet rs=stmt.executeQuery("select * from authors");  
			while(rs.next()){
				jsonResponse = new JSONObject();
				jsonResponse.put("title",rs.getString("title"));
				jsonResponse.put("name",rs.getString("name"));
				jsonResponse.put("email",rs.getString("email"));
				jsonResponse.put("abstract",rs.getString("abstract"));
				jArray.put(jsonResponse);
			}
				
			 
			}catch(Exception e){ System.out.println(e);} 
		finally{
			try {
				if(con != null)
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		
		return jArray.toString();
		
		
	}

}
