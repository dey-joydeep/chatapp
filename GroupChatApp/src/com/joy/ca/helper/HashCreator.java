package com.joy.ca.helper;

import com.joy.ca.utils.crypto.CryptoUtils;
import com.joy.ca.utils.crypto.CryptoUtils.CannotPerformOperationException;

public class HashCreator {

	public static void main(String[] args) throws CannotPerformOperationException {
		String[] passwords = { "123", "test1", "test2", "test3", "test4", "test5" };
		for (int i = 0; i < passwords.length; i++) {
			String hash = CryptoUtils.createHash(passwords[i]);
			System.out.println("Password: " + passwords[i]);
			System.out.println("Hash: " + hash + "\n");
		}
	}
}
