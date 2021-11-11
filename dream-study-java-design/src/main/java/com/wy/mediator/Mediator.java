package com.wy.mediator;

/**
 * 中介者模式
 * 
 * @author 飞花梦影
 * @date 2021-11-08 23:06:01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class Mediator {

	private MediatorMan man;

	private MediatorWoman woman;

	public void setMan(MediatorMan man) {
		this.man = man;
	}

	public void setWoman(MediatorWoman woman) {
		this.woman = woman;
	}

	public void getPartner(MediatorPerson person) {
		// 将搭档设置上
		if (person instanceof MediatorMan) {
			this.setMan((MediatorMan) person);
		} else {
			this.setWoman((MediatorWoman) person);
		}
		// 判断条件
		if (man == null || woman == null) {
			System.out.println("汗,我不是同性恋!");
		} else {

			if (man.getCondition() == woman.getCondition()) {
				System.out.println(man.getName() + "和" + woman.getName() + "绝配");
			} else {
				System.out.println(man.getName() + "和" + woman.getName() + "不相配");
			}
		}
	}
}