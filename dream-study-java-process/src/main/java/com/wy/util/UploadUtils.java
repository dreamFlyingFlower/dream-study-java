package com.wy.util;

import java.io.File;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.wy.enums.DateEnum;

public class UploadUtils {

	public static String getExt(String fileName) {
		if (fileName != null && "" != fileName) {
			return fileName.substring(fileName.lastIndexOf("."));
		}
		return null;
	}

	public static void deleteFile(String fileRealPath) {
		if (fileRealPath != null && "" != fileRealPath) {
			File file = new File(fileRealPath);
			if (file.exists()) {
				file.delete();
			}
		}
	}

	public static String saveUploadFile(File upload, HttpServletRequest request) {
		return saveUploadFile(upload, ".xls", request);
	}

	public static String saveUploadFile(File upload, String fileExt, HttpServletRequest request) {
		String basePath = request.getServletContext().getRealPath("/WEB-INF/upload");
		String subPath = DateTool.format(new Date(), DateEnum.DATE_NONE);
		File dir = new File(basePath + subPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String path = basePath + subPath + UUID.randomUUID().toString() + fileExt;
		File dest = new File(path);
		upload.renameTo(dest);
		return path;
	}
}