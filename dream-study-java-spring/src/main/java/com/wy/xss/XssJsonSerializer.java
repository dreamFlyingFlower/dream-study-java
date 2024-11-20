package com.wy.xss;

import java.io.IOException;

import org.apache.commons.text.StringEscapeUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * 序列化,处理返回给前端的数据
 *
 * @author 飞花梦影
 * @date 2023-12-07 14:34:59
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class XssJsonSerializer extends JsonSerializer<String> {

	@Override
	public void serialize(String value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
		jgen.writeString(StringEscapeUtils.escapeHtml4(value));
	}

}