package com.wy.assist;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.ProtectionDomain;

import com.wy.annotation.Example;

import javassist.CannotCompileException;
import javassist.ClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.LoaderClassPath;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.ParameterAnnotationsAttribute;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.ArrayMemberValue;
import javassist.bytecode.annotation.StringMemberValue;

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
		if (className.equals("com.wy.model.User")) {
			// 需要的操作,最后返回相应字节码
			ClassPool classPool = ClassPool.getDefault();
			// ClassPool classPool = new ClassPool(false);
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

@Example
class AssistExample {

	/**
	 * 添加注解
	 * 
	 * @param ctClass
	 * @param ctMethod
	 * @param clazz
	 * @throws CannotCompileException
	 * @throws IOException
	 */
	public static void addAnnotation(CtClass ctClass, CtMethod ctMethod, Class<?> clazz)
			throws CannotCompileException, IOException {
		MethodInfo methodInfo = ctMethod.getMethodInfo();
		ConstPool constPool = methodInfo.getConstPool();
		// 要添加的注解
		AnnotationsAttribute methodAttr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
		Annotation methodAnnot = new Annotation("org.springframework.kafka.annotation.KafkaListener", constPool);
		// 添加方法注解
		StringMemberValue[] elements =
				{ new StringMemberValue(String.join("-", ctClass.getName(), ctMethod.getName()), constPool) };
		ArrayMemberValue amv = new ArrayMemberValue(constPool);
		amv.setValue(elements);
		methodAnnot.addMemberValue("topics", amv);
		methodAnnot.addMemberValue("groupId", new StringMemberValue("test-group", constPool));
		methodAttr.addAnnotation(methodAnnot);
		ctMethod.getMethodInfo().addAttribute(methodAttr);
		// 添加参数注解
		ParameterAnnotationsAttribute parameterAtrribute =
				new ParameterAnnotationsAttribute(constPool, ParameterAnnotationsAttribute.visibleTag);
		Annotation paramAnnot = new Annotation("org.springframework.messaging.handler.annotation.Payload", constPool);
		paramAnnot.addMemberValue("value", new StringMemberValue("", constPool));
		Annotation[][] paramArrays = new Annotation[1][1];
		paramArrays[0][0] = paramAnnot;
		parameterAtrribute.setAnnotations(paramArrays);
		ctMethod.getMethodInfo().addAttribute(parameterAtrribute);
		// 写入class文件
		String path = clazz.getDeclaringClass().getResource("").getPath();
		ctClass.writeFile(path);
		ctClass.defrost();
	}

	public static Class<?> example() {
		try {
			// 获取ClassPool
			ClassPool pool = ClassPool.getDefault();
			// 创建User类
			CtClass ctClass = pool.makeClass("com.wy.model.User");
			// 创建User类成员变量name
			CtField name = new CtField(pool.get("java.lang.String"), "username", ctClass);
			// 设置username为私有
			name.setModifiers(Modifier.PRIVATE);
			// 将username写入class
			ctClass.addField(name, CtField.Initializer.constant(""));
			// 增加set方法,名字为setUsername
			ctClass.addMethod(CtNewMethod.setter("setUsername", name));
			// 增加get方法,名字为getUsername
			ctClass.addMethod(CtNewMethod.getter("getUsername", name));
			// 添加无参的构造体
			CtConstructor cons = new CtConstructor(new CtClass[] {}, ctClass);
			// 相当于public Sclass(){this.username = "test";}
			cons.setBody("{username = \"test\";}");
			ctClass.addConstructor(cons);
			// 添加有参的构造体
			cons = new CtConstructor(new CtClass[] { pool.get("java.lang.String") }, ctClass);
			// 第一个传入的形参$1,第二个传入的形参$2,相当于public Sclass(String s){this.username = s;}
			cons.setBody("{$0.username = $1;}");
			ctClass.addConstructor(cons);

			// 反射调用新创建的类
			Class<?> aClass = ctClass.toClass();
			Object user = aClass.newInstance();
			Method getter = null;
			getter = user.getClass().getMethod("getUsername");
			System.out.println(getter.invoke(user));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}