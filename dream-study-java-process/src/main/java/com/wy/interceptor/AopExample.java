package com.wy.interceptor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * aop编程例子
 * @author wanyang 2018年6月29日
 * 1.在需要进行拦截的类上加上注解,表明该类开启aop拦截
 * 2.在方法上可以开启各种注解,表明是前置拦截还是其他拦截,并且需要标明拦截该方法的类
 * execution表达式中的*代表任意值,根据返回值,包名的个数写*号,有多少个*表示多少层包,方法名中的..指任意参数
 * * com.wy.spring..*.*(..):com.wy.spring包下所有的类以及子类的所有方法
 */
@Aspect
public class AopExample {

	/**
	 * 前置方法:在目标方法调用之前调用
	 * value中的值是指调用那一个方法
	 */
	@Before(value="execution(* com.wy.service.UserService.addUser(..))")
	public void before() {
		
	}
	
	/**
	 * 后置方法:在方法调用完之后调用,若有返回值,可拿到返回值
	 * returning中的值要和参数相同
	 */
	@AfterReturning(value="execution(* com.wy.service.UserService.addUser(..))",returning="result")
	public void behind(Object result) {
		System.out.println(result);
	}
	
	/**
	 * 环绕增强:前置增强,后置增强
	 * @throws Throwable 
	 */
	@Around(value="execution(* com.wy.service.UserService.addUser(..))")
	public void around(ProceedingJoinPoint point) throws Throwable {
		// 拿到后置增强的结果
		Object result = point.proceed();
		System.out.println(result);
	}
	
	/**
	 * 异常仍出
	 */
	@AfterThrowing(value="execution(* com.wy.service.UserService.addUser(..))",throwing="e")
//	@AfterThrowing(value="AopExample.pointCut()")
	public void throwing(Throwable e) {
		System.out.println(e.getMessage());
	}
	
	/**
	 * 最终增强
	 */
	@After(value="execution(* com.wy.service.UserService.addUser(..))")
//	@After(value="AopExample.pointCut()")
	public void after() {
		System.out.println(11);
	}
	
	/**
	 * 切入点注解,相当于再次代理,可以在其他aop注解上直接使用该方法名做代理
	 * 应用于某个方法有多个拦截的时候方便修改
	 * 应用该注解的方法不会被调用,并没有其他作用,只是一个切入点,故而可以直接使用private
	 * 在其他aop注解上使用的时候可以直接用本类类名.方法名作为具体的方法路径名
	 */
	@Pointcut(value="execution(* com.wy.service.UserService.addUser(..))")
	private void pointCut() {};
}