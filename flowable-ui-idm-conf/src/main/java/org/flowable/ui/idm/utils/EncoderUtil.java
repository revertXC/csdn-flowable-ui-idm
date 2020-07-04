package org.flowable.ui.idm.utils;


import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Map;


/**
 * 加密/转码 工具类
 * Copyright (C) TECPIE, All rights reserved.
 *
 * @author Maxl
 * @date 2020-03-03 17:53
 */
public class EncoderUtil {

	public final static String ENCODE_MD5_32 = "MD5";
	public final static String ENCODE_MD5_16 = "md5";
	public final static String ENCODE_SHA1 = "SHA1";

	/**
	 * 进行多种类型的加密
	 * @param content 需要加密字符串
	 * @param type 加密类型
	 * @return 加密结果字符串
	 */
	public static String encode(String content, String type){
		if(type==null || "".equals(type)){
			return type;
		}
		switch (type) {
			case ENCODE_MD5_32 :
				return encodeByMd5(content, "UTF-8", 32);
			case ENCODE_MD5_16 :
				return encodeByMd5(content, "UTF-8", 16);
			case ENCODE_SHA1 :
				return encodeBySHA1(content);
			default:
				return "";
		}
	}

	/**
	 * 返回md5加密字段
	 * @param plainText  加密字段
	 * @param code  加密编码, 默认UTF-8
	 * @param length 返回的长度  16/32
	 * @return md5加密字段
	 */
	public static String encodeByMd5(String plainText, String code, int length) {
		if(length == StringUtil.ENCODE_MD5_RETURN_LENGTH16) {
			return encodeByMd5(plainText, code).substring(8, 24);
		} else if(length == StringUtil.ENCODE_MD5_RETURN_LENGTH32) {
			return encodeByMd5(plainText, code);
		} else {
			return "";
		}
	}

	/**
	 * MD5加密   32位
	 * @param str 需要加密字符串
	 * @param code 编码格式 默认UTF-8
	 * @return 加密结果
	 */
	public static String encodeByMd5(String str, String code) {
		if(code==null || "".equals(code)){
			code = "UTF-8";
		}
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes(code));
		} catch (Exception e) {
			e.printStackTrace();
		}

		assert messageDigest != null;
		byte[] byteArray = messageDigest.digest();
		StringBuilder md5StrBuff = new StringBuilder();
		for (byte b : byteArray) {
			if (Integer.toHexString(0xFF & b).length() == 1) {
				md5StrBuff.append("0").append(Integer.toHexString(0xFF & b));
			} else {
				md5StrBuff.append(Integer.toHexString(0xFF & b));
			}
		}
		return md5StrBuff.toString().toUpperCase();
	}
	
	// 先md5加密，再转为base64
//	public static String encodeByBase64Md5(String plainText){
//		MessageDigest md;
//		try {
//			md = MessageDigest.getInstance("MD5");
//			byte[] tmp = md.digest(plainText.getBytes());
//			return new BASE64Encoder().encode(tmp);
//		} catch (NoSuchAlgorithmException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return "";
//	}

	/**
	 * SHA1加密
	 * @param decript 需要加密字符串
	 * @return 加密结果字符串
	 */
	public static String encodeBySHA1(String decript) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			digest.update(decript.getBytes());
			byte[] messageDigest = digest.digest();
			// Create Hex String
			StringBuilder hexString = new StringBuilder();
			// 字节数组转换为 十六进制 数
			for (byte b : messageDigest) {
				String shaHex = Integer.toHexString(b & 0xFF);
				if (shaHex.length() < 2) {
					hexString.append(0);
				}
				hexString.append(shaHex);
			}
			return hexString.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 对参数升序排列,生成 ke1=value1&key2=value2 形式的字符串
	 * @param <T> 模板类
	 * @param serviceParams 待排序数据
	 * @return ke1=value1&key2=value2 形式的字符串排序结果
	 */
	public static <T> String dataDecrypt(Map<String, T> serviceParams) {
		StringBuilder sb = new StringBuilder();
		Object[] keys = serviceParams.keySet().toArray();
		Arrays.sort(keys);
		for (Object key : keys) {
			sb.append(key).append("=").append(serviceParams.get(key)).append("&");
		}
		sb.delete(sb.length()-1, sb.length());
		return sb.toString();
	}

	/**
	 * 对参数升序排列,生成 ke1value1key2value2 形式的字符串
	 * @param <T> 模板类
	 * @param serviceParams 待排序数据
	 * @return ke1value1key2value2 形式的字符串排序结果
	 */
	public static <T> String dataDecrypt1(Map<String, T> serviceParams) {
		StringBuilder sb = new StringBuilder();
		Object[] keys = serviceParams.keySet().toArray();
		Arrays.sort(keys);
		for (Object key : keys) {
			sb.append(key).append(serviceParams.get(key));
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		System.out.println(EncoderUtil.encodeBySHA1("123456"));
	}
}
