package com.wy.study;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 异步Servlet
 * 
 * @author 飞花梦影
 * @date 2022-09-02 09:45:38
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@WebServlet(value = "/async", asyncSupported = true)
public class AsyncServlet extends HttpServlet {

	private static final long serialVersionUID = 4853853843733183308L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
		long startTime = System.currentTimeMillis();

		// 获取异步上下文,开启异步操作(完成异步线程间的通讯)
		AsyncContext asyncContext = request.startAsync();
		// 获取NIO的异步请求与响应
		ServletRequest asyncContextRequest = asyncContext.getRequest();
		ServletResponse asyncContextResponse = asyncContext.getResponse();

		// 异步执行耗时操作
		CompletableFuture.runAsync(() -> doSome(asyncContext, asyncContextRequest, asyncContextResponse));

		long endTime = System.currentTimeMillis();
		System.out.println("异步操作时web服务器耗时(毫秒)：" + (endTime - startTime));
	}

	private void doSome(AsyncContext asyncContext, ServletRequest request, ServletResponse response) {
		try {
			TimeUnit.SECONDS.sleep(5);
			response.getWriter().println("Done!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 耗时业务代码通知异步操作,任务完成
		asyncContext.complete();
	}
}