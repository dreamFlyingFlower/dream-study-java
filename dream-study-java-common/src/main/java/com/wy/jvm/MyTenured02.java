package com.wy.jvm;

/**
 * -XX:MaxTenuringThreshold:默认15,指经多少次垃圾回收,对象实例从新生代进入老年代
 * 
 * 设置JVM参数:<br>
 * -verbose:gc -XX:+PrintGCDetails -XX:+UseSerialGC -Xms20M -Xmn10M
 * -XX:SurvivorRatio=8 -XX:MaxTenuringThreshold=13
 * 
 * @author 飞花梦影
 * @date 2021-09-10 23:19:01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class MyTenured02 {

	public static final int UNIT_M = 1024 * 1024;

	public static void main(String[] args) {
		byte[] b = new byte[1 * UNIT_M];
		// 当循环次数i<1次时,b仍然在eden区域
		for (int i = 0; i < 1; i++) {
			// 当循环次数i<10时,即使GC的次数没有达到13次,仍然会从新生代进入老年代
			// for (int i = 0; i < 10; i++) {
			byte[] bc = new byte[2 * UNIT_M];
			System.out.println(bc);
		}
		System.out.println(b);
	}
}