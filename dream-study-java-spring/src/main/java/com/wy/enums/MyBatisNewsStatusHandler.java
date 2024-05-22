package com.wy.enums;

import org.apache.ibatis.type.MappedTypes;

/**
 * 使用例子
 * 
 * JPA同理,但是要在需要转换的字段上使用 @Convert(converter = JpaNewsStatusConverter.class)
 *
 * @author 飞花梦影
 * @date 2024-05-22 17:58:05
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@MappedTypes(FileType.class)
public class MyBatisNewsStatusHandler extends CommonEnumTypeHandler<FileType> {

	public MyBatisNewsStatusHandler() {
		super(FileType.values());
	}
}