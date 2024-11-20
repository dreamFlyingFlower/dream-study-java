package com.wy.scalable.example;

import org.springframework.beans.factory.SmartInitializingSingleton;

/**
 * {@link SmartInitializingSingleton}:该接口是在spring容器管理的所有单例对象(非懒加载对象)初始化完成之后调用的回调接口.
 * 
 * 其触发时机为postProcessAfterInitialization之后,用户可以扩展此接口在对所有单例对象初始化完毕后,做一些后置的业务处理
 *
 * @author 飞花梦影
 * @date 2022-10-18 00:15:29
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class SelfSmartInitializingSingleton implements SmartInitializingSingleton {

	@Override
	public void afterSingletonsInstantiated() {
		System.out.println("[SelfSmartInitializingSingleton]");
	}
}