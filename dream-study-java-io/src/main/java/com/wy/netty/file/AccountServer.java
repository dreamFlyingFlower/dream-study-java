package com.wy.netty.file;

import java.io.File;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 文件服务器鉴权账户类
 * 
 * @author 飞花梦影
 * @date 2021-09-03 11:13:27
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountServer {

	private String userName;

	private String pwd;

	private String rootPath;

	private int level;

	@Builder.Default
	private int thumbHeight = 100;

	@Builder.Default
	private int thumbWidth = 100;

	public AccountServer(String userName, String pwd, String rootPath, int level) {
		this.userName = userName;
		this.pwd = pwd;
		this.rootPath = rootPath;
		if (!this.rootPath.endsWith(File.separator)) {
			this.rootPath += File.separator;
		}
		this.level = level;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
		if (!this.rootPath.endsWith(File.separator))
			this.rootPath += File.separator;
	}

	public boolean auth(String pwd) {
		return Objects.equals(this.pwd, pwd);
	}
}