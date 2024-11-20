package com.wy.minio.upload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 初始化参数
 *
 * @author 飞花梦影
 * @date 2024-01-03 16:45:00
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileTaskVO {

	/**
	 * 文件唯一标识(MD5)
	 */
	@NotBlank(message = "文件标识不能为空")
	private String md5;

	/**
	 * 文件大小(byte)
	 */
	@NotNull(message = "文件大小不能为空")
	private Long totalSize;

	/**
	 * 分片大小(byte)
	 */
	@NotNull(message = "分片大小不能为空")
	private Long chunkSize;

	/**
	 * 文件名称
	 */
	@NotBlank(message = "文件名称不能为空")
	private String fileName;
}