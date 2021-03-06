package net.xuwenhui.core.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 表单检查工具类
 * <p>
 * Created by xwh on 2016/4/12.
 */
public class FormCheckUtil {

	/**
	 * 非空检查
	 *
	 * @param input
	 * @return
	 */
	public static boolean isEmpty(String input) {
		return TextUtils.isEmpty(input);
	}

	/**
	 * 手机号格式检查
	 *
	 * @param phoneNum
	 * @return
	 */
	public static boolean checkPhoneNum(String phoneNum) {
		Pattern pattern = Pattern.compile("^1\\d{10}$");
		Matcher matcher = pattern.matcher(phoneNum);
		return matcher.matches();
	}

	/**
	 * 长度检查
	 *
	 * @param string
	 * @param minLength
	 * @param maxLength
	 * @return
	 */
	public static boolean checkLength(String string, int minLength, int maxLength) {
		return string.length() >= minLength && string.length() <= maxLength;
	}

}
