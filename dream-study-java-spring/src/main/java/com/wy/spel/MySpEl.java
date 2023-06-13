package com.wy.spel;

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
 * @serviceName:spring中的组件名
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

}