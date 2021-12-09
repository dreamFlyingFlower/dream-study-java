package com.wy;

import org.apache.shiro.web.filter.PathMatchingFilter;
import org.apache.shiro.web.servlet.AbstractFilter;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.apache.shiro.web.servlet.AdviceFilter;
import org.apache.shiro.web.servlet.NameableFilter;
import org.apache.shiro.web.servlet.OncePerRequestFilter;
import org.apache.shiro.web.servlet.ShiroFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.servlet.ProxiedFilterChain;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;

/**
 * Shiro权限拦截
 * 
 * <pre>
 * {@link AbstractFilter}:拦截接口抽象类,所有Shiro拦截都继承该类
 * ->{@link NameableFilter}:对Shiro所有的拦截接口进行管理
 * -->{@link OncePerRequestFilter}:确保每个filter只调用一次
 *
 * --->{@link AbstractShiroFilter}:{@link ShiroFilter}抽象实现类
 * ---->{@link ShiroFilter}:整个Shiro拦截器的入口点
 *
 * --->{@link AdviceFilter}:提供类似AOP的支持
 * --->{@link AdviceFilter#preHandle}:前置增强
 * --->{@link AdviceFilter#postHandle}:后置增强
 * --->{@link AdviceFilter#afterCompletion}:最终增强,不管结果如何都会调用,即使抛异常
 *
 * ---->{@link PathMatchingFilter}:对请求进行匹配以及拦截
 * ----->{@link AccessControlFilter}:访问资源的基础功能,比如允许是否访问等
 *
 * {@link ProxiedFilterChain}:对Shiro的代理,先于容器执行拦截
 * {@link AuthenticationInfo}:主要存储用户的登录验证信息
 * {@link AuthorizationInfo}:主要存储用户的角色,权限等相关信息
 * </pre>
 *
 * @author 飞花梦影
 * @date 2020-12-02 15:16:40
 * @git {@link https://github.com/mygodness100}
 */
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}