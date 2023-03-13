package com.wy.jdk14.example;

/**
 * 
 * 
 * @author 飞花梦影
 * @date 2022-02-23 23:59:05
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class Record {

	public static void main(String[] args) {
		com.wy.jdk14.example.User user = new User("lili", 10);
		// 输出username的值,但是无法修改
		System.out.println(user.username());
	}
}

/**
 * 利用关键字record定义一个近似实体类,没有无参构造,没有get,set方法
 * 
 * 可以定义无参的构造形式,可以定义普通方法和静态方法
 * 
 * @param username 构造参数
 * @param age 构造参数
 */
public record User(String username, int age) {

	// 不能定义成员变量,但是可以定义静态变量
	// private String passwrod;
}