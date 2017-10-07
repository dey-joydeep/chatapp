<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>ICS: Chat Room</title>
<meta charset="UTF-8">
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jqsm.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/init.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/websocket.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/notifier.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/loader.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/sender.js"></script>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/styler.css">
<link rel="shortcut icon" type="image/x-icon"
	href="${pageContext.request.contextPath}/images/favicon.ico" />
<script type="text/javascript">
	var contextPath = '${pageContext.request.contextPath}';
</script>
</head>
<body>
	<header id="heading">
		<div id="title">Intra-team Communication System</div>
		<div id="menu-div">
			<img src="${pageContext.request.contextPath}/images/logout.png"
				alt="Sign out" height="28" width="28" title="Sign out"
				class="img-btn" id="logout-btn">
		</div>
		<div id="my-name">
			You are logged in as:
			<c:out value="${user.realName}" />
		</div>
	</header>
	<div id="content">
		<div id="content-body"></div>
	</div>
	<div id="users">
		<div id="users-title">Friend List</div>
		<div id="user-list"></div>
	</div>
	<div style="" id="emoji-container"></div>
	<div id="enter-control">
		<input type="checkbox" id="enter-check" checked="checked"><label
			for="enter-check"> Allow enter to send message </label>
	</div>
	<form action="${pageContext.request.contextPath}/send" method="post"
		id="chat-form">
		<div id="text">
			<div id="msg-in-box" contenteditable="true"></div>
			<div id="emoji-control">
				<img id="emoji-list-opener"
					src="data:image/png;base64, iVBORw0KGgoAAAANSUhEUgAAAEgAAABICAMAAABiM0N1AAAAwFBMVEVMaXH/zEz/zlH/zE3/zEz/zEz/y0z/y0z/xkv/zEz/zEz/zU7/zEz/zEz/zEz/zEz/zEz/zEz/zEv/zE3/zEz/zEz/zEz/zE3/zU3/zEz/zEz/y0z/zEz/y03/zE1mRQBrSQPtvENyTwZ7VwpxUhL2w0iqgSLisj7e18js6OCSbBXJnDGlkmmGYRC1pYScdBv///+5jSnQojX49vP+/v13Wh3FuJ69kiuHbTfSpDZ/ZCrYqjmYglSkex7Pxa+Se0qAAzOiAAAAHnRSTlMAYwtu0UD5/gXuhyKScvFbsrkart3r5stWdVA0MuAShyFhAAAC4klEQVR4AazTRXpjMRAE4HrYkpnxS5m2kwnscv+DmVEm0b8UNwgvqGlSaeU9XZa6l7cqyVTBQydrNWhotLIOnKTNtvAhaTeV/THZgC/0KylsqLHmG7pu8aoip4VegddUTWhFagovVIe0Nqy+CKtBB42n4U1KOilHeCgROpLk4XuEzuTBm4qSHsoChmqDXhpG7dSQnoYK12r0VsOVQuhNrtKkcgbIFU7GDFLHUdpnkH769EGLr+Xya0HL8dOT1ICGxXK2tVzYjg9S7DRpkM/Z3qfYjZNN7LRp+J0d/dqNk21sdYSGr9OGL7txUjoAMppWpw0ru/GtDECLpvlpw9xufKsLKB3jIK0wZYzQOEVChiebTFAhn5b/z258p4IWGdaQp2znJAO+yEmOD5IBn/akB80o+igZRRnvoE0rVpEdNxAFzcxKNqHfTOoRk/n+pwrbHn3homutV+KiYLf2PQzRp8HrPx53pqWjDtAHeawIMKdiPUl3rGPlGBDVPyJCv0gOb+BO5fG9plTKHz+kpFTfx7lyHN6Qo1+k/9NuYDU26Kf9iiRnJbAoffsjbIgoaXxHZgg63ySY6PQESS0HAGGzrLKmSfxr91ALQQCIEPVD9+qTxtgqy6zA6hZh8XfwG7XNZmFr+A2Hxb9nRy38gTBzPEbAH7TYjnoGSf8/B19O0ZTJ/+dHsUHuXI/pct2UozRNPabb18MQoeENwpuqz1IZL+ANGocIHGue4QPiKXkxtqyq0pqX5EnAB55RrBkGLclhBbgcC1o7l9v0msAiiMbvfiyMFrCIAofR8XhcLF1TjOLxZGB/ZDAD9ogC+0yFoDNysqGoQsyWmuOcTFxO3lfG0/OlmiXVCBVREtesFcVPtugGN61cLn7jVZTGynFGCONOxXSkioYqx2Hq+ulhqAEh1KQRZGQ5OFwx+1x+Xowe+yfhh6h5nASZxt7GuugUs5xGK8c6hJNvu2fR23wYne1+m7uWX1iL7IE5/nCDAAAAAElFTkSuQmCC"
					alt="" height="50" width="50">
			</div>
			<div id="send-div">
				<input type="submit" id="send-btn" value="Send">
			</div>
			<div>
				<textarea id="msg-in-box-content" name="message"
					style="display: none;"></textarea>
				<input type="hidden" name="loginId" id="login-id"
					value="<c:out value='${user.loginId}' />"> <input
					type="hidden" id="event-id" value="" name="eventId">
			</div>
		</div>
	</form>
	<div id="err-div">
		<span id="err-box"></span>
	</div>
	<input type="hidden" id="last-msg-id" value="-1">
	<input type="hidden" id="page-unload-status" value="close">
</body>
</html>
