package com.wy.decorator;

public interface Animal {

	public void show();

	default void run() {

	}
}