package dream.study.spring.registerbean;

import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import dream.study.common.model.Role;
import dream.study.spring.autoconfigure.S_AutoConfig;

/**
 * 通过返回类的全限定类名数组批量注入,会在#ConfigurationClassParser.processImport()中注册bean对象
 * 
 * 使用{@link Import}注解引入某个类,让该类注入到spring的上下文中,有2种方法:
 * 
 * <pre>
 * 1.直接在Import注解中写入需要导入到spring环境中的类,Import修饰的的类需要被扫描到,
 * 而需要引入的类不需要其他注解,也不需要被扫描到,该类和类中带有{@link Bean}的方法返回类同样会被注入到spring上下文
 * 2.实现{@link ImportSelector}接口,该接口返回需要被注入到spring中的类的全路径名.实现了接口的类,
 * 也需要使用Import注解注入到spring中,也可以试试使用{@link Configuration}或其他注解
 * </pre>
 * 
 * @auther 飞花梦影
 * @date 2019-10-09 10:13:28
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class MyImportSelector implements ImportSelector {

	/**
	 * @param importingClassMetadata 当本类被Import注解使用时,可以通过该参数拿到Import注解所在类的信息
	 */
	@Override
	public String[] selectImports(AnnotationMetadata importingClassMetadata) {
		// 获得注解的属性
		Map<String, Object> annotationAttributes =
				importingClassMetadata.getAnnotationAttributes(S_AutoConfig.class.getName());
		// 获得的属性值需要自行强转
		annotationAttributes.get("value");
		return new String[] { "com.wy.model.User", Role.class.getName() };
	}
}