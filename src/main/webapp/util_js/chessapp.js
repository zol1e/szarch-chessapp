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
	checkLobbyTable();
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

jQuery(document).ready(function () {
	loadHomeArea();
});