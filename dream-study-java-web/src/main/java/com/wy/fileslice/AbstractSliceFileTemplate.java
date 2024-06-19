package com.wy.fileslice;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import com.wy.model.FileUpload;
import com.wy.model.FileUploadRequest;

import dream.flying.flower.digest.DigestHelper;
import dream.flying.flower.enums.DateEnum;
import dream.flying.flower.helper.DateTimeHelper;
import dream.flying.flower.io.file.FileHelper;
import dream.flying.flower.io.file.FileNameHelper;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2022-05-06 16:57:04
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Slf4j
public abstract class AbstractSliceFileTemplate implements SliceFileTemplate {

	protected final static Map<String, Object> UPLOAD_FILE_CACHE = new ConcurrentHashMap<>();

	public abstract boolean upload(FileUploadRequest param);

	protected String generateUploadDirectory(FileUploadRequest param) {
		String concatFileName =
				FileNameHelper.concatFileName(param.getFile().getOriginalFilename(), DigestHelper.uuid());
		return "/app/tmp/file/" + DateTimeHelper.formatDate(DateEnum.DATE_NONE.getPattern()) + File.separator
				+ concatFileName;
	}

	/**
	 * 创建临时文件
	 * 
	 * @param param
	 * @return
	 */
	protected File createTmpFile(FileUploadRequest param) {
		param.setUploadDirectory(generateUploadDirectory(param));
		String uploadDirPath = param.getUploadDirectory();
		File tmpDir = new File(uploadDirPath);
		if (!tmpDir.exists()) {
			tmpDir.mkdirs();
		}
		return new File(uploadDirPath, param.getFile().getOriginalFilename() + "_tmp");
	}

	@Override
	public FileUpload sliceUploadFile(FileUploadRequest param) {
		boolean isOk = this.upload(param);
		if (isOk) {
			File tmpFile = this.createTmpFile(param);
			FileUpload fileUploadDTO = this.saveUploadFile(param.getFile().getOriginalFilename(), tmpFile);
			return fileUploadDTO;
		}
		try {
			String md5 = FileHelper.getFileMd5(param.getFile().getInputStream());
			Map<Integer, String> map = new HashMap<>();
			map.put(param.getChunkIndex(), md5);
			return FileUpload.builder().chunkMd5Info(map).build();
		} catch (IOException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 检查并修改文件上传进度
	 */
	public boolean checkAndSetUploadProgress(FileUploadRequest param, String uploadDirPath) {
		String fileName = param.getFile().getOriginalFilename();
		File confFile = new File(uploadDirPath, fileName + ".conf");
		byte isComplete = 0;
		try (RandomAccessFile accessConfFile = new RandomAccessFile(confFile, "rw");) {
			// 把该分段标记为 true 表示完成
			System.out.println("set part " + param.getChunkIndex() + " complete");
			// 创建conf文件,文件长度为总分片数,每上传一个分块即向conf文件中写入一个127,没上传的位置默认0,已上传的就是Byte.MAX_VALUE
			accessConfFile.setLength(param.getChunks());
			accessConfFile.seek(param.getChunkIndex());
			accessConfFile.write(Byte.MAX_VALUE);
			// completeList 检查是否全部完成,如果数组里是否全部都是127(全部分片都成功上传)
			byte[] completeList = FileHelper.readToByte(confFile);
			isComplete = Byte.MAX_VALUE;
			for (int i = 0; i < completeList.length && isComplete == Byte.MAX_VALUE; i++) {
				// 与运算, 如果有部分没有完成则 isComplete 不是 Byte.MAX_VALUE
				isComplete = (byte) (isComplete & completeList[i]);
				System.out.println("check part " + i + " complete?:" + completeList[i]);
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return saveUploadProgress(param, uploadDirPath, fileName, confFile, isComplete);
	}

	/**
	 * 存储文件上传进度信息
	 */
	@Override
	public boolean saveUploadProgress(FileUploadRequest param, String uploadDirPath, String fileName, File confFile,
			byte isComplete) {
		if (isComplete == Byte.MAX_VALUE) {
			UPLOAD_FILE_CACHE.put(FileConsts.FILE_UPLOAD_STATUS + param.getMd5(), true);
			UPLOAD_FILE_CACHE.remove(FileConsts.FILE_MD5_KEY + param.getMd5());
			confFile.delete();
			return true;
		} else {
			if (Objects.isNull(UPLOAD_FILE_CACHE.get(param.getMd5()))) {
				UPLOAD_FILE_CACHE.put(FileConsts.FILE_UPLOAD_STATUS + param.getMd5(), false);
				UPLOAD_FILE_CACHE.put(FileConsts.FILE_MD5_KEY + param.getMd5(),
						uploadDirPath + File.separator + fileName + ".conf");
			}
			return false;
		}
	}

	/**
	 * 保存文件操作
	 */
	@Override
	public FileUpload saveUploadFile(String fileName, File tmpFile) {
		FileUpload fileUploadDTO = null;
		try {
			fileUploadDTO = renameFile(tmpFile, fileName);
			if (fileUploadDTO.isUploadComplete()) {
				System.out.println("upload complete !!" + fileUploadDTO.isUploadComplete() + " name=" + fileName);
				// TODO 保存文件信息到数据库
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {

		}
		return fileUploadDTO;
	}

	/**
	 * 文件重命名
	 * 
	 * @param sourceFile 源文件
	 * @param targetFileName 新文件名,不带路径
	 */
	private FileUpload renameFile(File sourceFile, String targetFileName) {
		FileHelper.checkFile(sourceFile);
		String filePath = sourceFile.getParent() + File.separator + targetFileName;
		File newFile = new File(sourceFile.getParent(), targetFileName);
		// 修改文件名
		boolean uploadFlag = sourceFile.renameTo(newFile);
		return FileUpload.builder().uploadTime(new Date()).uploadComplete(uploadFlag).filePath(filePath)
				.fileSize(newFile.length()).fileExtension(FileNameHelper.getExtension(targetFileName))
				.fileId(targetFileName).build();
	}
}