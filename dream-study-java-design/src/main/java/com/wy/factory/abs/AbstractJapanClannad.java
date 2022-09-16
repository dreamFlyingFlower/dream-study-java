package com.wy.factory.abs;

/**
 * 中文青空
 *
 * @author 飞花梦影
 * @date 2021-11-03 15:40:46
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class AbstractJapanClannad extends AbstractClannad {

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