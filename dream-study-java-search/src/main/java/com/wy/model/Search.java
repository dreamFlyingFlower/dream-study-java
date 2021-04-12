package com.wy.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.Getter;
import lombok.Setter;

/**
 * 搜索实体类
 * 
 * @apiNote Document注解表明该类是一个可以放入es中的类,indexName:索引库名,type:放入es中的类型名,
 *          类似数据库的table,shards:分片数量,默认为5,replicas:副本数量,默认为1
 * @author ParadiseWY
 * @date 2019年10月13日 下午3:42:08
 */
@Getter
@Setter
@Document(indexName = "search", type = "search")
public class Search implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键标识,该id为spring中的注解,非数据库持久化的javax.persistence中的id注解
	 */
	@Id
	private Long id;

	/**
	 * 标记为文档的字段,并指定字段映射属性
	 * 
	 * @apiNote type:字段类型,Text时会自动分词,生成索引,Keyword存储时不会建立索引;Date类型建议存储为long型
	 *          index:是否索引,默认true,store:是否存储,默认false,analyzer:分词器名称
	 */
	@Field(type = FieldType.Text)
	private String username;
}