package com.wy.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * jackson工具类
 * 
 * @author 飞花梦影
 * @date 2021-01-15 08:50:16
 * @git {@link https://github.com/mygodness100}
 */
@Slf4j
public class JacksonUtils {

	private static ObjectMapper objectMapper = new ObjectMapper();

	public static String bean2Json(Object bean) {
		try {
			return objectMapper.writeValueAsString(bean);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			log.error("jackson serializer bean to string failed");
		}
		return null;
	}

	public static <T> T json2Bean(String json, Class<T> clazz) {
		try {
			return objectMapper.readValue(json, clazz);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			log.error("jackson serializer string to bean failed");
		}
		return null;
	}
}