package com.wy.xss;

import java.util.List;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * 将XSS的JSON处理加入到JSON处理器中
 *
 * @author 飞花梦影
 * @date 2023-12-07 14:36:27
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class XssWebMvcConfigurer implements WebMvcConfigurer {

	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
		ObjectMapper mapper = builder.build();
		/* 注入自定义的序列化工具,将RequestBody的参数进行转译后传输 */
		SimpleModule simpleModule = new SimpleModule();
		// XSS序列化
		simpleModule.addSerializer(String.class, new XssJsonSerializer());
		simpleModule.addDeserializer(String.class, new XssJsonDeserializer());
		mapper.registerModule(simpleModule);
		converters.add(new MappingJackson2HttpMessageConverter(mapper));
	}
}