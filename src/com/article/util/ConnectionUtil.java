package com.article.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectionUtil {

	
	public static Connection getDBConnection(){
		Connection con = null;
		try{
		Class.forName("com.mysql.jdbc.Driver");  
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ats","root",""); 
		}catch(Exception e){
			
		}
		return con;
	}
	
	public static void closeQuitly(Connection con,PreparedStatement pstmt,ResultSet rs){
		try {
			if(con != null)
			con.close();
		} catch (SQLException e) {
			System.out.println("Exception :::"+e);
		} 
		try {
			if(pstmt != null)
				pstmt.close();
		} catch (SQLException e) {
			System.out.println("Exception :::"+e);
		} 
		try {
			if(rs != null)
			rs.close();
		} catch (SQLException e) {
			System.out.println("Exception :::"+e);
		} 
	
	
	}
	
	
	
}
