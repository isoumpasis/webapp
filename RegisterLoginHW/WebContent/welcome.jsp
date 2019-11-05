<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="com.servlets.*" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Welcome</title>
</head>
<body>
	<%
		User user = (User)session.getAttribute("user");
		
		String username = user.getUsername();
		String email = user.getEmail();
		String password = user.getPassword();
	%>
	<h2>Welcome <%= username %>!</h2>
	<h3>Have a look at your account information:</h3>
	
	<ul>
	    <li>My Username: "<%= username %>"</li>
	    <li>My Email: "<%= email %>"</li>
	    <li>My Password: "<%= password %>"</li>
   	</ul>
	
	<a href="index.html">Back Home</a>
</body>
</html>