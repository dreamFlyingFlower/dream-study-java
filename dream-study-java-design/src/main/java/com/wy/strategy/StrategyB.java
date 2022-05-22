package com.wy.strategy;

public class StrategyB implements Strategy {

	@Override
	public void drawText(String text, int lineWidth, int lineCount) {
		System.out.println("StrategyB......drawtext");
	}
}