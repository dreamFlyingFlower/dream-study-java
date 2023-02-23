package com.wy.retry;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

/**
 * Spring重试
 * 
 * Retryable:重试注解,需要配合AOP一起使用,同时要开启{@link EnableRetry}
 * 
 * <pre>
 * {@link Retryable#value()}:可重试的异常类型,同include,默认为空.如果excludes也为空,则重试所有异常
 * {@link Retryable#include()}:可重试的异常类型,默认为空.如果excludes也为空,则重试所有异常
 * {@link Retryable#exclude()}:无需重试的异常类型,默认为空.如果includes也为空,则重试所有异常
 * {@link Retryable#maxAttempts()}:最大重试次数,包括第一次失败,默认为3次
 * {@link Retryable#backoff()}:重试等待策略
 * {@link Retryable#recover()}:表示重试次数到达最大重试次数后的回调方法,如果不写,则抛出异常
 * ->回调方法的返回值必须与 Retryable 方法一致
 * ->回调方法的第一个参数,必须是 Throwable 类型,建议是与 Retryable 配置的异常一致
 * ->回调方法与重试方法写在同一个实现类里面
 * ->若当前类中只有一个方法标注了 Recover ,则 Retryable 中的recover可不指定
 * {@link Backoff#delay()}:重试之间的等待时间,单位为毫秒
 * {@link Backoff#maxDelay()}:重试之间的最大等待时间,单位为毫秒
 * {@link Backoff#multiplier()}:指定延迟的倍数
 * {@link Backoff#delayExpression()}:重试之间的等待时间表达式
 * {@link Backoff#maxDelayExpression()}:重试之间的最大等待时间表达式
 * {@link Backoff#multiplierExpression()}:指定延迟的倍数表达式
 * {@link Backoff#random()}:随机指定延迟时间
 * </pre>
 *
 * @author 飞花梦影
 * @date 2023-02-23 20:56:04
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
@Component
@EnableRetry
public class SelfRetry {

	@Retryable(recover = "fail", maxAttempts = 5, backoff = @Backoff(delay = 1000, multiplier = 2))
	public void retry() {

	}

	@Recover
	public void fail(Throwable throwable) {

	}
}