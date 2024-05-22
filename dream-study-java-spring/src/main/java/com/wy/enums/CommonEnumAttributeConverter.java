package com.wy.enums;

import java.util.Arrays;
import java.util.List;

import javax.persistence.AttributeConverter;

/**
 * 整合JPA增删改查枚举
 *
 * @author 飞花梦影
 * @date 2024-05-22 17:42:57
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public abstract class CommonEnumAttributeConverter<E extends Enum<E> & CommonEnum>
		implements AttributeConverter<E, Integer> {

	private final List<E> commonEnums;

	public CommonEnumAttributeConverter(E[] commonEnums) {
		this(Arrays.asList(commonEnums));
	}

	public CommonEnumAttributeConverter(List<E> commonEnums) {
		this.commonEnums = commonEnums;
	}

	@Override
	public Integer convertToDatabaseColumn(E e) {
		return e.getCode();
	}

	@Override
	public E convertToEntityAttribute(Integer code) {
		return (E) commonEnums.stream().filter(commonEnum -> commonEnum.match(String.valueOf(code))).findFirst()
				.orElse(null);
	}
}