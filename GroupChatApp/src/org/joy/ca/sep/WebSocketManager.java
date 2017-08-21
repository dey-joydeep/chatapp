package org.joy.ca.sep;

import java.io.IOException;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.joy.ca.process.CommonProcessor;
import org.joy.ca.resources.GlobalResources;
import org.json.JSONException;
import org.json.JSONObject;

@ServerEndpoint("/check")
public class WebSocketManager extends GlobalResources {

	private static final int NOTIFICATION_TYPE_STATUS = 0x1;
	private static final int NOTIFICATION_TYPE_MESSAGE = 0x2;

	@OnOpen
	public void open(Session session) throws IOException, JSONException {
		manageSession(session);
	}

	@OnClose
	public void close(Session session) {
	}

	@OnError
	public void onError(Throwable error) {
	}

	@OnMessage
	public void handleMessage(String message, Session session) {
	}

	private void manageSession(Session session) throws IOException, JSONException {
		Map<String, String> paramValMap = CommonProcessor.getRequestQuery(session.getQueryString());
		int loginId = Integer.parseInt(paramValMap.get("loginId"));

		if (!WS_SESSION_MAP.containsKey(loginId)) {
			WS_SESSION_MAP.put(loginId, session);
			notifyUser(NOTIFICATION_TYPE_STATUS, loginId, null);
		}
		System.out.println("1 connection is opened");
	}

	private void notifyUser(int notificationType, int loginId, String content) throws IOException, JSONException {

		JSONObject object = new JSONObject();
		switch (notificationType) {
		case NOTIFICATION_TYPE_STATUS:
			object.put("senderId", loginId);
			object.put("notificationType", "status");
			object.put("status", 1);
			break;
		case NOTIFICATION_TYPE_MESSAGE:
			break;
		default:
			break;
		}

		for (int id : WS_SESSION_MAP.keySet()) {
			if (loginId != id) {
				Session otherSession = WS_SESSION_MAP.get(id);
				otherSession.getBasicRemote().sendText(object.toString());
			}
		}
	}
}
