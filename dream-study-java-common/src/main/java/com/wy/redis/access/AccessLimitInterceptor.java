package com.wy.redis.access;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.alibaba.fastjson2.JSONObject;
import com.wy.limit.LimitAccessHandler;
import com.wy.limit.annotation.LimitAccess;
import com.wy.result.Result;

/**
 * Redis接口限流
 * 
 * FIXME 未完成
 *
 * @author 飞花梦影
 * @date 2021-11-09 17:10:49
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Component
public class AccessLimitInterceptor implements HandlerInterceptor {

	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		Method method = ((HandlerMethod) handler).getMethod();
		LimitAccess methodAnnotation = method.getAnnotation(LimitAccess.class);
		LimitAccess classAnnotation = method.getDeclaringClass().getAnnotation(LimitAccess.class);
		// 如果方法上有注解就优先选择方法上的参数,否则类上的参数
		LimitAccess limitAccess = methodAnnotation != null ? methodAnnotation : classAnnotation;
		if (Objects.isNull(limitAccess)) {
			return true;
		}

		if (isLimit(request, limitAccess)) {
			resonseOut(response, Result.error("被限流了"));
			return false;
		}

		if (limitAccess.custom()) {
			return limitAccess.handler().newInstance().handler(limitAccess);
		} else {
			return new DefaultAccessLimitHandler().handler(limitAccess);
		}
	}

	/**
	 * 判断请求是否受限
	 * 
	 * @param request 请求
	 * @param limitAccess 限流注解
	 * @return 是否限流
	 */
	private boolean isLimit(HttpServletRequest request, LimitAccess limitAccess) {
		if (limitAccess.custom()) {
			try {
				LimitAccessHandler limitAccessHandler =
						limitAccess.handler().getDeclaredConstructor(new Class<?>[0]).newInstance(new Object[0]);
				return limitAccessHandler.handler(limitAccess);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
			return false;
		}
		// 缓存key,根据实际情况获取
		String limitKey = request.getServletPath() + request.getRequestURI();
		Object redisCount = redisTemplate.opsForValue().get(limitKey);
		if (redisCount == null) {
			// 初始 次数
			redisTemplate.opsForValue().set(limitKey, 1, limitAccess.value(), limitAccess.timeUnit());
		} else {
			if (Integer.parseInt(redisCount.toString()) >= limitAccess.count()) {
				return true;
			}
			// 次数自增
			redisTemplate.opsForValue().increment(limitKey);
		}
		return false;
	}

	/**
	 * 回写给客户端
	 * 
	 * @param response
	 * @param result
	 * @throws IOException
	 */
	private void resonseOut(HttpServletResponse response, Result<?> result) throws IOException {
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		PrintWriter out = response.getWriter();
		out.append(JSONObject.toJSONString(result).toString());
	}
}