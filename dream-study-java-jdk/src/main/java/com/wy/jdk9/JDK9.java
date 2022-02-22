package com.wy.jdk9;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

/**
 * JDK9新特性
 * 
 * <pre>
 * 1.接口中可定义静态/非静态私有方法,默认方法可调用静态/非静态私有方法,静态方法可以调用静态私有方法
 * 2.自动推断类型运算符<>在JDK9之前不支持匿名内部类,在JDK9中也对匿名内部类做了支持
 * 3.try-with-resources可自动关闭资源,但必须写在try中,JDK9可以引用try代码块之外的变量自动关闭
 * 4.便利的集合特性:of(),创建只读的List,Set,Map
 * 5.增强的Stream API:添加了新的Stream方法takeWhile,dropWhile,ofNullable.重写了iterate
 * 6.String存储结构变更:JDK9之前的字符串底层是存的字符数组,9以后是用字节数组.
 * 		9以前字符编码是变长存储字符串;9以后增加了一个编码标识,同时所有的字符都改为2位存储
 * 		StringBuffer和StringBuilder底层也跟着String改变,存储的是字节数组
 * 7.模块化系统:类似于JS中的export和requires
 * 		在项目中添加module-info.java,用exports关键字指定那些包可以被访问;requires则指定访问那些项目
 * 8.全新的HTTP客户端API
 * 9.统一的JVM日志系统
 * 10.智能Java编译工具(sjavac),Java动态编译器(AOT,相当于JIT的升级版)
 * 11.JS引擎升级:Nashorn
 * 12.JavaDoc的HTML5支持
 * 13.多分辨率图像API
 * 14.添加JShell命令,多版本兼容JAR包,需要从安装目录的bin中打开jshell,类似于JS.进入jshell之后使用/edit命令可打开文本编辑器
 * 15.下划线使用限制:JDK9不能使用单独的下划线定义变量
 * </pre>
 *
 * @author 飞花梦影
 * @date 2021-06-25 10:01:46
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class JDK9 {

	public static void main(String[] args) {
		test04();
		test05();
	}

	/**
	 * 泛型的改进
	 */
	public static void test01() {
		// JDK8以前泛型不能带大括号,编译报错
		Set<String> set = new HashSet<>();
		// 编译报错
		// new HashSet<>() {};
		System.out.println(set);
		// JDK9以前泛型可以带大括号
		new HashSet<String>() {

			private static final long serialVersionUID = 1L;
		};
		Set<String> set1 = new HashSet<>() {

			private static final long serialVersionUID = 1L;

			{
				add("test01");
			}
		};
		System.out.println(set1);
	}

	/**
	 * 流的改进
	 */
	public static void test02() throws Exception {
		// JDK8以前要想使用自动关闭流,需要将定义语句写在try中
		try (FileInputStream fis = new FileInputStream("/app/test.txt");) {

		}
		// JDK9可以在try的括号里直接引用变量
		FileOutputStream fos = new FileOutputStream("/app/test01.txt");
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
		try (fos; writer) {
			// 默认是final的,不可重新赋值
			// fos = new FileOutputStream("/app/test02.txt");
		}
	}

	public static void test03() {
		// JDK8以前创建不可变的List,Set,Map,通常是通过Collections.unmodifiXXX方法
		List<String> list = new ArrayList<>();
		list.add("test");
		list.add("test0-1");
		Collections.unmodifiableList(list);
		// JDK9可以直接使用of()创建不可变数组等
		List.of("test01", "test02", "test03", "test");
		Map.of("k1", "v1");
		Map.ofEntries(Map.entry("k2", "v2"), Map.entry("k3", "v3"));
	}

	/**
	 * Stream的takeWhile():类似filter,但不同的是,只要遇到一个不符合条件的实例,就直接退出循环,不会继续执行
	 */
	public static void test04() {
		List<Integer> list = Arrays.asList(45, 56, 33, 77, 44, 98, 76, 78, 33);
		Stream<Integer> stream = list.stream();
		// 只会输出45,56,33,后面的44,33都不会输出
		stream.takeWhile(x -> x < 70).forEach(System.out::println);
		List<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
		list1.stream().takeWhile(x -> x < 5).forEach(System.out::println);
	}

	/**
	 * Stream的dropWhile():功能正好和takeWhile相反,只要遇到第一个不符合条件的实例,后面的实例都会当作结果输出
	 */
	public static void test05() {
		List<Integer> list = Arrays.asList(45, 56, 33, 77, 44, 98, 76, 78, 33);
		Stream<Integer> stream = list.stream();
		stream.dropWhile(x -> x < 70).forEach(System.out::println);
		System.out.println();
		List<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
		list1.stream().dropWhile(x -> x < 5).forEach(System.out::println);
	}

	/**
	 * Stream的ofNullable()
	 */
	public static void test06() {
		Stream<Integer> stream1 = Stream.of(1, 2, 3, null);
		stream1.forEach(System.out::println);
		System.out.println();
		// JDK8之前如果Stream中只有单个元素,此元素不能为null.否则报NullPointerException
		// Stream<Object> stream2 = Stream.of(null);
		// JDK9新增ofNullable(T t),单元素可以为null,且长度会计算为0
		Stream<String> stream3 = Stream.ofNullable("Tom");
		System.out.println(stream3.count());// 1
		Stream<String> stream4 = Stream.ofNullable(null);
		System.out.println(stream4.count());// 0
	}

	/**
	 * iterator()重载的方法
	 * 
	 * Stream的实例化:通过集合的stream();通过数组工具类Arrays;Stream.of();iterator(),generate()
	 */
	public static void test07() {
		// 相当于无限自增,到达10停止
		Stream.iterate(0, x -> x + 1).limit(10).forEach(System.out::println);
		System.out.println();
		// 功能和上面的相同,只不过下面的语句直接将条件写在了迭代中
		Stream.iterate(0, x -> x < 10, x -> x + 1).forEach(System.out::println);
	}

	/**
	 * Optinal中提供Stream方法,需要使用flatMap得到里面的值
	 */
	public static void test08() {
		List<String> list = new ArrayList<>();
		list.add("Tom");
		list.add("Jerry");
		list.add("Tim");
		Optional<List<String>> optional = Optional.ofNullable(list);
		Stream<String> stream = optional.stream().flatMap(x -> x.stream());
		// System.out.println(stream.count());
		stream.forEach(System.out::println);
	}

	/**
	 * httpclient:替代{@link HttpURLConnection},支持http1,http2,,websocket
	 * 
	 * HTTP/1.1依赖于请求/响应周期,HTTP/2允许服务器push数据,它可以发送比客户端请求更多的数据
	 * 
	 * 需要手动导入requires jdk.incubator.httpclient;/java.net.http
	 * 
	 * @throws ExecutionException
	 */
	public static void test09() throws ExecutionException {
		try {
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest req = HttpRequest.newBuilder(URI.create("http://www.baidu.com")).GET().build();
			HttpResponse<String> response = null;
			// 同步调用
			response = client.send(req, HttpResponse.BodyHandlers.ofString());
			// 异步调用
			CompletableFuture<HttpResponse<String>> sendAsync =
					client.sendAsync(req, HttpResponse.BodyHandlers.ofString());
			sendAsync.get();
			System.out.println(response.statusCode());
			System.out.println(response.version().name());
			System.out.println(response.body());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}