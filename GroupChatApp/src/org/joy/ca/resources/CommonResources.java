package com.joy.gca.resources;

public class CommonResources {

	public static final String APPLICATION_CONTEXT = "/app";

	public static final String SERVER_PATH = System.getProperty("user.dir");
	public static final String INIT_PAGE = SERVER_PATH + APPLICATION_CONTEXT
			+ "/resource/html/init.html";
	public static final String LOGIN_PAGE = SERVER_PATH + APPLICATION_CONTEXT
			+ "/resource/html/login.html";
	public static final String CLIENT_PAGE = SERVER_PATH + APPLICATION_CONTEXT
			+ "/resource/html/client.html";
	public static final String CREDENTIAL_DATA = SERVER_PATH
			+ APPLICATION_CONTEXT + "/resource/data/users.json";
	public static final String CHAT_DATA = SERVER_PATH + APPLICATION_CONTEXT
			+ "/resource/data/chats.json";
	public static final String EMOJI_DATA = SERVER_PATH + APPLICATION_CONTEXT
			+ "/resource/data/emoji.json";

	public static final String RESPONSE_PAGE_UNDERCONSTRUCTION = "<!DOCTYPE html><html><title>Chatroom- Notice</title><body><div style=\"width:100%; text-align:center; color:green; font-size:1em;\">This page is under construction!!</div></body></html>";

	public static final String KEY_EVENT = "##SPECIAL_APPENDER_EVENT##";
	public static final String SPECIAL_APPENDER_EVENT = "$('#clear-chat-btn').click(function(e){1==confirm('All chat records from all the users will be permanently removed. Press OK to proceed, or CANCEL to cancel cleraing.')&&($('#send-id').val(3),sendPostData(),e.preventDefault())});";
	public static final String KEY_CASE = "##SPECIAL_APPENDER_CASE##";
	public static final String SPECIAL_APPENDER_CASE = "case 3:'true'===data?($('#last-msg-id').val(-1),$('#err-box').empty(),$('#content-body').empty()):'false'===data?$('#err-box').text('Could not erase messages. Please retry!'):$(location).attr('href','"
			+ APPLICATION_CONTEXT + "/err');break;";
	public static final String KEY_BUTTON = "##SPECIAL_APPENDER_BUTTON##";
	public static final String SPECIAL_APPENDER_BUTTON = "<img src=\"##APPLICATION_CONTEXT##/resource/images/wipe.png\" alt=\"Clean All Chats\" height=\"28\" width=\"28\" title=\"Clean All Chats\" class=\"img-btn\" id=\"clear-chat-btn\">";
	public static final String REPLACE_PATTERN = "^[(##SPECIAL_APPENDER_].+[##]";

	public static final String KEY_APP_CTX = "##APPLICATION_CONTEXT##";
	public static final String KEY_TOKEN_VAL = "##TOKEN_VALUE##";
	public static final String KEY_UNAME_VAL = "##UNAME_VALUE##";
	public static final String KEY_REAL_NAME = "##REALNAME##";
}
