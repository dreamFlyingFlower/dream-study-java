package com.wy.convert;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.wy.model.User;

/**
 * MapStrut使用
 * 
 * <pre>
 * {@link Mapper}: 主要注解
 * {@link Mapper#componentModel()}: 注入模式,默认default需要使用{@link Mappers#getMapper(Class)}.spring表示是可以通过spring的方式注入
 * {@link Mapper#nullValuePropertyMappingStrategy()}: source为null时target的处理策略
 * ->{@link NullValueMappingStrategy#RETURN_NULL}: 默认,即target中对应的字段也设置为null
 * ->{@link NullValueMappingStrategy#RETURN_DEFAULT}: 设置为默认值,List设置为空集合,Map设置为空Map
 * 
 * {@link DecoratedWith}: 装饰器,对接口中的方法做统一处理.如果有特别的逻辑要实现.必须时抽象类
 * 
 * {@link Mapping}: 转换的主要注解
 * {@link Mapping#target()}: 转换后的属性
 * {@link Mapping#source()}: 源属性
 * {@link Mapping#expression()}: 设置target时的表达式,当前只支持Java
 * {@link Mapping#nullValuePropertyMappingStrategy()}: 同Mapper,优先级更高
 * </pre>
 *
 * @author 飞花梦影
 * @date 2025-12-29 16:00:07
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper(componentModel = "spring")
@DecoratedWith(UserDecorate.class)
public interface UserConvert {

	/**
	 * @Mapper(componentModel = "default")时注入方式,可以让当前接口在非spring类中使用
	 */
	UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

	/**
	 * 转化成 target 对象时,createTime字段的值,会设置为System.currentTimeMillis()
	 * 
	 * @param object
	 * @return
	 */
	@Mapping(target = "createTime", expression = "java(System.currentTimeMillis())")
	User toUser(Object object);
}