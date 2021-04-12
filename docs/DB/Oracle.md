# Oracle

## SQL语句

	t:代表表,c:代表字段,dothing:表示操作,cnd:条件

### Oracle专用
* merge into t1 using t2|c1... on cnd when matched then dothing1 when not matched then dothing2

	当匹配cnd的时候做dothing1,不匹配时做dothing2,类似于case when.t2可以是单表,也可以使子查询或者是字段c1...

* select c1... from t1 [where cnd1] start with cnd connect by prior pid=cid [order by]

	主要用于递归查询,类似sqlserver中的with as
	start with:开始查询的节点
	ex:select name from menu start with menuid=1 connect by prior pid = menuid
	左侧的字段是属于当前层的字段,而右边的则是下层的字段.
	即pid在左边则查询当前数据的上层数据,如果pid在右边则查询当前数据的下级数据
	
* over
* over partition by
* over partition by order by
* row_number
* rollup
* cube
* grouping

###### 表复制语句
* create table newtable as select * from existtable;

	newtable表不能存在,因为插入的时候会自动创建表,并将查询的数据插入到表中.	
	
* insert into table1(c1,c2,c3...) select c4,c5,c6... from table2;

	table1必须存在,除了可以插入table2已经存在的字段外,还可以插入常量



# 高级函数

## over

## over partition by

## over partition by order by

## row_number

## rollup

## cube

## grouping



# 用户操作

## 1 用户

* 用户锁定:alter user username account lock;
* 用户解锁:alter user username account unlock;
* 用户密码设置:alter user username identified by pwd;
* 查看用户信息:select * from dba_users;
* 创建用户:create user username identified by pwd;
* 删除用户:drop user username cascade;



## 2 权限

* 赋权:grant 权限 to username
* 删除权限:revoke 权限 from username
* 查看权限:select * from session_privs;
* unlimited:该权限是拥有所有表空间配额的使用权限,权限太大,应该收回
  * revoke unlimited tablespace from username;