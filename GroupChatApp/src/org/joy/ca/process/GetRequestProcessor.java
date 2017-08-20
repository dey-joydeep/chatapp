package org.joy.ca.process;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.joy.ca.beans.MessageBean;
import org.joy.ca.beans.RecieverInfoBean;
import org.joy.ca.beans.UserInfo;
import org.joy.ca.db.entity.table.EmojiEntity;
import org.joy.ca.db.entity.view.UserInfoView;
import org.joy.ca.db.service.EmojiService;
import org.joy.ca.db.service.EmojiServiceImpl;
import org.joy.ca.db.service.UserInfoService;
import org.joy.ca.db.service.UserInfoServiceImpl;
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
					object.put("loginId", info.getLoginId());
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
	public String getAllFriends(int loginId) {

		UserInfoService service = new UserInfoServiceImpl();
		List<UserInfoView> friendList = service.getFriendList(loginId);

		try {
			JSONObject object = null;
			JSONArray array = new JSONArray();

			for (UserInfoView userInfo : friendList) {
				object = new JSONObject(userInfo);
				array.put(object);
			}
			return array.toString();
		} catch (JSONException e) {
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

				JSONObject object = jsonArray.getJSONObject(jsonArray.length() - 1);
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

		String jsonResponse = CommonResources.EMPTY_STRING;
		List<RecieverInfoBean> latestMessages = new ArrayList<>();

		long startMsgId = ++lastMsgId;

		MessageBean messageBean = null;
		while ((messageBean = MSG_MAP.get(startMsgId++)) != null) {
			long count = 0;
			RecieverInfoBean recieverInfoBean = new RecieverInfoBean();

			recieverInfoBean.setMessageId(lastMsgId + count++);
			recieverInfoBean.setSenderName("XXX");
			recieverInfoBean.setSentTimestamp(messageBean.getSentTimestamp());
			recieverInfoBean.setMessage(messageBean.getMessage());
			recieverInfoBean.setLoginId(messageBean.getLoginId());
			latestMessages.add(recieverInfoBean);
		}

		if (!latestMessages.isEmpty()) {
			JSONArray array = new JSONArray(latestMessages);
			jsonResponse = array.toString();
		}
		return jsonResponse;

	}

	/**
	 * Load the emojis.<br>
	 * FIXME: To be modified. Emojis will be categorized in tab, click on that,
	 * emojis will be loaded. Loading emojis might be incremental too, if required.
	 *
	 * @return All emojis in as JSON
	 */
	public String loadEmojis() {
		EmojiService service = new EmojiServiceImpl();
		List<EmojiEntity> emojiList = service.getAllEmoji();
		JSONArray array = new JSONArray();
		for (EmojiEntity emoji : emojiList) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("code", emoji.getEmojiCode());
			jsonObject.put("name", emoji.getEmojiName());
			jsonObject.put("data", new String(emoji.getEmojiData(), StandardCharsets.UTF_8));
			array.put(jsonObject);
		}
		return array.toString();
	}
}
