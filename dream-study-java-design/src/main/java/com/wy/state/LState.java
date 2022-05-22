package com.wy.state;

public class LState implements State {

	@Override
	public void doSomething(JadeDynasty jadeDynasty) {
		if (jadeDynasty.getAge() == 12) {
			System.out.println("玩耍,长大");
		} else {
			jadeDynasty.setState(new SState());
			jadeDynasty.doSomething();
		}
	}
}