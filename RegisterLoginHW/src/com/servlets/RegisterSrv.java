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
 * Servlet implementation class RegisterSrv
 */
//@WebServlet("/RegisterSrv")
public class RegisterSrv extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
		
		// Get all url request parameters
		String username = request.getParameter("username");
		String email = request.getParameter("email");
		String password = request.getParameter("password");

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

		// Insert user data to MySQL Table users_db
		try {
			stmt =  con.createStatement();

//stmt.executeUpdate("delete from users_db"); //DEBUG delete db data
			
			if(!checkDataValidity(request, response, stmt)) {
				out.println("Invalid Data<br>");
				out.println("<br><form action=\"register.html\">" + 
						"<input type=\"submit\" value=\"Try Again\" />" + 
						"</form></body></html>");
				//close db environment
				return;
			}
			//Data is Valid
			stmt.executeUpdate("insert into users_db (username,email,password) values('"+username+"','"+email+"','"+password+"')");

			// Make a new user and redirect to welcome
			User newUser = new User(username, email, password);
			
			HttpSession session = request.getSession();
			session.setAttribute("user", newUser);
			
			response.sendRedirect("welcome.jsp");
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// Clean-up db environment
	    try {
			stmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	    
	}

	private boolean checkDataValidity(HttpServletRequest request, HttpServletResponse response, Statement stmt) throws IOException {
		PrintWriter out = response.getWriter();
		
		// Get all url request parameters
		String username = request.getParameter("username");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		
		//blank data problem
		if(username.isBlank() || email.isBlank() || password.isBlank()) {
			//You failed Try again (refresh page)
			out.println("<html><body>Please fill ALL the form to register. Try again!<br>");
			return false;
		}
		
		//already exist username problem
		try {
			ResultSet rs = stmt.executeQuery("select username, email from users_db where username = '"+username+"' OR email = '"+email+"'");
			if(rs.next()) {
				out.println("<html><body>Username or email already exists. Try another one.<br>");
				return false;
			}
			rs.close(); //clean-up
		} catch (SQLException e) {
			e.printStackTrace();
		}
		/*
		//already exist email problem
		try {
			ResultSet rs = stmt.executeQuery("select username from users_db where username = '"+username+"'");
			if(rs.next()) {
				out.println("Username already exists. Try another one.")
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		*/
		return true;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
