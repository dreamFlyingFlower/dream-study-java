# GC



![](JVM02.png)





# GC算法



## 复制算法

核心思想就是将内存空间分为两块,每次只使用其中一块,在垃圾回收时,将正在使用的内存中的存留对象复制到未被使用的内存块中,之后去清除之前正在使用内存块中所有的对象,反复去交换两个内存的角色,完成垃圾收集

适用于新生代垃圾回收



## 引用计数法

这是个比较古老而经典的垃圾收集算法,核心就是在对象被其他对象引用时计数器加1,而当引用失效时则减1,但是这种方法有非常严重的问题:无法处理循环引用的情况,而且每次进行加减操作比较浪费系统性能



## 标记清除法

标记阶段和清除阶段:在标记阶段,首先通过根节点,标记所有从根节点开始的可达对象.因此,未被标记的对象就是未被引用的垃圾对象.然后,在清除阶段,清除所有未被标记的对象

这种方式也有非常大的弊端,就是空间碎片问题,垃圾回收后的空间不是连续的,不连续的内存空间的工作效率要低于连续的内存空间



## 标记压缩法

标记压缩法在标记清除法基础上做了优化,把存活的对象压缩到内存一端,而后进行垃圾清理

Jvm中老年代就是使用的标记压缩法



## 分代算法

根据对象的特点把内存分为N块,而后根据每个内存的特点使用不同的算法

对于新生代和老年代来说,新生代回收的频率更高,但是每次回收耗时都很短,而老年代回收频率较低,但是耗时比较长,所以应该尽量减少老年代的GC



## 分区算法

将整个内存分为N多个小的独立空间,每个小空间都恶意独立使用,这样细粒度的控制一次回收多少个小空间的那些个小空间,而不是对整个空间进行GC,从而提升性能,并减少GC的停顿时间



## GC停顿

* Java中一种全局暂停的现象,又称STW(Stop The World)
* 垃圾回收器的任务是识别和回收垃圾对象进行内存清理,为了让垃圾回收器可以高效的执行,大部分情况下,会要求系统进入一个停顿的状态.停顿的目的是终止所有应用线程,只有这样系统才不会有新的垃圾产生,同时停顿保证了系统状态在某一个瞬间的一致性,也有益于更好的标记垃圾对象,因此在垃圾回收时,都会产生应用停顿的现象
* 全局停顿,有Java代码停止,native代码可以执行,但不能和JVM交互
* STW多半由于GC引起:Dump线程;死锁检查;堆Dump
* STW长时间服务停顿,没有响应,一旦遇到HA系统,可能引起主备切换,严重危害生产环境



# GC种类



## 串行回收器

* 使用单线程进行垃圾回收.每次回收时,串行回收器只有一个工作线程,对于并行能力较弱的计算机来说,串行回收器的专注性和独占性往往有更好的性能表现.串行回收器可以在新生代和老年代使用.
* -XX:+UseSerialGC:设置新生代和老年代回收器;新生代使用复制算法,老年代使用标记算法



## 并行回收器



### ParNew

* 工作在新生代的垃圾回收器,只是简单的将串行回收器多线程化,回收策略,算法和串行回收器一样
* -XX:+UseParNewGC:新生代并行回收器,老年代串行回收器



### Parallel

* 类似ParNew,新生代复制算法,老年代标记-压缩算法
* -XX:+UseParallelGC:使用Parallel收集器,老年代串行
* -XX:+UseParallelOldGC:使用Parallel收集器,并行老年代
* -XX:ParallelGCThreads:指定ParNew的回收器线程数,一般最好和CPU核心数相当



## CMS回收器

* ConcurrentMarkSweep,并发标记清除,使用的是标记清除法,主要关注系统停顿时间
* -XX:+UseConcMarkSweepGC:设置是否使用该回收器
* -XX:ConcGCThreads:设置并发线程数
* CMS并不是独占的回收器,即CMS回收过程中,应用程序仍然在不停的工作,又会有新的垃圾不断产生,所以在使用CMS的过程中应该确保应用程序的内存足够
* CMS不会等到应用程序饱和的时候采取回收垃圾,而是在某一个阀值的时候开始回收.如果内存使用率增长的很快,在CMS执行过程中已经出现了内存不足的情况,此时回收就会失败,虚拟机将启动老年代串行回收器进行垃圾回收,这会导致应用程序中断,直到垃圾回收完成后才会正常工作.这个过程GC停顿时间可能较长
* -XX:CMSInitiatingOccupancyFraction:指定回收阀值,默认68,即当老年代空间使用率达到68%时,会执行CMS回收
* -XX:+UseCMSCompactAtFullCollection:使用CMS回收器之后,是否进行碎片整理
* -XX:CMSFullGCsBeforeCompaction:设置进行多少次CMS回收之后对内存进行一次压缩
* 优点是尽可能降低停顿,但会影响系统整体吞吐量和性能,而且清理不彻底.因为在清理阶段,用户线程还在运行,会产生新的垃圾,无法清理干净.因为和用户线程一起运行,不能在空间快满时再清理.如果不幸内存预留空间不够,就会引起concurrent mode failure



## G1回收器

* Garbage First,在jdk1.7中提出的垃圾回收器,从长期来看是为了取代CMS回收器,G1回收器拥有独特的垃圾回收策略,G1属于分代垃圾回收器,区分新生代和老年代,依然有eden和from/to区,它不要求整个eden或新生代,老年代的空间都连续,它使用了分区算法
* 并行性:G1回收期间可多线程同时工作
* 并发性:G1拥有与应用程序交替执行能力,部分工作可与应用程序同时执行,在整个GC期间不会完全阻塞应用
* 分代GC:G1依然是一个分代收集器,但是它是兼顾新生代和老年代一起工作,之前的垃圾收集器在新生代,或老年代工作,这是一个很大的不同
* 空间整理:G1在回收过程中,不会像CMS那样在若干次GC后需要进行碎片整理,G1采用了有效复制对象的方式,减少空间碎片
* 可预见性:由于分区的原因,G1可以只选取部分区域进行回收,缩小了回收的范围,提升性能
* 分区回收,优先回收话费时间少,垃圾比例高的区域
* -XX:+UseG1GC:是否使用G1回收器
* -XX:MaxGCPauseMillis:指定最大停顿时间,默认是200ms
* -XX:ParallelGCThreads:设置并行回收的线程数量
* -XX:G1HeapRegionSize:1,2,4,8,16,32,只有这几个值,单位是M.region有多大,该代码是在headpregion.cpp中



## 次收集(Scavenge)

* 新生代GC(Scavenge GC):指发生在新生代的GC,因为新生代的java对象大多数都是朝生夕死的,所以ScavengeGC比较频繁,一般回收速度也比较快.当eden空间不足时,会触发ScavengeGC
* 一般情况下,当新对象生成,并且在eden申请空间失败时,就会触发ScavengeGC,对eden区域进行GC,清除非存活对象,并且把尚且存活的对象移动到Survivor(新生代的from和to)区.然后整理survivor的两个区.这种方式的GC是对年轻代的eden区进行,不会影响到老年代.



## 全收集

* 老年代(Full GC):发生在老年代的GC,出现了Full GC一般会伴随着至少一次的Scavenge GC
* Full GC的速度一般会比Scavenge GC慢10倍以上
* 当老年代内存不足或显示调用system.gc()时,会触发Full GC



## Minor GC

* 对于复制算法来说,当年轻代Eden区域满的时候会触发一次Minor GC,将Eden和From Survivor的对象复制到另外一块To Survivor上
* 如果某个对象存活的时间超过一定Minor gc次数会直接进入老年代,不再分配到To Survivor上(默认15次,对应虚拟机参数 -XX:+MaxTenuringThreshold)



## Full GC

* 用于清理整个堆空间,它的触发条件主要有以下几种:
* 显式调用System.gc方法(建议JVM触发)
* 方法区空间不足(JDK8及之后不会有这种情况了,详见下文)
* 老年代空间不足,引起Full GC.这种情况比较复杂,有以下几种:
  * 大对象直接进入老年代引起,由-XX:PretenureSizeThreshold参数定义
  * 经历多次Minor GC仍存在的对象进入老年代,由-XX:MaxTenuringThreashold定义
  * Minor GC时,动态对象年龄判定机制会将对象提前转移老年代.年龄从小到大进行累加,当加入某个年龄段后,累加和超过survivor区域-XX:TargetSurvivorRatio的时候,从这个年龄段往上的年龄的对象进入老年代
  * Minor GC时,Eden和From Space区向To Space区复制时,大于To Space区可用内存,会直接把对象转移到老年代

* JVM的空间分配担保机制可能会触发Full GC:
  * 在进行Minor GC之前,JVM的空间担保分配机制可能会触发上述老年代空间不足引发的Full GC
  * 空间担保分配是指在发生Minor GC之前,虚拟机会检查老年代最大可用的连续空间是否大于新生代所有对象的总空间
    * 如果大于,则此次Minor GC是安全的
    * 如果小于,则虚拟机会查看HandlePromotionFailure设置值是否允许担保失败
    * 如果HandlePromotionFailure=true,那么会继续检查老年代最大可用连续空间是否大于历次晋升到老年代的对象的平均大小,如果大于,则尝试进行一次Minor GC,但这次Minor GC依然是有风险的,失败后会重新发起一次Full gc:如果小于或者HandlePromotionFailure=false,则改为直接进行一次Full GC



# Jvm参数

* 所有参数示例可参见dream-study-java-common项目的com.wy.jvm包

* -verbose:gc:可以打印GC的简要信息

  ```java
  [GC 4790K->374K(15872K), 0.0001606 secs]
  [GC 4790K->374K(15872K), 0.0001474 secs]
  [GC 4790K->374K(15872K), 0.0001563 secs]
  [GC 4790K->374K(15872K), 0.0001682 secs]
  ```

* -XX:+PrintGC:当虚拟机启动后,只要遇到GC就会打印日志

* -XX:+PrintGCDetails:可以查看详细信息,包括各个区的情况

  ```java
  // eden为新生代伊甸区,from是s0,to是s1,tenured是老年代,compacting是JDK1.8之前的永久代,JDK1.8称为元空间Metaspace
  Heap
   def new generation  total 13824K, used 11223K [0x27e80000,0x28d80000,0x28d80000)
    eden space 12288K, 91% used [0x27e80000, 0x28975f20, 0x28a80000)
    from space 1536K,  0% used [0x28a80000, 0x28a80000, 0x28c00000)
    to   space 1536K,  0% used [0x28c00000, 0x28c00000, 0x28d80000)
   tenured generation  total 5120K, used 0K [0x28d80000, 0x29280000, 0x34680000)
     the space 5120K,  0% used [0x28d80000, 0x28d80000, 0x28d80200, 0x29280000)
   compacting perm gen total 12288K, used 142K [0x34680000, 0x35280000, 0x38680000)
     the space 12288K, 1% used [0x34680000, 0x346a3a90, 0x346a3c00, 0x35280000)
      ro space 10240K, 44% used [0x38680000, 0x38af73f0, 0x38af7400, 0x39080000)
      rw space 12288K, 52% used [0x39080000, 0x396cdd28, 0x396cde00, 0x39c80000)
  ```

* -XX:+PrintGCTimeStamps:打印CG发生的时间戳

* -XX:+PrintHeapAtGC:每次一次GC后,都打印堆信息

* -XX:+TraceClassLoading:监控类的加载

  ```java
  [Loaded java.lang.Object from shared objects file]
  [Loaded java.io.Serializable from shared objects file]
  [Loaded java.lang.Comparable from shared objects file]
  [Loaded java.lang.CharSequence from shared objects file]
  [Loaded java.lang.String from shared objects file]
  [Loaded java.lang.reflect.GenericDeclaration from shared objects file]
  [Loaded java.lang.reflect.Type from shared objects file]
  ```

* -XX:+PrintClassHistogram:按下Ctrl+Break后,打印类的信息

  ```java
   // 分别显示:序号,实例数量,总大小,类型
   num     #instances         #bytes  class name
  ----------------------------------------------
     1:        890617      470266000  [B
     2:        890643       21375432  java.util.HashMap$Node
     3:        890608       14249728  java.lang.Long
     4:            13        8389712  [Ljava.util.HashMap$Node;
     5:          2062         371680  [C
     6:           463          41904  java.lang.Class
  ```

* -Xloggc:filePath:指定GC日志的位置,以文件形式输出

* -Xms:设置java堆的最小值,包括新生和老年代 

* -Xmx:设置java堆的最大值.如-Xmx2048M

* -Xmn:设置新生代大小,一般会设置为整个堆空间的1/3或1/4

* -XX:NewRatio=n:设置新生代和老年代的比值,如为3,表示年轻代:老年代为1:3

* -XX:SurvivorRatio=n:设置新生代中eden和from/to空间比例,如2,表示eden/form=eden/to=2

* -Xss:指定线程的最大栈空间大小,通常只有几百k

* -XX:NewSize=n:设置新生代大小

* -XX:PermSize:设置老年代的初始大小,默认是64M

* -XX:MaxPermSize:设置老年代最大值

* -XX:PretenureSizeThreshold:指定占用内存多少的对象直接进入老年代.由系统计算得出,无默认值

* -XX:MaxTenuringThreshold:默认15,只能设置0-15.指经多少次垃圾回收,对象实例从新生代进入老年代.在JDK8中并不会严格的按照该次数进行回收,又是即使没有达到指定次数仍然会进入老年代

* -XX:+HandlePromotionFailure:空间分配担保.+表示开启,-表示禁用

* -XX:+UseSerialGC:配置年轻代为串行回收器

* -XX:+UseParNewGC:在新生代使用并行收集器

* -XX:+UseParallelGC:设置年轻代为并行收集器

* -XX:+UseParalledlOldGC:设置老年代并行收集器

* -XX:+UseConcMarkSweepGC:新生代使用并行收集器,老年代使用CMS+串行收集器

* -XX:ParallelGCThreads:设置用于垃圾回收的线程数

* -XX:ParallelCMSThreads:设定CMS的线程数量

* -XX:CMSInitiatingOccupancyFraction:CMS收集器在老年代空间被使用多少后触发

* -XX:+UseCMSCompactAtFullCollection:CMS收集器在完成垃圾收集后是否要进行一次内存碎片整理

* -XX:CMSFullGCsBeforeCompaction:设定进行多少次CMS垃圾回收后,进行一次内存压缩

* -XX:+CMSClassUnloadingEnabled:允许对类元数据进行回收

* -XX:CMSInitiatingPermOccupancyFraction:当永久区占用率达到这一百分比时,启动CMS回收

* -XX:UseCMSInitiatingOccupancyOnly:表示只在到达阀值的时候,才进行CMS回收

* -XX:+HeapDumpOnOutOfMemoryError:使用该参数可以在OOM时导出整个堆信息

* -XX:HeapDumpPath=filePath:设置OOM时导出的信息存放地址

* -XX:OnOutOfMemoryError=filePath:在OOM时,执行一个脚本,如发送邮件

* -XX:MaxGCPauseMillis:设置最大垃圾收集停顿时间,可以把虚拟机在GC停顿的时间控制在指定范围内.如果希望减少GC停顿时间,可以将MaxGCPauseMillis设置的很小,但是会导致GC频繁,从而增加了GC的总时间降低了吞吐量,所以需要根据实际情况设置

* -XX:GCTimeRatio:设置吞吐量大小,它是一个0到100之间的整数,默认情况下是99,系统将花费不超过1/(1+n)的时间用于垃圾回收,也就是1/(1+99)=1%的时间.该参数和-XX:MaxGCPauseMillis是矛盾的,因为停顿时间和吞吐量不可能同时调优

* -XX:UseAdaptiveSizePolicy:自适应模式,在这种情况下,新生代的大小,eden,from/to的比例,以及晋升老年代的对象年龄参数会被自动调整,已达到在堆大小,吞吐量和停顿时间之间的平衡



# GC输出

```java
public static void main(String[] args) {
    byte[] b = null;
    for (int i = 0; i < 20; i++) {
        b = new byte[1 * 1024 * 1024];
    }
}
```

```java
// 设置JVM启动参数:-verbose:gc -XX:+PrintGCDetails -XX:+UseSerialGC -Xmx20m -Xms20m -Xmn1m
[GC (Allocation Failure) [DefNew: 896K->63K(960K), 0.0009520 secs] 896K->628K(20416K), 0.0009838 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
[GC (Allocation Failure) [DefNew: 262K->63K(960K), 0.0012891 secs][Tenured: 19079K->1734K(19456K), 0.0012453 secs] 19259K->1734K(20416K), [Metaspace: 2661K->2661K(1056768K)], 0.0025666 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
Heap
 def new generation   total 960K, used 18K [0x00000000fec00000, 0x00000000fed00000, 0x00000000fed00000)
  eden space 896K,   2% used [0x00000000fec00000, 0x00000000fec04920, 0x00000000fece0000)
  from space 64K,   0% used [0x00000000fece0000, 0x00000000fece0000, 0x00000000fecf0000)
  to   space 64K,   0% used [0x00000000fecf0000, 0x00000000fecf0000, 0x00000000fed00000)
 tenured generation   total 19456K, used 3782K [0x00000000fed00000, 0x0000000100000000, 0x0000000100000000)
   the space 19456K,  19% used [0x00000000fed00000, 0x00000000ff0b1b90, 0x00000000ff0b1c00, 0x0000000100000000)
 Metaspace       used 2668K, capacity 4486K, committed 4864K, reserved 1056768K
  class space    used 286K, capacity 386K, committed 512K, reserved 1048576K
```

* JDK8和之前的策略不一样,GC信息也不同
* `STW`:`Stop The World`,表示垃圾收集时是否需要停顿
* `GC (Allocation Failure)`:表明进行了一次垃圾回收,且不需要STW
  * 前面没有Full修饰,表明这是一次Minor GC
  * 它不表示只GC新生代,并且JDK8垃圾回收不管是新生代还是老年代都会STW
  * `Allocation Failure`表明本次引起GC的原因是年轻代中没有足够的空间能够存储新的数据
* `[DefNew:896K->63K(960K),0.0009520 secs] 896K->628K(20416K),0.0009838 secs]`:
  * `DefNew`:表示是新生代发生垃圾回收,这个名称和所使用的收集器密切相关.可以有Tenured,Perm,ParNew,PSYoungGen等等.其中hotspot虚拟机使用的是PSYoungGen代表新生代
  * `896K->63K(960K)`:GC前该区域已使用容量->GC后该区域已使用容量(该内存区域总容量)
  * `0.0009520 secs`:该内存区域GC所占用的时间
  * `896K->628K(20416K)`:GC前Java堆已使用容量->GC后Java堆已使用容量(Java堆总容量)
* `[Tenured: 19079K->1734K(19456K), 0.0012453 secs] 19259K->1734K(20416K)`:
  * `Tenured`:老年代发生垃圾回收
* `[Metaspace: 2661K->2661K(1056768K)], 0.0025666 secs]`:
  * `Metaspace`:元空间发生垃圾回收.JDK1.8之前为compacting perm gen
* `[Times: user=0.00 sys=0.00, real=0.00 secs]`:分别表示用户态耗时,内核态耗时和总耗时





# JVM调优



* Full GC过长,20-30S
  * 减小堆内存大小,但是可以部署多个程序,避免内存浪费
* 不定期内存溢出,把堆内存加大,会加剧溢出.导出堆转储快照信息,没有任何信息.内存监控也正常
  * 该情况可能是NIO使用直接内存时,直接内存过小,而GC又不能控制直接内存,导致内存被撑爆
  * 可以修改JVM的DirectMemory相关参数解决或换更大内存的服务器
* 大量消息从A服务发送到B服务的时候,B服务无法及时处理导致B服务崩溃
  * 在A和B服务之间添加消息队列
