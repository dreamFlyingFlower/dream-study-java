package com.wy.study;

/**
 * wait,notify,notifyall:必须在同步代码块中使用,即必须放在synchronized代码块中.wait和notify使用的时候还需要是同一把锁.
 * 
 * wait():被调用的时候会释放锁,此时若是有多个线程同时在wait()等待,当wait被唤醒时,所有等待的线程都将被执行.wait()会释放锁
 * 
 * notify():会随机唤醒一个处于等待的线程;notifyAll():唤醒所有等待的线程,但拿到时间片的只有一个线程;
 * notify()调用之后wait线程立刻被唤醒,但是仍然需要notify()所持有的同步代码块释放锁才会调用wait()之后的代码.即notify不会释放锁
 * wait()和notify()的作用域都是当前对象,直接调用时会阻塞或唤醒所在对象
 * 
 * 为什么wait()的时候必须释放锁:
 * 
 * <pre>
 * 1.当线程A进入synchronized(obj1)中之后,也就是对obj1上了锁.此时,调用wait()进入阻塞状态,一直不能退出synchronized代码块
 * 2.那么,线程B永远无法进入synchronized(obi1)同步块里,永远没有机会调用notify(),发生死锁
 * 3.在wait()内部会先释放锁obj1,然后进入阻寒状态,之后,它被另外一个线程用notify()唤醒,重新获取锁
 * 4.其次,wait()调用完成后,执行后面的业务逻辑代码,然后退出synchronized同步块,再次释放锁
 * </pre>
 * 
 * wait/sleep的区别:
 * 
 * <pre>
 * 1.sleep 是 Thread 的静态方法,wait 是 Object 的方法,任何对象实例都能调用
 * 2.sleep 不会释放锁,它也不需要占用锁,wait 会释放锁,但调用它的前提是当前线程占有锁(即代码要在 synchronized 中)
 * 3.它们都可以被 interrupted 方法中断
 * </pre>
 * 
 * 
 * 
 * @author 飞花梦影
 * @date 2020-11-28 10:17:38
 * @git {@link https://github.com/mygodness100}
 */
public class StudyWait {

	private volatile int signal;

	public void set(int value) {
		this.signal = value;
	}

	public int get() {
		return signal;
	}

	public static void main(String[] args) {
		StudyWait d = new StudyWait();
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
					// 此处必须使用while判断,否则多线程环境下容易产生虚假唤醒问题
					while (d.get() != 1) {
						try {
							// 等待和唤醒需要使用同一把锁,否则抛异常
							// 线程在哪里等待的,那唤醒的时候就会在哪里
							// 如果上面的不是while,而是if,那上一个在此处等待的线程被唤醒时,即使d.get()仍不满足条件,依然会继续执行程序
							// 而如果是while,即使在此处等待的线程被唤醒,仍要需要循环判断d.get()的值,不会发生虚假唤醒问题
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