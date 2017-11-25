<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="container-fluid">
<h1>GameExlorer block of page</h1>

<div class="row" id="gameExplorer">

	<div class="col-sm-6">
		<table class="table" id="explorerTable">
		  <tr>
		    <th>Black Player</th>
		    <th>White Player</th>
		    <th>Result</th>
		    <th>Watch</th>
		  </tr>
		</table>
	</div>

	<div class="col-sm-6" id="concreteExploredGame"></div>

</div>

<div class="row">
	<div class="col-sm-6">
	</div>
	<div class="col-sm-6">
		<div id="exploreBoard" style="width: 400px"></div>
		<input id="prevButton" type="button" value="Previous move" onclick="doPreviousMove()">
		<input id="nextButton" type="button" value="Next move" onclick="doNextMove()">
	</div>
</div>

</div>