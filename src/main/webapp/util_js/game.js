// Chessboard utility, configuring board to only allow legal moves.
// Working with chessjs library.
// Some codes are from chessboardjs.com examples page

// Chessboardjs board
var board = null;

// Chessjs game
var game = null;

// Board configuration for chessboardjs
var boardConfig = null;

// Path of figure pictures, which are shown on the board
var chessFigurePicutrePath = "/chessboardjs/img/chesspieces/wikipedia/{piece}.png";

// White time left in the current game
var whiteTimeLeft = null;

// Black time left in the current game
var blackTimeLeft = null;

// Saves last date, when timer tick happened, to properly calculate the delta time
var lastTickTime = null;

// Timer object set up at game start and clear after game end
var jsTimer = null;

// do not pick up pieces if the game is over
// only pick up pieces for the side to move
var onDragStart = function(source, piece, position, orientation) {
	if (game.in_checkmate() === true || game.in_stalemate() === true
			|| (game.turn() === 'w' && piece.search(/^b/) !== -1)
			|| (game.turn() === 'b' && piece.search(/^w/) !== -1)) {
		return false;
	}
};

var onDrop = function(source, target) {
	var toPromote = document.querySelector('input[name="promotion"]:checked').value;

	// see if the move is legal
	var move = game.move({
		from : source,
		to : target,
		promotion : toPromote
	});

	console.log(move);
	console.log(game.pgn());
	console.log(game.fen());
	
	// illegal move
	if (move === null)
		return 'snapback';

	updateStatus();
	
	sendMove(move, toPromote);
};

// update the board position after the piece snap
// for castling, en passant, pawn promotion
var onSnapEnd = function() {
	board.position(game.fen());
};

var updateStatus = function() {
	if(whiteTimeLeft <= 0) {
		var myMessage;
		var opponentMessage;
		if(board.orientation() === 'white') {
			myMessage = "LOST ON TIME";
			opponentMessage = "WON ON TIME";
		} else {
			myMessage = "WON ON TIME";
			opponentMessage = "LOST ON TIME";
		}
		document.getElementById("myTime").innerHTML = myMessage;
		document.getElementById("opponentTime").innerHTML = opponentMessage;
		
		board.dragable = false;
		
		return;
	}
	
	if(blackTimeLeft <= 0) {
		var myMessage;
		var opponentMessage;
		if(board.orientation() === 'white') {
			myMessage = "WON ON TIME";
			opponentMessage = "LOST ON TIME";
		} else {
			myMessage = "LOST ON TIME";
			opponentMessage = "WON ON TIME";
		}
		document.getElementById("myTime").innerHTML = myMessage;
		document.getElementById("opponentTime").innerHTML = opponentMessage;
		
		board.dragable = false;
		
		return;
	}
	
	if (game.in_stalemate() === true) {
		document.getElementById("myTime").innerHTML = "DRAW";
		document.getElementById("opponentTime").innerHTML = "DRAW";
	} else if (isGameOver()) {
		if(game.turn() === 'b') {
			if(board.orientation() === 'white') {
				document.getElementById("myTime").innerHTML = "WON";
				document.getElementById("opponentTime").innerHTML = "LOST";
			} else {
				document.getElementById("opponentTime").innerHTML = "WON";
				document.getElementById("myTime").innerHTML = "LOST";
			}
		}
		if(game.turn() === 'w') {
			if(board.orientation() === 'white') {
				document.getElementById("myTime").innerHTML = "LOST";
				document.getElementById("opponentTime").innerHTML = "WON";
			} else {
				document.getElementById("opponentTime").innerHTML = "LOST";
				document.getElementById("myTime").innerHTML = "WON";
			}
		}
	}
};

// Set the times in starting position and initialize the timer
// 	- tickRate: the periodic call of the timer function
//  - timeLimit: time limit for the player
function startTimer(whiteTime, blackTime, tickRate) {
	lastTickTime = new Date().getTime();
	whiteTimeLeft = whiteTime;
	blackTimeLeft = blackTime;
	timeUp = false;
	
	if(board.orientation() === 'white') {
		document.getElementById("myTime").innerHTML = formatTime(convertMillisToHMS(whiteTimeLeft));
		document.getElementById("opponentTime").innerHTML = formatTime(convertMillisToHMS(blackTimeLeft));
	} else {
		document.getElementById("opponentTime").innerHTML = formatTime(convertMillisToHMS(whiteTimeLeft));
		document.getElementById("myTime").innerHTML = formatTime(convertMillisToHMS(blackTimeLeft));
	}

	clearInterval(jsTimer);
	jsTimer = setInterval(timer, tickRate);
}

// Timer which can be used in a javascript interval function
// Eg.: setIterval(timer, 200) means that this function called in every 200ms
function timer() {
	if (isGameOver() === true) {
		clearInterval(jsTimer);
		updateStatus();
	}
	
	var now = new Date().getTime();

	var deltaTime = now - lastTickTime;

	// After counting the delta time, the new temp time should be the current time
	lastTickTime = now;
	
	var timeUp = false;

	if (game.turn() == 'w') {
		whiteTimeLeft = whiteTimeLeft - deltaTime;
		if (whiteTimeLeft > 0) {
			var whiteTime = convertMillisToHMS(whiteTimeLeft);
			if(board.orientation() === 'white') {
				document.getElementById("myTime").innerHTML = formatTime(whiteTime);
			} else {
				document.getElementById("opponentTime").innerHTML = formatTime(whiteTime);
			}
		}
	}
	if (game.turn() == 'b') {
		blackTimeLeft = blackTimeLeft - deltaTime;
		if (blackTimeLeft > 0) {
			var blackTime = convertMillisToHMS(blackTimeLeft);
			if(board.orientation() === 'black') {
				document.getElementById("myTime").innerHTML = formatTime(blackTime);
			} else {
				document.getElementById("opponentTime").innerHTML = formatTime(blackTime);
			}
		}
	}

	updateStatus();
	
	if (isGameOver()) {
		clearInterval(jsTimer);
	}
}

// Return an object, which convert the time given in milliseconds to an , which cuts the time to hours, minutes, seconds:
// These can be accesed from the return value object:
// .hour
// .min
// .sec
function convertMillisToHMS(millis) {
	var hours = Math.floor(millis / (1000 * 60 * 60));
	var minutes = Math.floor((millis % (1000 * 60 * 60)) / (1000 * 60));
	var seconds = Math.floor((millis % (1000 * 60)) / 1000);
	
	return {hour: hours, min: minutes, sec: seconds};
}

// Format the time object returned by convertMillisToHMS to displayable text value
// Format: 
// 		[hours left] h : [minutes left] m : [seconds left] s
function formatTime(timeObject) {
	return timeObject.hour + " h : " + timeObject.min + " m : " + timeObject.sec + " s";
}

// Game is over if:
// - Black or white is out of time
// - The position on the board (in order to the chess rules) is not playable anymore
function isGameOver() {
	if(game.in_checkmate() === true || game.in_stalemate() === true || blackTimeLeft < 0 || whiteTimeLeft < 0) {
		return true;
	}
	return false;
}