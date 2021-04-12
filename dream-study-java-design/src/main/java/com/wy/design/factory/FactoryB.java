package com.wy.design.factory;

public class FactoryB implements Factory {
	@Override
	public void cry() {
		System.out.println("B cry....");
	}

	@Override
	public void eat() {
		System.out.println("B eat...");
	}
}