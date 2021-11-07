package com.wy.adapter;

/**
 * 适配器模式:需要用到A中和B中(可能更多)的方法, 但是又不能改A和B,只好写1个或多个中间类或接口来实现使用AB中的方法
 * 
 * 调用第三方接口,发现对方是一个map,而本方是一个实体类,而且字段不同, 就需要一个中间类来进行转换
 * 
 * 第一种方式:委派模式,将需要适配的类或接口作为构造传入
 * 
 * @author 飞花梦影
 * @date 2021-11-06 17:13:17
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class Adapter implements Shape {

	private Text txt;

	public Adapter(Text txt) {
		this.txt = txt;
	}

	@Override
	public void draw() {
		System.out.println("Draw a shap ! Impelement Shape interface !");
	}

	@Override
	public void border() {
		System.out.println("Set the border of the shap ! Impelement Shape interface !");
	}

	public void setContent(String str) {
		txt.setContent(str);
	}

	public String getContent() {
		return txt.getContent();
	}
}