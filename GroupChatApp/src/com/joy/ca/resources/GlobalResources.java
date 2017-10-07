package com.joy.ca.resources;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.Session;

public class GlobalResources {
	public static volatile Map<Long, List<Session>> SESSION_HOLDER = new ConcurrentHashMap<>();
}
