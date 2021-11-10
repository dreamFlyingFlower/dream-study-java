package com.wy.build;

import com.wy.entity.StrollSkyJiuGe;

/**
 * 建造模式:实际上就是属性设置
 *
 * @author 飞花梦影
 * @date 2021-11-04 13:37:15
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public interface HeavenBuilder {

	void name();

	void author();

	void people();

	StrollSkyJiuGe build();
}