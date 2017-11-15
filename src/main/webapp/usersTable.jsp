<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<table>
	<tr>
		<th>First name</th>
		<th>Last name</th>
    <th>User name</th>
		<th>Join date</th>
	</tr>
	<c:forEach items="${users}" var="user">
		<tr>
			<td>${user.getFirstName()}</td>
			<td>${user.getLastName()}</td>
			<td>${user.getNickName()}</td>
			<td>${user.getJoinDate()}</td>
		</tr>
	</c:forEach>
</table>