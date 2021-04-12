package com.wy.jdks;

public class JDK8 {

	/**
	 * @apiNote 函数式编程:即当一个接口中有且只有一个抽象方法,则该接口是一个函数式接口,可使用lambda实现
	 *          函数式接口中可以有default和static方法,在接口上添加@FunctionlInterface,表明该接口是一个函数式接口
	 * 
	 *          四大核心函数式接口,都包含一个接口:<br>
	 *          Consumer<T>->void:消费型接口,对类型为T的对象应用操作,包含void accept(T t)<br>
	 *          Supplier<T>->T:供给型接口,返回T类型的对象,包含T get()<br>
	 *          Function<T,R>->R:函数型接口,对T对象应用操作,并返回R结果,R可以和T同类型,包含R apply(T t)<br>
	 *          Predicate<T>->boolean:断定型接口,确定对象是否满足条件,返回boolean,包含boolean test(T t)
	 * 
	 *          其他常用函数式接口,部分是继承上述四大函数式接口:<br>
	 *          BiFunction<T, U, R>->R:和Function类型,只是参数的个数读了一个,包含R apply(T t,U u)
	 *          UnaryOperator<T>->T:继承Function,相当于Function中的R和T是同一类型
	 *          BinaryOperator<T>->T:继承BiFunction,相当于T,U,R都是同一类型
	 *          BiConsumer<T,U>-void:类似于Consumer,相当于多传了一个参数
	 *          ToIntFunction<T>,ToLongFunction<T>,ToDoubleFunction<T>:分别计算int,long,double值的函数
	 *          IntFunction<R>,LongFunction<R>,DoubleFunction<R>:根据int,long,double计算返回R类型的值
	 * 
	 * @apiNote Lanbda表达式三种使用情况:<br>
	 *          1.对象::实例方法名,注意中间是两个冒号<br>
	 *          2.类::静态方法名,中间也是两个冒号<br>
	 *          3.类::实例方法名,中间也是两个冒号
	 * 
	 * @apiNote 接口中可以定义方法,但方法必须是default和static修饰的方法,都可以有多个,
	 *          default方法只能是实现接口的实例调用,静态方法仍然是该接口调用.
	 * 
	 *          当接口A中定义了default方法test1时,若B继承A,同时B也定义了default方法test1,
	 *          当实体类C实现B时,调用test1方法,那么执行的是根据实现规则往上查找的最近一个test1方法,即调用B的方法.
	 *          当B不继承A时,若是C同时实现了A,B,那么会编译器报错,不可同时有2个同名的default方法
	 * 
	 * @apiNote 在匿名内部类中调用外部的变量时,外部变量不需要加上final修饰符,编译器会自动加上final
	 * 
	 * @apiNote Lambda,Stream,Optional等相关类简化对字符串,集合等数据类型的操作,分为串行流和并行流
	 *          StreamAPI只是减少了代码量,但在效率上比传统的for循环要低,只在极大数据量上进行循环时Stream才有优势
	 *          Stream对数据的操作都是不改变元数据的,需要对操作之后的数据调用Stream的结束方法
	 * 
	 * @apiNote 新增了LocaleDate,LocaleTime,LocaleDateTime等操作时间的类
	 * 
	 * @apiNote Hash表算法改变:由原来的数组+链表->数组+链表+红黑树
	 * 
	 * @apiNote 虚拟机中的永久区(PremGen)移除,改为元空间(MetaSpace),直接使用物理内存(即运行时内存).
	 *          相应的JVM参数将无效,如:PremGenSize,MaxPremGenSize,改为MetaSpaceSize,MaxMetaSpaceSize
	 * 
	 * @apiNote 便于并行的fork和join,多线程的应用中,很容易的将串行变成并行.<br>
	 *          在一般的线程池中,若一个线程正在执行的任务由于某些原因无法继续运行,那么该线程会处于等待状态.<br>
	 *          而在fork/join框架实现中,遇到上述问题时,该线程会主动寻找其他尚未运行的子问题来执行.<br>
	 *          这种方式减少了线程的等待时间,提高了性能
	 * 
	 * @apiNote 可重复注解,在相同的方法等上面可写一样的注解
	 * 
	 * @apiNote CompletableFuture,类型注解,通用目标类型推断
	 */

}