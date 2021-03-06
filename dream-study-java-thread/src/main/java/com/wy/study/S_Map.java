package com.wy.study;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * HashMap , ConcurrentHashMap ,HashTable基本已经淘汰
 * 
 * {@link HashMap}:数组+链表+红黑树,数组长度超过64且链表长度超过8就转为红黑树;红黑树长度小于6转为链表
 * 
 * <pre>
 * HashMap 通过 key 的 hashCode 经过扰动函数处理过后得到 hash 值,
 * 然后通过 (n - 1) & hash 判断当前元素存放的位置(n 指的是数组的长度),
 * 如果当前位置存在元素的话,就判断该元素与要存入的元素的 hash 值以及 key 是否相同,
 * 如果相同,直接覆盖,不相同就通过拉链法解决冲突,然后存入链表中
 * 扰动函数:指 HashMap 的 hash 方法,使用 hash 方法是为了防止一些实现比较差的 hashCode() 方法,可以减少碰撞
 * 拉链法:将链表和数组相结合,也就是数组中每一格就是一个链表.若遇到哈希冲突,则将冲突的值加到链表中即可
 * </pre>
 * 
 * {@link ConcurrentHashMap}:底层依然是数组+链表+红黑树,但是JDK8抛弃了Segment分段锁机制,
 * 利用CAS+Synchronized+Node来保证线程安全.如果数组index没有发生冲突,则使用CAS;发生冲突则使用Synchronized加锁.
 * JDK8之所以抛弃Segment,是因为需要先要查询Segment的位置,再根据key从Segment中查询key的位置;
 * JDK7使用{@link ReentrantLock}进行加锁,该锁是重量级锁.但是Synchronized会自动升级,从轻量级锁开始->自旋锁->重量级锁
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
 * 		table的元素数量达到容量阈值sizeCtl时,先构建一个nextTable,大小为table的两倍,再将table的数据复制到nextTable中
 * 		数组扩容时key的hash值是不变的
 * </pre>
 * 
 * {@link ConcurrentHashMap#put()}:存储元素,key和value都不能时null
 * 
 * <pre>
 * {@link ConcurrentHashMap#putVal()}:向table中新加入元素时,先定位索引位置.利用tabAt()获得最新的元素f,
 * 		如果f为null,说明table中这个位置第一次插入元素,利用Unsafe.compareAndSwapObject方法插入Node节点.
 * 		如果插入f成功,则直接跳出循环.如果当前f的hash值为-1,说明f是ForwardingNode节点,意味有其他线程正在扩容
 * {@link ConcurrentHashMap#tabAt()}:获得table中的元素,使用了{@link sun.misc.Unsafe#getObjectVolatile()},
 * 		在Java内存模型中,线程栈里面存储着table的副本,虽然table是volatile的,但不能保证线程每次都拿到table中的最新元素,
 * 		Unsafe.getObjectVolatile()可以直接获取指定内存的数据,保证每次拿到数据都是最新的
 * {@link ConcurrentHashMap#casTabAt()}:利用CAS插入Node节点,随后addCount()会检查当前容量是否需要进行扩容.
 * 		如果CAS失败,说明有其它线程提前插入了节点,自旋重新尝试在这个位置插入节点
 * {@link ConcurrentHashMap#putVal()}:其余情况把新Node按链表或红黑树的方式插入到合适的位置,采用同步内置锁实现并发
 * 		synchronized代码块:在节点f上进行同步,节点插入之前,再次利用tabAt(tab, i) == f判断,防止被其它线程修改
 * 		如果f.hash >= 0,说明f是链表结构的头结点,遍历链表,如果找到对应的node节点,则修改value,否则在链表尾部加入节点
 * 		如果f是TreeBin类型节点,说明f是红黑树根节点,则在树结构上遍历元素,更新或增加节点
 * 		如果链表中节点数binCount >= TREEIFY_THRESHOLD(默认是8),则把链表转化为红黑树结构
 * </pre>
 * 
 * 2的0次方到N次方求和公式:
 * 
 * <pre>
 * S=2^0 + 2^1 + 2^2 + 2^3 +…… + 2^N
 * 2S=2^1 + 2^2+2^3+……+2^(N+1)=S-2^0+2^(N+1)
 * S=2^(N+1)-2^0 = 2^(N+1) - 1
 * </pre>
 * 
 * 初始化设置数组长度为2的幂次方:
 * 
 * <pre>
 * {@link HashMap#tableSizeFor(n)}:初始化 HashMap 数组的最大长度,该长度不超过2^30,会对长度进行31位位移调整
 * 1.经过2的指数级位移,最终会将n的二进制位位移31位
 * 2.每次右移指定长度,n的二进制表示最高位都会补0
 * 3.移位后的二进制位和n的原二进制位做|运算,此运算保证了n的二进制位即使右移31位,仍然和原n的二进制位位数相同
 * 4.|运算完成后得到的新值最高位+右移的位都被1占满
 * 5.依次做运算,只要是int类型,所有的位都会被1占满
 * 6.若移位后的值超过了2^30,则数组长度为2^30
 * 7.若不超过2^30,数组长度则为n的最高位(2^m+...+2^1+...+1+1)的和,相当于2^(m+1)
 * 		如:00010010 -> 00011111,初始长度n为18,经过运算后m为4,最终的长度为2^5=32
 * 8.回过头看第一步的n-1,这是为了保证n如果本身就是2^x,则数组长度仍然是2^x而不是2^(x+1)
 * 9.数组长度初始化规则:若n本身是2的x次幂,则长度不变;若不是,则修改为大于n的最小2的x次幂
 * </pre>
 * 
 * HashMap的长度是2的幂次方,获得数组下标的公式为hash & (length - 1),需要用到上述求和公式:
 * 
 * <pre>
 * 当length是2的N次幂,hash & (length - 1) == hash % length
 * 要取得数组长度下标,通用方法就是取余,即key的hash%length
 * 如果length是2的N次幂,则取余相当于hash值无符号右移N位,被移除的N个二进制位求和就是余数
 * 二进制与运算中2^N = 2^(N-1) + 2^(N-2) + ... + 1 + 1,见上述求和公式.
 * 在二进制BIT位上,2^N次幂只有N+1位有值(1),减去1之后,刚好就是所有N+1之前的位置有值,且都是1
 * 如2^4 -> 00010000, 2^4 - 1 -> 00001111
 * 从上可见,hash和减去1之后的值进行与运算时,只会取最后N位的值,刚好和取余得到的值相同,真TMD精妙,擦
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2019-05-10 22:01:26
 * @git {@link https://github.com/dreamFlyingFlower}
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