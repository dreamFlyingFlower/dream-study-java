package com.wy.algorithms.queue;

public interface Queue<E> {

	int getSize();

	boolean isEmpty();

	void enqueue(E e);

	E dequeue();

	E getFront();
}