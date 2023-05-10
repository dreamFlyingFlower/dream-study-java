package com.wy.study;

import java.util.concurrent.TimeUnit;

/**
 * 多线程基础
 * 
 * 优先级:{@link Thread#MAX_PRIORITY},{@link Thread#MIN_PRIORITY}
 * 
 * {@link Thread.State}:线程状态
 * 
 * <pre>
 * {@link Thread.State#NEW}:初始化,新建,还没有调用start()
 * {@link Thread.State#RUNNABLE}:就绪状态,随时可以运行,此时已经调用了start()
 * {@link Thread.State#BLOCKED}:阻塞,如获取其他被其他线程占用的锁
 * {@link Thread.State#WAITING}:无限期等待,如果没有其他线程唤醒将一直等待,如join(),wait(),park()
 * {@link Thread.State#TIMED_WAITING}:超时等待,指定等待的时间,超过该时间将不再等待,如sleep(time),wait(time),join(time)
 * {@link Thread.State#TERMINATED}:销毁
 * 
 * 线程状态:初始化->start()被调用之后处于就绪状态->run()运行,抢到CPU时间片->阻塞,会释放锁等资源->消亡
 * 从运行到阻塞状态可以调用sleep(),wait(),同步块,唤醒可调用notify(),notifyAll()
 * 
 * {@link Thread#getId()}:返回Thread对象的标识符.该标识符是在钱程创建时分配的一个正整数,在线程的整个生命周期中唯一且无法改变
 * {@link Thread#interrupt()}:中断目标线程,给目标线程发送一个中断信号,线程被打上中断标记
 * {@link Thread#interrupted()}:判断目标线程是否被中断,但是将清除线程的中断标记
 * {@link Thread#isInterrupted()}:判断目标线程是否被中断,不会清除中断标记
 * {@link Thread#join()}:暂停线程的执行,直到调用该方法的线程执行结束为止。可以使用该方法等待另一个Thread对象结束
 * {@link Thread#setUncaughtExceptionHandler()}:当线程执行出现未校验异常时,该方法用于建立未校验异 常的控制器
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
public class StudyThread {

	public static void main(String[] args) throws Exception {
		Thread thread1 = new Thread(new TDaemon());
		// 线程是否后台启动,即当主线结束时,子线程也结束
		// 若设置为true,当主线程结束时,子线程就结束,不管子线程是否执行完毕
		// 若设置为false,所有的子线程结束,主线程才能结束
		// thread1.setDaemon(true);
		// 判断是否为守护线程
		thread1.isDaemon();
		// 设置优先级,最大是10,最小是1,值越大的优先级越高,但是线程的运行并不一定是优先级越高越先运行
		thread1.setPriority(5);
		// 获得线程优先级
		System.out.println(thread1.getPriority());
		thread1.start();
		Thread thread2 = new Thread(new TInterrupt());
		thread2.start();
		System.out.println("main线程结束了...");
		// 判断当前线程的运行状态
		System.out.println(thread1.getState());
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
				// 如果被打断线程正在sleep(),wait(),join(),即线程状态是WAITING或TIMED_WAITING,
				// 被打断线程会抛出InterruptedException,并清除打断标记(false),线程继续执行;
				// 如果被打断线程正在运行,则会设置打断标记为true,但线程仍继续运行;
				// 当线程A调用了LockSupport.park()之后,线程A会暂停;在线程B调用A.interrupt()之后,线程A打断标记为true;
				// 此时LockSupoort.park()检测到打断标记为true,将不阻塞线程A;如果后续再次调用LockSupport.park(),不会有任何效果
				Thread.currentThread().interrupt();
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("线程被打断了.............");
			}
			try {
				// System.out.println(Thread.interrupted());
				// 当Thread.currentThread().interrupt();被调用之后,再调用sleep(),wait(),join()就会抛出InterruptedException
				// 如果Thread.currentThread().interrupt();被调用之后再调用Thread.interrupted()就会清除打断状态,此时调用sleep()等不会抛异常
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName());
		}
	}
}