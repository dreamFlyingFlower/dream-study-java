package com.wy.registers;
//package com.wy.boot;
//
//import org.springframework.context.annotation.ImportSelector;
//import org.springframework.core.type.AnnotationMetadata;
//
//import com.wy.model.Role;
//
///**
// * @apiNote 当需要使用Import注解导入某个类,让该类注入到spring的上下文中时,有2种方法:
// *          1.直接在Import注解中写入需要导入到spring环境中的类,Import注解所在的类需要被扫描到,
// *          而需要导入的类不需要其他注解,该类中的带有Bean注解的类同样会被注入到spring上下文
// *          2.实现ImportSelector接口,该接口返回需要被注入到spring中的类的全路径名.同样实现了接口的类,
// *          也需要使用Import注解注入到spring中
// * @author ParadiseWY
// * @date 2019年10月9日
// */
//public class MyImportSelector implements ImportSelector {
//
//	/**
//	 * importingClassMetadata:当本类被Import注解使用时,可以通过该参数拿到Import注解所在类的信息
//	 */
//	@Override
//	public String[] selectImports(AnnotationMetadata importingClassMetadata) {
//		return new String[] {"com.wy.entity.User",Role.class.getName()};
//	}
//}