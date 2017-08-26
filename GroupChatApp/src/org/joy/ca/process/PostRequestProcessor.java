package org.joy.ca.process;

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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.joy.ca.beans.MessageBean;
import org.joy.ca.db.service.UserInfoService;
import org.joy.ca.db.service.UserInfoServiceImpl;
import org.joy.ca.resources.CommonResources;
import org.joy.ca.resources.GlobalResources;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Handle tasks, associated with POST requests
 *
 * @author Joydeep Dey
 *
 */
public class PostRequestProcessor extends GlobalResources {

	/**
	 * Process login request
	 *
	 * @param req
	 *            {@link HttpServletRequest}
	 * @return JSON string containing the login status
	 */
	public String login(HttpServletRequest req) {
		Map<String, String> formData = null;

		try {
			formData = CommonProcessor.getRequestBody(req.getInputStream());
			UserInfoService userInfoService = new UserInfoServiceImpl();
			JSONObject jsonObject = new JSONObject();

			Integer loginId = userInfoService.authenticateUser(formData);
			if (loginId != null) {
				String token = UUID.randomUUID().toString();
				jsonObject.put("success", true);
				jsonObject.put("token", token);
				jsonObject.put("loginId", loginId);
				// Create new session on successful login
				HttpSession session = req.getSession(true);
				session.setAttribute("token", token);
			} else {
				jsonObject.put("success", false);
				jsonObject.put("message", "Invalid login ID/password.");
			}
			return jsonObject.toString();
		} catch (IOException | JSONException e) {
			return CommonResources.EMPTY_STRING;
		}
	}

	/**
	 * FIXME: Write the user message to JSON file (later in DB)
	 *
	 * @param params
	 * @param hostName
	 * @return
	 */
	public String writeMessage(int loginId, String message, String ipAddress) {

		String status = CommonResources.RESPONSE_ERROR;
		try {
			message = URLDecoder.decode(message, "utf-8");

			if (message.endsWith("\r\n")) {
				message = message.substring(0, message.length() - 2);
			} else if (message.endsWith("\n")) {
				message = message.substring(0, message.length() - 1);
			}

			if (!message.isEmpty()) {
				MessageBean messageBean = new MessageBean();
				messageBean.setLoginId(String.valueOf(loginId));
				messageBean.setSenderIp(ipAddress);
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
			status = Boolean.FALSE.toString();
			MSG_MAP.remove(--msgId);
			e.printStackTrace();
		}

		return status;
	}

	/**
	 * FIXME: Write new message into JSON file. Later to be written in DB
	 *
	 * @throws IOException
	 * @throws JSONException
	 */
	private void writeJsonFile() throws IOException, JSONException {
		String chats = "";
		JSONArray jsonArray = null;

		chats = CommonProcessor.getResource(CommonResources.CHAT_DATA);

		// Add message to JSON or append to the existing
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

	/**
	 * Writes message to console (TODO: Method could be removed)
	 *
	 * @param messageBean
	 */
	private void logMessage(MessageBean messageBean) {
		System.out.println("Message ID: " + msgId);
		System.out.println("Sender ID: " + messageBean.getLoginId());
		System.out.println("Sender IP: " + messageBean.getSenderIp());

		System.out.println("Sent Timestamp: " + messageBean.getSentTimestamp());
		System.out.println("Message: " + messageBean.getMessage());
	}

	/**
	 * FIXME: Physically clear entire chat records. The implementation of this
	 * method is required to modify. The clearing could be either physical or
	 * logical, depending on the executor of this action. As the chat is basically
	 * conducting between two parties, when any one is deleting the chats, a delete
	 * flag is required to set. When the flag is set for both the parties, a
	 * physical delete can be executed.
	 *
	 * @return String representation of boolean value. true, if success, false
	 *         otherwise.
	 */
	public String clearChats() {
		String delStat = Boolean.FALSE.toString();

		if (USER_LIST.size() == 1) {
			File file = new File(CommonResources.CHAT_DATA);

			if (file.isFile() && file.canExecute() && file.delete()) {
				msgId = 0;
				MSG_MAP.clear();
				delStat = Boolean.TRUE.toString();
			}
		}

		return delStat;
	}

	/**
	 * FIXME: This method could be deleted as USER_LIST will be removed from the
	 * scenario. A push notification will be sent rather to inform that this user is
	 * offline currently.
	 *
	 * @param token
	 * @return
	 */
	public String logout(String token) {
		return CommonResources.RESPONSE_SUCCESS;
	}
}
