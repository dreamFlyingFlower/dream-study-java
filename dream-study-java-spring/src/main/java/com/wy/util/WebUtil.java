package com.wy.util;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.wy.result.Result;

/**
 * Web 工具类
 *
 * @author 飞花梦影
 * @date 2022-09-06 17:32:24
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class WebUtil {

	private static ObjectMapper objectMapper = new ObjectMapper();

	static {
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		objectMapper.setSerializationInclusion(Include.NON_EMPTY);
	}

	public static void writeOk(HttpServletResponse response) throws IOException {
		write(Result.ok(), response);
	}

	public static void writeError(HttpServletResponse response) throws IOException {
		write(Result.error(), response);
	}

	public static void write(Result<?> result, HttpServletResponse response) throws IOException {
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		objectMapper.writeValue(response.getOutputStream(), result);
	}
}