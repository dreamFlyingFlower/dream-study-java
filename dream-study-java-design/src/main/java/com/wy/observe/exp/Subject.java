package com.wy.observe.exp;

/**
 * 抽象主题角色类
 * 
 * @author 飞花梦影
 * @date 2022-09-16 14:54:07
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public interface Subject {

	// 添加订阅者（添加观察者对象）
	void attach(Observer observer);

	// 删除订阅者
	void detach(Observer observer);

	// 通知订阅者更新消息
	void notify(String message);
}