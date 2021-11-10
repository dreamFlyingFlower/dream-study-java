package com.wy.memento;

/**
 * 备忘录模式:行为模式,主要是保存对象的内部状态,并在需要的时候恢复对象以前的状态
 * 
 * 这个和原型模式除了在使用方式上,没多大区别
 * 
 * @author 飞花梦影
 * @date 2021-11-10 11:25:42
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class Caretaker<T extends Cloneable> {

	private T memento;

	public T getMemento() {
		return memento;
	}

	public void setMemento(T memento) {
		this.memento = memento;
	}
}