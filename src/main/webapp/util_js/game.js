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

// do not pick up pieces if the game is over
// only pick up pieces for the side to move
var onDragStart = function(source, piece, position, orientation) {
  if (game.game_over() === true ||
      (game.turn() === 'w' && piece.search(/^b/) !== -1) ||
      (game.turn() === 'b' && piece.search(/^w/) !== -1)) {
    return false;
  }
};

var onDrop = function(source, target) {
  var toPromote = document.querySelector('input[name="promotion"]:checked').value;
  
  // see if the move is legal
  var move = game.move({
    from: source,
    to: target,
    promotion: toPromote
  });

  // illegal move
  if (move === null) return 'snapback';

  updateStatus();
};

// update the board position after the piece snap 
// for castling, en passant, pawn promotion
var onSnapEnd = function() {
  board.position(game.fen());
};

var updateStatus = function() {
  var status = '';

  var moveColor = 'White';
  if (game.turn() === 'b') {
    moveColor = 'Black';
  }

  // checkmate?
  if (game.in_checkmate() === true) {
    status = 'Game over, ' + moveColor + ' is in checkmate.';
  }

  // draw?
  else if (game.in_draw() === true) {
    status = 'Game over, drawn position';
  }

  // game still on
  else {
    status = moveColor + ' to move';

    // check?
    if (game.in_check() === true) {
      status += ', ' + moveColor + ' is in check';
    }
  }
};