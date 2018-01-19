package com.articleTracking.controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;

import com.article.util.DashboardUtil;
import com.article.util.LoginUtil;

/**
 * Servlet implementation class TrackingControllerServlet
 */
@WebServlet("/TrackingController")
public class TrackingControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public TrackingControllerServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//String firstName = request.getParameter("first_name");
		//String lastName = request.getParameter("lastst_name");
		String resp = null;
		JSONObject jsonResponse = new JSONObject();
		JSONObject user = new JSONObject();
		try {
			jsonResponse.put("isAuthenticated", true);
			user.put("name", "jathin");
			user.put("email", "jathin@gmail.com");
			user.put("phone", "9538362426");
			user.put("Address", "Balendrapalli");
			jsonResponse.put("user", user);
			
		} catch (JSONException e) {
			
		}
		//resp = TestUtils.getData();
		if(1 == 1){
		response.getWriter().write(jsonResponse.toString());
		}else{
		response.getWriter().append("Served at: ").append(request.getContextPath());
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		String methodType = request.getParameter("methodType");
		JSONObject jsonResponse = new JSONObject();
		String responseStr = "";
		String userString = request.getReader().readLine();
		if(methodType.equalsIgnoreCase("registration")){
			responseStr = LoginUtil.registerUser(userString);
		}else if(methodType.equalsIgnoreCase("updateUserProfile")){
			responseStr = LoginUtil.updateUserProfile(userString);
		}else if(methodType.equalsIgnoreCase("login")){
			responseStr = LoginUtil.authenticateUser(userString,request,response);
		} else if(methodType.equalsIgnoreCase("logout")){
			responseStr = LoginUtil.logoutUser(request,response);
		}else if(methodType.equalsIgnoreCase("getLoggedinUserModel")){
			HttpSession session = request.getSession();
			responseStr = (String) session.getAttribute("user");
		}else if(methodType.equalsIgnoreCase("getArticleDataForAuthor")){
			HttpSession session = request.getSession();
			String userData = (String) session.getAttribute("user");
			responseStr = DashboardUtil.getArticleDataForAuthor(userData);
		}else if(methodType.equalsIgnoreCase("getArticlesForEditor")){
			HttpSession session = request.getSession();
			String userData = (String) session.getAttribute("user");
			responseStr = DashboardUtil.getArticlesForEditor(userData);
		}else if(methodType.equalsIgnoreCase("getArticlesForReviewer")){
			HttpSession session = request.getSession();
			String userData = (String) session.getAttribute("user");
			responseStr = DashboardUtil.getArticlesForReviewer(userData);
		}else if(methodType.equalsIgnoreCase("getUsers")){
			responseStr = DashboardUtil.getUserDetails();
		}else if(methodType.equalsIgnoreCase("saveSubject")){
			String subjectData = request.getParameter("data");
			responseStr = DashboardUtil.saveSubject(subjectData);
		}else if(methodType.equalsIgnoreCase("getSubjects")){
			responseStr = DashboardUtil.getSubjects();
		}else if(methodType.equalsIgnoreCase("saveJournal")){
			String journalData = request.getParameter("journal");
			responseStr = DashboardUtil.saveJournal(journalData);
		}else if(methodType.equalsIgnoreCase("getJournals")){
			responseStr = DashboardUtil.getJournals();
		}else if(methodType.equalsIgnoreCase("getJournalsBySubject")){
			long subjectId =Long.valueOf(request.getParameter("subjectId"));
			responseStr = DashboardUtil.getJournalsBySubject(subjectId);
		}else if(methodType.equalsIgnoreCase("getRoles")){
			responseStr = DashboardUtil.getRoles();
		}else if(methodType.equalsIgnoreCase("saveArticle")){
			String articleString = request.getParameter("article");
			System.out.println("request Article::: "+articleString);
			
		}else if(methodType.equalsIgnoreCase("getReviewersForArticle")){
			String articleString = request.getParameter("article");
			System.out.println("request Article::: "+articleString);
			responseStr="{\"reviewers\":{\"id\":123,\"name\":\"Prathap\"}}";
		}
	//resp = TestUtils.getData();
	response.getWriter().write(responseStr);
	
	}

}
