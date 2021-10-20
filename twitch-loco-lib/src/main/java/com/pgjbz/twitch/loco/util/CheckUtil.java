package com.pgjbz.twitch.loco.util;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class CheckUtil {

	private CheckUtil(){}

	public static String requireNotBlank(String input, String message) {
		if(isBlank(input))
			throw new IllegalArgumentException(message);
		return input;
	}

	public static <T> T requireNonNull(T t, String message) {
		if (t == null)
			throw new IllegalArgumentException(message);
		return t;
	}

}
