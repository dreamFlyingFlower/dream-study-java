package com.wy.design.adapter;

/**
 * 第二种适配
 * @author paradiseWy
 */
public class AdapterA extends Text implements Shape {
	public void Draw() {
		System.out.println("Draw a shap ! Impelement Shape interface !");
	}

	public void Border() {
		System.out.println("Set the border of the shap ! Impelement Shape interface !");
	}

	public static void main(String[] args) {
		AdapterA adapterA = new AdapterA();
		adapterA.Draw();
		adapterA.Border();
		adapterA.SetContent("A test text !");
		System.out.println("The content in Text Shape is :" + adapterA.GetContent());
	}
}