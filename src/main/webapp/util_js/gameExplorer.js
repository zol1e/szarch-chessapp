function checkExplorerTable() {
	if($('#explorerTable').is(':visible')){
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
		if(http.readyState == 4 && http.status == 200) {
			loadDataToExplorerTable(http.response);
		}
	}
	http.send();
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