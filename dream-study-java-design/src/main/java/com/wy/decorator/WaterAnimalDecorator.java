package com.wy.decorator;

/**
 * 继承抽象装饰器,实现自己的扩展方法
 * 
 * @author 飞花梦影
 * @date 2022-09-15 10:15:59
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class WaterAnimalDecorator extends AnimalDecorator {

	public WaterAnimalDecorator(Animal animal) {
		super(animal);
	}

	@Override
	public void show() {
		this.animal.show();
		this.swim();
	}

	public void swim() {
		System.out.println("能游泳");
	}
}