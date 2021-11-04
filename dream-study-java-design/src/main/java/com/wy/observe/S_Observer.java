package com.wy.observe;

import java.util.Observable;
import java.util.Observer;

/**
 * 观察者,当被观察者改变的时候触发update方法
 *
 * @author 飞花梦影
 * @date 2021-11-04 23:00:18
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class S_Observer implements Observer {

	@Override
	public void update(Observable o, Object arg) {
		System.out.println("我知道你起床了");
	}
}