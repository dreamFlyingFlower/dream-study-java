package com.wy.shiro.utils;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.Hex;

/**
 * 封装base64和16进制编码解码工具类
 */
public class EncodesUtil {

	/**
	 * HEX-byte[]--String转换
	 * 
	 * @param input 输入数组
	 * @return String
	 */
	public static String encodeHex(byte[] input) {
		return Hex.encodeToString(input);
	}

	/**
	 * HEX-String--byte[]转换
	 * 
	 * @param input 输入字符串
	 * @return byte数组
	 */
	public static byte[] decodeHex(String input) {
		return Hex.decode(input);
	}

	/**
	 * Base64-byte[]--String转换
	 * 
	 * @param input 输入数组
	 * @return String
	 */
	public static String encodeBase64(byte[] input) {
		return Base64.encodeToString(input);
	}

	/**
	 * Base64-String--byte[]转换
	 * 
	 * @param input 输入字符串
	 * @return byte数组
	 */
	public static byte[] decodeBase64(String input) {
		return Base64.decode(input);
	}
}