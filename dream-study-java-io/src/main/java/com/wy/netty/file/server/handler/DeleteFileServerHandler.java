package com.wy.netty.file.server.handler;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wy.lang.StrTool;
import com.wy.netty.file.AccountServer;
import com.wy.netty.file.Constants;
import com.wy.netty.file.Result;
import com.wy.netty.file.enums.EnumFileAction;
import com.wy.netty.file.server.handler.processor.AbstractFileServerHandler;
import com.wy.netty.file.server.handler.processor.FileServerProcessor;
import com.wy.netty.file.server.parse.RequestParam;

/**
 * 删除文件操作
 * 
 * @author 飞花梦影
 * @date 2021-09-03 12:09:45
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class DeleteFileServerHandler extends AbstractFileServerHandler implements FileServerProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeleteFileServerHandler.class);

	public DeleteFileServerHandler(AccountServer account) {
		super(account);
	}

	public Result process(RequestParam reqParams) {
		Result result = new Result();
		result.setCode(false);
		result.setAction(EnumFileAction.DELETE_FILE.getValue());
		if (StrTool.isNotBlank(reqParams.getFilePath())) {
			String realPath = getRealPath(reqParams.getFilePath());
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("进行删除文件：" + realPath);
			}
			File file = new File(realPath);

			boolean bool = false;
			if ((file.exists()) && (file.isFile())) {
				bool = file.delete();
			}
			int position = realPath.lastIndexOf(".");
			String suffix = realPath.substring(position);
			String thumbPath = realPath.substring(0, position) + Constants.THUMB_SUFFIX + suffix;
			File thumbFile = new File(thumbPath);
			if ((thumbFile.exists()) && (thumbFile.isFile()) && (bool)) {
				thumbFile.delete();
			}
			result.setCode(bool);
			result.setMsg("文件删除成功");
		}
		if (!result.isCode()) {
			result.setMsg("文件删除失败");
		}
		return result;
	}
}