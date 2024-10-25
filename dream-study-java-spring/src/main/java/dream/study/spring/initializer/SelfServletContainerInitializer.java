package dream.study.spring.initializer;

import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;

import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * Servlet容器启动时调用
 * 
 * {@link HandlesTypes}:启动时加载的接口的子类子接口,注意是子类或子接口,被加载的子类会加载到onStartup()里的第一个参数上
 *
 * @author 飞花梦影
 * @date 2023-03-31 11:23:14
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
@HandlesTypes({ BeanPostProcessor.class })
public class SelfServletContainerInitializer implements ServletContainerInitializer {

	/**
	 * 启动调用
	 * 
	 * @param c 由@HandlesTypes注解的接口注入,没有则为空
	 * @param ctx
	 * @throws ServletException
	 */
	@Override
	public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
		for (Class<?> clazz : c) {
			System.out.println(clazz);
		}
	}
}