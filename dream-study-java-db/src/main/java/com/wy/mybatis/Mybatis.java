package com.wy.mybatis;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.mybatis.spring.annotation.MapperScan;

/**
 * Mybatis使用
 * 
 * Mybatis扫描接口的常见方式:
 * 
 * <pre>
 * {@link MapperScan}:将该直接添加到启动类上,同时指定扫描的包,只会扫描指定的包,推荐使用
 * {@link Mapper}:直接将该注解添加到mapper接口上,mybatis会自动扫描
 * 第三种需要大量的配置,手动将扫面的接口配置到SqlSessionFactoryBean种.多数据源时需要使用
 * </pre>
 * 
 * Mybatis使用sql的2种方式:
 * 
 * <pre>
 * 1.写在对应的xml文件中,比直接写在方法上更灵活
 * 2.直接在接口中的方式上使用{@link Select}{@link Update}等注解,将sql写入其中,不推荐使用
 * </pre>
 * 
 * Mybatis读写分离: @see https://blog.csdn.net/qq_28929589/article/details/79191546
 * 
 * Mybatis2级缓存:
 * 
 * <pre>
 * 一级缓存:默认开启,即同一个sqlsession中使用sql第一次查数据库,将数据存入缓存.
 * 		第2次sql若相同,则直接从缓存中取,不查数据库.若2次查询中做了增删改操作,则清空缓存
 * 
 * 二级缓存:默认是不开启,需要在Mybatis的主配置文件和需要使用的mapper.xml中开启,
 * 		使用二级缓存存放的实体类需要实现Serializable接口.
 * 		二级缓存是在同一个命名空间中的sql进行缓存,第1次查数据库,第2次查缓存,中间有增删改操作清空缓存
 * 		开启缓存后的默认配置:在每一个mybatis的mapper.xml中添加cache标签即可开启二级缓存
 * 
 * xml文件中所有的select语句会被缓存,所有的insert,update和delete语句会刷新缓存
 * 缓存会使用默认的Least Recently Used(LRU,最近最少使用原则)的算法来回收缓存空间
 * 缓存会存储列表集合或对象(无论查询方法返回什么)的1024个引用
 * </pre>
 * 
 * @auther 飞花梦影
 * @date 2021-07-24 17:38:47
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class Mybatis {

}