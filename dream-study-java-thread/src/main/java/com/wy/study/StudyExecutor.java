package com.wy.study;

import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.ThreadPoolExecutor.DiscardOldestPolicy;
import java.util.concurrent.ThreadPoolExecutor.DiscardPolicy;
import java.util.concurrent.TimeUnit;

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
 * 线程状态:start()->就绪,running->正在运行,shuttingdown->正在关闭,优雅关闭,terminated->结束
 * 
 * {@link ThreadPoolExecutor}:除了ForkJoinPool之外,所有的线程池的底层都是该类,主要参数及方法:
 * 
 * <pre>
 * {@link ThreadPoolExecutor#corePoolSize}:核心容量,即使其他核心线程处于空闲状态,仍然会创建新的线程,直到达到核心线程数.
 * 		如果调用了prestartAllCoreThreads(),线程池会提前创建好并启动所有核心线程.
 * 		核心线程一直会存在,除非设置了allowCoreThreadTimeOut()
 * {@link ThreadPoolExecutor#maximumPoolSize}:最大线程数,若队列满了,将会继续创建线程,直到最大线程数.若设置了无限队列,则该参数无效
 * {@link ThreadPoolExecutor#keepAliveTime}:存活时间,只要线程空闲大于该时间并且maximumPoolSize>corePoolSize就回收空闲线程
 * {@link ThreadPoolExecutor#handler}:若队列满了和线程池都满了,按照指定的拒绝/饱和策略执行任务,默认策略是直接抛出异常
 * {@link ThreadPoolExecutor#threadFactory}:设置创建线程的工厂,可以设置每个创建出来的线程的名字,debug和定位问题时更容易
 * {@link ThreadPoolExecutor#workQueue}:阻塞队列,所有待执行的任务都放在队列中,等待空闲线程取出任务并执行,由以下几种队列可选
 * ->{@link ArrayBlockingQueue}:有界阻塞数组队列,按FIFO(先进先出)排序元素
 * ->{@link LinkedBlockingQueue}:有界阻塞链表队列,按FIFO(先进先出)排序元素,性能通常高于ArrayBlockingQueue,
 * 		Executors.newFixedThreadPool()使用该队列
 * ->{@link SynchronousQueue}:不存储元素的阻塞队列,每个元素插入必须等另一个线程调用移除,否则插入一直阻塞,
 * 		性能通常高于LinkedBlockingQueue,Executors.newCachedThreadPool使用该队列
 * ->{@link PriorityBlockingQueue}:具有优先级的无限阻塞队列
 * {@link ThreadPoolExecutor#ctl}:线程池的控制状态,用来表示线程池的运行状态(整型的高3位)和运行的worker数量(低29位)
 * {@link ThreadPoolExecutor#COUNT_BITS}:29位的偏移量
 * {@link ThreadPoolExecutor#CAPACITY}:最大线程数,2^29 - 1
 * {@link ThreadPoolExecutor#RUNNING}:线程运行状态,接受新任务并且处理已经进入阻塞队列的任务
 * {@link ThreadPoolExecutor#SHUTDOWN}:不接受新任务,但是处理已经进入阻塞队列的任务
 * {@link ThreadPoolExecutor#STOP}:不接受新任务,不处理已经进入阻塞队列的任务并且中断正在运行的任务
 * {@link ThreadPoolExecutor#TIDYING}:所有的任务都已经终止,workerCount为0,线程转化为TIDYING状态并且调用terminated钩子函数
 * {@link ThreadPoolExecutor#TERMINATED}:terminated钩子函数已经运行完成
 * {@link ThreadPoolExecutor#mainLock}:可重入锁
 * {@link ThreadPoolExecutor#workers}:存放工作线程集合,继承了AQS,所以Worker本身就是一把锁
 * {@link ThreadPoolExecutor#termination}:终止条件
 * {@link ThreadPoolExecutor#largestPoolSize}:最大线程池容量,可重入锁中才有效
 * {@link ThreadPoolExecutor#completedTaskCount}:已完成任务数量
 * 
 * {@link ThreadPoolExecutor#allowCoreThreadTimeOut}:是否运行核心线程超时
 * {@link ThreadPoolExecutor#runStateOf}:获取线程池状态,通过按位与操作,低29位将全部变成0
 * {@link ThreadPoolExecutor#workerCountOf}:获取线程池worker数量,通过按位与操作,高3位将全部变成0
 * {@link ThreadPoolExecutor#ctlOf}:根据线程池状态和线程池worker数量,生成ctl值
 * {@link ThreadPoolExecutor#execute(Runnable)}:执行任务
 * {@link ThreadPoolExecutor#submit(Callable)}:提交任务 task,用返回值 Future 获得任务执行结果
 * {@link ThreadPoolExecutor#invokeAll(Collection)}:提交 tasks 中所有任务
 * {@link ThreadPoolExecutor#invokeAll(Collection, long, TimeUnit)}:提交 tasks 中所有任务,带超时时间
 * {@link ThreadPoolExecutor#invokeAny(Collection)}:提交 tasks 中所有任务,哪个任务先成功执行完毕,返回此任务执行结果,其它任务取消
 * {@link ThreadPoolExecutor#invokeAny(Collection, long, TimeUnit)}:提交 tasks 中所有任务,带超时时间
 * {@link ThreadPoolExecutor#isShutdown()}:不在 RUNNING 状态的线程池,此方法就返回 true
 * {@link ThreadPoolExecutor#isTerminated()}:线程池状态是否是 TERMINATED
 * {@link ThreadPoolExecutor#awaitTermination()}:调用shutdown后,由于调用线程并不会等待所有任务运行结束,
 * 		因此如果想在线程池 TERMINATED 后做些操作,可以利用此方法等待
 * {@link ThreadPoolExecutor#terminated()}:钩子方法,用户可自定义线程池时实现.线程池关闭之前调用
 * {@link ThreadPoolExecutor#beforeExecute()}:钩子方法,用户可自定义线程池时实现.线程执行之前调用
 * {@link ThreadPoolExecutor#afterExecute()}:钩子方法,用户可自定义线程池时实现.线程执行之后调用
 * </pre>
 * 
 * 线程池运行中线程使用数量变化:
 * 
 * <pre>
 * 1.线程池创建,准备好core数量的核心线程,准备接受任务
 * 2.core满了之后将再进来的任务放入阻塞队列中,空闲的core就会自己去阻塞队列获取任务执行
 * 3.阻塞队列满了,就直接开新的线程,最大只能开到maximumPoolSize
 * 4.max满了就用拒绝策略拒绝任务或max都执行完成,有空闲线程,在指定存活时间后,释放max-core这些线程
 * </pre>
 * 
 * 拒绝/饱和策略,当队列和线程池都满了,新任务的执行策略,默认时AbortPolicy策略,可自定义
 * 
 * <pre>
 * {@link AbortPolicy}:直接抛出异常,默认策略
 * {@link CallerRunsPolicy}:不在新线程中执行任务,而是由调用者所在的线程来执行
 * {@link DiscardPolicy}:拒绝新的任务,也不处理,等同于丢失新的任务
 * {@link DiscardOldestPolicy}:丢失最长时间没有执行的任务,同时尝试执行处理新的任务,如果线程池未关闭
 * {@link RejectedExecutionHandler}:实现该接口,实现自定义的拒绝策略
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2019-05-11 00:19:31
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class StudyExecutor {

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
		// 系统自带的线程池有各自的缺陷,最好自定义一个线程池
		new ThreadPoolExecutor(2, 5, 2L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(200));
		new ThreadPoolExecutor(2, 5, 2L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(200),
				Executors.defaultThreadFactory());
		new ThreadPoolExecutor(2, 5, 2L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(200),
				Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
	}
}