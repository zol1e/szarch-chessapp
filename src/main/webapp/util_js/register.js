function register() {
	var username = $("#name").val();
	var password = $("#password").val();
	var cpassword = $("#cpassword").val();
	if (username == '' || password == '' || cpassword == '') {
		;
	} else if (!(password).match(cpassword)) {
		;
	} else {
		var http = new XMLHttpRequest();
		var url = "/RegisterServlet";
		var params = "uname=" + username + "&passw=" + password;
		http.open("POST", url, true);
		http.setRequestHeader("Content-type",
				"application/x-www-form-urlencoded");
		http.onreadystatechange = function() {
			if (http.readyState == 4 && http.status == 200) {
				window.location.replace(http.responseURL);
			} else if (http.readyState == 4 && http.status == 400) {
				$("form")[0].reset();
			}
		}
		http.send(params);
	}
}