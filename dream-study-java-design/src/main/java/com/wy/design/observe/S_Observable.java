package com.wy.design.observe;

import java.util.Observable;

/**
 * 被观察者,当被观察者调用指定的方法:setChanged()时,会通知所有观察者,观察者收到通知调用update方法
 *
 * @author ParadiseWY
 * @date 2020年9月26日 下午10:50:38
 */
public class S_Observable extends Observable {

	public void getUp() {
		System.out.println("起床了....");
		this.setChanged();
		if (this.hasChanged()) {
			System.out.println(11);
		}
		this.notifyObservers(this);
	}
}