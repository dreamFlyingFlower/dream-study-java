package com.wy.annotation;

import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import com.dream.ConstFile;

/**
 * 参照DefaultPropertySourceFactory,可同时解析yml,yaml,properties,xml以及inputstream
 * 
 * 使用: {@link org.springframework.context.annotation.PropertySource#factory()}
 *
 * @author 飞花梦影
 * @date 2022-06-18 08:49:45
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class SelfPropertySourceFactory implements PropertySourceFactory {

	@Override
	public PropertySource<?> createPropertySource(@Nullable String name, EncodedResource resource) throws IOException {
		String resourceName = name != null ? name : getNameForResource(resource.getResource());
		PropertiesPropertySource yamlSource = loadYamlSource(resourceName, resource);
		if (yamlSource != null) {
			return yamlSource;
		}
		Properties properties = PropertiesLoaderUtils.loadProperties(resource);
		// 默认实现的解析properties,xml,inputstream
		return new PropertiesPropertySource(resourceName, properties);
	}

	public static String getNameForResource(Resource resource) {
		String name = resource.getDescription();
		if (!StringUtils.hasText(name)) {
			name = resource.getClass().getSimpleName() + "@" + System.identityHashCode(resource);
		}
		return name;
	}

	public static PropertiesPropertySource loadYamlSource(String resourceName, EncodedResource resource) {
		String fileName = resource.getResource().getFilename();
		if (fileName != null && (fileName.endsWith(ConstFile.FILE_EXTENSION_YML)
				|| fileName.endsWith(ConstFile.FILE_EXTENSION_YAML))) {
			YamlPropertiesFactoryBean factoryBean = new YamlPropertiesFactoryBean();
			factoryBean.setResources(resource.getResource());
			return new PropertiesPropertySource(resourceName, factoryBean.getObject());
		}
		return null;
	}
}