package com.joy.ca.utils.validator;

import java.util.Map;

import com.joy.ca.resources.Commons;
import com.joy.ca.resources.Messages;

public class FormValidator {

	public static final String validateLoginForm(final Map<String, String> formData) {
		String message = null;
		String userId = formData.get(Commons.ReqParams.USER_ID);
		String password = formData.get(Commons.ReqParams.PASSWORD);

		if (userId == null || password == null)
			message = Messages.Login.CREDENTIAL_EMPTY;
		return message;
	}
}
