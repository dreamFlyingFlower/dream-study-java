package com.wy.build.exp;

/**
 * 抽象建造类
 * 
 * @author 飞花梦影
 * @date 2022-09-16 11:24:28
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public abstract class Builder {

	// 声明Bike类型的变量,并进行赋值
	protected Bike bike = new Bike();

	public abstract void buildFrame();

	public abstract void buildSeat();

	// 构建自行车的方法
	public abstract Bike createBike();
}