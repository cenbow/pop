package com.ai.bdx.pop.util;

import java.text.DecimalFormat;

public class NumberUtil {
	static final String zhnum_0 = "零壹贰叁肆伍陆柒捌玖";

	static final String zhnum = "零一二三四五六七八九";

	static final String[] zhnum1 = { "", "十", "百", "千" };

	static final String[] zhnum1_0 = { "", "拾", "佰", "仟" };

	static final String[] zhnum2 = { "", "万", "亿", "万亿", "亿亿" };

	private static String numberToZH4(String s, boolean fan) {
		StringBuilder sb = new StringBuilder();
		if (s.length() != 4)
			return null;
		for (int i = 0; i < 4; i++) {
			char c1 = s.charAt(i);
			if (c1 == '0' && i > 1 && s.charAt(i - 1) == '0')
				continue;
			if (c1 != '0' && i > 1 && s.charAt(i - 1) == '0')
				sb.append('零');
			if (c1 != '0') {
				if (fan) {
					sb.append(zhnum_0.charAt(c1 - 48));
					sb.append(zhnum1_0[4 - i - 1]);
				} else {
					sb.append(zhnum.charAt(c1 - 48));
					sb.append(zhnum1[4 - i - 1]);
				}
			}
		}
		return new String(sb);
	}

	public static String numberToZH(long n, boolean fan) {
		StringBuilder sb = new StringBuilder();
		String strN = "000" + n;
		int strN_L = strN.length() / 4;
		strN = strN.substring(strN.length() - strN_L * 4);
		for (int i = 0; i < strN_L; i++) {
			String s1 = strN.substring(i * 4, i * 4 + 4);
			String s2 = numberToZH4(s1, fan);
			sb.append(s2);
			if (s2.length() != 0)
				sb.append(zhnum2[strN_L - i - 1]);
		}
		String s = new String(sb);
		if (s.length() != 0 && s.startsWith("零"))
			s = s.substring(1);
		return s;
	}

	public static String numberToZH(double d, boolean fan) {
		return numberToZH("" + d, fan);
	}

	/**
	 * Description: 数字转化成整数
	 * 
	 * @param str
	 * @param fan
	 * @return
	 */
	public static String numberToZH(String str, boolean fan) {
		StringBuilder sb = new StringBuilder();
		int dot = str.indexOf(".");
		if (dot < 0)
			dot = str.length();

		String zhengshu = str.substring(0, dot);
		sb.append(numberToZH(Long.parseLong(zhengshu), fan));
		if (dot != str.length()) {
			sb.append("点");
			String xiaoshu = str.substring(dot + 1);
			for (int i = 0; i < xiaoshu.length(); i++) {
				if (fan) {
					sb.append(zhnum_0.charAt(Integer.parseInt(xiaoshu
							.substring(i, i + 1))));
				} else {
					sb.append(zhnum.charAt(Integer.parseInt(xiaoshu.substring(
							i, i + 1))));
				}
			}
		}
		String s = new String(sb);
		if (s.startsWith("零"))
			s = s.substring(1);
		if (s.startsWith("一十"))
			s = s.substring(1);
		while (s.endsWith("零")) {
			s = s.substring(0, s.length() - 1);
		}
		if (s.endsWith("点"))
			s = s.substring(0, s.length() - 1);
		return s;
	}

	public static String numberToRMB(double rmb) {
		String strRMB = "" + rmb;
		DecimalFormat nf = new DecimalFormat("#.#");
		nf.setMaximumFractionDigits(2);
		strRMB = nf.format(rmb).toString();
		strRMB = numberToZH(strRMB, true);
		if (strRMB.indexOf("点") >= 0) {
			strRMB = strRMB + "零";
			strRMB = strRMB.replaceAll("点", "圆");
			String s1 = strRMB.substring(0, strRMB.indexOf("圆") + 1);
			String s2 = strRMB.substring(strRMB.indexOf("圆") + 1);
			strRMB = s1 + s2.charAt(0) + "角" + s2.charAt(1) + "分整";
		} else {
			strRMB = strRMB + "圆整";
		}
		return "人民币(大写):" + strRMB;
	}

	public static void main(String[] args) {

		System.out.println(numberToZH("1103", false));
		System.out.println(numberToZH("1023", false));
		System.out.println(numberToZH("1003", false));

		System.out.println(numberToZH("103", false));
		System.out.println(numberToZH("31", false));
		System.out.println(numberToZH("30", false));

		System.out.println(numberToZH("9", false));

	}
}
