package com.wy.proxy;

/**
 * 静态代理: 一个接口,多个实现类,但是只能代理指定的接口类型,不能代理任何类型
 * 动态代理:有jdk的动态代理和cglib的动态代理,各有各的优势和用法,cglib底层使用ASM实现
 * 
 * @author ParadiseWY
 * @date 2020年9月26日 下午11:26:48
 */
public interface S_Proxy {

	void cry();

	void eat();
}