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

	ALIPAY("支付宝", 1),
	WECHAT("微信", 2),
	UNION("银联", 3);

	private String name;

	private Integer code;

	private PayType(String name, Integer code) {
		this.name = name;
		this.code = code;
	}

	public static String getName(Integer code, String name) {
		for (PayType c : PayType.values()) {
			if (c.getCode().intValue() == code.intValue()) {
				return c.name;
			}
		}
		return null;
	}
}