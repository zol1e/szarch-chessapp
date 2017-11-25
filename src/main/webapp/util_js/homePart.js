function checkLobbyTable() {
	if($('#lobbyTable').is(':visible')){
		refreshLobbyTable();
	} else {
	    setTimeout(checkLobbyTable, 50);
	}
}

function createNewGame() {
	var http = new XMLHttpRequest();
	var url = "/main/CreateJoinGameServlet";
	http.open("GET", url, true);
	http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	http.onreadystatechange = function() {
		if(http.readyState == 4 && http.status == 200) {if (this.responseURL.endsWith("/auth"))
			onAuthResponse(this.responseURL);
		}
	}
	http.send();
}

function joinGame(game, attname) {
	var http = new XMLHttpRequest();
	var url = "/main/CreateJoinGameServlet";
	var params = "game=" + game + "&func=" + attname;
	http.open("POST", url, true);
	http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	http.onreadystatechange = function() {
		if(http.readyState == 4 && http.status == 200) {
			if (this.responseURL.endsWith("/auth"))
				onAuthResponse(this.responseURL);
		}
	}
	http.send(params);
}

function refreshLobbyTable() {
	var http = new XMLHttpRequest();
	var url = "/main/GetGameLobbiesServlet";
	http.open("GET", url, true);
	http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	http.onreadystatechange = function() {
		if(http.readyState == 4 && http.status == 200) {
			if (this.responseURL.endsWith("/auth"))
				onAuthResponse(this.responseURL);
			else
				loadDataToLobbyTable(http.response);
		}
	}
	http.send();
}
function loadDataToLobbyTable(data) {
	data = JSON.parse(data);
	var table = document.getElementById("lobbyTable");
	while(table.rows.length > 1) {
		  table.deleteRow(-1);
		}
	jQuery.each(data["gameLobbies"], function() {
		var row = table.insertRow(-1);
		var cell1 = row.insertCell(0);
		var cell2 = row.insertCell(1);
		var cell3 = row.insertCell(2);
		cell1.innerHTML = this["whitePlayer"];
		cell2.innerHTML = this["blackPlayer"];
		let btn = document.createElement('input');
		btn.type = "button";
		btn.className = "btn";
		btn.value = this["btnname"];
		btn.setAttribute("func", this["btnname"]);
		btn.setAttribute("game", this["game"]);
		btn.onclick = function(event){
			joinGame(this.getAttribute('game'), this.getAttribute('func'));
		};
		cell3.appendChild(btn);
	});
}
