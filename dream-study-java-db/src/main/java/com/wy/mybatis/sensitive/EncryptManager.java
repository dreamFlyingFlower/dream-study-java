package com.wy.mybatis.sensitive;

import java.lang.reflect.Field;

/**
 * 加密处理器
 *
 * @author 飞花梦影
 * @date 2023-11-20 13:45:01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface EncryptManager {

	/**
	 * 加密
	 *
	 * @param declaredFields 加密字段
	 * @param paramsObject 对象
	 * @param <T> 入参类型
	 * @return 返回加密
	 * @throws IllegalAccessException 不可访问
	 */
	<T> T encrypt(Field[] declaredFields, T paramsObject) throws IllegalAccessException;
}