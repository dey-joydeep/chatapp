package com.joy.gca.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.joy.gca.beans.MessageBean;
import com.joy.gca.beans.UserInfo;

public class GlobalResources {

	protected static long msgId = 0;
	protected static final Map<Long, MessageBean> MSG_MAP = new TreeMap<>();
	protected static final List<UserInfo> USER_LIST = new ArrayList<>();
}
