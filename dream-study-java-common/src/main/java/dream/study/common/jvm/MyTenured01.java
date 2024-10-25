package dream.study.common.jvm;

/**
 * -XX:PretenureSizeThreshold:指定占用内存多少的对象直接进入老年代.由系统计算得出,无默认值
 * 
 * 设置JVM参数:<br>
 * -verbose:gc -XX:+PrintGCDetails -XX:+UseSerialGC -Xms20M -Xmn10M -XX:SurvivorRatio=8 -XX:PretenureSizeThreshold=6M
 * 
 * @author 飞花梦影
 * @date 2021-09-10 23:19:01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class MyTenured01 {

	public static final int UNIT_M = 1024 * 1024;

	public static void main(String[] args) {
		// 当该对象为7M时,直接进入老年代.若不设置-XX:PretenureSizeThreshold,将进入eden区
		byte[] b = new byte[7 * UNIT_M];
		System.out.println(b);
	}
}