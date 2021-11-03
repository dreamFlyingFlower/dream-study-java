package com.wy.abstracfactory;

/**
 * 日语青空
 *
 * @author 飞花梦影
 * @date 2021-11-03 15:39:09
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class AbstractJapanAir extends AbstractAir {

	@Override
	public String country() {
		return "中文";
	}

	@Override
	public String name() {
		return "Air";
	}

	@Override
	public String chineseName() {
		return "青空";
	}

	@Override
	public String type() {
		return "恋爱,悲剧";
	}
}