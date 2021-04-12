package com.wy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 锁优化:减少锁持有时间,只有在需要的地方才加锁,进来不要整个方法都加锁<br>
 * 减少锁粒度:将大对象拆成小对象,大大增加并行度,降低锁竞争,使用Concurrent包中的Map等代替原生的同步类<br>
 * 锁分离:根据功能进行锁分离,如读写锁,读多写少的情况可以提高性能<br>
 * 
 * 多线程的调试,线程dump及分析,JDK8对并发的新支持:LongAdder,CompletableFuture,StampedLock
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