package com.wy.download;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

/**
 * 文件下载方式
 *
 * @author 飞花梦影
 * @date 2025-08-09 16:04:00
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@RestController
@RequestMapping("download")
public class DownloadController {

	/**
	 * 直接通过流下载.适合小文件和低并发场景;性能有限,安全性差,缺乏错误处理,不适合大并发
	 * 
	 * @param response
	 * @throws IOException
	 */
	@GetMapping("/v1")
	public void v1(HttpServletResponse response) throws IOException {
		File file = new File("filePath");
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition",
				"attachment; filename=\"" + URLEncoder.encode(file.getName(), "UTF-8") + "\"");
		try (InputStream in = new FileInputStream(file); OutputStream out = response.getOutputStream()) {
			byte[] buffer = new byte[8192];
			int bytesRead;
			while ((bytesRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
			}
		}
	}

	/**
	 * 通过ResponseEntity下载,优缺点同上
	 * 
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/v2")
	public ResponseEntity<Resource> v2() throws Exception {
		File file = new File("filePath");
		Resource resource = new FileSystemResource(file);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=\"" + URLEncoder.encode(file.getName(), "UTF-8") + "\"")
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(resource);
	}

	/**
	 * 流式处理.非阻塞 I/O,内存占用低,避免内存溢出,适合大文件下载;代码稍复杂,需手动处理流
	 * 
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/v3")
	public ResponseEntity<StreamingResponseBody> v3(HttpServletResponse response) throws Exception {
		File file = new File("filePath");
		StreamingResponseBody body = os -> {
			try (InputStream in = new FileInputStream(file)) {
				byte[] buffer = new byte[8192];
				int bytesRead;
				while ((bytesRead = in.read(buffer)) != -1) {
					os.write(buffer, 0, bytesRead);
				}
			}
		};

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.length()))
				.header(HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=\"" + URLEncoder.encode(file.getName(), "UTF-8") + "\"")
				.body(body);
	}

	/**
	 * 流式处理.非阻塞 I/O,内存占用低,避免内存溢出,适合大文件下载;代码稍复杂,需手动处理流
	 * 
	 * @return
	 * @throws Exception
	 */
	@GetMapping("v4")
	public void v4(HttpServletResponse response) throws Exception {
		File file = new File("D:\\downloads\\47b079cd411af54553a925039fe2fe2f.jpeg");
		StreamingResponseBody body = os -> {
			try (InputStream in = new FileInputStream(file)) {
				byte[] buffer = new byte[8192];
				int bytesRead;
				while ((bytesRead = in.read(buffer)) != -1) {
					os.write(buffer, 0, bytesRead);
				}
			}
		};

		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition",
				"attachment; filename=\"" + URLEncoder.encode(file.getName(), "UTF-8") + "\"");
		try (OutputStream os = response.getOutputStream()) {
			body.writeTo(os);
		}
	}

	/**
	 * 分段下载.支持断点续传和大文件分块下载;实现复杂,需手动处理范围请求逻辑
	 * 
	 * @param headers
	 * @return
	 * @throws Exception
	 */
	@GetMapping("v5")
	public ResponseEntity<Resource> v5(HttpHeaders headers) throws Exception {
		File file = new File("D:\\downloads\\47b079cd411af54553a925039fe2fe2f.jpeg");
		Resource resource = new FileSystemResource(file);
		long fileLength = file.length();
		// 解析 Range 请求头:格式: bytes=0-999
		List<HttpRange> ranges = headers.getRange();
		if (ranges.isEmpty()) {
			// 无 Range 请求时返回完整文件
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION,
							"attachment; filename=\"" + URLEncoder.encode(file.getName(), "UTF-8") + "\"")
					.contentLength(fileLength)
					.body(resource);
		}
		// 处理第一个Range,一般只会有一个
		HttpRange range = ranges.get(0);
		long rangeStart = range.getRangeStart(fileLength);
		long rangeEnd = range.getRangeEnd(fileLength);
		long contentLength = rangeEnd - rangeStart + 1;
		// 自定义 Resource 实现
		try (InputStream inputStream = new FileInputStream(file);) {
			// 跳过起始字节
			inputStream.skip(rangeStart);
			Resource rangeResource = new InputStreamResource(inputStream) {

				@Override
				public long contentLength() {
					return contentLength;
				}
			};
			return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
					.header(HttpHeaders.CONTENT_RANGE, "bytes " + rangeStart + "-" + rangeEnd + "/" + fileLength)
					.contentType(MediaType.APPLICATION_OCTET_STREAM)
					.contentLength(contentLength)
					.body(rangeResource);
		}
	}
}