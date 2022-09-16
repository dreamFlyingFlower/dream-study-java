package com.wy.factory.abs;

import com.wy.entity.Cartoon;

/**
 * 没有当前类就和工厂模式是相同的结构
 *
 * @author 飞花梦影
 * @date 2021-11-03 15:44:57
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public abstract class AbstractClannad implements Cartoon {

	public void sex() {
		System.out.println("男");
	}
}