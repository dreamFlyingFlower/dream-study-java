package com.wy.design.observe;

import java.util.Observable;
import java.util.Observer;

/**
 * 观察者,当被观察者改变的时候触发update方法
 *
 * @author ParadiseWY
 * @date 2020年9月26日 下午10:50:07
 */
public class S_Observer implements Observer {

	@Override
	public void update(Observable o, Object arg) {
		System.out.println("我知道你起床了");
	}
}