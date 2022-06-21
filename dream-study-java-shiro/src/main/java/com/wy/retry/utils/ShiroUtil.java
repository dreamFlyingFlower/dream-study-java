package com.wy.retry.utils;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

/**
 * shiro工具类
 */
public class ShiroUtil {

	/**
	 * 获得shiro的session
	 * 
	 * @param
	 * @return
	 */
	public static Session getShiroSession() {
		return SecurityUtils.getSubject().getSession();
	}

	/**
	 * 获得shiro的sessionId
	 * 
	 * @param
	 * @return
	 */
	public static String getShiroSessionId() {
		return getShiroSession().getId().toString();
	}

	/**
	 * 是否登陆
	 * 
	 * @param
	 * @return
	 */
	public static Boolean isAuthenticated() {
		Subject subject = SecurityUtils.getSubject();
		return subject.isAuthenticated();
	}
}