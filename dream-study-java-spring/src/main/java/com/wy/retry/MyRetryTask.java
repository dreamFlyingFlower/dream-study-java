package com.wy.retry;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.remoting.RemoteAccessException;

import lombok.extern.slf4j.Slf4j;

/**
 * 重试方法
 *
 * @author 飞花梦影
 * @date 2024-04-25 15:23:08
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Slf4j
public class MyRetryTask {

	public static boolean retryTask(String param) {
		log.info("收到请求参数:{}", param);

		int i = RandomUtils.nextInt(0, 11);
		log.info("随机生成的数:{}", i);
		if (i == 0) {
			log.info("为0,抛出参数异常.");
			throw new IllegalArgumentException("参数异常");
		} else if (i == 1) {
			log.info("为1,返回true.");
			return true;
		} else if (i == 2) {
			log.info("为2,返回false.");
			return false;
		} else {
			// 为其他
			log.info("大于2,抛出自定义异常.");
			throw new RemoteAccessException("大于2,抛出远程访问异常");
		}
	}
}