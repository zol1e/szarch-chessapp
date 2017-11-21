<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<h1>Game block of page</h1>

<textarea readonly="readonly" id="privateChatBox"></textarea><br>

<input id="toSendPrivateMessage" type="text"> <input type="button" onclick="sendPrivateMessage()" value="Send private message"><br>
<input id="toSendMoveMessage" type="text"> <input type="button" onclick="sendMoveMessage()" value="Send move">

<div id="table"></div>

<div id="board" style="width: 400px">table place</div>

<h3>Promotion:</h3>
<input name="promotion" type="radio" value="q" checked> Queen
<input name="promotion" type="radio" value="n"> Knight
<input name="promotion" type="radio" value="r"> Rook
<input name="promotion" type="radio" value="b"> Bishop