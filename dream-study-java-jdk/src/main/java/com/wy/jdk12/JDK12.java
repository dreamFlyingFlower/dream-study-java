package com.wy.jdk12;

/**
 * JDK12新特性
 * 
 * <pre>
 * 1.switch-case的case中可以写多个值,不需要像以前一样写多个case,但不能写复杂的逻辑
 * 2.switch-case原来每个case后都要写break,否则会一直运行到default,12可以将冒号改成->,就不用写break
 * 3.switch-case可以将返回值赋值给变量
 * 4.instanceof强转一步完成,以前只能判断,现在可以判断后直接赋值,如obj instanceof String str,str就是赋值
 * 5.JMH基础测试,需要在pom.xml中添加相关代码
 * 6.Shenandoah GC:低停顿时间的GC,需要额外的参数开启
 * 7.String增加新方法:transform(再次处理),indent(添加前置空格对齐)
 * 8.Files.mismatch():返回内容第一次不匹配的字符串索引
 * </pre>
 *
 * @author 飞花梦影
 * @date 2021-06-25 10:24:39
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class JDK12 {

	public static void main(String[] args) {
		int key = 2;
		switch (key) {
		// 匹配1和3
		case 1,3 -> System.out.println(key);
		case 2 -> System.out.println(key);
		default -> System.out.println("default");
		}

		int ret = switch (key) {
		case 1 -> 1;
		case 2 -> 2;
		default -> 0;
		}
	}
}