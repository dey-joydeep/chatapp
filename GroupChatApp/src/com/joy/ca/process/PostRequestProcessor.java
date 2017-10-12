package com.joy.ca.process;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.joy.ca.beans.RecieverInfoBean;
import com.joy.ca.controller.websocket.WebSocketManager;
import com.joy.ca.db.entity.table.LoginEntity;
import com.joy.ca.db.entity.table.LoginSessionEntity;
import com.joy.ca.db.entity.table.MessageEntity;
import com.joy.ca.db.entity.view.MessageInfoView;
import com.joy.ca.db.service.LoginService;
import com.joy.ca.db.service.LoginServiceImpl;
import com.joy.ca.db.service.LoginSessionService;
import com.joy.ca.db.service.LoginSessionServiceImpl;
import com.joy.ca.db.service.MessageInfoService;
import com.joy.ca.db.service.MessageInfoServiceImpl;
import com.joy.ca.db.service.MessageService;
import com.joy.ca.db.service.MessageServiceImpl;
import com.joy.ca.resources.Commons;
import com.joy.ca.resources.Messages;
import com.joy.ca.resources.Commons.Notification.TypeStatus;
import com.joy.ca.utils.crypto.CryptoUtils;
import com.joy.ca.utils.crypto.CryptoUtils.CannotPerformOperationException;
import com.joy.ca.utils.crypto.CryptoUtils.InvalidHashException;
import com.joy.ca.utils.logger.LogHandler;
import com.joy.ca.utils.validator.FormValidator;

/**
 * Handle tasks, associated with POST requests
 *
 * @author Joydeep Dey
 *
 */
public class PostRequestProcessor {

	private static final LogHandler _logger = new LogHandler(PostRequestProcessor.class);

	LoginService loginService = new LoginServiceImpl();
	private LoginSessionService loginSessionService = new LoginSessionServiceImpl();

	/**
	 * Process login request
	 *
	 * @param req
	 *            {@link HttpServletRequest}
	 * @param resp
	 * @return
	 */
	public String login(HttpServletRequest req, HttpServletResponse resp) {

		try {
			Long loginId = null;
			String errMesg = null;
			Date loginSessionExpiryDate = null;
			boolean insertLoginSession = false;
			Map<String, String> formData = CommonProcessor.getRequestBody(req.getInputStream());
			// Look for request attribute- loginToken:_al
			String loginToken = (String) req.getAttribute(Commons.ReqAttribs.LOGIN_TOKEN);

			// _al = null, when coming from login page
			// _al != null, when coming from InitServlet(/init)
			if (loginToken != null) {
				// verify login session by cookies
				String[] tokens = loginToken.split(Commons.Cookies.SEPERATOR);
				loginId = Long.valueOf(tokens[0]);
				LoginSessionEntity entity = new LoginSessionEntity();
				entity.setLoginId(loginId);
				entity.setSessionId(tokens[1]);
				loginSessionExpiryDate = loginSessionService.verifyLoginSessionAndGetExpiryDate(entity);

				// If no info is found(wrong token/token expired/auto login disable),
				// redirect to login page to ask for login by login id and password
				if (loginSessionExpiryDate == null)
					return null;

				// Delete the old cookie for this device and generate a new one
				LoginSessionEntity deleteEntity = new LoginSessionEntity();
				deleteEntity.setLoginId(loginId);
				deleteEntity.setSessionId(tokens[1]);
				loginSessionService.deleteLoginSession(deleteEntity);

				// If the session cookie expires, return to login page
				if (loginSessionExpiryDate.compareTo(new Date()) < 0)
					return null;

				insertLoginSession = true;
			} else {
				errMesg = FormValidator.validateLoginForm(formData);
				if (errMesg == null) {
					LoginEntity entity = loginService.getAuthenticationData(formData.get(Commons.ReqParams.USER_ID));
					if (CryptoUtils.verifyPassword(formData.get(Commons.ReqParams.PASSWORD), entity.getPassword()))
					if (entity != null) {
						String password = formData.get(Commons.ReqParams.PASSWORD);
						if (CryptoUtils.verifyPassword(password, entity.getPassword()))
							loginId = entity.getId();
					}
				}
			}

			JSONObject jsonObject = new JSONObject();
			if (loginId != null) {
				String autoLogin = formData.get(Commons.ReqParams.AUTO_LOGIN);
				if (autoLogin != null && autoLogin.equals(Commons.ReqValues.AUTO_LOGIN)) {
					insertLoginSession = true;
				}
				loginService.updateLastLogin(loginId);
				jsonObject.put(Commons.JsonKeys.SUCCESS, true);
				jsonObject.put(Commons.JsonKeys.LOGIN_ID, loginId);

				// Create HttpSession data
				HttpSession session = req.getSession(true);
				session.setAttribute(Commons.SessionAttribs.LOGIN_ID, loginId);

				// If login session cookie is required to store
				if (insertLoginSession) {
					LoginSessionEntity entity = new LoginSessionEntity();
					entity.setLoginId(loginId);
					int cookieMaxAge = 0;
					if (loginSessionExpiryDate == null) {
						cookieMaxAge = Commons.Cookies.AGE_30_DAYS * 3600;
						entity.setSessionExpiryDate(CommonProcessor.addDaysToCurrentDate(Commons.Cookies.AGE_30_DAYS));
					} else {
						cookieMaxAge = (int) (CommonProcessor.getDateDifference(loginSessionExpiryDate, new Date())
								/ 1000);
						entity.setSessionExpiryDate(loginSessionExpiryDate);
					}
					entity.setDeviceInfo(req.getHeader(Commons.ReqHeaders.USER_AGENT));
					entity.setIpAddress(CommonProcessor.getClientIpAddress(req));
					entity.setLastAccessedAt(new Date());
					loginSessionService.insertLoginSession(entity);
					String sessionId = entity.getSessionId();
					Cookie cookie = new Cookie(Commons.Cookies.AUTO_LOGIN,
							loginId + Commons.Cookies.SEPERATOR + sessionId);
					cookie.setMaxAge(cookieMaxAge);
					resp.addCookie(cookie);
				}
			} else {
				jsonObject.put(Commons.JsonKeys.SUCCESS, false);
				if (errMesg == null)
					errMesg = Messages.Login.CREDENTIAL_INVALID;
				jsonObject.put(Commons.JsonKeys.MESSAGE, errMesg);
			}
			_logger.debug("Login Response: " + jsonObject.toString());
			return jsonObject.toString();
		} catch (IOException | JSONException | CannotPerformOperationException | InvalidHashException e) {
			_logger.error(e);
			return Commons.General.EMPTY_STRING;
		}
	}

	/**
	 * Write the user message to DB
	 * 
	 * @param loginId
	 * @param message
	 * @param ipAddress
	 * @return
	 * @throws JSONException
	 */
	public String writeMessage(long loginId, String message, String ipAddress) throws JSONException {

		String status = Commons.Response.ERROR;
		try {
			message = URLDecoder.decode(message, Commons.General.UTF_8);

			if (message.endsWith(Commons.General.CRLF)) {
				message = message.substring(0, message.length() - 2);
			} else if (message.endsWith(Commons.General.LF)) {
				message = message.substring(0, message.length() - 1);
			}

			if (!message.isEmpty()) {
				System.out.println(message);
				MessageEntity entity = new MessageEntity();
				entity.setSenderId(loginId);
				entity.setSenderIp(ipAddress);
				entity.setMessage(message);
				entity.setSentAt(new Date());
				MessageService messageService = new MessageServiceImpl();
				messageService.insertMessage(entity);

				MessageInfoService messageInfoService = new MessageInfoServiceImpl();
				MessageInfoView messageInfoView = messageInfoService.getMessageById(entity.getMessageId());

				RecieverInfoBean recieverInfoBean = new RecieverInfoBean();
				recieverInfoBean.setMessageId(messageInfoView.getMessageId());
				recieverInfoBean.setSenderName(messageInfoView.getSenderName());
				recieverInfoBean.setSentTimestamp(CommonProcessor.dateToString(messageInfoView.getSentAt()));
				recieverInfoBean.setMessage(messageInfoView.getMessage());
				recieverInfoBean.setLoginId(String.valueOf(messageInfoView.getSenderId()));
				List<RecieverInfoBean> infoList = new ArrayList<>();
				infoList.add(recieverInfoBean);
				JSONArray array = new JSONArray(infoList);
				WebSocketManager wsManager = new WebSocketManager();
				wsManager.notifyUser(TypeStatus.NOTIFICATION_TYPE_MESSAGE, loginId, array.toString());
			}
			status = Boolean.TRUE.toString();
		} catch (IOException e) {
			status = Boolean.FALSE.toString();
			_logger.error(e);
		}

		return status;
	}

	public void logout(HttpServletRequest req, HttpServletResponse resp) {
		HttpSession session = req.getSession(false);
		session.invalidate();
		Cookie[] cookies = req.getCookies();
		for (Cookie cookie : cookies) {
			if (Commons.Cookies.AUTO_LOGIN.equals(cookie.getName())) {
				LoginSessionEntity entity = new LoginSessionEntity();
				String[] cookieVals = cookie.getValue().split(Commons.Cookies.SEPERATOR);

				if (cookieVals.length != 2)
					return;

				entity.setLoginId(Long.valueOf(cookieVals[0]));
				entity.setSessionId(cookieVals[1]);
				loginSessionService.deleteLoginSession(entity);
				cookie.setMaxAge(0);
				resp.addCookie(cookie);
			}
			break;
		}
	}
}
