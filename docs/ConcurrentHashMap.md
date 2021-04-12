# ConcurrentHashMap源码解析

[TOC]

## jdk8之前的实现原理

## jdk8的实现原理

JDK8的实现已经抛弃了Segment分段锁机制，利用CAS+Synchronized来保证并发更新的安全，底层依然采用数组+链表+红黑树的存储结构。

## 变量解释

1. table：默认为null，初始化发生在第一次插入操作，默认大小为16的数组，用来存储Node节点数据，扩容时大小总是2的幂次方。
2. nextTable：默认为null，扩容时新生成的数组，其大小为原数组的两倍。
3. sizeCtl ：默认为0，用来控制table的初始化和扩容操作，具体应用在后续会体现出来。

    * -1 代表table正在初始化
    * -N 表示有N-1个线程正在进行扩容操作
    * 其余情况：
       * 1、如果table未初始化，表示table需要初始化的大小。
      * 2、如果table初始化完成，表示table的容量，默认是table大小的0.75倍，居然用这个公式算0.75（n - (n >>> 2)）。

4. Node：保存key，value及key的hash值的数据结构。

5. ForwardingNode：一个特殊的Node节点，hash值为-1，其中存储nextTable的引用。只有table发生扩容的时候，ForwardingNode才会发挥作用，作为一个占位符放在table中表示当前节点为null或则已经被移动。

## 初始化

实例化ConcurrentHashMap时带参数时，会根据参数调整table的大小，假设参数为100，最终会调整成256，确保table的大小总是2的幂次方。

```java
private static final int tableSizeFor(int c) {  
    int n = c - 1;  
    n |= n >>> 1;  
    n |= n >>> 2;  
    n |= n >>> 4;  
    n |= n >>> 8;  
    n |= n >>> 16;  
    return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;  
}  
```

### 初始化table

```java
    private final Node<K,V>[] initTable() {  
        Node<K,V>[] tab; int sc;  
        while ((tab = table) == null || tab.length == 0) {  
        //如果一个线程发现sizeCtl<0，意味着另外的线程执行CAS操作成功，当前线程只需要让出cpu时间片  
            if ((sc = sizeCtl) < 0)   
                Thread.yield(); // lost initialization race; just spin  
            else if (U.compareAndSwapInt(this, SIZECTL, sc, -1)) {  
                try {  
                    if ((tab = table) == null || tab.length == 0) {  
                        int n = (sc > 0) ? sc : DEFAULT_CAPACITY;  
                        @SuppressWarnings("unchecked")  
                        Node<K,V>[] nt = (Node<K,V>[])new Node<?,?>[n];  
                        table = tab = nt;  
                        sc = n - (n >>> 2);  
                    }  
                } finally {  
                    sizeCtl = sc;  
                }  
                break;  
            }  
        }  
        return tab;  
    }  
```

## put操作

```java
    final V putVal(K key, V value, boolean onlyIfAbsent) {  
        if (key == null || value == null) throw new NullPointerException();  
        int hash = spread(key.hashCode());  
        int binCount = 0;  
        for (Node<K,V>[] tab = table;;) {  
            Node<K,V> f; int n, i, fh;  
            if (tab == null || (n = tab.length) == 0)  
                tab = initTable();  
            // table中定位索引位置，n是table的大小
            // 如果f为null，说明table中这个位置第一次插入元素，利用Unsafe.compareAndSwapObject方法插入Node节点。
            else if ((f = tabAt(tab, i = (n - 1) & hash)) == null) {  
                
                // 如果CAS成功，说明Node节点已经插入，随后addCount(1L,binCout)方法会检查当前容量是否需要进行扩容。如果CAS失败，说明有其它线程提前插入了节点，自旋重新尝试在这个位置插入节点。
                if (casTabAt(tab, i, null, new Node<K,V>(hash, key, value, null)))  
                    break; // no lock when adding to empty bin  
            }  
            // 如果f的hash值为-1，说明当前f是ForwardingNode节点，意味有其它线程正在扩容，则一起进行扩容操作。
            else if ((fh = f.hash) == MOVED)  
                tab = helpTransfer(tab, f);  
            //省略部分代码  
        }  
        addCount(1L, binCount);  
        return null;  
    }  
```

### hash算法

```
static final int spread(int h) {return (h ^ (h >>> 16)) & HASH_BITS;}
```

### 获取table中对应的元素f

```java
static final <K,V> Node<K,V> tabAt(Node<K,V>[] tab, int i) {
    return (Node<K,V>)U.getObjectVolatile(tab, ((long)i << ASHIFT) + ABASE);
}
```

Doug Lea采用Unsafe.getObjectVolatile来获取，也许有人质疑，直接table[index]不可以么，为什么要这么复杂？
在java内存模型中，我们已经知道每个线程都有一个工作内存，里面存储着table的副本，虽然table是volatile修饰的，但不能保证线程每次都拿到table中的最新元素，Unsafe.getObjectVolatile可以直接获取指定内存的数据，保证了每次拿到数据都是最新的。

### 链表或红黑树操作

其余情况把新的Node节点按链表或红黑树的方式插入到合适的位置，这个过程采用同步内置锁实现并发。

```java
synchronized (f) {
    // 在节点f上进行同步，节点插入之前，再次利用tabAt(tab, i) == f判断，防止被其它线程修改。 
    if (tabAt(tab, i) == f) {
        // 如果f.hash >= 0，说明f是链表结构的头结点，遍历链表，如果找到对应的node节点，则修改value，否则在链表尾部加入节点。
        if (fh >= 0) {
            binCount = 1;
            for (Node<K,V> e = f;; ++binCount) {
                K ek;
                if (e.hash == hash &&
                    ((ek = e.key) == key ||
                     (ek != null && key.equals(ek)))) {
                    oldVal = e.val;
                    if (!onlyIfAbsent)
                        e.val = value;
                    break;
                }
                Node<K,V> pred = e;
                if ((e = e.next) == null) {
                    pred.next = new Node<K,V>(hash, key,
                                              value, null);
                    break;
                }
            }
        }
        // 如果f是TreeBin类型节点，说明f是红黑树根节点，则在树结构上遍历元素，更新或增加节点。
        else if (f instanceof TreeBin) {
            Node<K,V> p;
            binCount = 2;
            if ((p = ((TreeBin<K,V>)f).putTreeVal(hash, key,
                                           value)) != null) {
                oldVal = p.val;
                if (!onlyIfAbsent)
                    p.val = value;
            }
        }
    }
}
// 如果链表中节点数binCount >= TREEIFY_THRESHOLD(默认是8)，则把链表转化为红黑树结构。
if (binCount != 0) {
    if (binCount >= TREEIFY_THRESHOLD)
        treeifyBin(tab, i);
    if (oldVal != null)
        return oldVal;
    break;
}
```

## table 扩容

当table容量不足的时候，即table的元素数量达到容量阈值sizeCtl，需要对table进行扩容。

整个扩容分为两部分：

1. 构建一个nextTable，大小为table的两倍。
2. 把table的数据复制到nextTable中。

这两个过程在单线程下实现很简单，但是ConcurrentHashMap是支持并发插入的，扩容操作自然也会有并发的出现，这种情况下，第二步可以支持节点的并发复制，这样性能自然提升不少，但实现的复杂度也上升了一个台阶。

先看第一步，构建nextTable，毫无疑问，这个过程只能只有单个线程进行nextTable的初始化，具体实现如下：

```java
    private final void addCount(long x, int check) {  
        // 省略部分代码  
        if (check >= 0) {  
            Node<K,V>[] tab, nt; int n, sc;  
            while (s >= (long)(sc = sizeCtl) && (tab = table) != null &&  
                   (n = tab.length) < MAXIMUM_CAPACITY) {  
                int rs = resizeStamp(n);  
                if (sc < 0) {  
                    if ((sc >>> RESIZE_STAMP_SHIFT) != rs || sc == rs + 1 ||  
                        sc == rs + MAX_RESIZERS || (nt = nextTable) == null ||  
                        transferIndex <= 0)  
                        break;  
                    if (U.compareAndSwapInt(this, SIZECTL, sc, sc + 1))  
                        transfer(tab, nt);  
                }  
                else if (U.compareAndSwapInt(this, SIZECTL, sc,  
                                             (rs << RESIZE_STAMP_SHIFT) + 2))  
                    transfer(tab, null);  
                s = sumCount();  
            }  
        }  
    }  
```

## get操作

```java
public V get(Object key) {
    Node<K,V>[] tab; Node<K,V> e, p; int n, eh; K ek;
    int h = spread(key.hashCode());
    if ((tab = table) != null && (n = tab.length) > 0 &&
        (e = tabAt(tab, (n - 1) & h)) != null) {
        if ((eh = e.hash) == h) {
            if ((ek = e.key) == key || (ek != null && key.equals(ek)))
                return e.val;
        }
        else if (eh < 0) // 树
            return (p = e.find(h, key)) != null ? p.val : null;
        while ((e = e.next) != null) { // 链表
            if (e.hash == h &&
                ((ek = e.key) == key || (ek != null && key.equals(ek))))
                return e.val;
        }
    }
    return null;
}
```