	function login(){
		var username = $("#uname").val();
		var password = $("#passw").val();
		if( username =='' || password ==''){
			$('input[type="text"],input[type="password"]').css("border","2px solid red");
			$('input[type="text"],input[type="password"]').css("box-shadow","0 0 3px red");
		}else {
			var xhttp = new XMLHttpRequest();
			xhttp.onreadystatechange = function() {
				if (this.readyState == 4 && this.status == 200) {
					
				}
			}
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
					showMsg();
			    }
			    
			    
			}
			http.send(params);
			/*$.ajax({
				  type: "POST",
				  dataType: 'json',
				  url: "/LoginServlet",
				  data: "uname=" + username + "&passw=" + password,
				  success: function(data){
					  alert("succes");
					  window.location.replace(data.redirect);
				  },
				  failure : function(){
					  alert("fail");
				  }
				});*/
			
		}
	};

	
	function showMsg() {
	    var popup = document.getElementById("myPopup");
	    popup.classList.toggle("show", true);
	}
	
	$(document).ready(function(){
		blogin.onclick = login;
	})