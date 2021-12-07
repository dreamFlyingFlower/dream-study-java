package com.wy.jdk8.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Stream的使用
 * 
 * Stream的中间和结束方法都只能对流中的每一个元素进行操作,但不能汇总.汇总常用的是collect()或toArray()
 * collect():将流转换为其他形式,参数是Collector接口的实现
 * Collectors:该工具类可以求avg,sum,max,min,groupby,parting(分区),joining,summary(方法汇总)等
 * parallel:并行流,当数据量超过百万时效果比较明显.并行流类似多选线程的fork,join,根据条件将任务分解到最小,并行运算
 * 
 * Stream的运行机制:
 * 
 * <pre>
 * 所有操作都是链式调用,一个元素只迭代一次
 * 每个中间操作返回一个新的流,流里面有一个属性sourceStage,指向同一个Head
 * Head->nextStage->nextStage->...->null
 * 有状态操作(有返回值的操作)会把无状态操作(无返回值操作)阶段截断,进行单独处理
 * 并行环境下,有状态的中间操作不一定能并行执行
 * parallel/sequetial中2个操作也是中间操作(也是返回stream),但是他们不创建流,只修改Head的并行标志(sourceState)
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2019-08-22 20:23:35
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class MyTestStream {

	public static void main(String[] args) {
		testStream();
	}

	/**
	 * 获得Stream的方式:
	 * 
	 * <pre>
	 * 1.集合实例,直接调用stream()方法
	 * 2.数组,通过Arrays.stream(数组实例)
	 * 3.通过Stream.of的静态方法创建流
	 * 4.创建无限流
	 * </pre>
	 * 
	 * 开始方法:即获得流的操作,数组的Arrays.of,集合直接就有
	 * 
	 * 中间方法:对流的元素进行排除过滤,截断,筛选等操作,无返回结果,必须通过结束方法得出结果
	 * 
	 * <pre>
	 * filter:接收一个boolean的结果,排除结果为false的元素
	 * limit:截断流,使得符合的元素数量不超过给定数值
	 * skip:跳过元素,返回一个扔掉了前N个元素的流,若不足N,返回一个null的流
	 * distinct:筛选,通过流所生成元素的hashcode和equal去除重复元素
	 * map:将元素转换成其他形式或提取信息,接收一个方法,该方法将运行到每一个元素上,映射成一个新元素
	 * flatMap:将元素中的每一个元素都转换成一个流,再将所有的流连接成一个流
	 * sorted:不带参数是自然排序,带参数的是定制排序
	 * </pre>
	 * 
	 * 结束方法:只能用在stream的末尾,用了这些方法之后就不可再进行链式调用
	 * 
	 * <pre>
	 * allMatch:是否所有元素都匹配,是则返回true
	 * anyMatch:至少有一个匹配,有则返回true
	 * noneMatch:没有任何元素匹配,是则返回true
	 * findFirst:返回当前处理过的流中的第一个元素
	 * findAny:返回处理过的流中任意匹配的值元素
	 * count:返回匹配的元素个数
	 * max:返回匹配的流中元素的最大值
	 * min:返回匹配的元素的最小值
	 * foreach->内部迭代
	 * </pre>
	 * 
	 * reduce:归约方法,可以将流中的元素反复进行操作,得到一个值.类似递归
	 * 
	 * collect:将流转换为其他形式,接收一个Collector接口的实现,可使用Collectors工具类来返回值
	 */
	public static void testStream() {
		List<Integer> test = new ArrayList<>(Arrays.asList(1234, 6546, 765723));
		// 流在对对象执行操作时不会对原有对象进行修改,而是会形成一个新的对象,需要使用结束方法生成新的对象
		// stream():单线程流,按顺序执行操作
		// parallelStream():并行流,多个流同时执行操作,会打乱原本集合中的顺序,若必须要按照顺序来,可以使用带order的方法
		test.stream()
				// 自然排序
				.sorted()
				// 手动排序
				.sorted((t1, t2) -> Integer.compare(t1, t2))
				// filter方法可直接对数组中的元素进行过滤,但是只能调用返回值为boolean的方法
				.filter(t -> t > 1235)
				// 对符合前面所有条件的数据进行汇总
				// .count();
				// 是否stream中的所有元素都匹配,都匹配则返回true,有一个不匹配则返回false
				// .allMatch(t -> t > 100);
				// 对流中的元素进行操作,将元素的第1和第2个值赋值给2个参数,将结果赋值给下一次运算的x,流元素赋给y
				// .reduce((x,y)->x+y)
				// 第1个参数为起始值,将起始值赋值给x,流的第1个元素赋值给y,其他同上
				.reduce(0, (x, y) -> x + y);
		// 直接使用Collectors自带的汇总方法
		test.stream().map(t -> t.intValue() + 3).collect(Collectors.toList());
		// 自定义汇总
		test.stream().collect(Collectors.toCollection(HashSet::new));
		// 对数组中的元素进行排序,默认自然排序,升序
		Collections.sort(test);
		// 对数组中的元素进行排序,默认升序
		Collections.sort(test, Integer::compare);
		// 或者
		Collections.sort(test, (t1, t2) -> {
			return Integer.compare(t1, t2);
		});
		// 无限流,
		// 迭代.从第一个参数开始,不停的进行操作.下一次的操作参数就是第一次的结果
		Stream<Integer> stream = Stream.iterate(0, x -> x + 2);
		// limit:限制操作次数
		stream.limit(10).forEach(System.out::println);
		// 生成无限流,不同于迭代的是没有初始参数
		Stream<Double> generate = Stream.generate(() -> Math.random());
		generate.limit(5).forEach(System.out::println);
		// 获得数据分析,只能对数字类型有效
		IntSummaryStatistics summaryStatistics = test.stream().mapToInt(t -> t.intValue()).summaryStatistics();
		System.out.println(summaryStatistics.getMax());
		System.out.println(summaryStatistics.getMin());
		// flatmap:将流中的元素先单个分割转换为流之后,将所有的流合成为一个流
		Stream<List<Integer>> inputStream = Stream.of(Arrays.asList(1), Arrays.asList(2, 3), Arrays.asList(4, 5, 6));
		List<Integer> collect = inputStream.flatMap((childList) -> childList.stream()).collect(Collectors.toList());
		System.out.println(collect);
	}
}