<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
	<title>Chess application</title>
	<meta charset="utf-8">
	<!-- Latest compiled and minified CSS -->
	<link rel="stylesheet"
		href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
	<link rel="stylesheet" href="/chessboardjs/css/chessboard-0.3.0.min.css">
</head>
<body>
	<nav id="menu">
		<div class="container-fluid">			
			<div class="navbar-header">
	      		<p class="navbar-brand">Chessapp</p>
	    	</div>
			
			<ul class="nav navbar-nav">
		      <li><a onclick="loadHomeArea()">Home</a></li>
		      <li><a onclick="loadGameExplorerArea()">Game explorer</a></li>
		      <li><a onclick="loadGameArea()">Play!</a></li>
		      <li><a onclick="loadProfileArea()">Profile</a></li>
		    </ul>
		    <form action="/LogoutServlet" method="post" class="navbar-form navbar-left">
				<label>Logged in as <c:out value="${userName}"/></label>
				<input type="submit" value="Logout" class="btn btn-default">
			</form>
		</div>
	</nav>

	<div id="content" class="container"></div>

	<!-- jQuery library -->
	<script src="${pageContext.request.contextPath}/util_js/jquery-3.2.1.min.js"></script>

	<!-- Latest compiled JavaScript -->
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

	<!-- Chessboardjs -->
	<script type="text/javascript" src="${pageContext.request.contextPath}/chessboardjs/js/chessboard-0.3.0.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/chessboardjs/js/json3.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/chessjs/chess.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/util_js/game.js"></script>
	
	<!-- Main chess application -->
	<script src="${pageContext.request.contextPath}/util_js/chessapp.js"></script>
  <script src="${pageContext.request.contextPath}/util_js/homePart.js"></script>
  <script src="${pageContext.request.contextPath}/util_js/gameExplorer.js"></script>
</body>
</html>