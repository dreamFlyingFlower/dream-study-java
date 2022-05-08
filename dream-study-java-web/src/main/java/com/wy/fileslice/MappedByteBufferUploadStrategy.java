package com.wy.fileslice;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;

import com.wy.model.FileUploadRequest;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2022-05-06 16:56:36
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Slf4j
public class MappedByteBufferUploadStrategy extends AbstractSliceFileTemplate {

	@Value("${upload.chunkSize:10}")
	private long defaultChunkSize;

	@Override
	public boolean upload(FileUploadRequest param) {
		MappedByteBuffer mappedByteBuffer = null;
		File tmpFile = super.createTmpFile(param);
		try (RandomAccessFile tempRaf = new RandomAccessFile(tmpFile, "rw");) {
			String uploadDirPath = param.getPath();
			long chunkSize =
					Objects.isNull(param.getChunkSize()) ? defaultChunkSize * 1024 * 1024 : param.getChunkSize();
			// 写入该分片数据
			long offset = chunkSize * param.getChunkIndex();
			byte[] fileData = param.getFile().getBytes();
			mappedByteBuffer = tempRaf.getChannel().map(FileChannel.MapMode.READ_WRITE, offset, fileData.length);
			mappedByteBuffer.put(fileData);
			return super.checkAndSetUploadProgress(param, uploadDirPath);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} finally {
			mappedByteBuffer.clear();
		}
		return false;
	}
}