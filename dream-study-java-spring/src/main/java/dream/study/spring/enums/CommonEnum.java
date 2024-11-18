package dream.study.spring.enums;

import dream.flying.flower.common.CodeMsg;

/**
 * 通用枚举
 *
 * @author 飞花梦影
 * @date 2024-05-22 17:35:43
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface CommonEnum extends CodeMsg {

	default boolean match(String value) {
		if (value == null) {
			return false;
		}
		return value.equals(String.valueOf(getValue())) || value.equals(getName());
	}
}