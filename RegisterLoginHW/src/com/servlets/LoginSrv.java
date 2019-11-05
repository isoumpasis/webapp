package com.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class LoginSrv
 */
//@WebServlet("/LoginSrv")
public class LoginSrv extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
		
		// Get all url request parameters
		String username = request.getParameter("username");
		String password = request.getParameter("password");

//DEBUG out.println("LoginSrv");
		
		// Database
		Connection con = null;
		Statement stmt = null;
		
		// Register JDBC driver and open a connection
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con =  DriverManager.getConnection("jdbc:mysql://localhost:3306/registerlogindb?useSSL=false", "root", "root");
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Compare user posted data with MySQL Table users_db
		try {
			stmt =  con.createStatement();
			ResultSet rs = stmt.executeQuery("select username,password,email from users_db where username = '"+username+"'");
			if(rs.next() && (rs.getString("password").compareTo(password) == 0)){ //both username and password must match
				// Welcome user and redirect
				User loginUser = new User(username, rs.getString("email"), password);
				
				HttpSession session = request.getSession();
				session.setAttribute("user", loginUser);
				
				response.sendRedirect("welcome.jsp");
				
				rs.close(); // clean-up
				
			} else {
				//You failed Try again (refresh page)
				out.println("<html><body>Username or Password wrong.");
				out.println("<br><br><br><form action=\"login.html\">" + 
						"<input type=\"submit\" value=\"Try Again\" />" + 
						"</form>");
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//clean-up db environment
		try {
			stmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
