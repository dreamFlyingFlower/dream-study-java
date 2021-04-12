package com.wy.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Session:放在服务区中的一块内存区域,保存客户端所有信息,以Cookie相关联
 * 
 * @author 飞花梦影
 * @date 2021-04-11 15:00:42
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class SessionServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=utf-8");
		// 获得session,有则返回,没有则创建
		req.getSession();
		// 设置页面不缓存
		resp.setHeader("expires", "-1");
		resp.setHeader("pragma", "no-cache");
		resp.setHeader("cache-control", "no-cache");
	}
}
