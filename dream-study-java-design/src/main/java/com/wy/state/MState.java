package com.wy.state;

public class MState implements State {

	@Override
	public void doSomething(JadeDynasty jadeDynasty) {
		if (jadeDynasty.getAge() == 7) {
			System.out.println("玩耍");
		} else {
			jadeDynasty.setState(new LState());
			jadeDynasty.doSomething();
		}
	}
}