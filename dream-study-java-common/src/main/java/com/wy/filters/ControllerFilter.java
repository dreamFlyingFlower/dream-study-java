package com.wy.filters;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;

/**
 * 监听每个http请求接口，打印请求及返回数据的日志
 *
 * @author ParadiseWY
 * @date 2020年9月26日 下午7:48:11
 */
@Aspect
@Component
public class ControllerFilter {

	private Logger log = LoggerFactory.getLogger(getClass());

	// 申明一个切点 里面是 execution表达式
	@Pointcut("execution(public * wy.springboot.crl.*.*(..))")
	private void controllerAspect() {
	}

	// 请求method前打印内容
	@Before(value = "controllerAspect()")
	public void methodBefore(JoinPoint joinPoint) {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
		HttpServletRequest request = requestAttributes.getRequest();
		try {
			// 打印请求内容
			log.info("||===============请求内容 start===============||");
			log.info("请求地址:" + request.getRequestURL().toString());
			log.info("请求方式:" + request.getMethod());
			log.info("请求方法:" + joinPoint.getSignature());
			log.info("请求参数:" + Arrays.toString(joinPoint.getArgs()));
			log.info("||===============请求内容 end=================||");
		} catch (Exception e) {
			log.error("###ControllerLogFilter.class methodBefore() ### ERROR:", e);
		}
	}

	// 在方法执行完结后打印返回内容
	@AfterReturning(returning = "o", pointcut = "controllerAspect()")
	public void methodAfterReturing(Object o) {
		try {
			log.info("||--------------结果集 start----------------||");
			log.info("Response内容:" + JSON.toJSONString(o));
			log.info("||--------------结果集 end------------------||");
		} catch (Exception e) {
			log.error("###ControllerLogFilter.class methodAfterReturing() ### ERROR:", e);
		}
	}
}