package com.wy.aop;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

import dream.study.common.model.User;

/**
 * 不使用自动注入,手动注入
 *
 * @author 飞花梦影
 * @date 2024-05-09 11:06:08
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class MyCustomizeAspect {

	public void test() {
		// 定义目标对象
		User target = new User();
		// 创建pointcut,用来拦截UserService中的work方法
		Pointcut pointcut = new Pointcut() {

			@Override
			public ClassFilter getClassFilter() {
				// 判断是否是User类型的
				return clazz -> User.class.isAssignableFrom(clazz);
			}

			@Override
			public MethodMatcher getMethodMatcher() {
				return new MethodMatcher() {

					// 判断方法名称是否匹配
					@Override
					public boolean matches(Method method, Class<?> targetClass) {
						return "setUsername".equals(method.getName());
					}

					// 是否需要在runtime期间匹配,即动态匹配.根据情况返回true或false.默认false
					@Override
					public boolean isRuntime() {
						return false;
					}

					// 主要判断参数是否匹配,必要时isRuntime()需要返回true
					@Override
					public boolean matches(Method method, Class<?> targetClass, Object... args) {
						return false;
					}
				};
			}
		};
		// 创建通知,需要在方法之前执行操作,要用到MethodBeforeAdvice类型的通知
		MethodBeforeAdvice methodBeforeAdvice = (method, args, target1) -> System.out.println("你好:" + args[0]);

		// 创建通知,需要拦截方法的执行,所以需要用到MethodInterceptor类型的通知
		new MethodInterceptor() {

			@Override
			public Object invoke(MethodInvocation invocation) throws Throwable {
				System.out.println("准备调用:" + invocation.getMethod());
				long starTime = System.nanoTime();
				// 方法执行
				Object result = invocation.proceed();
				long endTime = System.nanoTime();
				System.out.println(invocation.getMethod() + "，调用结束！");
				System.out.println("耗时(纳秒):" + (endTime - starTime));
				// 返回结果
				return result;
			}
		};

		// 创建Advisor,将pointcut和advice组装起来
		DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, methodBeforeAdvice);
		// 通过spring提供的代理创建工厂来创建代理
		ProxyFactory proxyFactory = new ProxyFactory();
		// 为工厂指定目标对象
		proxyFactory.setTarget(target);
		// 调用addAdvisor方法,为目标添加增强的功能,即添加Advisor,可以为目标添加很多个Advisor
		proxyFactory.addAdvisor(advisor);
		// 通过工厂提供的方法来生成代理对象
		User userServiceProxy = (User) proxyFactory.getProxy();
		// 调用代理的方法
		userServiceProxy.setUsername("路人");
	}
}