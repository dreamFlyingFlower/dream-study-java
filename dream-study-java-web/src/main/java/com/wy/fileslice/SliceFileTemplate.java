package com.wy.fileslice;

import java.io.File;

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

	/**
	 * 切割文件
	 * 
	 * @param param 上传文件参数
	 * @return 切割后的文件对象
	 */
	FileUpload sliceUploadFile(FileUploadRequest param);

	/**
	 * 保存切割的单个文件
	 * 
	 * @param fileName 文件名
	 * @param tmpFile 临时文件
	 * @return 切割后的文件对象
	 */
	FileUpload saveUploadFile(String fileName, File tmpFile);

	/**
	 * 存储文件上传进度
	 * 
	 * @param param 文件上传参数
	 * @param uploadDirPath 文件上传目录
	 * @param fileName 切割后的文件名称
	 * @param confFile 配置文件
	 * @param isComplete 上传是否完成.true->完成,false->未完成
	 * @return 上传是否完成.true->完成,false->未完成
	 */
	boolean saveUploadProgress(FileUploadRequest param, String uploadDirPath, String fileName, File confFile,
			byte isComplete);
}