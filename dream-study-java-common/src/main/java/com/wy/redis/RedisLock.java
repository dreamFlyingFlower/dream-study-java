package com.wy.redis;

import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Redis的分布式锁:redisson,需要添加相关依赖
 * 
 * @author ParadiseWY
 * @date 2020-12-23 11:08:09
 * @git {@link https://github.com/mygodness100}
 */
@Component
public class RedisLock {

	@Autowired
	private RedissonClient redissonClient;

	public void test() throws Exception {
		// 获取一把锁,只要锁的名称一样就是同一把锁
		RLock lock = redissonClient.getLock("lock");
		try {
			// 加锁,阻塞等待,默认30秒过期
			// redisson的锁会自动续期,即业务超长时间,不用担心锁过期,默认是续期30秒
			// 若业务完成或线程突然断开,redisson将不会自动续期,即使不手动解锁,锁默认在30秒之后也会自动删除
			// 在拿到锁之后会设置一个定时任务,每10秒刷新一次过期时间,会自动续期,若线程断开,自然无法自动续期
			lock.lock();
			// 自定义过期时间,但是该方法不会自动续期,即业务时间超长锁就会自动删除
			// lock.lock(10, TimeUnit.SECONDS);
			// 加读写锁,读数据时加读锁,写的时候加写锁
			// 写锁存在时,不管其他线程是读或写,都需要等待
			// 读锁存在时,其他线程若是读,则无需等待,若是写,则写需要等待
			RReadWriteLock readWriteLock = redissonClient.getReadWriteLock("rw-lock");
			// 读锁
			RLock readLock = readWriteLock.readLock();
			readLock.lock();
			// dosomething
			readLock.unlock();
			// 写锁
			RLock writeLock = readWriteLock.writeLock();
			writeLock.lock();
			// dosomething
			writeLock.unlock();
			// 信号量,用来限流
			// 参数是redis中的一个key,该key的值必须是一个正整数
			RSemaphore semaphore = redissonClient.getSemaphore("semaphore");
			// 默认获取一个信号量,则该key表示的值将减1
			// 若该key表示的值已经等于0,则无法获取信号量,此时就会阻塞等待
			semaphore.acquire();
			// 一次获取2个信号量
			semaphore.acquire(2);
			// 尝试获取信号量,能获取就返回true,获取不到就返回false
			boolean tryAcquire = semaphore.tryAcquire();
			System.out.println(tryAcquire);
			// 释放信号量,相当于该key的值加1
			semaphore.release();
			// 释放2个信号量
			semaphore.release(2);
		} finally {
			// 解锁
			lock.unlock();
		}
	}
}