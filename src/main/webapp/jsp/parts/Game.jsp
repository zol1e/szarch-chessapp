<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<h1>Game block of page</h1>

<textarea readonly="readonly" id="privateChatBox"></textarea><br>

<input id="toSendPrivateMessage" type="text"> <input type="button" onclick="sendPrivateMessage()" value="Send private message"><br>

<div id="table"></div>

<h3 id="opponentTime"></h3>
<div id="board" style="width: 400px">table place</div>
<h3 id="myTime"></h3>

<input type="button" onclick="sendResign()" value="Resign">
<input type="button" onclick="sendDraw()" value="Offer/Accept draw">

Promote pawns to
<input name="promotion" type="radio" value="q" checked> Queen
<input name="promotion" type="radio" value="n"> Knight
<input name="promotion" type="radio" value="r"> Rook
<input name="promotion" type="radio" value="b"> Bishop