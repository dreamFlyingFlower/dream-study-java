package com.wy.alipay.builder;

import com.alibaba.fastjson.JSON;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2021-12-28 18:04:21
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public abstract class RequestBuilder {

	private String appAuthToken;

	private String notifyUrl;

	public RequestBuilder() {
	}

	public abstract boolean validate();

	public abstract Object getBizContent();

	public String toJsonString() {
		return JSON.toJSONString(this.getBizContent());
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("RequestBuilder{");
		sb.append("appAuthToken='").append(this.appAuthToken).append('\'');
		sb.append(", notifyUrl='").append(this.notifyUrl).append('\'');
		sb.append('}');
		return sb.toString();
	}

	public String getAppAuthToken() {
		return this.appAuthToken;
	}

	public RequestBuilder setAppAuthToken(String appAuthToken) {
		this.appAuthToken = appAuthToken;
		return this;
	}

	public String getNotifyUrl() {
		return this.notifyUrl;
	}

	public RequestBuilder setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
		return this;
	}
}