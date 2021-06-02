package com.wy.config;

import java.time.LocalDateTime;

import org.apache.ibatis.reflection.MetaObject;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;

/**
 * 自定义填充策略
 *
 * @author 飞花梦影
 * @date 2021-06-02 09:48:05
 */
public class MybatisPlusAutoFill implements MetaObjectHandler {

	/**
	 * 插入时的填充策略,只有当{@link @TableField(fill = FieldFill.INSERT)}修饰的字段为null时才使用
	 * 
	 * @param metaObject
	 */
	@Override
	public void insertFill(MetaObject metaObject) {
		// 第2个参数填写实体类中的属性名
		strictFillStrategy(metaObject, "createtime", LocalDateTime::now);
	}

	/**
	 * 插入时的填充策略,只有当{@link @TableField(fill = FieldFill.UPDATE)}修饰的字段为null时才使用
	 * 
	 * @param metaObject
	 */
	@Override
	public void updateFill(MetaObject metaObject) {
		strictFillStrategy(metaObject, "createtime", LocalDateTime::now);
	}
}