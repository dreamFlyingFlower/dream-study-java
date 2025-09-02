package com.wy.login;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.common.collect.ImmutableList;

import dream.flying.flower.framework.core.helper.IpHelpers;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 登录限制次数切面
 *
 * @author 飞花梦影
 * @date 2025-09-02 16:44:30
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Slf4j
@Aspect
@Component
@AllArgsConstructor
public class LimitCountAspect {

	private final RedisTemplate<String, Serializable> limitRedisTemplate;

	@Pointcut("@annotation(com.example.loginlimit.annotation.LimitCount)")
	public void pointcut() {
		// do nothing
	}

	@Around("pointcut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		HttpServletRequest request =
				((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
						.getRequest();

		MethodSignature signature = (MethodSignature) point.getSignature();
		Method method = signature.getMethod();
		LimitCount annotation = method.getAnnotation(LimitCount.class);
		// 注解名称
		String name = annotation.name();
		// 注解key
		String key = annotation.key();
		// 访问IP
		String ip = IpHelpers.getIp(request);
		// 过期时间
		int limitPeriod = annotation.period();
		// 过期次数
		int limitCount = annotation.count();

		ImmutableList<String> keys = ImmutableList.of(StringUtils.join(annotation.prefix() + "_", key, ip));
		String luaScript = buildLuaScript();
		RedisScript<Number> redisScript = new DefaultRedisScript<>(luaScript, Number.class);
		Number count = limitRedisTemplate.execute(redisScript, keys, limitCount, limitPeriod);
		log.info("IP:{} 第 {} 次访问key为 {}，描述为 [{}] 的接口", ip, count, keys, name);
		if (count != null && count.intValue() <= limitCount) {
			return point.proceed();
		} else {
			return "接口访问超出频率限制";
		}
	}

	/**
	 * 限流脚本 调用的时候不超过阈值,则直接返回并执行计算器自加
	 *
	 * @return lua脚本
	 */
	private String buildLuaScript() {
		return "local c" + "\nc = redis.call('get',KEYS[1])" + "\nif c and tonumber(c) > tonumber(ARGV[1]) then"
				+ "\nreturn c;" + "\nend" + "\nc = redis.call('incr',KEYS[1])" + "\nif tonumber(c) == 1 then"
				+ "\nredis.call('expire',KEYS[1],ARGV[2])" + "\nend" + "\nreturn c;";
	}
}