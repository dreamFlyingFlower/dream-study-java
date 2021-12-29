package com.wy.enums;

import lombok.Getter;

/**
 * 支付途径
 * 
 * @author 飞花梦影
 * @date 2021-12-29 23:39:00
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Getter
public enum PayWay {

	PC("PC,平板", (short) 1),
	MOBILE("手机", (short) 2);

	private Short code;

	private String name;

	private PayWay(String name, Short code) {
		this.name = name;
		this.code = code;
	}

	public static String getName(Short code, String name) {
		for (PayWay c : PayWay.values()) {
			if (c.getCode() == code) {
				return c.name;
			}
		}
		return null;
	}
}