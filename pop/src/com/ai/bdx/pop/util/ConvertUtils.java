package com.ai.bdx.pop.util;

import java.math.BigInteger;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.asiainfo.biframe.utils.string.StringUtil;

/**
 * Title: ConvertUtils：高效转化方法提供类<br>
 * Description: 针对行业的具体应用设计的一些高效的转化方法。<br>
 * 基准测试结果（Dell E6400环境下1000万条可转化为8位数整数的随机字符串）：<br> {@link ConvertUtils#parsePhoneId8(String)
 * ConvertUtils.parsePhoneId8(str)}：169.28毫秒<br> {@link ConvertUtils#parseInt(String)
 * ConvertUtils.parseInt(str)}：340.50毫秒<br> {@link Integer#parseInt(String) Integer.parseInt(str)}
 * ：1343.38毫秒<br>
 * Copyright: (C) Copyright 1993-2010 AsiaInfo Holdings, Inc<br>
 * Company: 亚信科技（中国）有限公司<br>
 *
 * @author <a href="mailto:wuqm@asiainfo-linkage.com">wuqm@asiainfo-linkage.com</a> 2010-06-07 15:17:46
 * @version 3.0
 */
public class ConvertUtils {
	private final static Logger log = LogManager.getLogger();
	/** 防止实例化本工具类 */
	private ConvertUtils() {
	}

	/** 把8位字符串转化成整数，不进行检查，转换错误不报错（空指针除外） */
	public static int parsePhoneId8(String id) {
		assert (id != null && id.length() == 8);
		return (id.charAt(0) * 0x989680 + id.charAt(1) * 0xf4240 + id.charAt(2) * 0x186a0 + id.charAt(3) * 10000
				+ id.charAt(4) * 1000 + id.charAt(5) * 100 + id.charAt(6) * 10 + id.charAt(7)) - 0x1FCA0550;
	}

	/** 把11位字符串转化成整数，不进行检查，转换错误不报错（空指针除外） */
	public static long parsePhoneId11(String id) {
		assert (id != null && id.length() == 11);
		return (id.charAt(0) * 0x2540be400L + id.charAt(1) * 0x3b9aca00L + id.charAt(2) * 0x5f5e100L + id.charAt(3)
				* 0x989680L + id.charAt(4) * 0xf4240L + id.charAt(5) * 0x186a0L + (id.charAt(6) * 10000)
				+ (id.charAt(7) * 1000) + (id.charAt(8) * 100) + (id.charAt(9) * 10) + id.charAt(10)) - 0x7C2D24D550L;
	}

	/** 把11位字符串的后8位转化成整数，不进行检查，转换错误不报错（空指针除外） */
	public static int parsePhoneId11To8(String id) {
		assert (id != null && id.length() == 11);
		return (id.charAt(3) * 0x989680 + id.charAt(4) * 0xf4240 + id.charAt(5) * 0x186a0 + id.charAt(6) * 10000
				+ id.charAt(7) * 1000 + id.charAt(8) * 100 + id.charAt(9) * 10 + id.charAt(10)) - 0x1FCA0550;
	}

	/** 把字符串转化成长整数，不进行检查，转换错误不报错（空指针除外） */
	public static long parseLong(String s) throws NumberFormatException {
		assert (s.length() > 0);
		long result = 0;
		boolean negative = false;
		int i = 0, max = s.length();
		char cc;
		if ((cc = s.charAt(i++)) == '-') {
			negative = true;
		} else {
			if (cc >= '0' && cc <= '9') {
				result += cc - '0';
			} else {
				throw new NumberFormatException("For input string: \"" + s + "\"");
			}
		}
		while (i < max) {
			cc = s.charAt(i++);
			if (cc >= '0' && cc <= '9') {// 如果要更高的效率，可以把这里的判断取消。
				result = result * 10 + (cc - '0');
			} else {
				throw new NumberFormatException("For input string: \"" + s + "\"");
			}
		}
		if (negative) {
			if (i > 1) {
				return -result;
			} else { /* Only got "-" */
				throw new NumberFormatException("For input string: \"" + s + "\"");
			}
		} else {
			return result;
		}
	}

	/** 把字符串转化成整数，不进行检查，转换错误不报错（空指针除外） */
	public static int parseInt(String s) throws NumberFormatException {
		assert (s.length() > 0);
		int result = 0;
		boolean negative = false;
		int i = 0, max = s.length();
		char cc;
		if ((cc = s.charAt(i++)) == '-') {
			negative = true;
		} else {
			if (cc >= '0' && cc <= '9') {
				result += cc - '0';
			} else {
				throw new NumberFormatException("For input string: \"" + s + "\"");
			}
		}
		while (i < max) {
			cc = s.charAt(i++);
			if (cc >= '0' && cc <= '9') {// 如果要更高的效率，可以把这里的判断取消。
				result = result * 10 + (cc - '0');
			} else {
				throw new NumberFormatException("For input string: \"" + s + "\"");
			}
		}
		if (negative) {
			if (i > 1) {
				return -result;
			} else { /* Only got "-" */
				throw new NumberFormatException("For input string: \"" + s + "\"");
			}
		} else {
			return result;
		}
	}

	/** 把字符串转化成短整数，不进行检查，转换错误不报错（空指针除外）。如果能够采用JNI，效率能得到明显提高。 */
	public static short parseShort(String s) throws NumberFormatException {
		return (short) parseInt(s);
	}

	/** 把字符串转化成字节整数，不进行检查，转换错误不报错（空指针除外）。如果能够采用JNI，效率能得到明显提高。 */
	public static byte parseByte(String s) throws NumberFormatException {
		return (byte) parseInt(s);
	}

	//将二进制字符串转换为十进制
	public static long transToDecimal(String binary) {
		if (StringUtil.isNotEmpty(binary)) {
			BigInteger bi = new BigInteger(binary, 2);
			return Long.valueOf(bi.toString(10));
		}
		return 0;
	}

	public static void main(String[] args) {
		int size = 10000 * 100;
		String[] strs = new String[size];
		for (int i = 0; i < size; i++) {
			strs[i] = RandomStringUtils.random(8, "0123456789");
		}
		StopWatch watch = new StopWatch();
		watch.start();
		for (String str : strs) {
			parseInt(str);
		}
		watch.stop();
		log.debug(watch.getTime() + "\tparseInt(str)");
		watch.reset();
		watch.start();
		for (String str : strs) {
			Integer.parseInt(str);
		}
		watch.stop();
		log.debug(watch.getTime() + "\tInteger.parseInt(str)");
		watch.reset();
		watch.start();
		for (String str : strs) {
			parsePhoneId8(str);
		}
		watch.stop();
		log.debug(watch.getTime() + "\tparsePhoneId8(str)");
	}
}