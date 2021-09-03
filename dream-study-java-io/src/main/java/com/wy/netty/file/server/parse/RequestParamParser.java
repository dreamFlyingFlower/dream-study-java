package com.wy.netty.file.server.parse;

import java.util.List;

import com.wy.netty.file.Constants;

import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.FileUpload;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;

/**
 * 请求参数解析工具类
 * 
 * @author 飞花梦影
 * @date 2021-09-03 12:05:00
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class RequestParamParser {

	public static void parseParams(HttpPostRequestDecoder decoder, RequestParam requestParams) {
		if (decoder == null) {
			return;
		}
		if (requestParams == null)
			requestParams = new RequestParam();
		try {
			List<InterfaceHttpData> datas = decoder.getBodyHttpDatas();
			if (datas != null) {
				for (InterfaceHttpData data : datas)
					if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
						// 接收到的是普通参数
						Attribute attribute = (Attribute) data;
						String value = attribute.getValue();
						String name = attribute.getName();
						if (Constants.PWD_KEY.equals(name))
							requestParams.setPwd(value);
						else if (Constants.USER_NAME_KEY.equals(name))
							requestParams.setUserName(value);
						else if (Constants.THUMB_MARK_KEY.equals(name))
							requestParams.setThumbMark(value);
						else if (Constants.ACTION_KEY.equals(name))
							requestParams.setAction(value);
						else if (Constants.FILE_PATH_KEY.equals(name))
							requestParams.setFilePath(value);
						else if (Constants.FILE_NAME_KEY.equals(name))
							requestParams.setFileName(value);
						else
							requestParams.putOtherParam(name, value);
					} else if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.FileUpload) {
						// 接收到的是文件
						FileUpload fileUpload = (FileUpload) data;
						if (fileUpload.isCompleted()) {
							requestParams.setFileUpload(fileUpload);
							requestParams.setFileContentType(fileUpload.getContentType());
						}
					}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}