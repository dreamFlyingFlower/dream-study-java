package com.wy.design.prototype;

/**
 * 原型模式,即对原型类的clone,实现Cloneable接口,重写clone方法,可以深浅克隆,只有一个原型,其他都是原型的克隆类
 *
 * @author ParadiseWY
 * @date 2020-09-28 20:45:39
 */
public class Prototype implements Cloneable {

	@Override
	protected Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		Prototype p = new Prototype();
		Prototype p1 = (Prototype) p.clone();// 浅克隆,深克隆需要重写hashcode和equals方法
		System.out.println(p1);
	}
}