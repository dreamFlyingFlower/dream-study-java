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
 * 生成缩略图操作
 * 
 * @author 飞花梦影
 * @date 2021-09-03 12:01:12
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class CreateThumbPictureServerHandler extends AbstractFileServerHandler implements FileServerProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(CreateThumbPictureServerHandler.class);

	public CreateThumbPictureServerHandler(AccountServer account) {
		super(account);
	}

	public Result process(RequestParam reqParams) {
		Result result = new Result();
		result.setCode(false);
		result.setAction(EnumFileAction.CREATE_THUMB_PICTURE.getValue());

		if (StrTool.isNotBlank(reqParams.getFilePath())) {
			String realPath = getRealPath(reqParams.getFilePath());

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("生成缩略图：" + realPath);
			}
			File file = new File(realPath);

			boolean bool = false;
			if (file.exists()) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("生成缩略图");
				}
				String thumbFilePath = ThumbUtil.getThumbImagePath(realPath);
				File thumbFile = new File(thumbFilePath);
				if (!thumbFile.exists()) {
					new ThumbUtil(new File(realPath), thumbFile, this.account.getThumbWidth(),
							this.account.getThumbHeight()).createThumbImage();
					// ThumbUtil.createThumbImage(new File(realPath), thumbFile,
					// this.account.getThumbWidth(), this.account.getThumbHeight());
					result.setCode(bool);
					result.setMsg("缩略图创建成功");
				} else {
					result.setCode(bool);
					result.setMsg("缩略图已存在，无法创建；缩略图路径=" + thumbFilePath);
				}
			}
		}
		return result;
	}
}