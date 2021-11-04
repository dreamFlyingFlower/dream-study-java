package com.wy.facade;

/**
 * 门面模式:比简单的封装更深的封装
 * 
 * 比如写信,需要写信,放到信封,送到邮局,写地址,再送;其中的顺序不能改,但是用户不知道,可能会乱调用,所以需要封装起来
 * 
 * 门面模式和策略模式的不同区别:门面模式注重于封装复杂过程,而策略模式注重于实现的切换
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