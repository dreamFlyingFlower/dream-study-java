package com.wy.study;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Join:当主线程中调用多线程时,使用该方法可让所有线程都执行完成之后再结束主线程.即单个线程运行完后再继续主线程
 * 
 * @author 飞花梦影
 * @date 2019-05-08 22:19:47
 * @git {@link https://github.com/mygodness100}
 */
public class StudyJoin {

	AtomicInteger atom = new AtomicInteger();

	private int i = 0;

	public static void main(String[] args) {
		StudyJoin s = new StudyJoin();
		List<Thread> ts = new ArrayList<>();
		for (int j = 0; j < 10; j++) {
			ts.add(new Thread(new Runnable() {

				@Override
				public void run() {
					s.m();
				}
			}));
		}
		for (Thread t : ts) {
			t.start();
		}
		for (Thread t : ts) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println(s.i);
		System.out.println(s.atom.get());
	}

	/**
	 * 由于多线程原因,i会损失数.当A线程刚+1的时候,停了.换成B执行,还是从+1之前开始加,就会丢失数据.
	 * 要想不丢失数据,要么在方法上加锁,要么在里面加同步代码块,或lock,总之就是编程线程安全的即可
	 * AtomicInteger本身就是原子性,线程安全的,不加synchronized,主线程也必须等多线程执行完才可结束
	 */
	void m() {
		int n = 0;
		for (int m = 0; m < 1000000; m++) {
			i++;
			n++;
			atom.incrementAndGet();
		}
		System.out.println(n);
	}
}