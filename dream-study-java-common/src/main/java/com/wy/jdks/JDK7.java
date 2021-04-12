package com.wy.jdks;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class JDK7 {

	/**
	 * 1.7新特性简介
	 * @apiNote 1.switch:可使用string做case后的引用,但不推荐,最好是使用枚举
	 *          2.try-with-resource:当引用了流等其他需要关闭的资源时,不需要手动关闭
	 *          3.若抛出多个异常,可在catch中使用|连接起来,只有最后一个异常使用参数.需要注意的是异常的大小不可颠, 否则编译将不会通过
	 *          4.多个异常类,可使用addSuppressed(Exception)将另外的异常信息加入其中,避免多异常信息现实不全 4.
	 * 
	 */
	public static void fileWith() {
		// 1.7以前的try后面是不能接括号的,但是现在可以,将需要申明的流放入到try后面的括号里
		// 不用再在finally中关闭流,系统会自动会自动关闭,但是写在try后面的括号里的类需要实现 AutoCloseable接口
		try (FileInputStream fis = new FileInputStream(""); FileOutputStream fos = new FileOutputStream("");) {
			// do something
		} catch (IOException | RuntimeException e) {
			e.addSuppressed(new RuntimeException("异常中加异常的堆栈信息"));
		}
	}
}