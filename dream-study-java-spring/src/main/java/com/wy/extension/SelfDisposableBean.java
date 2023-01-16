package com.wy.extension;

import org.springframework.beans.factory.DisposableBean;

/**
 * {@link DisposableBean}:该接口当此对象销毁时自动执行.比如运行applicationContext.registerShutdownHook时
 *
 * @author 飞花梦影
 * @date 2022-10-18 00:17:48
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class SelfDisposableBean implements DisposableBean {

	@Override
	public void destroy() throws Exception {
		System.out.println("[DisposableBean] SelfDisposableBean");
	}
}