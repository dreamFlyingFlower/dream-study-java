package com.register;

import javax.security.auth.login.Configuration;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.ImportSelector;

import com.register.bean.TestBean;

/**
 * 除了使用{@link Configuration}和{@link Bean}注入组件之外的注入方式
 * 
 * {@link Import}:标识需要注入的类即可注入到Spring容器中,往往和EnableXXX一起使用
 * {@link ImportSelector}:实现该接口,重写{@link ImportSelector#selectImports()},返回需要注入类的全路径,可注入多个
 * {@link Import}+{@link ImportBeanDefinitionRegistrar}:类似{@link ImportSelector},但是能处理更多的定义类
 * {@link Import}+{@link DeferredImportSelector}:同上,和SpringBoot中的自动导入配置文件,延迟导入有关
 * {@link FactoryBean}:实现该接口,然后通过{@link Configuration}和{@link Bean}注入,和直接使用2个注解类似
 *
 * @author 飞花梦影
 * @date 2022-01-14 08:48:15
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Import(TestBean.class)
public class BeanIimportRegister {

}