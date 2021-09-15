package com.wy.jvm;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

/**
 * 自定义ClassLoader
 * 
 * @author 飞花梦影
 * @date 2021-09-15 23:20:01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class MyClassLoader extends ClassLoader {

	/**
	 * 载入并返回一个Class
	 * 
	 * @param name 类的全限定名
	 */
	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		Class<?> findClass = findClass(name);
		if (findClass == null) {
			System.out.println("无法加载类:" + name + "需要请求父加载器");
			return super.loadClass(name);
		}
		return findClass;
	}

	/**
	 * loadClass回调该方法,自定义ClassLoader的推荐做法
	 * 
	 * @param name 类的全限定名
	 */
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		Class<?> clazz = this.findLoadedClass(name);
		if (null == clazz) {
			try {
				String filePath = "";
				FileInputStream fileInputStream = new FileInputStream(filePath);
				FileChannel fileChannel = fileInputStream.getChannel();
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				WritableByteChannel writableByteChannel = Channels.newChannel(byteArrayOutputStream);
				ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
				// 将文件信息加载到byteBuffer中
				while (fileChannel.read(byteBuffer) > 0) {
					writableByteChannel.write(byteBuffer);
					byteBuffer.clear();
				}
				fileInputStream.close();
				byte[] byteArray = byteArrayOutputStream.toByteArray();
				clazz = defineClass(name, byteArray, 0, byteArray.length);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return clazz;
	}
}