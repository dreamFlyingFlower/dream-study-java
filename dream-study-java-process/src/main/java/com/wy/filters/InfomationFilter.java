package com.wy.filters;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 暂时无用的类
 * 
 * @author 万杨
 */
public class InfomationFilter implements Filter {

	private static int COUNT = 1;

	@Override
	public void destroy() {

	}

	/**
	 * 获取用户真实IP地址,不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址, 参考文章：
	 * http://developer.51cto.com/art/201111/305181.htm
	 * 可是,如果通过了多级反向代理的话,X-Forwarded-For的值并不止一个,而是一串IP值,究竟哪个才是真正的用户端的真实IP呢？
	 * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串. 如:X-Forwarded-For:192.168.1.110,
	 * 192.168.1.120, 192.168.1.130, 192.168.1.100 用户真实IP为: 192.168.1.110
	 */
	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) arg0;
		HttpServletResponse resp = (HttpServletResponse) arg1;
		resp.setCharacterEncoding("UTF-8");
		System.out.println(req.getHeader("host"));
		Enumeration<String> headerNames = req.getHeaderNames();
		// host,connection,upgrade-insecure-requests,user-agent,accept,accept-encoding,accept-language
		while (headerNames.hasMoreElements()) {
			System.out.println(headerNames.nextElement());
		}
		COUNT++;
		req.getSession().setAttribute(req.getHeader("host") + req.getServletPath(), COUNT);
		System.out.println(req.getSession().getAttribute(req.getHeader("host") + req.getServletPath()));
		System.out.println(req.getHeaderNames());
		System.out.println(req.getRequestURI());
		System.out.println(req.getQueryString());
		System.out.println(req.getServletPath());
		System.out.println(req.getServletContext());
		String ip = req.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = req.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = req.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = req.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = req.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = req.getRemoteAddr();
		}
		System.out.println(ip);
		chain.doFilter(req, resp);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}
}