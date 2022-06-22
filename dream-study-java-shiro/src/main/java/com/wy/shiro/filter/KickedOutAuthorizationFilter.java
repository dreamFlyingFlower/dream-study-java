package com.wy.shiro.filter;

import java.util.Objects;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.session.ExpiredSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.redisson.api.RDeque;
import org.redisson.api.RedissonClient;

import com.wy.shiro.config.ShiroConfig;
import com.wy.shiro.constant.CacheConstant;
import com.wy.shiro.core.RedisSessionDao;
import com.wy.shiro.utils.ShiroUserUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 自定义过滤器,实现多人同时登录相同帐号时,只能一人在线,需要实现或继承Shiro拦截器,{@link ShiroConfig#shiroFilterFactoryBean}
 * 
 * <pre>
 * 只针对登录用户处理,首先判断是否登录
 * 使用RedissionClien创建队列
 * 判断当前sessionId是否存在于此用户的队列=key:登录名 value:多个sessionId
 * 不存在则放入队列尾端==>存入sessionId
 * 判断当前队列大小是否超过限定此账号的可在线人数
 * 超过:从队列头部拿到用户sessionId,从sessionManger根据sessionId拿到session,从sessionDao中移除session会话
 * 未超过:放过
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2022-06-22 09:00:48
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Slf4j
public class KickedOutAuthorizationFilter extends AccessControlFilter {

	private RedissonClient redissonClient;

	private RedisSessionDao redisSessionDao;

	private DefaultWebSessionManager defaultWebSessionManager;

	public KickedOutAuthorizationFilter(RedissonClient redissonClient, RedisSessionDao redisSessionDao,
	        DefaultWebSessionManager defaultWebSessionManager) {
		this.redissonClient = redissonClient;
		this.redisSessionDao = redisSessionDao;
		this.defaultWebSessionManager = defaultWebSessionManager;
	}

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
	        throws Exception {
		return false;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		// 只针对登录用户处理,首先判断是否登录
		Subject subject = getSubject(request, response);
		if (!subject.isAuthenticated()) {
			// 如果没有登录,直接进行之后的流程
			return true;
		}
		// 使用RedissionClien创建队列
		String sessionId = ShiroUserUtil.getShiroSessionId();
		String loginName = ShiroUserUtil.getShiroUser().getLoginName();
		// 当前用户的队列
		RDeque<String> deque =
		        redissonClient.getDeque(CacheConstant.GROUP_CAS + "kickedOutAuthorizationFilter:" + loginName);
		// 判断当前sessionId是否存在于此用户的队列=key:登录名 value:多个sessionId
		boolean flag = deque.contains(sessionId);
		// 不存在则放入队列尾端==>存入sessionId
		if (!flag) {
			deque.addLast(sessionId);
		}
		// 判断当前队列大小是否超过限定此账号的可在线人数
		if (deque.size() > 1) {
			// 超过:从队列头部拿到用户sessionId,从sessionManger根据sessionId拿到session,从sessionDao中移除session会话
			sessionId = deque.getFirst();
			deque.removeFirst();
			Session session = null;
			try {
				session = defaultWebSessionManager.getSession(new DefaultSessionKey(sessionId));
			} catch (UnknownSessionException ex) {
				log.info("session已经失效");
			} catch (ExpiredSessionException expiredSessionException) {
				log.info("session已经过期");
			}
			if (Objects.nonNull(session)) {
				redisSessionDao.delete(session);
			}
		}
		// 未超过:放过
		return true;
	}
}