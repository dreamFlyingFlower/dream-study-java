package com.wy.request;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

/**
 * 解决输入流无法重复使用问题,若只处理一次数据,则继承OncePerRequestFilter,若请求转发和包含时也要处理数据,可继承普通Filter
 * 
 * {@link ContentCachingRequestWrapper}:请求包装器,用于缓存请求的输入流.允许多次读取请求体,在需要多次处理请求数据(如日志记录和业务处理)时可用
 *
 * @author 飞花梦影
 * @date 2022-12-21 10:35:11
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Component
@Order(-1)
public class ReusableRequestWrapperFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
		// 可以在这里处理请求数据
		byte[] body = requestWrapper.getContentAsByteArray();
		// 处理body
		System.out.println(new String(body));
		filterChain.doFilter(requestWrapper, response);
	}
}