package com.wy.study;

import java.util.concurrent.TimeUnit;

/**
 * 多线程基础
 * 
 * 优先级:{@link Thread#MAX_PRIORITY},{@link Thread#MIN_PRIORITY}
 * 
 * <pre>
 * 线程状态:初始化start()->被调用之后处于就绪状态run()->运行,抢到CPU时间片->阻塞,会释放锁等资源->消亡
 * 从运行到阻塞状态可以调用sleep(),wait(),同步块,唤醒可调用notify(),notifyAll()
 * </pre>
 * 
 * 线程的打断,恢复:Interrupt()
 * 
 * yield:会让当前线程从Running->Runnable,然后调度执行其他同优先级的线程,如果没有同优先级线程,可能不会有暂停效果
 * 
 * @author 飞花梦影
 * @date 2019-05-08 20:39:35
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class S_Thread {

	public static void main(String[] args) {
		Thread thread1 = new Thread(new TDaemon());
		// 线程是否后台启动,即当主线结束时,子线程也结束
		// 若设置为true,当主线程结束时,子线程就结束,不管子线程是否执行完毕
		// 若设置为false,所有的子线程结束,主线程才能结束
		// thread1.setDaemon(true);
		// 设置优先级,最大是10,最小是1,值越大的优先级越高,但是线程的运行并不一定是优先级越高越先运行
		thread1.setPriority(5);
		thread1.start();
		Thread thread2 = new Thread(new TInterrupt());
		thread2.start();
		// 当线程启动之后,再中断,线程若是被中断,则抛出指定异常,可以对特殊情况进行特殊处理
		thread2.interrupt();
		System.out.println("main线程结束了...");
	}
}

class TDaemon implements Runnable {

	@Override
	public void run() {
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("子线程运行了");
	}
}

class TInterrupt implements Runnable {

	@Override
	public void run() {

		while (true) {
			// 线程中断标志,interrupt(),wait(),sleep(),join()都会造成线程中断
			// 判断线程是否有打断标记,即是否调用过interrupt(),调用过则返回true,该方法不会清除打断标记
			System.out.println(Thread.currentThread().isInterrupted());
			// 判断线程是否是中断状态,返回当前线程的中断标志,但会将目标中断标志设置为false
			System.out.println(Thread.interrupted());
			try {
				TimeUnit.SECONDS.sleep(1);
				// 如果被打断线程正在sleep(),wait(),join(),被打断线程会抛出InterruptedException,并清除打断标记(false),线程继续执行;
				// 如果被打断线程正在运行,则会设置打断标记为true,但线程仍继续运行;
				// 当线程A调用了LockSupport.park()之后,线程A会暂停;在线程B调用A.interrupt()之后,线程A打断标记为true;
				// 此时LockSupoort.park()检测到打断标记为true,将不阻塞线程A;如果后续再次调用LockSupport.park(),不会有任何效果
				Thread.currentThread().interrupt();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName());
		}
	}
}