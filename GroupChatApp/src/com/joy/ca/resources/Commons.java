package com.joy.ca.resources;

public interface Commons {

	interface ReqParams {
		public static final String LOGIN_ID = "loginId";
		public static final String USER_ID = "userId";
		public static final String PASSWORD = "password";
		public static final String AUTO_LOGIN = "autoLogin";
		public static final String EVENT_ID = "eventId";
		public static final String MESSAGE = "message";
	}

	interface ReqValues {
		public static final String AUTO_LOGIN = "1";
	}

	interface ReqAttribs {
		public static final String LOGIN_TOKEN = "login_token";
		public static final String LOGIN_ID = "loginId";
		public static final String USER_INFO = "user";
	}

	interface ReqHeaders {
		public static final String X_REQUEST = "X-Requested-With";
		public static final String USER_AGENT = "User-Agent";
	}

	interface SessionAttribs {
		public static final String LOGIN_ID = "loginId";
	}

	interface JsonKeys {
		public static final String LOGIN_ID = "loginId";
		public static final String SENDER_ID = "senderId";
		public static final String CONTENT = "content";
		public static final String NOTOFICATION_TYPE = "notificationType";
		public static final String SUCCESS = "success";
		public static final String MESSAGE = "message";
		public static final String ONLINE = "online";

		interface Emoji {
			public static final String CODE = "code";
			public static final String NAME = "name";
			public static final String DATA = "data";
		}
	}

	interface Notification {
		interface Type {
			public static final String STATUS = "status";
			public static final String MESSAGE = "message";
		}

		enum TypeStatus {
			NOTIFICATION_TYPE_STATUS, NOTIFICATION_TYPE_MESSAGE
		}
	}

	interface Cookies {
		public static final String AUTO_LOGIN = "_al";
		public static final String SEPERATOR = ":";
		public static final int AGE_30_DAYS = 30;
	}

	interface Redirect {
		interface Page {
			public static final String LOGIN_PAGE = "/WEB-INF/jsp/login.jsp";
			public static final String CLIENT = "/WEB-INF/jsp/client.jsp";
			public static final String ERROR_PAGE = "/WEB-INF/jsp/error.jsp";
		}

		interface Servlet {
			public static final String INIT = "/init";
			public static final String LOGIN = "/login";
			public static final String CHAT = "/chat";
			public static final String ERROR = "/err";
		}
	}

	interface Response {
		public static final String ERROR = "-1";
		public static final String SUCCESS = "0";

		interface ContentType {
			public static final String APPLICATION_JSON = "application/json";
		}
	}

	interface DateFormat {
		public static final String YYYYMMDD_HH24MMSS = "yyyy-MM-dd HH:mm:ss";
	}

	interface General {
		public static final String LF = "\n";
		public static final String CRLF = "\r\n";
		public static final String EMPTY_STRING = "";
		public static final String UTF_8 = "utf-8";
		public static final char URL_PATH_SEPERATOR = '/';
	}
}
