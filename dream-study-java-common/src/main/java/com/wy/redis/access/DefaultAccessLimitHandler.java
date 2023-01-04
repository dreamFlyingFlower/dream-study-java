package com.wy.redis.access;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.dream.starter.core.helper.RedisHelper;
import com.wy.limit.LimitAccessHandler;
import com.wy.limit.annotation.LimitAccess;

import lombok.extern.slf4j.Slf4j;

/**
 * 默认访问次数限制实现
 *
 * @author 飞花梦影
 * @date 2022-06-23 09:43:09
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Slf4j
public class DefaultAccessLimitHandler implements LimitAccessHandler {

	@Autowired
	private RedisHelper redisHelper;

	@Override
	public boolean handler(LimitAccess limitAccess) {
		ServletRequestAttributes servletRequestAttributes =
				(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = servletRequestAttributes.getRequest();
		// 直接利用ip+api作为key
		String key = request.getRemoteAddr() + ":" + request.getContextPath() + ":" + request.getServletPath();
		Integer count = (Integer) redisHelper.get(key);
		if (null == count || -1 == count) {
			redisHelper.setExpire(key, 1, limitAccess.value(), limitAccess.timeUnit());
			return true;
		}
		if (count < limitAccess.count()) {
			redisHelper.incr(key);
			return true;
		}
		if (count >= limitAccess.count()) {
			log.warn("请求过于频繁请稍后再试");
			return false;
		}
		return true;
	}
}