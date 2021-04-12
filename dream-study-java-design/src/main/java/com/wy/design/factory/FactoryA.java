package com.wy.design.factory;

public class FactoryA implements Factory {
	@Override
	public void cry() {
		System.out.println("A cry...");
	}

	@Override
	public void eat() {
		System.out.println("A eat...");
	}
}