package com.wy.servlet;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Cookie:原生Cookie Servlet技术,Cookie是放在客户端的一个文件,用来存储浏览器相关数据,最大只能存储4KB数据
 * 
 * @author 飞花梦影
 * @date 2021-04-11 14:39:29
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class CookieServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=utf-8");
		// 默认获得当前路径以及上级(递归)路径浏览器保存的所有Cookie,不能获得下级路径保存的Cookie
		Cookie[] cookies = req.getCookies();
		for (Cookie cookie : cookies) {
			System.out.println(cookie.getName() + ":" + cookie.getValue());
		}
		Cookie cookie = new Cookie("test", "test");
		// 设置cookie超时时间:0,删除;正数,单位s;负数,不超时,永久保存
		cookie.setMaxAge(0);
		// 默认设置的cookie只能在当前目录以及下级(递归)目录中被获得,设置所有的路径都能获得
		cookie.setPath("/");
		// cookie中设置中文
		cookie.setValue(URLEncoder.encode("中文", "UTF-8"));
		// 获得中文
		System.out.println(URLDecoder.decode(cookie.getValue(), "UTF-8"));
		// 通知浏览器存储cookie
		resp.addCookie(cookie);
	}
}