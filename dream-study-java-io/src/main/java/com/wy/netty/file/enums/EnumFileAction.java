package com.wy.netty.file.enums;

import lombok.Getter;

/**
 * 文件操作类型枚举
 * 
 * @author 飞花梦影
 * @date 2021-09-03 11:50:39
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Getter
public enum EnumFileAction {

	NULL("", ""),
	/**
	 * 上传文件
	 */
	UPLOAD_FILE("上传文件", "uploadFile"),
	/**
	 * 删除文件
	 */
	DELETE_FILE("删除文件", "deleteFile"),
	/**
	 * 替换文件
	 */
	REPLACE_FILE("替换文件", "replaceFile"),
	/**
	 * 生成缩略图
	 */
	CREATE_THUMB_PICTURE("生成缩略图", "createThumbPicture");

	private String value;

	private String name;

	private EnumFileAction(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public static EnumFileAction converByValue(String value) {
		if (UPLOAD_FILE.value.equals(value))
			return UPLOAD_FILE;
		if (DELETE_FILE.value.equals(value))
			return DELETE_FILE;
		if (REPLACE_FILE.value.equals(value))
			return REPLACE_FILE;
		if (CREATE_THUMB_PICTURE.value.equals(value)) {
			return CREATE_THUMB_PICTURE;
		}
		return NULL;
	}
}