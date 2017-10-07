package com.joy.ca.helper;

import com.joy.ca.utils.ua.UserAgentInfo;

public class UserAgentTester {

	public static void main(String[] args) {
		String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36";
		String accept = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8";
		UserAgentInfo userAgentInfo = new UserAgentInfo(userAgent, accept);
		userAgentInfo.initDeviceScan();
		while (!userAgentInfo.initCompleted)
			;
		
		if (userAgentInfo.isAndroid) {
			System.out.println();
		}
		if (userAgentInfo.isAndroidPhone) {
			System.out.println();
		}
		if (userAgentInfo.isIphone) {
			System.out.println();
		}
		if (userAgentInfo.isMobilePhone) {
			System.out.println();
		}
		if (userAgentInfo.isTierGenericMobile) {
			System.out.println();
		}
		if (userAgentInfo.isTierIphone) {
			System.out.println();
		}
		if (userAgentInfo.isTierRichCss) {
			System.out.println();
		}
		if (userAgentInfo.isTierTablet) {
			System.out.println();
		}
		if (userAgentInfo.isWebkit) {
			System.out.println();
		}
	}
}
