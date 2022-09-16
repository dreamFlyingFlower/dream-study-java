package com.wy.facade;

/**
 * 门面模式:多个对象之间进行交互,封装对象之间复杂的交互
 * 
 * 门面模式和策略模式:门面模式注重于封装复杂过程,而策略模式注重于实现的切换
 * 门面模式和模板模式:模板模式注重过程,方法的调用顺序,隐藏内部细节;门面模式注重多个对象之间的交互
 * 
 * @author 飞花梦影
 * @date 2021-11-04 23:23:13
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface Facade {

	// 首先要写信的内容
	void writeContext(String content);

	// 其次写信封
	void fillEnvelope(String address);

	// 把信放到信封里
	void letterInotoEnvelope();

	// 然后邮递
	void sendLetter();
}