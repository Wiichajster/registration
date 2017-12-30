<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Rejestracja</title>
</head>
<body>
	<h1>Rejestracja</h1>
	<br>
	<c:if test="${not empty errorMessage}">
		<c:out value="${errorMessage}"/>
	</c:if>
	<form action="register" method="post">
		<label for="username">Login: </label> 
		<input type="text" name="username" />
		<br>
		<label for="password">Hasło: </label>
		<input type="password" name="password"/>
		<br>
		<label for="email">Email: </label>
		<input type="text" name="email"/>
		<br>
		<input type="submit" value="Zarejestruj"/>
	</form>
</body>
</html>