package com.wy.netty.file;

/**
 * 常量
 * 
 * @author 飞花梦影
 * @date 2021-09-03 11:30:42
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public interface Constants {

	String BACKSLASH = "/";

	String USER_NAME_KEY = "userName";

	String PWD_KEY = "pwd";

	String ACTION_KEY = "action";

	String FILE_PATH_KEY = "filePath";

	String FILE_NAME_KEY = "fileName";

	String THUMB_MARK_KEY = "thumbMark";

	String THUMB_MARK_YES = "Y";

	String THUMB_MARK_NO = "N";

	String THUMB_SUFFIX = "_thumb";

	public static char[] LETTER_AND_NUMBER_CHAR =
			{ 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
					'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	int FOLDER_MAX_LEVEL = 10;

	int FOLDER_MIN_LEVEL = 1;

	AccountClient DEFAULT_CLIENT_ACCOUNT =
			new AccountClient("default_account", "test", "D:\\tmp\\up", FOLDER_MIN_LEVEL);

	AccountServer DEFAULT_SERVER_ACCOUNT =
			new AccountServer("default_account", "test", "D:\\tmp\\up", FOLDER_MIN_LEVEL);
}