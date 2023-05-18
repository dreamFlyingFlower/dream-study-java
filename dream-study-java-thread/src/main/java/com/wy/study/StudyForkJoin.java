package com.wy.study;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

/**
 * {@link ForkJoinPool}:分支合并,类似递归.当运行的线程数量不足以支持运算时,会将当前的任务平分,
 * 若是平分之后仍然不足以支持计算,那么继续平分,直到pool认为足够运算时,开始运算.
 * 当所有线程运算完成之后,再一层一层的将运算后的数据合并,最终汇聚到一个线程中.
 * 它没有容量,默认都是1个线程,根据任务自动分支新的子线程,当子线程任务结束后,自动合并.
 * 
 * 与ThreadPoolExector不同的是,除一个全局的任务队列之外,每个线程还有一个自己的局部队列
 * 
 * <pre>
 * {@link ForkJoinPool#ctl}:状态变量,类似于ThreadPoolExecutor中的ctl变量
 * {@link ForkJoinPool#workQueues}:工作线程队列
 * {@link ForkJoinPool#factory}:工作线程工厂
 * {@link ForkJoinPool#indexSeed}:下一个worker的下标
 * {@link ForkJoinPool.WorkQueue#id}:在ForkJoinPool的workQueues数组中的下标
 * {@link ForkJoinPool.WorkQueue#base}:队列尾部指针
 * {@link ForkJoinPool.WorkQueue#top}:队列头指针
 * {@link ForkJoinPool.WorkQueue#array}:工作线程的局部队列
 * {@link ForkJoinPool.WorkQueue#owner}:该工作队列的所有者线程,null表示共享
 * {@link ForkJoinPool#externalSubmit}:将一个可能是外部任务的子任务入队列.
 * ->如果当前线程是ForkJoinWorkerThread类型的,并且该线程的pool就是当前对象;并且当前pool的workQueue不是null,则将当前任务入队列
 * ->否则该任务不是当前线程的子任务,调用外部入队方法,入全局队列
 * {@link ForkJoinTask#fork()}:把自己放入当前线程所在的局部队列中,如果是外部线程调用fork(),则直接将任务添加到共享队列中
 * 
 * </pre>
 * 
 * {@link ForkJoinTask}:ForkJoinPool里的线程必须是{@link ForkJoinTask},否则无法实现分支和合并
 * 
 * <pre>
 * {@link ForkJoinTask#fork()},{@link ForkJoinTask#join()}:核心方法,分支,合并
 * {@link RecursiveAction}:继承自ForkJoinTask,无返回值
 * {@link RecursiveTask}:继承自ForkJoinTask,有返回值
 * {@link ForkJoinTask#status}:任务状态,初始为0.如果status >= 0表示任务未完成;status<0任务已完成
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2019-05-11 18:02:33
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class StudyForkJoin {

	static ForkJoinPool pool = new ForkJoinPool();

	private static int fork = 50000;

	private static int[] numbers = new int[5000000];

	public static void main(String[] args) {
		for (int i = 0; i < numbers.length; i++) {
			numbers[i] = new Random().nextInt(1000);
		}
		long result = 0l;
		System.out.println(System.currentTimeMillis());
		for (int i = 0; i < numbers.length; i++) {
			result += numbers[i];
		}
		System.out.println(System.currentTimeMillis());
		System.out.println(result);
		TestTask tt = new TestTask(0, numbers.length, fork, numbers);
		ForkJoinTask<Long> joinTask = pool.submit(tt);
		try {
			System.out.println(joinTask.get());
			System.out.println(System.currentTimeMillis());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
}

// class TestTask extends RecursiveAction{
class TestTask extends RecursiveTask<Long> {

	private static final long serialVersionUID = 1L;

	private int begin;

	private int end;

	private int fork;

	private int[] numbers;

	public TestTask(int begin, int end, int fork, int[] numbers) {
		this.begin = begin;
		this.end = end;
		this.fork = fork;
		this.numbers = numbers;
	}

	/**
	 * 该方法就是任务逻辑执行方法
	 */
	@Override
	protected Long compute() {
		if (end - begin > fork) {
			long sum = 0l;
			for (int i = 0; i < end; i++) {
				sum += numbers[i];
			}
			return sum;
		} else {
			int middle = begin + (end - begin) / 2;
			TestTask t1 = new TestTask(begin, middle, fork, numbers);
			TestTask t2 = new TestTask(middle, end, fork, numbers);
			// 用于开启新的任务,就是分支工作,开启一个新的线程任务
			t1.fork();
			t2.fork();
			// 合并,将任务的结果获取,这是一个阻塞方法,一定会得到结果数据
			return t1.join() + t2.join();
		}
	}
}