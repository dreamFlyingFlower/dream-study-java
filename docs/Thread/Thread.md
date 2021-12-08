# 线程



# CPU缓存

* CPU核心(寄存器,多个)速度->CPU缓存->主存
* CPU的频率太快了,快到主存跟不上.这样在处理器时钟周期内,CPU常常需要等待主存,浪费资源
* CPU缓存的出现是为了缓解CPU和内存之间速度不匹配的问题
* CPU缓存的时间局限性:如果某个数据被访问,将来某个时间可能再次被访问
* CPU缓存的空间局限性:如果某个数据被访问,这个数据旁边的数据也可能很快被访问



## CPU MESI

* CPU缓存一致性,用于保证多个CPU缓存之间缓存共享数据的一致性
* M表示被修改状态,数据在当前CPU缓存中,且是被修改过的,与主存中的数据不一致.改缓存中的数据需要再未来的某个时间点写到主存中,这个时间是其他CPU读取内存中的相关数据之前.当写回到主存中之后,改缓存数据将变为独享状态
* E表示独享,缓存在CPU中且是没有被修改的,与主存中的数据一致.该缓存数据可以在任意时刻被其他CPU读取,并且会将改数据的状态变为共享状态.当有其他CPU修改该值时,该缓存会变为M状态
* S表示共享,多个CPU都可以缓存,且和主存中的数据是一致的.当有一个CPU修改了该缓存数据,其他CPU中的该数据是可以被作废的,变成无效状态
* I表示无效,可能是其他CPU修改了缓存数据
* local read:操作缓存,读CPU本地缓存中的数据
* local write:将数据写到本地缓存中
* remote read:读主存中的数据
* remote write:将数据写到主存中



## CPU寄存器与主存

* 通常CPU要修改主存中的数据时,会先将主存中的数据读到CPU缓存中,也可能读到CPU寄存器中
* 修改完之后再将数据回写到CPU缓存中,在某个节点同步到主存中
* JVM中的每个线程都有一个私有的本地内存,都是先从主存中读数据,操作完成之后再将数据回写到主存中
* 若多个线程同时读取一个主存中的变量,若不加锁,会造成主存中变量的值和预期值不同的结果



# 同步操作



## Lock

* 锁定:作用于主内存的变量,把一个变量标识为一条线程独占状态



## Unlock

* 解锁:作用于主内存的变量,把一个处于锁定状态的变量释放出来,释放后的变量才可以被其他线程锁定



## Read

* 读取:作用于主内存的变量,把一个变量值从主内存传输到线程的工作内存中,以便随后的load操作



## Use

* 使用:作用于工作内存的变量,把工作内存中的一个变量值传递给执行引擎



## Assign

* 赋值:作用于工作内存的变量,它把一个从执行引擎接收到的值赋值给工作内存的变量



## Store

* 存储:作用于工作内存的变量,把工作内存中的一个变量值传递到主内存中,以便随后的write操作



## Write

* 写入:作用于主内存的变量,它把store操作从工作内存中一个变量的值传送到主存变量中



# 同步规则



* 如果要把一个变量从主内存中复制到工作内存,就需要按顺序地执行read和load操作,如果把变量从工作内存中同步到主内存中,就要按顺序地执行store和write操作.但Java内存模型只要求上述操作必须按顺序执行,而没有保证必须是连续操作
* 不允许read和load,store和write操作之一单独出现
* 不允许一个线程丢弃它的最近assign的操作,即变量在工作内存中改变了之后必须同步到主内存中
* 不允许一个线程无原因地(没有发生过任何assign操作)把数据从工作内存同步到主存中
* 一个新的变量只能在主内存中诞生,不允许在工作内存中直接使用一个未被初始化(load或assign)的变量.即就是对一个变量实施use和store操作之前,必须先执行过了assign和load操作
* 一个变量在同一个时刻只允许一条线程对其进行lock操作,但lock操作可以被同一条线程重复执行多次,多次执行lock后,只有执行相同次数的unlock操作,变量才会被解锁.lock和unlock必须成对出现
* 如果对一个变量执行lock操作,将会清空工作内存中此变量的值,在执行引擎使用这个变量前需要重新执行load和assign操作初始化变量的值
* 如果一个变量实现没有被lock操作锁定,则不允许对它进行unlock操作,也不允许去unlock一个被其他线程锁定的变量
* 对一个变量执行unlock操作之前,必须先把该变量同步到主存中(执行store和write操作)





# 锁



* 内置于JVM中的获取锁的优化方法和获取锁的步骤
  * 偏向锁可用会先尝试偏向锁
  * 轻量级锁可用会先尝试轻量级锁
  * 以上都失败,尝试自旋锁
  * 再失败,尝试普通锁,使用OS互斥量在操作系统层挂起



## 对象头Mark

* Mark Word,对象头的标记,32位
* 描述对象的hash,锁信息,垃圾回收标记,年龄
  * 指向锁记录的指针
  * 指向monitor的指针
  * GC标记
  * 偏向锁线程ID



## 偏向锁

* 大部分情况是没有竞争的,所以可以通过偏向来提高性能
* 所谓的偏向,就是偏心,即锁会偏向于当前已经占有锁的线程
* 将对象头Mark的标记设置为偏向,并将线程ID写入对象头Mark
* 只要没有竞争,获得偏向锁的线程,在将来进入同步块,不需要做同步
* 当其他线程请求相同的锁时,偏向模式结束
* -XX:+UseBiasedLocking:默认启用
* 在竞争激烈的场合,偏向锁会增加系统负担



## 轻量级锁

* 普通的锁处理性能不够理想,轻量级锁是一种快速的锁定方法
* 如果对象没有被锁定
  * 将对象头的Mark指针保存到锁对象中
  * 将对象头设置为指向锁的指针(在线程栈空间中)

```java
lock->set_displaced_header(mark);
 if (mark == (markOop) Atomic::cmpxchg_ptr(lock, obj()->mark_addr(), mark)) {
      TEVENT (slow_enter: release stacklock) ;
      return ;
}
```

* 如果轻量级锁失败,表示存在竞争,升级为重量级锁(常规锁)
* 在没有锁竞争的前提下,减少传统锁使用OS互斥量产生的性能损耗
* 在竞争激烈时,轻量级锁会多做很多额外操作,导致性能下降



## 自旋锁

* 当竞争存在时,如果线程可以很快获得锁,那么可以不在OS层挂起线程,让线程做几个空操作(自旋)
* JDK1.6中-XX:+UseSpinning开启
* JDK1.7中,去掉此参数,改为内置实现
* 如果同步块很长,自旋失败,会降低系统性能
* 如果同步块很短,自旋成功,节省线程挂起切换时间,提升系统性能



## 锁优化

* 减少锁持有时间
* 减小锁粒度
  * 将大对象,拆成小对象,大大增加并行度,降低锁竞争
  * 偏向锁,轻量级锁成功率提高
  * ConcurrentHashMap
    * 若干个Segment:Segment<K,V>[] segments
    * Segment中维护HashEntry<K,V>
    * put操作时,先定位到Segment,锁定一个Segment,执行put
    * JDK8以后使用CAS
  * HashMap的同步实现
    * Collections.synchronizedMap(Map<K,V> m)
    * 返回SynchronizedMap对象



## 锁分离

* 根据功能进行锁分离
* ReadWriteLock
* 读多写少的情况,可以提高性能
* 读锁:其他线程读可以,但是写不可以
* 写锁:其他线程读写都不可以



## 无锁

* 锁是悲观的操作,无锁是乐观的操作
* 无锁的一种实现方式
  * CAS(Compare And Swap):java.util.concurrent包下的类基本都是该模式,是系统底层实现
  * 非阻塞的同步
  * CAS(V,E,N)
* 在应用层面判断多线程的干扰,如果有干扰,则通知线程重试



# ThreadPoolExecutor



## execute()

```java
/**
 * 进行下面三步
 *
 * 1.若运行的线程小于corePoolSize,则尝试使用用户定义的Runnalbe对象创建一个新的线程
 * 调用addWorker()会原子性的检查runState和workCount,通过返回false来防止在不应
 * 该添加线程时添加了线程
 * 2.若一个任务能成功进入队列,在添加一个线程时仍需进行双重检查(因为在前一次检查后该线程死亡了),
 * 或者当进入到此方法时,线程池已经shutdown了,所以需要再次检查状态,
 * 若有必要,当停止时还需要回滚入队列操作,或者当线程池没有线程时需要创建一个新线程
 * 3.若无法进入队列,那么需要增加一个新线程,如果此操作失败,那么就意味着线程池已经shutdown
 * 或者已经饱和了,所以拒绝任务
 */
public void execute(Runnable command) {
    if (command == null)
        throw new NullPointerException();
    // 获取线程池控制状态
    int c = ctl.get();
    // worker数量小于corePoolSize
    if (workerCountOf(c) < corePoolSize) {
        // 添加worker
        if (addWorker(command, true))
            // 成功则返回
            return;
        // 不成功则再次获取线程池控制状态
        c = ctl.get();
    }
    // 线程池处于RUNNING状态,将用户自定义的Runnable对象添加进workQueue队列
    if (isRunning(c) && workQueue.offer(command)) {
        // 再次检查,获取线程池控制状态
        int recheck = ctl.get();
        // 线程池不处于RUNNING状态,将自定义任务从workQueue队列中移除
        if (! isRunning(recheck) && remove(command))
            // 拒绝执行命令
            reject(command);
        // worker数量等于0
        else if (workerCountOf(recheck) == 0) 
            // 添加worker
            addWorker(null, false);
    }
    // 添加worker失败
    else if (!addWorker(command, false)) 
        // 拒绝执行命令
        reject(command);
}
```



## addWorker()

1. 原子性的增加workerCount

2.  将用户给定的任务封装成为一个worker,并将此worker添加进workers集合中

3. 启动worker对应的线程,并启动该线程,运行worker的run方法

4. 回滚worker的创建动作,即将worker从workers集合中删除,并原子性的减少workerCount

```java
private boolean addWorker(Runnable firstTask, boolean core) {
    retry:
    // 外层无限循环
    for (;;) {
        // 获取线程池控制状态
        int c = ctl.get();
        // 获取状态
        int rs = runStateOf(c);
        // Check if queue empty only if necessary.
        if (rs >= SHUTDOWN &&// 状态大于等于SHUTDOWN,初始的ctl为RUNNING,小于SHUTDOWN
            ! (rs == SHUTDOWN &&// 状态为SHUTDOWN
               firstTask == null &&// 第一个任务为null
               ! workQueue.isEmpty()))// worker队列不为空
            // 返回
            return false;
        for (;;) {
            // worker数量
            int wc = workerCountOf(c);
            if (wc >= CAPACITY ||// worker数量大于等于最大容量
                wc >= (core ? corePoolSize : maximumPoolSize))// worker数量大于等于核心线程池大小或者最大线程池大小
                return false;
            if (compareAndIncrementWorkerCount(c))// 比较并增加worker的数量
                // 跳出外层循环
                break retry;
            // 获取线程池控制状态
            c = ctl.get();  // Re-read ctl
            if (runStateOf(c) != rs) // 此次的状态与上次获取的状态不相同
                // 跳过剩余部分,继续循环
                continue retry;
            // else CAS failed due to workerCount change; retry inner loop
        }
    }
    // worker开始标识
    boolean workerStarted = false;
    // worker被添加标识
    boolean workerAdded = false;
    Worker w = null;
    try {
        // 初始化worker
        w = new Worker(firstTask);
        // 获取worker对应的线程
        final Thread t = w.thread;
        if (t != null) { // 线程不为null
            // 线程池锁
            final ReentrantLock mainLock = this.mainLock;
            // 获取锁
            mainLock.lock();
            try {
                // Recheck while holding lock.
                // Back out on ThreadFactory failure or if
                // shut down before lock acquired.
                // 线程池的运行状态
                int rs = runStateOf(ctl.get());
                // 小于SHUTDOWN或等于SHUTDOWN并且firstTask为null
                if (rs < SHUTDOWN ||(rs == SHUTDOWN && firstTask == null)) {
                    // 线程刚添加进来,还未启动就存活
                    if (t.isAlive())
                        // 抛出线程状态异常
                        throw new IllegalThreadStateException();
                    // 将worker添加到worker集合
                    workers.add(w);
                    // 获取worker集合的大小
                    int s = workers.size();
                    // 队列大小大于largestPoolSize
                    if (s > largestPoolSize)
                        // 重新设置largestPoolSize
                        largestPoolSize = s;
                    // 设置worker已被添加标识
                    workerAdded = true;
                }
            } finally {
                // 释放锁
                mainLock.unlock();
            }
            // worker被添加
            if (workerAdded) {
                // 开始执行worker的run方法
                t.start();
                // 设置worker已开始标识
                workerStarted = true;
            }
        }
    } finally {
        // worker没有开始
        if (! workerStarted)
            // 添加worker失败
            addWorkerFailed(w);
    }
    return workerStarted;
}
```



## runWorker()

* 实际执行给定任务(即调用用户重写的run方法),并且当给定任务完成后,会继续从阻塞队列中取任务,直到阻塞队列为空(即任务全部完成).在执行给定任务时,会调用钩子函数,利用钩子函数可以完成用户自定义的一些逻辑.在runWorker中会调用到getTask函数和processWorkerExit钩子函数

```java
final void runWorker(Worker w) {
    // 获取当前线程
    Thread wt = Thread.currentThread();
    // 获取w的firstTask
    Runnable task = w.firstTask;
    // 设置w的firstTask为null
    w.firstTask = null;
    // 释放锁(设置state为0,允许中断)
    w.unlock(); // allow interrupts
    boolean completedAbruptly = true;
    try {
        // 任务不为null或者阻塞队列还存在任务
        while (task != null || (task = getTask()) != null) {
            // 获取锁
            w.lock();
            // If pool is stopping, ensure thread is interrupted;
            // if not, ensure thread is not interrupted.  This
            // requires a recheck in second case to deal with
            // shutdownNow race while clearing interrupt
            // 线程池的运行状态至少应该高于STOP
            if ((runStateAtLeast(ctl.get(), STOP) ||
                 // 线程被中断并再次检查,线程池的运行状态至少应该高于STOP
                (Thread.interrupted() && runStateAtLeast(ctl.get(), STOP)))
                // wt线程(当前线程)没有被中断
                && !wt.isInterrupted())
                // 中断wt线程(当前线程)
                wt.interrupt();                            
            try {
                // 在执行之前调用钩子函数
                beforeExecute(wt, task);
                Throwable thrown = null;
                try {
                    // 运行给定的任务
                    task.run();
                } catch (RuntimeException x) {
                    thrown = x; throw x;
                } catch (Error x) {
                    thrown = x; throw x;
                } catch (Throwable x) {
                    thrown = x; throw new Error(x);
                } finally {
                    // 执行完后调用钩子函数
                    afterExecute(task, thrown);
                }
            } finally {
                task = null;
                // 增加给worker完成的任务数量
                w.completedTasks++;
                // 释放锁
                w.unlock();
            }
        }
        completedAbruptly = false;
    } finally {
        // 处理完成后,调用钩子函数
        processWorkerExit(w, completedAbruptly);
    }
}
```



## getTask()

* 用于从workerQueue阻塞队列中获取Runnable对象,由于是阻塞队列,所以支持有限时间等待(poll)和无限时间等待(take).在该函数中还会响应shutDown和、shutDownNow函数的操作,若检测到线程池处于SHUTDOWN或STOP状态,则会返回null,而不再返回阻塞队列中的Runnalbe对象

```java
private Runnable getTask() {
    boolean timedOut = false; // Did the last poll() time out?
    // 无限循环,确保操作成功
    for (;;) {
        // 获取线程池控制状态
        int c = ctl.get();
        // 运行的状态
        int rs = runStateOf(c);
        // Check if queue empty only if necessary
        // 大于等于SHUTDOWN(表示调用了shutDown)并且(大于等于STOP(调用了shutDownNow)或者worker阻塞队列为空)
        if (rs >= SHUTDOWN && (rs >= STOP || workQueue.isEmpty())) {
            // 减少worker的数量
            decrementWorkerCount();
            // 返回null,不执行任务
            return null;
        }
        // 获取worker数量
        int wc = workerCountOf(c);
        // Are workers subject to culling?
        // 是否允许coreThread超时或者workerCount大于核心大小
        boolean timed = allowCoreThreadTimeOut || wc > corePoolSize;
		// worker数量大于maximumPoolSize
        if ((wc > maximumPoolSize || (timed && timedOut))
            // workerCount大于1或worker阻塞队列为空(在阻塞队列不为空时,需要保证至少有一个wc)
            && (wc > 1 || workQueue.isEmpty())) {
            // 比较并减少workerCount
            if (compareAndDecrementWorkerCount(c))
                // 返回null,不执行任务,该worker会退出
                return null;
            // 跳过剩余部分,继续循环
            continue;
        }

        try {
            Runnable r = timed ?
                // 等待指定时间
                workQueue.poll(keepAliveTime, TimeUnit.NANOSECONDS) : 
             // 一直等待,直到有元素
            workQueue.take();                                       
            if (r != null)
                return r;
            // 等待指定时间后,没有获取元素,则超时
            timedOut = true;
        } catch (InterruptedException retry) {
            // 抛出了被中断异常,重试,没有超时
            timedOut = false;
        }
    }
}
```



## processWorkerExit()

* 在worker退出时调用到的钩子函数,而引起worker退出的主要因素如下
  * 阻塞队列已经为空,即没有任务可以运行了
  * 调用了shutDown()或shutDownNow()
* 此方法会根据是否中断了空闲线程来确定是否减少workerCount的值,并且将worker从workers集合中移除并且会尝试终止线程池

```java
private void processWorkerExit(Worker w, boolean completedAbruptly) {
    // 如果被中断,则需要减少workCount
    if (completedAbruptly)
        decrementWorkerCount();
    // 获取可重入锁
    final ReentrantLock mainLock = this.mainLock;
    // 获取锁
    mainLock.lock();
    try {
        // 将worker完成的任务添加到总的完成任务中
        completedTaskCount += w.completedTasks;
        // 从workers集合中移除该worker
        workers.remove(w);
    } finally {
        // 释放锁
        mainLock.unlock();
    }
    // 尝试终止
    tryTerminate();
    // 获取线程池控制状态
    int c = ctl.get();
    // 小于STOP的运行状态
    if (runStateLessThan(c, STOP)) {
        if (!completedAbruptly) {
            int min = allowCoreThreadTimeOut ? 0 : corePoolSize;
            // 允许核心超时并且workQueue阻塞队列不为空
            if (min == 0 && ! workQueue.isEmpty())
                min = 1;
            // workerCount大于等于min
            if (workerCountOf(c) >= min) 
                // 直接返回
                return;
        }
        // 添加worker
        addWorker(null, false);
    }
}
```



## shutdown()

```java
public void shutdown() {
    final ReentrantLock mainLock = this.mainLock;
    mainLock.lock();
    try {
        // 检查shutdown权限
        checkShutdownAccess();
        // 设置线程池控制状态为SHUTDOWN
        advanceRunState(SHUTDOWN);
        // 中断空闲worker
        interruptIdleWorkers();
        // 调用shutdown钩子函数
        onShutdown(); // hook for ScheduledThreadPoolExecutor
    } finally {
        mainLock.unlock();
    }
    // 尝试终止
    tryTerminate();
}
```



## tryTerminate()

```java
final void tryTerminate() {
    // 无限循环,确保操作成功
    for (;;) {
        // 获取线程池控制状态
        int c = ctl.get();
        // 线程池的运行状态为RUNNING
        if (isRunning(c) ||
            // 线程池的运行状态最小要大于TIDYING
            runStateAtLeast(c, TIDYING) ||
            // 线程池的运行状态为SHUTDOWN并且workQueue队列不为null
            (runStateOf(c) == SHUTDOWN && ! workQueue.isEmpty()))
            // 不能终止,直接返回
            return;
        // 线程池正在运行的worker数量不为0
        if (workerCountOf(c) != 0) { 
            // 仅仅中断一个空闲的worker
            interruptIdleWorkers(ONLY_ONE);
            return;
        }
        // 获取线程池的锁
        final ReentrantLock mainLock = this.mainLock;
        // 获取锁
        mainLock.lock();
        try {
            // 比较并设置线程池控制状态为TIDYING
            if (ctl.compareAndSet(c, ctlOf(TIDYING, 0))) { 
                try {
                    // 终止,钩子函数
                    terminated();
                } finally {
                    // 设置线程池控制状态为TERMINATED
                    ctl.set(ctlOf(TERMINATED, 0));
                    // 释放在termination条件上等待的所有线程
                    termination.signalAll();
                }
                return;
            }
        } finally {
            // 释放锁
            mainLock.unlock();
        }
        // else retry on failed CAS
    }
}
```



## interruptIdleWorkers()

```java
private void interruptIdleWorkers(boolean onlyOne) {
    // 线程池的锁
    final ReentrantLock mainLock = this.mainLock;
    // 获取锁
    mainLock.lock();
    try {
        for (Worker w : workers) { // 遍历workers队列
            // worker对应的线程
            Thread t = w.thread;
            // 线程未被中断并且成功获得锁
            if (!t.isInterrupted() && w.tryLock()) { 
                try {
                    // 中断线程
                    t.interrupt();
                } catch (SecurityException ignore) {
                } finally {
                    // 释放锁
                    w.unlock();
                }
            }
            if (onlyOne) // 若只中断一个,则跳出循环
                break;
        }
    } finally {
        // 释放锁
        mainLock.unlock();
    }
}
```



# Volatile

* 保证线程之间变量可见性,但不保证原子性,必须是同一把锁的线程之间才能使用,可以在某些地方替代synchronized
* 程序启动时,类的字节码会加载到线程工作空间中,而CPU会将这些信息从工作空间加载到CPU缓存中,并且首先从CPU缓存中读数据
 * 当其他线程在其工作空间中对某个变量进行修改时,并不会立即将结果回写到内存中,而是继续在cpu缓存中停留一段时间
 * 当该变量使用volatile修饰后,如果被其他线程修改,MESI协议会将该变量的地址无效,同时将新的数据写到CPU缓存中.其他用到该变量的线程在使用时需要重新从内存中读取,保证数据的一致性



# 并行



## Amdahl定律

* 阿姆达尔定律,定义了串行系统并行化后的加速比的计算公式和理论上限
  * 加速比定义:加速比=优化前系统耗时/优化后系统耗时
* 增加CPU处理器的数量并不一定能起到有效的作用,提高系统内可并行化的模块比重,合理增加并行处理器数量,才能以最小的投入,得到最大的加速比



## Gustafson定律

* 古斯塔夫森定律:说明处理器个数,串行比和加速比之间的关系,只要有足够的并行化,那么加速比和CPU个数成正比

  ```
  优化前执行时间=串行时间a+并行时间b
  优化后执行时间=串行时间a+处理器个数n*并行时间b
  加速比=(a+nb)/(a+b)
  定义串行比例F=a/(a+b)
  加速比S(n)=(a+nb)/(a+b)=a/(a+b)+nb/(a+b)=F+n((a+b-a)/(a+b))=n-F(n-1)
  ```