package com.wy.enums;

import com.dream.common.CodeMsg;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2024-05-22 17:35:43
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface CommonEnum extends CodeMsg<Integer> {

	String getName();

	default boolean match(String value) {
		if (value == null) {
			return false;
		}
		return value.equals(String.valueOf(getCode())) || value.equals(getName());
	}
}