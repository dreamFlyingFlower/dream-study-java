package com.wy.enums;

/**
 * 文件类型
 * 
 * @author 飞花梦影
 * @date 2021-01-19 14:33:04
 * @git {@link https://github.com/mygodness100}
 */
public enum FileType implements CommonEnum {

	OTHER("其他"),
	IMAGE("图片"),
	AUDIO("音频"),
	VIDEO("视频"),
	TEXT("文本"),
	COMPRESS("压缩文件");

	private String msg;

	FileType(String msg) {
		this.msg = msg;
	}

	@Override
	public String getMsg() {
		return msg;
	}

	@Override
	public String getName() {
		return name();
	}
}