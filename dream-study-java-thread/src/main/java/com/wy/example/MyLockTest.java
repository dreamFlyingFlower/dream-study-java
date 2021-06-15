package com.wy.example;

/**
 * 测试自定义锁
 * 
 * @auther 飞花梦影
 * @date 2021-06-15 21:15:02
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class MyLockTest {

	private int value;

	private MyLock lock = new MyLock();

	public int next() {
		lock.lock();
		try {
			Thread.sleep(300);
			return value++;
		} catch (InterruptedException e) {
			throw new RuntimeException();
		} finally {
			lock.unlock();
		}
	}

	public void a() {
		lock.lock();
		System.out.println("a");
		b();
		lock.unlock();
	}

	public void b() {
		lock.lock();
		System.out.println("b");
		lock.unlock();
	}

	public static void main(String[] args) {
		MyLockTest m = new MyLockTest();
		new Thread(new Runnable() {

			@Override
			public void run() {
				m.a();
			}
		}).start();
	}
}