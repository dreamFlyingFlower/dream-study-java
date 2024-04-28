package com.wy.process;

import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

/**
 * 订阅者
 *
 * @author 飞花梦影
 * @date 2022-09-03 10:50:18
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class MySubscriber implements Subscriber<String> {

	private Subscription subscription;

	/**
	 * 当发布者和订阅者第一次建立连接时该方法会被发布者自动调用,发布和会将订阅令牌传入
	 * 
	 * @param subscription 订阅令牌
	 */
	@Override
	public void onSubscribe(Subscription subscription) {
		this.subscription = subscription;
		// 向发布者订阅消息的数量
		this.subscription.request(10);
	}

	/**
	 * 订阅者每接受一次订阅消息数据时,该方法会被发布者自动调用一次
	 * 
	 * @param item 消息
	 */
	@Override
	public void onNext(String item) {
		// 处理消息
		System.out.println(item);
		// 再次订阅消息的数量,即每消费一条消息,都可以再向发布者订阅一次消息
		this.subscription.request(5);
		// 取消消息订阅
		// this.subscription.cancel();
	}

	/**
	 * 当订阅,消费过程出现异常时,该方法会被发布者自动调用
	 * 
	 * @param throwable 异常
	 */
	@Override
	public void onError(Throwable throwable) {
		System.out.println(throwable.getMessage());
	}

	/**
	 * 当令牌中的所有消息全部处理完毕后,即当发布者关闭时,该方法会被调用
	 */
	@Override
	public void onComplete() {
	}
}