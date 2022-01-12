package com.wy.config;

import java.util.LinkedHashMap;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Shiro配置类
 *
 * {@link DefaultFilter}:Shiro对各种请求的不同拦截方式
 *
 * @author 飞花梦影
 * @date 2021-04-17 18:04:39
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
public class ShiroConfig {

	@Bean
	public ShiroFilterFactoryBean
			shiroFilterFactoryBean(@Qualifier("securityManager") SecurityManager securityManager) {
		ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
		factoryBean.setSecurityManager(securityManager);
		factoryBean.setLoginUrl("/user/login");
		// 登录成功的url
		factoryBean.setSuccessUrl("/user/index");
		factoryBean.setUnauthorizedUrl("/error");
		// 拦截请求的各种方式
		LinkedHashMap<String, String> map = new LinkedHashMap<>();
		// 拦截的请求和方式,方式可见
		// authc:必须登录验证
		map.put("/user/index", "authc");
		// anon:不需要任何校验
		map.put("/user/login", "anon");
		// user:走UserFilter拦截器,相当于验证是否已经登录
		map.put("/**", "user");
		// 需要指定角色,固定写法:roles固定写法,数组中为角色名,多个角色用逗号隔开
		map.put("/admin", "roles[admin]");
		// 需要指定权限的访问路径,固定写法:perms固定,数组中为需要的权限,多个用逗号隔开
		map.put("/role/assign", "perms[save]");
		factoryBean.setFilterChainDefinitionMap(map);
		return factoryBean;
	}

	@Bean
	public SecurityManager securityManager(@Qualifier("authRealm") AuthRealm authRealm) {
		DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
		defaultWebSecurityManager.setRealm(authRealm);
		return defaultWebSecurityManager;
	}

	@Bean
	public AuthRealm authRealm(@Qualifier("credentialMatcher") CredentialMatcher credentialsMatcher) {
		AuthRealm authRealm = new AuthRealm();
		authRealm.setCredentialsMatcher(credentialsMatcher);
		return authRealm;
	}

	@Bean("credentialMatcher")
	public CredentialMatcher credentialMatcher() {
		return new CredentialMatcher();
	}

	/**
	 * 建立Spring和Shiro之间的关系,用Shiro替代原来的Spring Security
	 *
	 * @param securityManager SecurityManager
	 * @return 认证拦截
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor
			authorizationAttributeSourceAdvisor(@Qualifier("securityManager") SecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
		advisor.setSecurityManager(securityManager);
		return advisor;
	}

	@Bean
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
		proxyCreator.setProxyTargetClass(true);
		return proxyCreator;
	}
}