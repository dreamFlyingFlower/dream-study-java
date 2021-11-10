package com.wy.prototype;

import java.util.ArrayList;
import java.util.Arrays;

import com.wy.entity.StrollSkyJiuGe;

/**
 * 原型模式,即对原型类的clone,实现Cloneable接口,重写clone方法,可以深浅克隆,只有一个原型,其他都是原型的克隆类
 *
 * @author 飞花梦影
 * @date 2020-09-28 20:45:39
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class Prototype {

	public static void main(String[] args) {
		StrollSkyJiuGe p = new StrollSkyJiuGe();
		p.setName("盖聂");
		p.setPeople(new ArrayList<String>(Arrays.asList("大叔", "二叔", "天明")));
		StrollSkyJiuGe p1 = p.clone();
		System.out.println(p.getName());
		System.out.println(p1.getName());
		p.setName("二叔");
		System.out.println(p1.getName());
		p.getPeople().add("少羽");
		System.out.println(p1.getPeople());
	}
}