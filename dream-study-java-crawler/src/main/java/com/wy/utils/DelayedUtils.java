package com.wy.utils;

import com.wy.callback.ConcurrentCallBack;
import com.wy.callback.DelayedCallBack;

import lombok.Getter;
import lombok.Setter;

/**
 * 延时调用工具类
 * 
 * @author 飞花梦影
 * @date 2021-01-07 17:31:45
 * @git {@link https://github.com/mygodness100}
 */
public class DelayedUtils {

	public static void delayed(long delayedTime) {
		try {
			Thread.sleep(delayedTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 延时方法
	 *
	 * @param callBack
	 */
	public static <T> T delayed(DelayedCallBack<T> callBack) {
		boolean flag = false;
		long sleepTime = callBack.sleepTime();
		long timeOut = callBack.timeOut();
		long currentTime = System.currentTimeMillis();
		T obj = null;
		while (true) {
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			long duration = System.currentTimeMillis() - currentTime;
			boolean isExist = callBack.isExist();
			obj = callBack.callBack(duration);
			if (isExist) {
				flag = true;
			} else if (duration > timeOut) {
				flag = true;
			}
			if (flag) {
				break;
			}
		}
		return obj;
	}

	/**
	 * 并发过滤,如果有多个并发,只取第一个
	 */
	public synchronized static ConcurrentCallBack getConcurrentFilter(final long time) {
		final ConcurrentEntity concurrentEntity = new ConcurrentEntity();
		concurrentEntity.setTimeInterval(time);
		return new ConcurrentCallBack() {

			@Override
			public boolean filter() {
				boolean flag = false;
				// 数据初始化
				long duration = System.currentTimeMillis() - concurrentEntity.getCurrentTime();
				if (duration > time) {
					concurrentEntity.setAvailable(true);
					concurrentEntity.setCallCount(0);
					concurrentEntity.setCurrentTime(System.currentTimeMillis());
				}
				long callCount = concurrentEntity.getCallCount();
				concurrentEntity.setCallCount(++callCount);
				if (callCount <= 1 && concurrentEntity.isAvailable()) {
					flag = true;
					concurrentEntity.setAvailable(false);
				}
				return flag;
			}
		};
	}

	@Getter
	@Setter
	static class ConcurrentEntity {

		/**
		 * 当前时间
		 */
		private long currentTime = System.currentTimeMillis();

		/**
		 * 时间区间
		 */
		private long timeInterval = 10000;

		/**
		 * 是否可用
		 */
		private boolean available = true;

		/**
		 * 调用次数
		 */
		private long callCount = 0;
	}
}