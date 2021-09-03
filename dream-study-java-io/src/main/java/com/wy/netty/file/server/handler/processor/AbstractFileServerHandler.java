package com.wy.netty.file.server.handler.processor;

import com.wy.netty.file.AccountServer;

public abstract class AbstractFileServerHandler {

	protected AccountServer account;

	public AbstractFileServerHandler(AccountServer account) {
		this.account = account;
	}

	public AbstractFileServerHandler() {
		super();
	}

	/**
	 * 服务器真实路径
	 * 
	 * @param savePath 数据库保存的路径
	 * @return
	 * @author liuyuanxian
	 */
	protected String getRealPath(String savePath) {
		return this.account.getRootPath() + savePath;
	}
}