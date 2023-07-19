package com.wy.guava;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Multimap;
import com.wy.annotation.Example;

/**
 * Guava使用
 *
 * @author 飞花梦影
 * @date 2023-07-19 16:16:05
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
@Example
public class MyGuava {

	public static void main(String[] args) {
		// 多值Map,相当于Map<String,List<Integer>>
		Multimap<String, Integer> multimap = ArrayListMultimap.create();
		multimap.put("ssss", 1);
		multimap.put("ssss", 2);
		System.out.println(multimap.get("ssss"));
		System.out.println(multimap.asMap());

		// 双向Map,有太多注意事项,不建议使用
		HashBiMap<String, String> biMap = HashBiMap.create();
		biMap.put("Hydra", "Programmer");
		biMap.put("Tony", "IronMan");
		biMap.put("Thanos", "Titan");
		// 使用key获取value
		System.out.println(biMap.get("Tony"));
		// 不是想象的那种双向Map,此处为null
		System.out.println(biMap.get("Titan"));

		// 需要反转之后才能用原来的value拿到key
		BiMap<String, String> inverse = biMap.inverse();
		// 使用value获取key
		System.out.println(inverse.get("Titan"));
		System.out.println(inverse == biMap);
		System.out.println(inverse);
		// 改变inverse的值仍然会同步到原biMap中
		inverse.put("Tom", "SB");
		System.out.println(inverse);
		System.out.println(biMap);

		Joiner.on(",").skipNulls().join((Iterable<?>) null);
		Splitter.on("regex").split("");
	}
}