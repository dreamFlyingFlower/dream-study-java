package com.wy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * MyBatis-plus学习
 * 
 * MyBatis-plus特性:<br>
 * 无侵入,损耗小,支持Lambda;支持分布式主键(雪花),支持XML热加载;支持ActiveRecord形式调用,
 * 实体类只需要继承Model类即可进行CRUD;支持自定义全局通用操作,支持关键词自动转义.<br>
 * 内置代码生成器,内置分页插件,内置性能分析插件,内置全局拦截插件,内置Sql注入剥离器
 * 
 * {@link BaseMapper#insert(Object)}:插入,null也插入,若默认值插入null会报错<br>
 * {@link BaseMapper#updateById(Object)}:更新,只更新实体类中的非null值<br>
 * {@link BaseMapper#update(Object, Wrapper)}:条件更新,可使用如下:<br>
 * {@link QueryWrapper}:只作为查询条件使用,接在where后面
 * {@link UpdateWrapper}:做条件同时更新,如将字段更新为null
 * 
 * @author 飞花梦影
 * @date 2021-04-08 11:25:56
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@MapperScan("com.wy.mapper")
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}