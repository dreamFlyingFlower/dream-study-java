package com.wy.study;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * CAS:Compare And Swap,比较替换,操作包含三个操作数-内存位置(V),预期原值(A)和新值(B)
 * 如果内存位置的值与预期原值相配,那么处理器会自动将该位置值更新为新值;否则,处理器不做任何操作.
 * CAS存在三大问题:ABA问题,循环时间长开销大,以及只能保证一个共享变量的原子操作
 * 案例可见{@link AtomicInteger#compareAndSet()}
 * 
 * 各种线程安全的扩展类:
 * <pre>
 * {@link ConcurrentHashMap}:线程安全的map,底层原来是分段锁,现在是CAS,可代替HashTable,多线程下优于HashMap
 * {@link ConcurrentSkipListMap}:线程安全的TreeMap(可排序map),多线程下优于TreeMap,但更新的消耗更大
 * {@link ConcurrentSkipListSet}:线程安全的TreeSet,多线程下优于TreeSet,但更新的消耗更大
 * {@link ArrayList},{@link HashSet} :线程不安全,多线程在同时进行增删改时会抛异常,因为没加锁
 * {@link CopyOnWriteArrayList}:线程安全的ArrayList,多线程下优于ArrayList,但更新的消耗更大,底层在写时会进行数组复制
 * {@link CopyOnWriteArraySet}:线程安全的HashSet,多线程下优于Set,更新的消耗会更大
 * {@link ConcurrentLinkedQueue}:实现原理和AQS的阻塞队列类似:都是基于CAS,都是通过head/tail指针记录队列头部和尾部,但有稍许差别
 * ->1.ConcurrentLinkedQueue内部队列是一个单向链表
 * ->2.在AQS的阻塞队列中,每次入队后,tail一定后移一个位置;每次出队,head一定后移一个位置,以保证head指向队列头部,tail指向链表尾部.
 * 		但在ConcurrentLinkedQueue中,head/tail的更新可能落后于节点的入队和出队,因为它不是直接对head/tail指针进行CAS操作的,
 * 		而是对Node中的item进行操作
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2020-11-24 22:40:00
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class StudyJUC {

	public static void main(String[] args) {
	}
}