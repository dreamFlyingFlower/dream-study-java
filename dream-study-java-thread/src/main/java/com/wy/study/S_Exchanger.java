package com.wy.study;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 有且只能有2个线程,在同一个阻塞点进行数据交换,必须同时都指定exchange方法,否则,只要一个还没有执行,则不交换数据
 *
 * @author ParadiseWY
 * @date 2019-03-09 23:43:26
 * @git {@link https://github.com/mygodness100}
 */
public class S_Exchanger {

	private static ExecutorService pool = Executors.newCachedThreadPool();

	public static void main(String[] args) {
		Exchanger<String> ex = new Exchanger<>();

		pool.execute(new Runnable() {

			@Override
			public void run() {
				try {
					String exchange1 = ex.exchange("交换线程1");
					System.out.println("111:" + exchange1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		pool.execute(new Runnable() {

			@Override
			public void run() {
				try {
					String exchange1 = ex.exchange("交换线程2");
					System.out.println("222:" + exchange1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		pool.shutdown();
	}
}