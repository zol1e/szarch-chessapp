// WebSocket connection instance for the client
var websocket = null;

// Játékhoz kapcsolódó vezérlő üzenet konstansok
var WS_TYPE_GAME_CONNECT = "connect_game";
var WS_TYPE_GAME_DISCONNECT = "disconnect_game";
var WS_TYPE_GAME_MOVE = "move";

// Globális chat vezérlő üzenet konstansok
var WS_TYPE_GLOBAL_CONNECT = "connect_global";
var WS_TYPE_GLOBAL_DISCONNECT = "disconnect_global";
var WS_TYPE_GLOBAL_MESSAGE = "global_message";

// Privát chat vezérlő üzenet konstansok
var WS_TYPE_PRIVATE_CONNECT = "connect_private";
var WS_TYPE_PRIVATE_DISCONNECT = "disconnect_private";
var WS_TYPE_PRIVATE_MESSAGE = "message_private";

// Melyik állapotban van az alkalmazás, Game-nél privát és lépés üzeneteket, Global-nál global üzeneteket fogad
var isGameState;
var isPrivateState;
var isGlobalState;

function initWebSocket() {
	// TODO: address should not be hardcoded
	websocket = new WebSocket("wss://localhost:8443/main/websocket");
	websocket.onopen = function(evt) {
		// TODO: proper handling of opening WebSocket event
		console.log("WebSocket connected");
		if(isGlobalState) {
			connect_global();
			disconnect_game();
			disconnect_private();
		}
		if(isPrivateState) {
			disconnect_global();
			connect_private();
		}
		if(isGameState) {
			disconnect_global();
			connect_game();
		}
	};
	websocket.onmessage = function(evt) {
		console.log("WebSocket message");	
		
		var json = evt.data;
		var message = JSON.parse(json);

		if(message.type == WS_TYPE_GLOBAL_MESSAGE && isGlobalState) {
			// TODO: fogadni a globális üzenetet
			var chatMessage = message.content + "\n";
			$("#chatbox").val($("#chatbox").val() + chatMessage);
		}
		if(message.type == WS_TYPE_PRIVATE_MESSAGE && isPrivateState) {
			// TODO: fogadni a privát üzenetet
			let privateChatMsg = message.content + "\n";
			$("#privateChatBox").val($("#privateChatBox").val() + privateChatMsg);
			//document.getElementById("table").innerHTML = "<p>" + message.content + "</p>";
		}
		if(message.type == WS_TYPE_GAME_MOVE && isGameState) {
			// TODO: fogadni a lépést
			document.getElementById("table").innerHTML = "<p>" + message.content + "</p>";
		}
	};
	websocket.onerror = function(evt) {
		websocket = null;
		console.log("WebSocket error");
	};
	websocket.onclose = function(evt) {
		websocket = null;
		console.log("WebSocket closed");
	};
}

function connect_websocket() {
	if (websocket == null) {
		initWebSocket();
	} else {
		// TODO: proper handling of WebSocket is already connected
	}
}

function sendGlobalMessage() {
	message_websocket(WS_TYPE_GLOBAL_MESSAGE, $("#toSendGlobalMessage").val());
}

function sendPrivateMessage() {
	message_websocket(WS_TYPE_PRIVATE_MESSAGE, $("#toSendPrivateMessage").val());
}

function sendMoveMessage() {
	message_websocket(WS_TYPE_GAME_MOVE, $("#toSendMoveMessage").val());
}

function message_websocket(type, content) {
	if (websocket != null && websocket.readyState == 1) {
		var message = { type: type, content: content };
		websocket.send(JSON.stringify(message));
	} else {
		// TODO: proper handling of WebSocket is not connected
	}
}

function loadContentWithDefaultCallback(url, contentId) {
	loadContent(url, contentId, null);
}

function loadContent(url, contentId, callback) {
	var xhttp = new XMLHttpRequest();
	
	// Load content with the default callback:
	// Inject the response html to the content area with the given id.
	if(callback == null) {
		xhttp.onreadystatechange = function() {
			if (this.readyState == 4 && this.status == 200) {
				document.getElementById(contentId).innerHTML = this.responseText;
			}
		};
	} else {
		// Or use a custom callback
		xhttp.onreadystatechange = callback;
	}
	
	xhttp.open("GET", url, true);
	xhttp.send();
}

function loadHomeArea() {
	loadContent("/main/part/home", "content");
	connect_websocket();
	connect_global();
	disconnect_game();
	disconnect_private();
}

function loadGameArea() {
	loadContent("/main/part/game", "content", function() {
		
		if (this.readyState == 4 && this.status == 200) {
			document.getElementById("content").innerHTML = this.responseText;
			
			// After the html is loaded, initialize the chessboard
			boardConfig = {
					  pieceTheme: chessFigurePicutrePath,
					  position: "start",
					  draggable: true,
					  onDragStart: onDragStart,
					  onDrop: onDrop,
					  onSnapEnd: onSnapEnd
			};
			
			board = ChessBoard("board", boardConfig);
			game = new Chess();
			
			startTimer();
			
			updateStatus();
		}
		
	});
	connect_websocket();
	disconnect_global();
	connect_game();
	connect_private();
}

function loadGameExplorerArea() {
	loadContent("/main/part/explorer", "content");
	checkExplorerTable();
	connect_websocket();
	disconnect_global();
	disconnect_game();
	disconnect_private();
}

function loadProfileArea() {
	loadContent("/main/part/profile", "content");
	connect_websocket();
	disconnect_global();
	disconnect_game();
	disconnect_private();
}

function connect_global() {
	isGlobalState = true;
	message_websocket(WS_TYPE_GLOBAL_CONNECT, null);
}

function disconnect_global() {
	isGlobalState = false;
	message_websocket(WS_TYPE_GLOBAL_DISCONNECT, null);
}

function connect_game() {
	isGameState = true;
	message_websocket(WS_TYPE_GAME_CONNECT, null);
}

function disconnect_game() {
	isGameState = false;
	message_websocket(WS_TYPE_GAME_DISCONNECT, null);
}

function connect_private() {
	isPrivateState = true;
	message_websocket(WS_TYPE_PRIVATE_CONNECT, null);
}

function disconnect_private() {
	isPrivateState = false;
	message_websocket(WS_TYPE_PRIVATE_DISCONNECT, null);
}

function createNewGame() {
	var http = new XMLHttpRequest();
	var url = "/main/CreateJoinGameServlet";
	http.open("GET", url, true);
	http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	http.onreadystatechange = function() {
		if(http.readyState == 4 && http.status == 200) {
			//window.location.replace(http.responseURL);
		}
	}
	http.send();
}

function joinGame(game) {
	var http = new XMLHttpRequest();
	var url = "/main/CreateJoinGameServlet";
	var params = "game=" + game;
	http.open("POST", url, true);
	http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	http.onreadystatechange = function() {
		if(http.readyState == 4 && http.status == 200) {
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
		cell1.innerHTML = this["blackPlayer"];
		cell2.innerHTML = this["whitePlayer"];
		let btn = document.createElement('input');
		btn.type = "button";
		btn.className = "btn";
		btn.value = "Join";
		btn.setAttribute("game", this["game"]);
		btn.onclick = function(event){
			joinGame(this.getAttribute('game'));
		};
		cell3.appendChild(btn);
	});
}

function refreshExplorerTable() {
	var http = new XMLHttpRequest();
	var url = "/main/ExploreLatestGamesServlet";
	http.open("GET", url, true);
	http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	http.onreadystatechange = function() {
		if(http.readyState == 4 && http.status == 200) {
			loadDataToExplorerTable(http.response);
		}
	}
	http.send();
}

function checkExplorerTable() {
	if($('#gameExplorer').is(':visible')){
		refreshExplorerTable();
	} else {
	    setTimeout(checkExplorerTable, 50);
	}
}

function loadDataToExplorerTable(data) {
	data = JSON.parse(data);
	var table = document.getElementById("explorerTable");
	while(table.rows.length > 1) {
		  table.deleteRow(-1);
		}
	jQuery.each(data["latestGames"], function() {
		var row = table.insertRow(-1);
		var cell1 = row.insertCell(0);
		var cell2 = row.insertCell(1);
		var cell3 = row.insertCell(2);
		var cell4 = row.insertCell(3);
		cell1.innerHTML = this["blackPlayer"];
		cell2.innerHTML = this["whitePlayer"];
		cell3.innerHTML = this["winner"];
		let btn = document.createElement('input');
		btn.type = "button";
		btn.className = "btn";
		btn.value = "Join";
		btn.setAttribute("game", this["game"]);
		btn.onclick = function(event){
			watchExploreGame(this.getAttribute('game'));
		};
		cell4.appendChild(btn);
	});
}

function watchExploreGame(game) {
	
}

jQuery(document).ready(function () {
	loadHomeArea();
});