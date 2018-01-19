package com.article.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DashboardUtil {
	
	
	
	public static String getArticleDataForAuthor(String userString){
		String responseData = null;
		Connection con= null;
		JSONObject userJson = new JSONObject();
		String dashboardQuery = "select * from article where author = ?"; 

		try {
			userJson = new JSONObject(userString);
		} catch (JSONException e2) {
		}
		JSONObject jsonResponse = new JSONObject();
		JSONObject articleJson = null;
		JSONArray articleArray = new JSONArray();
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{ 
			con = ConnectionUtil.getDBConnection();
			pstmt=con.prepareStatement(dashboardQuery);
			pstmt.setString(1, (userJson.getString("userId") ));
			//pstmt.setString(1,"prathap.bn@gmail.com");
			rs=pstmt.executeQuery();
			
			jsonResponse.put("articles", populateArticleDataFromRS(rs));
			
		}catch(Exception e){
			System.out.println("Exception occurred"+e);
			} 
		finally{
			ConnectionUtil.closeQuitly(con, pstmt, rs);
		}
		
		responseData = jsonResponse.toString();
		
		System.out.println("article responseData for author ::: "+ responseData);
		
		return responseData;
		
	}
	
	
	public static String getArticlesForEditor(String userString){
		String responseData = null;
		Connection con= null;
		JSONObject userJson = new JSONObject();
		String userId = "";
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		String dashboardQuery = " Select * from article where author != ? and editorId = ?";
		try {
			userJson = new JSONObject(userString);
			userId = (userJson.getString("userId"));
		} catch (JSONException e2) {
		}
		JSONObject jsonResponse = new JSONObject();
		JSONObject articleJson = null;
		JSONArray articleArray = new JSONArray();
		try{ 
			con = ConnectionUtil.getDBConnection();
			pstmt=con.prepareStatement(dashboardQuery);
			pstmt.setString(1, userId );
			pstmt.setString(2, userId );
			//pstmt.setString(1,"prathap1.bn@gmail.com");
			rs=pstmt.executeQuery();
			
			jsonResponse.put("articles", populateDataFromRS(rs));
			
		}catch(Exception e){
			System.out.println("Exception ::: "+e);
			} 
		finally{
			ConnectionUtil.closeQuitly(con, pstmt, rs);
		}
		responseData = jsonResponse.toString();
		
		System.out.println("editor article responseData ::: "+ responseData);
		
		return responseData;
		
	}
	
	public static String getArticlesForReviewer(String userString){
		String responseData = null;
		Connection con= null;
		JSONObject userJson = new JSONObject();
		String userId = "";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String dashboardQuery = " Select * from article where author != ? and reviewerId = ?";
		try {
			userJson = new JSONObject(userString);
			userId = (userJson.getString("userId"));
		} catch (JSONException e2) {
		}
		JSONObject jsonResponse = new JSONObject();
		JSONObject articleJson = null;
		JSONArray articleArray = new JSONArray();
		try{ 
			con = ConnectionUtil.getDBConnection();
			pstmt=con.prepareStatement(dashboardQuery);
			pstmt.setString(1, userId );
			pstmt.setString(2, userId );
			//pstmt.setString(1,"prathap1.bn@gmail.com");
			rs = pstmt.executeQuery();
			
			jsonResponse.put("articles", populateDataFromRS(rs));
			
		}catch(Exception e){
			System.out.println("Exception ::"+ e);
			} 
		finally{
			ConnectionUtil.closeQuitly(con, pstmt, rs);
		}
		
		responseData = jsonResponse.toString();
		
		System.out.println("reviewer article responseData ::: "+ responseData);
		
		return responseData;
		
	}
	

public static String getUserDetails(){
		
		String usersString = "";
		Connection con= null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		JSONObject jsonResponse = new JSONObject();
		JSONObject user = null;
		JSONArray userArray = new JSONArray();
		String usersQuery = "select * from users where status = 1";
		try{  
			con = getDBConnection();
			pstmt=con.prepareStatement(usersQuery);
			rs = pstmt.executeQuery();
			while(rs.next()){
				user = new JSONObject();
				 user.put("userName",rs.getString("USERNAME")); 
				 user.put("email",rs.getString("EMAIL")); 
				 user.put("mobileNumner",rs.getString("MOBILENUMBER")); 
				 user.put("country",rs.getString("country")); 
				 user.put("userId",rs.getString("userId")); 
				 user.put("roles", LoginUtil.getUserRoles(rs.getString("userId")));
				 userArray.put(user);
			}
			
			jsonResponse.put("users", userArray);
		}catch(Exception e){ 
			System.out.println("Exception Occurred"+e);
		} 
		finally{
			ConnectionUtil.closeQuitly(con, pstmt, rs);
		}
		return jsonResponse.toString();
	}


public static String submitArticle(HttpServletRequest request){
	
	String responseStr = "";
	String articleData = request.getParameter("articleData");
	String articleId = "";
	String articleName = "";
	String articleType = "";
	String abstractData = "";
	String keywords = "";
	String author = "";
	String coAuthor = "";
	String status = "";
	String journalId = "";
	String editorId = "";
	String numberOfPages = "";
	String reviewerId = "";
	String articleDoc = "";
	String lastUpdatedBy = "";
	
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String articleInsertQuery = "insert into article (articleId,articleName,articleType,abstract,keywords,author,coAuthor,submittedDate,status,journalId,lastUpdatedDate,editorId,numberOfPages,reviewerId,articleDocument,lastUpdatedBy)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	try{
		
		JSONObject	articleJson = new JSONObject(articleData);
		articleId = articleJson.getString("articleId");
		articleName = articleJson.getString("articleName");
		articleType = articleJson.getString("articleType");
		abstractData = articleJson.getString("abstractData");
		keywords = articleJson.getString("keywords");
		author = articleJson.getString("author");
		coAuthor = articleJson.getString("coAuthor");
		status = articleJson.getString("status");
		editorId = articleJson.getString("editorId");
		numberOfPages = articleJson.getString("numberOfPages");
		reviewerId = articleJson.getString("reviewerId");
		articleDoc = articleJson.getString("articleDoc");
		lastUpdatedBy = articleJson.getString("lastUpdatedBy");
		journalId = articleJson.getString("journalId");

		con = getDBConnection();

		pstmt=con.prepareStatement(articleInsertQuery);
		 pstmt.setString(1,articleId);
		 pstmt.setString(2,articleName);
		 pstmt.setString(3,articleType);
		 pstmt.setString(4,abstractData);
		 pstmt.setString(5,keywords);
		 pstmt.setString(6,author);
		 pstmt.setString(7,coAuthor);
		 pstmt.setString(8,(new Date()).toString());
		 pstmt.setString(9,status);
		 pstmt.setString(10,journalId);
		 pstmt.setString(11,(new Date()).toString());
		 pstmt.setString(12,editorId);
		 pstmt.setString(13,numberOfPages);
		 pstmt.setString(14,reviewerId);
		 pstmt.setString(15,articleDoc);
		 pstmt.setString(16,lastUpdatedBy);
		 
		 pstmt.executeUpdate();
		 
	}catch(Exception e){
		System.out.println("Exception::"+e);
	}finally{
		ConnectionUtil.closeQuitly(con, pstmt, rs);
	}
	return responseStr;
}

public static Connection getDBConnection(){
	Connection con = null;
	try{
	Class.forName("com.mysql.jdbc.Driver");  
	con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ats","root",""); 
	}catch(Exception e){
	System.out.println(" Exception ::"+e);	
	}
	return con;
}

public static JSONArray populateArticleDataFromRS(ResultSet rs){
	JSONArray articleArray = new JSONArray();
	JSONObject articleJson = null;
	try{
	while(rs.next()){
		articleJson = new JSONObject();
		articleJson.put("articleId", rs.getString("articleId"));
		articleJson.put("articleName", rs.getString("articleName"));
		articleJson.put("articleType", rs.getString("articleType"));
		articleJson.put("abstract", rs.getString("abstractData"));
		articleJson.put("keywords", rs.getString("keywords"));
		articleJson.put("author", rs.getString("author"));
		articleJson.put("authorName", LoginUtil.getUserNameById(Integer.valueOf(rs.getString("author"))));
		articleJson.put("coAuthor", rs.getString("coAuthor"));
		articleJson.put("submittedDate", rs.getString("submittedDate"));
		//articleJson.put("articleStatus", rs.getString("status"));
		articleJson.put("journalId", rs.getString("journalId"));
		articleJson.put("journalName",getJournalNamebyiD(Integer.valueOf(rs.getString("journalId"))));
		articleJson.put("lastUpdatedDate", rs.getString("lastUpdatedDate"));
		articleJson.put("editorId", rs.getString("editorId"));
		articleJson.put("editorName", LoginUtil.getUserNameById(Integer.valueOf(rs.getString("editorId"))));
		articleJson.put("status", rs.getString("status"));
		articleJson.put("reviewerName", getArticleReviewerNames(Integer.valueOf(rs.getString("articleId"))));
		articleJson.put("lastUpdatedBy", rs.getString("lastUpdatedBy"));
		articleJson.put("subjectid", rs.getString("subjectid"));
		Map<String,String> reviewDetails = DashboardUtil.getReviewDetails(Integer.valueOf(rs.getString("articleId")));
		if(null != reviewDetails && reviewDetails.size() > 0){
		articleJson.put("reviewComments", reviewDetails.get("reviewComments") != null ? reviewDetails.get("reviewComments") : "");	
		articleJson.put("editorComments", reviewDetails.get("editorComments") != null ? reviewDetails.get("editorComments") : "");
		}
		articleArray.put(articleJson);
	}
	}catch(Exception e){
		System.out.println("Exception Occured"+e);
	}

	return articleArray;
	}



	public static JSONArray populateDataFromRS(ResultSet rs){
	JSONArray articleArray = new JSONArray();
	JSONObject articleJson = null;
	try{
	while(rs.next()){
		articleJson = new JSONObject();
		articleJson.put("articleId", rs.getString("articleId"));
		articleJson.put("articleName", rs.getString("articleName"));
		articleJson.put("articleType", rs.getString("articleType"));
		articleJson.put("abstract", rs.getString("abstractData"));
		articleJson.put("keywords", rs.getString("keywords"));
		articleJson.put("author", rs.getString("author"));
		articleJson.put("authorName", LoginUtil.getUserNameById(Integer.valueOf(rs.getString("author"))));
		articleJson.put("coAuthor", rs.getString("coAuthor"));
		articleJson.put("submittedDate", rs.getString("submittedDate"));
		//articleJson.put("articleStatus", rs.getString("status"));
		articleJson.put("journalId", rs.getString("journalId"));
		articleJson.put("journalName",getJournalNamebyiD(Integer.valueOf(rs.getString("journalId"))));
		articleJson.put("lastUpdatedDate", rs.getString("lastUpdatedDate"));
		articleJson.put("editorId", rs.getString("editorId"));
		articleJson.put("editorName", LoginUtil.getUserNameById(Integer.valueOf(rs.getString("editorId"))));
		articleJson.put("status", rs.getString("status"));
		articleJson.put("reviewerName", getArticleReviewerNames(Integer.valueOf(rs.getString("articleId"))));
		articleJson.put("lastUpdatedBy", rs.getString("lastUpdatedBy"));
		
		articleArray.put(articleJson);
	}
	}catch(Exception e){
		System.out.println("Exception Occured"+e);
	}

	return articleArray;
	}
	// Save Journal Details
	
	public static String saveJournal(String journalData){
		JSONObject journalResponse = new JSONObject();
		JSONObject inputJournal = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		long id = 0;
		long subjectId = 0;
		String name = "";
		String jUrl = "";
		String jShortName = "";
		String query = "";
		String classifications = "";
		try{
		if(null != journalData){
			try {
				inputJournal = new JSONObject(journalData);
			} catch (JSONException e) {
				journalResponse.put("success", false);
				journalResponse.put("message", "Invalid Input Json");
			}
			if(null != inputJournal){
				id = inputJournal.getLong("id");
				name = (String) CommonUtil.getKeyValue(inputJournal, "name");
				jUrl = (String) CommonUtil.getKeyValue(inputJournal, "url");
				//jShortName = inputJournal.getString("shortName");
				JSONObject subject = (JSONObject) CommonUtil.getKeyValue(inputJournal, "subject");
				if(subject !=null) subjectId = Long.valueOf(CommonUtil.getKeyValue(subject, "id")+"");
				classifications  = inputJournal.getString("classifications");
			}
			con = ConnectionUtil.getDBConnection();
			if(id == 0){
				
				 String getQuery = " select max(id) from journal";
				  pstmt = con.prepareStatement(getQuery);
				  rs = pstmt.executeQuery();
				  while(rs.next()){
						 id = rs.getLong(1) + 1; 
					 }
			
				 query = "insert into journal (id,name,shortName,subjectId,url,classifications) values (?,?,?,?,?,?)";
				 pstmt = con.prepareStatement(query);
				 pstmt.setLong(1,id);					
				 pstmt.setString(2, name);
				 pstmt.setString(3, jShortName);
				 pstmt.setLong(4, subjectId);
				 pstmt.setString(5, jUrl);
				 pstmt.setString(6, classifications);
				 pstmt.execute();
			}else{
				 query = "update journal set name = ?,shortName = ? ,subjectId = ?,url = ? , classifications = ? where id= ?";	
				 pstmt = con.prepareStatement(query);
				 pstmt.setString(1, name);
				  pstmt.setString(2, jShortName);
				 pstmt.setLong(3,subjectId);
				 pstmt.setString(4, jUrl);
				 pstmt.setString(5, classifications);
				 pstmt.setLong(6,id);
				 pstmt.executeUpdate();
			}

			//responseStr = "Journal saved successfully";
			journalResponse.put("success", true);
			journalResponse.put("message", "Journal saved successfully");
		}
		}catch(Exception e){
			System.out.println("Exception  "+e);
			try{
				journalResponse.put("success", true);
				journalResponse.put("message", "Error while saving Journal");
			}catch(Exception ex){
				
			}
			//responseStr = "Error occurred while saving Journal details";
		}finally{
			ConnectionUtil.closeQuitly(con, pstmt, rs);
		}
		return journalResponse.toString();
	}
	
	
	//Save Subject Details
	public static String saveSubject(String subjectData){
		JSONObject responseStr = new JSONObject();
		JSONObject subjectJson = null;
		JSONObject inputSubject = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		long id = 0;
		String name = "";
		String query = "";
		try{
		if(null != subjectData){
			try {
				subjectJson = new JSONObject(subjectData);
				String subjectStr = subjectJson.getString("subject");
				inputSubject = new JSONObject(subjectStr);
			} catch (JSONException e) {
				responseStr.put("success", false);
				responseStr.put("message", "Invalid Input Json");
			}
			if(null != inputSubject){
				id = inputSubject.getLong("id");
				name = inputSubject.getString("name");
			}
			con = ConnectionUtil.getDBConnection();
			if(id == 0){
				
				 String getQuery = " select max(id) from subject";
				  pstmt = con.prepareStatement(getQuery);
				  rs = pstmt.executeQuery();
				  while(rs.next()){
						 id = rs.getLong(1) + 1; 
					 }
			
				 query = "insert into subject (name,id) values (?,?)";
				 pstmt = con.prepareStatement(query);
				 pstmt.setString(1, name);
				 pstmt.setLong(2,id);
				 pstmt.execute();
			}else{
				 query = "update subject set name = ? where id= ?";	
				 pstmt = con.prepareStatement(query);
				 pstmt.setString(1, name);
				 pstmt.setLong(2,id);
				 pstmt.executeUpdate();
			}
			responseStr.put("success", true);
			responseStr.put("message", "Subject saved successfully");
		}
		}catch(Exception e){
			System.out.println("Exception  "+e);
			try{
				responseStr.put("success", false);
				responseStr.put("message", "Error occurred while saving Subject details");
			}catch(Exception ex){
				
			}
		}finally{
			ConnectionUtil.closeQuitly(con, pstmt, rs);
		}
		return responseStr.toString();
	}
	
	public static String getSubjects(){
		String responseData = null;
		Connection con= null;
		JSONObject userJson = new JSONObject();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String subjectsQuery = " Select * from subject";
		
		JSONObject jsonResponse = new JSONObject();
		JSONObject subjectJson = null;
		JSONArray subjectArray = new JSONArray();
		try{ 
			con = getDBConnection();
			pstmt=con.prepareStatement(subjectsQuery);
			rs=pstmt.executeQuery();
			while(rs.next()){
				 subjectJson = new JSONObject();
				subjectJson.put("id", rs.getLong("id"));
				subjectJson.put("name", rs.getString("name"));
				subjectArray.put(subjectJson);
			}
			jsonResponse.put("subjects", subjectArray);
			
		}catch(Exception e){
			System.out.println("Exception occurred"+e);
			} 
		finally{
			ConnectionUtil.closeQuitly(con, pstmt, rs);
		}
		responseData = jsonResponse.toString();
		System.out.println("Subjects response ::: "+ responseData);
		return responseData;
	}
	
	public static String getJournals(){
		String responseData = null;
		Connection con= null;
		JSONObject userJson = new JSONObject();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String journalQuery = " Select * from journal";
		
		JSONObject jsonResponse = new JSONObject();
		JSONObject journalJson = null;
		JSONArray journalArray = new JSONArray();
		try{ 
			con = ConnectionUtil.getDBConnection();
			pstmt=con.prepareStatement(journalQuery);
			rs=pstmt.executeQuery();
			//{"name": "Clinical", "id": "111", "url": "www.cancer.com", "subject" :{"name" : "Clinical","id": "111"}, "classifications" :"clin1, clin2, clin3"}
			while(rs.next()){
				journalJson = new JSONObject();
				journalJson.put("id", rs.getLong("id"));
				journalJson.put("name", rs.getString("name"));
				journalJson.put("url", rs.getString("url"));
				journalJson.put("shortName", rs.getString("shortName"));
				journalJson.put("classifications", rs.getString("classifications"));
				journalJson.put("subject", getSubject(Long.parseLong(rs.getString("subjectId"))));
				journalArray.put(journalJson);
			}
			jsonResponse.put("journals", journalArray);
			
		}catch(Exception e){
			System.out.println("Exception occurred"+e);
			} 
		finally{
			ConnectionUtil.closeQuitly(con, pstmt, rs);
		}
		responseData = jsonResponse.toString();
		System.out.println("journals response ::: "+ responseData);
		return responseData;
	}
	
	
	//getJournalsBySubject
	
	public static String getJournalsBySubject(long subjectId){
		String responseData = null;
		Connection con= null;
		JSONObject userJson = new JSONObject();
		String journalQuery = " Select * from journal where subjectId = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		JSONObject jsonResponse = new JSONObject();
		JSONObject journalJson = null;
		JSONArray journalArray = new JSONArray();
		try{ 
			con = ConnectionUtil.getDBConnection();
			pstmt=con.prepareStatement(journalQuery);
			pstmt.setLong(1, subjectId);
			rs=pstmt.executeQuery();
			//{"name": "Clinical", "id": "111", "url": "www.cancer.com", "subject" :{"name" : "Clinical","id": "111"}, "classifications" :"clin1, clin2, clin3"}
			while(rs.next()){
				journalJson = new JSONObject();
				journalJson.put("id", rs.getLong("id"));
				journalJson.put("name", rs.getString("name"));
				journalJson.put("url", rs.getString("url"));
				journalJson.put("shortName", rs.getString("shortName"));
				journalJson.put("classifications", rs.getString("classifications"));
				journalJson.put("subject", getSubject(Long.parseLong(rs.getString("subjectId"))));
				journalArray.put(journalJson);
			}
			jsonResponse.put("journals", journalArray);
			
		}catch(Exception e){
			System.out.println("Exception occurred"+e);
			} 
		finally{
			ConnectionUtil.closeQuitly(con, pstmt, rs);
		}
		responseData = jsonResponse.toString();
		System.out.println("journals response ::: "+ responseData);
		return responseData;
	}
	
	
	public static JSONObject getSubject(long id){
		
		JSONObject subjectJson = null;
		Connection con= null;
		String subjectsQuery = " Select * from subject where id = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{ 
			con = ConnectionUtil.getDBConnection();
			pstmt=con.prepareStatement(subjectsQuery);
			pstmt.setLong(1,id);
			rs = pstmt.executeQuery();
			while(rs.next()){
				subjectJson = new JSONObject();
				subjectJson.put("id", rs.getLong("id"));
				subjectJson.put("name", rs.getString("name"));
			}
			
		}catch(Exception e){
			System.out.println("Exception occurred"+e);
			} 
		finally{
			ConnectionUtil.closeQuitly(con, pstmt, rs);
		}
		System.out.println("Subjects response ::: "+ subjectJson.toString());
		return subjectJson;
	}
	
	
	public static boolean isSuperAdmin(String userStr){
		boolean isSuperAdmin = true;;
		/*
		boolean isSuperAdmin = false;
		JSONObject userJson = null;
		String roles = "";
		String[] roleArr = null;
		
		try{
		if(null != userStr){
			userJson = new JSONObject(userStr);
			roles = userJson.getString("roles");
			roleArr = roles.split(",");
		}
		if(null != roleArr){
			for(int i =0; i<roleArr.length;i++){
				if(roleArr[i].equalsIgnoreCase("SuperAdmin")){
					isSuperAdmin = true;
					break;
				}
			}
		}
		}catch(Exception e){
		System.out.println("Exception Occured:::"+e);	
		}*/
		return isSuperAdmin;
	}
	
public static JSONObject deleteSubject(long id,String userStr){
		
		JSONObject response = new JSONObject();;
		Connection con= null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String subjectsQuery = " delete from subject where id = ?";
		JSONObject userJson = null;
		String roles = "";
		
		try{ 
			if(isSuperAdmin(userStr)){
			con = ConnectionUtil.getDBConnection();
			pstmt=con.prepareStatement(subjectsQuery);
			pstmt.setLong(1,id);
			rs=pstmt.executeQuery();
			response.put("success", true);
			response.put("message", "Subject deleted successfully");
			
			}else{
				response.put("success", false);
				response.put("message", "User not entitled to delete subject");
			}
		}catch(Exception e){
			System.out.println("Exception occurred"+e);
			try{
				response.put("success", false);
				response.put("message", "Error while deleting subject");
			}catch(Exception ex){
				
			}
		} 
		finally{
			ConnectionUtil.closeQuitly(con, pstmt, rs);
		}
		System.out.println("Subjects response ::: "+ response.toString());
		return response;
	}
	
public static JSONObject deleteJournal(long id,String userStr){
	
	JSONObject response = new JSONObject();;
	Connection con= null;
	String journalQuery = " delete from journal where id = ?";
	JSONObject userJson = null;
	String roles = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	try{ 
		if(isSuperAdmin(userStr)){
		con = ConnectionUtil.getDBConnection();
		pstmt=con.prepareStatement(journalQuery);
		pstmt.setLong(1,id);
		rs=pstmt.executeQuery();
		response.put("success", true);
		response.put("message", "Journal deleted successfully");
		
		}else{
			response.put("success", false);
			response.put("message", "User not entitled to delete journal");	
		}
	}catch(Exception e){
		System.out.println("Exception occurred"+e);
		try{
			response.put("success", false);
			response.put("message", "Error while deleting Journal");
		}catch(Exception ex){
			
		}
	} 
	finally{
		ConnectionUtil.closeQuitly(con, pstmt, rs);
	}
	System.out.println("Subjects response ::: "+ response.toString());
	return response;
}


public static String getRoles(){
	String responseData = null;
	Connection con= null;
	JSONObject userJson = new JSONObject();
	String rolesQuery = " Select * from roles";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	JSONObject rolesResponse = new JSONObject();
	JSONObject rolesJson = null;
	JSONArray rolesArray = new JSONArray();
	try{ 
		con = ConnectionUtil.getDBConnection();
		pstmt=con.prepareStatement(rolesQuery);
		rs=pstmt.executeQuery();
		//{"name": "Clinical", "id": "111", "url": "www.cancer.com", "subject" :{"name" : "Clinical","id": "111"}, "classifications" :"clin1, clin2, clin3"}
		while(rs.next()){
			rolesJson = new JSONObject();
			rolesJson.put("id", rs.getLong("id"));
			rolesJson.put("name", rs.getString("name"));
			rolesJson.put("url", rs.getString("url"));
			rolesJson.put("shortName", rs.getString("shortName"));
			rolesJson.put("classifications", rs.getString("classifications"));
			rolesJson.put("subject", getSubject(Long.parseLong(rs.getString("subjectId"))));
			rolesArray.put(rolesJson);
		}
		rolesResponse.put("journals", rolesArray);
		
	}catch(Exception e){
		System.out.println("Exception occurred"+e);
		} 
	finally{
		ConnectionUtil.closeQuitly(con, pstmt, rs);
	}
	responseData = rolesResponse.toString();
	System.out.println("journals response ::: "+ responseData);
	return responseData;
	}
	
	public static String getArticleReviewerNames(long articleId){
		String reviewers = "";

		Connection con= null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String query = "select * from article_review where articleId = ? and roleid = 400";
		try{
			con = ConnectionUtil.getDBConnection();
			pstmt = con.prepareStatement(query);
			pstmt.setLong(1,articleId);
			rs = pstmt.executeQuery();
			while(rs.next()){
				if(!reviewers.equals("")){
				reviewers = reviewers + "," +LoginUtil.getUserNameById(Integer.valueOf(rs.getString("userId")));
				}else{
					reviewers = LoginUtil.getUserNameById(Integer.valueOf(rs.getString("userId")));
				}
			}
		}catch(Exception e){
			System.out.println("Exception ::"+e);
		}finally{
			ConnectionUtil.closeQuitly(con, pstmt, rs);
		}
		
		return reviewers;
	}
	
	public static String getJournalNamebyiD(long journalId){
		String journalName = "";

		Connection con= null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String query = "select * from journal where id = ?";
		try{
			con = ConnectionUtil.getDBConnection();
			pstmt = con.prepareStatement(query);
			pstmt.setLong(1,journalId);
			rs = pstmt.executeQuery();
			while(rs.next()){
				journalName = rs.getString("NAME");
			}
		}catch(Exception e){
			System.out.println("Exception ::"+e);
		}finally{
			ConnectionUtil.closeQuitly(con, pstmt, rs);
		}
		return journalName;
	}
	
	public static Map<String,String> getReviewDetails(long articleId){
		 Map<String,String> reviewDetailsMap =new HashMap<String,String>();
		Connection con= null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String query = "select * from article_review where articleId = ?";
		try{
			con = ConnectionUtil.getDBConnection();
			pstmt = con.prepareStatement(query);
			pstmt.setLong(1,articleId);
			rs = pstmt.executeQuery();
			while(rs.next()){//rs.getString("comments")
				if(Integer.valueOf(rs.getString("roleId")) == 400){
					if(reviewDetailsMap.containsKey("reviewComments")){
					reviewDetailsMap.put("reviewComments",reviewDetailsMap.get("reviewComments")+"|"+ LoginUtil.getUserNameById(Integer.valueOf(rs.getString("userId")))+"::"+rs.getString("comments"));	
					}else{
					reviewDetailsMap.put("reviewComments", LoginUtil.getUserNameById(Integer.valueOf(rs.getString("userId")))+"::"+rs.getString("comments"));
					}
				}else if(Integer.valueOf(rs.getString("roleId")) == 300){

					if(reviewDetailsMap.containsKey("editorComments")){
					reviewDetailsMap.put("editorComments",reviewDetailsMap.get("editorComments")+"|"+ LoginUtil.getUserNameById(Integer.valueOf(rs.getString("userId")))+"::"+rs.getString("comments"));	
					}else{
					reviewDetailsMap.put("editorComments", LoginUtil.getUserNameById(Integer.valueOf(rs.getString("userId")))+"::"+rs.getString("comments"));
					}
				}
			}
		}catch(Exception e){
			System.out.println("Exception ::"+e);
		}finally{
			ConnectionUtil.closeQuitly(con, pstmt, rs);
		}
		return reviewDetailsMap;
	}
 
	public static String saveArticleUpdated(String article){
		String response = "";
		JSONObject articleJson = null;
		try {
			articleJson = new JSONObject(article);
		} catch (JSONException e) {
			System.out.println("Exception while parsing request json:::" + e);
		}
		
		if(null != articleJson && articleJson.toString() != ""){
			
		}
		
		
		
		return response;
	}
	
	
	public static void main(String[] args) {
		
	System.out.println("getUserDetails:::: " +getUserDetails());
		//getArticleDataForAuthor("");
	//System.out.println(getArticleDataForAuthor(""));
	System.out.println(getJournalsBySubject(2));
		
		
	}

}
