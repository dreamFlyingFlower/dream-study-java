package com.wy.example;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

/**
 * 利用ForkJoinPool快排
 *
 * @author 飞花梦影
 * @date 2023-05-11 09:55:25
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class MyForkJoin1 {

	public static void main(String[] args) throws InterruptedException {
		long[] array = { 5, 3, 7, 9, 2, 4, 1, 8, 10 };
		// 一个任务
		ForkJoinTask<Void> sort = new SortTask(array);
		// 一个pool
		ForkJoinPool pool = new ForkJoinPool();
		// ForkJoinPool开启多个线程,同时执行上面的子任务
		pool.submit(sort);
		// 结束ForkJoinPool
		pool.shutdown();
		// 等待结束Pool
		pool.awaitTermination(10, TimeUnit.SECONDS);
		System.out.println(Arrays.toString(array));
	}

	static class SortTask extends RecursiveAction {

		private static final long serialVersionUID = 8101931027773407003L;

		final long[] array;

		final int lo;

		final int hi;

		public SortTask(long[] array) {
			this.array = array;
			this.lo = 0;
			this.hi = array.length - 1;
		}

		public SortTask(long[] array, int lo, int hi) {
			this.array = array;
			this.lo = lo;
			this.hi = hi;
		}

		private int partition(long[] array, int lo, int hi) {
			long x = array[hi];
			int i = lo - 1;
			for (int j = lo; j < hi; j++) {
				if (array[j] <= x) {
					i++;
					swap(array, i, j);
				}
			}
			swap(array, i + 1, hi);
			return i + 1;
		}

		private void swap(long[] array, int i, int j) {
			if (i != j) {
				long temp = array[i];
				array[i] = array[j];
				array[j] = temp;
			}
		}

		@Override
		protected void compute() {
			if (lo < hi) {
				// 找到分区的元素下标
				int pivot = partition(array, lo, hi);
				// 将数组分为两部分
				SortTask left = new SortTask(array, lo, pivot - 1);
				SortTask right = new SortTask(array, pivot + 1, hi);
				left.fork();
				right.fork();
				left.join();
				right.join();
			}
		}
	}
}