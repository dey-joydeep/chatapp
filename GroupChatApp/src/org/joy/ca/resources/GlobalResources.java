package org.joy.ca.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.websocket.Session;

import org.joy.ca.beans.MessageBean;
import org.joy.ca.beans.UserInfo;

public class GlobalResources {
	/********************************************************************************************/
	// Below two fields are required to revise for their usage
	public static long msgId = 0;
	public static final Map<Long, MessageBean> MSG_MAP = new TreeMap<>();
	/********************************************************************************************/

	// TODO: DB replacement
	public static final List<UserInfo> USER_LIST = new ArrayList<>();

	// TODO: to be handled as noted in InitServlet.init()
	public static final Map<String, String> RESOURCE_PATH_MAP = new HashMap<>();

	public static final Map<Integer, Session> WS_SESSION_MAP = new HashMap<>();
}
