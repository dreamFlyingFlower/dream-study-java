package com.wy.jdk7;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * JDK7新特性
 * 
 * <pre>
 * 1.switch:可使用string做case后的引用,但不推荐,最好是使用枚举
 * 2.try-with-resource:当引用了流等其他需要关闭且实现了{@link Closeable}的资源时,程序可自动关闭流
 * 3.若抛出多个异常,可在catch中使用|连接,最后一个异常指定参数.异常的大小不可颠倒,否则编译不通过
 * 4.多个异常类,可使用addSuppressed(Exception)将另外的异常信息加入其中,避免多异常信息现实不全
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2021-10-05 22:00:56
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class JDK7 {

	public static void main(String[] args) {
		// 1.7以前的try后面是不能接括号的,但是现在可以,将需要申明的流放入到try后面的括号里
		// 不用再在finally中关闭流,系统会自动会自动关闭,但是写在try后面的括号里的类需要实现 AutoCloseable接口
		try (FileInputStream fis = new FileInputStream(""); FileOutputStream fos = new FileOutputStream("");) {
			// do something
		} catch (IOException | RuntimeException e) {
			e.addSuppressed(new RuntimeException("异常中加异常的堆栈信息"));
		}
	}
}