package com.joy.ca.controller.websocket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONException;
import org.json.JSONObject;

import com.joy.ca.db.service.LoginService;
import com.joy.ca.db.service.LoginServiceImpl;
import com.joy.ca.process.CommonProcessor;
import com.joy.ca.resources.Commons;
import com.joy.ca.resources.GlobalResources;
import com.joy.ca.resources.Commons.Notification.TypeStatus;
import com.joy.ca.utils.logger.LogHandler;

@ServerEndpoint("/check")
public class WebSocketManager extends GlobalResources {

	private static final LogHandler _logger = new LogHandler(WebSocketManager.class);

	@OnOpen
	public void open(Session session) throws IOException, JSONException {
		manageOpenedSession(session);
	}

	@OnClose
	public void close(Session session) throws IOException, JSONException {
		Map<String, String> paramValMap = CommonProcessor.getRequestQuery(session.getQueryString());
		long loginId = Long.parseLong(paramValMap.get(Commons.ReqParams.LOGIN_ID));
		_logger.debug("A connection is closed by login ID: " + loginId);
		manageClosingSession(loginId, session.getId());
	}

	@OnMessage
	public void messageHandler(String message, Session session) {
		System.out.println("Session ID: " + session.getId());
		System.out.println("Message: " + message);
	}

	@OnError
	public void error(Throwable t) {
		t.printStackTrace();
		_logger.error(t);
	}

	private void manageOpenedSession(Session session) throws IOException, JSONException {
		Map<String, String> paramValMap = CommonProcessor.getRequestQuery(session.getQueryString());
		long loginId = Long.parseLong(paramValMap.get(Commons.ReqParams.LOGIN_ID));

		_logger.debug("A connection is opened by login ID: " + loginId);

		List<Session> sessionList = SESSION_HOLDER.get(loginId);

		// If no session is present for the user, create new list
		if (sessionList == null) {
			sessionList = new ArrayList<>();
		}
		sessionList.add(session);
		SESSION_HOLDER.put(loginId, sessionList);

		// Notify user(s) about login when other users are logged in and this user
		// logging in for the first time
		if (SESSION_HOLDER.size() > 1 && sessionList.size() == 1) {
			notifyUser(TypeStatus.NOTIFICATION_TYPE_STATUS, loginId, 1);
		}
	}

	private void manageClosingSession(long loginId, String sessionId) throws JSONException, IOException {
		// On closing a WS session, get the session details from SESSION_HOLDER
		List<Session> sessionList = SESSION_HOLDER.get(loginId);

		// Loop over all the available WS sessions
		for (int i = 0; i < sessionList.size(); i++) {
			Session session = sessionList.get(i);
			// When the ID of this session is matched to the ID of saved one
			if (session.getId().equals(sessionId)) {
				// Remove the session
				sessionList.remove(i);
				break;
			}
		}

		if (sessionList.size() == 0) {
			LoginService loginService = new LoginServiceImpl();
			loginService.updateLastOnline(loginId);
			notifyUser(TypeStatus.NOTIFICATION_TYPE_STATUS, loginId, 0);
		}
	}

	public void sendPong(Session session) {
		try {
			session.getBasicRemote().sendPong(ByteBuffer.wrap(new byte[] { (byte) 1 }));
		} catch (IllegalArgumentException | IOException e) {
			_logger.error(e);
		}
	}

	public void notifyUser(TypeStatus notificationType, long loginId, Object content)
			throws IOException, JSONException {
		boolean allowAll = false;
		JSONObject object = new JSONObject();
		String action;

		switch (notificationType) {
		case NOTIFICATION_TYPE_STATUS:
			object.put(Commons.JsonKeys.NOTOFICATION_TYPE, Commons.Notification.Type.STATUS);
			action = "login";
			if (0 == Integer.parseInt(content.toString())) {
				SESSION_HOLDER.remove(loginId);
				action = "logout";
			}
			break;
		case NOTIFICATION_TYPE_MESSAGE:
			allowAll = true;
			object.put(Commons.JsonKeys.NOTOFICATION_TYPE, Commons.Notification.Type.MESSAGE);
			action = "new message";
			break;
		default:
			return;
		}

		object.put(Commons.JsonKeys.SENDER_ID, loginId);
		object.put(Commons.JsonKeys.CONTENT, content);

		for (long id : SESSION_HOLDER.keySet()) {
			if (!(loginId != id || allowAll))
				continue;

			List<Session> otherSessionList = SESSION_HOLDER.get(id);
			for (Session otherSession : otherSessionList) {
				if (otherSession.isOpen()) {
					otherSession.getBasicRemote().sendText(object.toString());
					_logger.debug("Notify user about " + action + ". From ID: " + loginId + " To ID: " + id);
				}
			}
		}
	}
}
