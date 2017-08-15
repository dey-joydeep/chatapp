package com.joy.gca.process;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.joy.gca.beans.UserInfo;
import com.joy.gca.resources.CommonResources;

public class CommonProcessor extends RequestProcessor {

	public static final Map<String, String> getRequestBody(InputStream inputStream) throws IOException {
		Map<String, String> paramValueMap = new TreeMap<>();

		String line;
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}

		String[] fragments = sb.toString().split("&");
		for (int i = 0; i < fragments.length; i++) {
			String paramValuePair = fragments[i];
			String[] paramValue = paramValuePair.split("=");
			String param = paramValue[0];
			String value = paramValue.length > 1 ? paramValue[1] : null;
			paramValueMap.put(param, value);
		}
		return paramValueMap;
	}

	public static final Map<String, String> getRequestQuery(String query) {
		Map<String, String> paramValueMap = new TreeMap<>();

		String[] fragments = query.split("&");
		for (int i = 0; i < fragments.length; i++) {
			String paramValuePair = fragments[i];
			String[] paramValue = paramValuePair.split("=");
			String param = paramValue[0];
			String value = paramValue.length > 1 ? paramValue[1] : null;
			paramValueMap.put(param, value);
		}
		return paramValueMap;
	}

	public static final String getStringResource(String filepath) throws IOException {
		String resource = null;
		StringBuilder sb = new StringBuilder(512);
		Reader r = null;
		InputStream stream = null;
		try {
			stream = new FileInputStream(filepath);
			r = new InputStreamReader(stream, StandardCharsets.UTF_8);
			int c = 0;
			while ((c = r.read()) != -1) {
				sb.append((char) c);
			}
			resource = sb.toString();
		} catch (FileNotFoundException e) {
			resource = "";
			System.err.println("Could not find the file: " + filepath);
		} finally {
			if (stream != null)
				stream.close();
			if (r != null)
				r.close();
		}
		return resource;
	}

	public static final Object getBinaryResource(String filepath) throws IOException {
		Path p = FileSystems.getDefault().getPath("", filepath);
		byte[] fileData = Files.readAllBytes(p);
		return fileData;
	}

	public static final boolean checkLoggedInUserByToken(String token) {
		for (UserInfo info : USER_LIST) {
			if (info.getToken().equals(token)) {
				return true;
			}
		}
		return false;
	}

	public static final UserInfo getUserDetailsByToken(String token) {

		for (UserInfo info : USER_LIST) {
			String storedToken = info.getToken();
			if (storedToken.equals(token)) {
				return info;
			}
		}
		return null;
	}

	public static final String getErrorPage() {
		StringBuffer sb = new StringBuffer();
		sb.append("<html><head><title>Chatroom- Error</title></head><body>");
		sb.append("<div style=\"width:100%; color:red; font-size:1em;\">");
		sb.append("Error: Possible reason could be- ");
		sb.append("<ul>");
		sb.append("<li>Unauthencated access</li>");
		sb.append("<li>You might have logged out.</li>");
		sb.append("<li>Error occurred during request processing.</li>");
		sb.append("</ul>");
		sb.append("</div>");
		sb.append("<div style=\"width:100%; font-size:1em;\"> Try again by <a href=\""
				+ CommonResources.APPLICATION_CONTEXT + "/init\">re-login</a><div>");
		sb.append("</body></html>");
		return sb.toString();
	}

	public static final String getResponseContentType(String path, Object data) {

		boolean isFound = false;
		String contentType = "text/html";

		if (data != null) {

			try {
				new JSONArray(data.toString());
				contentType = "application/json";
				isFound = true;
			} catch (Exception e) {
				try {
					new JSONObject(data.toString());
					contentType = "application/json";
					isFound = true;
				} catch (JSONException e1) {
				}
			}
		}
		if (!isFound) {
			String type = path.lastIndexOf('.') != -1 ? path.substring(path.lastIndexOf('.') + 1, path.length()) : path;

			switch (type) {
			case "css":
				contentType = "text/css";
				break;
			case "js":
				contentType = "text/javascript";
				break;
			case "jpg":
			case "jpeg":
				contentType = "image/jpeg";
			case "ico":
				contentType = "image/x-icon";
			case "png":
				contentType = "image/png";
				break;
			default:
				break;
			}
		}
		return contentType;
	}

	public static final int checkFileType(String filepath) {
		int type = 0;
		if (filepath != null) {
			String ext = filepath.lastIndexOf('.') != -1
					? filepath.substring(filepath.lastIndexOf('.') + 1, filepath.length())
					: filepath;

			switch (ext) {
			case "css":
			case "js":
			case "html":
				type = 0;
				break;
			case "jpg":
			case "jpeg":
			case "ico":
			case "png":
				type = 1;
				break;
			default:
				break;
			}
		}
		return type;
	}
}
