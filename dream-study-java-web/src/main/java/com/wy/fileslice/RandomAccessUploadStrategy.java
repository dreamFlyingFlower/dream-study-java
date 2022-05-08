package com.wy.fileslice;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;

import com.wy.model.FileUploadRequest;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2022-05-06 16:53:51
 */
@Slf4j
public class RandomAccessUploadStrategy extends AbstractSliceFileTemplate {

	@Value("${upload.chunkSize}")
	private long defaultChunkSize;

	@Override
	public boolean upload(FileUploadRequest param) {
		File tmpFile = super.createTmpFile(param);
		try (RandomAccessFile accessTmpFile = new RandomAccessFile(tmpFile, "rw");) {
			String uploadDirPath = param.getUploadDirectory();
			// 这个必须与前端设定的值一致
			long chunkSize =
					Objects.isNull(param.getChunkSize()) ? defaultChunkSize * 1024 * 1024 : param.getChunkSize();
			long offset = chunkSize * param.getChunkIndex();
			// 定位到该分片的偏移量
			accessTmpFile.seek(offset);
			// 写入该分片数据
			accessTmpFile.write(param.getFile().getBytes());
			return super.checkAndSetUploadProgress(param, uploadDirPath);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return false;
	}
}