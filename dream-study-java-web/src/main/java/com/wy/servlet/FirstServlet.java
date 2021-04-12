package com.wy.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * SpringBoot整合Servlet方式一,需要开启{@link ServletComponentScan},该注解扫描{@link WebServlet}
 *
 * <servlet>
 * 		<servlet-name>firstServlet</servlet-name>
 *		<servlet-class>com.bjsxt.servlet.FirstServlet</servlet-class>
 * </servlet>
 *
 * <servlet-mapping>
 * 		<servlet-name>firstServlet</servlet-name>
 * 		<url-pattern>/first</url-pattern>
 * </servlet-mapping>
 *
 * @author ParadiseWY
 * @date 2020-12-08 19:46:51
 * @git {@link https://github.com/mygodness100}
 */
@WebServlet(name = "firstServlet", urlPatterns = "/first")
public class FirstServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("FirstServlet............");
	}
}