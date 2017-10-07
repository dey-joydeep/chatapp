package com.joy.ca.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.joy.ca.db.entity.table.EmojiEntity;
import com.joy.ca.db.service.EmojiService;
import com.joy.ca.db.service.EmojiServiceImpl;

public class EmojiUploader {

	public static void main(String[] args) throws JSONException {
		String filepath = "D:\\Project\\Java\\Git/chatapp/GroupChatApp/WebContent/data/emoji.json";
		File f = new File(filepath);
		StringBuilder sb = new StringBuilder(512);

		try (InputStreamReader r = new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8)) {
			int c = 0;
			while ((c = r.read()) != -1) {
				sb.append((char) c);
			}
			JSONArray jsonArray = new JSONArray(sb.toString());
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject object = jsonArray.getJSONObject(i);
				EmojiEntity entity = new EmojiEntity();
				entity.setEmojiCode(object.getString("code"));
				entity.seteGroupId(1);
				entity.setEmojiName(object.getString("name"));
				entity.setEmojiData(object.getString("data").getBytes(StandardCharsets.UTF_8));

				EmojiService service = new EmojiServiceImpl();
				service.insertEmoji(entity);

				System.out.println((i + 1) + " Record inserted. Name: " + entity.getEmojiName());
			}
		} catch (IOException e) {
			System.err.println("Could not find the file: " + f.getAbsolutePath());
		}
	}
}
