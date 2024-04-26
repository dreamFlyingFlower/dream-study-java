package com.wy.netty.file.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dream.lang.StrHelper;
import com.wy.netty.file.AccountServer;
import com.wy.netty.file.Constants;

/**
 * 文件服务器基本信息容器
 * 
 * @author 飞花梦影
 * @date 2021-09-03 11:55:47
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class FileServerContainer {

	private static Log log = LogFactory.getLog(FileServerContainer.class);

	/**
	 * 文件服务器上传鉴权账户列表
	 */
	private List<AccountServer> accounts = new ArrayList<AccountServer>();

	/**
	 * 文件上传的根目录
	 */
	private String fileBaseDirectory = "D:\\test\\tmp";

	private int port = 9999;

	private static FileServerContainer instance;

	private static Map<String, AccountServer> AccountMap = new HashMap<String, AccountServer>();

	public static FileServerContainer factoryMethod() {
		log.info("初始化静态资源传输平台");
		instance = new FileServerContainer();
		instance.accounts.add(AccountServer.builder().userName("test1").pwd("123456").rootPath("D:\\test\\tmp").level(1)
				.thumbHeight(100).thumbWidth(100).build());
		instance.accounts.add(AccountServer.builder().userName("test2").pwd("123456").rootPath("D:\\test\\tmp").level(1)
				.thumbHeight(100).thumbWidth(100).build());
		return instance;
	}

	public static FileServerContainer getInstance() {
		return instance;
	}

	public void setAccounts(List<AccountServer> accounts) {
		this.accounts = accounts;
		for (AccountServer item : this.accounts) {
			if ((StrHelper.isBlank(item.getUserName())) || (StrHelper.isBlank(item.getPwd()))
					|| (StrHelper.isBlank(item.getRootPath()))) {
				log.error("账户配置出现错误，请检查，" + item);
				new Exception().printStackTrace();
			}
			if (!AccountMap.containsKey(item.getUserName())) {
				AccountMap.put(item.getUserName(), item);
			} else
				log.error("账户出现重复配置：" + item);
		}

		log.info("加入默认账户：" + Constants.DEFAULT_SERVER_ACCOUNT);
		AccountMap.put(Constants.DEFAULT_SERVER_ACCOUNT.getUserName(), Constants.DEFAULT_SERVER_ACCOUNT);
	}

	public AccountServer getAccount(String userName) {
		AccountServer account = (AccountServer) AccountMap.get(userName);
		if (account == null) {
			log.error("不存在账户UserName=" + userName + ",返回默认账户");
			account = Constants.DEFAULT_SERVER_ACCOUNT;
		}
		return account;
	}

	public String getFileBaseDirectory() {
		return fileBaseDirectory;
	}

	public void setFileBaseDirectory(String fileBaseDirectory) {
		this.fileBaseDirectory = fileBaseDirectory;
	}

	public int getPort() {
		return this.port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}