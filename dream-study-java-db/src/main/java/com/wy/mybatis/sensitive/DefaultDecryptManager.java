package com.wy.mybatis.sensitive;

import java.lang.reflect.Field;

import dream.flying.flower.digest.DigestHelper;

/**
 * 默认解密处理器
 *
 * @author 飞花梦影
 * @date 2023-11-20 13:47:07
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class DefaultDecryptManager implements DecryptManager {

	@Override
	public <T> T decrypt(T result) throws IllegalAccessException {
		Class<?> resultClass = result.getClass();
		Field[] declaredFields = resultClass.getDeclaredFields();
		for (Field field : declaredFields) {
			if (field.isAnnotationPresent(EncryptData.class)) {
				field.setAccessible(true);
				Object object = field.get(result);
				// 只对string类型的数据进行加解密
				if (object instanceof String) {
					String value = (String) object;
					try {
						field.set(result, DigestHelper.aesDecrypt(ConstSensitive.AES_SECRET, value));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return result;
	}
}