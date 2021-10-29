package com.wy.redis;

/**
 * 业务执行类
 *
 * @author 飞花梦影
 * @date 2021-10-29 16:53:40
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class DelayJobHandler implements Runnable {

	private DelayBucket delayBucket;

	private TaskJobPool jobPool;

	private ReadyQueue readyQueue;

	private int row;

	public DelayJobHandler(DelayBucket delayBucket, TaskJobPool jobPool, ReadyQueue readyQueue, int row) {
		this.delayBucket = delayBucket;
		this.jobPool = jobPool;
		this.readyQueue = readyQueue;
		this.row = row;
	}

	@Override
	public void run() {
		System.out.println(delayBucket);
		System.out.println(jobPool);
		System.out.println(readyQueue);
		System.out.println(row);
	}
}