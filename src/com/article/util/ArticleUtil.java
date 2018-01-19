package com.article.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import com.articleTracking.email.util.EmailUtil;

public class ArticleUtil {
	
	
	public String submitArticle(String fileName,String articleType,String correspondingAuthor,String authorEmail,String articleSubject,String co_author,
			String co_authorEmail,String articleAbstract,String keywords,String country,String address,String methodType,String fileExtension,String fileContent){
		
		String responseStr = "";
		StringBuilder  articleInsertQuery = new StringBuilder ();
		long articleId = 0;
		long journalId = 0;
		long editorialId = 0;
		long reviewerId = 0;
		 try{
		 Connection con = ConnectionUtil.getDBConnection();
		 String getQuery = " select max(articleId) from article";
		 PreparedStatement pstmt = con.prepareStatement(getQuery);
		 ResultSet rs = pstmt.executeQuery();
		 
		 while(rs.next()){
			 articleId = rs.getLong(1) + 1; 
		 }
		 
		 articleInsertQuery.append("insert into article (articleId,articleName,articleType,abstractData,keywords,author,coAuthor,submittedDate,status,journalId,lastUpdatedDate,editorId,lastUpdatedBy,subjectId) "
		 		+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		 PreparedStatement preparedStmt = con.prepareStatement(articleInsertQuery.toString());
		//articleId,articleName,articleType,abstractData,keywords,author,coAuthor,submittedDate,status,journalId,lastUpdatedDate,editorId,lastUpdatedBy,subjectId 
		 preparedStmt.setLong(1,articleId);
		 preparedStmt.setString(2, "article"+articleId);
		 preparedStmt.setString(3,articleType);
		 preparedStmt.setString(4,articleAbstract);
		 preparedStmt.setString(5,keywords);
		 preparedStmt.setString(6,correspondingAuthor);
		 preparedStmt.setString(7,co_author);
		 preparedStmt.setString(8,new Date().toString());
		 preparedStmt.setString(9,"SUBMITTED");
		 preparedStmt.setLong(10,journalId);
		 preparedStmt.setString(11,new Date().toString());
		 preparedStmt.setLong(12,editorialId);
		 preparedStmt.setString(13,new Date().toString());
		// preparedStmt.setLong(13,noOfPages);
		// preparedStmt.setLong(14,reviewerId);
		// preparedStmt.setString(15,fileContent);
		// preparedStmt.setString(16,correspondingAuthor );
		 
		 preparedStmt.execute();
		 
		 EmailUtil.sendEmail("bnprathap39@gmail.com,jayasankar.du@gmail.com,manasag1992@gmail.com", "Article Submission Status", "Article Submitted Successfully");
		 responseStr = "Article Submitted Successfully";
		 }catch(Exception e){
			 System.out.println("Exception Occurred"+e);
			 responseStr = "Exception Occurred while submitting article";
		 }
		return responseStr;
		
	}

}
