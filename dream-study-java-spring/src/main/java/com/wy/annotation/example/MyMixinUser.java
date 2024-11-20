package com.wy.annotation.example;

import org.springframework.boot.jackson.JsonMixin;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.core.userdetails.User;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 混合注解使用
 * 
 * 第一种:直接使用{@link JsonMixin},该方式必须要在Spring环境中,且环境中注入了{@link Jackson2ObjectMapperBuilder}
 *
 * @author 飞花梦影
 * @date 2024-11-13 16:39:45
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@JsonMixin(User.class)
public class MyMixinUser {

	public static void main(String[] args) {
		// 自己新建或从Spring中获取Jackson2ObjectMapperBuilder
		// 如果是从Spring环境中获取,已经注入了@JsonMixin标注的类
		Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder = new Jackson2ObjectMapperBuilder();
		// 创建ObjectMapper
		ObjectMapper objectMapper = jackson2ObjectMapperBuilder.createXmlMapper(false).build();
		objectMapper = new ObjectMapper();
		// 第二种:自定义混合类;需要修改的类
		objectMapper.addMixIn(MyMixinUser.class, User.class);
	}
}