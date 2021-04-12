# MyCat



[官网](http://www.mycat.io/),具体见Mysql_Mycat.pdf



# 概述

* 纵向切分:把数据库切成多个数据库,根据路由规则匹配进行数据库操作.mycat只能实现2张表的连接查询
* 横向切分:把单张表中的字段切成多个表,根据id关联.mycat中不能表连接查询
* 逻辑库:mycat中database属性,逻辑上存在,物理上未必存在,主要针对纵向切分提供概念
* 逻辑表:mycat中table属性,主要针对横向切分
* 默认端口:8066
* datahost:数据主机,mysql存放的物理主机地址,每个主机可以存放一个或多个datanode
* datanode:数据节点,database的物理存放节点,每个节点可以分配一个或多个数据库
* mycat只能访问mysql的schema,不能自动创建逻辑库对应的真实数据库,也不能创建逻辑表对应的物理表



# 命令

* 启动:mycat/bin下,mycat start
* 停止:mycat stop
* 重启:mycat restart
* 状态:mycat status
* 远程访问:mysql -u username -p password -hmycat_ip -P8066



# MySQL主从
* 需要Mysql5.5以上,在linux环境安装搭建主从

* 将mysql的配置文件(my.conf)复制到/etc下,会覆盖默认的配置文件

* 修改my.conf的server-id=1,这是主从的唯一标识符,从数据库的server-id必须大于主库

* 主库取消log_bin的注释,值可自定义,从库可使用默认值,也可注释.这是操作日志,记录了数据库中所有的操作命令

* 重启mysql,service mysqld restart

* 查看master二进制日志,show master status;\G

* 为从库创建用户信息

  ```mysql
  # repl是用户名,可随意,ip后最后以为%表示任意数字,password是repl用户密码,随意;
  create user 'repl'@'192.168.1.%' identified by 'password';
  # 给salve赋repl的权限,*.*表示所有表
  grant replication salve on *.* to 'repl'@'192.168.1.%' identified by 'password';
  # 给来自任意ip的repl用户赋值所有权限
  grant all privileges on *.* to 'repl'@'%' identified by 'password';
  # 刷新权限
  flush privileges;
  ```

* 修改数据库的uuid,该文件为/var/lib/mysql/auto.conf,若是主从库的uuid一样,需要修改为不一样的.重启数据库

* 配置从库,登录从库,停止从库stop slave

* 配置从库访问主库的信息

  ```mysql
  change master to master_host='主ip',master_user='repl',master_password='password',
  master_log_file='show master status的File值',master_log_pos='show master status的Position值'
  ```

* 启动slave,show slave status;\G,若是其中展示的信息中以下几个没错,就是配置成功
  * Last_IO_Errno:0
  * Last_SQL_Errno:0
  * Slave_SQL_Running_State:不显示错误信息



# MyCat配置

* 下载mycat的安装包,解压

* 主库mysql给mycat新建一个用户,注意不同版本赋权语句不一样,8以上自行查找

  ```mysql
  grant all privileges on *.* 'mycat'@'%' identified by 'password' with grant option;
  flush privileges;
  ```



## rule.xml

* 分库策略,修改了rule之后,需要删除bin目录下的ruleData文件夹,该文件夹中存放了分片的规则信息,但是每次修改了rule.xml之后并不会重置该文件夹,需要手动删除.重启mycat后自动创建该文件夹

* tableRule:分片规则配置
  * name:属性,分片规则名称,自定义,在

  * rule:子标签,具体分片规则
    * column:子标签,需要进行分片的表字段
    * algorithm:子标签,分片的算法规则,对应function的name属性

* function:分片算法,可以使用mycat自带的,也可以自己实现
  * name:属性,算法名称
  * class:属性,算法实现类,自定义算法的类完整地址
  * property:子标签,根据算法不同,需要的参数不同,见官网



## schema.xml

* 分库,分表,集群,读写分离,负载均衡策略

* schema:
  * name:属性,配置逻辑库名,如logicName,并非真实存在的数据库
  * checkSQLschema:属性,如select * from logicName.tableName时,
    * true,发送到mysql语句会去掉logicName
    * false,原样发送sql语句到mysql
  * sqlMaxLimit:属性,最大查询数据条数.若查询时不带分页条件,默认查该属性值的数据
  * table:子标签.如有多个逻辑表,可写多个table标签,逻辑表的表名要和物理表名一致
    * name:逻辑表名,如table1,并非真实存在
    * dataNode:mycat中dataNode标签的name属性值,多个用逗号隔开或使用$0-100
    * rule:分片规则,对应rule.xml的tableRule的name属性,用来计算sql应该发送到那一个物理库中
    * type:global,全局表,只在一个库中存在即可
    * autoIncrement:主键自增策略
    * primaryKey:主键字段名

* dataNode:定义物理db的信息,可以定义多个
  * name:逻辑节点名称,对应table中的dataNode属性
  * dataHost:对应dataHost的name属性
  * database:物理主机中,真实的数据库名称

* dataHost:定义物理数据主机的安装位置
  * name:属性,逻辑节点名称,在dataNode标签中需要使用
  * maxCon/minCon:属性,最大连接数/最小连接数
  * dbType:属性,数据库类型,mysql
  * dbDriver:属性,驱动类型,native,使用mycat提供的本地驱动
  * balance:属性,读和写的时候如何实现负载均衡.默认1时表示写走writeHost,读走readhost
  * writeType:属性,写策略.当集群时,多个writeHost,如何进行写操作.0表示按顺序,从上到下
  * switchType:属性,是否自动切换,默认1自动切换
  * heartbeat:子标签,内容为心跳语句
  * writeHost:子标签,定义物理数据库的连接.写数据库的定义标签,可实现读写分离操作
    * host:逻辑节点名
    * url:连接地址ip:port
    * user:登录用户名
    * password:密码
  * readHost:writeHost的子标签,需要读写分离时,在标签里配置
    * host:逻辑节点名
    * url:真实数据库连接地址ip:port
    * user:数据库用户名
    * password:数据库密码



## server.xml

* mycat对外服务策略

* property:属性定义
  * serverPort:mycat的服务端口,默认8066
  * managerPort:mycat的管理端口,默认9066

* user:访问mycat的属性,类似访问mysql的属性
  * name:访问mycat用户名,类似mysql的登录名
  * property:
    * password:访问密码
    * schemas:可访问的逻辑库名,多个逗号隔开,对应schema.xml的name属性
    * readOnly:是否只读,true只读,默认false

* privileges:user的子标签,表级DML权限设置
  * check:true检查权限,false不检查权限

* schema:privileges的子标签,对数据库的具体访问权限
  * dml:权限值,4位数字,分别表示insert,update,select,delete,0表示禁止,1表示不禁止,如0110,0000,1111

* table:schema的子标签,具体的表访问权限
  * name:逻辑表名,非真实表名
  * dml:权限值,4位数字,分别表示insert,update,select,delete,0表示禁止,1表示不禁止,如0110,0000,1111



# 数据库读写分离

* 直接在schema.xml中的writeHost标签里写readHost,而writeHost的balance值设为1即可



# 数据库集群

* 需要在schema.xml的dataHost中配置多个writeHost标签,而每个writeHost中配置一个readHost标签



# HAProxy+KeepAlived搭建高可用集群

* 见Mysql_Mqcat文档