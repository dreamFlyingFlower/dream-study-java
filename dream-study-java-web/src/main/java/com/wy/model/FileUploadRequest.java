package com.wy.model;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2022-05-06 17:42:49
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileUploadRequest {

	private Long chunkSize;

	private Integer chunkIndex;

	private Integer chunks;

	private MultipartFile file;

	private String md5;

	private String path;
}