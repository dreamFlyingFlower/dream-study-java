package com.wy.jdk11;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * JDK11新特性
 * 
 * <pre>
 * 1.Lambda中支持var关键字
 * 2.可直接在控制台使用java命令运行.java文件,不需要先编译,或者说现在已经自动先编译了
 * 3.字符串添加了一些新方法,如isBlank(),strip()等
 * 4.JDK11正式使用HttpClient,但是包移到了java.net.http中
 * 5.新的垃圾收集器Epsilon:一个处理内存分配但不实现任何实际内存回收机制的GC,一旦堆内存用完,JVM就会退出.试用
 * 6.新的垃圾收集器ZGC:一个可伸缩,低延迟的垃圾回收器,只支持64位系统,试用.
 * 		STW不会超过10ms,既能处理几百兆的小堆,也能处理几个T的堆(OMG),和G1相比,应用吞吐能力不会下降超过15%
 * 7.基于嵌套的访问控制:如果一个类中嵌套了多个类,各类中可以直接访问彼此的私有成员,为此提供了新的访问机制:Nest
 * 8.更方便的IO
 * </pre>
 *
 * @author 飞花梦影
 * @date 2021-06-25 10:21:53
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class JDK11 {

	public static void main(String[] args) throws IOException {
		String str = "\t    \n   ";
		System.out.println(str.isBlank());
		// strip()去除空格以及制表符等,同时会去除中文格式的空格等,但是trim()只能去除英文格式的空格
		System.out.println(str.strip());
		// 去除头部空格
		System.out.println(str.stripLeading());
		// 去除尾部空格
		System.out.println(str.stripTrailing());
		// 字符串重复
		System.out.println(str.repeat(3));
		// 行统计
		System.out.println(str.lines().count());
		// Files类更方便的读写数据方法
		Files.writeString(Path.of("path.txt"), "this is a test");
		// 读数据
		Files.readString(Path.of("path.txt"), StandardCharsets.UTF_8);
	}
}