package dream.study.spring.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

/**
 * 异步调用拦截
 *
 * @author 飞花梦影
 * @date 2023-01-03 14:49:49
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Slf4j
public class MyAsyncHandlerInterceptor implements AsyncHandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// HandlerMethod handlerMethod = (HandlerMethod) handler;
		log.info(Thread.currentThread().getName() + "服务调用完成,返回结果给客户端");
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		if (null != ex) {
			System.out.println("发生异常:" + ex.getMessage());
		}
	}

	@Override
	public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 拦截之后,重新写回数据,将原来的success换成如下字符串
		String resp = "DreamFlyingFlower!";
		response.setContentLength(resp.length());
		response.getOutputStream().write(resp.getBytes());

		log.info(Thread.currentThread().getName() + " 进入afterConcurrentHandlingStarted方法");
	}
}