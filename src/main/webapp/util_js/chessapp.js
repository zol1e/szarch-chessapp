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

// Game message constants
var WS_USERNAME = "username";

// Game message type constants
var WS_TYPE_GAME_STATUS = "game_status";
var WS_TYPE_GAME_MOVE = "move";

// Status message property constants
var WS_GAME_STATUS_WHITE_TIME = "whitetime";
var WS_GAME_STATUS_BLACK_TIME = "blacktime";
var WS_GAME_STATUS_FEN = "fen";
var WS_GAME_STATUS_PGN = "pgn";
var WS_GAME_STATUS_WHITE_USER = "whiteuser";
var WS_GAME_STATUS_BLACK_USER = "blackuser";

// Move message property constants
var WS_MOVE_COLOR = "color";
var WS_MOVE_FLAGS = "flags";
var WS_MOVE_FROM = "from";
var WS_MOVE_TO = "to";
var WS_MOVE_PROMOTION = "promotion";

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
			var chatMessage = message.content + "\n";
			$("#chatbox").val($("#chatbox").val() + chatMessage);
		}
		if(message.type == WS_TYPE_PRIVATE_MESSAGE && isPrivateState) {
			let privateChatMsg = message.content + "\n";
			$("#privateChatBox").val($("#privateChatBox").val() + privateChatMsg);
		}
		if(message.type == WS_TYPE_GAME_STATUS && isGameState) {
			var fen = message.fen;
			var whitetime = message.whitetime;
			var blacktime = message.blacktime;
			var whiteuser = message.whiteuser;
			var blackuser = message.blackuser;
			var myUsername = message.username;
			
			game = new Chess();
			game.load(fen);

			var isMyTurn = false;
			var tableOrientation = null;
			if(whiteuser === myUsername) {
				tableOrientation = "white";
				// If white to play, this is my turn
				if(game.turn() === 'w') {
					isMyTurn = true;
				}
			} else {
				tableOrientation = "black";
				// If black to play, this is my turn
				if(game.turn() === 'b') {
					isMyTurn = true;
				}
			}
			
			// Initialize the chessboard
			boardConfig = {
					  pieceTheme: chessFigurePicutrePath,
					  position: fen,
					  draggable: isMyTurn,
					  onDragStart: onDragStart,
					  onDrop: onDrop,
					  onSnapEnd: onSnapEnd,
					  orientation: tableOrientation
			};
			board = ChessBoard("board", boardConfig);
			
			startTimer(whitetime, blacktime, 200);
			updateStatus();
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

function sendMove(move, toPromote) {
	if (websocket != null && websocket.readyState == 1) {
		var message = { 
				type: WS_TYPE_GAME_MOVE,
				from: move.from, 
				to: move.to,
				color: move.color,
				flags: move.flags,
				promotion: toPromote
		};
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
			isGameState = true;
			connect_game();
		}		
	});
	connect_websocket();
	disconnect_global();
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

function joinGame(game, attname) {
	var http = new XMLHttpRequest();
	var url = "/main/CreateJoinGameServlet";
	var params = "game=" + game + "&func=" + attname;
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

	let fields = ["blackPlayer", "whitePlayer", "winner"];
	jQuery.each(data["latestGames"], function() {
		var row = table.insertRow(-1);
		let i = 0;
		for (i = 0; i < fields.length; i++) {
			var cell = row.insertCell(i);
			cell.innerHTML = this[fields[i]];
		}
		var cell4 = row.insertCell(i);
		let btn = document.createElement('input');
		btn.type = "button";
		btn.className = "btn";
		btn.value = "Explore";
		btn.setAttribute("game", this["gameId"]);
		btn.onclick = function(event){
			watchExploreGame(this.getAttribute('game'));
		};
		cell4.appendChild(btn);
	});
}

function watchExploreGame(game) {
	var http = new XMLHttpRequest();
	var url = "/main/ExploreLatestGamesServlet";
	var params = "game=" + game;
	http.open("POST", url, true);
	http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	http.onreadystatechange = function() {
		if(http.readyState == 4 && http.status == 200) {
			fillConcreteExploredGame(http.response);
		}
	}
	http.send(params);
}

function fillConcreteExploredGame(data) {
	data = (JSON.parse(data))["selectedGame"];
	let fields = Object.keys(data);
	$("#concreteExploredGame").empty();
	
	var table = $('<table></table>').addClass('table right');
	var tbody = $('<tbody></tbody>');
	table.append(tbody);
	for (let i = 0; i < fields.length; i++) {
		var row = $('<tr></tr>');
		var cell1 = $('<td></td>').text(fields[i]);
		var cell2 = $('<td></td>').text(data[fields[i]]);
		row.append(cell1);
		row.append(cell2);
		tbody.append(row);
	}
	$('#concreteExploredGame').append(table);
}

jQuery(document).ready(function () {
	loadHomeArea();
});