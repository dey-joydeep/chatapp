var websocket = null;

$(function() {
	window.onbeforeunload = closeSocket;
	function closeSocket(){
		if (websocket != null) {
			websocket.close();
		}
	}
	if (websocket == null) {

		var wsUrl = document.defaultView.location.origin.replace("http", "ws");
		wsUrl += contextPath + "/check?loginId=" + $('#login-id').val();
		websocket = new WebSocket(wsUrl);

		websocket.onmessage = function(e) {
			var resp = JSON.parse(e.data);
			switch (resp.notificationType) {
			case "status":
				setUserStatus(resp);
				break;
			case "message":
				setNewMessage(resp.content);
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