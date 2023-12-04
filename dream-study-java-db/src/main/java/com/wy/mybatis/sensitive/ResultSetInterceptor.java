package com.wy.mybatis.sensitive;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Properties;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

/**
 * 结果拦截,进行加解密
 *
 * @author 飞花梦影
 * @date 2023-11-20 14:32:13
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Component
@Intercepts({ @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = { Statement.class }) })
public class ResultSetInterceptor implements Interceptor {

	@Autowired
	private DecryptManager decryptManager;

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		// 取出查询的结果
		Object resultObject = invocation.proceed();
		if (Objects.isNull(resultObject)) {
			return null;
		}
		// 基于selectList
		if (resultObject instanceof ArrayList) {
			@SuppressWarnings("unchecked")
			ArrayList<Objects> resultList = (ArrayList<Objects>) resultObject;
			if (CollectionUtils.isNotEmpty(resultList) && needToDecrypt(resultList.get(0))) {
				for (Object result : resultList) {
					decryptManager.decrypt(result);
				}
			}
		} else {
			// 基于selectOne
			if (needToDecrypt(resultObject)) {
				decryptManager.decrypt(resultObject);
			}
		}
		return resultObject;
	}

	private boolean needToDecrypt(Object object) {
		Class<?> objectClass = object.getClass();
		SensitiveData sensitiveData = AnnotationUtils.findAnnotation(objectClass, SensitiveData.class);
		return Objects.nonNull(sensitiveData);
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {

	}
}