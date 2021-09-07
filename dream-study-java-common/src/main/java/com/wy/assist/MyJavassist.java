package com.wy.assist;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Modifier;
import java.security.ProtectionDomain;

import javassist.ClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.LoaderClassPath;

/**
 * Javassist代理,作用和ASM相同,在不改变源代码的情况下进行其他相关操作,如收集日志.
 * 
 * 需要在pom.xml的配置文件中配置插件代码,不会将该javassist打入到最终的jar包中,如何使用百度即可
 *
 * Javassist相关类:
 * 
 * <pre>
 * {@link ClassPath}:加载Java中字节码文件
 * ->{@link LoaderClassPath}:ClassPath实现类,还有其他几种,如JarClassPath等
 * {@link ClassPool}:装载需要修改的类的字节码文件,从 ClassPath 获取
 * {@link CtClass}:从ClassPool加载得到的相关类字节码,对类进行相关操作
 * {@link CtMethod}:修改方法
 * {@link CtField}:修改字段
 * {@link CtConstructor}:修改构造函数
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2021-09-06 11:09:32
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class MyJavassist implements ClassFileTransformer {

	public static void main(String[] args) {

	}

	/**
	 * 在应用启动之前调用
	 * 
	 * @param args
	 * @param instrumentation
	 */
	public static void premain(String[] args, Instrumentation instrumentation) {
		// 添加了一个监听器
		instrumentation.addTransformer(new MyJavassist());
	}

	/**
	 * JVM中每个类加载之前执行该方法,返回修改后的字节码
	 * 
	 * 有自动容错,即使抛异常或null,仍然会返回源字节码文件
	 */
	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		// 排除系统加载类,原生类加载器
		if (className == null || loader == null
				|| loader.getClass().getName().equals("sun.reflect.DelegatingClassLoader")
				|| loader.getClass().getName().equals("org.apache.catalina.loader.StandardClassLoader")) {
			return null;
		}
		// 排除动态代理类
		if (className.indexOf("$Proxy") != -1) {
			return null;
		}
		if (className.equals("com.wy.test.Test")) {
			// 需要的操作,最后返回相应字节码
			ClassPool classPool = new ClassPool(false);
			classPool.insertClassPath(new LoaderClassPath(loader));
			try {
				CtClass ctClass = classPool.get(className.replaceAll("/", "."));
				CtMethod[] ctMethods = ctClass.getDeclaredMethods();
				for (CtMethod ctMethod : ctMethods) {
					if (!Modifier.isPublic(ctMethod.getModifiers()) || Modifier.isNative(ctMethod.getModifiers())
							|| Modifier.isStatic(ctMethod.getModifiers())) {
						continue;
					}
					CtMethod m = ctMethod;
					String methodName = m.getName();
					String oldMethodName = methodName + "$agent";
					// 重构被代理的方法名称
					m.setName(oldMethodName);
					// 基于原方法复制生成代理方法
					CtMethod agentMethod = CtNewMethod.copy(m, methodName, ctClass, null);
					agentMethod.setBody("需要加入到代理方法中的代码,类似于动态代理生成一串代码字符串");
					ctClass.addMethod(agentMethod);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return new byte[0];
		}
		return null;
	}
}