/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.travel.common.utils;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Date;
import java.util.UUID;

/**
 * 封装各种生成唯一性ID算法的工具类.
 */
public class IdUtil {

	private static SecureRandom random = new SecureRandom();
	
	/**
	 * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
	 */
	public static String uuid() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 * 使用SecureRandom随机生成Long. 
	 */
	public static long randomLong() {
		return Math.abs(random.nextLong());
	}

	/**
	 * 使用SecureRandom随机生成固定宽度字符串.
	 */
	public static String randomLenNmberStr(int len){

		int intMaxLen=String.valueOf(Integer.MAX_VALUE).length();
		int longMaxLen=String.valueOf(Long.MAX_VALUE).length();
		if(len<1||len>longMaxLen)len=longMaxLen;

		long r=0l;
		if(len>intMaxLen){
			r=Math.abs(random.nextLong());
		}else{
			r=Math.abs(random.nextInt());
		}

		String  seria = String.valueOf(r);
		if(seria.length()<len){
			seria = String.format("%0"+len+"d", r);
		}else {
			seria = seria.substring(0,len);
		}
		return seria;
	}

	/**
	 * 基于Base62编码的SecureRandom随机生成bytes.
	 */
	public static String randomBase62(int length) {
		byte[] randomBytes = new byte[length];
		random.nextBytes(randomBytes);
		return EncodeUtil.encodeBase62(randomBytes);
	}
	
	/**
	 * 基于时间生成随机数.
	 */
	public static Long randomTime(){
		StringBuffer sb=new StringBuffer();
		sb.append(DateUtil.date_string(new Date(), "yyMMddHHmmssSSS"));
		//生成1000~9999之间的随机数
		sb.append(Math.round(Math.random()*(9999-1000)+1000));
		return Long.parseLong(sb.toString());
	}

	
	public static void main(String[] args) {
		//System.out.println(IdUtil.uuid());
		System.out.println(IdUtil.uuid().length());
		for (int i=0; i<10; i++){
			System.out.println(IdUtil.randomLong() + "  " +randomTime()+" "+ IdUtil.randomBase62(14)+" " +randomLenNmberStr(19));
		}

		System.out.println(String.valueOf(Long.MAX_VALUE).length());
		//String s="2015061314410176054";
		
		//System.out.println(randomTime());
		
		//System.out.println(getCode());
	}

}
