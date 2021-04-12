package com.wy.study;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * {@link Executor}:线程池,可以对线程进行重复利用,减少线程创建和销毁的开销
 * 
 * 线程池是进程级的重量级资源,默认生命周期和JVM一致,或者显示的调用shutdown()关闭线程池
 * 
 * 线程池中有一个BlockingQueue作为任务的载体,待执行的任务保存在其中,等待空闲线程从队列中取出并执行任务
 * 
 * 线程池默认的的容量上限是Integer.MAX_VALUE.常见的线程池容量,pc200个,服务器1000到10000
 * 
 * {@link ExecutorService}:继承Executor,提供了有返回值的submit方法,返回类型是Future.
 * Future是submit方法的返回值,代表未来,也就是线程执行结束后的一种结果
 * 
 * {@link Executors}:线程池工具类,可以更简单的创建线程池<br>
 * {@link Executors#newFixedThreadPool}:固定数量线程的线程池<br>
 * {@link Executors#newCachedThreadPool}:容量不限,自动扩容.当线程池中的数量不满足任务执行时,创建新的线程.
 * 当线程池中的线程空闲到一定时间时(默认60秒),自动释放线程.缓存线程多数用于测试高并发时,固定线程池的最佳线程数
 * {@link Executors#newScheduledThreadPool}:执行定时任务的线程,底层还是用DelayedQueue
 * 
 * 线程状态:start->就绪,running->正在运行,shuttingdown->正在关闭,优雅关闭,terminated->结束
 * 
 * {@link ThreadPoolExecutor}:除了ForkJoinPool之外,所有的线程池的底层都是该类,主要构造参数:<br>
 * 
 * @apiNote corePoolSize:核心容量,创建好后就准备就绪的线程数量,一直会存在,除非设置了allowCoreThreadTimeOut
 *          maximumPoolSize:最大线程数,控制资源,同时并发的最大数量<br>
 *          keepAliveTime:存活时间,只要线程空闲大于该时间并且maximumPoolSize>corePoolSize就回收空闲线程 unit:时间单位<br>
 *          BlockingQueue:阻塞队列,所有待执行的任务都放在队列中,等待空闲线程取出任务并执行<br>
 *          handler:若队列满了,按照指定的拒绝策略执行任务<br>
 * 
 * @apiNote 运行流程:<br>
 *          线程池创建,准备好core数量的核心线程,准备接受任务<br>
 *          ->core满了之后将再进来的任务放入阻塞队列中,空闲的core就会自己去阻塞队列获取任务执行
 *          ->阻塞队列满了,就直接开心的线程,最大只能开到maximumPoolSize<br>
 *          ->max满了就用拒绝策略拒绝任务或max都执行完成,有空闲线程,在指定存活时间后,释放max-core这些线程
 * 
 * @author ParadiseWY
 * @date 2019-05-11 00:19:31
 * @git {@link https://github.com/mygodness100}
 */
public class S_Executor {

	static ExecutorService fixPool = Executors.newFixedThreadPool(5);

	static ExecutorService cachaPool = Executors.newCachedThreadPool();

	static ExecutorService scheduledPool = Executors.newScheduledThreadPool(5);

	public static void main(String[] args) {
		for (int i = 0; i < 25; i++) {
			fixPool.execute(new Runnable() {

				@Override
				public void run() {
					System.out.println(Thread.currentThread().getName());
				}
			});
		}
		// 优雅关机,不是强行关闭线程池,回收线程池中的资源.而是不再处理新的任务,而是等所有的任务都处理完后关闭
		// 优雅关机后,无法再使用线程池
		fixPool.shutdown();
		// 是否已经关闭,是否调用了shutdown方法
		System.out.println(fixPool.isShutdown());
		// 是否已经结束.相当于回收了资源.当该方法返回true时,表示多线程任务都已经完成
		System.out.println(fixPool.isTerminated());
	}

}