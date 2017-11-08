<html>
<body>
	<h1>this is the index page. i suppose ur logged in</h1>

	<div>
		<h1>Chess web application</h1>
	</div>

	<div id="menu">
		<nav>
			<input onclick="loadUsers()" type="button" value="Users">
		</nav>
		<form action="/LogoutServlet" method="post">
			<input type="submit" value="Logout">
		</form>
	</div>

	<div id="content"></div>

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
