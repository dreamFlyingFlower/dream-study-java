package com.wy.database;

/**
 * oracle以表空间为单位,在表空间中建表.oracle中下标从1开始,不是从0开始
 * @author wanyang 2018年7月8日
 */
public class S_Oracle {

	/**
	 * 函数
	 * 1.oracle中null代表不确定,不能做四则运算
	 * 2.nvl(arg1,arg2):如果arg1为null,则使用arg2,相当于msql中的ifnull;
	 * 		nvl2(arg1,arg2,arg3):若arg1为null,则返回arg3,不为空则返回arg2
	 * 		nullif(arg1,arg2):如果arg1=arg2,则返回null,否则返回1
	 * 		decode(column,cnd1,val1,cnd2,val2,.......[,default]):字段的值若是等于cnd1,则显示val1,
	 * 		若是等于cnd2,则显示val2,依次类推.若没有default也都不符合,则返回null,类似于if-else
	 * 3.拼接字符串用||或concat,concat和mysql的用法一样,相当于mysql的concat,sqlserver的+
	 * 4.若在查询的条件中有转义字符,可以使用escape '\' 
	 * 5.排序中null问题,可以在desc或asc后接nulls first/last,代表null排开始或结束 
	 * 6.ceil,floor,round,trunc(截取数值,不截取字符串,不进行四舍五入),mod求磨
	 * 7.substr(arg,index,leng):截取字符串,index从下标几开始,下标从1开始,leng截取长度
	 * 8.replace(arg,src,des):替换所有符合条件的src
	 * 9.exists(expression):当expression中有结果的时候exists返回true,否则返回false
	 * 10.union/union all:并集,union去除重复的并排序,all不去重
	 * 11.intersect:交集,只取两个sql相同部分
	 * 12.minus:差集,只取不同部分
	 * 13.initcap(arg1):首字母大写,lower(arg1):转换为小写,upper(arg):转换为大写
	 * 14.ltrim(arg1,arg2):将arg1左边完全符合arg2的字符去除,如ltrim('ddewrew','dd')=ewrew.rtrim函数类似
	 * 15.translate(arg1,from,to):让arg1中的字符按照from中的字符替换成to中的字符.
	 * 		如translate('erdfsg','dfgh','1234')=er12s3,即让arg
	 * 16.instr(arg1,des[,pos]):查找des字符串位置,默认从1开始,若有pos,则从pos位置开始查找
	 * 17.months_between(arg1,arg2):返回2个日期之间相隔的月数;
	 * 		add_months(arg1,num):给arg1加上num数值的月数,若超过12,则到明年的日期
	 * 		next_day(arg1,星期几):返回指定日期后的最近一个星期几的日期,如next_day('06-2月-2019','星期五')
	 * 		last_day(arg1):返回指定日期所在月的最后一天的日期
	 * 		round和trunc也可对日期进行操作,当对年round时候,判断月是否超过中旬,超过则返回明年,天是判断周
	 * 18:to_number(arg1):将字符转换为数字;to_char(arg1):将数值转换为字符串;
	 * 		to_number和to_char在oracle中可以隐式互转,但是在mysql和sqlserver中不可以;
	 * 19.to_date(arg1[,format]):将字符转为日期,format为指定格式,默认为类似'01-2月-2018'
	 * 		oracle中时间类型不能和时间类型的字符串做比较,必须将字符串转为时间类型,并且要带上转换格式.
	 * 		但是mysql和sqlserver中可以直接进行比较,会隐式转换.
	 *		将时间转为字符串用to_char(date[,format]):将时间转换为指定格式的字符串
	 *20.length(column):查看字符的长度
	 *21.sysdate:获得当前系统时间;sqlserver是getdate();mysql是current_timestamp
	 */
	
	/**
	 * 忘记密码,需要先配置好oracle的环境变量,或直接进入oracle下的product下的使用sqlplus
	 * 控制台sqlplus /nolog;conn /as sysdba;alter user 用户名 identified by 新的密码;
	 */
	
	/**
	 * sql语句中的关键字
	 * 1.any:当子查询有多个结果时,而查询的结果只需要满足子查询结果的任意一个条件时,可在子查询前使用any,
	 * 		表示满足子查询任意一个条件的结果即可
	 * 2.all:类似any,但是他是必须满足子查询的所有条件才能得到结果
	 * 3.使用sql语句创建主键:constraints 主键名 primary key(字段名);
	 * 		表已经创建:alter table 表名 add constraints 约束名 primary key(字段名)
	 * 4.check约束:在新建表的时候可使用:check 字段名 操作,可多个约束,用and连接
	 * 5.创建主键索引:create sequence 索引名 [start with 1 step 1 cache 10],索引名.nextval,索引名.curval
	 * 5.分页:使用rownum关键字,rownum不能做大于的操作,只能做小于等于的操作.
	 * 		分页需要将第一次查询的结果作为子查询,并将rownum做为一个别名在外层查询中进行大于操作.
	 * 		总的来说,分页要先排序,之后编号,之后再分页,2次子查询,最外层返回结果
	 */
	
	/**
	 * oracle的专有方法
	 * 1.merge into table1 using table2 on cnd when matched then dothing1 when not matched then dothing2
	 * 		当cnd为条件.table2可以是单表,也可以使子查询,当匹配的时候做dothing1,不匹配时做dothing2
	 * 2.select column from table1 [where cnd] start with cnd1 or cnd2 connect by prior [order by]
	 * 		主要用于递归查询,类似sqlserver中的with as;column可任意写,cnd是查询第一次的条件,父级的标志
	 * 		如select name from menu start with menuid=1 connect by prior pid = menuid,
	 * 		其中靠近prior的字段是属于当前层的字段,而右边的则是下层的字段.
	 * 		即pid在左边则查询当前数据的上层数据,如果pid在右边则查询当前数据的下级数据
	 */

	public static void testOracle() {
		// name="tes%te"
		// select * from ti_basic where name like '%\%%' escape '\'
		// 意思是\是转义字符,将转义紧跟后面的%,此时%不代表任意字符,而代表%
	}
}
