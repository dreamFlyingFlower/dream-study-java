package com.wy.registerbean;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import com.wy.model.Role;
import com.wy.model.User;

/**
 * 通过{@link Import}注入类到Spring上下文中,和MyImportSelector类似,只不过是直接使用BeanDefinitionRegistry注入类
 * 
 * 可以指定扫描包,然后通过{@link BeanDefinitionRegistry}批量注入
 * 
 * @auther 飞花梦影
 * @date 2019-10-09 10:23:20
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class MyImportBeanDefinitionRegister implements ImportBeanDefinitionRegistrar {

	/**
	 * @param importingClassMetadata 当本类被Import注解使用时,可以通过该参数拿到Import注解所在类的信息
	 */
	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		BeanDefinitionBuilder rootBeanDefinition = BeanDefinitionBuilder.rootBeanDefinition(User.class);
		registry.registerBeanDefinition("user", rootBeanDefinition.getBeanDefinition());

		BeanDefinitionBuilder rootBeanDefinition1 = BeanDefinitionBuilder.rootBeanDefinition(Role.class);
		registry.registerBeanDefinition("role", rootBeanDefinition1.getBeanDefinition());
	}
}