package com.wy.example;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * 求1到n个数的和
 *
 * @author 飞花梦影
 * @date 2023-05-11 09:57:19
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class MyForkJoin2 {

	public static void main(String[] args) throws ExecutionException, InterruptedException {
		SumTask sum = new SumTask(100);
		ForkJoinPool pool = new ForkJoinPool();
		ForkJoinTask<Long> future = pool.submit(sum);
		Long aLong = future.get();
		System.out.println(aLong);
		pool.shutdown();
	}

	static class SumTask extends RecursiveTask<Long> {

		private static final long serialVersionUID = -6609173749185660612L;

		private static final int THRESHOLD = 10;

		private long start;

		private long end;

		public SumTask(long n) {
			this(1, n);
		}

		public SumTask(long start, long end) {
			this.start = start;
			this.end = end;
		}

		@Override
		protected Long compute() {
			long sum = 0;
			// 如果计算的范围在threshold之内,则直接进行计算
			if ((end - start) <= THRESHOLD) {
				for (long l = start; l <= end; l++) {
					sum += l;
				}
			} else {
				// 否则找出起始和结束的中间值,分割任务
				long mid = (start + end) >>> 1;
				SumTask left = new SumTask(start, mid);
				SumTask right = new SumTask(mid + 1, end);
				left.fork();
				right.fork();
				// 收集子任务计算结果
				sum = left.join() + right.join();
			}
			return sum;
		}
	}
}