package com.wy.build.exp;

/**
 * 具体的构建者,用来构建摩拜单车对象
 * 
 * @author 飞花梦影
 * @date 2022-09-16 11:25:30
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class MobileBuilder extends Builder {

	@Override
	public void buildFrame() {
		bike.setFrame("碳纤维车架");
	}

	@Override
	public void buildSeat() {
		bike.setSeat("真皮车座");
	}

	@Override
	public Bike createBike() {
		return bike;
	}
}