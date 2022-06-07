package com.wy;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.authz.aop.AuthenticatedAnnotationMethodInterceptor;
import org.apache.shiro.authz.aop.GuestAnnotationMethodInterceptor;
import org.apache.shiro.authz.aop.PermissionAnnotationMethodInterceptor;
import org.apache.shiro.authz.aop.RoleAnnotationMethodInterceptor;
import org.apache.shiro.authz.aop.UserAnnotationMethodInterceptor;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.apache.shiro.web.servlet.AbstractFilter;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.apache.shiro.web.servlet.AdviceFilter;
import org.apache.shiro.web.servlet.NameableFilter;
import org.apache.shiro.web.servlet.OncePerRequestFilter;
import org.apache.shiro.web.servlet.ProxiedFilterChain;
import org.apache.shiro.web.servlet.ShiroFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
 * {@link Subject}:认证主体,包含两个信息:
 * 		Principals:身份,可以是用户名,邮件,手机号码等等,用来标识一个登录主体身份.
 * 		Credentials:凭证,常见有密码,数字证书等等
 * </pre>
 * 
 * 相关注解:
 * 
 * <pre>
 * {@link RequiresAuthentication}:Subject必须已经在当前的session中被验证通过.
 * 		被 {@link AuthenticatedAnnotationMethodInterceptor}拦截
 * {@link RequiresGuest}:Subject是一个guest,即必须是在之前的session中没有被验证或被记住才能被访问或调用.
 * 		被 {@link GuestAnnotationMethodInterceptor}拦截 
 * {@link RequiresPermissions}:Subject含有指定一个或多个权限才能访问.
 * 		被 {@link PermissionAnnotationMethodInterceptor}拦截
 * {@link RequiresRoles}:Subject必须拥有所指定的角色.如果没有,抛出{@link AuthorizationException}异常.
 * 		被 {@link RoleAnnotationMethodInterceptor}拦截
 * {@link RequiresUser}:Subject是指定用户,或在当前session中通过验证被确认,或在之前 session 中的RememberMe服务被记住.
 * 		被 {@link UserAnnotationMethodInterceptor}拦截
 * </pre>
 *
 * @author 飞花梦影
 * @date 2020-12-02 15:16:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}