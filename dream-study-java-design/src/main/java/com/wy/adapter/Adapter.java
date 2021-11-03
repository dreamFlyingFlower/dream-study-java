package com.wy.adapter;

/**
 * 适配器模式:需要用到A中和B中(可能更多)的方法, 但是又不能改A和B,
 * 只好写1个或多个中间类或接口来实现使用AB中的方法
 * @description 调用第三方接口,发现对方是一个map,而本方是一个实体类,而且字段不同,
 *              就需要一个中间类来进行转换
 * @author paradiseWy
 */
public class Adapter implements Shape {
	private Text txt;

	public Adapter(Text txt) {
		this.txt = txt;
	}

	public void Draw() {
		System.out.println("Draw a shap ! Impelement Shape interface !");
	}

	public void Border() {
		System.out.println("Set the border of the shap ! Impelement Shape interface !");
	}

	public void SetContent(String str) {
		txt.SetContent(str);
	}

	public String GetContent() {
		return txt.GetContent();
	}

	public static void main(String[] args) {
		Text myText = new Text();
		Adapter adapter = new Adapter(myText);
		adapter.Draw();
		adapter.Border();
		adapter.SetContent("A test text !");
		System.out.println("The content in Text Shape is :" + adapter.GetContent());
	}
}