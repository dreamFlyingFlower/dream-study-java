package com.wy.memento.white;

/**
 * 备忘录对象管理对象
 * 
 * @author 飞花梦影
 * @date 2022-09-16 14:56:59
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class RoleStateCaretaker {

	private RoleStateMemento roleStateMemento;

	public RoleStateMemento getRoleStateMemento() {
		return roleStateMemento;
	}

	public void setRoleStateMemento(RoleStateMemento roleStateMemento) {
		this.roleStateMemento = roleStateMemento;
	}
}