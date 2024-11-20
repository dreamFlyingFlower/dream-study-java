package com.wy.enums;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 加载{@link CommonEnum}
 * 
 * <pre>
 * 通过 Spring 的 ResourcePatternResolver 根据配置的 basePackage 对classpath进行扫描.
 * 扫描结果以Resource来表示,通过 MetadataReader 读取 Resource 信息,并将其解析为 ClassMetadata
 * 获得 ClassMetadata 之后,找出实现 CommonEnum 的类
 * 将 CommonEnum 实现类注册到两个 Map 中进行缓存
 * </pre>
 *
 * @author 飞花梦影
 * @date 2024-05-22 17:32:45
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Component
@Slf4j
public class CommonEnumRegistry {

	private static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

	private static final String BASE__ENUM_CLASS_NAME = CommonEnum.class.getName();

	@Getter
	private final Map<String, List<CommonEnum>> nameDict = Maps.newLinkedHashMap();

	@Getter
	private final Map<Class<?>, List<CommonEnum>> classDict = Maps.newLinkedHashMap();

	@Value("${baseEnum.basePackage:''}")
	private String basePackage;

	@Autowired
	private ResourceLoader resourceLoader;

	@PostConstruct
	public void initDict() {
		if (StringUtils.isEmpty(basePackage)) {
			return;
		}
		ResourcePatternResolver resourcePatternResolver =
				ResourcePatternUtils.getResourcePatternResolver(this.resourceLoader);
		MetadataReaderFactory metadataReaderFactory = new SimpleMetadataReaderFactory();
		try {
			String pkg = toPackage(this.basePackage);
			// 对 basePackage 包进行扫描
			String packageSearchPath =
					ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + pkg + DEFAULT_RESOURCE_PATTERN;
			Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
			for (Resource resource : resources) {
				if (resource.isReadable()) {
					try {
						MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
						ClassMetadata classMetadata = metadataReader.getClassMetadata();

						String[] interfaceNames = classMetadata.getInterfaceNames();
						// 实现 BASE_ENUM_CLASS_NAME 接口
						if (Arrays.asList(interfaceNames).contains(BASE__ENUM_CLASS_NAME)) {
							String className = classMetadata.getClassName();

							// 加载 cls
							Class<?> cls = Class.forName(className);
							if (cls.isEnum() && CommonEnum.class.isAssignableFrom(cls)) {

								Object[] enumConstants = cls.getEnumConstants();
								List<CommonEnum> commonEnums =
										Arrays.asList(enumConstants).stream().filter(e -> e instanceof CommonEnum)
												.map(e -> (CommonEnum) e).collect(Collectors.toList());

								String key = convertKeyFromClassName(cls.getSimpleName());

								this.nameDict.put(key, commonEnums);
								this.classDict.put(cls, commonEnums);
							}
						}
					} catch (Throwable ex) {
						// ignore
					}
				}
			}
		} catch (IOException e) {
			log.error("failed to load dict by auto register", e);
		}
	}

	private String toPackage(String basePackage) {
		String result = basePackage.replace(".", "/");
		return result + "/";
	}

	private String convertKeyFromClassName(String className) {
		return Character.toLowerCase(className.charAt(0)) + className.substring(1);
	}
}