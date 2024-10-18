package com.wy.request;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

/**
 * 对响应进行自定义操作,若只处理一次数据,则继承OncePerRequestFilter,若请求转发和包含时也要处理数据,可继承普通Filter
 * 
 * {@link ContentCachingResponseWrapper}:响应包装器,用于缓存响应的输出流.允许在响应提交给客户端之前修改响应体,在需要对响应内容进行后处理(如添加额外的头部信息和修改响应体)时可用
 *
 * @author 飞花梦影
 * @date 2022-12-21 10:35:11
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Component
public class ReusableResponseWrapperFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
		filterChain.doFilter(request, responseWrapper);

		// 可以在这里处理响应数据
		byte[] body = responseWrapper.getContentAsByteArray();
		System.out.println(new String(body));
		// 处理body,例如添加签名
		responseWrapper.setHeader("X-Signature", "some-signature");

		// 必须调用此方法以将响应数据发送到客户端
		responseWrapper.copyBodyToResponse();
	}
}