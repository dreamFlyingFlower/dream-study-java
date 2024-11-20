package com.wy.minio.upload;

import java.util.List;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2024-01-03 16:48:38
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class WebMvcConfig implements WebMvcConfigurer {

	/**
	 * 长整型数字处理:JavaScript的整数类型范围有限,精度为17位 ,当接口返回的Long类型过长时,javaScript会进行截断造成精度丢失
	 */
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

		MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
		ObjectMapper objectMapper = new ObjectMapper();
		SimpleModule simpleModule = new SimpleModule();
		simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
		simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
		objectMapper.registerModule(simpleModule);
		jackson2HttpMessageConverter.setObjectMapper(objectMapper);
		// add(0, converter) 提高优先级
		converters.add(0, jackson2HttpMessageConverter);
	}
}