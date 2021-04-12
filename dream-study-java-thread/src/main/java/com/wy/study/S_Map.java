package com.wy.study;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 线程安全的map方法,HashTable基本已经淘汰
 * 
 * @author ParadiseWY
 * @date 2019-05-10 22:01:26
 * @git {@link https://github.com/mygodness100}
 */
public class S_Map {

	public static void main(String[] args) {

		// 线程安全的,无序map,key和value都不能为空,否则抛异常
		ConcurrentHashMap<String, String> map1 = new ConcurrentHashMap<>();
		map1.put("key", "value");

		// 跳表map,线程安全的,但是是有序的,key和value都不能为空,否则抛异常
		ConcurrentSkipListMap<Object, Object> map2 = new ConcurrentSkipListMap<>();
		map2.put("key", "value");
	}
}