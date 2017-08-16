package org.joy.ca.process;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.joy.ca.beans.MessageBean;
import org.joy.ca.beans.RecieverInfoBean;
import org.joy.ca.beans.UserInfo;
import org.joy.ca.resources.CommonResources;
import org.joy.ca.resources.GlobalResources;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Handle tasks, associated with GET requests
 *
 * @author Joydeep Dey
 *
 */
public class GetRequestProcessor extends GlobalResources {

	/***
	 * Get status of friends (online/offline).<br>
	 * FIXME: To be replaced by server push
	 *
	 * @param token
	 * @return
	 */
	public String getLoggedInUsers(String token) {

		JSONObject object = null;
		JSONArray array = new JSONArray();
		try {
			for (UserInfo info : USER_LIST) {
				if (!info.getToken().equals(token)) {
					object = new JSONObject();
					object.put("uname", info.getUsername());
					object.put("rname", info.getRealName());
					array.put(object);
				}
			}
			return array.length() == 0 ? "" : array.toString();
		} catch (JSONException e) {
			e.printStackTrace();
			return "-1";
		}

	}

	/**
	 * Load the friend list<br>
	 * FIXME: To be loaded from database
	 *
	 * @param token
	 * @return
	 */
	public String getAllUsers(String token) {

		try {
			JSONObject object = null;
			JSONObject newData = null;
			JSONArray array = new JSONArray();
			String credentials = CommonProcessor
					.getResource(CommonResources.CREDENTIAL_DATA);
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
		} catch (JSONException | IOException e) {
			e.printStackTrace();
			return CommonResources.RESPONSE_ERROR;
		}
	}

	/**
	 * Get all previous chats.<br>
	 * FIXME: To be loaded from database
	 *
	 * @return
	 */
	public String getOldChats() {
		String chats = CommonResources.EMPTY_STRING;
		try {
			chats = CommonProcessor.getResource(CommonResources.CHAT_DATA);

			if (!chats.isEmpty()) {
				JSONArray jsonArray = new JSONArray(chats);

				JSONObject object = jsonArray
						.getJSONObject(jsonArray.length() - 1);
				long lastMsgId = object.getLong("messageId");
				if (msgId <= lastMsgId)
					msgId = lastMsgId + 1;
			}
		} catch (IOException | JSONException e) {
			e.printStackTrace();
			chats = CommonResources.RESPONSE_ERROR;
		}
		return chats;
	}

	/**
	 * Get the latest message. <br>
	 * FIXME: To be replaced by server push
	 *
	 * @param lastMsgId
	 * @return
	 */
	public String getMessage(int lastMsgId) {

		String jsonResponse = null;
		List<RecieverInfoBean> latestMessages = new ArrayList<>();

		long startMsgId = ++lastMsgId;
		if (startMsgId > 100)
			startMsgId %= 100;

		MessageBean messageBean = null;
		while ((messageBean = MSG_MAP.get(startMsgId++)) != null) {
			long count = 0;
			RecieverInfoBean recieverInfoBean = new RecieverInfoBean();

			if (messageBean != null) {

				recieverInfoBean.setMessageId(lastMsgId + count++);
				recieverInfoBean.setSenderPcName(messageBean.getSenderPcName());
				recieverInfoBean.setSenderName(messageBean.getSenderName());
				recieverInfoBean.setSentTimestamp(messageBean
						.getSentTimestamp());
				recieverInfoBean.setMessage(messageBean.getMessage());
				recieverInfoBean.setUsername(messageBean.getUsername());
				latestMessages.add(recieverInfoBean);
			}
		}

		if (!latestMessages.isEmpty()) {
			JSONArray array = new JSONArray(latestMessages);
			jsonResponse = array.toString();
		} else {
			jsonResponse = "";
		}
		return jsonResponse;

	}

	/**
	 * Load the emojis.<br>
	 * FIXME: To be modified. Emojis will be categorized in tab, click on that,
	 * emojis will be loaded. Loading emojis might be incremental too, if
	 * required.
	 *
	 * @return All emojis in as JSON
	 */
	public String loadEmojis() {
		String emojis = CommonResources.RESPONSE_ERROR;
		try {
			emojis = CommonProcessor.getResource(CommonResources.EMOJI_DATA);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return emojis;
	}
}
