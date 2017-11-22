<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<h1>GameExlorer block of page</h1>
<div id="gameExplorer">
<table class="table left" id="explorerTable">
  <tr>
    <th>Black Player</th>
    <th>White Player</th>
    <th>Result</th>
    <th>Watch</th>
  </tr>
</table>
<style>
table.left {
    float:left;
    width:45%;
}

table.right   {
    width:45%;
    float:right;
} 
</style>
<div id="concreteExploredGame"></div>
</div>
