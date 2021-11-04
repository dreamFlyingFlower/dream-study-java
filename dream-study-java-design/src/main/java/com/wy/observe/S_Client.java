package com.wy.observe;

/**
 * 观察者模式:当被观察者执行完方法之后,通知观察者做其他的事,可直接利用JDK的Observable和Observer
 * 观察者可以传递,即观察者可以是观察者,也可以是被观察者,但是最好只有1个这样的中间对象<br>
 * 因为要考虑安全因素,observer中都是同步
 * 
 * @author 飞花梦影
 * @date 2021-11-04 23:03:48
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class S_Client {

	public static void main(String[] args) {
		S_Observer o1 = new S_Observer();
		S_Observable o2 = new S_Observable();
		// 注册观察者
		o2.addObserver(o1);
		o2.getUp();
		// 输出观察者数量
		System.out.println(o2.countObservers());
		// 删除所有观察者
		o2.deleteObservers();
	}
}