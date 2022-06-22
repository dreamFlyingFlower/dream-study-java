package com.wy.shiro.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;

import com.wy.shiro.constant.SuperConstant;

/**
 * Shiro散列算法工具类.Shiro支持的散列算法:Md2Hash、Md5Hash、Sha1Hash、Sha256Hash、Sha384Hash、Sha512Hash
 * 
 * @author 飞花梦影
 * @date 2022-06-22 09:40:42
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class DigestsUtil {

	/**
	 * sha1方法
	 * 
	 * @param input 需要散列字符串
	 * @param salt 盐字符串
	 * @return
	 */
	public static String sha1(String input, String salt) {
		return new SimpleHash(SuperConstant.HASH_ALGORITHM, input, salt, SuperConstant.HASH_INTERATIONS).toString();
	}

	/**
	 * 随机获得salt字符串
	 * 
	 * @return
	 */
	public static String generateSalt() {
		SecureRandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
		return randomNumberGenerator.nextBytes().toHex();
	}

	/**
	 * 生成密码字符密文和salt密文
	 * 
	 * @param
	 * @return
	 */
	public static Map<String, String> entryptPassword(String passwordPlain) {
		Map<String, String> map = new HashMap<>();
		String salt = generateSalt();
		String password = sha1(passwordPlain, salt);
		map.put("salt", salt);
		map.put("password", password);
		return map;
	}
}