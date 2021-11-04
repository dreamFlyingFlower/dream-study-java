package com.wy.observe;

import java.util.Observable;

/**
 * 被观察者,当被观察者调用指定的方法:setChanged()时,会通知所有观察者,观察者收到通知调用update方法
 *
 * @author 飞花梦影
 * @date 2021-11-04 23:00:02
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class S_Observable extends Observable {

	public void getUp() {
		System.out.println("起床了....");
		// 调用该方法通知观察者被观察者有改变
		this.setChanged();
		// 测试对象是否改变
		if (this.hasChanged()) {
			System.out.println(11);
		}
		this.notifyObservers(this);
	}
}