package com.wy.filters;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * 一个下载的例子
 * @author wanyang
 */
public class DownLoadFilter implements Filter {

	@Override
	public void destroy() {
		
	}

	/**
	 * 本例可用于post方式请求下载文件
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain arg2)
			throws IOException, ServletException {
		HttpServletResponse resp = (HttpServletResponse)response;
		resp.setHeader("content-disposition", "attachment;filename=22.xlsx");
		//获得本地文件的输入流
		InputStream is = this.getClass().getResourceAsStream("222.xlsx");
		//获得响应的输出流
		OutputStream os = resp.getOutputStream();
		byte[] bytes = new byte[1024];
		int i =0;
		while((i = is.read()) != -1) {
			os.write(bytes,0,i);
		}
		os.close();
		is.close();
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}


}
