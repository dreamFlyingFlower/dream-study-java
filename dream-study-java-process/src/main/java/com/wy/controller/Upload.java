package com.wy.controller;

import java.io.File;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.wy.result.Result;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("upload")
@Slf4j
public class Upload {

	@RequestMapping(value = "/uploadFile")
	public void upload(HttpServletRequest request) {
		CommonsMultipartResolver multi = new CommonsMultipartResolver(request.getSession().getServletContext());
		if (multi.isMultipart(request)) {
			MultipartHttpServletRequest multiReq = (MultipartHttpServletRequest) request;
			Iterator<String> it = multiReq.getFileNames();
			while (it.hasNext()) {
				MultipartFile file = multiReq.getFile(it.next());
				if (file != null) {
					String fileName = "upload" + file.getName();
					System.out.println(System.getProperty("user.dir") + File.separator + fileName);
					String path = System.getProperty("user.dir") + File.separator + fileName;
					File localFile = new File(path);
					try {
						file.transferTo(localFile);
					} catch (Exception e) {
						log.info(e.getMessage());
					}
				}
			}
		}
	}

	// var fileupload = $("input[name='file']")[0].files[0];
	// var formdata = new FormData();
	// formdata.append('file',fileupload);
	// formdata.append('test',"test");
	// $.ajax({
	// url: 'http://127.0.0.1:8080/com.wy.springjava/upload/testUpload',
	// type: 'post',
	// processData:false,
	// contentType:false,
	// cache:false,
	// data:formdata
	// });
	/**
	 * 前端不用form表单上传文件,可以拿到file类型input的files属性,每个标签节点都有这个属性
	 * 
	 * @param file
	 */
	@PostMapping("testUpload")
	public void testUpload(@RequestParam("file") MultipartFile file) {
		// CommonsMultipartResolver multi = new
		// CommonsMultipartResolver(getRequest().getSession().getServletContext());
		// Iterator<String> file = ((MultipartHttpServletRequest)req).getFileNames();
		if (file != null) {
			System.out.println(file.getOriginalFilename());
		} else {
			System.out.println(111);
		}
	}

	/**
	 * CommonsMultipartFile是MultipartFile的子类
	 */
	@PostMapping("testUpload1")
	public Result<?> testUpload(CommonsMultipartFile[] file) {
		if (file != null && file.length > 0) {
			for (CommonsMultipartFile fileChild : file) {
				fileChild.getOriginalFilename();
			}
		}
		return Result.ok("部分上传成功", null);
	}
}