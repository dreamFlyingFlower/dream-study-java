package com.wy.mybatis;

/**
 * mybatis缓存有2级,第一级缓存是默认开启的,即同一个sqlsession中使用sql第一次查数据库,将数据存入缓存,
 * 
 * @第2次若是同样的sql则不查数据库,直接从缓存中取数据,若2次查询中做了增删改操作,则清空缓存
 * @第二级默认是不开启的,可在mybatis的主配置文件中开启,并且在需要的mapper文件中开启, 需要在二级缓存中存放的实体类需要实现Serializable接口
 * @第2级缓存是在同一个命名空间中的sql进行缓存,查询第1次,查数据库,第2次查缓存,中间有增删改操作清空缓存 开启缓存后的默认配置:(在每一个mybatis的mapper.xml中添加cache标签即可开启二级缓存)
 * 
 * @映射文件所有的select语句会被缓存,所有的insert,update和delete语句会刷新缓存
 * @缓存会使用默认的Least Recently Used(LRU,最近最少使用原则)的算法来回收缓存空间
 * @根据时间表,比如No Flush Interval,(CNFI,没有刷新间隔),缓存不会以任何时间顺序来刷新
 * @缓存会存储列表集合或对象（无论查询方法返回什么）的1024个引用
 * @缓存会被视为是read/write(可读/可写)的缓存,意味着对象检索不是共享的,而且可以很安全的被调用者修改,不干扰其他调用者或县城所作的潜在修改
 * 
 * @author wanyang
 */
public class MybatisCache {

}