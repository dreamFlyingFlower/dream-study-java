package com.wy.database;

/**
 * @description 数据库通用技术
 * @instruction 分布式数据库存储:sharding-jdbc,缓存技术
 * @author paradiseWy 2019年3月23日 上午11:04:08
 * @git {@link https://github.com/mygodness100}
 */
public class S_Database {

	/**
	 * 索引的使用 
	 * 1.like:当%name%的时候索引在该字段不起作用,只有在name%的时候才起作用
	 * 2.组合索引中,where条件的顺序必须和索引的一致,且必须是从第一个字段就一致.后面的字段若顺序相同,则使用.
	 * 		如:索引a,b,c,d,条件是a或ab,abc,abcd才会使用索引,ac,只会使用a作为索引条件,cd不使用索引
	 * 3.在where子句中,对null值进行判断的时候不会使用索引;解决办法是尽量给默认值
	 * 4.在where子句=号左边的字段不要进行运算,函数操作,否则将不会使用索引
	 * 5.单表索引最好不要超过6个
	 * 6.比较操作符,>,<,between,in,>=,<=才会使用索引,or,!=,<>不使用索引,可以改成in或union all
	 * 7.in和not in要慎用,有时候也会导致全表扫描,如in的条件是一个子查询,可以将子查询当作一张表来进行联表查询
	 * 		若是连续的值,可以用between and代替
	 * 8.很多时候,可以用exists代替in
	 * 9.当表中某个字段有大量重复值时,即便该字段简历索引,也不会使用索引,如性别
	 */
}