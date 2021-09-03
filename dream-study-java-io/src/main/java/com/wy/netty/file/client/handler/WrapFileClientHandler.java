package com.wy.netty.file.client.handler;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestEncoder;

public abstract class WrapFileClientHandler {

	protected static final Logger LOGGER = LoggerFactory.getLogger(WrapFileClientHandler.class);

	private String host;

	private URI uri;

	private String userName;

	private String pwd;

	private HttpRequest request;

	public WrapFileClientHandler(String host, URI uri, String userName, String pwd) {
		this.host = host;
		this.uri = uri;
		this.userName = userName;
		this.pwd = pwd;
		this.request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, uri.toASCIIString());
		setHeaderDatas();
	}

	private void setHeaderDatas() {
		this.request.headers().add("Host", this.host);
		this.request.headers().add("Connection", "close");
		this.request.headers().add("Accept-Encoding", "gzip,deflate");
		this.request.headers().add("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
		this.request.headers().add("Accept-Language", "fr");
		this.request.headers().add("Referer", this.uri.toString());
		this.request.headers().add("User-Agent", "Netty Simple Http Client side");
		this.request.headers().add("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
	}

	public abstract HttpPostRequestEncoder wrapRequestData(HttpDataFactory paramHttpDataFactory);

	public HttpRequest getRequest() {
		return this.request;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPwd() {
		return this.pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
}