package com.wy.spel;

import javax.cache.annotation.CachePut;

import org.springframework.beans.factory.annotation.Value;

/**
 * SpringEL表达式
 * 
 * <pre>
 * #root.methodName:要调用的方法名称
 * #root.method.name:正在调用的方法
 * #root.target:正在调用的目标对象
 * #root.targetClass:正在调用的目标class
 * #root.args[0]:调用目标参数
 * #root.caches[0].name:正在调用的方法使用的缓存列表
 * #参数名:直接引用方法参数名,也可以使用#p0,#p1...
 * #{@serviceName}/#{serviceName}:spring中的组件名,调用spring组件.@可加可不加
 * </pre>
 * 
 * SpringEL运算符
 * 
 * <pre>
 * <,>,<=,>=,==,!=,lt,gt,le,ge,eq,ne
 * +,-,*,/,%,^
 * &&,||,!,and,or,not,between,instanceof
 * ?:
 * matches
 * ?.,?[...],![...],^[...],$[...]
 * </pre>
 *
 * @author 飞花梦影
 * @date 2023-06-13 22:31:08
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class MySpEl {

	/** 直接赋值基本类型或字符串 */
	@Value("#{'fdsfds'}")
	private String code;

	/** 调用类中的属性赋值,该属性必须是public或含有get方法,否则失败 */
	@Value("#{sysLogService.age}")
	private int age;

	@Value("#{@sysLogService.age}")
	private int age1;

	/** 方法调用,必须是public */
	@Value("#{sysLogService.test()}")
	private int age2;

	/** 方法调用并传参,必须是public */
	@Value("#{sysLogService.test(1)}")
	private int age3;

	/** 直接引用类的属性,需要在类的全限定名外面使用 T () 包围 */
	@Value("#{T(java.lang.Integer).MAX_VALUE}")
	private Integer priority;

	@CachePut(cacheName = "#name")
	public void test(String name) {

	}
}