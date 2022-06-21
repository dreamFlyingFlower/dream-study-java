package com.wy.redis;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

import com.wy.config.CredentialMatcher;

/**
 * Shiro将token存入到redis
 *
 * @author 飞花梦影
 * @date 2022-06-09 10:54:18
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class ShiroRedisConfig {

	@Bean("credentialMatcher")
	public CredentialMatcher credentialMatcher() {
		return new CredentialMatcher();
	}

	/**
	 * 创建realm
	 * 
	 * @param credentialsMatcher 密码管理
	 * @return 自定义realm
	 */
	@Bean
	public SelfShiroRedisRealm
	        selfShiroRedisRealm(@Qualifier("credentialMatcher") CredentialMatcher credentialsMatcher) {
		SelfShiroRedisRealm authRealm = new SelfShiroRedisRealm();
		authRealm.setCredentialsMatcher(credentialsMatcher);
		return authRealm;
	}

	@Bean("securityManager")
	public SecurityManager securityManager(SelfShiroRedisRealm realm) {
		DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
		// 使用自己的realm
		manager.setRealm(realm);
		/*
		 * 关闭shiro自带的session,详情见文档
		 * http://shiro.apache.org/session-management.html#SessionManagement-
		 * StatelessApplications%28Sessionless%29
		 */
		DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
		DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
		defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
		subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
		manager.setSubjectDAO(subjectDAO);
		return manager;
	}

	@Bean("shiroFilter")
	public ShiroFilterFactoryBean factory(@Qualifier("securityManager") DefaultWebSecurityManager securityManager) {
		ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
		factoryBean.setSecurityManager(securityManager);
		// 配置登录页面
		factoryBean.setLoginUrl("/user/login");
		// 登录成功的url
		factoryBean.setSuccessUrl("/user/index");
		factoryBean.setUnauthorizedUrl("/error");
		// 添加自己的过滤器并且取名为token
		Map<String, Filter> filterMap = new HashMap<>();
		filterMap.put("token", new SelfShiroTokenFilter());
		factoryBean.setFilters(filterMap);
		Map<String, String> filterRuleMap = new LinkedHashMap<>();
		// swagger2,swagger3
		filterRuleMap.put("/swagger-ui.html", "anon");
		filterRuleMap.put("/swagger/index.html", "anon");
		filterRuleMap.put("/swagger/**", "anon");
		filterRuleMap.put("/**/*.js", "anon");
		filterRuleMap.put("/**/*.png", "anon");
		filterRuleMap.put("/**/*.ico", "anon");
		filterRuleMap.put("/**/*.css", "anon");
		filterRuleMap.put("/**/ui/**", "anon");
		filterRuleMap.put("/**/swagger-resources/**", "anon");
		filterRuleMap.put("/**/api-docs/**", "anon");
		filterRuleMap.put("/webjars/**", "anon");
		filterRuleMap.put("/druid/**", "anon");
		filterRuleMap.put("/app/**", "anon");
		filterRuleMap.put("/generator/**", "anon");
		filterRuleMap.put("/v1/api-docs", "anon");
		filterRuleMap.put("/websocket/**", "anon");
		filterRuleMap.put("/**", "oauth2");
		filterRuleMap.put("/login/login", "anon");
		filterRuleMap.put("/login/logout", "anon");
		filterRuleMap.put("/login/verifyCode", "anon");
		filterRuleMap.put("/login/captcha", "anon");
		// 所有请求通过自己的tokenFilter
		filterRuleMap.put("/**", "token");
		factoryBean.setFilterChainDefinitionMap(filterRuleMap);
		return factoryBean;
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
		/*
		 * setUsePrefix(false)用于解决一个奇怪的bug.SpringAop环境下,
		 * 在@Controller类的方法中加入@RequiresRole等shiro注解,会导致该方法无法映射请求,导致返回404,加入这项配置能解决这个bug
		 */
		// proxyCreator.setUsePrefix(true);
		proxyCreator.setProxyTargetClass(true);
		return proxyCreator;
	}
}