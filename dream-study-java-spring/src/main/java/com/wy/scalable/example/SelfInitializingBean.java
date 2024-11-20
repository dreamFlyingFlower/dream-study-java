package com.wy.scalable.example;

import org.springframework.beans.factory.InitializingBean;

/**
 * {@link InitializingBean}:该接口为bean提供了初始化方法的方式,凡是继承该接口的类,在初始化bean的时候都会执行afterPropertiesSet
 * 
 * 这个扩展点的触发时机在postProcessAfterInitialization之前.用户实现此接口,来进行系统启动的时候一些业务指标的初始化工作
 *
 * @author 飞花梦影
 * @date 2022-10-18 00:10:49
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class SelfInitializingBean implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("[InitializingBean] SelfInitializingBean");
	}
}