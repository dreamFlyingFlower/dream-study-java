package com.wy.adapter;

/**
 * 第二种适配:直接继承
 * 
 * @author 飞花梦影
 * @date 2021-11-06 17:17:34
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class AdapterA extends Text implements Shape {

	@Override
	public void draw() {
		System.out.println("Draw a shap ! Impelement Shape interface !");
	}

	@Override
	public void border() {
		System.out.println("Set the border of the shap ! Impelement Shape interface !");
	}
}