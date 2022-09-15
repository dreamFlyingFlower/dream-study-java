package com.wy.decorator;

/**
 * 继承抽象装饰器,实现自己的扩展方法
 * 
 * @author 飞花梦影
 * @date 2022-09-15 10:15:59
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class FlyAnimalDecorator extends AnimalDecorator {

	public FlyAnimalDecorator(Animal animal) {
		super(animal);
	}

	@Override
	public void show() {
		this.animal.show();
		this.fly();
	}

	public void fly() {
		System.out.println("能飞");
	}
}