package dream.study.spring.apicontrol.header;

import java.lang.reflect.Method;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import dream.study.spring.apicontrol.ApiVersion;

/**
 * 基于Header实现的版本控制:用于注入spring用来管理
 *
 * @author 飞花梦影
 * @date 2023-10-12 17:10:33
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class HeaderVersionHandlerMapping extends RequestMappingHandlerMapping {

	@Override
	protected boolean isHandler(Class<?> beanType) {
		return AnnotatedElementUtils.hasAnnotation(beanType, Controller.class);
	}

	@Override
	protected RequestCondition<?> getCustomTypeCondition(Class<?> handlerType) {
		ApiVersion apiVersion = AnnotationUtils.findAnnotation(handlerType, ApiVersion.class);
		return createCondition(apiVersion);
	}

	@Override
	protected RequestCondition<?> getCustomMethodCondition(Method method) {
		ApiVersion apiVersion = AnnotationUtils.findAnnotation(method, ApiVersion.class);
		return createCondition(apiVersion);
	}

	private RequestCondition<ApiVersionCondition> createCondition(ApiVersion apiVersion) {
		return apiVersion == null ? null : new ApiVersionCondition(apiVersion);
	}
}