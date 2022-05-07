package com.wy.strategy;

import com.wy.model.FileUpload;
import com.wy.model.FileUploadRequest;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2022-05-07 08:42:13
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public interface SliceFileTemplate {

	FileUpload sliceUpload(FileUploadRequest param);
}