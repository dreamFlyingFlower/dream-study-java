package com.wy.simplefactory;

import com.wy.entity.Air;
import com.wy.entity.Cartoon;
import com.wy.entity.Clannad;

/**
 * 简单工厂模式,建造模式.构建实例可以使用反射,也可以使用名称
 * 
 * 简单工厂模式和策略模式:工厂注重对象的创建,而策略是注重行为的实现
 * 
 * 建造模式和简单工厂模式差不多,但工厂模式注重创建对象,而建造模式则是接口中多个方法的调用顺序
 * 
 * 缺点:每新增一个Cartoon的实现就需要改动工厂类,实现越多工作量越大
 * 
 * @author 飞花梦影
 * @date 2021-11-03 09:25:17
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class FactorySimpleCartoon {

	public static Cartoon build(Class<? extends Cartoon> factory) {
		try {
			return factory.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Cartoon build(String name) {
		if ("Air".equalsIgnoreCase(name)) {
			return new Air();
		}
		if ("clannad".equalsIgnoreCase(name)) {
			return new Clannad();
		}
		return null;
	}

	public static void main(String[] args) {
		Cartoon cartoon1 = FactorySimpleCartoon.build(Air.class);
		Cartoon cartoon2 = FactorySimpleCartoon.build(Clannad.class);
		System.out.println(cartoon1.chineseName());
		System.out.println(cartoon2.chineseName());
	}
}