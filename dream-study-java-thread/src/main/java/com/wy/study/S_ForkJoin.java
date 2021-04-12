package com.wy.study;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * ForkJoinPool:分支合并,类似递归.当运行的线程数量不足以支持运算时,会将当前的任务评分,
 * 若是平分之后仍然不足以支持计算,那么继续平分,直到pool认为足够运算时,开始运算.
 * 当所有线程运算完成之后,再一层一层的将运算后的数据合并,最终汇聚到一个线程中.
 * 它没有容量,默认都是1个线程,根据任务自动分支新的子线程,当子线程任务结束后,自动合并.
 * 
 * @apiNote ForkJoinTask:要想使用ForkJoinPool,该线程里执行的任务必须是ForkJoinTaks类型的子类型,
 *          否则就无法实现分支和合并.通常可以直接继承的有2类:RecursiveAction(无返回值),RecursiveTask(有返回值)
 * @author ParadiseWY
 * @date 2019-05-11 18:02:33
 * @git {@link https://github.com/mygodness100}
 */
public class S_ForkJoin {

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