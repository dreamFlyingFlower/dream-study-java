package com.wy.process;

import java.util.concurrent.Flow.Processor;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.SubmissionPublisher;

/**
 * 将发布者的信息经过转换过滤后再发送给订阅者
 *
 * @author 飞花梦影
 * @date 2022-09-03 13:01:40
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class MyProcessor extends SubmissionPublisher<String> implements Processor<String, String> {

	private Subscription subscription;

	@Override
	public void onSubscribe(Subscription subscription) {
		this.subscription = subscription;
		this.subscription.request(10);
	}

	@Override
	public void onNext(String item) {
		// 处理信息
		if (item.contains("##")) {
			this.submit(item.replaceAll("##", "#####"));
		}
		this.subscription.request(10);
	}

	@Override
	public void onError(Throwable throwable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onComplete() {
		// TODO Auto-generated method stub

	}

}