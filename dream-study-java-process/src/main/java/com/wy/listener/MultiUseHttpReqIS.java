package com.wy.listener;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.wy.io.IOUtils;

/**
 * 当需要重复使用request请求的inputstream时可使用此类
 * 拦截器中必须对请求方式校验为post,且contenttype为application/json;charset=utf-8
 *
 * @author ParadiseWY
 * @date 2020-12-03 23:11:37
 * @git {@link https://github.com/mygodness100}
 */
public class MultiUseHttpReqIS extends HttpServletRequestWrapper {

	private final byte[] body;

	public MultiUseHttpReqIS(HttpServletRequest request) {
		super(request);
		InputStream is = null;
		try {
			is = request.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		body = IOUtils.readIO(is).getBytes(StandardCharsets.UTF_8);
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
			public void setReadListener(ReadListener arg0) {}
		};
	}
}