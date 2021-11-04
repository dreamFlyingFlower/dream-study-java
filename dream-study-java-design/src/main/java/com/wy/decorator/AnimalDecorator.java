package com.wy.decorator;

public abstract class AnimalDecorator implements Animal {

	protected Animal car;

	public AnimalDecorator(Animal car) {
		this.car = car;
	}
}