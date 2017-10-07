package com.joy.ca.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.joy.ca.beans.UserInfo;
import com.joy.ca.db.entity.view.UserInfoView;
import com.joy.ca.db.service.UserInfoService;
import com.joy.ca.db.service.UserInfoServiceImpl;
import com.joy.ca.resources.Commons;

/**
 * Common class to process general purpose tasks
 *
 * @author Joydeep Dey
 *
 */
public class CommonProcessor {

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
	 * Check for user authentication
	 *
	 * @param session
	 * @return
	 */
	public static final boolean isAuthenticated(HttpSession session) {
		return (session != null && session.getAttribute("loginId") != null);
	}

	/**
	 * Get user details by login ID
	 * 
	 * @param loginId
	 * @return user info
	 */
	public static final UserInfo getUserDetailsByLoginId(long loginId) {

		UserInfo info = null;
		UserInfoService service = new UserInfoServiceImpl();
		UserInfoView userInfoView = service.getUserInfoByLoginId(loginId);
		if (userInfoView != null) {
			info = new UserInfo();
			info.setLoginId(loginId);
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

	public static final boolean isEmptyString(String str, boolean isStrict) {
		return str == null ? true : (isStrict ? str.trim().isEmpty() : str.isEmpty());
	}

	public static final boolean isAsyncReq(HttpServletRequest req) {
		return "XMLHttpRequest".equals(req.getHeader("X-Requested-With"));
	}

	public static final String dateToString(Date date) {
		return new SimpleDateFormat(Commons.DateFormat.YYYYMMDD_HH24MMSS).format(date);
	}

	public static final Date addDaysToCurrentDate(int daysToAdd) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.add(Calendar.DATE, daysToAdd);
		return calendar.getTime();
	}

	public static final Date addDaysToDate(Date date, int daysToAdd) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, daysToAdd);
		return calendar.getTime();
	}

	/**
	 * Get time in milliseconds
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static final long getDateDifference(Date date1, Date date2) {
		return date1.getTime() - date2.getTime();
	}

	public static String getClientIpAddress(HttpServletRequest req) {
		 String ip = req.getHeader("X-Forwarded-For");  
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	            ip = req.getHeader("Proxy-Client-IP");  
	        }  
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	            ip = req.getHeader("WL-Proxy-Client-IP");  
	        }  
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	            ip = req.getHeader("HTTP_CLIENT_IP");  
	        }  
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	            ip = req.getHeader("HTTP_X_FORWARDED_FOR");  
	        }  
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	            ip = req.getRemoteAddr();  
	        }  
	        return ip;  
	}
}
