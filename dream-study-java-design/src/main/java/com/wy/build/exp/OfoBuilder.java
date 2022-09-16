package com.wy.build.exp;

/**
 * ofo单车构建者,用来构建ofo单车
 * 
 * @author 飞花梦影
 * @date 2022-09-16 11:25:53
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class OfoBuilder extends Builder {

	@Override
	public void buildFrame() {
		bike.setFrame("铝合金车架");
	}

	@Override
	public void buildSeat() {
		bike.setSeat("橡胶车座");
	}

	@Override
	public Bike createBike() {
		return bike;
	}
}