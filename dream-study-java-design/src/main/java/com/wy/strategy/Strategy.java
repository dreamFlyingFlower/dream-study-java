package com.wy.strategy;

/**
 * 策略模式
 * @description 一个接口,多个实现类,再加一个使用接口实现类的类,该类需要一个该接口为参数的构造或其他方法
 * @author paradiseWy
 */
public interface Strategy {
	void drawText(String s, int lineWidth, int lineCount);
}