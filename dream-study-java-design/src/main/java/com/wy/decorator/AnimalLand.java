package com.wy.decorator;

public class AnimalLand implements Animal {

	public void run() {
		System.out.println("能跑");
	}

	public void show() {
		this.run();
	}
}