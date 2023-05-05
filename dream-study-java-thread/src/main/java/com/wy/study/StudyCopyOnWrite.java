package com.wy.study;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * JUC包中各种线程安全的CopyOnWrite扩展类,底层使用CAS实现:
 * 
 * <pre>
 * {@link ArrayList},{@link HashSet} :线程不安全,多线程在同时进行增删改时会抛异常,因为没加锁
 * {@link CopyOnWriteArrayList}:线程安全的ArrayList,多线程下优于ArrayList,但更新的消耗更大,底层在写时会进行数组复制
 * {@link CopyOnWriteArraySet}:线程安全的HashSet,多线程下优于Set,更新的消耗会更大
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2020-11-24 22:40:00
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class StudyCopyOnWrite {

	public static void main(String[] args) {
	}
}