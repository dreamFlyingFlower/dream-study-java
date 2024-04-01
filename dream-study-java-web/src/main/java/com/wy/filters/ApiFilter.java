package com.wy.filters;

import java.io.IOException;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import com.dream.collection.MapHelper;
import com.dream.lang.StrHelper;
import com.dream.result.ResultException;
import com.wy.properties.ConfigProperties;

/**
 * url拦截器,只有登录和下载资源不需要校验,其他都需要进行校验
 * 
 * @author ParadiseWY
 * @date 2020-12-07 11:31:15
 * @git {@link https://github.com/mygodness100}
 */
@Order(1)
@Configuration
@ConfigurationProperties(prefix = "config.resource")
public class ApiFilter extends OncePerRequestFilter {

	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;

	@Autowired
	private ConfigProperties config;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if (!config.getCommon().isValidApi()) {
			filterChain.doFilter(request, response);
		} else {
			if (request.getRequestURI().startsWith("/user/login") || request.getRequestURI().startsWith("/download/")
					|| request.getRequestURI().startsWith("/test")
					|| request.getRequestURI().startsWith("/bigscreen")) {
				// 登录和下载资源放过
				filterChain.doFilter(request, response);
			} else {
				// 从redis缓存中检验是否存在某个值,值从请求头的auth中来
				String auth = request.getHeader("Authentication");
				if (StrHelper.isBlank(auth)) {
					throw new ResultException("您还未登录,请登录");
				}
				Map<Object, Object> entity = redisTemplate.opsForHash().entries(auth);
				if (MapHelper.isNotEmpty(entity)) {
					filterChain.doFilter(request, response);
				} else {
					throw new ResultException("您还未登录,请登录");
				}
			}
		}
	}
}