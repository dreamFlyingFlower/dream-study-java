package com.wy.study;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TransferQueue;

/**
 * 队列.先进先出fifo
 * 
 * {@link BlockingQueue}:阻塞队列接口,当入队列时,若队列已满,则阻塞调用者;当出队列时,若队列为空,则阻塞调用者
 * 
 * <pre>
 * {@link BlockingQueue#add()}:入队方法,返回boolean,非阻塞.队列满时抛异常
 * {@link BlockingQueue#offer()}:入队方法,返回boolean,非阻塞.队列满时返回false
 * {@link BlockingQueue#put()}:入队方法,无返回值,阻塞且会抛中断异常
 * {@link BlockingQueue#remove()}:出队方法,非阻塞
 * {@link BlockingQueue#poll()}:出队方法,阻塞
 * {@link BlockingQueue#take()}:出队方法,阻塞且会抛中断异常
 * 
 * {@link ArrayBlockingQueue}:数组阻塞环形队列,只使用一把锁+2个条件对数据进行存储获取
 * {@link ArrayBlockingQueue#put()}:可被打断,队列满则阻塞.添加成功后通知非空条件(Condition)
 * {@link ArrayBlockingQueue#take()}:可被打断,队列空则阻塞.取出数据后通知非满条件(Condition)
 * 
 * {@link LinkedBlockingQueue}:链表阻塞单向队列,队头和队尾是2个指针分开操作的,使用2把锁+2个条件,同时有1个AtomicInteger记录count.
 * 		在其构造方法中,也可以指定队列的总容量,如果不指定,默认为Integer.MAX_VALUE
 * ->当节点总数大于2时(包括dummy节点),putLock保证last节点的线程安全,takeLock保证head节点的线程安全,两把锁保证入队和出队没有竞争
 * ->当节点总数等于2时(即一个dummy节点,一个正常节点)这时候,仍然是两把锁锁两个对象,不会竞争
 * ->当节点总数等于1时(就一个dummy节点),这时take线程会被notEmpty条件阻塞,有竞争,会阻塞
 * 
 * {@link PriorityBlockingQueue}:用数组实现的二插小根堆.队列默认先进先出,而PriorityQueue是按照元素的优先级从小到大出队列的.
 * 		PriorityQueue中的2个元素之间需要可以比较大小,并实现Comparable接口.
 * 		1个锁+一个条件,没有非满条件.如果不指定初始大小,内部会设定一个默认值11,当元素个数超过这个大小之后,会自动扩容
 * {@link PriorityBlockingQueue#put}:直接调用offer(),元素个数超过数组长度则自动扩容,不阻塞
 * {@link PriorityBlockingQueue#tryGrow}:扩容方法.当数组长度不够时调用
 * {@link PriorityBlockingQueue#take}:阻塞的实现和ArrayBlockingQueue相似,主要区别是用数组实现了一个二叉堆,
 * 		从而实现按优先级从小到大出队列.另一个区别是没有notFull条件,当元素个数超出数组长度时,执行扩容操作
 * 
 * {@link DelayQueue}:延迟队列,是一个按延迟时间从小到大出队列的PriorityQueue,元素必须实现Delayed.
 * 		包含1个锁+1个非空条件,一个优先级队列PriorityQueue
 * {@link DelayQueue#take()}:
 * ->1.该方法会无限循环,每次循环先取出二叉堆的堆顶元素,即延迟时间最小的对象,如果队列为空,则阻塞
 * ->2.如果队列不为空,获得堆顶元素的延迟时间,若延迟时间小于等于0,出队列返回该元素
 * ->3.如果延迟时间大于0,检查是否有其他线程也在等待该leader,有则无限期等待,否则阻塞有限时间
 * ->4.finally里:当前线程是leader,已经获取了堆顶元素,唤醒其他线程
 * 
 * ->1.一般的阻塞队列只在队列为空的时候才阻塞,DelayQueue如果堆顶元素的延迟时间没到,也会阻塞.
 * ->2.在上面的代码中使用了一个优化技术,用一个leader变量记录了等待堆顶元素的第1个线程.
 * 		这样做,通过getDelay()可以知道堆顶元素何时到期,不必无限期等待,可以使用condition.awaitNanos等待一个有限的时间;
 * 		只有当发现还有其他线程也在等待堆顶元素 (leader!=NULL)时,才需要无限期等待
 * {@link DelayQueue#put()},{@link DelayQueue#offer()}:
 * ->1.元素放入二叉堆,如果放进去的元素刚好在堆顶,说明放入的元素延迟时间最小,需要通知等待的线程;否则没有必要通知等待的线程
 * ->2.放入的元素延迟时间大于当前堆顶的元素延迟时间,就没必要通知等待的线程,只有当延迟时间是最小且在堆顶时,才有必要通知等待的线程
 * 
 * {@link SynchronousQueue}:不存储元素的单向阻塞队列,可设置为公平队列或非公平队列,每个元素插入必须等另一个线程调用移除,
 * 		否则插入一直阻塞.若同时有多个线程put(),则全部阻塞,直到有相同数据的线程调用take,这些线程才会全部解锁.
 * 		内部由内部类Transferer调度.性能通常高于LinkedBlockingQueue.
 * ->SynchronousQueue的put/take都调用了transfer(),而TransferQueue和TransferStack分别实现了这个接口.
 * 		如果是put(),则第1个参数就是对应的元素;如果是take(),则第1个参数为null.后2个参数分别为是否设置超时和对应的超时时间.
 * ->公平模式和非公平模式:假设3个线程分别调用了put(),3个线程会进入阻塞状态,直到其他线程调用3次take(),和3个put()一一配对
 * -->如果是公平模式(队列模式),则第1个调用put()的线程1会在队列头部,第1个到来的take()线程和它进行配对,遵循先到先配对的原则
 * -->如果是非公平模式(栈模式),则第3个调用put()的线程3会在栈顶,第1个到来的take()线程和它进行配对,遵循的是后到先配对的原则
 * {@link SynchronousQueue.TransferQueue}:单向链表队列,通过head和tail 2个指针记录头部和尾部.初始的时候,head和tail会指向一个空节点
 * {@link SynchronousQueue.TransferQueue#transfer}:如果当前线程和队列中的元素是同一种模式,
 * 		则与当前线程对应的节点被加入队列尾部并且阻塞;如果不是同一种模式,则选取队列头部的第1个元素进行配对.
 * 		这里的配对就是m.casItem(x,e),把自己的item x换成对方的item e,如果CAS操作成功,则配对成功;
 * 		如果是put节点,则isData=true,item!=null;如果是take节点,则isData=false,item=null;
 * 		如果CAS操作不成功,则isData和item之间将不一致,也就是isData!=(x!=null),通过这个条件可以判断节点是否已经被匹配过了
 * {@link SynchronousQueue.TransferStack#transfer}:一个单向链表,只需要head指针就能 实现入栈和出栈操作,没有空节点.
 * 		链表中的节点有三种状态,REQUEST对应take节点,DATA对应put节点,二者配对之后,会生成一个FULFILLING节点,入栈,
 * 		然后FULLING节点和被配对的节点一起出栈
 * 
 * {@link LinkedTransferQueue}:数据转移,无界队列,一般用于处理即时信息.
 * {@link LinkedTransferQueue#add}:队列会保存数据,不做阻塞等待.transfer会阻塞等待里面的数据被获取,必须有消费者来获取数据
 * 
 * {@link ConcurrentLinkedQueue}:实现原理和AQS的阻塞队列类似:都是基于CAS,都是通过head/tail指针记录队列头部和尾部,但有稍许差别
 * ->ConcurrentLinkedQueue内部队列是一个单向链表
 * ->在AQS的阻塞队列中,每次入队后,tail一定后移一个位置;每次出队,head一定后移一个位置,以保证head指向队列头部,tail指向链表尾部.
 * 		但在ConcurrentLinkedQueue中,head/tail的更新可能落后于节点的入队和出队,因为它不是直接对head/tail指针进行CAS操作的,
 * 		而是对Node中的item进行操作
 * 
 * {@link BlockingDeque}:阻塞双端队列,继承BlockingQueue,只有一个实现即LinkedBlockingDeque
 * {@link LinkedBlockingDeque}:链表组成的双向阻塞队列(可以从队列两端插入和删除元素),数据有界,一把锁+两个条件.
 * 		对应的实现原理,和LinkedBlockingQueue基本一样,只是LinkedBlockingQueue是单向链表,而LinkedBlockingDeque是双向链表
 * {@link ConcurrentLinkedDeque}:安全的双向队列,实现和ConcurrentLinkedQueue相同
 * </pre>
 * 
 * LinkedBlockingQueue和ArrayBlockingQueue的差异:
 * 
 * <pre>
 * 1.为了提高并发度,用2把锁,分别控制队头,队尾的操作,意味着在put()和put()之间,take()与take()之间是互斥的,put()和take()之间并不互斥.
 * 但对于count(元素个数)变量,双方都需要操作,所以必须是原子类型
 * 2.因为各自拿了一把锁,所以当需要调用对方的condition的signal时,还必须再加上对方的锁,就是signalNotEmpty()和signalNotFull()
 * 3.不仅put会通知 take,take 也会通知 put.当put 发现非满的时候,也会通知其他 put线程;当take发现非空的时候,也会通知其他take线程
 * 4.Array必须指定长度,Linked可以指定长度,也可以不指定长度,最大值为Integer.MAX
 * 5.Array是数组实现,Linked是链表实现
 * 6.Array的Node节点是提前初始化好的,Linked是添加的时候才新建Node对象
 * </pre>
 * 
 * LinkedBlockingQueue和ConcurrentLinkedQueue的差异:最大的差异是LinkedBlockingQueue使用锁,ConcurrentLinkedQueue使用CAS
 * 
 * @author 飞花梦影
 * @date 2019-05-10 22:08:59
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class StudyQueue {

	public static void main(String[] args) {
		/**
		 * 线程安全非阻塞队列
		 */
		ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();
		for (int i = 0; i < 10; i++) {
			queue.offer("value" + i);
		}
		System.out.println(queue.size());
		// 查看queue中的第一个数据
		System.out.println(queue.peek());
		// 拿出第一个数据,返回拿出的数据,先进先出
		System.out.println(queue.poll());

		/**
		 * 线程阻塞队列,put队列容量满后,自动阻塞;take队列容量为0后,自动阻塞
		 */
		BlockingQueue<String> queue1 = new LinkedBlockingQueue<String>();
		try {
			queue1.put("String1");
			queue1.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// FIXME
		BlockingQueue<String> queue2 = new LinkedBlockingDeque<>();
		try {
			queue2.put("str");
			queue2.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		/**
		 * 底层数组实现的有界队列,自动阻塞,根据调用的api(add/put/offer),有不同的特性 当容易不足时,自动阻塞
		 */
		BlockingQueue<String> queue3 = new ArrayBlockingQueue<>(10);
		// add方法,当队列的容量满后,再往队列中add数据时,会抛异常
		queue3.add("test");
		try {
			// put方法,当队列满时,会自动阻塞,可被线程打断
			queue3.put("对方答复");
			// 若添加成功,会返回true,添加失败会返回false.超出容量相当于添加失败,返回false,不阻塞也不抛异常
			queue3.offer("这是offer");
			// 有参的offer方法,当容量不足时,会进行阻塞,等待添加.
			// 若在阻塞时队列有空闲,则可添加.若超过时长,会返回false,不抛异常
			queue3.offer("offer阻塞", 2, TimeUnit.SECONDS);
			queue3.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		/**
		 * 延迟加载队列,类似于定时任务.泛型参数必须实现Delayed接口,重写比较方法以及获取计划时长的方法
		 * 应用场景,相当于随时随地的新建一个定时任务,而不需要重新修改定时任务的参数
		 */
		DelayQueue<TestDelay> queue4 = new DelayQueue<>();
		TestDelay t1 = new TestDelay(System.currentTimeMillis() + 1000);
		TestDelay t2 = new TestDelay(System.currentTimeMillis() + 2000);
		TestDelay t3 = new TestDelay(System.currentTimeMillis() + 3000);
		TestDelay t4 = new TestDelay(System.currentTimeMillis() + 5000);
		TestDelay t5 = new TestDelay(System.currentTimeMillis() + 4000);
		queue4.put(t1);
		queue4.put(t2);
		queue4.put(t3);
		queue4.put(t4);
		queue4.put(t5);
		System.out.println(queue4);
		for (int i = 0; i < 5; i++) {
			try {
				System.out.println(queue4.take());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		/**
		 * 转移队列,数据无界
		 * 
		 * add:队列会保存数据,不做阻塞等待.transfer会阻塞等待里面的数据被获取,必须有消费者来获取数据 一般用于处理即时信息
		 */
		TransferQueue<String> queue5 = new LinkedTransferQueue<>();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					queue5.add("这是被存在队列里的数据");
					// transfer方法是阻塞方法,若是放在主线程中,若是先调用transfer方法,那么会一直阻塞,只能先调用take方法
					// 若是多线程使用transfer和take,那就无所谓先take还是先transfer
					queue5.transfer("这是被转移的数据");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					System.out.println(queue5.take());
					System.out.println(queue5.take());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();

		/**
		 * 同步队列,是一个容量为0的特殊transferqueue,必须先有消费线程等待,才能使用
		 * SynchronousQueue和LinkedTransferQueue的区别在于:即使LinkedTransferQueue调用了add方法,
		 * 而此时并没有take方法来消费LinkedTransferQueue,也不会抛出异常.但是SynchronousQueue会抛出异常
		 * 应用场景:例如游戏配对
		 */
		BlockingQueue<String> queue6 = new SynchronousQueue<>();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					System.out.println(queue6.take());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();

		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					// 该队列的容量永远为0,当调用add方法时,会直接抛出异常,除非已经有线程在等待调用take方法
					// queue6.add("srewrw");
					// put若没有消费者take,会阻塞当前线程,直到有消费者take,不会抛出异常
					queue6.put("tsrt queue6");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}

class TestDelay implements Delayed {

	// 用来做比较大小的参数
	private long compare;

	public TestDelay(long compare) {
		this.compare = compare;
	}

	/**
	 * 比较大小,自定义实现,可升序,可降序 建议和getDelay方法配置完成
	 * 如果在DelayQueue是需要按时间完成的计划任务,必须配合getDelay方法完成
	 */
	@Override
	public int compareTo(Delayed o) {
		return (int) (this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
	}

	/**
	 * 获取计划时长的方法,即根据什么规则来取出队列中的数据.具体根据参数TimeUnit来决定如何返回结果值 要根据具体的业务场景来实现该方法
	 */
	@Override
	public long getDelay(TimeUnit unit) {
		return unit.convert(compare - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
	}
}