package com.wy.study;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 多个线程在同一个阻塞点进行数据交换,必须同时都指定exchange方法,否则,只要一个还没有执行,则不交换数据
 * 
 * Exchanger的核心机制和Lock一样,也是CAS+park/unpark
 * 在Exchanger内部,有两个内部类: Participant和Node,每个线程在调用exchange()交换数据的时候,会先创建一个Node对象.
 * 这个Node对象就是对该线程的包装,里面包含了3个重要字段: 第一个是该线程要交互的数据,第 二个是对方线程交换来的数据,最后一个是该线程自身
 * 一个Node只能支持2个线程之间交换数据,要实现多个线程并行地交换数据,需要多个Node,因此在Exchanger里面定义了Node数组
 * 
 * <pre>
 * {@link Exchanger#exchange()}:数据交换.
 * ->如果arena不是null,表示启用了arena方式交换数据;
 * ->如果arena不是null,并且线程被中断,则抛异常;
 * ->如果arena不是null,并且arenaExchange的返回值为null,则抛异常.对方线程交换来的null值是封装为NULL_ITEM对象的,而不是null
 * ->如果slotExchange的返回值是null,并且线程被中断,则抛异常;
 * ->如果slotExchange的返回值是null,并且areaExchange的返回值是null,则抛异常
 * 
 * {@link Exchanger#slotExchange}:如果不启用arenas,则使用该方法进行线程间数据交换.
 * 		3个参数分别是需要交换的数据;是否是计时等待,true表示是计时等待;如果是计时等待,该值表示最大等待的时长.
 * 		返回对方线程交换来的数据;如果等待超时或线程中断,或者启用了arena,则返回null.
 * 
 * {@link Exchanger#arenaExchange}:当启用arenas的时候,使用该方法进行线程间的数据交换
 * 		3个参数分别是本线程要交换的非null数据;是否是计时等待,true表示是计时等待;如果是计时等待,该值表示最大等待的时长.
 * 		返回对方线程交换来的数据;如果线程被中断,或者等待超时,则返回null
 * </pre>
 *
 * @author 飞花梦影
 * @date 2019-03-09 23:43:26
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class StudyExchanger {

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