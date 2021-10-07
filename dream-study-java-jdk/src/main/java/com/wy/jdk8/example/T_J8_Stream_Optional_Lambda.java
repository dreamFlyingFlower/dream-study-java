package com.wy.jdk8.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.wy.model.Pojo;

/**
 * Stream,Optional,Lambda的使用
 * 
 * @apiNote Optional中主要方法是ofNullable,isPresent,ifPresent,orElse
 * @apiNote Lambda表达式的核心接口:Predicate,Consumer,Supplier,Function,
 *          当核心接口不满足时,可使用其他类似接口,全部都在java.util.function包中
 * @apiNote {@link java.util.function.Predicate<T>}:函数式接口,实现方法为boolean test(T t)
 *          即调用该方法的时候,必须使用lambda返回一个boolean结果.
 *          该接口中的其他default方法:and类似&&,or类似||,negate是test方法取反,isEqual判断2个对象是否相同
 *          这些方法都可以链式调用,返回的都是Predicate
 * @apiNote {@link java.util.function.Consumer<T>}:函数式接口,实现方法为void accept(T t)
 *          即调用该方法的时候是直接对参数进行操作,不能有返回值.其他方法类似Predicate可看源码
 * @apiNote {@link java.util.function.Supplier<T>}:函数式接口,实现方法为T get()
 *          即调用该方法的时候会返回指定类型的对象实例,但是无参
 * @apiNote {@link java.util.function.Function<T, R>}:函数式接口,实现方法为R apply(T t)
 *          即调用该方法的时候传入一个类型实例参数,但是会返回另外一个类型的实例,当然这2个类型也可以是相同的
 * @apiNote Stream的中间和结束方法都只能对流中的每一个元素进行操作,但是不能汇总.
 *          汇总需要用到collect方法或toArray或其他能将结果集重新收集的方法
 * @apiNote collect:将流转换为其他形式,接口一个Collector接口的实现,用于给stream中元素汇总.
 *          Collectors工具类可以求avg,sum,max,min,groupby,parting(分区),joining,summary(方法汇总)等
 * @apiNote parallel:并行流,当数据量比较大的时候用效果比较明显.
 *          并行流类似多选线程的fork,join,根据条件将任务分解到最小任务,并行运算.但是太麻烦
 * @author ParadiseWY
 * @date 2019-08-22 20:23:35
 * @git {@link https://github.com/mygodness100}
 */
public class T_J8_Stream_Optional_Lambda {

	public static void main(String[] args) {
		testStream();
	}

	public static void testOptional() {
		Pojo t1 = null;
		// 当使用of方法生成optional实例时,若是t1为null,扔会报空指针异常,该方法仍然不安全
		// System.out.println(Optional.of(t1));
		// 生成optional实例,相比于of方法ofNullable方法即使参数为null也不会报错,更安全
		Optional<Pojo> o11 = Optional.ofNullable(t1);
		// 直接生成一个空的optional,但是似乎没什么用,不能往里面set值
		Optional<Object> empty = Optional.empty();
		// 判断optional中的实体类是否有值,有值则返回true,没有则返回false
		System.out.println(empty.isPresent());
		// 根据一个实例对象生成optional对象,之后对生成的optional对象进行操作
		Optional<Object> o1 = Optional.of(new Pojo());
		// optional实例
		System.out.println(o1);
		// optional.empty实例
		System.out.println(o11);
		// true
		System.out.println(o1.isPresent());
		// 当o1中有值时,进行的操作,无返回值
		o1.ifPresent(t -> System.out.println(11));
		// 不输入22
		o11.ifPresent(t -> System.out.println(22));
		// false
		System.out.println(o11.isPresent());
		// 当o11中的实例有值返回,没有值时返回参数中的实例
		System.out.println(o11.orElse(new Pojo()));
		// 获得optional中的值,此处因为o11为null,获取值时仍然抛异常
		System.out.println(o11.get());
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
	}
}