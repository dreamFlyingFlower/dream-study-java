package dream.study.spring.privacy;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import dream.flying.flower.reflect.ReflectHelper;

/**
 * 脱敏切面,拦截 {@link PrivacyKey}注解
 *
 * @author 飞花梦影
 * @date 2023-12-07 15:00:56
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Component
@Aspect
public class PrivacyAspect {

	@Around(value = "@annotation(com.wy.privacy.PrivacyKey)")
	public Object repeatSub(ProceedingJoinPoint joinPoint) throws Throwable {
		return joinPoint.proceed();
	}

	@AfterReturning(value = "@annotation(com.wy.privacy.PrivacyKey)", returning = "result")
	public void setPrivacyKeyType(JoinPoint joinPoint, Object result) throws Throwable {
		// 进行注解 值获取
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		// 是否开启脱敏
		boolean flag = method.getDeclaredAnnotation(PrivacyKey.class).isKey();
		// 是否对分页进行脱敏
		boolean status = method.getDeclaredAnnotation(PrivacyKey.class).isPageKey();
		if (!status) {
			// 进行返回值反射
			Field field = ReflectionUtils.findField(result.getClass(), "isPrivacyKey");
			if (null != field) {
				setFieldMethod(result, flag, field);
			}
		} else {
			// 反射分页page
			// 反射list类型
			Parameter[] parameters = signature.getMethod().getParameters();
			// 泛型名称
			String name = parameters[0].getName();
			System.out.println(name);
			// 泛型class
			Class<?> type = parameters[0].getType();
			// 包名
			String typeName = type.getName();
			PropertyDescriptor[] ps =
					Introspector.getBeanInfo(result.getClass(), Object.class).getPropertyDescriptors();
			for (PropertyDescriptor prop : ps) {
				if (prop.getPropertyType().isAssignableFrom(List.class)) {
					Object obj = result.getClass().getMethod(prop.getReadMethod().getName()).invoke(result);
					if (obj != null) {
						List<?> listObj = (List<?>) obj;
						for (Object next : listObj) {
							Class<?> classObj = Class.forName(typeName);
							// 获取成员变量
							Field field = ReflectionUtils.findField(classObj, "isPrivacyKey");
							setFieldMethod(next, flag, field);
						}
					}
				}
			}
		}
	}

	/**
	 * 内容填充
	 */
	private void setFieldMethod(Object result, boolean flag, Field field) throws IllegalAccessException {
		// 设置属性的可访问性
		ReflectHelper.fixAccessible(field);
		// 只获取isPrivacyKey
		String name = field.getName();
		// 过滤非布尔类型
		Class<?> type = field.getType();
		// 并且只添加 isPrivacyKey
		if (type.isAssignableFrom(Boolean.class) && "isPrivacyKey".equals(name)) {
			// 重新写入
			field.set(result, flag);
		}
	}
}