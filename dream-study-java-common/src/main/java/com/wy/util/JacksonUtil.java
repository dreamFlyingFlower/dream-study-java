package com.wy.util;

import java.util.Map;

import org.springframework.data.redis.hash.Jackson2HashMapper;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.extern.slf4j.Slf4j;

/**
 * jackson工具类
 * 
 * @author 飞花梦影
 * @date 2021-01-15 08:50:16
 * @git {@link https://github.com/mygodness100}
 */
@Slf4j
public class JacksonUtil {

	private static ObjectMapper objectMapper = new ObjectMapper();

	static {
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		objectMapper.setSerializationInclusion(Include.NON_EMPTY);
	}

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

	/**
	 * 将对象转换为Map
	 * 
	 * @param source 对象
	 * @return Map<String, Object>
	 */
	public static Map<String, Object> bean2Map(Object source) {
		Jackson2HashMapper jackson2HashMapper = new Jackson2HashMapper(objectMapper, false);
		return jackson2HashMapper.toHash(source);
	}

	/**
	 * 将Map转换为bean
	 * 
	 * @param <T> 泛型类
	 * @param source 源数据
	 * @param clazz 目标类
	 * @return 目标对象
	 */
	public static <T> T map2Bean(Map<String, Object> source, Class<T> clazz) {
		return objectMapper.convertValue(source, clazz);
	}

	/**
	 * 将任意对象转换为bean
	 * 
	 * @param <T> 泛型类
	 * @param source 源数据
	 * @param clazz 目标类
	 * @return 目标对象
	 */
	public static <T> T object2Bean(Object source, Class<T> clazz) {
		return objectMapper.convertValue(source, clazz);
	}
}