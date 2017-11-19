<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<h3>Global chat</h3>
<textarea readonly="readonly" id="chatbox"></textarea><br>
<input id="toSendGlobalMessage" type="text"> <input type="button" onclick="sendGlobalMessage()" value="Send global message">
<div id="gameLobby">

</div>
<input type="button" onclick="createNewGame()" value="New Game">