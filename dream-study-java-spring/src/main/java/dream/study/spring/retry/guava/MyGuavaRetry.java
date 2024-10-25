package dream.study.spring.retry.guava;

import java.util.concurrent.TimeUnit;

import org.springframework.remoting.RemoteAccessException;

import com.github.rholder.retry.Attempt;
import com.github.rholder.retry.RetryListener;
import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.StopStrategy;
import com.github.rholder.retry.WaitStrategies;
import com.github.rholder.retry.WaitStrategy;

import dream.study.spring.retry.MyRetryTask;
import lombok.extern.slf4j.Slf4j;

/**
 * Guava重试
 * 
 * {@link WaitStrategy}:等待策略
 * 
 * <pre>
 * {@link WaitStrategies#fixedWait(long, TimeUnit)}: 固定等待时长策略
 * {@link WaitStrategies#randomWait(long, TimeUnit)}: 随机等待时长策略(可以提供一个最小和最大时长,等待时长为其区间随机值)
 * {@link WaitStrategies#incrementingWait(long, TimeUnit, long, TimeUnit)}: 递增等待时长策略(提供一个初始值和步长,等待时间随重试次数增加而增加)
 * {@link WaitStrategies#exponentialWait(long, TimeUnit)}: 指数等待时长策略
 * {@link WaitStrategies#fibonacciWait(long, TimeUnit)}: Fibonacci等待时长策略
 * {@link WaitStrategies#exceptionWait(Class, com.google.common.base.Function)}: 异常时长等待策略
 * {@link WaitStrategies#join(WaitStrategy...)}: 复合时长等待策略
 * </pre>
 * 
 * {@link StopStrategy}:停止策略
 * 
 * <pre>
 * {@link StopStrategies#stopAfterDelay(long)}: 设定一个最长允许的执行时间,无论任务执行次数,只要重试的时候超出了最长时间,则任务终止,并返回重试异常RetryException
 * {@link StopStrategies#stopAfterAttempt(int)}: 设定最大重试次数,如果超出最大重试次数则停止重试,并返回重试异常
 * {@link StopStrategies#neverStop()}: 不停止,用于需要一直轮训知道返回期望结果的情况
 * </pre>
 *
 * @author 飞花梦影
 * @date 2024-04-25 15:41:42
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Slf4j
public class MyGuavaRetry {

	public void fun01() {
		// RetryerBuilder 构建重试实例 retryer,可以设置重试源且可以支持多个重试源，可以配置重试次数或重试超时时间,以及可以配置等待时间间隔
		Retryer<Boolean> retryer = RetryerBuilder.<Boolean>newBuilder()
				// 设置特定异常重试源,可设置多个
				.retryIfExceptionOfType(RemoteAccessException.class).retryIfExceptionOfType(NullPointerException.class)
				// 抛出 runtime 异常,checked 异常时都会重试,但是抛出 error 不会重试
				.retryIfException()
				// 抛 runtime 异常的时候才重试,checked 异常和error 都不重试
				.retryIfRuntimeException()
				// 设置根据结果重试,只有返回false的方法才会重试
				.retryIfResult(res -> !res)
				// 等同于上一个条件
				// .retryIfResult(Predicates.equalTo(false))
				// 以_error结尾才重试,需要修改Retryer中的泛型
				// .retryIfResult(Predicates.containsPattern("_error$"))
				// 设置等待间隔时间
				.withWaitStrategy(WaitStrategies.fixedWait(3, TimeUnit.SECONDS))
				// 设置最大重试次数之后的停止策略
				.withStopStrategy(StopStrategies.stopAfterAttempt(3))
				// 任务阻塞策略,默认为BlockStrategies.threadSleepStrategy()
				// .withBlockStrategy(BlockStrategies.threadSleepStrategy())
				// 监听.每次重试之后,会自动回调我们注册的监听.可注册多个,依次条用
				.withRetryListener(new RetryListener() {

					@Override
					public <V> void onRetry(Attempt<V> attempt) {
						log.error("第【{}】次调用失败", attempt.getAttemptNumber());
					}
				}).build();
		try {
			retryer.call(() -> MyRetryTask.retryTask("abc"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}