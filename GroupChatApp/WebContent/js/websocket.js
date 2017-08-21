$(function() {
	if (typeof (websocket) == "undefined" || websocket == null) {

		var wsUrl = document.origin.replace("http", "ws");
		wsUrl += contextPath + "/check?uname=" + $('#uname').val();
		websocket = new WebSocket(wsUrl);

		websocket.onmessage = function(e) {
			var msg = JSON.parse(e.data);
			switch (msg.notificationType) {
			case "status":
				setUserStatus(msg);
				break;
			default:
				break;
			}
		};
		websocket.onerror = function(e) {
		};
		websocket.onclose = function(e) {
			websocket = null;
		};
	}
});

function setUserStatus(data) {
	var elem = $('.u-details').find(
			'.users-uname:input[value="' + data.senderId + '"]');

	if (elem != undefined && elem != null && elem.length > 0) {
		if (data.status === 1) {
			$(elem.parent().find('.status')[0]).removeClass('s-off').addClass(
					's-on');
		} else {
			$(elem.parent().find('.status')[0]).removeClass('s-on').addClass(
					's-off');
		}
	}
}