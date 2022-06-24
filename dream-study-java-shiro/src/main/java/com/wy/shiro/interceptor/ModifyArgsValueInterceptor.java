package com.wy.shiro.interceptor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import com.wy.lang.StrTool;
import com.wy.shiro.utils.SeqGenerator;

import lombok.extern.slf4j.Slf4j;

/**
 * 修改参数值
 * 
 * @author 飞花梦影
 * @date 2022-06-24 17:04:43
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Intercepts({ @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }) })
@Slf4j
public class ModifyArgsValueInterceptor implements Interceptor {

	private SeqGenerator seqGenerator;

	private int workId = 0;

	private String primaryKey = "id";

	public ModifyArgsValueInterceptor() {

	}

	public ModifyArgsValueInterceptor(int workId, String primaryKey) {
		this(null, workId, primaryKey);
	}

	public ModifyArgsValueInterceptor(SeqGenerator seqGenerator, int workId, String primaryKey) {
		if (StrTool.isNotBlank(primaryKey)) {
			this.primaryKey = primaryKey;
		}
		if (Objects.isNull(seqGenerator)) {
			this.workId = workId;
			init();
		} else {
			this.seqGenerator = seqGenerator;
		}
	}

	public ModifyArgsValueInterceptor(SeqGenerator seqGenerator, String primaryKey) {
		this(seqGenerator, 0, primaryKey);
	}

	private void init() {
		if (seqGenerator == null) {
			synchronized (this) {
				if (seqGenerator == null) {
					seqGenerator = new SeqGenerator(workId);
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object[] args = invocation.getArgs();
		if (args == null || args.length != 2) {
			return invocation.proceed();
		} else {
			MappedStatement ms = (MappedStatement) args[0];
			// 操作类型
			SqlCommandType sqlCommandType = ms.getSqlCommandType();
			// 只处理insert操作
			if (Objects.nonNull(sqlCommandType) && sqlCommandType == SqlCommandType.INSERT) {
				if (args[1] instanceof Map) {
					// 批量插入
					List list = (List) ((Map) args[1]).get("list");
					for (Object obj : list) {
						setProperty(obj, primaryKey, seqGenerator.nextId());
					}
				} else {
					setProperty(args[1], primaryKey, seqGenerator.nextId());
				}
			}
		}
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object o) {
		return Plugin.wrap(o, this);
	}

	@Override
	public void setProperties(Properties properties) {
		String workId = properties.getProperty("workId");
		if (StrTool.isNotBlank(workId)) {
			this.workId = Integer.parseInt(workId);
		}
		String primaryKey = properties.getProperty("primaryKey");
		if (StrTool.isNotBlank(primaryKey)) {
			this.primaryKey = primaryKey;
		}
		init();
	}

	/**
	 * 设置对象属性值
	 * 
	 * @param obj 对象
	 * @param property 属性名称
	 * @param value 属性值
	 */
	private void setProperty(Object obj, String property, Object value) {
		try {
			Field field = obj.getClass().getDeclaredField(property);
			if (Objects.nonNull(field)) {
				field.setAccessible(true);
				Object val = field.get(obj);
				if (Objects.isNull(val)) {
					BeanUtils.setProperty(obj, property, value);
				}
			}
		} catch (IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
			log.warn(e.toString());
		}
	}
}