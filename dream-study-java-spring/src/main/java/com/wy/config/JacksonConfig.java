package com.wy.config;

import java.text.SimpleDateFormat;

import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wy.enums.DateEnum;

/**
 * 自定义Jackson序列化机制,在配置文件中定义更方便
 * 
 * @author 飞花梦影
 * @date 2021-01-08 10:44:19
 * @git {@link https://github.com/mygodness100}
 */
// @Configuration
public class JacksonConfig {

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		// 设置null参数将不再返回
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		// 设置时间返回的格式
		objectMapper.setDateFormat(new SimpleDateFormat(DateEnum.DATETIME.getPattern()));
		return objectMapper;
	}
}