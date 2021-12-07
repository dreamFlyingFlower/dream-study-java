package com.wy.pay.factory;

import lombok.Getter;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2021-12-07 17:28:44
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Getter
public enum Payments {

	ALI_PAY("ALI", AlipayFactory.class),
	WEIXIN_PAY("WEIXIN", WeixinFactory.class);

	private String code;

	private Class<? extends PaymentFactory> clazz;

	private Payments(String code, Class<? extends PaymentFactory> clazz) {
		this.code = code;
		this.clazz = clazz;
	}

	public static Class<? extends PaymentFactory> getInstance(String code) {
		Payments[] values = Payments.values();
		for (Payments payments : values) {
			if (payments.code.equalsIgnoreCase(code)) {
				return payments.clazz;
			}
		}
		return null;
	}
}