package com.wy.mybatis.sensitive;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import dream.flying.flower.digest.DigestHelper;

/**
 * 参数拦截,进行加解密
 *
 * @author 飞花梦影
 * @date 2023-11-20 14:27:21
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Component
@Intercepts({ @Signature(type = ParameterHandler.class, method = "setParameters", args = PreparedStatement.class), })
public class ParameterInterceptor implements Interceptor {

	@Autowired
	private EncryptManager encryptManager;

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		// @Signature指定了type=parameterHandler后,这里的invocation.getTarget()便是parameterHandler
		// 若指定ResultSetHandler,这里则能强转为ResultSetHandler
		ParameterHandler parameterHandler = (ParameterHandler) invocation.getTarget();
		// 获取参数对像,即 mapper 中 paramsType 的实例
		Field parameterField = parameterHandler.getClass().getDeclaredField("parameterObject");
		parameterField.setAccessible(true);
		// 取出实例
		Object parameterObject = parameterField.get(parameterHandler);
		// 搜索该方法中是否有需要加密的普通字段
		List<String> paramNames = searchParamAnnotation(parameterHandler);
		if (parameterObject != null) {
			Class<?> parameterObjectClass = parameterObject.getClass();
			SensitiveData sensitiveData = AnnotationUtils.findAnnotation(parameterObjectClass, SensitiveData.class);
			if (Objects.nonNull(sensitiveData)) {
				// 取出当前当前类所有字段,传入加密方法
				Field[] declaredFields = parameterObjectClass.getDeclaredFields();
				encryptManager.encrypt(declaredFields, parameterObject);
			}
			// 对普通字段进行加密
			if (CollectionUtils.isNotEmpty(paramNames)) {
				// 反射获取 BoundSql 对象,此对象包含生成的sql和sql的参数map映射
				Field boundSqlField = parameterHandler.getClass().getDeclaredField("boundSql");
				boundSqlField.setAccessible(true);
				BoundSql boundSql = (BoundSql) boundSqlField.get(parameterHandler);
				System.out.println(boundSql.toString());
				PreparedStatement ps = (PreparedStatement) invocation.getArgs()[0];
				// 改写参数
				processParam(parameterObject, paramNames);
				// 改写的参数设置到原parameterHandler对象
				parameterField.set(parameterHandler, parameterObject);
				parameterHandler.setParameters(ps);
			}
		}
		return invocation.proceed();
	}

	private void processParam(Object parameterObject, List<String> params) throws Exception {
		// 处理参数对象 如果是 map 且map的key 中没有 tenantId,添加到参数map中
		// 如果参数是bean,反射设置值
		if (parameterObject instanceof Map) {
			@SuppressWarnings("unchecked")
			Map<String, String> map = ((Map<String, String>) parameterObject);
			for (String param : params) {
				String value = map.get(param);
				map.put(param, value == null ? null : DigestHelper.aesEncrypt(ConstSensitive.AES_SECRET, value));
			}
		}
	}

	private List<String> searchParamAnnotation(ParameterHandler parameterHandler)
			throws NoSuchFieldException, ClassNotFoundException, IllegalAccessException {
		Class<ParameterHandler> handlerClass = ParameterHandler.class;
		Field mappedStatementFiled = handlerClass.getDeclaredField("mappedStatement");
		mappedStatementFiled.setAccessible(true);
		MappedStatement mappedStatement = (MappedStatement) mappedStatementFiled.get(parameterHandler);
		String methodName = mappedStatement.getId();
		Class<?> mapperClass = Class.forName(methodName.substring(0, methodName.lastIndexOf('.')));
		methodName = methodName.substring(methodName.lastIndexOf('.') + 1);
		Method[] methods = mapperClass.getDeclaredMethods();
		Method method = null;
		for (Method m : methods) {
			if (m.getName().equals(methodName)) {
				method = m;
				break;
			}
		}
		List<String> paramNames = null;
		if (method != null) {
			Annotation[][] pa = method.getParameterAnnotations();
			Parameter[] parameters = method.getParameters();
			for (int i = 0; i < pa.length; i++) {
				for (Annotation annotation : pa[i]) {
					if (annotation instanceof EncryptData) {
						if (paramNames == null) {
							paramNames = new ArrayList<>();
						}
						paramNames.add(parameters[i].getName());
					}
				}
			}
		}
		return paramNames;
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {

	}
}