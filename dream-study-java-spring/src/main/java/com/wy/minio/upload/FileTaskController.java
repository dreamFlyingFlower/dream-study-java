package com.wy.minio.upload;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.result.Result;

import lombok.AllArgsConstructor;

/**
 * Minio断点续传
 * 
 * https://github.com/robinsyn/MinIO_Demo/tree/master/minio-upload-web
 *
 * @author 飞花梦影
 * @date 2024-01-03 16:11:08
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@RestController
@RequestMapping("minio")
@AllArgsConstructor
public class FileTaskController {

	private final FileTaskService minioUploadService;

	/**
	 * 根据MD5获取是否存在相同文件,若存在,返回TaskInfoDTO
	 * 
	 * @param identifier 文件md5
	 * @return Result
	 */
	@GetMapping("/{md5}")
	public Result<?> taskInfo(@PathVariable("md5") String md5) {
		return Result.ok(minioUploadService.getTaskInfo(md5));
	}

	/**
	 * 创建一个上传任务
	 * 
	 * @return Result
	 */
	@PostMapping
	public Result<?> initTask(@Valid @RequestBody FileTaskVO param) {
		return Result.ok(minioUploadService.initTask(param));
	}

	/**
	 * 获取每个分片的预签名上传地址
	 * 
	 * @param identifier
	 * @param partNumber
	 * @return Result
	 */
	@GetMapping("/{identifier}/{partNumber}")
	public Result<?> preSignUploadUrl(@PathVariable("identifier") String identifier,
			@PathVariable("partNumber") Integer partNumber) {
		FileTaskEntity task = minioUploadService.getByMd5(identifier);
		if (task == null) {
			return Result.error("分片任务不存在");
		}
		Map<String, String> params = new HashMap<>();
		params.put("partNumber", partNumber.toString());
		params.put("uploadId", task.getUploadId());
		return Result.ok(minioUploadService.genPreSignUploadUrl(task.getBucketName(), task.getObjectKey(), params));
	}

	/**
	 * 合并分片
	 * 
	 * @param identifier
	 * @return Result
	 */
	@PostMapping("/merge/{identifier}")
	public Result<?> merge(@PathVariable("identifier") String identifier) {
		minioUploadService.merge(identifier);
		return Result.ok();
	}
}