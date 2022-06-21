package com.wy.shiro.service.impl;

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

import com.alibaba.fastjson.JSONObject;
import com.wy.result.Result;
import com.wy.shiro.constant.ShiroConstant;
import com.wy.shiro.core.base.ShiroUser;
import com.wy.shiro.core.base.SimpleToken;
import com.wy.shiro.core.bridge.UserBridgeService;
import com.wy.shiro.core.impl.JwtTokenManager;
import com.wy.shiro.entity.vo.LoginVo;
import com.wy.shiro.service.LoginService;
import com.wy.shiro.utils.ShiroUserUtil;
import com.wy.shiro.utils.ShiroUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 登录业务实现
 *
 * @author 飞花梦影
 * @date 2022-06-22 00:11:05
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

	@Resource(name = "redissonClientForShiro")
	private RedissonClient redissonClient;

	@Autowired
	private UserBridgeService userBridgeService;

	@Autowired
	private JwtTokenManager jwtTokenManager;

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
	 * 加载缓存
	 * 
	 * @param token
	 */
	private void loadAuthorityToCache(SimpleToken token) {
		// 登陆成功后缓存用户的权限信息进入缓存
		ShiroUser shiroUser = ShiroUserUtil.getShiroUser();
		userBridgeService.loadUserAuthorityToCache(shiroUser);
	}

	@Override
	public Result<?> routeForJwt(LoginVo loginVo) throws UnknownAccountException, IncorrectCredentialsException {
		// 实现登录
		String jwtToken = null;
		try {
			SimpleToken simpleToken = new SimpleToken(null, loginVo.getLoginName(), loginVo.getPassWord());
			Subject subject = SecurityUtils.getSubject();
			subject.login(simpleToken);
			// 登录完成之后需要颁发令牌
			String sessionId = ShiroUserUtil.getShiroSessionId();
			ShiroUser shiroUser = ShiroUserUtil.getShiroUser();
			Map<String, Object> claims = new HashMap<>();
			claims.put("shiroUser", JSONObject.toJSONString(shiroUser));
			jwtToken = jwtTokenManager.issuedToken("system", subject.getSession().getTimeout(), sessionId, claims);
			// 缓存加载
			this.loadAuthorityToCache(simpleToken);
		} catch (Exception e) {
			return Result.error(ShiroConstant.LOGIN_FAILURE_CODE, ShiroConstant.LOGIN_FAILURE_MESSAGE);
		}
		return Result.result(ShiroConstant.LOGIN_SUCCESS_CODE, ShiroConstant.LOGIN_SUCCESS_MESSAGE, jwtToken);
	}
}