package com.wy.config;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.wy.io.IOTool;
import com.wy.result.ResultException;

/**
 * 当需要重复使用request请求的inputstream时可使用此类
 * 拦截器中必须对请求方式校验为post,且contenttype为application/json;charset=utf-8
 * 
 * @author 飞花梦影
 * @date 2021-08-15 11:09:05
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class RequestWrapperConfig extends HttpServletRequestWrapper {

	private final byte[] body;

	public RequestWrapperConfig(HttpServletRequest request) {
		super(request);
		try {
			InputStream is = request.getInputStream();
			body = IOTool.toByteArray(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		throw new ResultException();
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(getInputStream()));
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {

		final ByteArrayInputStream bais = new ByteArrayInputStream(body);

		return new ServletInputStream() {

			@Override
			public int read() throws IOException {
				return bais.read();
			}

			@Override
			public boolean isFinished() {
				return false;
			}

			@Override
			public boolean isReady() {
				return false;
			}

			@Override
			public void setReadListener(ReadListener readListener) {
			}
		};
	}
}