package com.wy.activity;

import org.activiti.engine.impl.interceptor.AbstractCommandInterceptor;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandConfig;

/**
 * Activiti拦截器
 *
 * @author 飞花梦影
 * @date 2021-08-12 08:58:34
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class MyActivitiCommand extends AbstractCommandInterceptor {

	@Override
	public <T> T execute(CommandConfig config, Command<T> command) {
		System.out.println("进入命令拦截器");
		return this.getNext().execute(config, command);
	}
}