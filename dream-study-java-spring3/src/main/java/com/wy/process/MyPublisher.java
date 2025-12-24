package com.wy.process;

import java.util.concurrent.SubmissionPublisher;

/**
 * 消息发布者
 *
 * @author 飞花梦影
 * @date 2022-09-03 11:05:07
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class MyPublisher {

	public static void main(String[] args) {
		SubmissionPublisher<String> submissionPublisher = new SubmissionPublisher<>();
		MyProcessor myProcessor = new MyProcessor();
		try (submissionPublisher; myProcessor) {
			MySubscriber mySubscriber = new MySubscriber();
			// 如果没有processor,可以直接由publisher发送
			// submissionPublisher.subscribe(mySubscriber);
			// 如果有processor,publisher先发布给processor,再由processor发布给subscriber
			submissionPublisher.subscribe(myProcessor);
			myProcessor.subscribe(mySubscriber);
			for (int i = 0; i < 10; i++) {
				// 发布消息,发布者缓存满时submit阻塞
				submissionPublisher.submit("sxxx" + i);
			}
		}
	}
}