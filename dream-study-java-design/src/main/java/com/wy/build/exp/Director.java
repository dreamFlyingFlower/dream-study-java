package com.wy.build.exp;

/**
 * 指挥者类
 * 
 * @author 飞花梦影
 * @date 2022-09-16 11:25:16
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class Director {

	// 声明builder类型的变量
	private Builder builder;

	public Director(Builder builder) {
		this.builder = builder;
	}

	// 组装自行车的功能
	public Bike construct() {
		builder.buildFrame();
		builder.buildSeat();
		return builder.createBike();
	}
}