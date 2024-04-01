package com.wy.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.CrossOrigin;

/**
 *  简单的通用跨域请求拦截器,若是需要对单个请求进行跨域,可使用{@link CrossOrigin}
 *
 * @author 飞花梦影
 * @date 2021-04-18 21:09:17
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class CorsFilter implements Filter {

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse servletResponse,
            FilterChain chain) throws IOException, ServletException {

        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // 允许跨域访问的域
        response.setHeader("Access-Control-Allow-Origin", "*");
        // 允许使用的请求方法
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,DELETE,OPTIONS");
        // 缓存此次请求的秒数
        response.setHeader("Access-Control-Max-Age", "3600");
        // 是否允许请求带有验证信息
        response.setHeader("Access-Control-Allow-Credentials", "true");
        // 允许脚本访问的返回头
        // response.setHeader("Access-Control-Expose-Headers", arg1);
        // 允许自定义的头部，以逗号隔开，大小写不敏感
        response.setHeader("Access-Control-Allow-Headers", "Accept,Origin, Content-Disposition,Authorization,No-Cache, X-Requested-With, If-Modified-Since, " + "Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
        // 若需要实现ajax跨域访问session,则需要修改请求头如下
        // response.setHeader("Access-Control-Allow-Origin",
        // response.getHeader("Origin"));
        // response.setHeader("XDomainRequestAllowed","1");
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }

}
