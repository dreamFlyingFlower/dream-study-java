package com.wy.util;

import java.lang.reflect.Field;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.wy.reflect.ReflectTool;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2022-01-06 13:57:10
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class RestTemplateUtil {

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
			ReflectTool.fixAccessible(field);
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