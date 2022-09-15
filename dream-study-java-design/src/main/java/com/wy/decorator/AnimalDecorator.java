package com.wy.decorator;

/**
 * 抽象装饰类,需要输入被实现的接口示例,同时可以添加一些公共的方法或属性
 * 
 * @author 飞花梦影
 * @date 2022-09-15 10:13:31
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public abstract class AnimalDecorator implements Animal {

	protected Animal animal;

	public AnimalDecorator(Animal animal) {
		this.animal = animal;
	}
}