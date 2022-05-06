package com.wy.model;

import java.util.Date;
import java.util.Map;

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
public class FileUpload {

	private String fileId;

	private boolean uploadComplete;

	private String filePath;

	private String fileExtension;

	private Long fileSize;

	private Date uploadTime;

	private Map<Integer, String> chunkMd5Info;
}