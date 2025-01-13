package com.wy;

import java.lang.ref.PhantomReference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 锁:通俗叫管程,是一种Monitor(监视器),在Java中叫做锁.它是一种同步机制,保证多线程下只有一个线程能访问被保护的资源.
 * JVM对锁的管理就是基于管程对象的进入和退出实现的,进入加锁,退出解锁.管程对象是对象实例的一部分,随着对象建立,销毁
 * 
 * 锁优化:减少锁持有时间,只有在需要的地方才加锁,进来不要整个方法都加锁<br>
 * 减少锁粒度:将大对象拆成小对象,大大增加并行度,降低锁竞争,使用Concurrent包中的Map等代替原生的同步类<br>
 * 锁分离:根据功能进行锁分离,如读写锁,读多写少的情况可以提高性能<br>
 * 抛异常默认会自动释放锁
 * 
 * 多线程的调试,线程dump及分析,JDK8对并发的新支持:LongAdder,CompletableFuture,StampedLock
 * 
 * 不可变对象:
 * 
 * <pre>
 * 对象创建后其状态就不可修改,如String
 * 对象所有域都是final类型
 * 对象是正确创建的(在对象创建期间,this引用没有逸出)
 * guaua的一些集合,列表等
 * 使用局部变量,ThreadLocal让线程封闭,阻止并发环境
 * {@link Collections#synchronizedCollection(java.util.Collection)}等,内部仍然是使用synchronized
 * </pre>
 * 
 * 强,弱,软,虚引用:
 * 
 * <pre>
 * 强引用:普通对象就是强引用.当内存不足时,JVM开始进行 GC(垃圾回收),对于强引用对象,就算是出现了OOM 也不会对该对象进行回收
 * 软引用:利用{@link SoftReference}实现.当内存充足时,不会被回收;当内存不足时,会被回收,软引用通常用在对内存敏感的.程序中,比如高速缓存就用到软引用
 * 弱引用:利用{@link WeakReference}来实现.只要有垃圾回收,不管JVM 的内存空间够不够用,都会回收该对象占用的内存空间,ThrealLocal就是弱引用
 * 虚引用:利用{@link PhantomReference}来实现.虚引用就是形同虚设,与其它几种引用不同,虚引用并不会决定对象的声明周期
 * </pre>
 * 
 * 
 * 
 * 内存溢出(OOM)和内存泄漏:
 * 
 * <pre>
 * 内存溢出:内存不足
 * 内存泄漏:内存无法被释放.如ThreadLocal底层是ThreadLocalMap,如果不用remove,ThreadLocal将会一直被ThreadLocalMap引用,无法释放
 * </pre>
 * 
 * Lock 和 synchronized 的区别:
 * 
 * <pre>
 * 1.Lock 是一个接口,而 synchronized 是Java 中的关键字,synchronized 是内置的语言实现;
 * 2.synchronized 在发生异常时,会自动释放线程占有的锁,因此不会导致死锁现象发生;
 * 		而 Lock 在发生异常时,如果没有主动去释放锁,则很可能造成死锁现象,因此使用 Lock 时需要在 finally 块中释放锁;
 * 3.Lock 可以让等待锁的线程响应中断,而 synchronized 却不行,使用synchronized 时,等待的线程会一直等待下去,不能够响应中断;
 * 4.通过 Lock 可以知道有没有成功获取锁,而 synchronized 却无法办到
 * 5.Lock 可以提高多个线程进行读操作的效率.如果竞争不激烈,两者性能差不多,而竞争非常激烈时,Lock 的性能要远远优于synchronized
 * </pre>
 * 
 * 自旋和锁,如果程序耗时很久,用系统锁(synchronized),自旋很消耗CPU,如果线程太多,使用自旋同样很浪费资源,即并发较小或执行时间短用自旋
 * 
 * i++和++i都不是原子性的操作
 * 
 * 锁和CAS,为什么无锁效率高:
 * 
 * <pre>
 * 无锁情况下,即使重试失败,线程始终在高速运行,没有停歇,而synchronized 会让线程在没有获得锁的时候发生上下文切换,进入阻塞,代价比较大
 * 无锁情况下,线程要保持运行,需要额外CPU的支持,虽然不会进入阻塞,但由于没有分到时间片,仍然会进入可运行状态,还是会导致上下文切换
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2020-12-09 22:47:03
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}