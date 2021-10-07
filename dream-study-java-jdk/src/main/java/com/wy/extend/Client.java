package com.wy.extend;

/**
 * 继承和实现
 * 
 * @apiNote 非静态->同名属性,只根据申明的类来取值,接口也一样;同名方法则看实体对象的指向,接口也一样
 *          静态->同名属性和同名方法,只根据申明的类来取值,接口也一样
 * 
 * @author ParadiseWY
 * @date 2020-09-29 09:45:45
 */
public class Client {

	public static void main(String[] args) {
		Parent parent = new Son();
		System.out.println(parent.richer); // 500000
		System.out.println(((Parent) parent).richer);// 500000
		// System.out.println(parent.boy); // 10
		// System.out.println(((Parent) parent).boy); // 10
		parent.name(); // son
		// parent.name1(); // parent1
		((Parent) parent).name(); // son
		// ((Parent) parent).name1(); // parent1

		Son son = new Son();
		System.out.println(son.richer); // 500
		System.out.println(((Parent) son).richer);// 500000
		son.name(); // son
		// son.name1(); // son1
		((Parent) son).name(); // son
		// ((Parent) son).name1(); // parent1

		IParent iParent = new ISon();
		// System.out.println(iParent.richer); // 500000
		// System.out.println(((IParent)iParent).richer); // 500000
		IParent.name(); // iparent static func
		iParent.name1(); // ison default name1

		ISon iSon = new ISon();
		// System.out.println(iSon.richer); // 50
		// System.out.println(((IParent)iSon).richer); // 500000
		// iSon.name(); // iparent static func
		// iSon.name1(); // ison default name1
		((IParent) iSon).name1(); // ison default name1
	}
}