package com.wy.disruptor;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.wy.disruptor.DisruptorService;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2024-04-01 17:20:24
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestDisruptor.class)
public class TestDisruptor {

	@Autowired
	private DisruptorService disruptorService;

	/**
	 * 项目内部使用Disruptor做消息队列
	 * 
	 * @throws Exception
	 */
	@Test
	public void sayHelloTest() throws Exception {
		disruptorService.sayHello("消息到了，Hello world!");
		log.info("消息队列已发送完毕");
		// 这里停止2000ms是为了确定是处理消息是异步的
		Thread.sleep(2000);
	}
}