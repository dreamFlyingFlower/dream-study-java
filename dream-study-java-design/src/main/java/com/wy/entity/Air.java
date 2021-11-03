package com.wy.entity;

/**
 * 动漫-青空实现类
 * 
 * @author 飞花梦影
 * @date 2021-11-03 09:59:21
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class Air implements Cartoon {

	@Override
	public String country() {
		return "日本";
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