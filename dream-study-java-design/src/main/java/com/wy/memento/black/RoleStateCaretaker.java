package com.wy.memento.black;

/**
 * 备忘录对象管理对象
 * 
 * @author 飞花梦影
 * @date 2022-09-16 15:02:34
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class RoleStateCaretaker {

	// 声明RoleStateMemento类型的变量
	private Memento memento;

	public Memento getMemento() {
		return memento;
	}

	public void setMemento(Memento memento) {
		this.memento = memento;
	}
}