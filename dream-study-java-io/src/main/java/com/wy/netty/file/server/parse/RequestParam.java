package com.wy.netty.file.server.parse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.wy.lang.StrTool;
import com.wy.netty.file.Constants;

import io.netty.handler.codec.http.multipart.FileUpload;
import lombok.Getter;
import lombok.Setter;

/**
 * 请求参数
 * 
 * @author 飞花梦影
 * @date 2021-09-03 12:03:42
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Getter
@Setter
public class RequestParam {

	// 文件操作类型
	private String action;

	// 鉴权信息
	private String userName;

	private String pwd;

	private Map<String, String> otherParams = new HashMap<String, String>();

	// 是否需要转为缩略图
	private String thumbMark = Constants.THUMB_MARK_NO;

	// 上传的文件对象
	private FileUpload fileUpload;

	// 上传的文件路径
	private String filePath;

	// 上传的文件名称
	private String fileName;

	// 上传的文件contentType
	private String fileContentType;

	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPwd() {
		return this.pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public Map<String, String> getOtherParams() {
		return this.otherParams;
	}

	public FileUpload getFileUpload() {
		return this.fileUpload;
	}

	public void setFileUpload(FileUpload fileUpload) {
		this.fileUpload = fileUpload;
	}

	public String getThumbMark() {
		return this.thumbMark;
	}

	public void setThumbMark(String thumbMark) {
		this.thumbMark = thumbMark;
	}

	public void putOtherParam(String key, String value) {
		this.otherParams.put(key, value);
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("\r\nNETTY WEB Server\r\n");
		sb.append("===================================\r\n");
		sb.append("\r\n\r\n");
		if (StrTool.isNotBlank(getUserName())) {
			sb.append("UserName=" + getUserName() + "\r\n");
		}
		if (StrTool.isNotBlank(getPwd())) {
			sb.append("pwd=" + getPwd() + "\r\n");
		}
		if (StrTool.isNotBlank(getAction())) {
			sb.append("action=" + getAction() + "\r\n");
		}
		if (StrTool.isNotBlank(this.fileName)) {
			sb.append("fileName=" + getFileName() + "\r\n");
		}
		if (StrTool.isNotBlank(this.fileContentType)) {
			sb.append("fileContentType=" + getFileContentType() + "\r\n");
		}
		if (fileUpload != null) {
			try {
				sb.append("fileSize=" + fileUpload.getFile().length() / 1024 + " KB \r\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (this.otherParams != null) {
			for (Map.Entry<String, String> item : this.otherParams.entrySet()) {
				sb.append((String) item.getKey() + "=" + (String) item.getValue() + "\r\n");
			}
		}
		return sb.toString();
	}
}