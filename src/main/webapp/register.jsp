<!DOCTYPE html>
<html>
<head>
</head>
<body>
	<div class="container">
		<div class="main">
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
		</div>
  <script src="${pageContext.request.contextPath}/util_js/jquery-3.2.1.min.js"></script>
  <script src="${pageContext.request.contextPath}/util_js/register.js"></script>
</body>
</html>