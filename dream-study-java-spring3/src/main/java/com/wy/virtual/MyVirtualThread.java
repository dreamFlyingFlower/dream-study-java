//package com.wy.virtual;
//
///**
// * 虚拟线程,JDK19开始
// * 
// * <pre>
// * Thread.setPriority(int) 和 Thread.setDaemon(boolean) 对虚拟线程不起作用
// * Thread.getThreadGroup() 会返回一个虚拟的空VirtualThreads group
// * 同一个任务使用Virtual Threads和Platform Threads执行效率上是完全一样的,并不会有什么性能上的提升
// * 尽量使用JUC包下的并发控制例如ReentrantLock来进行同步控制,而不使用synchronized,synchronized会Block虚拟线程,这点JDK官方说后面有可能会优化这点
// * Virtual Threads 被设计成final类,并不能使用子类来继承
// * 不适用于计算密集型任务:虚拟线程适用于I/O密集型任务(读写文件等),但不适用于计算密集型任务,因为它们在同一线程中运行,可能会阻塞其他虚拟线程
// * 新特性自然有很多BUG,这点在JDK的Issue中确实也体现了,使用请慎重
// * </pre>
// *
// * @author 飞花梦影
// * @date 2024-05-28 14:45:20
// * @git {@link https://github.com/dreamFlyingFlower}
// */
//public class MyVirtualThread {
//
//	public static void main(String[] args) {
//		Thread.startVirtualThread(() -> {
//			System.out.println(Thread.currentThread());
//		});
//
//		Thread.ofVirtual().start(() -> {
//			System.out.println(Thread.currentThread());
//		});
//		// 创建一个无限的虚拟线程池
//		ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
//	}
//}