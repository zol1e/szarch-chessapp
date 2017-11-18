<html>
<head>
	<title>Chess application</title>
	<meta charset="utf-8">
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
	<nav id="menu">
		<div class="container-fluid">
			
			<div class="navbar-header">
	      		<a class="navbar-brand" href="/main/index">Chessapp</a>
	    	</div>
			
			<ul class="nav navbar-nav">
		      <li class="active"><a href="/main/index">Home</a></li>
		      <li><a onclick="loadUsers()">Game explorer</a></li>
		      <li><a onclick="loadUsers()">Play!</a></li>
		      <li><a onclick="loadUsers()">Profile</a></li>
		    </ul>
		    <form action="/LogoutServlet" method="post" class="navbar-form navbar-left">
				<input type="submit" value="Logout" class="btn btn-default">
			</form>
		</div>
	</nav>

	<div id="content" class="container">
		<h3>Welcome page</h3>
  		<p>Welcome text here</p>
	</div>

	<script>
		function loadUsers() {
			var xhttp = new XMLHttpRequest();
			xhttp.onreadystatechange = function() {
				if (this.readyState == 4 && this.status == 200) {
					document.getElementById("content").innerHTML = this.responseText;
				}
			};
			xhttp.open("GET", "/users", true);
			xhttp.send();
		}
	</script>
</body>
</html>
