package com.wy.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.wy.model.User;

import dream.flying.flower.framework.web.convert.BaseConvert;

/**
 * MapStruct
 * 
 * {@link Mapper#componentModel()}:当该值为spring时,标识该接口将注入到spring上下文
 * {@link Mapper#nullValueMappingStrategy()}:如果source为null时,对应的target的处理策略,默认是NullValueMappingStrategy.RETURN_NULL
 * ->{@link NullValueMappingStrategy#RETURN_NULL}:当source为null时,设置target为null
 * ->{@link NullValueMappingStrategy#RETURN_DEFAULT}:当source为null时,设置target为默认值,如空集合,空Map
 * {@link Mapper#nullValuePropertyMappingStrategy()}:null值匹配策略,默认设置为null
 * {@link Mapper#unmappedSourcePolicy()}:不匹配的source属性如何处理,默认忽略
 * {@link Mapper#unmappedTargetPolicy()}:不匹配的target属性如何处理,默认警告
 * 
 * @author 飞花梦影
 * @date 2025-01-13 15:00:57
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
		unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MyMapStruct extends BaseConvert<User, User> {

	MyMapStruct INSTANCE = Mappers.getMapper(MyMapStruct.class);

	// 转化成target时,createTime字段的值,会设置为System.currentTimeMillis()
	@Mapping(target = "createTime", expression = "java(System.currentTimeMillis())")
	User toTarget(User source);

	// 特殊处理target的值,默认直接使用set/get
	@Mapping(target = "username", source = "username", qualifiedByName = "toUpperCase")
	User toTarget1(User source);

	@Named("toUpperCase")
	default String toUpperCase(String value) {
		// 这里写转换大写的逻辑
		return value == null ? null : value.toUpperCase();
	}
}