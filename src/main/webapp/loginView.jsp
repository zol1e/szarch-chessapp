<html>
<head>
<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1256">
<title>Login Page</title>
</head>

<body>

	<form id="loginform">

		Please enter your username <input type="text" id="uname" /><br>

		Please enter your password <input type="password" id="passw" /> <input
			type="button" id="blogin" value="Login">
	</form>
	<script
		src="${pageContext.request.contextPath}/util_js/jquery-3.2.1.min.js"></script>
	<script src="${pageContext.request.contextPath}/util_js/login.js"></script>
	<link rel="stylesheet" type="text/css"
		href="${pageContext.request.contextPath}/styles/login.css" />
</body>
</html>