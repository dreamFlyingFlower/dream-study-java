package com.wy.classloader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import dream.flying.flower.reflect.ReflectHelper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 动态加载JAR
 *
 * @author 飞花梦影
 * @date 2024-05-08 15:25:51
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Component
@Slf4j
@AllArgsConstructor
public class MyDynamicLoader {

	private final ApplicationContext applicationContext;

	private Map<String, MyDynamicClassLoader> myClassLoaderCenter = new ConcurrentHashMap<>();

	@Value("${dynamicLoad.path}")
	private String path;

	/**
	 * 动态加载指定路径下指定jar包
	 * 
	 * @param path
	 * @param fileName
	 * @param isRegistXxlJob 是否需要注册xxljob执行器,项目首次启动不需要注册执行器
	 * @return map<jobHander, Cron> 创建xxljob任务时需要的参数配置
	 */
	public void loadJar(String path, String fileName, Boolean isRegistXxlJob)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		File file = new File(path + "/" + fileName);
		// 获取beanFactory
		DefaultListableBeanFactory beanFactory =
				(DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
		// 获取当前项目的执行器
		try {
			// URLClassloader加载jar包规范必须这么写
			URL url = new URL("jar:file:" + file.getAbsolutePath() + "!/");
			URLConnection urlConnection = url.openConnection();
			JarURLConnection jarURLConnection = (JarURLConnection) urlConnection;
			// 获取jar文件
			JarFile jarFile = jarURLConnection.getJarFile();
			Enumeration<JarEntry> entries = jarFile.entries();

			// 创建自定义类加载器,并加到map中方便管理
			MyDynamicClassLoader myClassloader =
					new MyDynamicClassLoader(new URL[] { url }, ClassLoader.getSystemClassLoader());
			myClassLoaderCenter.put(fileName, myClassloader);
			Set<Class<?>> initBeanClass = new HashSet<>(jarFile.size());
			// 遍历文件
			while (entries.hasMoreElements()) {
				JarEntry jarEntry = entries.nextElement();
				if (jarEntry.getName().endsWith(".class")) {
					// 1. 加载类到jvm中
					// 获取类的全路径名
					String className =
							jarEntry.getName().replace('/', '.').substring(0, jarEntry.getName().length() - 6);
					// 1.1进行反射获取
					myClassloader.loadClass(className);
				}
			}
			Map<String, Class<?>> loadedClasses = myClassloader.getLoadedClasses();
			for (Map.Entry<String, Class<?>> entry : loadedClasses.entrySet()) {
				String className = entry.getKey();
				Class<?> clazz = entry.getValue();
				// 2. 将有@spring注解的类交给spring管理
				// 2.1 判断是否注入spring
				Boolean flag = hasSpringAnnotation(clazz);
				if (flag) {
					// 2.2交给spring管理
					BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
					AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
					// 此处beanName使用全路径名是为了防止beanName重复
					String packageName = className.substring(0, className.lastIndexOf(".") + 1);
					String beanName = className.substring(className.lastIndexOf(".") + 1);
					beanName = packageName + beanName.substring(0, 1).toLowerCase() + beanName.substring(1);
					// 2.3注册到spring的beanFactory中
					beanFactory.registerBeanDefinition(beanName, beanDefinition);
					// 2.4允许注入和反向注入
					beanFactory.autowireBean(clazz);
					beanFactory.initializeBean(clazz, beanName);
					/*
					 * if(Arrays.stream(clazz.getInterfaces()).collect(Collectors.toSet()).contains(InitializingBean.
					 * class)){ initBeanClass.add(clazz); }
					 */
					initBeanClass.add(clazz);
				}
			}
			// spring bean实际注册
			initBeanClass.forEach(beanFactory::getBean);
		} catch (IOException e) {
			log.error("读取{} 文件异常", fileName);
			e.printStackTrace();
			throw new RuntimeException("读取jar文件异常: " + fileName);
		}
	}

	/**
	 * 判断一个类是否有 Spring 核心注解
	 *
	 * @param clazz 要检查的类
	 * @return true 如果该类上添加了相应的 Spring 注解,否则返回 false
	 */
	public static boolean hasSpringAnnotation(Class<?> clazz) {
		if (clazz == null) {
			return false;
		}
		// 是否是接口
		if (clazz.isInterface()) {
			return false;
		}
		// 是否是抽象类
		if (Modifier.isAbstract(clazz.getModifiers())) {
			return false;
		}

		try {
			if (clazz.getAnnotation(Component.class) != null || clazz.getAnnotation(Repository.class) != null
					|| clazz.getAnnotation(Service.class) != null || clazz.getAnnotation(Controller.class) != null
					|| clazz.getAnnotation(Configuration.class) != null) {
				return true;
			}
		} catch (Exception e) {
			log.error("出现异常：{}", e.getMessage());
		}
		return false;
	}

	/**
	 * 动态卸载指定路径下指定jar包
	 * 
	 * @param fileName
	 */
	@SuppressWarnings("unchecked")
	public void unloadJar(String fileName) throws IllegalAccessException, NoSuchFieldException {
		// 获取加载当前jar的类加载器
		MyDynamicClassLoader myClassLoader = myClassLoaderCenter.get(fileName);

		// 获取beanFactory,准备从spring中卸载
		DefaultListableBeanFactory beanFactory =
				(DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
		Map<String, Class<?>> loadedClasses = myClassLoader.getLoadedClasses();

		Set<String> beanNames = new HashSet<>();
		for (Map.Entry<String, Class<?>> entry : loadedClasses.entrySet()) {
			// 截取beanName
			String key = entry.getKey();
			String packageName = key.substring(0, key.lastIndexOf(".") + 1);
			String beanName = key.substring(key.lastIndexOf(".") + 1);
			beanName = packageName + beanName.substring(0, 1).toLowerCase() + beanName.substring(1);

			// 获取bean,如果获取失败,表名这个类没有加到spring容器中,则跳出本次循环
			Object bean = null;
			try {
				bean = applicationContext.getBean(beanName);
			} catch (Exception e) {
				// 异常说明spring中没有这个bean
				continue;
			}

			// 从spring中移除,这里的移除是仅仅移除的bean,并未移除bean定义
			beanNames.add(beanName);
			beanFactory.destroyBean(beanName, bean);
		}
		// 移除bean定义
		Field mergedBeanDefinitions =
				beanFactory.getClass().getSuperclass().getSuperclass().getDeclaredField("mergedBeanDefinitions");
		ReflectHelper.fixAccessible(mergedBeanDefinitions);

		Map<String, RootBeanDefinition> rootBeanDefinitionMap =
				((Map<String, RootBeanDefinition>) mergedBeanDefinitions.get(beanFactory));
		for (String beanName : beanNames) {
			beanFactory.removeBeanDefinition(beanName);
			// 父类bean定义去除
			rootBeanDefinitionMap.remove(beanName);
		}

		// 从类加载中移除
		try {
			// 从类加载器底层的classes中移除连接
			Field field = ClassLoader.class.getDeclaredField("classes");
			ReflectHelper.fixAccessible(field);
			Vector<Class<?>> classes = (Vector<Class<?>>) field.get(myClassLoader);
			classes.removeAllElements();
			// 移除类加载器的引用
			myClassLoaderCenter.remove(fileName);
			// 卸载类加载器
			myClassLoader.unload();
		} catch (NoSuchFieldException e) {
			log.error("动态卸载的类,从类加载器中卸载失败");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			log.error("动态卸载的类,从类加载器中卸载失败");
			e.printStackTrace();
		}
		log.error("{} 动态卸载成功", fileName);
	}
}