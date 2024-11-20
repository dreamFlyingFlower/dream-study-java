package com.wy.xss;

import java.io.IOException;

import org.apache.commons.text.StringEscapeUtils;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * 反序列化,处理JSON中的字符串,处理RequestBody方式接收的参数
 *
 * @author 飞花梦影
 * @date 2023-12-07 14:33:06
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class XssJsonDeserializer extends JsonDeserializer<String> {

	@Override
	public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		// 判断值是不是JSON的格式,如果是JSON就不处理
		try {
			JSON.parse(jp.getText());
			return jp.getText();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return StringEscapeUtils.escapeHtml4(jp.getText());
	}
}