package com.joy.gca.process;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.joy.gca.beans.MessageBean;
import com.joy.gca.beans.RecieverInfoBean;
import com.joy.gca.beans.UserInfo;
import com.joy.gca.resources.CommonResources;
import com.sun.net.httpserver.HttpExchange;

public class GetRequestProcessor extends RequestProcessor {

	public static Object handleGet(HttpExchange t) throws IOException, JSONException {
		Object response = null;
		String path = t.getRequestURI().getPath();

		path = path.endsWith("/") ? path.substring(0, path.length() - 1) : path;
		String command = path.substring(path.lastIndexOf('/') + 1, path.length());
		int idx = path.lastIndexOf("/");

		if (idx == 0) {

			String page = CommonProcessor.getStringResource(CommonResources.INIT_PAGE);
			page = page.replaceAll(CommonResources.KEY_APP_CTX, CommonResources.APPLICATION_CONTEXT);
			response = page;

		} else {

			String query = t.getRequestURI().getQuery();

			if (query != null && !query.isEmpty()) {
				Map<String, String> paramValueMap = CommonProcessor.getRequestQuery(query);

				if (paramValueMap.size() == 1 && paramValueMap.containsKey("token")) {

					if (command.equals("chat")) {
						String page = getClientPage(paramValueMap.get("token"));
						page = page.replaceAll(CommonResources.KEY_APP_CTX, CommonResources.APPLICATION_CONTEXT);
						response = page;
					} else if (command.equals("users")) {
						response = getLoggedInUsers(paramValueMap.get("token"));
					} else if (command.equals("allusers")) {
						response = getAllUsers(paramValueMap.get("token"));
					} else if (command.equals("emoji")) {
						response = loadEmojis(paramValueMap.get("token"));
					}

				} else if (paramValueMap.size() == 2 && paramValueMap.containsKey("msgId")
						&& paramValueMap.containsKey("token")) {

					response = getMessage(Integer.parseInt(paramValueMap.get("msgId")), paramValueMap.get("token"));

				}
			} else {
				if (command.contains("init")) {

					String page = CommonProcessor.getStringResource(CommonResources.LOGIN_PAGE);
					page = page.replaceAll(CommonResources.KEY_APP_CTX, CommonResources.APPLICATION_CONTEXT);
					response = page;

				} else if (command.contains("recover")) {

					response = getOldChats();

				} else if (command.contains("err")) {

					response = CommonProcessor.getErrorPage();

				} else {

					path = CommonResources.SERVER_PATH + path;
					if (CommonProcessor.checkFileType(path) == 0) {
						response = CommonProcessor.getStringResource(path);
					} else {
						response = CommonProcessor.getBinaryResource(path);
					}
				}
			}

			if (response != null) {
				String contentType = CommonProcessor.getResponseContentType(path, response);

				t.getResponseHeaders().set("Content-Type", contentType + "; charset=" + Charset.defaultCharset());
			}
		}

		return response;
	}

	private static String getClientPage(String token) throws IOException {

		for (UserInfo info : USER_LIST) {
			if (info.getToken().equals(token)) {

				String clientPage = CommonProcessor.getStringResource(CommonResources.CLIENT_PAGE);

				if ("admin".equals(info.getGroup())) {
					clientPage = clientPage.replace(CommonResources.KEY_BUTTON,
							CommonResources.SPECIAL_APPENDER_BUTTON);
					clientPage = clientPage.replace(CommonResources.KEY_EVENT, CommonResources.SPECIAL_APPENDER_EVENT);
					clientPage = clientPage.replace(CommonResources.KEY_CASE, CommonResources.SPECIAL_APPENDER_CASE);
				} else {
					clientPage = clientPage.replace(CommonResources.KEY_BUTTON, "");
					clientPage = clientPage.replace(CommonResources.KEY_EVENT, "");
					clientPage = clientPage.replace(CommonResources.KEY_CASE, "");
				}

				clientPage.replaceAll(CommonResources.KEY_APP_CTX, CommonResources.APPLICATION_CONTEXT);

				return clientPage;
			}
		}

		return CommonProcessor.getErrorPage();
	}

	private static String getLoggedInUsers(String token) throws JSONException {
		if (!CommonProcessor.checkLoggedInUserByToken(token)) {
			return "-1";
		}
		JSONObject object = null;
		JSONArray array = new JSONArray();
		for (UserInfo info : USER_LIST) {
			if (!info.getToken().equals(token)) {
				object = new JSONObject();
				object.put("uname", info.getUsername());
				object.put("rname", info.getRealName());
				array.put(object);
			}
		}

		return array.length() == 0 ? "" : array.toString();
	}

	private static String getAllUsers(String token) throws JSONException, IOException {
		if (!CommonProcessor.checkLoggedInUserByToken(token)) {
			return "-1";
		}
		JSONObject object = null;
		JSONObject newData = null;
		JSONArray array = new JSONArray();
		String credentials = CommonProcessor.getStringResource(CommonResources.CREDENTIAL_DATA);
		UserInfo info = CommonProcessor.getUserDetailsByToken(token);
		JSONArray jsonArray = new JSONArray(credentials);

		for (int i = 0; i < jsonArray.length(); i++) {
			object = new JSONObject(jsonArray.get(i).toString());
			String uname = object.getString("username");
			if (!uname.equals(info.getUsername())) {
				newData = new JSONObject();
				newData.put("uname", uname);
				newData.put("rname", object.get("name"));
				array.put(newData);
			}
		}
		return array.toString();
	}

	private static String getOldChats() throws IOException, JSONException {
		String chats = CommonProcessor.getStringResource(CommonResources.CHAT_DATA);

		if (!chats.isEmpty()) {
			JSONArray jsonArray = new JSONArray(chats);

			JSONObject object = jsonArray.getJSONObject(jsonArray.length() - 1);
			long lastMsgId = object.getLong("messageId");
			if (msgId <= lastMsgId)
				msgId = lastMsgId + 1;
		}
		return chats;
	}

	private static String getMessage(int lastMsgId, String token) {
		if (!CommonProcessor.checkLoggedInUserByToken(token)) {
			return "-1";
		}
		String jsonResponse = null;
		List<RecieverInfoBean> latestMessages = new ArrayList<>();

		long startMsgId = ++lastMsgId;
		if (startMsgId > 100)
			startMsgId %= 100;

		MessageBean messageBean = null;
		while ((messageBean = MSG_MAP.get(startMsgId++)) != null) {
			long count = 0;
			RecieverInfoBean recieverInfoBean = new RecieverInfoBean();

			recieverInfoBean.setMessageId(lastMsgId + count++);
			recieverInfoBean.setSenderPcName(messageBean.getSenderPcName());
			recieverInfoBean.setSenderName(messageBean.getSenderName());
			recieverInfoBean.setSentTimestamp(messageBean.getSentTimestamp());
			recieverInfoBean.setMessage(messageBean.getMessage());
			recieverInfoBean.setUsername(messageBean.getUsername());
			latestMessages.add(recieverInfoBean);
		}

		if (!latestMessages.isEmpty()) {
			JSONArray array = new JSONArray(latestMessages);
			jsonResponse = array.toString();
		} else {
			jsonResponse = "";
		}
		return jsonResponse;
	}

	private static Object loadEmojis(String string) throws IOException {
		String chats = CommonProcessor.getStringResource(CommonResources.EMOJI_DATA);
		return chats;
	}
}
