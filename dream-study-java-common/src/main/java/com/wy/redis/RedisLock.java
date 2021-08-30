package com.wy.redis;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import com.wy.digest.DigestTool;

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

	@Autowired
	private RedisTemplate<String, String> stringRedisTemplate;

	/**
	 * 使用Redisson做分布式锁
	 * 
	 * @throws Exception
	 */
	public void redissonLock() throws Exception {
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

	/**
	 * 直接使用redis做分布式锁
	 * 
	 * @throws InterruptedException
	 */
	public void redisLock() throws InterruptedException {
		// 生成的随机uuid,避免删除锁时删除其他线程的锁
		String uuid = DigestTool.uuid();
		// 分布式锁占坑,设置过期时间,必须和加锁一起作为原子性操作
		Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent("lock", uuid, 300, TimeUnit.SECONDS);
		if (lock) {
			try {
				// 加锁成功执行业务
				// do something
			} finally {
				// 利用redis的脚本功能执行删除的操作,删除自己的锁,需要原子环境,否则可能锁刚过期,删除的是别人的锁
				String script =
						"if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
				// 该操作可以返回操作是否成功,1成功,0失败
				stringRedisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class), Arrays.asList("lock"),
						uuid);
			}
		} else {
			// 加锁失败,自旋,必须要休眠一定时间,否则对cpu消耗极大,且容易抛异常
			TimeUnit.MILLISECONDS.sleep(100);
			redisLock();
		}
	}
}