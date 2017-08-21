package org.joy.ca.process;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;

import org.joy.ca.beans.UserInfo;
import org.joy.ca.db.entity.view.UserInfoView;
import org.joy.ca.db.service.UserInfoService;
import org.joy.ca.db.service.UserInfoServiceImpl;
import org.joy.ca.resources.CommonResources;
import org.joy.ca.resources.GlobalResources;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Common class to process general purpose tasks
 *
 * @author Joydeep Dey
 *
 */
public class CommonProcessor extends GlobalResources {

	private static final String PARAM_SPLITTER = "&";
	private static final String VALUE_SPLITTER = "=";

	/**
	 * Extract request parameters into {@link Map} for POST request
	 *
	 * @param inputStream
	 * @return Parameter-Value map
	 * @throws IOException
	 */
	public static final Map<String, String> getRequestBody(InputStream inputStream) throws IOException {
		Map<String, String> paramValueMap = new TreeMap<>();

		String line;
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		paramValueMap = getRequestQuery(sb.toString());
		return paramValueMap;
	}

	/**
	 * Extract request parameters into {@link Map} for GET request
	 *
	 * @param inputStream
	 * @return Parameter-Value map
	 * @throws IOException
	 */
	public static final Map<String, String> getRequestQuery(String query) throws IOException {
		Map<String, String> paramValueMap = new TreeMap<>();

		if (query != null && !query.isEmpty()) {
			String[] fragments = query.split(PARAM_SPLITTER);
			for (int i = 0; i < fragments.length; i++) {
				String paramValuePair = fragments[i];
				String[] paramValue = paramValuePair.split(VALUE_SPLITTER);
				String param = paramValue[0];
				String value = paramValue.length > 1 ? URLDecoder.decode(paramValue[1], "utf-8") : null;
				paramValueMap.put(param, value);
			}
		}
		return paramValueMap;
	}

	/**
	 * Read a file specified in the parameter
	 *
	 * @param resourceKey
	 *            Name of the file as key
	 * @return File contents
	 * @throws IOException
	 */
	public static final String getResource(String resourceKey) throws IOException {
		String resource = CommonResources.EMPTY_STRING;
		String filepath = RESOURCE_PATH_MAP.get(resourceKey);
		StringBuilder sb = new StringBuilder(512);

		if (filepath != null && !filepath.isEmpty()) {
			try (InputStreamReader r = new InputStreamReader(new FileInputStream(filepath), StandardCharsets.UTF_8)) {
				int c = 0;
				while ((c = r.read()) != -1) {
					sb.append((char) c);
				}
				resource = sb.toString();
			} catch (FileNotFoundException e) {
				System.err.println("Could not find the file: " + filepath);
			}
		}
		return resource;
	}

	/**
	 * Check for valid logged in user
	 *
	 * @param pageToken
	 * @param sessionToken
	 * @return
	 */
	public static final boolean checkLoggedInUserByToken(String pageToken, HttpSession session) {
		if (session == null)
			return false;
		Object sessionToken = session.getAttribute("token");
		return (pageToken != null && !pageToken.isEmpty() && sessionToken != null && pageToken.equals(sessionToken));
	}

	// TODO: To be replaced with DB implementation
	public static final UserInfo getUserDetailsByLoginId(int loginId) {

		UserInfo info = null;
		UserInfoService service = new UserInfoServiceImpl();
		UserInfoView userInfoView = service.getUserInfoByLoginId(loginId);
		if (userInfoView != null) {
			info = new UserInfo();
			info.setLoginId(loginId);
			info.setGroup(userInfoView.getGroup());
			info.setRealName(userInfoView.getFullname());
		}
		return info;
	}

	/**
	 * Check, if a string is in valid JSON structure
	 *
	 * @param json
	 * @return
	 */
	public static final boolean isJson(String json) {
		boolean isJson = false;
		if (json != null && !json.isEmpty()) {
			try {
				new JSONArray(json);
				isJson = true;
			} catch (JSONException e) {
				try {
					new JSONObject(json);
					isJson = true;
				} catch (JSONException e1) {
				}
			}
		}
		return isJson;

	}
}
