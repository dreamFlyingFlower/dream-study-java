package com.wy.retry.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wy.retry.core.base.ShiroUser;
import com.wy.retry.core.base.SimpleToken;
import com.wy.retry.core.bridge.UserBridgeService;
import com.wy.retry.service.LoginService;
import com.wy.retry.utils.ShiroUserUtil;
import com.wy.retry.utils.ShiroUtil;
import com.wy.retry.vo.LoginVo;

import lombok.extern.log4j.Log4j2;

/**
 * 登陆业务实现
 */
@Service("loginService")
@Log4j2
public class LoginServiceImpl implements LoginService {

	@Resource(name = "redissonClientForShiro")
	RedissonClient redissonClient;

	@Autowired
	UserBridgeService userBridgeService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see LoginService#route(com.yz.commons.vo.LoginVo)
	 */
	@Override
	public Map<String, String> route(LoginVo loginVo) throws UnknownAccountException, IncorrectCredentialsException {
		Map<String, String> map = new HashMap<>();
		try {
			SimpleToken token = new SimpleToken(null, loginVo.getLoginName(), loginVo.getPassWord());
			Subject subject = SecurityUtils.getSubject();
			subject.login(token);
			this.loadAuthorityToCache(token);
		} catch (UnknownAccountException ex) {
			log.error("登陆异常:{}", ex);
			throw new UnknownAccountException(ex);
		} catch (IncorrectCredentialsException ex) {
			log.error("登陆异常:{}", ex);
			throw new IncorrectCredentialsException(ex);
		}
		map.put("authorizationKey", ShiroUtil.getShiroSessionId());
		return map;
	}

	/**
	 *
	 * <b>方法名：</b>：loadAuthorityToCache<br>
	 * <b>功能说明：</b>：加载缓存<br>
	 * 
	 * @author <font color='blue'>束文奇</font>
	 * @date 2017年11月16日 下午4:55:04
	 * @param token
	 */
	private void loadAuthorityToCache(SimpleToken token) {
		// 登陆成功后缓存用户的权限信息进入缓存
		ShiroUser shiroUser = ShiroUserUtil.getShiroUser();
		userBridgeService.loadUserAuthorityToCache(shiroUser);
	}
}