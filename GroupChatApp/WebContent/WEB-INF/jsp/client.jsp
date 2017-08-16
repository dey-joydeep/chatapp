<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>ICS: Chat Room</title>
<meta charset="UTF-8">
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resource/js/jqsm.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resource/js/loader.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resource/js/user.js"></script>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resource/css/styler.css">
<link rel="shortcut icon" type="image/x-icon"
	href="${pageContext.request.contextPath}/resource/images/favicon.ico" />
<script type="text/javascript">
	var isWindowActive = true;
	$(function() {
// 		$(this).bind("contextmenu", function(e) {
//             e.preventDefault();
//         });

// 		$(window).on('unload', function() {
// 			if($('#page-unload-status').val() === 'close')
//  				$('#logout-btn').click();
// 		});

// 		$(window).on('keydown', function(e){
// 			// F12=123, I=73 (Ctrl+Shift+I), F5= ,R=82
// 			var allowKey = event.keyCode == 123
// 						   || (event.keyCode == 73 && event.shiftKey && event.ctrlKey)
// 						   || (event.keyCode == 82 && event.ctrlKey)
// 						   || event.keyCode == 116;
// 			if(allowKey) {
// 				e.preventDefault();
// 			}
// 		});

		$(window).blur(function(){
			  isWindowActive = false;
		});
		$(window).focus(function(){
				isWindowActive = true;
		});
		String.prototype.replaceAll = function(search, replacement) {
		    var target = this;
		    return target.replace(new RegExp(search, 'g'), replacement);
		};

		$('#msg-in-box').focus();

		getAllUsers();
		loadAllMessages();
 		loadAllEmojis();

		$('#msg-in-box').on('keyup', function() {
			if (!event.shiftKey) {
				var key = event.keyCode || event.charCode;
				if (key == 13) {
					if($('#enter-check').is(':checked')) {
						$('#send-btn').submit();
					}
				}
			}
		});
		$('#chat-form').submit(function(e) {
			var content = $('#msg-in-box').html();
			content = content.replaceAll('<div><br></div>', '\n').replaceAll('nbsp;', ' ');
			if (content.endsWith("\r\n")) {
				content = content.substring(0, msg.length - 2);
			} else if (content.endsWith("\n")) {
				content = content.substring(0, content.length - 1);
			}

			$('#msg-in-box-content').val(content.trim());

			if ($('#msg-in-box-content').val() !== "") {
				$('#event-id').val(1);
				sendPostData();
			}
			$('#msg-in-box').focus();
			e.preventDefault();
		});

		$("#msg-in-box").bind("paste", function(e){
 		    e.preventDefault();
		    var pastedData = e.originalEvent.clipboardData.getData('text');
		    document.execCommand("insertText", false, pastedData);
		    scrollToBottom('msg-in-box');
		} );

		$('#reload-btn').click(function(e) {
			$('#page-unload-status').val('reload')
			window.location.reload(true);
		});

		$('#logout-btn').click(function(e) {
			$('#event-id').val(2);
			sendPostData();
			e.preventDefault();
		});

		var size = [ window.outerWidth, window.outerHeight ];

		$('#err-box').bind("DOMSubtreeModified",function(){
			if($('#err-box').text() != ''){
				size = [ window.outerWidth, window.outerHeight + 27 ];
				$( window ).resize();
				$('#err-box').fadeOut(10000,'linear', function(){
					$('#err-box').empty();
					$('#err-box').show();
					size = [ window.outerWidth, window.outerHeight - 27 ];
					$(window).resize();
				});
			}
		});

		$('#emoji-list-opener').click(function(){
			var modifier = -50;
			if($('#emoji-container').is(':hidden')){
				modifier = -modifier;
			}

			$('#emoji-container').toggle();
			size = [ window.outerWidth, window.outerHeight + modifier ];
			$( window ).resize();
		});

		<c:if test = "${user.group eq 'admin'}">
			$('#clear-chat-btn').click(function(e){
				1==confirm('All chat records from all the users will be permanently removed. Press OK to proceed, or CANCEL to cancel cleraing.')&&($('#event-id').val(3)
						,sendPostData()
						,e.preventDefault())
			});
		</c:if>

		$(window).resize(function() {
			window.resizeTo(size[0], size[1]);
		});

		if (Notification.permission !== "granted")
		    Notification.requestPermission();
	});

	function notifyMe(senderName) {
		if (Notification.permission !== "granted")
			Notification.requestPermission();
		else {
			var a = $('#my-name').text();
			a = a.substring(a.lastIndexOf(':')+1, a.length)

		    var notification = new Notification('Hi ' + a + '!', {
		      icon: '${pageContext.request.contextPath}/resource/images/cr.png',
		      body: 'You have a new message from ' + senderName + '.',
		    });
			$(notification).css('cursor', 'pointer');
		    notification.onclick = function () {
		    	window.focus();
		    	notification.close();
		    };
		}
	}

	function sendPostData() {
		var form = $('#chat-form');
		var postData = form.serializeArray();
		var formURL = form.attr("action");
		var type = form.attr("method");
		var eventId = parseInt($('#event-id').val());

		var jqXhr = $.ajax({
			url : formURL,
			type : type,
			data : postData,
		});

		jqXhr
				.done(function(data) {
					switch (eventId) {
					case 1:
						if (data === 'true') {
							$('#msg-in-box').empty();
							$('#msg-in-box-content').val('');
							$('#err-box').empty();
						} else if (data === 'false') {
							$('#err-box').text(
									"Could not send message. Please retry!");
						} else {
							window.location.replace('${pageContext.request.contextPath}/error');
						}
						break;
					case 2:
						if (data === '0') {
							if (w1 != "undefined") {
								w1.terminate();
								w1 = undefined;
							}
							if (w2 != "undefined") {
								w2.terminate();
								w2 = undefined;
							}
							window.location.replace('${pageContext.request.contextPath}/init');
						} else {
							$('#err-box').text(
									"Could not logout. Please retry!");
						}
						break;
						<c:if test = "${user.group eq 'admin'}">
							case 3:
								'true'===data?($('#last-msg-id').val(-1),$('#err-box').empty(),$('#content-body').empty())
										:'false'===data?$('#err-box').text('Could not erase messages. Please retry!')
												:$(location).attr('href','${pageContext.request.contextPath}/error');
							break;
						</c:if>
					}
				});
	}

	function loadAllMessages() {
		var jqXhr = $.ajax({
			type : "GET",
			url : "${pageContext.request.contextPath}/chat/recover?token=" + $('#token').val(),
		});

		jqXhr
				.done(function(data) {
					if (data !== null && data !== '') {
						if(data !== '-1') {
							messageAppender(data);
							$('#err-box').empty();
						} else {
							$('#err-box')
									.text(
											"Could not load previous messages. Please retry by refreshing!");
						}
					}
					startChatWorker();
				});
	}

	function startChatWorker() {
		if (typeof (Worker) !== "undefined") {
			if (typeof (w2) == "undefined") {
				w2 = new Worker(URL.createObjectURL(new Blob([ "("
						+ loadMsg.toString() + ")()" ], {
					type : 'text/javascript'
				})));
			}
			var url = $(location).attr('origin') + "${pageContext.request.contextPath}/worker/load?msgId="
					+ $('#last-msg-id').val() + "&token=" + $('#token').val();
			w2.postMessage(url);
			w2.addEventListener('message', function(e) {
				if (e.data !== '' && e.data !== 'false') {
					if (e.data === "-1") {
						window.location.replace('${pageContext.request.contextPath}/error');
					} else {
						var response = JSON.parse(e.data);
						messageAppender(response);

// 						if(!isWindowActive)
// 							notifyMe();
					}
				}
				url = $(location).attr('origin') + "${pageContext.request.contextPath}/worker/load?msgId="
						+ $('#last-msg-id').val() + "&token="
						+ $('#token').val();
				w2.postMessage(url);
			}, false);
		} else {
			alert("Can't execute Chatroom. Upgrade your browser to modern one!!");
		}
	}

	function getAllUsers() {
		var jqXhr = $.ajax({
			type : "GET",
			url : "${pageContext.request.contextPath}/chat/allusers?token=" + $('#token').val(),
		});

		jqXhr
				.done(function(data) {
					if (data !== null && data !== '') {
					 	if (data === "-1") {
							window.location.replace('${pageContext.request.contextPath}/error');
						} else {
							$.each(data, function(i, item) {
								$('#user-list').append('<div class="u-details"><span class="users">'
										+ '<div class="status s-off"></div> '
										+ item.rname
										+ '</span><input type="hidden" value="'
										+ item.uname +'" class="users-uname"></div>');
							});
						}
					}
					startUserWorker();
				});
	}

	function loadAllEmojis(){
		var jqXhr = $.ajax({
			type : "GET",
			url : "${pageContext.request.contextPath}/chat/emoji?token="+ $('#token').val(),
		});

		jqXhr
				.done(function(data) {
					if (data !== null && data !== 'false') {

						if (data === "-1") {
							window.location.replace('${pageContext.request.contextPath}/error');
						} else {
							$.each(data, function(i, item) {
								var div = $('<div class="emoji-div"></div>');
								$('#emoji-container').append(div);
								var img = $('<img class="emoji" src="data:image/png;base64, '+ item.data + '" alt="<<' + item.name + '>>" title="' + item.name + '" />');
								div.append(img);

								img.click(function(){
									var newImg = $(this).clone();
									newImg.removeAttr('title');
									newImg.css('margin', '0 4px');
									$('#msg-in-box').append(newImg);
								});
							});
						}
						startUserWorker();
					}
				});
	}

	function startUserWorker() {
		if (typeof (Worker) !== "undefined") {
			if (typeof (w1) == "undefined") {
				w1 = new Worker(URL.createObjectURL(new Blob([ "("
						+ loadUsers.toString() + ")()" ], {
					type : 'text/javascript'
				})));
			}
			var url = $(location).attr('origin') + "${pageContext.request.contextPath}/worker/users?token="
					+ $('#token').val();
			w1.postMessage(url);
			w1.addEventListener('message', function(e) {
				if (e.data !== "") {
					if (e.data === "-1") {
						window.location.replace('${pageContext.request.contextPath}/error');
					} else {
						var data = JSON.parse(e.data);
						$('.s-on').removeClass('.s-on').addClass('s-off');
						$.each(data, function(i, item) {
							var elem = $('.u-details').find('.users-uname:input[value="' + item.uname + '"]');

							if(elem != undefined && elem != null && elem.length > 0) {
								$(elem.parent().find('.status')[0]).removeClass('s-off').addClass('s-on');
							}
						});
					}
				} else {
					$('.s-on').removeClass('.s-on').addClass('s-off');
				}
				url = $(location).attr('origin') + "${pageContext.request.contextPath}/worker/users?token="
						+ $('#token').val();
				w1.postMessage(url);
			}, false);
		} else {
			alert("Can't execute Chatroom. Upgrade your browser to modern one!!");
		}
	}

	function messageAppender(response) {
		for (var i = 0; i < response.length; i++) {
			var msgs = $('.msg');
			var lastTd = $(msgs[msgs.length - 1]);

			var lastSender = lastTd.find('.sender-ids').val();
			var lastSentAt = lastTd.find('.ts').text();
			lastSentAt = lastSentAt.substring(lastSentAt.indexOf('@')+1);
			var ts = response[i].sentTimestamp;
			ts = ts.substring(0, ts.lastIndexOf(':'));


			var dtGrp = getDateGroupDiv(ts);
			if(dtGrp !== null) {
				$('#content-body').append(dtGrp);
			}

			var elP;
			if(lastSender === response[i].username && lastSentAt === ts){
				var textContentP = elP =lastTd.find('p.text-content');
				var text = textContentP.html();
				textContentP.empty();
				textContentP.html(text+'\n'+response[i].message);
				lastTd.find('.msg-ids').val(response[i].messageId);
			} else {
				var div = $('<div class="msg">'
						+ '<input type="hidden" class="msg-ids" value="'+response[i].messageId+'">'
						+ '<input type="hidden" class="sender-ids" value="'+response[i].username+'">'
						+ '</div>');

				var msgMain = $('<div class="msg-main"></div>');
				var msgDiv = $('<div class="msg-div"></div>');
				var tsDiv = $('<div class="ts-div"></div>');
				var p1 = elP = $('<p class="text-content"></p>').html(
						response[i].senderName + ': ' + response[i].message);

				var p2 = '<p class="ts">Sent @' + ts + '</p>';

				$('#content-body').append(div);
				div.append(msgMain);
				msgMain.append(msgDiv);
				msgDiv.append(p1);
				msgMain.append(tsDiv);
				tsDiv.append(p2);
				tsDiv.hide();
				p1.click(function(){
					var parent = $(this).parent().parent().parent();
					var isLast = parent.is(':last-child');

					var duration = 0;
					if(!isLast)
						duration = 500;

					parent.find('.ts-div').fadeToggle(duration, function() {
						if(isLast)
							scrollToBottom('content');
					});
				});

				var username = response[i].username;
				if (username === $('#uname').val()) {
					div.addClass('msg-right');
					msgDiv.addClass('msg-self');
				} else {
					div.addClass('msg-left');
					msgDiv.addClass('msg-other');
					if(!isWindowActive)
						notifyMe(response[i].senderName);
				}
			}

			var content = elP.html();
			var regEx = /(https?:\/\/([-\w\.]+)+(:\d+)?(\/([\w\/_\.]*(\?\S+)?)?)?)/igm;

			var match = regEx.exec(content);
			while(match != null) {
				var link = match[0];
				var actLink = '<a href="'+link+'" target="_blank">'+link+'</a>';
				p1.html(content.replace(link, actLink));
				match = regEx.exec(content);
			}
		}
		setLastMsgId();
		scrollToBottom('content');
	}

	function scrollToBottom(id) {
		var content = $('#' + id);
		var height = content[0].scrollHeight;
		content.scrollTop(height);
	}

	function getDateGroupDiv(ts) {
		var tsLast = $('.ts:last');
		var proceed = true;
		var lastTs = ts.substring(0, ts.lastIndexOf(' '));

		if(tsLast.length > 0 ){
			var lastSentAt = tsLast.text();
			lastSentAt = lastSentAt.substring(lastSentAt.indexOf('@')+1, lastSentAt.lastIndexOf(' '));
			proceed = lastSentAt === lastTs ? false : true;
		}

		if(proceed){
			var dateParts = lastTs.split('-');
			var dtGrp = dateParts[0] + ' ';
			switch(parseInt(dateParts[1])) {
			case 1:
				dtGrp += 'JAN' + ' ';
				break;
			case 2:
				dtGrp += 'FEB' + ' ';
				break;
			case 3:
				dtGrp += 'MAR' + ' ';
				break;
			case 4:
				dtGrp += 'APR' + ' ';
				break;
			case 5:
				dtGrp += 'MAY' + ' ';
				break;
			case 6:
				dtGrp += 'JUN' + ' ';
				break;
			case 7:
				dtGrp += 'JUL' + ' ';
				break;
			case 8:
				dtGrp += 'AUG' + ' ';
				break;
			case 9:
				dtGrp += 'SEP' + ' ';
				break;
			case 10:
				dtGrp += 'OCT' + ' ';
				break;
			case 11:
				dtGrp += 'NOV' + ' ';
				break;
			case 12:
				dtGrp += 'DEC' + ' ';
				break;
			}
			dtGrp += dateParts[2];

			return $('<div class="dt-grp"><p style="margin: 0;">' + dtGrp + '</p></div>');
		}else {
			return null;
		}
	}

	function setLastMsgId() {
		var msgIds = $('.msg-ids');
		if (msgIds === undefined || msgIds.length === 0) {
			$('#last-msg-id').val(-1);
		} else {
			$('#last-msg-id').val($(msgIds[msgIds.length - 1]).val());
		}
	}
</script>
</head>
<body>
	<header id="heading">
		<div id="title">Intra-team Communication System</div>
		<div id="menu-div">
			<img
				src="${pageContext.request.contextPath}/resource/images/refresh.png"
				alt="Reload Page" height="28" width="28" title="Reload Page"
				class="img-btn" id="reload-btn">
			<c:if test="${user.group eq 'admin' }">
				<img
					src="${pageContext.request.contextPath}/resource/images/wipe.png"
					alt="Clean All Chats" height="28" width="28"
					title="Clean All Chats" class="img-btn" id="clear-chat-btn">
			</c:if>
			<img
				src="${pageContext.request.contextPath}/resource/images/logout.png"
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
		<div id="users-title">Logged in Users</div>
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
				<input type="hidden" name="token" id="token"
					value="<c:out value='${user.token}' />"> <input
					type="hidden" name="uname" id="uname"
					value="<c:out value='${user.username}' />"> <input
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