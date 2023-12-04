package com.wy.mybatis.sensitive;

/**
 * 解密处理器
 *
 * @author 飞花梦影
 * @date 2023-11-20 13:43:44
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface DecryptManager {

	/**
	 * 解密
	 *
	 * @param result resultType的实例
	 * @return T
	 * @throws IllegalAccessException 字段不可访问异常
	 */
	<T> T decrypt(T result) throws IllegalAccessException;
}