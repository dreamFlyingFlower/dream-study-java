package com.wy.database;

/**
 * mysql中字符串下标从1开始,而不是从0开始<br>
 * mysql中没有递归方法<br>
 * 分库分表中的问题:多数据库管理;跨库事务以及join查询;分布式全局唯一id;方案:多数据源;
 * mysql5.7版本支持XA分布式事务atomikos,通过业务避免join查询,异构方式内存计算(spark,es);zk,redis
 * 
 * @author paradiseWy 2019年3月20日 下午9:41:25
 * @git {@link https://github.com/mygodness100}
 */
public class S_Mysql {

	/**
	 * linux安装mysql 1.解压myql或下载mysql的压缩包,将解压的文件复制到/usr/local/mysql
	 * 2.必须新建一个用户组,添加一个用户来对mysql有操作权限,最好不要给root,测试就无所谓了 3.新建用户组:groupadd mysql(名字随意);新建用户useradd -r -g
	 * mysql(用户名) mysql(组名) 4.必须在usr/local/mysql中,给mysql赋组和用户权限, chgrp -R mysql .:R表示目录, . 不能掉,表示当前目录
	 * chown -R mysql .:给当前文件夹赋值mysql用户权限 5.删除原始的mysql配置文件,rm -rf /etc/my.conf,该文件是跟随系统存在的,即使没有装mysql也会有
	 * 6.初始化数据库.在local中执行命令:./scripts/mysql_install_db --user=mysql 7.复制配置文件.cp
	 * support-files/my-default.cnf /etc/my.cnf 8.复制启动文件.cp support-files/mysql.server
	 * /etc/rc.d/init.d/mysql(自启动文件夹,是一个服务文件夹) 9.添加启动软连接.ln -s /usr/local/mysql/bin/mysql /usr/bin/mysql
	 * 10.若忘记密码,可在/etc/my.conf中添加skip-grant-tables,启动安全模式,将不会验证密码,重启mysql服务 11.修改密码:use mysql;update
	 * user set password=password("新密码") where root = 'root';flush privileges; 12.赋权.grant all
	 * privileges on *.* to root@'%' identified by 'pwd' with grant option;flush privileges; all
	 * privileges:赋值所有权限 *.*:那个用户的那些表 root@'%':这些权限给那个用户,可以让某个ip或所有的ip访问数据库,%表示所有 identified
	 * by:需要验证的密码是什么
	 */

	/**
	 * 1.mysql中sql语句连接字符串需要用concat 2.mysql分页用limit,公式为limit
	 * (pageIndex-1)*pageCount,pageCount;从多少条数据开始,取多少条数据 3.ifnull(arg1,arg2);若arg1为空,则使用arg2
	 * 4.insert(str,begin,num,replaceStr):从str的begin位置开始,将num个字符串换成replaceStr
	 * 5.left(str,num),right(str,num):返回字符串最左边或最右边num个字符串,若num为null,返回null
	 * 6.lpad(str,num,fillStr),rpad(str,num,fillStr):用fillStr中的字符往str最左或右填充,直到长度到达num
	 * 7.repeat(str,num):将str重复num次 8.replace(str,a,b):将str中所有的a都换成b
	 * 9.substr(str,x,y):返回str中从x开始的y个字符,和java中不一样 10.rand():0-1的随机数
	 * 11.curdate(),curtime(),now():当前年月日,当前时分秒,当前年月日时分秒 12.unix_timestamp():返回当前时间戳的秒形式,即不要毫秒数
	 * 13.from_unixtime(timestamp):将标准时间戳除去毫秒数的值转成年月日时分秒的时间
	 * 14.date_format(date,format):将字符串时间或时间按format格式进行转换为相应的时间格式 15.group_concat([distinct] column
	 * [order by column asc/desc] [separator 'arg1']): 该方法能将相同的行结果或符合条件的结果组合起来,返回指定分隔符连接的字符串,默认逗号
	 */

	/**
	 * mysql视图 1.视图提高了查询的效率,提高了数据的独立性,提高了数据的保密性,可直接跟用户挂钩,设置权限 2.创建视图:create
	 * algorithm=[undefined|merge|temptable] view viewname as select数据 [with [cascaded|local] check
	 * option]: algorithm: merge:处理查询时的方式为替换式,即查询时直接将创建view时的语句带入查询视图的语句中
	 * temptable:具化式,处理查询时,先将创建视图的语句的数据查询出来,放在内存的临时表里,再从临时表取数据 undefined:不定义该参数,默认是merge,因为更高效 with
	 * check option:更新数据时不能插入或更新不符合视图限制条件的数据 cascaded,local:检测范围,默认是cascaded,可不设置
	 * 3.视图中的数据若来源于聚合函数,distinct,group by,having,union,from中引用了多个表,select引用了不可更新视图,
	 * 即视图中引入了非来自于基表的数据,则视图不可直接修改,需要通过修改基表的数据进行间接修改
	 */

	/**
	 * 存储过程:create procedure procedurename(in param1 type,int params2 type)
	 * 1.定义变量用declare,给变量赋值用set,如declare ddd varchar(64) default null,set ddd='rewre'
	 * 2.将存储过程查询的某个值赋给某个变量用into,如:select avg(salary) into avgsalary from table
	 * 3.存储过程中传入用in,多个参数用逗号隔开,参数要带上类型以及长度
	 */

	/**
	 * 一个类似递归的存储过程,只能从数据库中取得一个字段的值,并且用逗号拼接成字符串输出 存储过程P_getTreeIds,参数依次是表名,上级字段名,下级字段名,需要查询的参数 CREATE
	 * PROCUDURE P_getTreeIds(IN `tableName` varchar(30),IN `parentColumn` varchar(30),IN `childColumn`
	 * varchar(30),IN `paramId` int);
	 * 
	 * BEGIN
	 * 
	 * DECLARE result VARCHAR(2000);
	 * 
	 * SET result = ''; SET @children = CAST(paramId as CHAR); SET @sqlStr = concat("SELECT
	 * GROUP_CONCAT(",childColumn,") INTO @children FROM ",tableName, " WHERE
	 * FIND_IN_SET(",parentColumn,",@children)>0;"); PREPARE stmt from @sqlStr;
	 * 
	 * WHILE @children is not null DO
	 * 
	 * SET result = concat(result,',',@children);
	 * 
	 * EXECUTE stmt;
	 * 
	 * END WHILE;
	 * 
	 * DEALLOCATE PREPARE stmt;
	 * 
	 * SELECT result;
	 * 
	 * END
	 */

	/**
	 * 自定义函数:create function functionname(param1 type,param2 type) returns type 1.if expression then
	 * statements end; 2.case when statements then statements when ... then ... else ... end;/case
	 * column when statement end; 3.while expression do statements end; 4.repeat statements until
	 * expression end;
	 */
}