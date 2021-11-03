package com.wy.template;

/**
 * 实现类可根据需要对某些方法进行扩展后改写
 * @author paradiseWy
 *
 */
public class TemplateA extends TemplateDefault {

	@Override
	public void start() {
		System.out.println("悍马H2发动...");
	}

	@Override
	public void stop() {
		System.out.println("悍马H1停车...");
	}

	@Override
	public void alarm() {
		System.out.println("悍马H2鸣笛...");
	}

	@Override
	public void engineBoom() {
		System.out.println("悍马H2引擎声音是这样在...");
	}
}