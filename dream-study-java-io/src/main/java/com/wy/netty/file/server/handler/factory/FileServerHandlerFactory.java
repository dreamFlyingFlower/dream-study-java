package com.wy.netty.file.server.handler.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wy.lang.StrTool;
import com.wy.netty.file.AccountServer;
import com.wy.netty.file.Result;
import com.wy.netty.file.enums.EnumFileAction;
import com.wy.netty.file.server.FileServerContainer;
import com.wy.netty.file.server.handler.CreateThumbPictureServerHandler;
import com.wy.netty.file.server.handler.DeleteFileServerHandler;
import com.wy.netty.file.server.handler.ReplaceFileServerHandler;
import com.wy.netty.file.server.handler.UploadFileServerHandler;
import com.wy.netty.file.server.handler.processor.FileServerProcessor;
import com.wy.netty.file.server.parse.RequestParam;
import com.wy.netty.file.utils.JSONUtil;

public class FileServerHandlerFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileServerHandlerFactory.class);

	/**
	 * 根据请求的不同参数，进行相应的文件处理操作
	 * 
	 * @param requestParams
	 * @return
	 */
	public static String process(RequestParam requestParams) {
		AccountServer account = FileServerContainer.getInstance().getAccount(requestParams.getUserName());
		EnumFileAction action = EnumFileAction.converByValue(requestParams.getAction());
		Result result = null;
		if (account.auth(requestParams.getPwd())) {
			FileServerProcessor handler = null;
			if (EnumFileAction.UPLOAD_FILE == action) {// 上传文件
				if (requestParams.getFileUpload() != null) {
					LOGGER.info("进行文件上传操作....");
					handler = new UploadFileServerHandler(account);
				}
			} else if (EnumFileAction.DELETE_FILE == action) {// 删除文件
				if (StrTool.isNotBlank(requestParams.getFilePath())) {
					LOGGER.info("进行文件删除操作....");
					handler = new DeleteFileServerHandler(account);
				}
			} else if (EnumFileAction.REPLACE_FILE == action) {// 替换文件
				if ((requestParams.getFileUpload() != null) && (StrTool.isNotBlank(requestParams.getFilePath()))) {
					LOGGER.info("进行文件替换操作....");
					handler = new ReplaceFileServerHandler(account);
				}
			} else if ((EnumFileAction.CREATE_THUMB_PICTURE == action)
					&& (StrTool.isNotBlank(requestParams.getFilePath()))) {// 生成缩略图
				LOGGER.info("进行生成缩略图操作....");
				handler = new CreateThumbPictureServerHandler(account);
			}
			if (handler != null) {
				result = handler.process(requestParams);
			}
		} else {
			result = new Result();
			result.setAction(EnumFileAction.NULL.getValue());
			result.setCode(false);
			result.setMsg("密码错误");
		}
		if (result == null) {
			result = new Result();
			result.setAction(EnumFileAction.NULL.getValue());
			result.setCode(false);
			result.setMsg("无效动作");
		}

		String json = JSONUtil.toJSONString(result);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("执行结果:" + json);
		}
		return json;
	}
}