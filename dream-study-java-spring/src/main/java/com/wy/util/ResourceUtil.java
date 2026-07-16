package com.wy.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.core.io.ClassPathResource;

/**
 * 读取Spring Jar中的资源文件
 *
 * @author 飞花梦影
 * @date 2022-08-10 09:52:17
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class ResourceUtil {

	/**
	 * 从resources根路径下获取文件,相对路径,不能以/开头
	 *
	 * @param fileName
	 * @throws IOException
	 */
	public static void function4(String fileName) throws IOException {
		InputStream in = ResourceUtil.class.getClassLoader().getResourceAsStream(fileName);
		getFileContent(in);
	}

	/**
	 * 从resources根路径下获取文件,必须是绝对路径
	 *
	 * @param fileName
	 * @throws IOException
	 */
	public static void function5(String fileName) throws IOException {
		InputStream in = ResourceUtil.class.getResourceAsStream(fileName);
		getFileContent(in);
	}

	/**
	 * 建议SpringBoot中使用,相对路径或绝对路径都可
	 *
	 * @param fileName
	 * @throws IOException
	 */
	public static void function6(String fileName) throws IOException {
		ClassPathResource classPathResource = new ClassPathResource(fileName);
		InputStream inputStream = classPathResource.getInputStream();
		getFileContent(inputStream);
	}

	/**
	 * 根据文件路径读取文件内容
	 *
	 * @param fileInPath
	 * @throws IOException
	 */
	public static void getFileContent(Object fileInPath) throws IOException {
		BufferedReader br = null;
		if (fileInPath == null) {
			return;
		}
		if (fileInPath instanceof String) {
			br = new BufferedReader(new FileReader(new File((String) fileInPath)));
		} else if (fileInPath instanceof InputStream) {
			br = new BufferedReader(new InputStreamReader((InputStream) fileInPath));
		}
		String line;
		while ((line = br.readLine()) != null) {
			System.out.println(line);
		}
		br.close();
	}
}