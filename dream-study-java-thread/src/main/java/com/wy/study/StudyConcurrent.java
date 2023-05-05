package com.wy.study;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * JUC包中各种线程安全的Concurrent扩展类,底层使用CAS实现,可以提高并发:
 * 
 * <pre>
 * {@link ConcurrentHashMap}:见{@link StudyConcurrentMap}
 * {@link ConcurrentSkipListMap}:线程安全的TreeMap(可排序map),多线程下优于TreeMap,但更新的消耗更大
 * {@link ConcurrentSkipListSet}:线程安全的TreeSet,多线程下优于TreeSet,但更新的消耗更大
 * {@link ConcurrentLinkedQueue},{@link ConcurrentLinkedDeque}:见{@link StudyQueue}
 * </pre>
 * 
 * Concurrent的弱一致性问题:
 * 
 * <pre>
 * 遍历时弱一致性.例如,当利用迭代器遍历时,如果容器发生修改,迭代器仍然可以继续进行遍历,这时内容是旧的
 * 容器容量弱一致性.size操作未必是100%准确,同上
 * 读取弱一致性
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2020-11-24 22:40:00
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class StudyConcurrent {

	public static void main(String[] args) {
	}
}