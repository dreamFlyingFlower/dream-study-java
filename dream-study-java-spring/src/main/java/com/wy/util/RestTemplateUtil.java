package com.wy.util;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import dream.flying.flower.collection.MapHelper;
import dream.flying.flower.lang.StrHelper;
import dream.flying.flower.reflect.ReflectHelper;

/**
 * RestTemplate工具类
 *
 * @author 飞花梦影
 * @date 2022-01-06 13:57:10
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class RestTemplateUtil {

	/** Get请求URL */
	private static final String FORMAT_URL_GET = "{0}?{1}";

	/**
	 * Get请求将参数构建到URL中
	 * 
	 * @param params 参数
	 * @return 构建后的参数字符串
	 */
	public static String generateGetUrl(Map<String, Object> params) {
		return generateGetUrl("", params);
	}

	/**
	 * Get请求将参数构建到URL中
	 * 
	 * @param url URL
	 * @param params 参数
	 * @return 构建后的URL字符串
	 */
	public static String generateGetUrl(String url, Map<String, Object> params) {
		if (MapHelper.isNotEmpty(params)) {
			StringJoiner stringJoiner = new StringJoiner("&");
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				if (StrHelper.isBlank(entry.getKey()) || Objects.isNull(entry.getValue())) {
					continue;
				}
				stringJoiner.add(entry.getKey() + "={" + entry.getKey() + "}");
			}
			url = MessageFormat.format(FORMAT_URL_GET, Optional.ofNullable(url).orElse(""), stringJoiner.toString());
		}
		return url;
	}

	/**
	 * 将对象转为表单可接收的 LinkedMultiValueMap
	 * 
	 * @param <T> 类
	 * @param t 对象
	 * @return LinkedMultiValueMap
	 */
	public static <T> MultiValueMap<String, Object> toLinkedMultiValueMap(T t) {
		MultiValueMap<String, Object> valueMap = new LinkedMultiValueMap<>();
		Class<?> clazz = t.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			ReflectHelper.fixAccessible(field);
			try {
				valueMap.add(field.getName(), field.get(t));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return valueMap;
	}

	/**
	 * 构建 LinkedMultiValueMap 参数
	 * 
	 * @return LinkedMultiValueMap
	 */
	public static MultiValueMapBuilder builder() {
		return new MultiValueMapBuilder();
	}

	public static class MultiValueMapBuilder {

		private MultiValueMap<String, Object> ret = new LinkedMultiValueMap<>();

		public MultiValueMap<String, Object> build() {
			return ret;
		}

		public MultiValueMapBuilder add(String key, Object value) {
			ret.add(key, value);
			return this;
		}
	}
}