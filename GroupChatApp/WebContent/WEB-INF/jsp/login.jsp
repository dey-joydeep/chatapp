<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>ICS: Login</title>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jqsm.js"></script>
<link rel="shortcut icon" type="image/x-icon"
	href="${pageContext.request.contextPath}/images/favicon.ico" />
<script type="text/javascript">
	$(function() {
		// 		$(this).bind("contextmenu", function(e) {
		// 			e.preventDefault();
		// 		});
		$('#login-id').focus();
		$("#submit-btn").click(function(e) {
			var form = $('#login-form');
			var postData = form.serializeArray();
			var formURL = form.attr("action");
			var type = form.attr("method");
			$.ajax({
				url : formURL,
				type : type,
				data : postData,
				success : function(data, textStatus, jqXHR) {
					var response = JSON.parse(data);
					if (response.success) {
						$('#token').val(response.token);
						$('#login-id').val(response.loginId);
						redirect();
					} else {
						$('#err-msg').text(response.message);
						$('#login-id').focus();
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
				}
			});
			e.preventDefault();
		});
	});

	function redirect() {
		window.history.replaceState({},
				'${pageContext.request.contextPath}/init',
				'${pageContext.request.contextPath}/chat');
		$("#login-form").attr('action',
				'${pageContext.request.contextPath}/chat');
		$("#login-form").submit();
	}
</script>
<style type="text/css">
#login-div {
	width: 20%;
	border: 1px solid;
	margin: 235px auto;
}

#login-div div {
	padding: 3px;
}

#input-div {
	width: 50%;
	margin: 3px auto;
}
</style>
</head>
<body>
	<form action="${pageContext.request.contextPath}/login" id="login-form"
		method="post">
		<div id="login-div">
			<div>Chatroom Login:</div>
			<div id="input-div">
				<div>
					<input type="text" name="userId"
						placeholder="Email ID or Username Phone number">
				</div>
				<div>
					<input type="password" name="password" placeholder="Password">
				</div>
				<div style="text-align: center;">
					<input type="submit" value="Sign in" id="submit-btn">
				</div>
			</div>
			<div>
				<span style="color: red;" id="err-msg"></span>
			</div>
		</div>
		<input type="hidden" name="token" id="token"> <input
			type="hidden" name="loginId" id="login-id">
	</form>
</body>
</html>