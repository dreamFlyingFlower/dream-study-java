package com.wy.excel;

import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 性别枚举
 *
 * @author 飞花梦影
 * @date 2024-04-01 13:53:47
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum GenderType {

	/**
	 * 未知
	 */
	UNKNOWN(0, "未知"),

	/**
	 * 男性
	 */
	MALE(1, "男性"),

	/**
	 * 女性
	 */
	FEMALE(2, "女性");

	private final Integer value;

	@JsonFormat
	private final String description;

	public static GenderType convert(Integer value) {
		return Stream.of(values()).filter(bean -> bean.value.equals(value)).findAny().orElse(UNKNOWN);
	}

	public static GenderType convert(String description) {
		return Stream.of(values()).filter(bean -> bean.description.equals(description)).findAny().orElse(UNKNOWN);
	}
}