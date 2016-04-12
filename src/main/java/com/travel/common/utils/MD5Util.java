package com.travel.common.utils;

import java.security.MessageDigest;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @description  
 *
 * @date 2014-11-16 下午04:22:33
 *
 * @author 崔红涛
 *
 */
public class MD5Util {
	
	private final static char[] hexDigits = { '0', '1', '2', '3', '4', '5','6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	
	//hex编码
	private static String bytes2hex(byte[] bytes) {
		StringBuffer sb = new StringBuffer();
		int t;
		for (int i = 0; i < 16; i++) {// 16 == bytes.length;
			t = bytes[i];
			if (t < 0)
				t += 256;
			sb.append(hexDigits[(t >>> 4)]);
			sb.append(hexDigits[(t % 16)]);
		}
		return sb.toString();
	}

	
	/**
	 * @desription:默认加密后为大写
	 * @param strSrc 待加密字符串
	 */
	public static String getMd5String(String strSrc) {
		try {
			//  确定计算方法：md5
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			// 加密后的字符串：先摘要，再hex编码
			return bytes2hex(md5.digest(strSrc.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param strSrc 待加密字符串
	 * @param lower 是否小写
	 */
	public static String getMd5String(String strSrc,boolean lower) {
		try {
			String r=getMd5String(strSrc);
			if(lower)r=r.toLowerCase();
			return r;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

	public static void main(String args[]){
		System.out.println(getMd5String("123456"));
		System.out.println(DigestUtils.md5Hex("123456"));
		System.out.println(EncodeUtil.encodeHex(DigestUtil.md5("123456".getBytes())));
	}

}
