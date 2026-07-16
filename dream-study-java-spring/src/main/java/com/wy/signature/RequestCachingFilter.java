package com.wy.signature;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 解决请求体只能读取一次
 * 
 * 注意:ContentCachingRequestWrapper只能在请求完成之后获得请求体中的数据,在RequestBody解析之后,获取不到inputstream
 *
 * @author 飞花梦影
 * @date 2023-12-26 17:46:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Slf4j
@Component
@ConditionalOnBean(SignatureProperties.class)
public class RequestCachingFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {
		boolean isFirstRequest = !isAsyncDispatch(request);
		ContentCachingRequestWrapper requestWrapper = null;
		if (isFirstRequest && !(request instanceof ContentCachingRequestWrapper)) {
			requestWrapper = new ContentCachingRequestWrapper(request);
		} else {
			requestWrapper = (ContentCachingRequestWrapper) request;
		}

		ContentCachingResponseWrapper responseWrapper = null;
		if (isFirstRequest && !(response instanceof ContentCachingResponseWrapper)) {
			responseWrapper = new ContentCachingResponseWrapper(response);
		} else {
			responseWrapper = (ContentCachingResponseWrapper) request;
		}
		filterChain.doFilter(requestWrapper, response);

		// 必须要在doFilter之后调用,否则获取不到inputstream中的字节数组,获取到的是[]
		requestWrapper.getContentAsByteArray();

		// 如果使用了ContentCachingResponseWrapper,也必须是在doFilter之后调用,同时必须调用如下方法返回结果
		responseWrapper.copyBodyToResponse();
	}
}