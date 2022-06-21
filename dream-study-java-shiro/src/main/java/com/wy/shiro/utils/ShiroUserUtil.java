package com.wy.shiro.utils;

import java.util.Objects;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;

import com.wy.shiro.constant.SuperConstant;
import com.wy.shiro.core.base.ShiroUser;

/**
 * shiroUser工具类
 */
public class ShiroUserUtil extends ShiroUtil {

	/**
	 * 返回当前登录用户封装对象
	 * 
	 * @param
	 * @return
	 */
	public static ShiroUser getShiroUser() {
		if (Objects.nonNull(ThreadContext.getSubject()) && Objects.nonNull(SecurityUtils.getSubject().getPrincipal())) {
			return (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		} else {
			return new ShiroUser(SuperConstant.ANON_ID, SuperConstant.ANON_LOGIN_NAME);
		}
	}

	/**
	 * 获得shiroUserId
	 * 
	 * @param
	 * @return
	 */
	public static String getShiroUserId() {
		ShiroUser shiroUser = ShiroUserUtil.getShiroUser();
		if (Objects.isNull(shiroUser)) {
			return null;
		} else {
			return shiroUser.getId();
		}
	}

	/**
	 * 更新登录用户信息 用户id 和用户登录名不更新
	 * 
	 * @param shiroUser 需要修改对象
	 * @return
	 */
	public static void updateShiroUser(ShiroUser shiroUser) {
		String subjectKey = "org.apache.shiro.subject.support.DefaultSubjectContext_PRINCIPALS_SESSION_KEY";
		Subject subject = SecurityUtils.getSubject();
		Object ooo = subject.getSession().getAttribute(subjectKey);
		SimplePrincipalCollection collection = (SimplePrincipalCollection) ooo;
		ShiroUser user = (ShiroUser) collection.getPrimaryPrincipal();
		System.out.println(user);
		subject.getSession().setAttribute(subjectKey, collection);
	}
}