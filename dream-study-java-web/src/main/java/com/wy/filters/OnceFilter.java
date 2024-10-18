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

import com.wy.properties.ConfigProperties;

import dream.flying.flower.collection.MapHelper;
import dream.flying.flower.lang.StrHelper;
import dream.flying.flower.result.ResultException;

/**
 * url拦截器,只有登录和下载资源不需要校验,其他都需要进行校验
 * 
 * {@link OncePerRequestFilter}:
 * 
 * <pre>
 * 单次执行:确保在一次请求的生命周期内,无论请求如何转发(forwarding)或包含(including),过滤器逻辑只执行一次.这对于避免重复处理请求或响应非常有用
 * 内置支持:内置了对请求和响应包装器的支持,使得开发者可以方便地对请求和响应进行包装和处理
 * 简化代码:通过继承 OncePerRequestFilter,可以减少重复代码,因为过滤器的执行逻辑已经由基类管理
 * 易于扩展:通过重写 doFilterInternal() 来实现自己的过滤逻辑,而不需要关心过滤器的注册和执行次数
 * 
 * 使用场景:
 * 
 * 请求日志记录:在请求处理之前和之后记录请求的详细信息,如请求头、请求参数和请求体,而不希望在请求转发时重复记录
 * 请求数据修改:在请求到达控制器之前,对请求数据进行预处理或修改,例如添加或修改请求头,而不希望这些修改在请求转发时被重复应用
 * 响应数据修改:在响应发送给客户端之前,对响应数据进行后处理或修改,例如添加或修改响应头,而不希望这些修改在请求包含时被重复应用
 * 安全控制:实现安全控制逻辑,如身份验证、授权检查等,确保这些逻辑在一次请求的生命周期内只执行一次
 * 请求和响应的包装:使用 ContentCachingRequestWrapper 和 ContentCachingResponseWrapper 等包装器来缓存请求和响应数据,以便在请求处理过程中多次读取或修改数据
 * 性能监控:在请求处理前后进行性能监控,如记录处理时间,而不希望这些监控逻辑在请求转发时被重复执行
 * 异常处理:在请求处理过程中捕获和处理异常,确保异常处理逻辑只执行一次,即使请求被转发到其他处理器
 * </pre>
 * 
 * @author ParadiseWY
 * @date 2020-12-07 11:31:15
 * @git {@link https://github.com/mygodness100}
 */
@Order(1)
@Configuration
@ConfigurationProperties(prefix = "config.resource")
public class OnceFilter extends OncePerRequestFilter {

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