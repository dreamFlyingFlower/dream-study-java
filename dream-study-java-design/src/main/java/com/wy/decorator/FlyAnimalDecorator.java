package com.wy.decorator;

public class FlyAnimalDecorator extends AnimalDecorator {

	public FlyAnimalDecorator(Animal car) {
		super(car);
	}

	public void show() {
		this.car.show();
		this.fly();
	}

	public void fly() {
		System.out.println("能飞");
	}
}