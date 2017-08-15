package com.joy.gca.process;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.joy.gca.beans.MessageBean;
import com.joy.gca.beans.UserInfo;
import com.joy.gca.resources.CommonResources;
import com.sun.net.httpserver.HttpExchange;

public class PostRequestProcessor extends RequestProcessor {

	public static String handlePost(HttpExchange t) throws IOException, JSONException {
		String response = null;
		String path = t.getRequestURI().getPath();
		path = path.endsWith("/") ? path.substring(0, path.length() - 1) : path;

		Map<String, String> params = CommonProcessor.getRequestBody(t.getRequestBody());

		String[] pathFragments = path.split("/");
		if (pathFragments.length > 3) {
			response = "-1";
		} else {
			String command = pathFragments[2];

			if ("login".equals(command)) {

				response = login(params, t.getRemoteAddress().getAddress().getHostAddress());
			} else if ("chat".equals(command)) {
				response = getClientPage(params.get("token"));
				response = response.replaceAll(CommonResources.KEY_APP_CTX, CommonResources.APPLICATION_CONTEXT);

			} else if ("send".equals(command)) {

				switch (params.get("sendId")) {
				case "1":
					response = writeMessage(params, t.getRemoteAddress().getAddress().getCanonicalHostName());
					break;
				case "2":
					response = logout(params.get("token"));
					break;
				case "3":
					response = clearChats(params.get("token"));
					break;
				default:
					break;
				}
			}
		}
		return response;
	}

	/**
	 *
	 * @param formData
	 * @param ip
	 * @return JSON
	 * @throws IOException
	 * @throws JSONException
	 */
	private static String login(Map<String, String> formData, String ip) throws IOException, JSONException {
		int errorType = 1;
		UserInfo info = null;
		String token = null;

		String credentials = CommonProcessor.getStringResource(CommonResources.CREDENTIAL_DATA);
		JSONArray jsonArray = new JSONArray(credentials);

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);

			String username = jsonObject.getString("username");
			if (formData.get("uname").equals(username)
					&& formData.get("pass").equals(jsonObject.getString("password"))) {

				info = getUserDetailsByUsername(username);
				if (info == null) {
					info = new UserInfo();
					info.setIpAddress(ip);
					info.setRealName(jsonObject.getString("name"));
					info.setUsername(username);
					info.setGroup(jsonObject.getString("group"));
					info.setUsername(username);
					token = UUID.randomUUID().toString();
					info.setToken(token);
					USER_LIST.add(info);
					errorType = 0;
					System.out.println("1 User is logged in: " + info.getRealName() + "(" + username + ")");
				} else {
					if (ip.equals(info.getIpAddress())) {
						errorType = 0;
						token = info.getToken();
					} else {
						errorType = 2;
					}
				}

				break;
			}
		}
		JSONObject jsonObject = new JSONObject();
		switch (errorType) {
		case 0:
			jsonObject.put("success", true);
			jsonObject.put("token", token);
			break;
		case 1:
			jsonObject.put("success", false);
			jsonObject.put("message", "Invalid username/password.");
			break;
		case 2:
			jsonObject.put("success", false);
			jsonObject.put("message",
					"The user is already logged in from different system (" + info.getIpAddress() + ")");
			break;
		default:
			break;
		}

		return jsonObject.toString();
	}

	/**
	 *
	 * @param username
	 * @return
	 */
	private static UserInfo getUserDetailsByUsername(String username) {

		for (UserInfo info : USER_LIST) {
			String storedUsername = info.getUsername();
			if (storedUsername.equals(username)) {
				return info;
			}
		}
		return null;
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

				clientPage = clientPage.replaceAll(CommonResources.KEY_APP_CTX, CommonResources.APPLICATION_CONTEXT)
						.replace(CommonResources.KEY_TOKEN_VAL, token)
						.replace(CommonResources.KEY_UNAME_VAL, info.getUsername())
						.replace(CommonResources.KEY_REAL_NAME, info.getRealName());

				return clientPage;
			}
		}

		return CommonProcessor.getErrorPage();
	}

	/**
	 *
	 * @param params
	 * @param hostName
	 * @return Status
	 * @throws IOException
	 *             , JSONException
	 * @throws IOException
	 * @throws JSONException
	 */
	private static String writeMessage(Map<String, String> params, String hostName) throws IOException, JSONException {
		String status = null;
		if (CommonProcessor.checkLoggedInUserByToken(params.get("token"))) {
			try {
				String message = URLDecoder.decode(params.get("message"), "utf-8");

				if (message.endsWith("\r\n")) {
					message = message.substring(0, message.length() - 2);
				} else if (message.endsWith("\n")) {
					message = message.substring(0, message.length() - 1);
				}

				if (!message.isEmpty()) {
					UserInfo info = CommonProcessor.getUserDetailsByToken(params.get("token"));
					MessageBean messageBean = new MessageBean();
					messageBean.setSenderName(info.getRealName());
					messageBean.setUsername(info.getUsername());
					messageBean.setSenderPcName(hostName);
					messageBean.setSenderIp(info.getIpAddress());
					messageBean.setSentTimestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					messageBean.setMessage(message);

					if (MSG_MAP.size() == 100) {
						MSG_MAP.clear();
						msgId = 0;
					}
					MSG_MAP.put(msgId++, messageBean);

					writeJsonFile();

					logMessage(messageBean);
				}
				status = Boolean.TRUE.toString();
			} catch (IOException | JSONException e) {
				MSG_MAP.remove(--msgId);
				status = Boolean.FALSE.toString();
				throw e;
			}
		} else {
			status = "-1";
		}
		return status;
	}

	/**
	 *
	 * @throws IOException
	 * @throws JSONException
	 */
	private static void writeJsonFile() throws IOException, JSONException {
		String chats = "";
		JSONArray jsonArray = null;

		chats = CommonProcessor.getStringResource(CommonResources.CHAT_DATA);

		if (!chats.isEmpty()) {
			jsonArray = new JSONArray(chats);
			JSONObject jsonObject = new JSONObject(MSG_MAP.get(msgId - 1));
			jsonObject.put("messageId", msgId - 1);
			jsonArray.put(jsonObject);
		} else {
			jsonArray = new JSONArray();

			for (Long key : MSG_MAP.keySet()) {
				JSONObject jsonObject = new JSONObject(MSG_MAP.get(key));
				jsonObject.put("messageId", key);
				jsonArray.put(jsonObject);
			}
		}

		OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(CommonResources.CHAT_DATA, false),
				StandardCharsets.UTF_8);
		writer.write(jsonArray.toString(3));
		writer.close();

	}

	private static void logMessage(MessageBean messageBean) {
		System.out.println("Message ID: " + msgId);
		System.out.println("Sender Name: " + messageBean.getSenderName());
		System.out.println("Sender IP: " + messageBean.getSenderIp());

		System.out.println("Sent Timestamp: " + messageBean.getSentTimestamp());
		System.out.println("Message: " + messageBean.getMessage());
	}

	private static String clearChats(String token) {
		String delStat = Boolean.FALSE.toString();

		if (CommonProcessor.checkLoggedInUserByToken(token)) {
			if (USER_LIST.size() == 1) {
				File file = new File(CommonResources.CHAT_DATA);

				if (file.isFile() && file.canExecute() && file.delete()) {
					msgId = 0;
					MSG_MAP.clear();
					delStat = Boolean.TRUE.toString();
				}
			}
		} else {
			delStat = "-1";
		}

		return delStat;
	}

	private static String logout(String token) {
		for (int i = 0; i < USER_LIST.size(); i++) {
			if (USER_LIST.get(i).getToken().equals(token)) {
				System.out.println("1 User is logged out: " + USER_LIST.get(i).getRealName() + "("
						+ USER_LIST.get(i).getUsername() + ")");
				USER_LIST.remove(i);
				return "0";
			}
		}
		return "-1";
	}
}
