package com.wy.config;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import dream.study.common.model.User;

/**
 * 自定义User类序列化器,在User上添加jackson的{@link JsonSerialize}时定义
 * 
 * @author 飞花梦影
 * @date 2021-01-08 20:49:04
 * @git {@link https://github.com/mygodness100}
 */
public class UserSerializer extends JsonSerializer<User> {

	/**
	 * @param value 需要实现序列化的类
	 * @param gen json序列化生成器
	 * @param serializers 生成序列化的提供类,通常不需要关心
	 */
	@Override
	public void serialize(User value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		// 开始序列化对象
		gen.writeStartObject();
		// 将类中的字段进行何种方式的序列化,若不进行自定义,则使用默认的方式
		gen.writeStringField("user_id", value.getUserId().toString());
		// 结束序列化
		gen.writeEndObject();
	}
}