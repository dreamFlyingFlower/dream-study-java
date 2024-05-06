package com.wy.transaction;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * {@link TransactionSynchronizationManager}:这个类内部所有的变量,方法都是static修饰的,也就是说它其实是一个工具类,是一个事务同步器
 * 
 * @author 飞花梦影
 * @date 2018-03-06 22:37:19
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class MyTransactionSynchronizationManager {

	private static final ExecutorService executor = Executors.newSingleThreadExecutor();

	public static void main(String[] args) {
		// 判断当前线程是否有事务
		TransactionSynchronizationManager.isActualTransactionActive();
		// 有事务,则添加一个事务同步器,并重写afterCompletion(),此方法在事务提交后会做回调
		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

			@Override
			public void afterCompletion(int status) {
				// 事务提交后,再异步发送消息给MQ等异步调用方法
				if (status == TransactionSynchronization.STATUS_COMMITTED) {
					executor.submit(() -> {
						try {
							// 发送消息给kafka
						} catch (Exception e) {
							// 记录异常信息,发邮件或者进入待处理列表
						}
					});
				}
			}
		});
	}
}