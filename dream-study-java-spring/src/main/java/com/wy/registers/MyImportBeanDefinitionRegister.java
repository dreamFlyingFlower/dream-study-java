package com.wy.registers;
//package com.wy.boot;
//
//import org.springframework.beans.factory.support.BeanDefinitionBuilder;
//import org.springframework.beans.factory.support.BeanDefinitionRegistry;
//import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
//import org.springframework.core.type.AnnotationMetadata;
//
//import com.wy.model.Role;
//import com.wy.model.User;
//
///**
// * 通过Import方式注入实体类到Spring的上下文中,和MyImportSelector类似
// * 
// * @author ParadiseWY
// * @date 2019年10月9日
// */
//public class MyImportBeanDefinitionRegister implements ImportBeanDefinitionRegistrar {
//
//	@Override
//	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
//		BeanDefinitionBuilder rootBeanDefinition = BeanDefinitionBuilder.rootBeanDefinition(User.class);
//		registry.registerBeanDefinition("user", rootBeanDefinition.getBeanDefinition());
//
//		BeanDefinitionBuilder rootBeanDefinition1 = BeanDefinitionBuilder.rootBeanDefinition(Role.class);
//		registry.registerBeanDefinition("user", rootBeanDefinition1.getBeanDefinition());
//	}
//}