package com.wy.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 数据层基础接口
 * 
 * {@link NoRepositoryBean}:表明当前接口不是一个需要纳入jpa的接口
 * 
 * {@link JpaRepository}:泛型参数的第一个参数是实体类,第二个参数是实体类主键的类型,主要是增删改查的简单操作
 * 
 * {@link JpaSpecificationExecutor}:复杂语句查询
 * 
 * @author 飞花梦影
 * @date 2021-01-06 22:44:27
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

}