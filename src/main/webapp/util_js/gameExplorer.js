var explorerTable = null;
var exploreGame = null;
var actualMove = null;
var moves = null;

function checkExplorerTable() {
	if ($('#explorerTable').is(':visible')) {
		refreshExplorerTable();
	} else {
		setTimeout(checkExplorerTable, 50);
	}
}

function refreshExplorerTable() {
	var http = new XMLHttpRequest();
	var url = "/main/ExploreLatestGamesServlet";
	http.open("GET", url, true);
	http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	http.onreadystatechange = function() {
		if (http.readyState == 4 && http.status == 200) {
			if (this.responseURL.endsWith("/auth")) {
				onAuthResponse(this.responseURL);
			} else {
				loadDataToExplorerTable(http.response);
				initExploreBoard();
			}
		}
	}
	http.send();
}

function initExploreBoard() {
	console.log("init board");
	var cfg = {
		position : 'start',
		pieceTheme : chessFigurePicutrePath
	};
	explorerTable = ChessBoard('exploreBoard', cfg);
	
	exploreGame = new Chess();
	
	explorerTable.position(exploreGame.fen());
}

function loadDataToExplorerTable(data) {
	data = JSON.parse(data);
	var table = document.getElementById("explorerTable");
	while (table.rows.length > 1) {
		table.deleteRow(-1);
	}

	let fields = [ "blackPlayer", "whitePlayer", "winner" ];
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
		btn.onclick = function(event) {
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
		if (http.readyState == 4 && http.status == 200) {
			if (this.responseURL.endsWith("/auth")) {
				onAuthResponse(this.responseURL);
			} else {
				fillConcreteExploredGame(http.response);
				initExploreBoard();
				showExplorerTable(http.response);
			}
		}
	}
	http.send(params);
}

function showExplorerTable(data) {
	data = (JSON.parse(data))["selectedGame"];
	moves = data.moves.split("|");
	actualMove = 0;
	console.log(moves);
}

function doNextMove() {
	if (moves != null && moves.length > 0 && actualMove + 1 <= moves.length) {
		var move = moves[actualMove].split(" ");
		console.log("nextmove: " + move[0] + "-" + move[1]);
		//explorerTable.move(move[0] + "-" + move[1]);
		actualMove = actualMove + 1;
		exploreGame.move({ color: move[2], from: move[0], to: move[1], flags: move[3], promotion: move[4] });
		explorerTable.position(exploreGame.fen());
	}
}

function doPreviousMove() {
	if (moves != null && moves.length > 0 && actualMove - 1 >= 0) {
		actualMove = actualMove - 1;
		var move = moves[actualMove].split(" ");
		console.log("prevmove: " + move[1] + "-" + move[0]);
		//explorerTable.move(move[1] + "-" + move[0]);
		exploreGame.undo();
		explorerTable.position(exploreGame.fen());
	}
}

function fillConcreteExploredGame(data) {
	data = (JSON.parse(data))["selectedGame"];
	let fields = Object.keys(data);
	$("#concreteExploredGame").empty();

	var table = $('<table></table>').addClass('table');
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