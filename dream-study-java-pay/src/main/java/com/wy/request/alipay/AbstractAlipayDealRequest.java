package com.wy.request.alipay;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2021-12-28 18:04:21
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Getter
@Setter
@ToString
@SuperBuilder
public abstract class AbstractAlipayDealRequest {

	private String appAuthToken;

	private String notifyUrl;

	public AbstractAlipayDealRequest() {
	}

	public abstract boolean validate();

	public abstract Object getBizContent();
}