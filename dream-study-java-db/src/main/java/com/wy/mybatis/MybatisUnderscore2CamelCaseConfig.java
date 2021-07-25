package com.wy.mybatis;

import java.util.Map;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.wrapper.MapWrapper;
import org.apache.ibatis.reflection.wrapper.ObjectWrapper;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.springframework.context.annotation.Configuration;

/**
 * Mybatis在多数据源下的下划线转驼峰
 * 
 * @auther 飞花梦影
 * @date 2021-07-24 17:50:14
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
public class MybatisUnderscore2CamelCaseConfig {

	/**
	 * mybatis resultType为map时下划线键值转小写驼峰形式插
	 */
	static class MapWrapperFactory implements ObjectWrapperFactory {

		@Override
		public boolean hasWrapperFor(Object object) {
			return object != null && object instanceof Map;
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		public ObjectWrapper getWrapperFor(MetaObject metaObject, Object object) {
			return new MyMapWrapper(metaObject, (Map) object);
		}
	}

	static class MyMapWrapper extends MapWrapper {

		MyMapWrapper(MetaObject metaObject, Map<String, Object> map) {
			super(metaObject, map);
		}

		@Override
		public String findProperty(String name, boolean useCamelCaseMapping) {
			if (useCamelCaseMapping && ((name.charAt(0) >= 'A' && name.charAt(0) <= 'Z') || name.contains("_"))) {
				return underlineToCamelhump(name);
			}
			return name;
		}

		/**
		 * 将下划线风格替换为驼峰风格
		 *
		 * @param inputString
		 * @return
		 */
		private String underlineToCamelhump(String inputString) {
			StringBuilder sb = new StringBuilder();
			boolean nextUpperCase = false;
			for (int i = 0; i < inputString.length(); i++) {
				char c = inputString.charAt(i);
				if (c == '_') {
					if (sb.length() > 0) {
						nextUpperCase = true;
					}
				} else {
					if (nextUpperCase) {
						sb.append(Character.toUpperCase(c));
						nextUpperCase = false;
					} else {
						sb.append(Character.toLowerCase(c));
					}
				}
			}
			return sb.toString();
		}
	}
}