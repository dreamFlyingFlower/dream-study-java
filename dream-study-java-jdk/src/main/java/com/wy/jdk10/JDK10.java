package com.wy.jdk10;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.wy.model.User;

/**
 * JDK10新特性
 * 
 * <pre>
 * 1.var:类型推断,JS中不建议使用甚至要舍弃的.只会类型推断,不会变量提升.Lambda中不支持.只针对局部变量,不能赋null
 * 2.List,Set,Map中新增了copyof(),该方法返回目标参数的复制体,但是结果不可变.类似于of()
 * 3.部分流方法添加了字符集的重载方法
 * 4.InputStream,Reader的transferTo(),直接将读取的流写入到输出流OutputStream,Writer中
 * </pre>
 *
 * @author 飞花梦影
 * @date 2021-06-25 10:18:21
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class JDK10 {

	public static void main(String[] args) {
		var ttt = 1;
		var str = "3244";
		// 不能在不同类型之间进行转换
		// ttt = str;
		var user = new User();
		user.setUsername("admin");
		System.out.println(ttt);
		System.out.println(str);
		System.out.println(user);
	}

	public static void test01() {
		List<String> list = new ArrayList<>();
		list.add("fdsfds");
		list.add("fdgfdg");
		List<String> copyOf = List.copyOf(list);
		// 抛异常
		copyOf.add("fdsfdsddgfd");
	}

	public static void test02() throws FileNotFoundException {
		BufferedReader bufferedReader =
				new BufferedReader(new InputStreamReader(new FileInputStream("/app/test/test.txt")));
		PrintWriter printWriter = new PrintWriter("/app/test/test01.txt");
		try (bufferedReader; printWriter) {
			bufferedReader.transferTo(printWriter);
			printWriter.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}