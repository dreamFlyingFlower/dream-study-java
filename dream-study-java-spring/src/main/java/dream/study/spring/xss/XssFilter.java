package dream.study.spring.xss;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * 加入拦截器
 *
 * @author 飞花梦影
 * @date 2023-12-06 23:08:15
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class XssFilter implements Filter {

	// 忽略权限检查的url地址
	private final String[] excludeUrls = new String[] { "null" };

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		String pathInfo = req.getPathInfo() == null ? "" : req.getPathInfo();
		// 获取请求url的后两层
		String url = req.getServletPath() + pathInfo;
		System.out.println(url);
		// 获取请求你ip后的全部路径
		String uri = req.getRequestURI();
		// 注入xss过滤器实例
		XssHttpServletRequestWraper reqW = new XssHttpServletRequestWraper(req);
		// 过滤掉不需要的Xss校验的地址
		for (String str : excludeUrls) {
			if (uri.indexOf(str) >= 0) {
				chain.doFilter(request, response);
				return;
			}
		}
		// 过滤
		chain.doFilter(reqW, response);
	}
}