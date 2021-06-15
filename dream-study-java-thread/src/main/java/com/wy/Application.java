package com.wy;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 锁优化:减少锁持有时间,只有在需要的地方才加锁,进来不要整个方法都加锁<br>
 * 减少锁粒度:将大对象拆成小对象,大大增加并行度,降低锁竞争,使用Concurrent包中的Map等代替原生的同步类<br>
 * 锁分离:根据功能进行锁分离,如读写锁,读多写少的情况可以提高性能<br>
 * 
 * 多线程的调试,线程dump及分析,JDK8对并发的新支持:LongAdder,CompletableFuture,StampedLock
 * 
 * {@link AbstractQueuedSynchronizer}:AQS,为了方便实现同步锁以及相关的同步器(信号量,事件等)提供的一个框架.
 * 该类设计成依靠单个原子int值来表示状态的同步器,子类必须定义更改此状态的受保护方法,并定义何种状态是被获取或释放.
 * 可以使用{@link AbstractQueuedSynchronizer#setState()},{@link AbstractQueuedSynchronizer#getState()},
 * {@link AbstractQueuedSynchronizer#compareAndSetState()}以原子方式更改该状态的值,以便实现对锁的操作.
 * 
 * 子类可以支持独占和共享其中一种,也可以两种都支持.不同模式下的等待线程可以共享相同的FIFO队列
 * 独占模式:该模式下,其他线程试图获得锁将无法成功,需要实现AbstractQueuedSynchronizer的tryAcquire()和tryRelease
 * 共享模式:多个线程获取某个锁可能成功,也可能失败,需要实现AbstractQueuedSynchronizer#tryAcquireShared()和tryReleaseShared()
 *
 * @author ParadiseWY
 * @date 2020-12-09 22:47:03
 * @git {@link https://github.com/mygodness100}
 */
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}