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
	};
	websocket.onmessage = function(evt) {
		console.log("WebSocket message");	
		
		var json = event.data;
		var message = JSON.parse(json);

		if(message.type == WS_TYPE_GLOBAL_MESSAGE && isGlobalState) {
			// TODO: fogadni a globális üzenetet
		}
		if(message.type == WS_TYPE_PRIVATE_MESSAGE && isPrivateState) {
			// TODO: fogadni a privát üzenetet
		}
		if(message.type == WS_TYPE_GAME_MOVE && isGameState) {
			// TODO: fogadni a lépést
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

function message_websocket(type, content) {
	if (websocket == null) {
		// TODO: proper handling of WebSocket is not connected
	} else {
		var message = { type: type, content: content };
		websocket.send(JSON.stringify(message));
	}
}

function loadContent(url, contentId) {
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			document.getElementById(contentId).innerHTML = this.responseText;
		}
	};
	xhttp.open("GET", url, true);
	xhttp.send();
}

function loadHomeArea() {
	connect_websocket();
	connect_global();
	disconnect_game();
	disconnect_private();
	loadContent("/main/part/home", "content");
}

function loadGameArea() {
	connect_websocket();
	disconnect_global();
	connect_game();
	connect_private();
	loadContent("/main/part/game", "content");
}

function loadGameExplorerArea() {
	connect_websocket();
	disconnect_global();
	disconnect_game();
	disconnect_private();
	loadContent("/main/part/explorer", "content");
}

function loadProfileArea() {
	connect_websocket();
	disconnect_global();
	disconnect_game();
	disconnect_private();
	loadContent("/main/part/profile", "content");
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

$(document).ready(function() {
	connect_websocket();
});