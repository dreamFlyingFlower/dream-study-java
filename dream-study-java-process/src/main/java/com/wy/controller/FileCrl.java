package com.wy.controller;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文件下载,根据文件后缀名选择文件,并且加入时间到文件路径中 FIXME
 *
 * @author ParadiseWY
 * @date 2018-03-09 23:13:05
 * @git {@link https://github.com/mygodness100}
 */
@RestController
@RequestMapping("file")
public class FileCrl {

	/**
	 * 利用spring的文件下载,不能实现断点续传,也不能下载太大(1.5G上)的文件,否则会造成内存溢出
	 * 若需要断点续传,可使用java自带的读取字节的方式下载
	 */
	@RequestMapping(value = "download", method = RequestMethod.GET)
	public ResponseEntity<byte[]> download(HttpServletRequest req, String filename) {
		try {
			// 获取项目根目录文件下的image文件路径
			String path = req.getSession().getServletContext().getRealPath("/image/");
			File file = new File(path + File.separator + filename);
			HttpHeaders headers = new HttpHeaders();
			// 下载解决中文文件名乱码,需要根据实际情况修改
			String downloadfile = new String(filename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
			// 通知浏览器以attachment方式打开图片
			headers.setContentDispositionFormData("attachment", downloadfile);
			// 二进制文件流
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			return new ResponseEntity<>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}