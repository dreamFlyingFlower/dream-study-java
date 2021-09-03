package com.wy.netty.file.client.handler;

import java.net.URI;

import com.wy.netty.file.Constants;
import com.wy.netty.file.enums.EnumFileAction;

import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestEncoder;

/**
 * 客户端生成缩略图处理器
 * 
 * @author 飞花梦影
 * @date 2021-09-03 11:17:43
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class CreateThumbPictureClientHandler extends WrapFileClientHandler {

	private String filePath;

	public CreateThumbPictureClientHandler(String host, URI uri, String userName, String pwd, String filePath) {
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
			bodyRequestEncoder.addBodyAttribute(Constants.ACTION_KEY, EnumFileAction.CREATE_THUMB_PICTURE.getValue());
			// 设置文件路径
			bodyRequestEncoder.addBodyAttribute(Constants.FILE_PATH_KEY, this.filePath);
			// 设置鉴权
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