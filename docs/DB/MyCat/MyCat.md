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
  * balance:属性,读和写的时候如何实现负载均衡
    * 0:不开启读写分离机制,所有读操作都发送到当前可用的writeHost 上
    * 1:默认,写走writeHost,读走readhost
    * 2:所有读操作都随机的在writeHost,readhost上分发
    * 3:所有读请求随机的分发到wiriterHost对应的readhost执行,writerHost不负担读压力,1.4 其以后版本有效
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



# MyCat集群



```shell
# 直接将整个MyCat复制
cp mycat mycat2 -R
vim wrapper.conf
# 设置jmx端口
wrapper.java.additional.7=-Dcom.sun.management.jmxremote.port=1985
vim server.xml
# 设置服务端口以及管理端口
<property name="serverPort">8067</property>
<property name="managerPort">9067</property>
# 重新启动服务
./startup_nowrap.sh
tail -f ../logs/mycat.log
```



# HAProxy高可用集群

* 见Mysql_Mqcat文档

* HaProxy配置

  ```cfg
  # 创建文件
  vim /app/haproxy/haproxy.cfg
  # 输入如下内容
  global
      log 127.0.0.1 local2
      maxconn 4000
      daemon
  defaults
      mode http
      log global
      option httplog
      option dontlognull
      option http-server-close
      option forwardfor except 127.0.0.0/8
      option redispatch
      retries 3
      timeout http-request 10s
      timeout queue 1m
      timeout connect 10s
      timeout client 1m
      timeout server 1m
      timeout http-keep-alive 10s
      timeout check 10s
      maxconn 3000
  listen admin_stats
      bind 0.0.0.0:4001
      mode http
      stats uri /dbs
      stats realm Global\ statistics
      stats auth admin:admin123
  listen proxy-mysql
      bind 0.0.0.0:4002
      mode tcp
      balance roundrobin
      option tcplog
      # 代理mycat服务
      server mycat_1 192.168.1.150:8066 check port 8066 maxconn 2000
      server mycat_2 192.168.1.150:8067 check port 8067 maxconn 2000
  ```



# PXC



* Percona XtraDB Cluster是针对MySQL用户的高可用性和扩展性解决方案,是一个针对事务性应用程序的同步多主机复制插件  
* 同步复制,事务可以在所有节点上提交
* 多主机复制,你可以写到任何节点
* 从slave服务器上的并行应用事件,真正的并行复制
* 自动节点配置
* 数据一致性,不再有未同步的从服务器
* 尽可能的控制PXC集群的规模,节点越多,数据同步速度越慢
* 所有PXC节点的硬件配置要一致,如果不一致,配置低的节点将拖慢数据同步速度
* PXC集群只支持InnoDB引擎,不支持其他的存储引擎
* PXC集群方案与Replication区别:
  * PXC集群方案所有节点都是可读可写的,Replication从节点不能写入,因为主从同步是单向的,无法从slave节点向master点同步
  * PXC同步机制是同步进行的,这也是它能保证数据强一致性的根本原因,Replication同步机制是异步进行的,它如果从节点停止同步,依然可以向主节点插入数据,正确返回,造成数据主从数据的不一致性
  * PXC是用牺牲性能保证数据的一致性,Replication在性能上是高于PXC的
  * PXC是用于重要信息的存储,例如订单,用户信息等.Replication用于一般信息的存储,能够容忍数据丢失,例如购物车,用户行为日志等



# 整体解决方案



* HAProxy作为负载均衡器,负责应用和MyCat之间的请求转发
* Mycat节点作为数据库中间件可以部署多个,负责处理HAProxy的请求
* 部署PXC集群,作为2个Mycat分片,每个PXC集群中有2个节点,作为数据的同步存储,存储重要信息
* 根据实际情况部署1个主从复制集群,存储不太重要的数据