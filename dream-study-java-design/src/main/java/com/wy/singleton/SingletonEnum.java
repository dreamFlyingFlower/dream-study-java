package com.wy.singleton;

/**
 * 利用枚举进行单例实现,枚举不能被反射实例化
 *
 * @author 飞花梦影
 * @date 2021-12-22 10:09:11
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public enum SingletonEnum {

	INSTANCE;

	public int add() {
		return 0;
	}
}