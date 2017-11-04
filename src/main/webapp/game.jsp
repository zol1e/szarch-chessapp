<html>

<head>
<script language="javascript" type="text/javascript">
	var websocket = null;

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
</head>

<body>

	<h1>WebSocket test</h1>

	<input onclick="send_message()" value="Send" type="button">
	<input onclick="connect_websocket()" value="Connect with websocket" type="button">
	<input id="text" name="message" value="Message to websocket" type="text">
	
	<div id="content"></div>

</body>
</html>