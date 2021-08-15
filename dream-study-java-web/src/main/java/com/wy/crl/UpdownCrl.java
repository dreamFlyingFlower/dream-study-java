package com.wy.crl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "文件上传和下载")
@RestController
@RequestMapping("fileinfo")
public class UpdownCrl {

	@ApiOperation(value = "直接使用流进行文件的传输,前端形成文件,如导出excel表", hidden = true)
	@PostMapping("exportForm")
	public void curDeviceStatus(HttpServletResponse response,
			@RequestBody(required = false) Map<String, Object> params) {
	}

	/**
	 * 文件下载.若需要断点续传,可使用java自带的读取字节的方式下载
	 * 
	 * @param fileType
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "文件已经形成,重新形成流传输,前端形成文件,如下载一张图片", hidden = true)
	@RequestMapping("downloadFile")
	public ResponseEntity<byte[]> downloadFile(String fileType, HttpServletRequest request) {
		HttpHeaders headers = new HttpHeaders();
		ResponseEntity<byte[]> entity = null;
		try (InputStream in = new FileInputStream(new File("d:/myImg/001.png"));) {
			byte[] bytes = new byte[in.available()];
			String filename = "001.png";
			// 处理IE下载文件的中文名称乱码的问题
			String header = request.getHeader("User-Agent").toUpperCase();
			if (header.contains("MSIE") || header.contains("TRIDENT") || header.contains("EDGE")) {
				// IE下载文件名空格变+号问题
				filename = URLEncoder.encode(filename, "utf-8");
				filename = filename.replace("+", "%20");
			} else {
				// 下载解决中文文件名乱码,需要根据实际情况修改
				filename = new String(filename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
			}
			in.read(bytes);
			// 通知浏览器以attachment方式打开图片
			headers.add("Content-Disposition", "attachment;filename=" + filename);
			// HttpHeaders headers = new HttpHeaders();
			// headers.setContentDispositionFormData("attachment", filename);
			// 二进制文件流
			// headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			entity = new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return entity;
	}

	// 前端不用form表单上传文件,可以拿到file类型input的files属性,每个标签节点都有这个属性
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
	 * 文件上传
	 * 
	 * @param file
	 * @param fileType
	 * @param request
	 * @param response
	 * @return
	 */
	@ApiOperation(value = "文件上传", hidden = true)
	@PostMapping(value = "uploadFile", produces = { MediaType.APPLICATION_JSON_VALUE },
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String uploadFile(@RequestPart(value = "file") MultipartFile file,
			@RequestParam(value = "fileType") String fileType, HttpServletRequest request,
			HttpServletResponse response) {
		String orgFilename = file.getOriginalFilename();
		String suffix = orgFilename.substring(orgFilename.lastIndexOf("."));// 后缀
		String uuid = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
		File dest = new File("f:/b13/" + uuid + suffix);
		try {
			file.transferTo(dest);
			return dest.getCanonicalPath();// 文件的绝对路径
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}
		return "failure";
	}
}