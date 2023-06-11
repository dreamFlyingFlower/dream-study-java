package com.wy.jvm;

/**
 * 测试年轻代中不同启动参数在内存中的空间分配以及GC触发场景
 * 
 * <pre>
 * -verbose:gc -XX:+PrintGCDetails -XX:+UseSerialGC -Xmx20m -Xms20m -Xmn1m
 * </pre>
 *
 * @author 飞花梦影
 * @date 2021-09-14 14:19:37
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class MyNewGC {

	public static void main(String[] args) {
		byte[] b = null;
		for (int i = 0; i < 20; i++) {
			b = new byte[1 * 1024 * 1024];
		}
		System.out.println(b);
	}
}