package com.wy.mybatis.sensitive;

import java.lang.reflect.Field;

import dream.flying.flower.digest.DigestHelper;
import dream.flying.flower.reflect.ReflectHelper;

/**
 * 默认加密处理器
 *
 * @author 飞花梦影
 * @date 2023-11-20 13:48:21
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class DefaultEncryptManager implements EncryptManager {

	@Override
	public <T> T encrypt(Field[] declaredFields, T paramsObject) throws IllegalAccessException {
		for (Field field : declaredFields) {
			if (field.isAnnotationPresent(EncryptData.class)) {
				ReflectHelper.fixAccessible(field);
				Object object = field.get(paramsObject);
				// 只对string类型的数据进行加解密
				if (object instanceof String) {
					String value = (String) object;
					try {
						field.set(paramsObject, DigestHelper.aesEncrypt(ConstSensitive.AES_SECRET, value));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return paramsObject;
	}
}