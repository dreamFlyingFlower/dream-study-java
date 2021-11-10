package com.wy.state;

public class NoState implements State {

	public void doSomething(JadeDynasty jadeDynasty) {
		System.out.println(jadeDynasty.getAge() + "修仙");
	}
}