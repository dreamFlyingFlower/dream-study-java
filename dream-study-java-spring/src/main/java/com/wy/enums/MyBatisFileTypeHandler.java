package com.wy.enums;

import org.apache.ibatis.type.MappedTypes;

/**
 * 使用例子
 * 
 * @author 飞花梦影
 * @date 2024-05-22 17:58:05
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@MappedTypes(FileType.class)
public class MyBatisFileTypeHandler extends CommonEnumTypeHandler<FileType> {

	public MyBatisFileTypeHandler() {
		super(FileType.values());
	}
}