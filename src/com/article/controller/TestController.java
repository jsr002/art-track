package com.article.controller;

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
 * Servlet implementation class TestController
 */
@WebServlet("/TestController")
public class TestController extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public TestController() {
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
		if(methodType.equals("registration")){
			responseStr = LoginUtil.registerUser(userString);
		}else if(methodType.equals("login")){
			responseStr = LoginUtil.authenticateUser(userString,request,response);
		} else if(methodType.equals("logout")){
			responseStr = LoginUtil.logoutUser(request,response);
		}else if(methodType.equals("getLoggedinUserModel")){
			HttpSession session = request.getSession();
			responseStr = (String) session.getAttribute("user");
		}else if(methodType.equals("getArticleDataForAuthor")){
			HttpSession session = request.getSession();
			String userData = (String) session.getAttribute("user");
			responseStr = DashboardUtil.getArticleDataForAuthor(userData);
		}else if(methodType.equals("getArticlesForEditor")){
			HttpSession session = request.getSession();
			String userData = (String) session.getAttribute("user");
			responseStr = DashboardUtil.getArticlesForEditor(userData);
		}else if(methodType.equals("getArticlesForReviewer")){
			HttpSession session = request.getSession();
			String userData = (String) session.getAttribute("user");
			responseStr = DashboardUtil.getArticlesForReviewer(userData);
		}else if(methodType.equals("submitArticle")){
			
			
		}else if(methodType.equals("getUsers")){
			responseStr = DashboardUtil.getUserDetails();
		}
	//resp = TestUtils.getData();
	response.getWriter().write(responseStr);
	
	}

}
