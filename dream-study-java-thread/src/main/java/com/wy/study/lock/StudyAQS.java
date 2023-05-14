package com.wy.study.lock;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.LockSupport;

/**
 * {@link AbstractQueuedSynchronizer}:AQS,JUC包中基本所有的锁都是基于该类实现,底层是对state的CAS操作
 * 
 * 公平锁vs非公平锁: 新线程来了之后,发现有其他线程在排队,自己排到队伍末尾,这叫公平;线程来了之后直接去抢锁,这叫作不公平
 * 
 * <pre>
 * AQS内部的阻塞队列实现原理是基于双向链表,通过对head/tail进行CAS操作,实现入队和出队
 * 利用CAS对state进行修改来操作锁的获取和释放
 * 利用{@link LockSupport#park},{@link LockSupport#unpark}对线程进行阻塞或唤醒
 * 
 * {@link AbstractQueuedSynchronizer#state}:volatile修饰,当state=0表示没有线程竞争,state=1表示有线程竞争,state>1表示锁多次重入
 * {@link AbstractOwnableSynchronizer#exclusiveOwnerThread()}: 记录当前持有锁的线程
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
 * 队列操作:head指向双向链表头部,tail指向双向链表尾部.入队就是把新的Node加到tail后面;出队就是对head把head向后移一个位置.出队和入队都利用了CAS操作
 * 初始时,head=tail=NULL;在往队列中加入阻塞的线程时,会新建一个空的Node,让head和tail都指向这个空Node;之后,在后面加入被阻塞的线程对象.
 * 当head=tail的时候,说明队列为空
 * 
 * 子类可以支持独占和共享其中一种,也可以两种都支持.不同模式下的等待线程可以共享相同的FIFO队列
 * 独占模式:该模式下,其他线程试图获得锁将无法成功,需要实现AbstractQueuedSynchronizer的tryAcquire()和tryRelease
 * 共享模式:多个线程获取某个锁可能成功,也可能失败,需要实现AbstractQueuedSynchronizer#tryAcquireShared()和tryReleaseShared()
 * 
 * {@link AbstractQueuedSynchronizer#acquire()},{@link AbstractQueuedSynchronizer#release()}:互斥锁和读写锁的写锁都是基于这2个模板方法来实现的.
 * 		实际需要被子类实现的是{@link AbstractQueuedSynchronizer#tryAcquire()},{@link AbstractQueuedSynchronizer#tryRelease()}
 * {@link AbstractQueuedSynchronizer#acquireShared()},{@link AbstractQueuedSynchronizer#releaseShared()}:读写锁的读锁是基于这2个模板方法来实现的.
 * 		实际需要被子类实现的是{@link AbstractQueuedSynchronizer#tryAcquireShared},{@link AbstractQueuedSynchronizer#tryReleaseShared}
 * 
 * 对于非公平锁,读锁和写锁的实现策略略有差异:
 * ->写线程能抢锁,前提是state==0,只有在没有其他线程持有读锁或写锁的情况下,它才有机会去抢锁;或者state != 0,但那个持有写锁的线程是它自己,再次重入.
 * 		写线程是非公平的,即Sync#writerShouldBlock()一直返回false.
 * ->对于读线程,假设当前线程被读线程持有,然后其他读线程还非公平地一直去抢,可能导致写线程永远拿不到锁,所以对于读线程的非公平,要做一些约束.
 * 		当发现队列的第1个元素是写线程时,读线程也要阻塞,不能直接去抢,即偏向写线程

 * </pre>
 * 
 * @author 飞花梦影
 * @date 2023-05-05 09:40:17
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class StudyAQS {

}