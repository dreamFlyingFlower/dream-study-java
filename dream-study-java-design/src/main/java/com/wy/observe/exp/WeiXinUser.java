package com.wy.observe.exp;

/**
 * 具体的观察者角色类
 * 
 * @author 飞花梦影
 * @date 2022-09-16 14:54:56
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class WeiXinUser implements Observer {

	private String name;

	public WeiXinUser(String name) {
		this.name = name;
	}

	@Override
	public void update(String message) {
		System.out.println(name + "-" + message);
	}
}