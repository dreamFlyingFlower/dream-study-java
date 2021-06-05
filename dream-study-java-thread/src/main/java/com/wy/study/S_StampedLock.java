package com.wy.study;import java.util.concurrent.locks.ReadWriteLock;import java.util.concurrent.locks.StampedLock;/** * {@link StampedLock}:{@link ReadWriteLock}的增强版,读写锁是读写,写写互斥.StampedLock读写到时候不互斥, * 若有读写操作,写完之后再重新读 * * @author 飞花梦影 * @date 2021-06-05 10:05:42 */public class S_StampedLock {	private int balance;	private StampedLock lock = new StampedLock();	public void conditionReadWrite(int value) {		// 首先判断balance的值是否符合更新的条件		long stamp = lock.readLock();		while (balance > 0) {			// 读锁转写锁			long writeStamp = lock.tryConvertToWriteLock(stamp);			if (writeStamp != 0) {				// 成功转换成为写锁				stamp = writeStamp;				balance += value;				break;			} else {				// 没有转换成写锁,这里需要首先释放读锁,然后再拿到写锁				lock.unlockRead(stamp);				// 获取写锁				stamp = lock.writeLock();			}		}		lock.unlock(stamp);	}	public void optimisticRead() {		long stamp = lock.tryOptimisticRead();		int c = balance;		// 这里可能会出现了写操作，因此要进行判断		if (!lock.validate(stamp)) {			// 要从新读取			long readStamp = lock.readLock();			c = balance;			stamp = readStamp;		}		System.out.println(c);		lock.unlockRead(stamp);	}	public void read() {		// 普通读锁		long stamp = lock.readLock();		// 乐观锁		lock.tryOptimisticRead();		// 读		// System.out.println(balance);		lock.unlockRead(stamp);	}	public void write(int value) {		// 普通写锁		long stamp = lock.writeLock();		balance += value;		lock.unlockWrite(stamp);	}}