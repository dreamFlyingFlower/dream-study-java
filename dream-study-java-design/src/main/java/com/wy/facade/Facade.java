package com.wy.facade;

/**
 * 门面模式:比简单的封装更深的封装
 * @description 比如写信,需要先写信,然后放到信封,之后要送到邮局,再写地址,再送;
 *              从写信,到邮局送信,只需要做内容和地址不一样,其他都可以封装,而内容和地址就是参数;
 *              而其中的顺序则是不能改的,但是用户不知道,可能会乱调用,所以需要封装起来.
 * @instruction 门面模式和策略模式的不同区别:门面模式注重于封装复杂过程,而策略模式注重于实现的切换
 * @author paradiseWy 2018年12月8日
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