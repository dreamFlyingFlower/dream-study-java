package com.wy.enums;

import dream.flying.flower.common.StatusMsg;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2024-05-22 17:35:43
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface CommonEnum extends StatusMsg<Integer> {

	String getName();

	default boolean match(String value) {
		if (value == null) {
			return false;
		}
		return value.equals(String.valueOf(getCode())) || value.equals(getName());
	}
}