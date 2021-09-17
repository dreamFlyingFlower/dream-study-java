package com.wy.study;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 线程安全的map方法,HashTable基本已经淘汰
 * 
 * {@link ConcurrentHashMap}:JDK8抛弃了Segment分段锁机制,利用CAS+Synchronized来保证线程安全,
 * 底层依然是数组+链表+红黑树.当链表长度超过8时,链表转换为红黑树
 * 
 * <pre>
 * table:第一次插入时初始化,默认大小为16的数组,用来存储Node节点数据,扩容时大小总是2的幂次方
 * nextTable:默认为null,扩容时新生成的数组,其大小为原数组的两倍
 * sizeCtl:默认为0,用来控制table的初始化和扩容操作,具体应用在后续会体现出来
 * ->-1:代表table正在初始化
 * ->-N:表示有N-1个线程正在进行扩容操作
 * ->其余情况:如果table未初始化,表示table需要初始化的大小;如果table已初始化,表示table的容量
 * Node:保存key,value及key的hash值的数据结构
 * ForwardingNode:一个特殊的Node,hash值为-1,存储nextTable的引用.
 * 		只有table扩容时,该值作为一个占位符放在table中表示当前节点为null或则已经被移动
 * 扩容:当容器中的元素个数大于capacity * loadfactor时,容器会进行扩容resize 为 2n
 * </pre>
 * 
 * {@link ConcurrentHashMap#put()}:存储元素,key和value都不能时null
 * 
 * <pre>
 * ->{@link ConcurrentHashMap#putVal()}:向table中新加入元素时,先定位索引位置.利用tabAt()获得最新的元素f,
 * 		如果f为null,说明table中这个位置第一次插入元素,利用Unsafe.compareAndSwapObject方法插入Node节点.
 * 		如果插入f成功,则直接条出循环.如果当前f的hash值为-1,说明f是ForwardingNode节点,意味有其他线程正在扩容
 * ->{@link ConcurrentHashMap#tabAt()}:获得table中的元素,使用了{@link sun.misc.Unsafe#getObjectVolatile()},在Java内存模型中,
 * 		每个线程都有一个栈内存,里面存储着table的副本,虽然table是volatile修饰的,但不能保证线程每次都拿到table中的最新元素,
 * 		Unsafe.getObjectVolatile()可以直接获取指定内存的数据,保证每次拿到数据都是最新的
 * ->{@link ConcurrentHashMap#casTabAt()}:利用CAS插入Node节点,随后addCount()会检查当前容量是否需要进行扩容.
 * 		如果CAS失败,说明有其它线程提前插入了节点,自旋重新尝试在这个位置插入节点
 * ->{@link ConcurrentHashMap#putVal()}:其余情况把新Node按链表或红黑树的方式插入到合适的位置,采用同步内置锁实现并发
 * -->synchronized代码块:在节点f上进行同步,节点插入之前,再次利用tabAt(tab, i) == f判断,防止被其它线程修改
 * 		如果f.hash >= 0,说明f是链表结构的头结点,遍历链表,如果找到对应的node节点,则修改value,否则在链表尾部加入节点
 * 		如果f是TreeBin类型节点,说明f是红黑树根节点,则在树结构上遍历元素,更新或增加节点
 * 		如果链表中节点数binCount >= TREEIFY_THRESHOLD(默认是8),则把链表转化为红黑树结构
 * </pre>
 * 
 * table扩容:table的元素数量达到容量阈值sizeCtl时,先构建一个nextTable,大小为table的两倍,再将table的数据复制到nextTable中
 * 
 * @author ParadiseWY
 * @date 2019-05-10 22:01:26
 * @git {@link https://github.com/mygodness100}
 */
@SuppressWarnings("restriction")
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