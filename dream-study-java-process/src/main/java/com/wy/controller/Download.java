package com.wy.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wy.utils.StrUtils;

@Controller
@RequestMapping("download")
public class Download {

	public void download(HttpServletRequest request, HttpServletResponse response) {
		// 若是下载文件,必须传download,文件名
		if (!StrUtils.isBlank(request.getParameter("download"))) {
			String filename = request.getParameter("filename");
			if (!StrUtils.isBlank(filename)) {
				System.out.println(filename);
				response.setContentType(request.getServletContext().getMimeType(filename));
				response.setHeader("Content-Disposition", "attachment;filename=" + filename);
				String path = request.getServletContext()
						.getRealPath(File.separator + "download" + File.separator + filename);
				InputStream is = null;
				try {
					is = new FileInputStream(path);
					OutputStream os = response.getOutputStream();
					int b;
					while ((b = is.read()) != -1) {
						os.write(b);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						is.close();
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			}
		}
	}
}