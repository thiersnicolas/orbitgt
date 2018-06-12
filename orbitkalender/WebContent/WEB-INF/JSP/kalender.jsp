<%@page contentType='text/html' pageEncoding='UTF-8' session='false'%>
<%@taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core'%>
<%@taglib prefix='fmt' uri='http://java.sun.com/jsp/jstl/fmt'%>
<!doctype html>
<html lang='nl'>
<head>
<title>Orbit kalender</title>
<link rel='stylesheet' href='<c:url value="/styles/default.css"/>'>
</head>
<body>
	<h1>Kalender</h1>
	<table class="kalender">
		<thead class="hoofd">
			<tr>
				<th scope="col">Datum</th>
				<c:forEach var="gebruiker" items="${gebruikers}">
					<th scope="col">${gebruiker.naam}</th>
				</c:forEach>
			</tr>
		</thead>
		<tbody class="entries">
		<c:forEach var="entry" items="${kalendermap}">
			<fmt:parseDate value='${entry.key}' type='date' pattern="yyyy-MM-dd" var="datumAlsDate"/>
			<tr>
				<td><fmt:formatDate value="${datumAlsDate}" type='date' pattern="dd-MM-yyyy"/></td>
				<c:forEach var="kalenderEntry" items="${entry.value}">
					<td>${kalenderEntry.entry}</td>
				</c:forEach>
		</c:forEach>
		</tbody>
	</table>
</body>
</html>