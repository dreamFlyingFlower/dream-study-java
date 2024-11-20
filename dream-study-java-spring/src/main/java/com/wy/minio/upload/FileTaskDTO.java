package com.wy.minio.upload;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2024-01-03 16:22:56
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FileTaskDTO implements Serializable {

	private static final long serialVersionUID = 3651817440517885146L;

	/**
	 * 是否完成上传（是否已经合并分片）
	 */
	private Boolean finished;

	/**
	 * 文件地址
	 */
	private String path;

	/**
	 * 上传记录
	 */
	private FileTaskRecordDTO taskRecord;
}