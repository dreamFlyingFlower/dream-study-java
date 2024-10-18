package com.wy.request;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import dream.flying.flower.io.IOHelper;
import dream.flying.flower.result.ResultException;

/**
 * 解决输入流无法重复使用问题
 * 
 * @author 飞花梦影
 * @date 2022-12-21 10:35:11
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class ReusableHttpServletRequestWrapper extends HttpServletRequestWrapper {

	/** 用于缓存输入流 */
	private final byte[] body;

	public ReusableHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);
		try {
			InputStream is = request.getInputStream();
			body = IOHelper.toByteArray(is);
		} catch (IOException e) {
			e.printStackTrace();
			throw new ResultException();
		}
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		final ByteArrayInputStream bais = new ByteArrayInputStream(body);

		// 从缓存输入流中获取流并返回
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

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(getInputStream()));
	}
}