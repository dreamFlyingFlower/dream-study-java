package com.wy.factory.abs;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2021-11-03 16:12:36
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class AbstractChinaAir extends AbstractAir {

	@Override
	public String country() {
		return "中国";
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