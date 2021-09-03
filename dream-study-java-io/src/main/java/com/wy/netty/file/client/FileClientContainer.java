package com.wy.netty.file.client;

import java.util.ResourceBundle;

public class FileClientContainer {

	private static ResourceBundle rb = null;

	static {
		rb = ResourceBundle.getBundle("file-config");
	}

	private static String userName = rb.getString("upload.userName");

	private static String password = rb.getString("upload.password");

	private static String host = rb.getString("upload.server.host");

	private static int port = Integer.parseInt(rb.getString("upload.server.port"));

	public static String getUserName() {
		return userName;
	}

	public static String getPassword() {
		return password;
	}

	public static String getHost() {
		return host;
	}

	public static int getPort() {
		return port;
	}
}