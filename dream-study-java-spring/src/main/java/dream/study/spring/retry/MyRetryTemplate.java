package dream.study.spring.retry;

import java.util.HashMap;
import java.util.Map;

import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import lombok.extern.slf4j.Slf4j;

/**
 * 使用{@link RetryTemplate}进行重试,效果和使用注解相同,但更灵活
 * 
 * 重试策略:
 * 
 * <pre>
 * NeverRetryPolicy: 只允许调用RetryCallback一次,不允许重试
 * AlwaysRetryPolicy: 允许无限重试,直到成功,此方式逻辑不当会导致死循环
 * SimpleRetryPolicy: 固定次数重试策略,默认重试最大次数为3次,RetryTemplate默认使用的策略
 * TimeoutRetryPolicy: 超时时间重试策略,默认超时时间为1秒,在指定的超时时间内允许重试
 * ExceptionClassifierRetryPolicy: 设置不同异常的重试策略,类似组合重试策略,区别在于这里只区分不同异常的重试
 * CircuitBreakerRetryPolicy: 有熔断功能的重试策略,需设置3个参数openTimeout、resetTimeout和delegate
 * CompositeRetryPolicy: 组合重试策略.有两种组合方式:
 * 		乐观组合重试策略:指只要有一个策略允许即可以重试;
 * 		悲观组合重试策略:指只要有一个策略不允许即可以重试,但不管哪种组合方式,组合中的每一个策略都会执行
 * </pre>
 * 
 * 重试回退策略:每次重试是立即重试还是等待一段时间后重试.默认是立即重试,如果需要配置等待一段时间后重试则需要指定回退策略BackoffRetryPolicy
 * 
 * <pre>
 * NoBackOffPolicy: 无退避算法策略,每次重试时立即重试
 * FixedBackOffPolicy: 固定时间的退避策略,需设置参数sleeper和backOffPeriod,sleeper指定等待策略.
 * 		默认是Thread.sleep,即线程休眠,backOffPeriod指定休眠时间,默认1秒
 * UniformRandomBackOffPolicy: 随机时间退避策略,需设置sleeper,minBackOffPeriod和maxBackOffPeriod.
 * 		该策略在minBackOffPeriod,maxBackOffPeriod之间取一个随机休眠时间,minBackOffPeriod默认500毫秒,maxBackOffPeriod默认1500毫秒
 * ExponentialBackOffPolicy: 指数退避策略,需设置参数sleeper,initialInterval,maxInterval和multiplier.
 * 		nitialInterval指定初始休眠时间,默认100毫秒,maxInterval指定最大休眠时间,默认30秒,multiplier指定乘数,即下一次休眠时间为当前休眠时间*multiplier
 * ExponentialRandomBackOffPolicy: 随机指数退避策略,引入随机乘数可以实现随机乘数回退
 * </pre>
 *
 * @author 飞花梦影
 * @date 2023-02-23 21:15:51
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
@Slf4j
public class MyRetryTemplate {

	/** 重试间隔时间ms,默认1000ms */
	private long fixedPeriodTime = 1000L;

	/** 最大重试次数,默认为3 */
	private int maxRetryTimes = 3;

	/** 表示哪些异常需要重试,key表示异常的字节码,value为true表示需要重试 */
	private Map<Class<? extends Throwable>, Boolean> exceptionMap = new HashMap<>();

	public void test() {
		// 指定重试异常
		exceptionMap.put(NullPointerException.class, true);
		// 构建重试模板实例
		RetryTemplate retryTemplate = new RetryTemplate();
		// 设置重试回退操作策略,主要设置重试间隔时间
		FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
		backOffPolicy.setBackOffPeriod(fixedPeriodTime);
		// 设置重试策略,主要设置重试次数
		SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(maxRetryTimes, exceptionMap);
		retryTemplate.setRetryPolicy(retryPolicy);
		retryTemplate.setBackOffPolicy(backOffPolicy);
		Boolean execute = retryTemplate.execute(
				// RetryCallback:正常的重试逻辑
				retryContext -> {
					boolean b = MyRetryTask.retryTask("abc");
					log.info("调用的结果:{}", b);
					return b;
				},
				// RecoveryCallback:重试次数消耗完之后的策略
				retryContext -> {
					System.out.println("已达到最大重试次数或抛出了不重试的异常~~~");
					return false;
				});
		System.out.println(execute);
	}
}