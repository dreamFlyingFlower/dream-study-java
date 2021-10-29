package com.wy.redis;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2021-10-29 16:44:59
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Component
public class DelayTimer implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private DelayBucket delayBucket;

	@Autowired
	private TaskJobPool jobPool;

	@Autowired
	private ReadyQueue readyQueue;

	@Value("${thread.size}")
	private int length;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		ExecutorService executorService =
				new ThreadPoolExecutor(length, length, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

		for (int i = 0; i < length; i++) {
			executorService.execute(new DelayJobHandler(delayBucket, jobPool, readyQueue, i));
		}
	}
}