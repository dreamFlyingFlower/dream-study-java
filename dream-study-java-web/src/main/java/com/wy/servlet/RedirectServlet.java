package com.wy.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Redirect:重定向,即立刻跳转到指定页面,同时浏览器URL会显示重定向后的URL
 * 
 * 重定向:浏览器请求服务器一次,服务器告诉浏览器需要请求另外一个地址,浏览器再请求另外的地址
 * 转发:浏览器请求服务器一次,服务器直接请求另外的地址,将最终的结果返回给浏览器,中间不需要浏览器的再次介入
 * 
 * @author 飞花梦影
 * @date 2021-04-11 14:19:10
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class RedirectServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * 假设当前请求页面为test.html
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 重定向,可以指定任意URL
		// 当请求某个URL时,可以直接重定向到其他URL,最终是2次请求
		resp.sendRedirect("http://localhost:9090/redirect.html");
		// 下面等价于上面一句
		// resp.setStatus(302);
		// resp.setHeader("location", "http://localhost:9090/redirect.html");
		// 转发,只能指定当前web项目的URL
		PrintWriter writer = resp.getWriter();
		// 将不会在页面上输出
		writer.print("页面转发前");
		// 转发到指定页面,指定页面中需要输出的内容才会输出
		RequestDispatcher dispatcher = req.getRequestDispatcher("forward.html");
		// 当前页面将请求转发到另外一个地址
		dispatcher.forward(req, resp);
	}
}