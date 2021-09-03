package com.wy.netty.file.client.handler;

import java.net.URI;

import com.wy.netty.file.Constants;
import com.wy.netty.file.enums.EnumFileAction;

import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestEncoder;

/**
 * 客户端删除文件处理器
 * 
 * @author 飞花梦影
 * @date 2021-09-03 11:19:22
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class DeleteFileClientHandler extends WrapFileClientHandler {

	private String filePath;

	public DeleteFileClientHandler(String host, URI uri, String filePath, String userName, String pwd) {
		super(host, uri, userName, pwd);
		this.filePath = filePath;
	}

	public HttpPostRequestEncoder wrapRequestData(HttpDataFactory factory) {
		HttpPostRequestEncoder bodyRequestEncoder = null;
		try {
			bodyRequestEncoder = new HttpPostRequestEncoder(factory, getRequest(), false);
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (HttpPostRequestEncoder.ErrorDataEncoderException e) {
			e.printStackTrace();
		}
		try {
			// 设置请求方式
			bodyRequestEncoder.addBodyAttribute("getform", "POST");
			// 设置文件操作类型
			bodyRequestEncoder.addBodyAttribute(Constants.ACTION_KEY, EnumFileAction.DELETE_FILE.getValue());
			// 设置文件路径
			bodyRequestEncoder.addBodyAttribute(Constants.FILE_PATH_KEY, this.filePath);
			// 鉴权
			bodyRequestEncoder.addBodyAttribute(Constants.USER_NAME_KEY, super.getUserName());
			bodyRequestEncoder.addBodyAttribute(Constants.PWD_KEY, super.getPwd());
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (HttpPostRequestEncoder.ErrorDataEncoderException e) {
			e.printStackTrace();
		}
		try {
			bodyRequestEncoder.finalizeRequest();
		} catch (HttpPostRequestEncoder.ErrorDataEncoderException e) {
			e.printStackTrace();
		}
		return bodyRequestEncoder;
	}
}