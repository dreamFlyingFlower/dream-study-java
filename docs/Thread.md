# 线程



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