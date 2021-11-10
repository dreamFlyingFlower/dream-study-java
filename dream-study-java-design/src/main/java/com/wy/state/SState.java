package com.wy.state;

public class SState implements State {

	public void doSomething(JadeDynasty jadeDynasty) {
		if (jadeDynasty.getAge() == 18) {
			System.out.println("看诛仙");
		} else {
			jadeDynasty.setState(new NoState());
			jadeDynasty.doSomething();
		}
	}
}