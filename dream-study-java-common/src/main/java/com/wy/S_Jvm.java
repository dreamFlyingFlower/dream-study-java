package com.wy;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;

/**
 * JVM包括:类加载子系统,java栈,方法区,java堆,直接内存,本地方法栈,垃圾回收系统,pc寄存器,执行引擎
 * 
 * 垃圾回收期分为4个区:eden,新生代(from),老年代(to)
 * 
 * @author ParadiseWY
 * @date 2019年3月24日 下午9:28:53
 */
public class S_Jvm {

	/**
	 * JVM内存分代策略:java虚拟机根据对象周期存活的周期不同,把堆内存分为几块,<br>
	 * 一般分为新生代,老年代和永久代(对HotSpot虚拟机而言),不同的区可能采用不同的GC算法<br>
	 * 如果不分代,在垃圾回收时就需要频繁遍历内存中的对象,而遍历所有对象的花费是巨大的. 同时在回收不使用的内存时会产生内存碎片,对内存的利用效率大大降低
	 * 
	 * 堆内存是虚拟机管理内存中最大的一块,也是垃圾回收最频繁的,程序所有对象的实例都放在堆中. 给堆分代是为了提高对象内存分配和垃圾回收的效率.
	 * 
	 * 新创建的对象会在新生代中分配内存,经过多次回收仍然存活下来的对象将存放到老年代中.<br>
	 * 静态属性,常量,类信息等存放在永久代中,7之前常量池放在永久代中,7之后常量池从新生代中移出. 新生代中的对象存活时间短,只需要在新生代中频繁的GC,
	 * 老年代中对象生命周期长,不需要频繁GC,永久代中回收效果太差,一般不进行垃圾回收.<br>
	 * 
	 * HotSpot将java堆中的年轻代大致分为3部分:Eden,From,To;From和To又可统称为SurvivorSpaces.<br>
	 * 大部分的对象创建都在Eden,而垃圾回收在该区可回收70%-95%左右的内存,大对象直接进入老年代<br>
	 * Eden较大,Survivor较小,默认比例为8:1:1,划分的目的是因为HotSpot采用复制算法回收新生代.<br>
	 * 当Eden没有足够的空间运算时,虚拟机将发起一次GC.GC开始时,对象只会存在Eden和From Survivor, To<br>
	 * Survivor是空的,作为保留区域.GC进行时,Eden中所有存活的对象都会复制到To中,而在From中,<br>
	 * 仍存活的对象会根据他们的年龄决定去To还是去老年代,年龄达到阀值(默认15,新生代回收一次+1)会进入老年代<br>
	 * 之后,From和To会交换他们的角色,下次GC回收的时候就会回收Eden和To,而From会作为保留区.<br>
	 * 如果To没有足够的空间存放GC之后没有被回收的对象,就需要老年代来存放<br>
	 */

	/**
	 * Java栈:<br>
	 * 描述的是方法执行的内存模型,每个方法被调用都会创建一个栈帧来存储局部变量,操作数,方法出口等<br>
	 * JVM为每个线程创建一个栈,用于存放该线程执行方法的信息(实际参数,局部变量等)<br>
	 * 栈属于线程私有,不能实现线程间共享.线程执行完毕,栈就清除<br>
	 * 栈中存储各种变量,方法等的数据结构,其实就是一个栈(Stack)<br>
	 * 栈是由系统自动分配,速度快,是一个连续的内存空间
	 * 
	 * 堆: 堆用于存储创建好的对象和数组,堆只有一个,所有线程共享,堆是一个不连续的内存空间,分配灵活,访问慢
	 * 
	 * 方法区:<br>
	 * JDK7之前方法区是永久代,JDK7将方法区中常量池,静态变量都放到了堆内存中,JDK8是元空间和堆结合<br>
	 * JVM只有一个方法区,被所有线程共享<br>
	 * 方法区也是堆,只是用于存储类,常量相关信息<br>
	 * 用来存放程序中永远不变或唯一的内容(类信息,静态变量,字符串常量)
	 */

	/**
	 * 类的加载:加载,连接(验证,准备,解析),初始化使用,卸载;class的字节码文件是放在堆中的<br>
	 * 在类加载的过程中,加载的顺序:静态成员,非静态;按照代码的顺序执行; 若是定义了该对象,则初始化静态和非静态变量后,初始化构造函数
	 */

	/**
	 * Java agent:主要作用是在加载class字节码文件之前,对其进行拦截,用来插入自定义的字节码 javap -c<br>
	 * test.class:将class文件反编译成jvm指令,而class实际上就是在jvm中使用指令.操作jvm的指令,有2种方法<br>
	 * ASM:需要会操作字节码指令,学习成本较高 javassit:日本开发一个类库,简单易用
	 */

	public static void main(String[] args) {
		// 获取当前虚拟机GC的信息
		List<GarbageCollectorMXBean> beans = ManagementFactory.getGarbageCollectorMXBeans();
		for (GarbageCollectorMXBean garbageCollectorMXBean : beans) {
			System.out.println(garbageCollectorMXBean.getName());
		}
	}
}