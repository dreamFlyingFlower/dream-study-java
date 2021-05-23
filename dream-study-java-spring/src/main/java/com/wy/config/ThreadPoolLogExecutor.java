package com.wy.config;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;

import lombok.extern.slf4j.Slf4j;

/**
 * 自定义异步线程池,对线程池中的每个任务进行日志打印或其他操作.可以只重写部分方法
 * 
 * @author 飞花梦影
 * @date 2021-01-08 10:24:53
 * @git {@link https://github.com/mygodness100}
 */
@Configuration
@Slf4j
public class ThreadPoolLogExecutor extends ThreadPoolTaskExecutor {

	private static final long serialVersionUID = 1L;

	private void logThreadStatus(String mark) {
		ThreadPoolExecutor threadPoolExecutor = getThreadPoolExecutor();
		if (null == threadPoolExecutor) {
			return;
		}
		log.info("{}, {},taskCount [{}], completedTaskCount [{}], activeCount [{}], queueSize [{}]",
				this.getThreadNamePrefix(), mark, threadPoolExecutor.getTaskCount(),
				threadPoolExecutor.getCompletedTaskCount(), threadPoolExecutor.getActiveCount(),
				threadPoolExecutor.getQueue().size());
	}

	@Override
	public void execute(Runnable task) {
		logThreadStatus("execute(Runnable)");
		super.execute(task);
	}

	@Override
	public void execute(Runnable task, long startTimeout) {
		logThreadStatus("execute(Runnable,long)");
		super.execute(task, startTimeout);
	}

	@Override
	public Future<?> submit(Runnable task) {
		logThreadStatus("submit(Runnable)");
		return super.submit(task);
	}

	@Override
	public <T> Future<T> submit(Callable<T> task) {
		logThreadStatus("execute(Callable<T>)");
		return super.submit(task);
	}

	@Override
	public ListenableFuture<?> submitListenable(Runnable task) {
		logThreadStatus("submitListenable(Runnable)");
		return super.submitListenable(task);
	}

	@Override
	public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
		logThreadStatus("submitListenable(Callable<T>)");
		return super.submitListenable(task);
	}

	@Override
	protected void cancelRemainingTask(Runnable task) {
		logThreadStatus("cancelRemainingTask(Runnable)");
		super.cancelRemainingTask(task);
	}
}