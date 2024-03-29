package com.wy.build.exp;

/**
 * 使用类
 * 
 * @author 飞花梦影
 * @date 2022-09-16 11:24:59
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class Client {

	public static void main(String[] args) {
		// 创建指挥者对象
		Director director = new Director(new MobileBuilder());
		// 让指挥者只会组装自行车
		Bike bike = director.construct();

		System.out.println(bike.getFrame());
		System.out.println(bike.getSeat());
	}
}