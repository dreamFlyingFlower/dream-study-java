package com.wy.entity;

/**
 * 动漫-Clannad实现类
 * 
 * @author 飞花梦影
 * @date 2021-11-03 09:59:53
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class Clannad implements Cartoon {

	@Override
	public String country() {
		return "日本";
	}

	@Override
	public String name() {
		return "Clannad";
	}

	@Override
	public String chineseName() {
		return "Clannad";
	}

	@Override
	public String type() {
		return "恋爱,治愈";
	}
}