function setUserStatus(data) {
	var elem = $('.u-details').find(
			'.users-login-id:input[value="' + data.senderId + '"]');

	if (elem != undefined && elem != null && elem.length > 0) {
		if (data.content === 1) {
			$(elem.parent().find('.status')[0]).removeClass('s-off').addClass(
					's-on');
		} else {
			$(elem.parent().find('.status')[0]).removeClass('s-on').addClass(
					's-off');
		}
	}
}

function setNewMessage(message) {
	if (message !== '' && message !== 'false') {
		if (message === "-1") {
			window.location.replace(contextPath + '/error');
		} else {
			var response = JSON.parse(message);
			messageAppender(response);
		}
	}
}

function messageAppender(response) {
	for (var i = 0; i < response.length; i++) {
		var msgs = $('.msg');
		var lastTd = $(msgs[msgs.length - 1]);
		var lastSender = lastTd.find('.sender-ids').val();
		var lastSentAt = lastTd.find('.ts').text();
		lastSentAt = lastSentAt.substring(lastSentAt.indexOf('@') + 1);
		var ts = response[i].sentTimestamp;
		ts = ts.substring(0, ts.lastIndexOf(':'));
		var dtGrp = getDateGroupDiv(ts);
		if (dtGrp !== null) {
			$('#content-body').append(dtGrp);
		}
		var elP;
		if (lastSender === response[i].loginId && lastSentAt === ts) {
			var textContentP = elP = lastTd.find('p.text-content');
			var text = textContentP.html();
			textContentP.empty();
			textContentP.html(text + '\n' + response[i].message);
			lastTd.find('.msg-ids').val(response[i].messageId);
		} else {
			var div = $('<div class="msg">'
					+ '<input type="hidden" class="msg-ids" value="'
					+ response[i].messageId + '">'
					+ '<input type="hidden" class="sender-ids" value="'
					+ response[i].loginId + '">' + '</div>');
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
			p1.click(function() {
				var parent = $(this).parent().parent().parent();
				var isLast = parent.is(':last-child');
				var duration = 0;
				if (!isLast)
					duration = 500;
				parent.find('.ts-div').fadeToggle(duration, function() {
					if (isLast)
						scrollToBottom('content');
				});
			});
			var loginId = response[i].loginId;
			if (loginId === $('#login-id').val()) {
				div.addClass('msg-right');
				msgDiv.addClass('msg-self');
			} else {
				div.addClass('msg-left');
				msgDiv.addClass('msg-other');
				if (!isWindowActive)
					notifyMe(response[i].senderName);
			}
		}
		var content = elP.html();
		var regEx = /(?:(?:https?|ftp|wss?):\/\/)(?:\S+(?::\S*)?@)?(?:(?!10(?:\.\d{1,3}){3})(?!127(?:\.\d{1,3}){3})(?!169\.254(?:\.\d{1,3}){2})(?!192\.168(?:\.\d{1,3}){2})(?!172\.(?:1[6-9]|2\d|3[0-1])(?:\.\d{1,3}){2})(?:[1-9]\d?|1\d\d|2[01]\d|22[0-3])(?:\.(?:1?\d{1,2}|2[0-4]\d|25[0-5])){2}(?:\.(?:[1-9]\d?|1\d\d|2[0-4]\d|25[0-4]))|(?:(?:[a-z\u{00a1-\uffff0-9]+-?)*[a-z\u00a1-\uffff0-9]+)(?:\.(?:[a-z\u00a1-\uffff0-9]+-?)*[a-z\u00a1-\uffff0-9]+)*(?:\.(?:[a-z\u00a1-\uffff}]{2,})))(?::\d{2,5})?(?:\/[^\s]*)?/igm;
		var match = regEx.exec(content);
		while (match != null) {
			var link = match[0];
			var actLink = '<a href="' + link + '" target="_blank">' + link
					+ '</a>';
			p1.html(content.replace(link, actLink));
			match = regEx.exec(content);
		}
	}
	setLastMsgId();
	scrollToBottom('content');
}

function getDateGroupDiv(ts) {
	var tsLast = $('.ts:last');
	var proceed = true;
	var lastTs = ts.substring(0, ts.lastIndexOf(' '));
	if (tsLast.length > 0) {
		var lastSentAt = tsLast.text();
		lastSentAt = lastSentAt.substring(lastSentAt.indexOf('@') + 1,
				lastSentAt.lastIndexOf(' '));
		proceed = lastSentAt === lastTs ? false : true;
	}
	if (proceed) {
		var dateParts = lastTs.split('-');
		var dtGrp = dateParts[0] + ' ';
		switch (parseInt(dateParts[1])) {
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
		return $('<div class="dt-grp"><p style="margin: 0;">' + dtGrp
				+ '</p></div>');
	} else {
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

function scrollToBottom(id) {
	var content = $('#' + id);
	var height = content[0].scrollHeight;
	content.scrollTop(height);
}
