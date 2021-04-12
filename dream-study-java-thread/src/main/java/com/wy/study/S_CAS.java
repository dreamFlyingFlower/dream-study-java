package com.wy.study;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * CAS:jvm底层的线程安全操作,即{@link AtomicInteger#compareAndSet}方法.<br>
 * 
 * @apiNote ConcurrentHashMap:线程安全的map,底层原来是分段锁,现在是CAS,可代替HashTable,多线程下优于HashMap
 *          ConcurrentSkipListMap:线程安全的TreeMap(可排序map),多线程下优于TreeMap,更新的消耗会更大
 *          CopyOnWriteArrayList:线程安全的ArrayList,多线程下优于ArrayList,但是更新操作的消耗也比ArrayList更大,
 *          因为底层在写入的同时会进行数组的复制<br>
 *          CopyOnWriteArraySet:线程安全的HashSet,多线程下优于Set,更新的消耗会更大
 * 
 * @author ParadiseWY
 * @date 2020-11-24 22:40:00
 * @git {@link https://github.com/mygodness100}
 */
public class S_CAS {

	public static void main(String[] args) {
	}
}