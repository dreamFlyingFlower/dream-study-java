package com.wy.design.factory;

/**
 * 工厂设计模式,建造模式
 * @description 工厂设计模式和策略模式的不同,工厂注重对象的创建,而策略是注重行为的实现
 * @instruction 建造模式和工厂模式差不多,只不过工厂是注重创建对象,而建造模式则是接口中多个方法的调用顺序
 * @author paradiseWy
 */
public interface Factory {

	void cry();

	void eat();
}