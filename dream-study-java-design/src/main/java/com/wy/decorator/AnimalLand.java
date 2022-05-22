package com.wy.decorator;

public class AnimalLand implements Animal {

	@Override
	public void run() {
		System.out.println("能跑");
	}

	@Override
	public void show() {
		this.run();
	}
}