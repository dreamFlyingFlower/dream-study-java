package com.wy.observe;

/**
 * 观察者模式:当被观察者执行完方法之后,通知观察者做其他的事,现在可直接利用jdk的Observable和Observer
 * 观察者可以传递,即观察者可以是观察者,也可以是被观察者,但是最好只有1个这样的中间对象<br>
 * 因为要考虑安全因素,observer中都是同步
 *
 * @author ParadiseWY
 * @date 2020年9月26日 下午10:54:03
 */
public class S_Client {

	public static void main(String[] args) {
		S_Observer o1 = new S_Observer();
		S_Observable o2 = new S_Observable();
		o2.addObserver(o1);
		o2.getUp();
	}
}