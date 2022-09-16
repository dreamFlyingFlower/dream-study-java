package com.wy.flyweight;

import java.util.HashMap;

/**
 * 工厂类,将该类设计为单例
 * 
 * @author 飞花梦影
 * @date 2022-09-16 11:17:00
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class BoxFactory {

	private HashMap<String, Box> map;

	// 在构造方法中进行初始化操作
	private BoxFactory() {
		map = new HashMap<String, Box>();
		map.put("I", new IBox());
		map.put("L", new LBox());
	}

	// 提供一个方法获取该工厂类对象
	public static BoxFactory getInstance() {
		return factory;
	}

	private static BoxFactory factory = new BoxFactory();

	// 根据名称获取图形对象
	public Box getShape(String name) {
		return map.get(name);
	}
}