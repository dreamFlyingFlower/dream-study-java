package com.wy.study;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 同步Servlet
 * 
 * @author 飞花梦影
 * @date 2022-09-02 09:45:54
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@WebServlet("/sync")
public class SyncServlet extends HttpServlet {

	private static final long serialVersionUID = -2907189875428836319L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
		long startTime = System.currentTimeMillis();
		doSome(request, response);
		long endTime = System.currentTimeMillis();
		System.out.println("同步操作时web服务器耗时：" + (endTime - startTime));
	}

	private void doSome(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		response.getWriter().println("Done!");
	}
}