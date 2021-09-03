package com.wy.netty.file.server.handler;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wy.lang.StrTool;
import com.wy.netty.file.AccountServer;
import com.wy.netty.file.Result;
import com.wy.netty.file.enums.EnumFileAction;
import com.wy.netty.file.server.handler.processor.AbstractFileServerHandler;
import com.wy.netty.file.server.handler.processor.FileServerProcessor;
import com.wy.netty.file.server.parse.RequestParam;
import com.wy.netty.file.utils.ThumbUtil;

/**
 * 替换文件操作
 * 
 * @author 飞花梦影
 * @date 2021-09-03 12:11:26
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class ReplaceFileServerHandler extends AbstractFileServerHandler implements FileServerProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReplaceFileServerHandler.class);

	public ReplaceFileServerHandler(AccountServer account) {
		super(account);
	}

	public Result process(RequestParam reqParams) {
		Result result = new Result();
		result.setCode(false);
		result.setAction(EnumFileAction.REPLACE_FILE.getValue());
		if ((StrTool.isNotBlank(reqParams.getFilePath())) && (reqParams.getFileUpload() != null)) {
			String realPath = getRealPath(reqParams.getFilePath());
			LOGGER.info("进行替换文件：" + realPath);
			File oldFile = new File(realPath);
			if ((oldFile.exists()) && (oldFile.isFile())) {
				oldFile.delete();
			} else {
				result.setMsg("替换的文件不存在");
				LOGGER.info("替换的文件不存在：" + realPath);
				return result;
			}
			String thumbFilePath = ThumbUtil.getThumbImagePath(realPath);
			File thumbFile = new File(thumbFilePath);
			boolean thumbBool = false;
			if ((thumbFile.exists()) && (thumbFile.isFile())) {
				thumbFile.delete();
				thumbBool = true;
			}
			try {
				boolean bool = reqParams.getFileUpload().renameTo(oldFile);
				result.setCode(bool);
				result.setMsg("文件替换上传成功");
				LOGGER.info("文件替换上传成功");
				result.setFilePath(reqParams.getFilePath());
				if ((bool) && (thumbBool)) {
					LOGGER.info("生成缩略图");
					new ThumbUtil(oldFile, thumbFile, this.account.getThumbWidth(), this.account.getThumbHeight())
							.createThumbImage();
					// ThumbUtil.createThumbImage(oldFile, thumbFile, this.account.getThumbWidth(),
					// this.account.getThumbHeight());
				}
			} catch (Exception e) {
				e.printStackTrace();
				result.setCode(false);
				result.setMsg("文件替换报错" + e + ",acount:" + this.account);
				LOGGER.error("文件替换报错" + e + ",acount:" + this.account);
			}
		}
		return result;
	}
}