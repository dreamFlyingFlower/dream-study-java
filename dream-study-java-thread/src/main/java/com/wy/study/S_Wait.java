package com.wy.study;

/**
 * wait,notify,notifyall:必须在同步代码块中使用.wait和notify使用的时候还需要是同一把锁.
 * 
 * wait():被调用的时候会释放锁,此时若是有多个线程同时在wait()等待,当wait被唤醒时,所有等待的线程都将被执行
 * 
 * notify():会随机唤醒一个处于等待的线程;notifyAll():唤醒所有等待的线程,但拿到时间片的只有一个;
 * notify()调用之后wait线程立刻被唤醒,但是仍然需要notify()所持有的同步代码块释放锁才会调用wait()之后的代码,
 * 
 * @author ParadiseWY
 * @date 2020-11-28 10:17:38
 * @git {@link https://github.com/mygodness100}
 */
public class S_Wait {

	private volatile int signal;

	public void set(int value) {
		this.signal = value;
	}

	public int get() {
		return signal;
	}

	public static void main(String[] args) {
		S_Wait d = new S_Wait();
		new Thread(new Runnable() {

			@Override
			public void run() {
				synchronized (d) {
					System.out.println("修改状态的线程执行...");
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					d.set(1);
					System.out.println("状态值修改成功...");
					d.notify();
				}
			}
		}).start();

		new Thread(new Runnable() {

			@Override
			public void run() {
				synchronized (d) {
					// 等待signal为1开始执行,否则不能执行
					while (d.get() != 1) {
						try {
							// 等待和唤醒需要使用同一把锁,否则抛异常
							d.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					// 当信号为1 的时候,执行代码
					System.out.println("模拟代码的执行...");
				}
			}
		}).start();
	}
}