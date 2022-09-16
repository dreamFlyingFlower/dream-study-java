package com.wy.flyweight;

/**
 * 抽象享元角色
 * 
 * @author 飞花梦影
 * @date 2022-09-16 11:16:13
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public interface Box {

	/**
	 * 获取图形的方法
	 * 
	 * @return
	 */
	public abstract String getShape();

	// 显示图形及颜色
	default void display(String color) {
		System.out.println("方块形状：" + getShape() + ", 颜色：" + color);
	}
}
