package com.wy.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wy.common.Constant;

public class FileUtils {

	// 文件后缀名匹配
	private static final Map<String, List<String>> FILE_SUFFIXMAP = new HashMap<String, List<String>>() {
		private static final long serialVersionUID = 1L;
		{
			put(Constant.FILE_IMAGE, Arrays.asList(".BMP", ".PNG", ".GIF", ".JPG", ".JPEG"));
			put(Constant.FILE_VEDIO, Arrays.asList(".MP4", ".AVI", ".3GP", ".RM", ".RMVB", ".WMV"));
			put(Constant.FILE_AUDIO, Arrays.asList(".AMR", ".MP3", ".WMA", ".WAV", ".MID"));
			put(Constant.FILE_TEXT, Arrays.asList(".TXT", ".JSON", ".XML"));
		}
	};

	/**
	 * 检查文件是否匹配,并返回文件存贮路径
	 * 
	 * @param suffix 后缀名,需要带上点
	 */
	public static String getFileType(String suffix) {
		for (Map.Entry<String, List<String>> entry : FILE_SUFFIXMAP.entrySet()) {
			if (entry.getValue().contains(suffix.toUpperCase())) {
				return entry.getKey();
			}
		}
		return Constant.FILE_OTHER;
	}
}