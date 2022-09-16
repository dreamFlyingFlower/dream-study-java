package com.wy.flyweight;

/**
 * 调用类
 * 
 * @author 飞花梦影
 * @date 2022-09-16 11:18:44
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class Client {

	public static void main(String[] args) {
		// 获取I图形对象
		Box box1 = BoxFactory.getInstance().getShape("I");
		box1.display("灰色");

		// 获取L图形对象
		Box box2 = BoxFactory.getInstance().getShape("L");
		box2.display("绿色");
	}
}