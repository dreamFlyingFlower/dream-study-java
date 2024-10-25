package dream.study.spring.registerbean;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.ImportSelector;

/**
 * 除了使用{@link Configuration}和{@link Bean}注入组件之外的注入方式
 * 
 * 通过Import注入实体类的3种方法,可从Import源码查看相关类,{@link #ConfigurationClassParser#processImports}
 * 
 * {@link Import}:标识需要注入的类即可注入到Spring容器中,被标识的类不需要添加如{@link Configuration}等额外注解.
 * 		被导入的类以全限定类名为beanBane注入到Spring容器中,往往和EnableXXX一起使用
 * {@link ImportSelector}:实现该接口,重写{@link ImportSelector#selectImports()},返回需要注入类的全路径,可注入多个
 * {@link Import}+{@link ImportBeanDefinitionRegistrar}:类似{@link ImportSelector},但是能处理更多的定义类
 * {@link Import}+{@link DeferredImportSelector}:同上,和SpringBoot中的自动导入配置文件,延迟导入有关
 * {@link FactoryBean}:实现该接口,然后通过{@link Configuration}和{@link Bean}注入,和直接使用2个注解类似
 * {@link ClassPathScanningCandidateComponentProvider}:可以对指定包进行扫描并注入,参照{@link EurekaServerAutoConfiguration#jerseyApplication}
 * {@link ClassPathBeanDefinitionScanner}:ClassPathScanningCandidateComponentProvider子类,可以扫描指定包
 * 
 * @auther 飞花梦影
 * @date 2019-10-09 10:22:36
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
// @Import(User.class)
// @Import(MyImportSelector.class)
@Import(MyImportBeanDefinitionRegister.class)
public class MyImport {

}