package com.wy.netty.tcp.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.wy.netty.tcp.MessageProcessor;
import com.wy.netty.tcp.NettyMsgModel;
import com.wy.netty.tcp.QueueHolder;
import com.wy.netty.tcp.ThreadFactoryImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * Netty TCP长连接客户端
 *
 * @author 飞花梦影
 * @date 2024-04-28 17:04:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@SpringBootApplication
@Slf4j
public class NettyClientApplication implements CommandLineRunner {

	@Autowired
	private MessageProcessor messageProcessor;

	private static final Integer MAIN_THREAD_POOL_SIZE = 4;

	public static void main(String[] args) {
		SpringApplication.run(NettyClientApplication.class, args);
	}

	private final ExecutorService executor =
			Executors.newFixedThreadPool(MAIN_THREAD_POOL_SIZE, new ThreadFactoryImpl("Demo_TestThread_", false));

	@Override
	public void run(String... args) throws Exception {
		Thread loopThread = new Thread(new LoopThread());
		loopThread.start();
	}

	/**
	 * 使用一个类保存队列的静态实例以便在任何类中都可以快速引用,之后需要启动一个线程去监听队列中的消息,一但消息投递到队列中,就取出消息然后异步多线程处理该消息
	 *
	 * @author 飞花梦影
	 * @date 2024-04-28 17:43:35
	 * @git {@link https://github.com/dreamFlyingFlower}
	 */
	public class LoopThread implements Runnable {

		@Override
		public void run() {
			for (int i = 0; i < MAIN_THREAD_POOL_SIZE; i++) {
				executor.execute(() -> {
					while (true) {
						// 取走BlockingQueue里排在首位的对象,若BlockingQueue为空,阻断进入等待状态直到
						try {
							NettyMsgModel nettyMsgModel = QueueHolder.get().take();
							messageProcessor.process(nettyMsgModel);
						} catch (InterruptedException e) {
							log.error(e.getMessage(), e);
						}
					}
				});
			}
		}
	}
}