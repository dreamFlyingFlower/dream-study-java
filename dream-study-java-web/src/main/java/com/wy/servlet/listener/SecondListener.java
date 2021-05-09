package com.wy.servlet.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.wy.config.GlobalWebConfig;

/**
 * SpringBoot整合ServletListener二,需要添加到{@link ServletListenerRegistrationBean}中,并注入到上下文中,不需要添加{@link Configuration}
 *
 * @author ParadiseWY
 * @date 2020-12-08 23:24:55
 * @git {@link https://github.com/mygodness100}
 */
public class SecondListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println("SecondListener..init.....");
	}

	/**
	 * 添加监听器到spring环境中,可在 {@link GlobalWebConfig}中配置,只需一个地方配置即可
	 * 
	 * @return 监听器对象
	 */
	@Bean
	public ServletListenerRegistrationBean<SecondListener> secondListener() {
		ServletListenerRegistrationBean<SecondListener> bean = new ServletListenerRegistrationBean<SecondListener>(
				new SecondListener());
		return bean;
	}
}