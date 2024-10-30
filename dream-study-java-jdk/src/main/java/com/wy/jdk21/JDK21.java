package com.wy.jdk21;

import static java.lang.StringTemplate.STR;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.SequencedMap;
import java.util.SequencedSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * JDK21新特性
 * 
 * <pre>
 * 1.虚拟线程:不是真正的系统级别的线程.提高应用程序吞吐量,提高应用程序可用性,减少内存消耗
 * 2.顺序集合:SequencedSet,	SequencedMap
 * 3.字符串模板:STR
 * 4.记录模式
 * 5.switch模式匹配:从预览转为实际功能
 * 6.未命名模式和变量-预览
 * 7.未命名类和实例主要方法-预览
 * 8.作用域值-预览
 * 9.结构化并发-预览
 * </pre>
 *
 * @author 飞花梦影
 * @date 2024-10-30 09:11:10
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class JDK21 {

	public static void main(String[] args) {
		sequence();
		text();
		getTransactionType(new Transaction(11d));
	}

	public static void virtual() {
		// 创建虚拟线程,并立刻启动,调用线程的start()
		Thread.ofVirtual().start(() -> {
		});
		// 创建虚拟线程,但是不立刻启动
		Thread.ofVirtual().unstarted(() -> {
		});

		// 创建虚拟线程池
		ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
		executor.submit(() -> {
			System.out.println(Thread.currentThread().getName());
		});

		executor.shutdown();
	}

	public static void sequence() {
		// 有序set,用于访问第一个和最后一个元素并以相反的顺序迭代,这意味着可以在集合的两端添加、检索或删除元素
		// 在新的JDK版本中,LinkedHashSet实现了SequencedSet,而不是直接实现了Set
		SequencedSet<String> sequencedSet = new LinkedHashSet<>();
		// 如果不调用addFirst,addLast,只调用add,那就和List功能差不多,只是元素不能重复
		sequencedSet.add("str1");
		// addFirst按照调用顺序来,后调用的会放到更前面.addLast后调用的放到更后面
		sequencedSet.addFirst("stre2");
		sequencedSet.addFirst("stre3");
		sequencedSet.add("str1");
		sequencedSet.add("fdef4");
		sequencedSet.addLast("fer6");
		sequencedSet.addLast("fer5");
		System.out.println(sequencedSet);

		// LinkedHashMap是有序集合SequencedMap的实现类,之前的实现的是其他接口
		SequencedMap<String, Object> linkedHashMap = new LinkedHashMap<>();
		// 排序情况和SequencedSet类型
		linkedHashMap.put("lalalala1", "lalalala1");
		linkedHashMap.putFirst("lalanan2", "lalanan2");
		linkedHashMap.putLast("lalanan5", "lalanan5");
		linkedHashMap.putLast("lalanan3", "lalanan3");
		System.out.println(linkedHashMap);

		// 访问第一个和最后一个元素
		System.out.println(linkedHashMap.firstEntry());
		System.out.println(linkedHashMap.lastEntry());
		// 删除第一个/最后一个元素并返回被删除的元素
		System.out.println(linkedHashMap.pollFirstEntry());
		System.out.println(linkedHashMap.pollLastEntry());
	}

	/**
	 * 模板表达式由三个组成部分组成:
	 * 
	 * <pre>
	 * 1.模板处理器:Java 提供了两种用于执行字符串插值的模板处理器:STR 和 FMT
	 * 2.包含包装表达式的模板,如 {name}
	 * 3.点 (.) 字符
	 * </pre>
	 */
	public static void text() {
		String firstName = "first";
		String lastName = "last";
		// 需要开启预览功能,在本项目的Java Compile中勾选Enable Preview features
		System.out.println(STR."Hello! Good morning \{ firstName } \{ lastName }");

		int a = 10;
		int b = 20;
		System.out.println(STR."\{ a } times \{ b } = \{ Math.multiplyExact(a, b) }");

				String text1 = STR."""
         {
           "httpStatus": "\{ firstName }",
           "errorMessage": "\{ lastName }"
         }""";

		System.out.println(text1);

		// 会影响格式化
		String text2 = STR."Today's date: \{
		        LocalDate.now().format(		                
		        		DateTimeFormatter.ofPattern("yyyy-MM-dd")		      
		        		) }";
		// FMT暂时没有,主要处理小数
		// System.out.println(FMT."%.2f\{ a } times %.2f\{ b } = %.2f\{ a * b }" );
				System.out.println(text2);
	}

	/**
	 * 模式匹配
	 * 
	 * @param transaction
	 * @return
	 */
	public static String getTransactionType(Transaction transaction) {
		return switch (transaction) {
		case null:
			throw new IllegalArgumentException("Transaction can't be null.");
		case Deposit deposit when deposit.getAmount() > 0:
			yield "Deposit";
		case Withdrawal withdrawal:
			yield "Withdrawal";
		default:
			yield "Unknown transaction type";
		};
	}
}

sealed class Transaction permits Deposit, Withdrawal {

	private double amount;

	public Transaction(double amount) {
		this.amount = amount;
	}

	public double getAmount() {
		return amount;
	}
}

final class Withdrawal extends Transaction {

	public Withdrawal(double amount) {
		super(amount);
	}
}

final class Deposit extends Transaction {

	public Deposit(double amount) {
		super(amount);
	}
}