package com.wy.classloader;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.net.URLClassLoader;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

/**
 * 自定义ClassLoader
 * 
 * JDK9之前如果没有太过于复杂的需求,可以直接继承{@link URLClassLoader},可以避免重写findClass()及其获取字节码流的方式
 * JDK9之后不要继承URLClassLoader,因为继承关系发生了改变.{@link AppClassLoader}和{@link ExtClassLoader}不再继承URLClassLoader
 * 
 * {@link ClassLoader#findLoadedClass}:查看已经加载过的class
 * {@link ClassLoader#resolveClass}:链接一个指定的Java类
 * {@link ClassLoader#defineClass}:将字节数组转换为一个Java类
 * 
 * 类加载器(ClassLoader):顶层为BootStrap,该加载器并不属于Java类,而是为了加载Java类存在的原生(native)组件
 * BootStrap主要加载jre/lib/rt.jar,所有追溯到最顶层的ClassLoader都是null,即BootStrap
 * ExtClassLoader:BootStrap的下一层,主要加载jre/lib/ext/*.jar,如果将其他jar包放在该目录下,则其加载类则为ExtClassLoader
 * AppClassLoader:ExtClassLoader的子加载类,默认的系统加载类,主要加载classpath下的jar包
 * 
 * ClassLoader的加载顺序:当前线程的类加载器加载第一个类->如果类加载器中引入了其他类,则其他类也由当前ClassLoader加载
 * ->每个ClassLoader加载类时,又先委托给其上级ClassLoader,这样是为了保证字节码文件的唯一性
 * ->所有上级ClassLoader没有加载到类时则会回到发起者ClassLoader,还加载不了则抛异常,不会再去找发起者的子ClassLoader
 * 
 * 自定义的ClassLoader必须继承{@link ClassLoader},重写loadClass,findClass,defineClass方法
 * 
 * JDK9之后的双亲委派模型发生了改变,不再直接从顶层开始加载,而是先判断类属于哪个模块,由对应的模块类加载器加载.如果没有,再从顶层开始加载
 * 
 * @author 飞花梦影
 * @date 2021-09-15 23:20:01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class MyClassLoader extends ClassLoader {

	public static void main(String[] args) {
		// 系统类加载器
		ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
		// JDK9之后还可以直接获得PlatformClassLoader,该类和ExtClassLoader相同,只是改了名字
		// ClassLoader platformClassLoader = ClassLoader.getPlatformClassLoader();
		try {
			systemClassLoader.loadClass("使用指定的类加载器加载某个类");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 载入并返回一个Class,但是不建议直接重写该方法,可以重写findClass
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