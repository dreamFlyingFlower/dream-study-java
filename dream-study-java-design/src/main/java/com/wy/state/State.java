package com.wy.state;

/**
 * 状态模式:通过不同状态来展示不同的行为,其实就是if-else-if
 *
 * @author 飞花梦影
 * @date 2021-11-10 13:37:38
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public interface State {

	void doSomething(JadeDynasty jadeDynasty);
}