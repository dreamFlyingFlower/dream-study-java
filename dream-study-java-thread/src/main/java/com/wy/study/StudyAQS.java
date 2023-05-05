package com.wy.study;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * {@link AbstractQueuedSynchronizer}:AQS,JUC包中基本所有的锁都是基于该类实现
 * 
 * <pre>
 * AQS内部的阻塞队列实现原理是基于双向链表,通过对head/tail以及state进行CAS操作,实现入队和出队,锁的获取和释放.
 * 该类使用volatile修饰的state这个int值来表示状态的同步器,子类必须重写更改此状态的方法,并定义何种状态是被获取或释放
 * 
 * AQS底层是CAS+volatile,因为state是volatile修饰的,当state=0表示没有线程竞争,state=1表示有线程竞争,state>1表示可重入
 * 
 * {@link AbstractQueuedSynchronizer#setState()},{@link AbstractQueuedSynchronizer#getState()}:获取或设置state
 * {@link AbstractQueuedSynchronizer#compareAndSetState()}:以原子方式更改该状态的值,以便实现对锁的操作
 * 
 * {@link AbstractQueuedSynchronizer.Node}:采用双向链表的形式存放正在等待的线程
 * ->CANCELLED:1,表示当前的线程被取消
 * ->SIGNAL:-1,释放资源后需唤醒后继节点.在每次释放锁的时候会改变该值
 * ->CONDITION:-2,等待 condition 唤醒
 * ->PROPAGATE:-3,工作于共享锁状态,需要向后传播,比如根据资源是否剩余,唤醒后继节点
 * ->0:表示当前节点在 sync 队列中,等待着获取锁
 * ->head:等待队列的头节点
 * ->tail:正在等待的线程尾节点
 * ->state:锁的状态. 0无锁;大于0表示已获取锁,当前线程重入不断+1
 * ->exclusiveOwnerThread:记录锁的持有线程
 * 
 * 子类可以支持独占和共享其中一种,也可以两种都支持.不同模式下的等待线程可以共享相同的FIFO队列
 * 独占模式:该模式下,其他线程试图获得锁将无法成功,需要实现AbstractQueuedSynchronizer的tryAcquire()和tryRelease
 * 共享模式:多个线程获取某个锁可能成功,也可能失败,需要实现AbstractQueuedSynchronizer#tryAcquireShared()和tryReleaseShared()
 * 
 * JUC包里的同步组件主要实现了AQS的tryAcquire,tryRelease,tryAcquireShared,tryReleaseShared,isHeldExclusivelycoding方法
 * </pre>
 *
 * @author 飞花梦影
 * @date 2023-05-05 09:40:17
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class StudyAQS {

}