package com.articleTracking.controllers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import com.article.util.ArticleUtil;
import com.sun.org.apache.xml.internal.security.utils.Base64;
/**
 * Servlet implementation class ArticleSubmissionServlet
 */
@WebServlet("/ArticleSubmissionServlet")
public class ArticleSubmissionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String UPLOAD_DIRECTORY = "upload";
	 
    // upload settings
    private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ArticleSubmissionServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (!ServletFileUpload.isMultipartContent(request)) {
            // if not, we stop here
            PrintWriter writer = response.getWriter();
            writer.println("Error: Form must has enctype=multipart/form-data.");
            writer.flush();
            return;
        }
		
		String fileName = "";
		String articleType = "";
		String correspondingAuthor = "";
		String authorEmail = "";
		String articleSubject = "";
		String co_author = "";
		String co_authorEmail = "";
		String articleAbstract = "";
		String keywords = "";
		String country = "";
		String address = "";
		String methodType = "";
		String fileExtension = "";
		String fileContent = "";
		InputStream iStream = null;
		ByteArrayOutputStream oStream = null;
		byte[] bytesData = null;
		ArticleUtil articleUtil = new ArticleUtil();
		
        // configures upload settings
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // sets memory threshold - beyond which files are stored in disk
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        // sets temporary location to store files
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
 
        ServletFileUpload upload = new ServletFileUpload(factory);
         
        // sets maximum size of upload file
        upload.setFileSizeMax(MAX_FILE_SIZE);
         
        // sets maximum size of request (include file + form data)
        upload.setSizeMax(MAX_REQUEST_SIZE);
 
 
        try {
            // parses the request's content to extract file data
            @SuppressWarnings("unchecked")
            List<FileItem> formItems = upload.parseRequest(request);
 
            if (formItems != null && formItems.size() > 0) {
                // iterates over form's fields
                for (FileItem item : formItems) {
                    // processes only fields that are not form fields
                    if (!item.isFormField()) {
                   
                        fileName = item.getName();
                        fileName = fileName.substring(fileName.lastIndexOf("\\")+1);
                    	fileExtension = FilenameUtils.getExtension(fileName);
                    	long sizeInBytes = item.getSize();
                    	System.out.println("sizeInBytes::: "+sizeInBytes+"   "+item.get());
                    	iStream = item.getInputStream();
                    	System.out.println("iStream data :: "+iStream.toString());
                    	bytesData = IOUtils.toByteArray(iStream);
                    	//IOUtils.copy(iStream, oStream);
                        
                     }else{
                    	 
                    	 if(item.getFieldName().equalsIgnoreCase("articleType")){
                    		 articleType = item.getString();
                    	 }
                    	 if(item.getFieldName().equalsIgnoreCase("correspondingAuthor")){
                    		 correspondingAuthor = item.getString();
                    	 }
                    	 if(item.getFieldName().equalsIgnoreCase("authorEmail")){
                    		 authorEmail = item.getString();
                    	 }
                    	 if(item.getFieldName().equalsIgnoreCase("articleSubject")){
                    		 articleSubject = item.getString();
                    	 }
                    	 if(item.getFieldName().equalsIgnoreCase("co-author")){
                    		 co_author = item.getString();
                    	 }
                    	 if(item.getFieldName().equalsIgnoreCase("co-authorEmail")){
                    		 co_authorEmail = item.getString();
                    	 }
                    	 if(item.getFieldName().equalsIgnoreCase("abstract")){
                    		 articleAbstract = item.getString();
                    	 }
                    	 if(item.getFieldName().equalsIgnoreCase("keyWords")){
                    		 keywords = item.getString();
                    	 }
                    	 if(item.getFieldName().equalsIgnoreCase("country")){
                    		 country = item.getString();
                    	 }
                    	 if(item.getFieldName().equalsIgnoreCase("address")){
                    		 address = item.getString();
                    	 }
                    	 if(item.getFieldName().equalsIgnoreCase("methodType")){
                    		 methodType = item.getString();
                    	 }
                    	 
                    }
                }
            }
            fileContent = Base64.encode(bytesData);
            System.out.println("fileContent:::"+fileContent);
           String responseStr =  articleUtil.submitArticle(fileName,articleType,correspondingAuthor,authorEmail,articleSubject,co_author,
        			co_authorEmail,articleAbstract,keywords,country,address,methodType,fileExtension,fileContent);
           System.out.println("Article Submission response :::"+responseStr);
        } catch (Exception ex) {
        	System.out.println(ex);
            request.setAttribute("message",
                    "There was an error: " + ex.getMessage());
        }
	}

}
