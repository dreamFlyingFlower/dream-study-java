package com.wy.enums;

import lombok.Getter;

/**
 * 支付类型
 * 
 * @author 飞花梦影
 * @date 2021-12-29 23:38:17
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Getter
public enum PayType {

	ALI("支付宝", (short) 1),
	WECHAT("微信", (short) 2),
	UNION("银联", (short) 3);

	private Short code;

	private String name;

	private PayType(String name, Short code) {
		this.name = name;
		this.code = code;
	}

	public static String getName(Short code, String name) {
		for (PayType c : PayType.values()) {
			if (c.getCode() == code) {
				return c.name;
			}
		}
		return null;
	}
}