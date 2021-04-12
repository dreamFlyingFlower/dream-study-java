package com.wy.crl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
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
import org.springframework.web.bind.annotation.RequestMethod;
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
			@RequestBody(required = false) Map<String, Object> params) {}

	/**
	 * 文件下载
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
			String imageName = "001.png";
			// 处理IE下载文件的中文名称乱码的问题
			String header = request.getHeader("User-Agent").toUpperCase();
			if (header.contains("MSIE") || header.contains("TRIDENT") || header.contains("EDGE")) {
				imageName = URLEncoder.encode(imageName, "utf-8");
				imageName = imageName.replace("+", "%20"); // IE下载文件名空格变+号问题
			} else {
				imageName = new String(imageName.getBytes(), "iso-8859-1");
			}
			in.read(bytes);
			// 二进制文件流
			// headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.add("Content-Disposition", "attachment;filename=" + imageName);
			entity = new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);
			// entity = new ResponseEntity<byte[]>(org.apache.commons.io.FileUtils.readFileToByteArray(new
			// File("d:/myImg/001.png")), headers, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entity;
	}

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
	@RequestMapping(method = RequestMethod.POST, value = "uploadFile", produces = { MediaType.APPLICATION_JSON_VALUE },
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