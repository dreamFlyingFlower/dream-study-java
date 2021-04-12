package com.wy.design.strategy;

/**
 * 该类实现接口和实现类的使用,需要传入一个实现类做参数
 * @author paradiseWy
 */
public class Context {
	private Strategy strategy = null;
	private int lineWidth;
	private int lineCount;
	private String text;

	public Context() {
		lineWidth = 10;
		lineCount = 0;
	}

	public void setStrategy(Strategy s) {
		if (s != null) {
			strategy = s;
		}
	}

	public void drawText() {
		strategy.drawText(text, lineWidth, lineCount);
	}

	public void setText(String str) {
		text = str;
	}

	public void setLineWidth(int width) {
		lineWidth = width;
	}

	public void setLineCount(int count) {
		lineCount = count;
	}

	public String getText() {
		return text;
	}
}