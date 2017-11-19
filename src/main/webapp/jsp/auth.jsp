<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=windows-1256" charset="utf-8">
	<title>Login or Reister - Chessapp</title>
		
	<!-- Latest compiled and minified CSS -->
	<link rel="stylesheet"
		href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">

	<!-- jQuery library -->
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>

	<!-- Latest compiled JavaScript -->
	<script
		src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>

<body>

	<div class="container-fluid">
	  <div class="row">
	    <div class="col-sm-6">
	    	<form action="javascript:login()" id="loginform">
	    		<h3>Login</h3>
	    		<div class="form-group">
		    		<label for="uname">Username</label>
		    		<input type="text" id="uname" class="form-control">
	    		</div>
				<div class="form-group">
					<label for="passw">Password</label>
					<input type="password" id="passw" class="form-control">
				</div>
				<button id="blogin" class="btn btn-default">Login</button>
			</form>
	    </div>
	    <div class="col-sm-6">
	    	<form action="javascript:register()" id="registerform">
				<h3>Register</h3>
				<div class="form-group">
					<label for="name">Username</label>
					<input type="text" name="dname" id="name" class="form-control">
				</div>
				<div class="form-group">
					<label for="password">Password</label>
					<input type="password" name="password" id="password" class="form-control">
				</div>
				<div class="form-group">
					<label for="cpassword">Confirm Password</label>
					<input type="password" name="cpassword" id="cpassword" class="form-control">
				</div> 
				<button id="bregister" class="btn btn-default">Register</button>
			</form>
	    </div>
	  </div>
	</div>
	
	<script src="${pageContext.request.contextPath}/util_js/jquery-3.2.1.min.js"></script>
	<script src="${pageContext.request.contextPath}/util_js/login.js"></script>
	<script src="${pageContext.request.contextPath}/util_js/register.js"></script>
</body>
</html>