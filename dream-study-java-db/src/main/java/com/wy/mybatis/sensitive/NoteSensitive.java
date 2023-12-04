package com.wy.mybatis.sensitive;

import org.apache.ibatis.type.TypeHandler;

/**
 * 利用MyBatis的Intercept进行参数加解密,还可以通过{@link TypeHandler}对单个字段进行处理
 * 
 * {@link SensitiveData},{@link EncryptData}:在类和字段上使用,或者单独对Mapper中的方法参数使用,暂时只对String有效
 * {@link EncryptManager},{@link DecryptManager}:对参数和结果加解密处理的接口
 * {@link DefaultDecryptManager},{@link DefaultEncryptManager}:默认参数和结果加解密处理类
 * {@link ParameterInterceptor}:对参数进行加解密处理
 * {@link ResultSetInterceptor}:对结果集进行加解密处理
 *
 * @author 飞花梦影
 * @date 2023-11-20 15:19:59
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class NoteSensitive {

}