package com.wy.example;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 利用{@link AbstractQueuedSynchronizer}实现自己的锁
 * 
 * @auther 飞花梦影
 * @date 2021-06-15 21:09:57
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class MyLock implements Lock {

	private Sync sync = new Sync();

	private class Sync extends AbstractQueuedSynchronizer {

		private static final long serialVersionUID = -6171289566091690128L;

		@Override
		protected boolean tryAcquire(int arg) {
			// 如果第一个线程进来,可以拿到锁,返回true;如果第二个线程进来,则拿不到锁,返回false.
			// 有种特例,如果当前进来的线程和当前保存的线程是同一个线程,则可以拿到锁,但是有代价,要更新状态值
			int state = getState();
			Thread t = Thread.currentThread();
			if (state == 0) {
				if (compareAndSetState(0, arg)) {
					setExclusiveOwnerThread(t);
					return true;
				}
			} else if (getExclusiveOwnerThread() == t) {
				setState(state + 1);
				return true;
			}
			return false;
		}

		@Override
		protected boolean tryRelease(int arg) {
			// 锁的获取和释放肯定是一一对应的,那么调用此方法的线程一定是当前线程
			if (Thread.currentThread() != getExclusiveOwnerThread()) {
				throw new RuntimeException();
			}
			int state = getState() - arg;
			boolean flag = false;
			if (state == 0) {
				setExclusiveOwnerThread(null);
				flag = true;
			}
			setState(state);
			return flag;
		}

		Condition newCondition() {
			return new ConditionObject();
		}
	}

	@Override
	public void lock() {
		sync.acquire(1);
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {
		sync.acquireInterruptibly(1);
	}

	@Override
	public boolean tryLock() {
		return sync.tryAcquire(1);
	}

	@Override
	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		return sync.tryAcquireNanos(1, unit.toNanos(time));
	}

	@Override
	public void unlock() {
		sync.release(1);
	}

	@Override
	public Condition newCondition() {
		return sync.newCondition();
	}
}