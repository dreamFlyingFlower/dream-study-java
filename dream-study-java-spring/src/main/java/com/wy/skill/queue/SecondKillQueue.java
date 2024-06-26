package com.wy.skill.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.wy.skill.SuccessKilled;

/**
 * 利用阻塞队列进行秒杀(固定长度为100),当队列长度与商品数量一致时,会出现少卖的现象,可以调大数值
 *
 * @author 飞花梦影
 * @date 2024-05-29 10:41:43
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class SecondKillQueue {

	/** 队列大小 */
	static final int QUEUE_MAX_SIZE = 100;

	/** 用于多线程间下单的队列 */
	static BlockingQueue<SuccessKilled> blockingQueue = new LinkedBlockingQueue<SuccessKilled>(QUEUE_MAX_SIZE);

	/** 使用静态内部类,实现单例模式 */
	private SecondKillQueue() {

	};

	private static class SingletonHolder {

		// 静态初始化器,由JVM来保证线程安全
		private static SecondKillQueue queue = new SecondKillQueue();
	}

	/**
	 * 单例队列
	 * 
	 * @return
	 */
	public static SecondKillQueue getSkillQueue() {
		return SingletonHolder.queue;
	}

	/**
	 * 生产入队
	 * 
	 * @param kill 秒杀对象
	 * @return 是否成功
	 * @throws InterruptedException
	 */
	public Boolean produce(SuccessKilled kill) {
		// 队列未满时,返回true;队列满则抛出IllegalStateException(“Queue full”)异常
		// blockingQueue.add(kill);
		// 队列未满时,直接插入没有返回值;队列满时会阻塞等待,一直等到队列未满时再插入
		// blockingQueue.put(kill);
		// 设定等待的时间,如果在指定时间内还不能往队列中插入数据则返回false,插入成功返回true
		// blockingQueue.offer(kill, 1000, null);
		// 队列未满时,返回true;队列满时返回false,非阻塞立即返回
		return blockingQueue.offer(kill);
	}

	/**
	 * 消费出队
	 * 
	 */
	public SuccessKilled consume() throws InterruptedException {
		// 获取并移除队首元素,在指定的时间内去轮询队列看有没有首元素有则返回,否者超时后返回null
		// blockingQueue.poll();
		// 与带超时时间的poll类似不同在于take时候如果当前队列空了它会一直等待其他线程调用notEmpty.signal()才会被唤醒
		return blockingQueue.take();
	}

	/**
	 * 获取队列大小
	 * 
	 * @return
	 */
	public int size() {
		return blockingQueue.size();
	}
}