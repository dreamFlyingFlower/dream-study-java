package com.wy.state;

public class SState implements State {

	@Override
	public void doSomething(JadeDynasty jadeDynasty) {
		if (jadeDynasty.getAge() == 18) {
			System.out.println("็่ฏไป");
		} else {
			jadeDynasty.setState(new NoState());
			jadeDynasty.doSomething();
		}
	}
}