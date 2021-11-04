package com.wy.decorator;

public class WaterAnimalDecorator extends AnimalDecorator {

	public WaterAnimalDecorator(Animal car) {
		super(car);
	}

	public void show() {
		this.car.show();
		this.swim();
	}

	public void swim() {
		System.out.println("能游泳");
	}
}