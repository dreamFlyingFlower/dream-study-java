package com.wy.convert;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;

import com.wy.enums.UserType;

/**
 * 转换器,主要用在枚举字段上进行值的转换,字段需要添加{@link Convert}
 *
 * 需要实现{@link AttributeConverter}接口,第一个泛型是需要进行转换的类;第二个泛型是转换后的类型
 * 
 * @author 飞花梦影
 * @date 2021-01-08 17:16:30
 * @git {@link https://github.com/mygodness100}
 */
public class UserTypeConverter implements AttributeConverter<UserType, String> {

	/**
	 * 将实体类中的UserType类型转换为String存储到数据库中,主要是增删改查(DML)操作使用
	 */
	@Override
	public String convertToDatabaseColumn(UserType attribute) {

		return null;
	}

	/**
	 * 将数据库中的值反序列为实体类中的属性,主要是查询(DQL)操作使用
	 */
	@Override
	public UserType convertToEntityAttribute(String dbData) {
		return null;
	}
}