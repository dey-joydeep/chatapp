<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Chatroom- Error</title>
</head>
<body>
	<div style="width: 100%; color: red; font-size: 1em;">
		Error: Possible reason could be-
		<ul>
			<li>Unauthenticated access</li>
			<li>You might have logged out.</li>
			<li>Error occurred during request processing.</li>
		</ul>
	</div>
	<div style="width: 100%; font-size: 1em;">
		Try again by <a href="${pageContext.request.contextPath}/">re-login</a>
	</div>
</body>
</html>