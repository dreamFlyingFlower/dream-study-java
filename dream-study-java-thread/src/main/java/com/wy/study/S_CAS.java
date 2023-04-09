package com.wy.study;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * CAS:jvm底层的线程安全操作,即{@link AtomicInteger#compareAndSet()}
 * 
 * {@link ConcurrentHashMap}:线程安全的map,底层原来是分段锁,现在是CAS,可代替HashTable,多线程下优于HashMap
 * {@link ConcurrentSkipListMap}:线程安全的TreeMap(可排序map),多线程下优于TreeMap,但更新的消耗更大
 * {@link ConcurrentSkipListSet}:线程安全的TreeSet,多线程下优于TreeSet,但更新的消耗更大
 * {@link CopyOnWriteArrayList}:线程安全的ArrayList,多线程下优于ArrayList,但更新的消耗更大,底层在写时会进行数组复制
 * {@link CopyOnWriteArraySet}:线程安全的HashSet,多线程下优于Set,更新的消耗会更大
 * 
 * @author 飞花梦影
 * @date 2020-11-24 22:40:00
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class S_CAS {

	public static void main(String[] args) {
	}
}