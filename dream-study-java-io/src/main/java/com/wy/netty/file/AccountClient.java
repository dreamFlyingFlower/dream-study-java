package com.wy.netty.file;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountClient {

	private String userName;

	private String pwd;

	private String rootPath;

	private int level;

	@Builder.Default
	private int thumbHeight = 20;

	@Builder.Default
	private int thumbWidth = 20;

	public AccountClient(String userName, String pwd, String rootPath, int level) {
		this.userName = userName;
		this.pwd = pwd;
		this.rootPath = rootPath;
		if (!this.rootPath.endsWith("/")) {
			this.rootPath += "/";
		}
		this.level = level;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
		if (!this.rootPath.endsWith("/"))
			this.rootPath += "/";
	}

	public boolean auth(String pwd) {
		return Objects.equals(this.pwd, pwd);
	}
}