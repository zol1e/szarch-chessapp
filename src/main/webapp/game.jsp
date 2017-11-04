<html>

<head>
<link rel="stylesheet" href="chessboardjs/css/chessboard-0.3.0.min.css">
</head>

<body>

	<h1>WebSocket test</h1>

	<input onclick="send_message()" value="Send" type="button">
	<input onclick="connect_websocket()" value="Connect with websocket"
		type="button">
	<input id="text" name="message" value="Message to websocket"
		type="text">

	<div id="content"></div>

	<div id="board1" style="width: 400px"></div>

	<script type="text/javascript"
		src="chessboardjs/js/chessboard-0.3.0.min.js"></script>
	<script type="text/javascript"
		src="chessboardjs/js/jquery-3.2.1.min.js"></script>
	<script type="text/javascript" src="chessboardjs/js/json3.min.js"></script>

	<script type="text/javascript">
		var websocket = null;

		var onSnapEnd = function(oldPos, newPos) {
			document.getElementById("content").innerHTML = "Move happend";
			if (websocket == null) {
				document.getElementById("content").innerHTML = "Not connected, press connect button";
			} else {
				websocket.send(newPos);
			}
		};
		
		var cfg = {
				  pieceTheme: 'chessboardjs/img/chesspieces/wikipedia/{piece}.png',
				  position: 'start',
				  draggable: true,
				  onSnapEnd: onSnapEnd
		};
		var board1 = ChessBoard('board1', cfg);
		
		function initWebSocket() {
			websocket = new WebSocket("ws://localhost:8080/game/websocket");
			websocket.onopen = function(evt) {
				document.getElementById("content").innerHTML = evt.data;
			};
			websocket.onmessage = function(evt) {
				document.getElementById("content").innerHTML = evt.data;
			};
			websocket.onerror = function(evt) {
				websocket = null;
			};
			websocket.onclose = function(evt) {
				websocket = null;
			};
		}

		function send_message() {
			if (websocket == null) {
				document.getElementById("content").innerHTML = "Not connected, press connect button";
			} else {
				doSend();
			}
		}

		function connect_websocket() {
			if (websocket == null) {
				initWebSocket();
			} else {
				document.getElementById("content").innerHTML = "Already connected";
			}
		}

		function doSend() {
			message = text.value;
			websocket.send(message);
		}
	</script>

</body>
</html>