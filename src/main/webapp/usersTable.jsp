<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<table>
	<tr>
		<th>User name</th>
		<th>First name</th>
		<th>Last name</th>
		<th>Join date</th>
	</tr>
	<c:forEach items="${users}" var="users">
		<tr>
			<td>${users.userName}</td>
			<td>${users.firstName}</td>
			<td>${users.lastName}</td>
			<td>${users.joinDate}</td>
		</tr>
	</c:forEach>
</table>