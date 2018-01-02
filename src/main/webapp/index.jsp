<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
	<title>Strona kryta</title>
</head>
<body>
<h2>Hello World!</h2>
<p>User: ${pageContext.request.remoteUser}</p>
<br>
<a href="logout">Wyloguj się</a>
</body>
</html>
