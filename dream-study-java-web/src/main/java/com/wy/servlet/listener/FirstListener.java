package com.wy.servlet.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * SpringBoot整合ServletListener一,直接在类上添加{@link WebListener}注解,同时要开启{@link ServletComponentScan}
 *
 * <listener>
 * <listener-class>com.wy.servletlistener.FirstListener</listener-class>
 * </listener>
 *
 * @author ParadiseWY
 * @date 2020-12-08 23:23:14
 * @git {@link https://github.com/mygodness100}
 */
@WebListener
public class FirstListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println("Listener...init......");
	}
}