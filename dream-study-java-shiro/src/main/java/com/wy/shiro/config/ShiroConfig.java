package com.wy.shiro.config;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import com.wy.shiro.core.RedisSessionDao;
import com.wy.shiro.core.SelfShiroRealm;
import com.wy.shiro.filter.JwtAuthcFilter;
import com.wy.shiro.filter.JwtPermsFilter;
import com.wy.shiro.filter.JwtRolesFilter;
import com.wy.shiro.filter.KickedOutAuthorizationFilter;
import com.wy.shiro.filter.RolesOrAuthorizationFilter;
import com.wy.shiro.jwt.JwtShiroSessionManager;
import com.wy.shiro.jwt.JwtTokenManager;
import com.wy.shiro.properties.ShiroRedisProperties;
import com.wy.shiro.utils.PropertiesUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 权限配置类
 * 
 * @author 飞花梦影
 * @date 2022-06-21 16:46:39
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Configuration
@EnableConfigurationProperties({ ShiroRedisProperties.class })
@Slf4j
public class ShiroConfig {

	@Autowired
	private ShiroRedisProperties shiroRedisProperties;

	@Autowired
	private JwtTokenManager jwtTokenManager;

	@Bean("redissonClientForShiro")
	public RedissonClient redissonClient() {
		// 获取当前redis节点信息
		String[] nodes = shiroRedisProperties.getNodes().split(",");
		// 创建配置信息:1-单机redis配置;2-集群redis配置
		Config config = new Config();
		if (nodes.length == 1) {
			// 单机redis配置
			config.useSingleServer().setAddress(nodes[0]).setConnectTimeout(shiroRedisProperties.getConnectTimeout())
					.setConnectionMinimumIdleSize(shiroRedisProperties.getConnectionMinimumidleSize())
					.setConnectionPoolSize(shiroRedisProperties.getConnectPoolSize())
					.setTimeout(shiroRedisProperties.getTimeout());
		} else if (nodes.length > 1) {
			// 集群redis配置
			config.useClusterServers().addNodeAddress(nodes).setConnectTimeout(shiroRedisProperties.getConnectTimeout())
					.setMasterConnectionMinimumIdleSize(shiroRedisProperties.getConnectionMinimumidleSize())
					.setMasterConnectionPoolSize(shiroRedisProperties.getConnectPoolSize())
					.setTimeout(shiroRedisProperties.getTimeout());
		} else {
			return null;
		}
		// 创建redission的客户端,交于spring管理
		RedissonClient client = Redisson.create(config);
		return client;
	}

	/**
	 * 创建cookie对象
	 */
	@Bean(name = "sessionIdCookie")
	public SimpleCookie simpleCookie() {
		SimpleCookie simpleCookie = new SimpleCookie();
		simpleCookie.setName("ShiroSession");
		return simpleCookie;
	}

	/**
	 * 权限管理器
	 */
	@Bean(name = "securityManager")
	public DefaultWebSecurityManager defaultWebSecurityManager() {
		// 默认权限管理器
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		// 设置自定义的校验
		securityManager.setRealm(selfShiroRealm());
		// 管理会话
		securityManager.setSessionManager(shiroSessionManager());
		return securityManager;
	}

	/**
	 * 自定义RealmImpl
	 */
	@Bean(name = "selfShiroRealm")
	public SelfShiroRealm selfShiroRealm() {
		return new SelfShiroRealm();
	}

	/**
	 * 自定义session会话存储的实现类,使用Redis来存储共享session,达到分布式部署目的
	 */
	@Bean("redisSessionDao")
	public RedisSessionDao redisSessionDao() {
		RedisSessionDao redisSessionDao = new RedisSessionDao();
		redisSessionDao.setGlobalSessionTimeout(shiroRedisProperties.getGlobalSessionTimeout());
		return redisSessionDao;
	}

	/**
	 * 会话管理器
	 */
	@Bean(name = "sessionManager")
	public DefaultWebSessionManager shiroSessionManager() {
		// DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		JwtShiroSessionManager sessionManager = new JwtShiroSessionManager();
		sessionManager.setSessionDAO(redisSessionDao());
		// 关闭会话更新
		sessionManager.setSessionValidationSchedulerEnabled(false);
		// cookie生效
		sessionManager.setSessionIdCookieEnabled(true);
		// 指定cookie的生成策略
		sessionManager.setSessionIdCookie(simpleCookie());
		// 指定全局会话超时时间
		sessionManager.setGlobalSessionTimeout(shiroRedisProperties.getGlobalSessionTimeout());
		return sessionManager;
	}

	/**
	 * 保证实现了Shiro内部lifecycle函数的bean执行
	 */
	@Bean(name = "lifecycleBeanPostProcessor")
	public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

	/**
	 * AOP式方法级权限检查
	 */
	@Bean
	@DependsOn("lifecycleBeanPostProcessor")
	public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
		defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
		return defaultAdvisorAutoProxyCreator;
	}

	/**
	 * 配合DefaultAdvisorAutoProxyCreator事项注解权限校验
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor() {
		AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
		aasa.setSecurityManager(defaultWebSecurityManager());
		return new AuthorizationAttributeSourceAdvisor();
	}

	/**
	 * 拦截器链
	 */
	private Map<String, String> filterChainDefinition() {
		List<Object> list = PropertiesUtil.propertiesShiro.getKeyList();
		Map<String, String> map = new LinkedHashMap<>();
		for (Object object : list) {
			String key = object.toString();
			String value = PropertiesUtil.getShiroValue(key);
			log.info("读取防止盗链控制:---key{},---value:{}", key, value);
			map.put(key, value);
		}
		return map;
	}

	/**
	 * 自定义拦截器定义
	 */
	private Map<String, Filter> filters() {
		Map<String, Filter> map = new HashMap<String, Filter>();
		map.put("role-or", new RolesOrAuthorizationFilter());
		map.put("kicked-out",
				new KickedOutAuthorizationFilter(redissonClient(), redisSessionDao(), shiroSessionManager()));
		map.put("jwt-authc", new JwtAuthcFilter(jwtTokenManager));
		map.put("jwt-perms", new JwtPermsFilter());
		map.put("jwt-roles", new JwtRolesFilter());
		return map;
	}

	/**
	 * Shiro过滤器
	 */
	@Bean("shiroFilter")
	public ShiroFilterFactoryBean shiroFilterFactoryBean() {
		ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
		shiroFilter.setSecurityManager(defaultWebSecurityManager());
		// 使自定义拦截器生效
		shiroFilter.setFilters(filters());
		shiroFilter.setFilterChainDefinitionMap(filterChainDefinition());
		shiroFilter.setLoginUrl("/login");
		shiroFilter.setUnauthorizedUrl("/login");
		return shiroFilter;
	}
}