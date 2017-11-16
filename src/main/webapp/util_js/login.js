	function login(){
		var username = $("#uname").val();
		var password = $("#passw").val();
		if( username =='' || password ==''){
			$('input[type="text"],input[type="password"]').css("border","2px solid red");
			$('input[type="text"],input[type="password"]').css("box-shadow","0 0 3px red");
		}else {
			var http = new XMLHttpRequest();
			var url = "/LoginServlet";
			var params = "uname=" + username + "&passw=" + password;
			http.open("POST", url, true);
			http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
			http.onreadystatechange = function() {
			    if(http.readyState == 4 && http.status == 200) {
			    	window.location.replace(http.responseURL);
			    } else if (http.readyState == 4 && http.status == 400) {
			    	$("form")[0].reset();
			    }
			    
			    
			}
			http.send(params);			
		}
	};

	
	$(document).ready(function(){
		blogin.onclick = login;
	})