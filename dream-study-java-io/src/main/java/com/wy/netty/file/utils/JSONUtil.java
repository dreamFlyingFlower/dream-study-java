package com.wy.netty.file.utils;

import com.alibaba.fastjson.JSON;

/**
 * JSON工具类
 * 
 * @author 飞花梦影
 * @date 2021-09-03 11:22:57
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class JSONUtil {

	/**
	 * 把对象转化为json字符串
	 * 
	 * @param obj
	 * @return
	 */
	public static String toJSONString(Object obj) {
		return JSON.toJSONString(obj);
	}

	/**
	 * 把json字符串转化为相应的实体对象
	 * 
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static <T> T parseObject(String json, Class<T> clazz) {
		return JSON.parseObject(json, clazz);
	}
}