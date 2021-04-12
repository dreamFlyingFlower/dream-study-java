package com.wy.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SpringBoot整合Servlet方式二,该方式需要当前类被注入到{@link ServletRegistrationBean}中
 *
 * @author ParadiseWY
 * @date 2020-12-08 19:55:09
 * @git {@link https://github.com/mygodness100}
 */
@Configuration
public class SecondServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("SecondServlet..........");
	}

	@Bean
	public ServletRegistrationBean<SecondServlet> getServletRegistrationBean() {
		ServletRegistrationBean<SecondServlet> bean = new ServletRegistrationBean<>(new SecondServlet());
		bean.addUrlMappings("/second");
		return bean;
	}
}