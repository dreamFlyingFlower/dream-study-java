package com.wy.listener;

import javax.servlet.http.HttpServlet;

/**
 * 在web程序启动的时候启动,但是会在spring之后启动
 * 需要在xml文件中进行配置
 * @author wanyang
 */
public class PreHttpListener extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	@Override
	public void init() {
		System.out.println("111111111111111111");
	}
}
