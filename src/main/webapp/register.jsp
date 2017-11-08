<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form class="form" method="post" action="#">
	<h2>Create Registration Form Using jQuery</h2>
	
	<label>Name :</label>
	<input type="text" name="dname" id="name">
	
	<label>Password :</label>
	<input type="password" name="password" id="password">
	
	<label>Confirm Password :</label>
	<input type="password" name="cpassword" id="cpassword">
	
	<input type="button" name="register" id="bregister" value="Register">
</form>