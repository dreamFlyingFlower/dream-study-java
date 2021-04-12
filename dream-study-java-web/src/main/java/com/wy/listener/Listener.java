package com.wy.listener;

import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Web Listener:各种Web原生监听器,监听器没有顺序
 * 
 * {@link HttpSessionActivationListener}:监听HttpSession活化和顿化,监听事件对象{@link HttpSessionEvent}
 * {@link HttpSessionAttributeListener}:监听session的属性变化,监听对象{@link HttpSessionBindingEvent}
 * {@link HttpSessionBindingListener}:监听什么对象绑定到session上
 * {@link HttpSessionListener}:监听session的创建销毁,监听对象{@link HttpSessionEvent}
 * {@link ServletContextAttributeListener}:监听ServletContext的属性变化
 * {@link ServletContextListener}:监听ServletContext创建和销毁
 * {@link ServletRequestListener}:监听Request的创建销毁
 * {@link ServletRequestAttributeListener}:监听Request的属性变化
 * 
 * @author 飞花梦影
 * @date 2021-04-11 11:03:33
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class Listener {

}