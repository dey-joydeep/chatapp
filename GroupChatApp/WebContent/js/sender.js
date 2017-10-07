function sendMessage() {
	var content = $('#msg-in-box').html();
	content = content.replaceAll('<div><br></div>', '\n').replaceAll('&nbsp;',
			' ');
	if (content.endsWith("\r\n")) {
		content = content.substring(0, msg.length - 2);
	} else if (content.endsWith("\n")) {
		content = content.substring(0, content.length - 1);
	}
	content = content.trim();
	if (content !== "") {
		$('#msg-in-box-content').val(encodeURIComponent(content));
		$('#event-id').val(1);
		sendPostData();
	}
	$('#msg-in-box').focus();
}

function logout() {
	$('#event-id').val(2);
	sendPostData();
}

function sendPostData() {
	var form = $('#chat-form');
	var postData = form.serializeArray();
	var formURL = form.attr("action");
	var type = form.attr("method");
	var jqXhr = $.ajax({
		url : formURL,
		type : type,
		data : postData,
	});
	jqXhr.done(function(data) {
		var eventId = parseInt($('#event-id').val());
		switch (eventId) {
		case 1:
			if (data === 'true') {
				$('#msg-in-box').empty();
				$('#msg-in-box-content').val('');
				$('#err-box').empty();
			} else if (data === 'false') {
				$('#err-box').text("Could not send message. Please retry!");
			} else {
				window.location.replace(contextPath + '/error');
			}
			break;
		case 2:
			window.location.replace(contextPath);
			break;
		}
	});
}