package com.wy.alipay.builder;

import com.fasterxml.jackson.annotation.JsonAlias;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2021-12-28 18:06:38
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class ExtendParams {

	@JsonAlias("sys_service_provider_id")
	private String sysServiceProviderId;

	public ExtendParams() {
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("ExtendParams{");
		sb.append("sysServiceProviderId='").append(this.sysServiceProviderId).append('\'');
		sb.append('}');
		return sb.toString();
	}

	public String getSysServiceProviderId() {
		return this.sysServiceProviderId;
	}

	public ExtendParams setSysServiceProviderId(String sysServiceProviderId) {
		this.sysServiceProviderId = sysServiceProviderId;
		return this;
	}
}