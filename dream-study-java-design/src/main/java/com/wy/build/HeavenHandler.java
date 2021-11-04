package com.wy.build;

import com.wy.entity.HeavenNineSong;

/**
 * 建造的使用
 *
 * @author 飞花梦影
 * @date 2021-11-04 13:49:33
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class HeavenHandler {

	public HeavenNineSong build(HeavenBuilder builder) {
		builder.name();
		builder.author();
		builder.people();
		return builder.build();
	}
}