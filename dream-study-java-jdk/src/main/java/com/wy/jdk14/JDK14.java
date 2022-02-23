package com.wy.jdk14;

/**
 * JDK14新特性
 * 
 * <pre>
 * 1.新类型record,相当于pojo的进阶版,不需要写getter/setter
 * 2.安全的堆外内存(DirectBuffer)操作MemorySegment,不需要再使用Unsafe的各种copy/get/offset等
 * 3.jpackage:将Java打包成exe或rpc等可执行文件
 * </pre>
 *
 * @author 飞花梦影
 * @date 2021-06-25 10:36:44
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class JDK14 {

	public static void main(String[] args) {
		Object obj = "fdsftretr";
		// 新模式匹配的instanceOf,直接将匹配的值赋值给新变量.新变量只能在条件为true的代码块中使用
		if (obj instanceof String str) {
			System.out.println(str.length());
		}
		// 可以直接做运算
		if (obj instanceof String str && str.length() > 3) {
			System.out.println(str.length());
		}
		// ||后面不能访问str,不管是true还是false的代码块都不能访问.因为短路的关系,其他条件可能直接返回true或false
		if (obj instanceof String str || str.length() > 3) {
			System.out.println(str.length());
		}
	}
}