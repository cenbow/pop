package com.ai.bdx.pop.wsclient.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

public class StringUtil {
	public static String escape(String src) {
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length() * 6);

		for (int i = 0; i < src.length(); i++) {
			char j = src.charAt(i);

			if ((Character.isDigit(j)) || (Character.isLowerCase(j))
					|| (Character.isUpperCase(j))) {
				tmp.append(j);
			} else if (j < 'Ā') {
				tmp.append("%");
				if (j < '\020')
					tmp.append("0");
				tmp.append(Integer.toString(j, 16));
			} else {
				tmp.append("%u");
				tmp.append(Integer.toString(j, 16));
			}
		}
		return tmp.toString();
	}

	public static String unescape(String src) {
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length());
		int lastPos = 0;
		int pos = 0;

		while (lastPos < src.length()) {
			pos = src.indexOf("%", lastPos);
			if (pos == lastPos) {
				if (src.charAt(pos + 1) == 'u') {
					char ch = (char) Integer.parseInt(
							src.substring(pos + 2, pos + 6), 16);

					tmp.append(ch);
					lastPos = pos + 6;
				} else {
					char ch = (char) Integer.parseInt(
							src.substring(pos + 1, pos + 3), 16);

					tmp.append(ch);
					lastPos = pos + 3;
				}
			} else if (pos == -1) {
				tmp.append(src.substring(lastPos));
				lastPos = src.length();
			} else {
				tmp.append(src.substring(lastPos, pos));
				lastPos = pos;
			}
		}

		return tmp.toString();
	}

	public static String trimPrefix(String toTrim, String trimStr) {
		while (toTrim.startsWith(trimStr)) {
			toTrim = toTrim.substring(trimStr.length());
		}
		return toTrim;
	}

	public static String trimSufffix(String toTrim, String trimStr) {
		while (toTrim.endsWith(trimStr)) {
			toTrim = toTrim.substring(0, toTrim.length() - trimStr.length());
		}
		return toTrim;
	}

	public static String trim(String toTrim, String trimStr) {
		return trimSufffix(trimPrefix(toTrim, trimStr), trimStr);
	}

	public static String escapeHtml(String content) {
		return StringEscapeUtils.escapeHtml(content);
	}

	public static String unescapeHtml(String content) {
		return StringEscapeUtils.unescapeHtml(content);
	}

	public static boolean isEmpty(String str) {
		if (str == null)
			return true;
		if (str.trim().equals(""))
			return true;
		return false;
	}

	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	public static String subString(String str, int len) {
		int strLen = str.length();
		if (strLen < len)
			return str;
		char[] chars = str.toCharArray();
		int cnLen = len * 2;
		String tmp = "";
		int iLen = 0;
		for (int i = 0; i < chars.length; i++) {
			int iChar = chars[i];
			if (iChar <= 128)
				iLen += 1;
			else
				iLen += 2;
			if (iLen >= cnLen)
				break;
			tmp = new StringBuilder().append(tmp)
					.append(String.valueOf(chars[i])).toString();
		}
		return tmp;
	}

	public static boolean isNumberic(String s) {
		if (StringUtils.isEmpty(s))
			return false;
		boolean rtn = validByRegex("^[-+]{0,1}\\d*\\.{0,1}\\d+$", s);
		if (rtn)
			return true;

		return validByRegex("^0[x|X][\\da-eA-E]+$", s);
	}

	public static boolean isInteger(String s) {
		boolean rtn = validByRegex("^[-+]{0,1}\\d*$", s);
		return rtn;
	}

	public static boolean isEmail(String s) {
		boolean rtn = validByRegex(
				"(\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*)*", s);
		return rtn;
	}

	public static boolean isMobile(String s) {
		boolean rtn = validByRegex(
				"^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\\d{8})$", s);
		return rtn;
	}

	public static boolean isPhone(String s) {
		boolean rtn = validByRegex(
				"(0[0-9]{2,3}\\-)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?", s);
		return rtn;
	}

	public static boolean isZip(String s) {
		boolean rtn = validByRegex("^[0-9]{6}$", s);
		return rtn;
	}

	public static boolean isQq(String s) {
		boolean rtn = validByRegex("^[1-9]\\d{4,9}$", s);
		return rtn;
	}

	public static boolean isIp(String s) {
		boolean rtn = validByRegex(
				"^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$",
				s);
		return rtn;
	}

	public static boolean isChinese(String s) {
		boolean rtn = validByRegex("^[一-龥]+$", s);
		return rtn;
	}

	public static boolean isChrNum(String s) {
		boolean rtn = validByRegex("^([a-zA-Z0-9]+)$", s);
		return rtn;
	}

	public static boolean isUrl(String url) {
		return validByRegex(
				"(http://|https://)?([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?",
				url);
	}

	public static boolean validByRegex(String regex, String input) {
		Pattern p = Pattern.compile(regex, 2);
		Matcher regexMatcher = p.matcher(input);
		return regexMatcher.find();
	}

	public static boolean isNumeric(String str) {
		for (int i = str.length(); --i >= 0;)
			if (!Character.isDigit(str.charAt(i)))
				return false;

		return true;
	}

	public static String makeFirstLetterUpperCase(String newStr) {
		if (newStr.length() == 0) {
			return newStr;
		}
		char[] oneChar = new char[1];
		oneChar[0] = newStr.charAt(0);
		String firstChar = new String(oneChar);
		return new StringBuilder().append(firstChar.toUpperCase())
				.append(newStr.substring(1)).toString();
	}

	public static String makeFirstLetterLowerCase(String newStr) {
		if (newStr.length() == 0) {
			return newStr;
		}
		char[] oneChar = new char[1];
		oneChar[0] = newStr.charAt(0);
		String firstChar = new String(oneChar);
		return new StringBuilder().append(firstChar.toLowerCase())
				.append(newStr.substring(1)).toString();
	}

	public static String formatParamMsg(String message, Object[] args) {
		for (int i = 0; i < args.length; i++) {
			message = message.replace(new StringBuilder().append("{").append(i)
					.append("}").toString(), args[i].toString());
		}
		return message;
	}

	public static String formatParamMsg(String message, Map params) {
		if (params == null)
			return message;
		Iterator keyIts = params.keySet().iterator();
		while (keyIts.hasNext()) {
			String key = (String) keyIts.next();
			Object val = params.get(key);
			if (val != null) {
				message = message.replace(new StringBuilder().append("${")
						.append(key).append("}").toString(), val.toString());
			}
		}
		return message;
	}

	public static StringBuilder formatMsg(CharSequence msgWithFormat,
			boolean autoQuote, Object[] args) {
		int argsLen = args.length;
		boolean markFound = false;

		StringBuilder sb = new StringBuilder(msgWithFormat);

		if (argsLen > 0) {
			for (int i = 0; i < argsLen; i++) {
				String flag = new StringBuilder().append("%").append(i + 1)
						.toString();
				int idx = sb.indexOf(flag);

				while (idx >= 0) {
					markFound = true;
					sb.replace(idx, idx + 2, toString(args[i], autoQuote));
					idx = sb.indexOf(flag);
				}
			}

			if ((args[(argsLen - 1)] instanceof Throwable)) {
				StringWriter sw = new StringWriter();
				((Throwable) args[(argsLen - 1)])
						.printStackTrace(new PrintWriter(sw));
				sb.append("\n").append(sw.toString());
			} else if ((argsLen == 1) && (!markFound)) {
				sb.append(args[(argsLen - 1)].toString());
			}
		}
		return sb;
	}

	public static StringBuilder formatMsg(String msgWithFormat, Object[] args) {
		return formatMsg(new StringBuilder(msgWithFormat), true, args);
	}

	public static String toString(Object obj, boolean autoQuote) {
		StringBuilder sb = new StringBuilder();
		if (obj == null) {
			sb.append("NULL");
		} else if ((obj instanceof Object[])) {
			for (int i = 0; i < ((Object[]) obj).length; i++) {
				sb.append(((Object[]) (Object[]) obj)[i]).append(", ");
			}
			if (sb.length() > 0)
				sb.delete(sb.length() - 2, sb.length());
		} else {
			sb.append(obj.toString());
		}

		if ((autoQuote)
				&& (sb.length() > 0)
				&& ((sb.charAt(0) != '[') || (sb.charAt(sb.length() - 1) != ']'))
				&& ((sb.charAt(0) != '{') || (sb.charAt(sb.length() - 1) != '}'))) {
			sb.insert(0, "[").append("]");
		}
		return sb.toString();
	}

	public static String returnSpace(String str) {
		String space = "";
		if (!str.isEmpty()) {
			String[] path = str.split("\\.");
			for (int i = 0; i < path.length - 1; i++) {
				space = new StringBuilder().append(space)
						.append("&nbsp;&emsp;").toString();
			}
		}
		return space;
	}

//	public static synchronized String encryptSha256(String inputStr) {
//		try {
//			MessageDigest md = MessageDigest.getInstance("SHA-256");
//			byte[] digest = md.digest(inputStr.getBytes("UTF-8"));
//			return new String(Base64.encodeBase64(digest));
//		} catch (Exception e) {
//		}
//		return null;
//	}

	public static synchronized String encryptMd5(String inputStr) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(inputStr.getBytes());
			byte[] digest = md.digest();
			StringBuffer sb = new StringBuffer();
			for (byte b : digest) {
				sb.append(Integer.toHexString(b & 0xFF));
			}

			return sb.toString();
		} catch (Exception e) {
		}
		return null;
	}

	public static String getArrayAsString(List<String> arr) {
		if ((arr == null) || (arr.size() == 0))
			return "";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.size(); i++) {
			if (i > 0)
				sb.append(",");
			sb.append((String) arr.get(i));
		}
		return sb.toString();
	}

	public static String getArrayAsString(String[] arr) {
		if ((arr == null) || (arr.length == 0))
			return "";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			if (i > 0)
				sb.append(",");
			sb.append(arr[i]);
		}
		return sb.toString();
	}

	public static String getSetAsString(Set set) {
		if ((set == null) || (set.size() == 0))
			return "";
		StringBuffer sb = new StringBuffer();
		int i = 0;
		Iterator it = set.iterator();
		while (it.hasNext()) {
			if (i++ > 0)
				sb.append(",");
			sb.append(it.next().toString());
		}
		return sb.toString();
	}

	// cjh
	public static String hangeToBig(double value) {
		char[] hunit = { '拾', '佰', '仟' };
		char[] vunit = { '万', '亿' };
		char[] digit = { 38646, '壹', 36144, '叁', 32902, '伍', 38470, '柒', '捌',
				'玖' };
		// char hunit[] = { '\u62FE', '\u4F70', '\u4EDF' };
		// char vunit[] = { '\u4E07', '\u4EBF' };
		// char digit[] = { '\u96F6', '\u58F9', '\u8D30', '\u53C1', '\u8086',
		// '\u4F0D', '\u9646', '\u67D2', '\u634C', '\u7396' };
		// char[] hunit = { 25342, 20336, 20191 };
		// char[] vunit = { 19975, 20159 };
		// char[] digit = { 38646, 22777, 36144, 21441, 32902, 20237, 38470,
		// 26578, 25420, 29590 };

		long midVal = (long) (value * 100.0D);
		String valStr = String.valueOf(midVal);

		String head = valStr.substring(0, valStr.length() - 2);
		String rail = valStr.substring(valStr.length() - 2);

		String prefix = "";
		String suffix = "";

		if (rail.equals("00")) {
			suffix = "整";
		} else {
			suffix = new StringBuilder().append(digit[(rail.charAt(0) - '0')])
					.append("角").append(digit[(rail.charAt(1) - '0')])
					.append("分").toString();
		}

		char[] chDig = head.toCharArray();
		char zero = '0';
		byte zeroSerNum = 0;
		for (int i = 0; i < chDig.length; i++) {
			int idx = (chDig.length - i - 1) % 4;
			int vidx = (chDig.length - i - 1) / 4;
			if (chDig[i] == '0') {
				zeroSerNum = (byte) (zeroSerNum + 1);
				if (zero == '0') {
					zero = digit[0];
				} else if ((idx == 0) && (vidx > 0) && (zeroSerNum < 4)) {
					prefix = new StringBuilder().append(prefix)
							.append(vunit[(vidx - 1)]).toString();
					zero = '0';
				}
			} else {
				zeroSerNum = 0;
				if (zero != '0') {
					prefix = new StringBuilder().append(prefix).append(zero)
							.toString();
					zero = '0';
				}
				prefix = new StringBuilder().append(prefix)
						.append(digit[(chDig[i] - '0')]).toString();
				if (idx > 0)
					prefix = new StringBuilder().append(prefix)
							.append(hunit[(idx - 1)]).toString();
				if ((idx == 0) && (vidx > 0)) {
					prefix = new StringBuilder().append(prefix)
							.append(vunit[(vidx - 1)]).toString();
				}
			}
		}
		if (prefix.length() > 0)
			prefix = new StringBuilder().append(prefix).append('圆').toString();
		return new StringBuilder().append(prefix).append(suffix).toString();
	}

	public static String htmlEntityToString(String dataStr) {
		dataStr = dataStr.replace("&apos;", "'").replace("&quot;", "\"")
				.replace("&gt;", ">").replace("&lt;", "<")
				.replace("&amp;", "&");

		int start = 0;
		int end = 0;
		StringBuffer buffer = new StringBuffer();

		while (start > -1) {
			int system = 10;
			if (start == 0) {
				int t = dataStr.indexOf("&#");
				if (start != t) {
					start = t;
				}
				if (start > 0) {
					buffer.append(dataStr.substring(0, start));
				}
			}
			end = dataStr.indexOf(";", start + 2);
			String charStr = "";
			if (end != -1) {
				charStr = dataStr.substring(start + 2, end);

				char s = charStr.charAt(0);
				if ((s == 'x') || (s == 'X')) {
					system = 16;
					charStr = charStr.substring(1);
				}
			}
			try {
				char letter = (char) Integer.parseInt(charStr, system);
				buffer.append(new Character(letter).toString());
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}

			start = dataStr.indexOf("&#", end);
			if (start - end > 1) {
				buffer.append(dataStr.substring(end + 1, start));
			}

			if (start == -1) {
				int length = dataStr.length();
				if (end + 1 != length) {
					buffer.append(dataStr.substring(end + 1, length));
				}
			}
		}
		return buffer.toString();
	}

	public static String stringToHtmlEntity(String str) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);

			switch (c) {
			case '\n':
				sb.append(c);
				break;
			case '<':
				sb.append("&lt;");
				break;
			case '>':
				sb.append("&gt;");
				break;
			case '&':
				sb.append("&amp;");
				break;
			case '\'':
				sb.append("&apos;");
				break;
			case '"':
				sb.append("&quot;");
				break;
			default:
				if ((c < ' ') || (c > '~')) {
					sb.append("&#x");
					sb.append(Integer.toString(c, 16));
					sb.append(';');
				} else {
					sb.append(c);
				}
				break;
			}
		}
		return sb.toString();
	}
}