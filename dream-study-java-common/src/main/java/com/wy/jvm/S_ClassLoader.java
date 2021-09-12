package com.wy.jvm;

/**
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
 * @auther 飞花梦影
 * @date 2021-05-10 23:28:02
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class S_ClassLoader {

	public static void main(String[] args) {
		ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
		try {
			systemClassLoader.loadClass("使用指定的类加载器加载某个类");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}