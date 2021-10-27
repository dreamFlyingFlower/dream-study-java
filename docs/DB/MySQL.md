# MySQL



# Linux安装



## 卸载MySQL

* 所有操作都是以linux的centos7版本为基础进行的,其他操作系统可百度
* 查询linux中是否安装了mysql

```shell
rpm -qa|grep -i mysql #查询系统中已经安装的mysql包,例如mysql-community...
```

* 卸载mysql

```shell
service mysqld status # 查看mysql状态
service mysqld stop # 停止mysql服务
rpm -ev mysql-community... # 卸载2中查询到的mysql安装包,若卸载时提示找不到依赖,可以在命令后加上--nodeps,该参数表示不检查依赖
```

* 找到系统中关于mysql的文件夹并删除

```shell
find / -name mysql # 查找系统中所有关于mysql的文件夹,之后通过命令删除
```



## 安装MySQL



### rpm安装

* 进入https://dev.mysql.com/downloads/repo/yum/,下载mysql的rpm包
* 根据linux版本选择mysql版本,点击download
* 页面跳转之后会需要登录,可以不登陆,直接点击左下方的No thanks,just start download或者直接右键点击该文件,复制连接地址
* 若是复制链接地址的,需要在linux中使用wget下载该文件

```shell
wget https://dev.mysql.com/get/mysql80-community-release-el7-3.noarch.rpm
```

* 若是直接下载的,可以把下载后的文件上传到linux中,此处下载的文件名为mysql80-community-release-el7-3.noarch.rpm,版本不一样,可能文件名不一样
* 安装镜像

```shell
rpm -ivh mysql80-community-release-el7-3.noarch.rpm
```

* 升级系统上的mysql软件包

```shell
yum update mysql-server
```

* 安装mysql

```shell
yum install mysql-server
```

* 安装成功之后会自动将mysql用户和mysql用户组添加到mysql中
* 设置mysql权限

```shell
chown mysql:mysql -R /var/lib/mysql
```

* 初始化mysql

```shell
mysqld --initialize # 初始化完成之后会生成密码,该密码在/var/log/mysqld.log中
grep "password" /var/log/mysqld.log # 查找安装时的默认密码
```

* 启动mysql,并设置开机启动

```shell
systemctl start mysqld或service mysqld start # 启动mysql
systemctl enable mysqld # 开机启动mysql
systemctl daemon-reload # 重新加载mysql的配置文件
```

* **启动mysql的时候报错**

```shell
# Job for mysqld.service failed because the control process exited with error code. See "systemctl status mysqld.service" and "journalctl -xe" for details
# 解决办法
chown mysql:mysql -R /var/lib/mysql
# 之后再启动mysql
service mysqld start
service mysqld status # 查看mysql状态
mysqladmin --version # 查看mysql版本
```

* 修改数据库密码root密码

```shell
mysqladmin -u root password "密码" # 从10那步的日志中找
```

* 若设置密码遇到问题,

```shell
# ERROR 1045 (28000): Access denied for user 'root'@'localhost' (using password: NO)
# 一般该错误是由密码错误引起,只能重置密码,查看3.重置密码
```



### 压缩包安装

1. 进入[mysql下载页](https://dev.mysql.com/downloads/mysql/),选择linux-Generic,选择下载版本**Linux - Generic (glibc 2.12) (x86, 64-bit), TAR**(根据需求选择),点击download进入下载页

2. 页面跳转之后会需要登录,可以不登陆,直接点击左下方的No thanks,just start download或者直接右键点击该文件,复制连接地址

3. 若是复制链接地址的,需要在linux中使用wget下载该文件

   ```shell
   wget https://dev.mysql.com/get/Downloads/MySQL-8.0/mysql-8.0.19-linux-glibc2.12-x86_64.tar
   ```

4. 若是直接下载的,可以把下载后的文件上传到linux中,此处下载的文件名为mysql-8.0.19-linux-glibc2.12-x86_64.tar,版本不一样,可能文件名不一样

5. 解压压缩包到指定目录,如/app/mysql

   ```shell
   tar -xvf mysql-8.0.19-linux-glibc2.12-x86_64.tar /app/mysql
   ```

6. 新建mysql的用户和用户组,新建mysql的数据目录和日志目录

   ```shell
   groupadd mysql # 创建mysql用户组
   useradd mysql # 创建mysql用户
   chmod -R 755 /app/mysql # 给文件赋权
   mkdir -p /app/mysql/data /app/mysql/logs # 创建数据和日志目录
   ```

8. 进入/app/mysql/bin中执行以下命令

   ```mysql
   mysqld --initalize --user=root --basedir=/app/mysql --datadir=/app/mysql/data
   # 若是包依赖找不到的错误,安装指定依赖即可
   # 初始化时会在屏幕上显示root密码,若是没有注意,可以在日志文件中查找
   # cat /var/log/mysql |grep password
   ```

9. 添加系统环境变量,开机启动

   ```shell
   vi /etc/profile
   export PATH=/app/mysql/bin:$PATH
   source /etc/profile
   cp /app/mysql/support-files/mysql.server  /etc/init.d/mysqld
   chkconfig --add mysqld # 或者chkconfig mysql on
   vi /etc/init.d/mysqld # 价格datadir和basedir的目录改成自己的
   ```

###  启动数据库

* service mysqld start/systemctl start mysqld

* service mysqld restart/systemctl  restart mysqld



### 停止数据库

* service mysqld stop/systemctl stop mysqld

* mysqladmin -uroot -p123456 shutdown

* /etc/init.d/mysqld stop

* kill -USER2 \`cat path/pid\`:不要用这种方法,可能造成数据丢失



### 多实例

> 一台机器上开多不同的服务端口,运行多个Mysql服务进程,这些Mysql多实例公用一套安装程序,使用相同的/不同的my.cnf配置,启动程序,数据文件

* 配置多个数据目录,多个配置文件及多个启动程序实现多实例

* 多实例启动mysql

```shell
mysqld_safe --defaults-file=/app/mysql/data/3306/my.cnf 2>&1 > /dev/null &
mysqld_safe --defaults-file=/app/mysql/data/3307/my.cnf 2>&1 > /dev/null &
```

* 多实例停止mysql

```shell
mysqladmin -uroot -p123456 -S /app/mysql/data/3306/mysql.sock shutdown
mysqladmin -uroot -p123456 -S /app/mysql/data/3307/mysql.sock shutdown
```



##  修改密码

* 在mysql环境外

```mysql
# 单实例
mysqladmin -uroot -poldpwd password newpwd;
# 多实例
mysqladmin -uroot -poldpwd password newpwd -S /app/mysql/data/3306/mysql.sock
```

* 在mysql环境中

```mysql
update mysql.user set password=PASSWORD('newpwd') where user='root';
flush privileges;
# 或者如下
set global validate_password.policy=0;
set global validate_password.length=1;
ALTER USER "root"@"localhost" IDENTIFIED BY "新密码";
```



## 忘记密码

```shell
# 停止mysql
systemctl stop mysqld
# /etc/init.d/mysqld stop
# 跳过权限启动mysql
mysqld_safe --skip-grant-tables --user=mysql &;
# 如果上面的命令无效报错,则执行下面这段命令
# mysqld --skip-grant-tables --user=mysql &;
# 或者直接在/etc/my.cnf加上如下,之后再正常启动mysql
# skip-grant-tables
# 无密码登录mysql
mysql -uroot -p
# 更新root密码
ALTER USER 'root'@'localhost' IDENTIFIED BY 'newpwd';
# 或者
UPDATE MYSQL.`user` SET PASSWORD=PASSWORD('newpwd') WHERE `user` = 'root';
# 刷新权限
FLUSH PRIVILEGES;
```

若修改密码提示错误:**ERROR 1290 (HY000): The MySQL server is running with the --skip-grant-tables option so it cannot execute this statement**

```shell
# 解决方法,刷新权限
FLUSH PRIVILEGES;
# 再修改密码
ALTER USER 'root'@'localhost' IDENTIFIED BY 'newpwd';
# 再刷新权限
FLUSH PRIVILEGES;
```



## 登录加密

1. **注意打开远程访问时,mysql8之前的版本和8以后的版本不一样,因为登录时密码的加密方式不一样.MYSQL_NATIVE_PASSWORD是5的加密方式,8的加密方式改成了caching_sha2_password**,若是用远程访问工具登录数据库时,需要做部分修改

2. 创建用户时指定登录的加密方式,该方式只会影响单个用户,不会影响其他用户

   ```mysql
   CREATE USER 'newuser'@'%' IDENTIFIED WITH MYSQL_NATIVE_PASSWORD BY 'newpwd';
   ```

3. 修改用户登录时候的加密方式

   ```mysql
   ALTER USER 'newuser'@'%' IDENTIFIED WITH mysql_native_password BY 'newpwd';
   ```

4. 在配置文件的mysqld下加上如下配置,则所有的加密方式都用旧的

   ```mysql
   default_authentication_plugin=mysql_native_password
   service mysqld restart # 重启之后还需要重置原帐号密码,重新设置
   update mysql.user set authentication_string='' where user='root';
   alter user 'root'@'%' identified by '123456';
   flush privileges;
   ```

5. 登录mysql

   ```shell
   # mysql是服务名称,默认mysql
   # -h是登录的数据库自治,-u表示登录mysql的用户名,-p表示对应的用户名密码
   # 密码可以明文接在-p后面,也可以回车之后提示输入密码
   mysql [-h127.0.0.1] -uroot -p
   ```



## 配置文件

> mysqld --help --verbose|grep -A 1 'Default options':查看mysql读取配置文件的顺序,不同系统顺序不一样

* set global config_option=xxx:设置全局参数,对所有连接都有效,但是有些已经登录了的连接无效,需要重新登录之后才有效
* set [session] config_option=xxx:设置会话参数,只对当前会话有效,不影响其他连接

```mysql
[client] # 客户端配置文件
socket=/app/mysql/data/mysql.sock
port = 3306 # 客户端端口
default-character-set=utf8mb4 # 客户端字符集
[mysqld] # 服务端配置文件
server-id=1 # 服务器唯一标识
basedir=/app/mysql # mysql安装目录
datadir=/app/mysql/data # 数据存放目录
socket=/app/mysql/data/mysql.sock 
port = 3306 # socket访问端口
bind-address=0.0.0.0 # 可访问数据库的ip地址,默认只能是本地访问
character-set-server=utf8mb4 # 服务端字符集
default_storage_engine # 数据库默认引擎类型,默认InnoDB
log-bin=mysql-bin # 默认关闭,生产环境一定要开启.所有对数据库的更新操作都会记录到该文件中
sql-log-bin # 临时不记录binlog
# 0:默认值,表示不主动刷新cache到磁盘,而是由操作系统自己决定
# 大于0:2次写入到磁盘的间隔不超过binlog的多少次写操作,写1最好.若开启主从同步,该配置要开启
sync_binlog # 控制mysql如何向磁盘刷新binlog
# strict_trans_tables:如果给定的事务数据不能插入到事务类型数据库中,则中断操作,在非事务数据库无效
# no_engine_subtitution:若create table所指定的引擎无效,不会使用默认引擎建表
# no_zero_date:不接受日期为0的日期
# no_zero_in_date:在严格模式下,不接受部分日期为0的日期
# only_full_group_by:select的字段必须在group子句中出现
sql_mode # 支持的各种服务器sql模式
# 若是从库也开启binlog日志,做其他从库的主库(级联),或者做备份,还需要下面的参数
# log-slave-updates 该参数无值,只需要写上即可,主库不需要
binlog-ignore-db=mysql,performance-schema # binlog日志中,忽略同步的表,多个用逗号隔开
binlog-do-db = mysql # binlog中只记录某些数据库,多个用逗号隔开
binlog_format = mixed # binlog的类型,row,statement,mixed
expire_logs_days = 15 # 保留binlog日志的天数,一般一个星期
pid-file=/app/mysql/data/mysql.pid # mysql的标识文件
innodb_buffer_pool_size = 3G # innodb数据,索引等缓存,一般配置为内存的1/3到1/2
innodb_additional_mem_pool_size # 定义innodb的数据字典和内部数据结构的缓冲池大小
innodb_file_io_threads = 4 # 文件的io线程数
innodb_thread_concurrency = 8 # 线程并发情况
innodb_flush_method = O_DIRECT # 设置如何跟文件系统交互
innodb_log_buffer_size = 16M # 事务日志缓冲区大小,不需要很大
innodb_log_file_size = 200M # 单个事务日志的大小,即bin.log单个日志大小,根据业务而定
innodb_log_files_in_group = 2 # 控制事务日志文件的个数
# 日志刷新到硬盘上的模式:0,每秒写入一次到缓存,同时写入到磁盘,可能会丢失1秒数据
# 1,默认,每次事务都将事务日志写到缓存,同时写入到磁盘,不会丢失数据,但是太影响性能
# 2,建议,每次提交把事务日志写入到缓存,每秒执行一次将缓存写入到磁盘,除非服务器宕机,可能会丢失1秒数据
innodb_flush_log_at_trx_commit = 2
innodb_file_per_table = 1 # 每个表是否设置一个独立的表空间,最好开启
innodb_max_dirty_pages_pct = 90 # 设置在缓冲池中保存的最大的脏页数量
innodb_lock_wait_timeout = 120
innodb_doublewrite = 1 # 是否开启双写缓存,避免数据损坏
innodb_strict_mode # 定义一个专门为innodb插件提供的服务器sql模式级别
query_cache_size # 设置查询缓存大小
tmp_table_size = 72M # Memory引擎临时文件表的大小
max_heap_table_size # 定义一个Memory引擎表的最大容量,该参数和tmp_table_size最好大小相同
max_connections = 1000 # 最大客户端连接数
max_connect_errors = 6000 # 单次连接最大可能出现的错误数
max_allowed_packet = 32M # 结果集的最大容量
max_length_for_fort_data # 用于排序数据的最大长度,可以影响mysql选择那个排序算法
sysdate_is_now # 确保sysdate()返回日期和now()的结果是一样的
optimizer_switch # 设置mysql优化器中那个高级索引合并功能被开启
init-connect = 'SET NAMES utf8mb4' # 初始化数据库链接时提供的配置信息
wait_timeout = 600 # 锁表超过该时间仍然没有释放锁,mysql将自动释放锁,单位s
interactive_timeout = 600 # 超过多长时间没有写入数据就断开连接,单位s
autocommit = 1 # 自动提交事务,0为手动提交
thread_cache_size=768 # 线程缓存大小
table_cache_size # 缓存表的大小
# 使用的时候才分配,而且是一次性指定,每个表会分配一个,多表同时排序可能会造成内存溢出
sort_buffer_size = 32M # 每个线程排序缓存大小
join_buffer_size = 128M # 每个线程联表缓存大小,一次性指定,每张关联会分配一个,多表时同样可能造成内存溢出
key_buffer_size=32M # 为MyISAM数据库的索引设置缓冲区大小,使用时才真正分配
read_buffer_size = 16M # 每个线程查询MyISAM表时缓存大小,一次性分配指定大小
read_rnd_buffer_size = 32M # 每个线程查询时索引缓存大小,只会分配需要的内存大小
read_only # 不需要值,只要该参数存在于配置文件中,表示当前数据库只读,不能写
lower_case_table_names = 1
skip_name_resolve # 忽略名字解析,DNS查找,不加可能导致权限错误,但是最好禁用
#skip_networking
table_open_cache = 400
read_buffer_size=8M
read_rnd_buffer_size=4M
back_log=1024
#flush_time=0
open_files_limit=65535
table_definition_cache=1400
# 慢查询日志
long_query_time = 10 # 慢查询的超时时间,单位为秒
slow_query_log = 1/on # 开启慢日志,默认不开启
slow_query_log_file = /data/slow.log # 慢查询日志文件地址
log_queries_not_using_indexes = 1 # 记录所有查询到日志中
log_slow_admin_statements = 1
log_slow_slave_statements = 1
log_throttle_queries_not_using_indexes = 10
min_examined_row_limit = 100
secure_file_priv=’’
# 主从开启时从库的设置
replication-do-db # 设定需要复制的数据库,多个用逗号隔开
replication-ignore-db # 设定忽略复制的数据库,多个用逗号隔开
replication-do-table # 设定需要复制的表,多个用逗号隔开
replication-ignore-table # 设定需要忽略复制的表,多个用逗号隔开
replication-wild-do-table # 同replication-do-table功能一样,但可以加通配符
replication-wild-ignore-table # 同replication-ignore-table功能一样,但可以加通配符
slave-skip-errors=1032,1062 # 主从复制时,忽略符合错误码的错误,1032是错误码
[mysqld_safe]
# 错误日志,默认是关闭的
log-error=/app/mysql/logs/mysql-error.log
```



## 字符集

* 在安装时指定服务端和客户端的字符集,一般utf8或utfmb4

* SHOW VARIABLES LIKE '%char%':查看所有字符集编码项

  * character_set_client:客户端向服务器发送数据时使用的编码
  * character_set_connection:连接字符集
  * character_set_database:数据库字符集,配置文件指定或建库建表指定
  * character_set_filesystem:文件系统字符集
  * character_set_results:服务器端将结果返回给客户端所使用的编码
  * character_set_server:服务器字符集,配置文件指定或建库建表指定
  * character_set_system:系统字符集

* SET 变量名 = 变量值:设置字符集编码

  * SET character_set_client = utf8mb4;
  * SET character_set_results = utf8mb4;
  * SET character_set_connection = utf8mb4;

* SET NAMES utf8:设置当前回话所有的字符集

* 安装完成之后,若想修改字符集,可以修改/etc/my.cnf文件的client和mysqld

  ```mysql
  [client]
  # 修改客户端字符集影响客户端,连接和结果字符集
  default-character-set=utf8
  [mysqld]
  # 修改服务器端字符集影响database和server字符集
  default-character-set=utf8 # 适合5.1以前
  character-set-server=utf8 # 适合5.5以后
  ```

* **修改已有数据库乱码数据的唯一办法:将数据导出,之后修改数据库的字符集,再将数据重新导入**,以将数据库远字符集为gbk切换成utf8为例

  1. 导出表结构

  ```mysql
  # -d表示只导出表结构
  mysqldump -uroot -p123456 --default-charater-set=gbk -d dbname > alltable.sql --default-character-set=utf8
  ```

  2. 确保数据库不再更新数据,导出所有数据

  ```mysql
  # --quick:用于转出大的表,强制mysqldump从服务器一次一行的加锁数据而不是检索所有行,并输出前cache到内存中
  # --no-create-info:不创建create table语句
  # --extended-insert:使用包括几个values列表的多行insert语法,这样文件更小,io也小
  # --default-character-set:按照原有字符集导出所有数据,保证数据不乱码
  mysqldump -uroot -p123456 --quick --no-create-info --extended-insert --default-character-set=gbk > alldata.sql
  ```

  7. 打开alldata.sql将set names gbk 修改成 utf8.或者删除该语句,直接将mysql服务端和客户端字符集设置为utf8
  8. 创建数据库,表,导入数据

  ```mysql
  create database dbname default charset utf8;
  mysql -uroot -p123456 dbname < alltables.sql; # 导入表结构
  mysql -uroot -p123456 <alldata.sql; # 导入数据
  ```




## 开启远程访问

1. 登录数据库

2. **创建用户用来远程连接mysql**

   ```shell
   # mysql8及以前版本赋权,同时创建新用户
   # *.*表示所有数据库的所有表,%表示所有的ip都可以连接,newuser是新用户名,newpwd是新用户的密码
   GRANT ALL PRIVILEGES ON *.* TO 'newuser'@'%' IDENTIFIED BY 'newpwd' WITH GRANT OPTION;
   # mysql8以后的用户创建和赋权分开了,不能用以前的方式,必须先创建用户,之后赋权
   CREATE USER 'newuser'@'%' IDENTIFIED WITH MYSQL_NATIVE_PASSWORD BY 'newpwd';
   # mysql8赋所有权,没有privileges
   GRANT ALL ON *.* TO 'newuser'@'%';
   # 若只赋部分权,部分数据库,部分表
   GRANT SELECT,INSERT ON db1.table1 TO 'newuser'@'%'
   # 刷新权限
   FLUSH PRIVILEGES;
   # 查看授权信息
   SHOW GRANT FOR 'newuser'@'%'
   ```

3. 创建用户的时候加上远程加密方式,该方式只对单个帐号有效,不影响其他帐号

4. 查询数据库的用户

   ```mysql
   SELECT DISTINCT CONCAT('User: ''',user,'''@''',host,''';') AS query FROM mysql.user;
   ```



# Windows安装

* 下载压缩包到自定义目录,解压之后得到目录如:E:\mysql-8.0.24
* 进入mysql目录新建data和my.ini文件,data为mysql的数据目录,my.ini为配置文件,内容如下

```ini
[mysqld]
# 设置3306端口
port=3306
# 设置mysql的安装目录
basedir=E:\\mysql-5.7.22-winx64
# 设置mysql数据库的数据的存放目录
datadir=E:\\mysql-5.7.22-winx64\\data
# 允许最大连接数
max_connections=200
# 允许连接失败的次数。这是为了防止有人从该主机试图攻击数据库系统
max_connect_errors=10
# 服务端使用的字符集默认为UTF8
character-set-server=utf8mb4
# 创建新表时将使用的默认存储引擎
default-storage-engine=INNODB
[mysql]
# 设置mysql客户端默认字符集
default-character-set=utf8mb4
[client]
# 设置mysql客户端连接服务端时默认使用的端口
port=3306
default-character-set=utf8mb4
```

* 配置环境变量:MYSQL_HOME=E:\mysql-8.0.24,加入Path中:%MYSQL_HOME%\bin

* 进入E:\mysql-8.0.24\bin,执行以下命令进行数据库初始化

```mysql
mysqld --initialize --user=mysql --console
```

* 初始化时会将root密码输出到控制台中,登录时需要使用
* 将服务添加到windows启动任务中:mysqld -install
* 启动:net start mysql
* 登录数据库,使用刚才的密码
* 修改密码:ALTER USER root@localhost IDENTIFIED BY '123456'; 



# 数据库引擎

> 主要介绍MyISAM和InnoDB,还有其他类型,用的不多.mysql5.5之前默认是MyISAM,之后是InnoDB

* show engines:查看所有引擎类型
* show variables like '%storage_engine%':查看所有表所用引擎类型



## MyISAM

### 概述

* 不支持外键,不支持事务
* 只能表锁,即使操作一条数据时也会锁住整个表,不适合高并发
* 读写互相阻塞:写入的时候肯定是都阻塞的,读的时候也阻塞写,但不阻塞另外的读
* 只缓存索引,不缓存真实数据
* 读取速度较快,占用资源较少
* 不支持外键约束,但支持全文索引
* 表空间小



### 适用场景

* 适用于不需要事务的生产场景,一般是读数据比较多的应用
* 并发相对较低的业务
* 数据一致性要求不是很高的业务



### 优化

* 设置合适的索引
* 调整速写优先级,根据实际需求确保重要操作更优先执行
* 启用延迟插入改善大批量写入性能,降低写入频率,尽可能多条数据一次性写入
* 尽量顺序操作让insert数据都写入到末尾,减少阻塞
* 分解大的时间长的操作,降低单个操作的阻塞时间
* 降低并发数,减少对Mysql的访问,某些高并发场景通过应用进行排队机制
* 对于相对静态(更改不频繁)的数据库数据,充分利用query cache或redis缓存服务提高访问效率
* MyISAM的count只有在全表扫描到时候效率比较高,带有其他条件的的count都需要进行实际的数据访问



## InnoDB

###  概述

* 支持外键,支持事务,支持多版本读
* 行锁,操作时只锁某一行,不对其他行有影响,适合高并发,通过索引实现.但是全表扫描时仍然会升级成表锁,同时需要注意间隙锁的影响
* 读写阻塞与事务的隔离级别相关
* 不仅缓存索引还缓存真实数据,对内存要求较高,而且内存大小对性能有决定性的影响
* 支持分区和表空间
* 整个表和主键以cluster方式存储,组成一颗平衡树
* 所有secondary index都会保存主键信息



### 适用场景

* 需要事务支持的业务,具有较好的事务特性
* 行级锁定对高并发有很好的适应能力,但需要确保查询是通过索引完成
* 数据读写以及更新比较频繁的场景
* 数据一致性高的业务



### 优化

* 主键尽可能小,避免给secondary index带来过大的空间负担
* 避免全表扫描,会使用表锁
* 尽可能缓存所有的索引和数据,提高响应速度,减少磁盘IO消耗
* 在大批量小插入的时候,尽量自己控制事务而不要使用自动提交
* 合理设置innodb_flush_log_at_trx_commit(日志刷到磁盘上)参数,不要过度追求安全性
* 避免主键更新,因为这会带来大量的数据移动



# 权限

## 用户信息

1. 用户信息表:mysql.user
2. 刷新权限:FLUSH PRIVILEGES;

```mysql
CREATE mysql.USER username IDENTIFIED BY password;
# 重命名用户
RENAME mysql.user old_username TO new_username;
# 设置密码
SET PASSWORD = PASSWORD('密码'); # 为当前用户设置密码
SET PASSWORD FOR username = PASSWORD('密码'); # 为指定用户设置密码
# 删除用户
DROP mysql.USER username;
```

1. 必须拥有mysql的全局CREATE USER权限,或拥有INSERT权限
2. 只能创建用户,不能赋予权限
3. 用户名和密码都需要加上引号
4. 要在纯文本中指定密码,需忽略PASSWORD关键词.要把密码指定为由PASSWORD()函数返回的混编值,需包含关键字PASSWORD



## 权限操作

### 赋权

GRANT 权限列表 ON dbname.tablename TO 'username'@'ip';

```mysql
# mysql8以前的版本用下面的语句在创建用户的同时可以赋权,但是8以后的不行
GRANT ALL PRIVILEGES ON *.* TO 'username'@'%' IDENTIFIED BY 'password';
# 8以后的创建用户和赋权分开了,只能先创建用户,之后再赋权
GRANT ALL ON *.* TO 'username'@'%';
# 查询权限
SHOW GRANTS FOR username;
```

1. 权限列表:查看5.2.5权限列表
2. dbname.tablename:某个数据库的某个表,也可以是\*.\*,表示所有数据库的所有表
3. ip:允许用户用那个ip连接,%表示所有ip都可以



### 撤销权限

1. 单个权限:REVOKE 权限列表 ON tablename FROM username;
2. 所有权限:REVOKE ALL PRIVILEGES GRANT OPTION FROM username;
3. 撤销权限的时候最好是和赋权时候一致,给的什么权限就删除是什么权限



### 权限层级

1. 要使用GRANT或REVOKE,您必须拥有GRANT OPTION权限,并且您必须用于您正在授予或撤销的权限

2. 全局层级:全局权限适用于一个给定服务器中的所有数据库,mysql.user

   > GRANT ALL ON *.*|REVOKE ALL ON *.*:授予或撤销全局权限

3. 数据库层级:数据库权限适用于一个给定数据库中的所有目标,mysql.db, mysql.host

   > GRANT ALL ON db_name.*|REVOKE ALL ON db_name.*:授予和撤销某个数据库权限

4. 表层级:表权限适用于一个给定表中的所有列,mysql.talbes_priv

   > GRANT ALL ON db_name.tbl_name|REVOKE ALL ON db_name.tbl_name:授予和撤销表权限

5. 列层级:列权限适用于一个给定表中的单一列,mysql.columns_priv,当使用REVOKE时,必须指定与被授权列相同的列



### 权限列表

* ALL [PRIVILEGES]:设置除GRANT OPTION之外的所有简单权限
* ALTER:允许使用ALTER TABLE
* ALTER ROUTINE:更改或取消已存储的子程序
* CREATE:允许使用CREATE TABLE
* CREATE ROUTINE:创建已存储的子程序
* CREATE TEMPORARY TABLES:允许使用CREATE TEMPORARY TABLE
* CREATE USER:允许使用CREATE USER,DROP USER,RENAME USER和REVOKE ALL PRIVILEGES
* CREATE VIEW:允许使用CREATE VIEW
* DELETE:允许使用DELETE
* DROP:允许使用DROP TABLE
* EXECUTE:允许用户运行已存储的子程序
* FILE:允许使用SELECT...INTO OUTFILE和LOAD DATA INFILE
* INDEX:允许使用CREATE INDEX和DROP INDEX
* INSERT:允许使用INSERT
* LOCK TABLES:允许对您拥有SELECT权限的表使用LOCK TABLES
* PROCESS:允许使用SHOW FULL PROCESSLIST
* REFERENCES:未被实施
* RELOAD:允许使用FLUSH
* REPLICATION CLIENT:允许用户询问从属服务器或主服务器的地址
* REPLICATION SLAVE:用于复制型从属服务器(从主服务器中读取二进制日志事件)
* SELECT:允许使用SELECT
* SHOW DATABASES:显示所有数据库
* SHOW VIEW:允许使用SHOW CREATE VIEW
* SHUTDOWN:允许使用mysqladmin shutdown
* SUPER:允许使用CHANGE MASTER,KILL,PURGE MASTER LOGS和SET GLOBAL语句,mysqladmin debug命令;允许您连接(一次),即使已达到max_connections
* UPDATE:允许使用UPDATE
* USAGE:“无权限”的同义词
* GRANT OPTION:允许授予权限

# 语法

## 基础语法

### 特殊语法

* mysql中使用了关键字,需要用``(反引号)包裹起来

* mysql  -uroot -p123456 -e "show databases;":-e参数可以直接在非mysql环境中执行sql语句

* system ls /:在mysql中使用system加上linux命令,可以查看linux系统的文件

* mysqlbinlog:可以查看bin-log日志里的内容

* 命令行内的语句结束符可以为;(分号),此时输出为横排结果.若末尾是\G,不是分号,则是竖排结果

* delimiter:修改当前对话的语句结束符

* SQL对大小写不敏感

* +:mysql中+号只能用来做运算,拼接字符串要用concat

  * +两边是数字,直接做运算

  * 两边的不是数字,先尝试转成数字,该处有2种情况

  * 若A以字符串开头,则将整个字符串转换为0进行运算

    ```mysql
    select 'fdfd34'+4; # 4
    ```

  * 若A是以数字开头的字符串,那么将会把从开头到第一个字符换之间的数字转化之后做运算

    ```mysql
    select '234f'+4; # 238
    select '5fdfd34'+4; # 9 
    ```

  * 若一边为null,则结果null

* Mysql中截取函数的下标从1开始,并不是从0开始,含头不含尾

* LOCK TABLES table:锁定表

* UNLOCK TABLES:解锁所有表



### 系统语法

* SHOW DATABASES:查看所有的数据库
* USE dbname:使用名称为dbname数据库
* SHOW TABLES FOR dbname:直接查看dbname数据库中的所有表
* SHOW TABLES:必须先使用use dbname之后才可以使用,查看当前数据库中所有表
* SHOW TABLE STATUS:查看表状态
* SHOW CREATE TABLE tablename:查看表结构,信息更详细
* DESC tablename/DESCRIBE tablename/EXPLAIN tablename/SHOW COLUMNS FROM tablename:查看表结构,分析表结构,展示表字段
* RENAME TABLE old_tablename TO new_tablename:修改表名
* RENAME TABLE old_tablename TO dbname.tablename:将表移动到另外一个库并重新命名
* ALTER TABLE table []:修改表结构

  * ADD COLUMN col property []:新增字段col,以及其他属性,如类型,长度等
    * AFTER col1:新增在某个已有的字段后
    * FIRST:新增为第一个字段

  * ADD PRIMARY KEY(col):将col字段设置为主键
  * DROP PRIMARY KEY:删除主键,需要先删除AUTO_INCREMENT属性
  * ADD UNIQUE [indexname]  (col):将某字段设置为唯一索引,可自定义索引名
  * ADD INDEX [indexname]  (col):创建普通索引,可自定义索引名
  * DROP INDEX indexname:删除索引
  * DROP COLUMN col:删除字段
  * MODIFY COLUMN col property:修改字段属性,不可修改字段名,所有原有属性也都必须写上
  * CHANGE COLUMN old_col new_col property:同modify,但可修改字段名
  * ADD CONSTRAINT foreignname FOREIGN KEY(col) REFERENCES table1(col1):将table的col字段外键关联到table1的col1字段,table1为主表
  * DROP FOREIGN KEY foreignname:删除外键
* TRUNCATE TABLE table:直接删除表之后再重新建表结构,数据不可恢复
* CREATE TABLE table LIKE table1:复制表table的表结构到table1
* CREATE TABLE table AS SELECT * FROM table1:复制tablename1的表结构和数据到table



## 常用函数

### 通用函数

* SELECT database(),now(),user(),version():查看mysql当前数据库,时间,登录用户,版本号
* IFNULL(exp1,exp2):若exp1表达式为null,则使用exp2的值,若不为null,则使用exp1的值
* MD5(str):对字符串加密,只用在新增mysql用户的时候
* LOAD_FILE(file_name):从文件读取内容
* GROUP_CONCAT():



### 字符串函数

* CONCAT(str1,str2...):直接将字符串拼接

* CONCAT_WS(sperate,str1,str2...):将字符串按照指定的分隔符sperate拼接

* LENGTH(str):str的**字节**长度,需要根据编码来测算

* CHAR_LENGTH(str):str的字符个数

* UPPER(str):将str字符串全部转换为大写

* UCASE(str):将str全部转换成大写

* LOWER(str):将str字符串全部转换为小写

* LCASE(str):将str全部转换成小写

* SUBSTR/SUBSTRING(str, position [,length]):从str的position开始截取length个字符串,若length不存在,截取到末尾.当position为正数,从开始向末尾读position个下标.若position为负数,从末尾开始向开头读取position,同样是向末尾截取.**注意,mysql中字符串下标从1开始,含头不含尾**

  ```mysql
  substr('sfewrew',5); # rew
  substr('sioioplb,-5); # ioplb
  ```

* LEFT(str,length):从str开头向末尾截取length个字符.若str长度不够,返回str.若length为负数,返回空字符串

* RIGHT(str,length):从str末尾起开头截取length个字符.若str长度不够,返回str.若length为负数,返回空字符串

* INSTR(str,str1):从str中查找str1的第一次出现的下标,找不到返回0

  ```mysql
  substr('sfewrew','ew'); # 3
  ```

* LOCATE(str, str1[,start_position]):同instr,但可指定开始位置

* TRIM([str FROM] str1):去除str1两边的空白字符串,中间的不去除.str表示去除指定字符串

* LTRIM(str):去除前端空白字符串

* RTRIM(str):去除后端空白字符串

* REPEAT(str, count):将str重复count次拼接起来

* LPAD(str, length, pad):从str开头截取length个字符串.若str长度不够,则左边补充pad,直到长度达到length

* RPAD(str, length, pad):从str开头截取length个字符串.若str长度不够,则右边补充pad,直到长度达到length

* REPLACE(str ,search_str ,replace_str):在str中用replace_str替换所有的search_str

* CHARSET(str):返回字串字符集



### 数学函数

* ABS(x):返回x的绝对值

* FORMAT(x, d):格式化千分位数值

  ```mysql
  select format(1234567.456, 2) # 1,234,567.46
  ```

* CEIL(x):向上取整,返回大于等于x的最小整数

  ```mysql
  select ceil(10.1); # 11
  select ceil(-10.1); # -10
  ```

* FLOOR(x):向下取整,返回小于等于x的最大整数

* ROUND(x[,precise]):四舍五入,默认取整.precise,保留几位小数

* TRUNCATE(x, d):x截取d位小数,并不进行四舍五入.若小数位不够,返回x

* MOD(m, n):等同于m%n, m mod n,求余.10%3=1

* PI():返回6位小数的圆周率

* POW(m, n):返回m的n次方

* SQRT(x):算术平方根

* RAND():随机数



### 时间函数

```mysql
# %Y:4位年
# %y:2位年
# %m:2位月
# %c:1位或2位月
# %d:2位月中日
# %H:24小时制
# %h:12小时制
# %i:2位分钟
# %s:2位秒
```

* NOW(), CURRENT_TIMESTAMP():当前日期时间

* CURDATE()/CURRENT_DATE():返回当前日期

* CURTIME()/CURRENT_TIME():返回当前时间

* DATE('yyyy-mm-dd hh:ii:ss'):获取日期部分

* TIME('yyyy-mm-dd hh:ii:ss'):获取时间部分

* DATE_FORMAT(date/datestr,format):将日期或时间字符串格式化为指定格式字符串

  ```mysql
  select date_format(now(),'%y-%c-%d'); # 20-1-01
  select date_format('2020-01-01','%y-%c-%d'); # 20-1-01
  select date_format('20-01-01','%y-%c-%d'); # 20-1-01
  select date_format('20-01-01','%Y-%c-%d'); # 2020-1-01
  select date_format('2020-01-01','%Y-%m-%d'); # 2020-01-01
  ```

* STR_TO_DATE(datestr,format):将日期格式的字符串转换为指定格式的日期,年月日必须一起,时分秒必须一起.在转换时,不要用%y,%c,可能会出现预期之外的结果.尽量使用date_format

  ```mysql
  select str_to_date('2020-01-01','%Y-%m-%d'); # 2020-01-01
  select str_to_date('2020-01-01','%y-%m-%d'); # NULL,不知道是什么逻辑
  select str_to_date('20-01-01','%y-%m-%d'); # 2020-01-01,不知道是什么逻辑
  select str_to_date('20-01-01','%y-%c-%d'); # 2020-01-01,不知道是什么逻辑
  select str_to_date('2020-1-01','%Y-%c-%d'); # 2020-01-01,不知道是什么逻辑,%c无效
  select str_to_date('2020-01-01','%Y-%c-%d'); # 2020-01-01,不知道是什么逻辑,%c无效
  select str_to_date('2020-1-01','%y-%c-%d'); # NULL,不知道是什么逻辑,%y,%c无效
  select str_to_date('2020-01-01','%y-%c-%d'); # NULL,不知道是什么逻辑,%y,%c无效
  ```

* UNIX_TIMESTAMP():获得unix时间戳

* FROM_UNIXTIME():从时间戳获得时间

* YEAR/MONTH/DAY(NOW()/'yyyy-mm-dd hh:ii:ss'):获得指定时间的4位年部分,1或2位月,1或2位月中天

* MONTHNAME(NOW()):获得指定时间的月份英文



### 流程控制函数

#### IF

```mysql
# 第一种形式,只有2种结果
IF(cnd,res1,res2) # 当cnd条件的结果为true,返回结果res1,false返回res2
# 第二种形式,有多种结果
IF cnd1 THEN 
	res1
ELSEIF cnd2 THEN
	res2
ELSE
	res3
END IF
```



#### CASE WHEN

```mysql
# 第一种形式,该方式适用于等值比较
CASE column # column可以是字段,也可以某个表达式
WHEN cnd1 THEN res1 # cnd1是column的可能值或者是某种判断,res1是满足cnd1之后返回的值
[WHEN cnd2 THEN res] # 另外的分支结果,可以有多个,cnd2是另外的值或判断,满足cnd2则返回res2
[ELSE res3] # 默认值,可有可无
END
# 第二种形式,该方式既适用于等值比较,也适用于范围值比较,如<,>
CASE WHTN cnd1 THEN res1 # cnd1直接是一个条件,若满足该条件,则返回res1
[WHEN cnd2 THEN res2] # 满足cnd2则返回res2
[ELSE res3] # 返回默认值
END
```



#### WHILE

```mysql
[label:]WHILE cnd1 DO # label可有无,主要是用来跳出循环时使用.cnd1为循环的条件
	sql;	# 需要执行的sql
END WHILE[label]
```

* 如果需要在循环内提前终止while循环,则需要使用标签,标签需要成对出现
* 退出循环,通过退出的标签决定退出哪个循环
  * leave label:退出整个循环
  * iterate label:退出当前循环



#### REPEAT

```mysql
[label:]REPEAT  # label可有无,主要是用来跳出循环时使用
	sql;
UNTIL cnd1 # cnd1为结束循环的条件
END REPEAT[label]
```

* 如果需要在循环内提前终止REPEAT循环,则需要使用标签,标签需要成对出现
* 退出循环,通过退出的标签决定退出哪个循环
  * leave label:退出整个循环
  * iterate label:退出当前循环



## 自定义变量

* 自定义变量只在当前会话有效,只能在定义了之后使用
* SET/SELECT @var_name=value:声明一个变量并给变量赋值
* SELECT column INTO @var_name from ...:利用sql语句给变量赋值,sql只能返回一个值,但是可以同时给多个变量赋值
* SELECT @var_name:查看自定义变量的值



## 局部变量

* BEGIN...END:语句块,可以在其中写多条语句,一般用于逻辑比较复杂的存储过程等
* DECLARE @var_name[,...] type [default]:声明局部变量,初始化值可以是常数或表达式,不赋值则为null.
* 局部变量必须先声明,之后才可以用自定义变量赋值的方式进行赋值
* 局部变量只能在begin...end中使用,且必须是第一句话



## 触发器

* TRIGGER:触发器,主要是监听表中记录的增删改
* CREATE TRIGGER triggername triggertime triggerevent ON tablename FOR EACH ROW trigger_sql
  * triggertime:触发程序的时间,可以是before或after,以指明触发程序是在激活它的语句之前或之后触发
  * triggerevent:指明激活触发程序的语句类型,可以是INSERT,DELETE,UPDATE
  * tablename:必须是永久表,不可以是临时表
  * trigger_sql:激活触发程序之后执行的语句,多个语句可以用begin...end
* DROP TRIGGER triggername:删除触发器



## 方法

* CREATE FUNCTION func_name(参数列表) RETURNS 返回值类型 BEGIN ...sql逻辑... END:创建一个方法,必须有且仅有一个返回值

* 参数列表:变量名称 类型(长度),多个用逗号隔开

* SELECT func_name(参数列表):调用方法

  ```mysql
  CREATE FUNCTION func_name(userId int) RETURNS VARCHAR 
  BEGIN
  	DECLARE @result VARCHAR(50);
  	SELECT username into @result from ts_user where id=userId;
  	RETURN @result;
  END
  SELECT func_name(1);
  ```

* DROP FUNCTION [IF EXISTS] function_name:删除方法

* SHOW FUNCTION STATUS LIKE 'partten'/SHOW CREATE FUNCTION function_name:显示方法

* ALTER FUNCTION function_name 函数选项:修改方法



## 存储过程

* CREATE PROCEDURE sp_name (参数列表) BEGIN ...sql逻辑... END:创建存储过程

  * 参数列表:不同于函数的参数列表,需要指明参数类型
    * IN:表示输入型,需要调用的时候传入
    * OUT:表示输出型,将过程体处理完的结果返回到客户端.存储过程体中不需要加return语句
    * INOUT:表示混合型,输入和输出都可以

* CALL sp_name(参数列表):调用存储过程.只能单独调用,不可夹杂在其他语句中

  ```mysql
  CREATE PROCEDURE sp_name(IN userId INT,OUT username VARCHAR(50))
  BEGIN
  	SELECT username into username from ts_user where id=userId;
  END
  CALL sp_name(1,@username); # @username是定义的一个变量
  ```

* DROP PROCEDURE sp_name:删除存储过程

* SHOW CREATE PROCEDURE sp_name:查看存储过程的定义



# 备份还原

## 备份

```mysql
# 直接输入用户名和密码进行备份,username是登录的用户名,password是登录的密码,dbname是数据库名
# 最后的sql文件可以是路径,若不是路径直接保存到当前目录
mysqldump -uusername -ppassword []> sql_bak_dbname.sql
```

* --databases dbname1 dbname2...:指定备份多个数据库数据和表结构
* -A:备份所有数据库数据和结构
* -A -d:备份所有数据库表结构
* -A -t:备份所有数据库数据
* -A  -B  --events:将所有数据库都一次备份完成
* -A  -B  -F  --events:将所有数据库都一次备份完成,并且对bin-log进行分割,产生新的bin-log日志,而以前的数据就直接从即将备份的文件中取,以后增量数据从产生的新的bin-log日志中读.需要将bin-log先打开
* -B dbname1 dbname2...:备份的sql中添加创建数据库和使用数据库的语句,可同时备份多个库
* -B dbname|gzip:在备份的sql中添加创建数据库和使用使用数据库的语句,并压缩文件
* dbname tablename1 tablename2...:备份数据库中的指定表
* -d dbname:只备份数据库中所有表的结构
* -t dbname:只备份数据库中所有表的数据
* --master-data=1:在备份时直接定位到当前bin-log的终点位置,恢复的时候可以直接从提示的位置恢复.需要结合bin_log相关命令完成全量备份,见7.1

```mysql
# 会在备份的文件开头添加此时备份的数据到那个文件,在该文件的那个位置
CHANGE MASTER TO MASTER_LOG_FILE='mysql-bin.000016',MASTER_LOG_POS=17;
```

* --default-character-set=utf8:备份时设置导出数据的字符集

* --compact:去除备份的sql中的注释部分,调试的时候才可以用

* -x或--lock-all-tables:锁所有数据库的所有表,不能进行更新

* -l或--lock-tables:锁指定数据库的所有表为只读

* --single-transaction:适合innodb事务数据库备份,用来保证事务的一致性.本质上设置本次的会话隔离级别为REPEATABLE READ

* --triggers:备份触发器

* --routines:备份存储过程

* **专业的DBA备份**

  ```mysql
  mysqldump -uroot -p123456 --all-databases --flush-privileges --single-transactioin --master-data=1 --flush-logs --triggers --routines --events --hex-blob > #BACKUP_DIR/full_dump_$BACKUP_TIMESTAMP.SQL
  ```
  
* rsync备份:rsync -avz /data/mysql-bin.0* rsync_backup@127.0.0.1::backup -password-file=/etc/rsync.password



## 还原

```shell
# 非压缩sql文件,在不登录mysql时直接恢复
mysql -uusername -ppassword dbname < sql_bak_dbname.sql
# 登录mysql之后恢复,需要写完整的sql路径
source /bak/mysql/sql_bak_dbname.sql 
# 压缩文件需要先解压之后恢复
gizp -d sql_bak_dbname.sql.gz # 之后再用上面的方法恢复
```

* 只有一个主库是否需要做增量回复

  * 应该做定时全量备份(一天一次)以及增量备份(每个10分钟左右对bin)log日志做切割然后备份到其他服务器上,或者本地其他的硬盘里)或者写到网络文件系统(备份服务器)
  * 如果不允许数据丢失,最好的办法是做从库,通过drbd(基于磁盘块的)同步

* 还原时能锁库锁表的尽量锁库锁表,避免在恢复时用户又写入数据

* 如果实在不能锁库锁表,可以立即刷新bin_log:登录mysql,执行命令flush-logs.此时会立刻生成一个新的bin_log日志文件,新的bin_log是新的更新数据,可以不用管,只需要恢复刷新之前的数据即可

* 假设现在恢复的bin_log是15,刷新出来的bin_log是16,需要从15的bin_log中利用-d参数筛选出错误行为的数据库内容到sql中

  ```mysql
  mysqlbinlog -d dbname mysql-bin.000015 >dbname.sql;
  ```

* 假设是删除了某个数据,此时需要从新生成的dbname.sql中找到该语句,并从dbname.sql中删除该语句,之后保存

* 若是直接将修改后的dbname.sql恢复到数据库中,仍然会存在一个问题:因为15的log中已经有了dbname.sql的操作,而直接导入dbname.sql之后,16的bin_log会再次记录dbname.sql中的操作

* 此时最好的办法是停库.然后进行恢复.无法人为的避免这种情况

* 大公司的做法是:多slave的情况下,让某一个slave延迟复制bin_log日志,延迟多少时间根据实际情况控制.若出现上述情况,直接将主库的bin_log复制到从库上进行恢复,进行主从切换,能保证数据的不丢失

* 另外的解决方案:主从情况下,停止一个从库,将主库的bin_log刷新,把mysql-bin.000015恢复成dbname.sql,删除出错的语句,然后把停止的从库进行全量恢复,加上dbname.sql恢复.恢复完成之后,再将停止的从库切成主库,开始提供服务,之后再将原主库的mysql-bin.000016恢复成sql,恢复到新主库上,原主库可以之后重新作为新主库的从库提供服务



## 导出表数据

```mysql
select * into outfile 文件地址 from tablename;
```



## 导入数据

```mysql
load data [local] infile 文件地址 into table tablename;
```



## 定时备份

* 创建备份脚本目录:mkdir  -p  /bak/tasks,新建mysql备份目录mkdir  -p  /bak/mysql
* 将执行备份的mysql语句写到脚本中,利用定时任务执行脚本

```shell
cd /bak/tasks
vi mysql_bak.sh
#!/bin/sh
# DATE=$(date +%Y%m%d) # 加上时间后缀,每天只会有一个备份
mysqldump -u username -p password [--lock-all-tables] [--default-character-set=utf8mb4] dbname | gzip > /bak/mysql/sql_bak_dbname_`date +%Y%m%d`.sql.gz
# 编写完之后给脚本加上执行权限
chmod +x mysql_bak.sh
```

* 编写定时任务进行定时备份

```shell
crontab -l # 查看正在执行的定时任务
crontab -e # 新增定时任务,进入vi模式
* */3 * * * root sh /bak/tasks/mysql_bak.sh # 每3小时执行一次任务,同名文件会自动覆盖
systemctl restart crontab # 重启定时任务
```




# 表检查

## 检查表错误

```mysql
CHECK TABLE tablename1,tablename2... [option]
# option = {QUICK | FAST | MEDIUM | EXTENDED | CHANGED}
```



## 优化表,整理数据文件碎片

```mysql
OPTIMIZE [LOCAL|NO_WRITE_TO_BINLOG] TABLE tablename1,tablename2...
```



## 修复表

```mysql
REPAIR [LOCAL|NO_WRITE_TO_BINLOG] TABLE tablename1,tablename2... [QUICK,EXTENDED,USE_FRM]
```



## 分析表和存储表的关键字分布

```mysql
ANALYZE [LOCAL|NO_WRITE_TO_BINLOG] TABLE tablename1,tablename2
```



## 分析sql语句

```mysql
EXPLAIN select ... # explain后接sql语句
```



# 优化



## 硬件优化



* 数据库主机的IO性能是需要最优先考虑的因素,适当增大IO数
* CPU处理能力需求更大
* 网络设备性能要好
* 打开tcp连接数的限制,打开文件数的限制,安全性的限制





## EXPLAIN

* explain可以分析sql的表读取顺序,数据读取操作的操作类型,那些索引可以使用,那些索引被实际使用,表之间的引用,每张表有多少行被优化器查询

* explain sql:直接在sql前面加上explain即可

* explain内容:id,select_type,table,type,possible_keys,key,key_len,ref,rows,extra

  

### ID

* select查询的序列号,包含一组数字,表示查询中执行select子句或操作表的顺序

* id相同:table加载的顺序由上而下
* id不同:如果是子查询,id的序号会递增,id越大优先级越高,越先被执行.一般是先执行子句
* id不同和相同同时存在:先加载数字大的,数字相同的顺序执行.若table行内出现衍生表(derived+id),衍生表后的数字是id的值,表示该验证表是由那一个id衍生而来
* id为null,表示这是一个结果集,不需要进行查询

```mysql
# id相同,此时id是相同的,都为1,而表加载顺序是t1->t3->t2
explain select * from t1,t2,t3 where t1.id =t2.id and t2.id = t3.id;
# id不同,此时id递增,1,2,3,而表加载顺序是t3->t1->t2
explain select * from t2 where id=(select id from t1 where id = (select t3.id from t3 where t3.name=''));
# id相同和不同同时存在,数字大的先加载,相同的顺序执行,加载顺序为t3->t2
explan select * from (select t3.id from t3 where t3.name='') s1,t2 where s1.id=t2.id
```



### Select_type

* 查询类型,在版本8和版本5中可能出现的情况不一样

* simple:简单select查询,查询中不包含子查询或union.有连接查询时,外层查询为simple,且只有一个
* primary:查询中若包含任何子查询或union,最外层查询将会认为是primary,且只有一个
* subquery:除了from子句中包含的子查询外,其他地方的子查询都可能是subquery
* dependent subquery:表示当前subquery的查询结果会受到外部表查询的影响
* derived:在from列表中包含的子查询被标记为DERIVED(衍生),mysql会递归执行这些子查询,把结果放在临时表中
* union:union连接的2个查询.除第一个查询是derived之外,之后的表都是union;若union包含在from子句的子查询中,外层select将被标记为derived
* union result:在union表获取结果的select,不需要参与查询,所以id为null
* dependent union:与union一样,出现在union中,但是会受到外部查询的影响
* materialized:物化通过将子查询结果作为一个临时表来加快查询速度,正常来说是常驻内存,下次查询会再次引用临时表.通常情况下是作为子查询的大表第一次被查询之后,结果将会被存储在内存中,下次再试用该大表查询时就能直接从内存中读取



### Table

* 显示查询表名,也可能是表别名.如果不涉及数据操作,就显示null
* <subqueryN>:表示这个是子查询.N就是执行计划的id,表示结果来自于该子查询id
* <derivedN>:表示这个是临时表.N就是执行计划中的id,表示结果来自于该查询id
* <union M,N>:与derived类型,表示结果来自于union查询的id为M,N的结果集



### Prititions

* 查询涉及到的分区



### Type

* 表示查询的数据使用了何种类型,从好到坏:system>const>eq_ref>ref>range>index>all
* 一般来说,得保证查询至少达到range级别,最好能达到ref

* system:表只有一行记录(等于系统表),这是const类型的特例,一般不会出现,可以忽略.而且只能用于myisam和memory表,如果是innodb表,通常显示all或index

* const:表示通过索引或主键一次就找到了,const用于比较主键或者unique索引.因为只匹配一行数据,所以很快.如将主键置于where子句中,mysql就能将该查询转换为一个常量

* eq_ref:主键和唯一索引扫描.出现在要连接多个表的查询中,驱动表循环获取数据,这行数据的连接条件是第二个表的主键或唯一索引,作为条件查询只返回一条数据,且必须是not null.唯一索引和主键是多列时,只有所有列都用作比较时才会出现eq_ref

* ref:非唯一性索引扫描,返回匹配某个单独值的所有行,属于查找和扫描的混合体,但本质上也是一种索引访问.和eq_ref不同,ref不要求连接顺序,也不一定需要唯一索引和主键,只要使用等值查找或多列主键,唯一索引中,使用第一个列之外的列作为等值查找也会出现

* fulltext:全文索引检索,优先级很高.若全文索引和普通索引同时存在时,优先使用全文索引.只能在创建了全文索引(fulltext)的表中,才可以使用match和against函数

* ref_or_null:与ref类似,只是增加了null值的比较,实际用的不多

* unique_subquery:用户where中的in子查询,子查询返回不重复唯一值

* index_subquery:用户in子查询使用了辅助索引或in常数列表,子查询可能返回重复值

* range:只检索给定范围的行,使用一个索引来选择行,key列显示使用了那个索引

  * 一般是where语句中出现了between,<,>,in等查询时会出现range
  * 这种范围索引扫描比全表扫描要好,因为它只需要开始于索引的某一点,而结束于另一点,不用扫描全部索引
  * range会让复合索引在排序时失效:当range类型用于复合索引的中间时,即使where子句中的字段和顺序都符合复合索引,同时排序,仍然用不上索引

  ```mysql
  # col1,col2,col3为复合索引
  # 此时虽然用上了索引,但是在排序时因为col2为range模式,使得排序的col3使用了using filesort
  explain select * from t1 where t1.col1=1 and t1.col2>1 order by t1.col3;
  # 解决办法:将col2从索引中剔除或直接建立col1和col3的独立索引
  ```

* index:扫描索引,比all快一点,因为索引文件通常比较小

* all:扫描全表数据,效率最低



### Possible_keys

* 显示可能应用在这张表中的索引,一个或多个
* 查询涉及到的字段上若存在索引,则该索引将被列出,但不一定被查询使用
* 和key一起判断是否使用了索引,索引是否失效.多个索引竞争下,到底用了那一个索引



### Key

* 实际使用的索引,null表示没有使用.若查询中使用了覆盖索引,则该索引仅出现在key列表中



### Key_len

* 表示索引中使用的字节数,可通过该列计算查询中使用的索引长度
* 在不损失精确性的情况下,长度越短越好
* key_len显示的值为索引字段的最大可能长度,并非实际使用长度
* 多列索引时,索引可能不会全部使用,需要手动计算使用了那些索引
* 只会计算where条件中使用的索引,排序分组使用的索引不会计算进去



### Ref

* 显示索引使用了那些列或常量被用于查找索引列上的值
  * const:表示使用的是一个常量
  * db.table.column:表示使用的是某个数据库的某张表的某个字段
  * null:没有使用索引
* 如果连接查询时,被驱动表的执行计划显示的是驱动表的关联字段
* 如果是条件使用了表达式,函数或条件列发生了内部隐式转换,可能显示为func



### Rows

* 根据表统计信息以及索引选用情况,大致估算出找到所需记录要读取的行数



### Extra

* 包含不适合在其他列显示但十分重要的额外信息

* Using filesort:该类型需要避免出现.文件内排序,MySQL中无法利用索引完成的排序操作称为文件排序,相当于排序字段并非索引字段.此时MySQL会对数据使用外部索引排序,而不是按照表内的索引顺序进行读取行.当索引字段为复合索引时,where里使用了索引字段,且是按照复合索引的顺序使用,那么排序所使用的字段若不符合复合索引的顺序,也将不使用索引

  ```mysql
  # col1,col2,col3为复合索引
  # col3将无法使用索引进行排序,此时会出现using filesort的内排序
  select * from t1 where col1='' order by col3;
  # 此时仍然使用的是索引排序,而不会出现using filesort,性能更好
  select * from t1 where col1='' order by col2,col3;
  # 此时虽然复合索引中间出现了其他的字段,但仍然会使用索引排序,而不会出现using filesort
  select * from t1 where col1='' and col4='' order by col2,col3;
  ```

* Using temporary:该类型需要避免出现.新建了一个内部临时表,保存中间结果.mysql对结果进行排序时使用临时表.该种情况多出现于复合索引的时候使用group by和order by

  ```mysql
  # col1,col2为复合索引
  # col2将无法使用索引进行排序,此时会出现using temporary,using filesort
  explain select * from t1 where col1 in('','') group by col2;
  # 此时仍然使用的是索引排序,而不会出现using temporary,using filesort,性能更好
  explain select * from t1 where col1 in('','') group by col1,col2;
  ```

* Using index:表示相应的select操作中使用了覆盖索引(Covering Index),避免访问了表的数据,效率还行

  ```mysql
  # col1,col2为复合索引
  # 此时会用到using where和using index
  explain select col2 from t1 where col1 ='';
  # 只用到了using index
  explain select col1,col2 from t1;
  ```

  * 如果同时出现using where,表明索引被用来执行索引键值的查找
  * 如果没有同时出现using where,表明索引用来读取数据而非执行查找动作.

* Using where:用到了where条件,但未使用索引

* Using where Using index:查询的列被索引覆盖,并且where筛选条件是索引列之一但是不是索引的前导列,意味着不能直接通过索引查找符合条件的数据.多出现于复合索引中,被查询字段非复合索引的第一个字段,而是其他字段

* Using index condition:与Using where类似,查询的列不完全被索引覆盖,where条件中是一个前导列的索引,即查询的字段中包含了非索引中的字段

* Using join buffer:表示使用了连接缓存,可以调整join buffer来调优

* impossible where:where子句的值总是false,不能用来获取任何元组

* select tables optimized away:在没有group by子句的情况下,基于索引优化MIN/MAX操作或者对于MyISAM存储引擎优化count(*)操作,补习等到执行阶段再进行计算,查询执行计划生成的的阶段即完成优化

* distinct:优化distinct操作,在找到第一行匹配的元组后即停止找同样值的动作

* no tables used:不带from子句的查询或from dual查询

* null:查询的列未被索引覆盖,并且where筛选条件是索引的前导列,意味着用到了索引

* Using intersect:表示使用and连接各个索引条件时,从处理结果获取交集

* Using union:表示使用or连接各个使用索引的条件时,从处理结果获得并集,只有一个or

* Using sort_union,Using sort_intersect:用and和or查询信息量大时,先查询主键,然后排序合并后返回结果集



### Filtered

* 表示存储引擎返回的数据在srever层过来后,剩下多少满足查询的记录数量的比例
* 是百分比,不是具体记录数



## SQL优化



### SQL执行顺序

```mysql
select distinct(t1.c) c,sum(t1.c) num from t1 inner join t2 on t1.a=t2.a where t1.a = t2.a group by t1.c having num > 0 order by t1.a limit 10 # 一个sql例子
```

* from:先从from语句开始读,多表会形成笛卡尔积
* on:主表数据保留
  * inner join:2张表都有的数据才会保留到下一步
  * left join:2张表中left左边表的数据都会保留,右边表没的数据以null代替
  * right join:2张表中right右边表的数据都会保留,左边表没有的数据以null代替
* where:非聚合,非select别名的条件筛选
* group by:分组
* having:对分组后的数据进行再次筛选
* select:查询出所需要的字段.如果进行了聚合,那么只能查询进行聚合的字段
* order by:排序
* limit:分页



### 优化

* 查询不要使用select *,查询字段也是开销,使用具体的字段代替
* 如exist和in,in和not in,like,索引的使用,关联太多join.可使用explain sql来分析
* 不要在where子句中使用!=,<>,is null,is not null这种条件将跳过索引,直接使用全表扫描
* 不要在where中使用or,会导致索引失效
* 尽量去除表连接操作
* 尽可能在索引中完成排序
* 小结果集驱动大结果集
* 试用mysqldumpshow查看慢日志,优化慢SQL
* 利用第三方工具pt-query-digest查找慢SQL



## 索引优化

### 概述

* 索引也是一张表,该表保存了主键与索引字段,并指向实体表的记录,所以索引是要占空间的

* 索引大大提高了查询速度,但是会降低增删改的速度,因为要同时对表和索引都做操作

* 查询数据在总数据的7%-15%之间加索引效果最好.若查询数据超过20%,相当于全表扫描

* 字段多适用数值,字段长度多使用定长

* 冷热隔离,即使用频率高的字段放一张表,使用频率低的放另外表,如实时和历史数据

* 索引在新增数据时也会消耗大量资源,一个表中的索引最多不要超过6个

* 索引一般也很大,不可能全部都存在内存中,因为索引往往都以索引文件的形式存储在磁盘上

* where子句中多个索引同时只能用一个

* 索引字段的值必须和数据库定义类型相同,若是数据库自动进行类型转换,那么索引将不生效

  ```mysql
  # col1=varchar,col2=varchar,col1和col2为复合索引
  explain select * from test where col1='1' order by col2; # 索引生效
  explain select * from test where col1=1 order by col2; # 索引不生效
  ```

  



### 索引类型

* 聚簇索引:是物理索引,数据表就是按顺序存储的,物理上是连续的
  * 如果表中定义了主键,MySQL将使用主键作为聚簇索引
  * 如果表中不指定主键,MySQL将第一个组成列的not null的唯一索引作为聚簇索引
  * 如果表中没有主键且没有适合的唯一索引,MySQL将自动创建一个名为GEN_CLUST_INDEX的隐藏聚簇索引
* 一旦创建了聚簇索引,表中的所有列都根据构造聚簇索引的关键列来存储
* 因为聚簇索引是按该列的排序存储的,因此一个表只能有一个聚簇索引
* MySQL中每个InnoDB表都需要一个聚簇索引,该聚簇索引可以帮助表优化增删改查操作
* 所有不是聚簇索引的索引都叫非聚簇索引或者辅助索引
* 在InnDB存储引擎中,每个辅助索引的每条记录都包含主键,也包含非聚簇索引指定的列.MySQL使用辅助索引的主键值来检索聚簇索引,因此应该尽可能将主键缩短,否则辅助索引占用空间会更大



### 辅助索引

* 单值索引:即一个索引只包含单个列,一个表可以有多单列索引

* 唯一索引:索引列的值必须唯一,但允许有空值

* 复合索引:一个索引包含多个列

* 覆盖索引:该索引类型并不是一个实际存在的,只是一个特殊情况.当select查询的字段是单索引字段或与复合索引的字段的个数,顺序相同时,即使是全表扫描的情况下,仍然会使用索引

* 基本语法:

  ```mysql
  # 创建主键/唯一/全文/普通索引
  CREATE [PRIMARY KEY]/[UNIQUE]/[FULLTEXT]/[INDEX] indexname ON tablename(columnname(length));
  # 新增索引
  ALTER TABLE tablename ADD [PRIMARY KEY]/[UNIQUE]/[FULLTEXT]/[INDEX] indexname[ON](columnname(length));
  # 删除表中所有/单个索引
  DROP INDEX [indexname] ON tablename
  # 查看索引,\G表示竖排显示
  SHOW INDEX FROM tablename\G
  ```

  

### 回表

* 假如普通索引INDEX1为非唯一索引,要查询age=10的数据,需要先在INDEX1索引查找age=10
* 根据age=10的辅助索引上得到主键索引的值为30
* 然后再到主键索引树查找值为30对应的记录R
* 然后INDEX1索引树继续向右查找,发现下一个是age=5不满足条件(非唯一索引后面有可能有相等的值,因此向右查找到第一个不等于3的地方),停止搜索
* 整个过程从INDEX1索引树到主键索引树的过程叫做回表



### 索引结构

* hash:key-value,检索效率远高于B+tree,可以一次定位
* fulltext:目前仅char,varchar,text3种类型可以
* B+tree:二叉树的变种,每一个结点有多个数据,可以进行指定.子节点可以是多个,当该节点存放的数据个数超过指定的数据个数,就分裂出另外的同层子节点.当子节点超过一定数量时,向下分裂子节点
* B-Tree

![](B-tree.png)

* B+Tree

![](B+tree.png)





### 适合创建索引

* 主键自动创建索引,外键应当有索引
* 频繁用来查询的字段创建索引
* 和其他表关联的字段创建索引
* 查询中用来排序的字段可用来创建索引
* 用来分组和统计的字段可创建索引
* 数据量超过 300 的表应该有索引
* 索引应该建在选择性高的字段上
* 索引应该建在小字段上,对于大的文本字段甚至超长字段,不要建索引
* 复合索引的建立需要进行仔细分析,尽量考虑用单字段索引代替
* 复合索引的几个字段经常同时以AND方式出现在WHERE子句中,单字段查询极少甚至没有,则可以建立复合索引,否则考虑单字段索引
* 如果复合索引中包含的字段经常单独出现在WHERE子句中,则分解为多个单字段索引
* 如果复合索引所包含的字段超过 3 个,那么仔细考虑其必要性,考虑减少复合的字段
* 如果既有单字段索引,又有这几个字段上的复合索引,一般可以删除复合索引
* 频繁进行数据操作的表,不要建立太多的索引
* 删除无用的索引,避免对执行计划造成负面影响
* 过多的复合索引,在有单字段索引的情况下,一般都是没有存在价值的.相反,还会降低数据增加删除时的性能,特别是对频繁更新的表来说,负面影响更大
* 尽量不要对数据库中某个含有大量重复的值的字段建立索引



### 不适合创建索引

* 频繁更新的字段不适合创建索引
* where条件用不到的字段不创建索引
* 数据库列有多个重复值的,不适合创建索引
* 数据量比较小的表不适合建立索引
* 数据太少的字段不创建索引



### 优化

* 复合索引:只要where和order by条件中的字段和索引定义的一样,且复合索引的第一个字段在where子句中,不管是第几个条件,索引就会生效

  * 不用所有的索引字段都作为条件
  * 顺序可以不相同
  * 中间可以添加其他非索引字段
  * **最优的方案应该是复合索引中间不跳过任何字段,按照索引顺序来.虽然上述3种情况仍然会使用索引,但是会减少精度(key_len)**
  * 存储引擎不能使用索引中范围条件右边的列
  * order by和group by使用索引排序的条件差不多,有一个不一样,group by可以接having
  * 索引中只要有一个列含有null,那么这一列也不会使用索引

  ```mysql
  # e为普通字段,索引顺序为a,b,c,d
  explain select * from ts_user where a='' and b='' and c='' and d='';
  explain select * from ts_user where b='' and a='' and c='' and d=''; # 同上
  # 会用到索引,但是key_len会下降,其实是索引查找中只用到了a和b,但排序用到了索引的c,d
  explain select * from ts_user where a='' and b='' order by c, d;
  explain select * from ts_user where a='' and b='' and e='' order by c, d;
  # 会用到索引,但是key_len同样会下降,只用到了abc的key_len
  explain select * from ts_user where a='' and b='' and c='';
  # 会用到索引,但是key_len只是用到a的key_len
  explain select * from ts_user where a='' and c='' and d='';
  # ab会用到查找索引,但是d用不到,c作为排序也可以用到索引
  explain select * from ts_user where a='' and b='' and d='' order by c;
  # ab会用到查找索引,但是d既用不到索引查找,也用不到索引排序,此时排序用的是using filesort
  explain select * from ts_user where a='' and b='' order by d;
  # ab会用到查找索引,cd用的索引排序
  explain select * from ts_user where a='' and b='' order by c,d;
  # ab会用到查找索引,cd既用不到索引查找,也用不到索引排序,此时排序用的是using filesort
  explain select * from ts_user where a='' and b='' order by d,c;
  # ab会用到查找索引,此时排序用的不是using filesort,因为c已经是一个常量了,cd是索引排序
  explain select * from ts_user where a='' and b='' and c='' order by d,c;
  # 索引不生效
  explain select * from ts_user where b='' and c='' and d='';
  explain select * from ts_user where b='' and c='' order by a;
  # 查找索引生效,但只用了ab,精度也只有ab精度,b是范围查找,c没有用上,故而排序索引也不生效
  explain select * from ts_user where a='' and b>1 order by c;
  # abc索引全部都用到了,虽然是范围查找
  explain select * from ts_user where a='' and b like '1%' and c='';
  ```

* 应该尽量避免更新复合索引,因为复合索引的顺序就是表记录的物理顺序,变更将会消耗大量资源

* like:当%name%和%name时,索引不生效.只有当name%时索引才会生效

  ```mysql
  # 通常状态下都是必须使用%name%,此时有一种解决方式,使用覆盖索引
  # 将name当作select的字段查询出来,不管是什么类型索引,都不能有索引外字段,否则索引将不生效
  # name为单字段索引
  explain select name from t1 where name like '%aaa%'; # 索引生效
  explain select * from t1 where name like '%aaa%'; # 索引不生效
  explain select name,age from t1 where name like '%aaa%'; # 索引不生效
  # name,age为复合索引
  explain select name,age from t1 where name like '%aaa%'; # 索引生效
  # 若仍然需要额外的字段,可以将只有like查询的字段作为另外的表进行连接查询
  explain explain select b.name,b.age,b.pwd from (select col1 from t1 where name like '%1%') a LEFT JOIN t1 b on b.name = a.name; 
  ```

* !=,<>,is null,is not null:在where子句中使用时将不使用索引

* <,<=,>,>=,between:在where子句中将会使用索引

* or:在where子句中or不会使用索引,可以使用union或union all代替or

* 小表驱动大表,适当的时候用exists代替in是可以提高效率,特别是子查询

  * in:当in后面接的是一个子查询时,先查询子查询中的数据,然后再查询外部数据
  * exists:正好和in相反,先查询外部数据,放到子查询中做条件验证,true则保留

  ```mysql
  # 当B表的数据小于A表的数据时,in优于exists
  select * from t1 where t1.a in (select a from B);
  # 当A表的数据小于B表的数据时,exists优于in
  select * from t1 where exists(select 1 from B.a=t1.a);
  ```

* 索引列上不要使用函数,计算,类型转换,表达式,这将会使用全表扫描.可以对常量进行表达式操作

  ```mysql
  select num from ts_user where num/2=100; # 不使用所用
  select num from ts_user where num = 100*2; # 使用索引
  select num from ts_user where lower(num)='str'; # 不使用索引
  select num from ts_user where num='100'; # 类型会自动转换,将不使用索引
  ```

* 尽量避免频繁创建和删除临时表,可以适当作为存储过程来使用临时表数据或建立视图

* 在新建临时表时,若一次性插入数据量很大,可以使用select into 代替create table,避免造成大量log

* 如果存储过程中使用到临时表,应在最后将临时表显示删除

* 尽量避免使用游标,因为游标的效率较差

* 在所有的存储过程和触发器开始处设置set nocount on,在结束时设置set nocount off,无需在执行存储过程和触发器的每个语句后向客户端发送done_in_proc消息

* 应尽量少的连表查询,因为连表查询的数据量更大,且很容易造成锁表和阻塞

* where中有多索引时,选择key_len最短的使用

* MySQL存储引擎不能继续使用索引中范围条件(between,<,>,in等)右边的列

* 多条联合查询时,需要根据情况加索引

  * 保证sql语句中被驱动表上的join条件字段已经被索引
  * 左右连接:非主表的连接字段上加索引能提高查询效率,不管是2表还是多表都是如此.**注意,该种情况只能对单字段索引有效,若是多字段索引**

  ```mysql
  # left join,此时应该在t2表col1加索引
  select * from t1 left join t2 on t1.col1=t2.col1;
  # right join,此时应该在t1表col1加索引
  select * from t1 right join t2 on t1.col1=t2.col1;
  ```




## 排序优化

* order by子句,尽量使用index方式排序,避免使用filesort方式排序

* 尽可能在索引列上完成排序操作,详见索引优化的复合索引优化

* order by子句中只要是索引的前导列都可以使索引生效,可以直接在索引中排序

* 复合索引在where子句中的某些情况,可能会导致索引排序失效

  ```mysql
  -- id,name是复合索引
  -- 排序走索引
  select * from user where id=1 order by name;
  -- 排序不走索引
  select * from user where id > 1 order by name;
  -- 发生上述情况是因为=相当于已经进行分组了,而>则没有分组,无法使用前导列排序
  ```

* 如果不在索引列上,filesort有多种算法

  * 单路排序:4.1之后版本,从磁盘读取查询需要的所有列,按照order by列在buffer对它们进行排序,然后扫描排序后的列表进行输出,效率更快一些.并且将随机IO变成了顺序IO,但是会消耗更多的空间,因为它把每一行都保存在内存中了.**注意,该排序模式下,需要对mysql配置文件中的sort_buffer_size配置进行酌情修改,因为排序放在了sort_buffer_size中,若是超出了sort_buffer_size就需要多次读取,反而不如双路排序**
    * 增大sort_buffer_size参数的设置
    * 增大max_length_for_sort_data参数的设置
  * 双路排序:4.1版本之前使用,分别读2次,一次是数据行,一次读排序列.在磁盘取排序字段,在buffer进行排序,再从磁盘取其他字段
  * 常规排序:
    * 从表中获取满足where条件的记录
    * 将每条记录的主键+排序键取出放入sort buffer
    * 如果sort buffer可以存放所有满足条件的对(主键+排序键),则进行排序;否则sort buffer满后,进行排序并固化到临时文件中
    * 若排序中产生了临时文件,需要利用归并排序算法,保证临时文件中记录是有序的
    * 循环执行上述过程,直到所有满足条件的记录全部参与排序
    * 扫描排好序的对,并利用id去获取select需要返回的其他列
    * 将获得的结果返回给用户
  * 优先队列排序算法:主要是针对order by limit M,N语句,在空间层面做了优化.这种排序采用堆排序实现,堆排序算法特征正好可以解决limit M,N排序的问题,虽然仍然需要所有元素参与排序,但是只需要M+N个元组的sort buffer空间即可
  
  ```mysql
  # 索引顺序为a,b
  # 使用索引排序
  explain select * from ts_user where a='' order by a;
  explain select * from ts_user where a='' order by a,b;
  explain select * from ts_user where a='' order by b;
  # 不使用索引排序,使用的是filesort
  explain select * from ts_user order by b;
  explain select * from ts_user where a='' order by b,a;
  explain select * from ts_user where a='' order by a desc,b asc;
  ```



## 联表优化



* 小结果集驱动大结果集.联表查询相当于多重循环,应该以小表驱动大表,减少循环次数
* 当进行多表连接查询时,驱动表的定义:
  *  指定了联接条件时,满足查询条件的记录行数少的表为驱动表
  * 未指定联接条件时,行数少的表为驱动表
* 如果搞不清楚该让谁做驱动表,谁Join谁,则让MySQL自行判断,即不显示的使用left,right,inner
* MySQL表关联查询的算法是Nest Loop Join,是通过驱动表的结果集作为循环基础数据,然后一条一条地通过该结果集中的数据作为过滤条件到下一个表中查询数据,然后合并结果
  * Simple Nested-Loop:简单循环.就是简单的嵌套循环,不使用索引,Join Buffer等
  * Index Nested-Loop:索引循环.即先从索引中获取值,之后再和内层表进行循环
  * Block Nested-Loop:块循环.利用Join Buffer缓冲区进行多条数据同时循环的策略,在内存足够的情况下,可尽量增大Join Buffer的值

```mysql
-- user表10000条数据,class表20条数据
select * from user u left join class c u.userid=c.userid;
-- 这样则需要用user表循环10000次才能查询出来,而如果用class表驱动user表则只需要循环20次
select * from class c left join user u c.userid=u.userid
```

* 根据驱动表的字段排序.对驱动表可以直接排序,对非驱动表(的字段排序)需要对循环查询的合并结果(临时表)进行排序,产生临时表会浪费性能
* 尽可能避免复杂的join和子查询
* 最终驱动表是哪张表,可以通过EXPLAIN的id查看
* 当联表查询有条件,且不指定驱动表时,默认会使用无索引的表为驱动表

```mysql
-- user表10000条数据,class表20条数据,user表的id无索引,class表userid有索引
-- 此时用class表驱动
select * from user t1 join class t2;
-- 此时t1表驱动,因为条件中t1表无索引,会被MySQL优化成驱动表
select * from user t1 join class t2 on t1.userid = t2.userid;
```



## 表优化

* 尽量遵循三范式原则,但在必要的情况下,可以适当做数据的冗余,反三范式
* 不常用的字段单独存在在一个表中
* 大字段独立存在到一个表中
* 经常一起使用的字段放到一起
* 如果表数据量超过百万级别就要考虑分表



## 其他优化

* 当单表数据超过700W(根据数据库不同而不同)时,sql优化已达到极致,此时应该增加缓存,读写分离,分库分表

* 尽量避免全表扫描,避免多表连接,首先应考虑在where以及order by设计的列上建立索引

* 尽量避免在where子句中对null进行判断,否则将导致引擎放弃使用索引而进行全表扫描

* 尽量设置默认值以避免null值出现

* 尽量使用数字型字段,尽量使用定长的字符串类型

* 尽量进行批量操作,而不是频繁的读写

* 分页limit优化,偏移量越大,执行越慢.即limit M,N中M越大,执行越慢.可以使用索引先将一部分数据过来,之后再分页

  ```mysql
  -- 效率低
  select * from t1 limit 100000,10;
  -- 效率高,id上有索引
  select * from t1 where id in (select id from t1 where id > 100000) limit 100000,10;
  ```

* show profiles:是mysql提供可用来分析当前会话中语句执行的资源消耗情况.默认情况下,参数处于关闭状态,并保存最近15次运行的结果
  * show profiles:查看最近记录运行的sql语句,其中包括查询id,查询消耗时间以及查询语句
  * show profiles [] for query 查询id:查看该id的sql语句在执行中的全过程
    * cpu:显示cpu相关信息
    * block io:显示io相关开销
    * all:显示所有信息
    * context switches:上下文切换相关开销
    * ipc:显示发送和接收相关开销
    * memory:显示内存相关开销
    * page faults:显示页面错误相关开销
    * source:显示和source_function,source_file,source_line相关开销
    * swaps:显示交换次数相关开销
  * converting heap to myisam:查询结果太大,内存不够用
  * creating tmp table:创建临时表,拷贝数据到临时表,用完再删除
  * copying to tmp table on disk:把内存中临时表复制到磁盘,危险
  * locked:锁表了
  * 出现上述4种结果,说明查询有很大问题,需要优化
  
* 为每个表建立独立表空间,使用系统表空间会出现大量浪费空间的问题
  * show variables like '%innodb_file_per_table%':查看是否使用了独立表空间,ON表示使用.每个表都会有一个单独的表空间名称->tablename.idb,该文件会生成在数据目录的数据库目录下,每个表一个
  * optimize table:收缩系统文件.可以收缩独立表空间和系统表空间,但是收缩系统表空间需要很复杂的操作,也很浪费时间.但是收缩独立表空间就很简单,而且可以不停服
  * 将表空间从系统表空间转到独立表空间
    * 使用mysqldump导出所有的数据库表数据
    * 停止mysq服务,修改参数,并删除Innodb相关文件
    * 重启mysql服务,重建Innodb独立空间
    * 重新导入数据



## 分区

> 分区是将表分解成多个区块进行操作和保存,从而降低每次操作的数据,提高性能.但是对于应用程序来说,仍然只是一个表,但是在物理上这个表可能是由多个物理分区组成的,每一个分区都是一个独立的对象,可以进行独立处理.分区仍然是在同一个数据库中进行处理,只是看不到,由mysql自行完成相关处理,对应用程序的增删改差没有任何改变



### 作用

* 进行逻辑数据分割,分割数据能够有多个不同的物理文件路径
* 可以存储更多的数据,突破系统单个文件最大限制
* 提升性能,提高每个分区的读写速度,提高分区范围查询的速度
* 可以通过删除相关分区来快速删除数据
* 通过跨多个磁盘来分散数据查询,从而提高IO性能
* 涉及到例如sum()之类的聚合函数查询,可以很容易的进行并行处理
* 可以备份和恢复独立的分区,这对大数据量很有好处



### 类型

* range:基于属于一个给定连续区间的列值,把多行分配个分区,常用
* list:类似于按range分区,list是列值匹配一个离散值集合中的某个值来进行选择
* hash:基于用户定义的表达式的返回值来进行选择的分区,该表达式使用将要插入到表中的这些行的列值进行计算,这个函数必须产生非负整数.hash比较消耗性能
* key:类似于hash分区,由mysql服务器提供其自身的hash函数



### 特性

* 若表中存在主键或unique时,分区的列必须是主键或者unique的一个组成部分
* 若不存在主键或唯一列,可指定任意一列作为分区列
* 5.5版本前的range,list,hash分区要求分区键必须是int,5.5以后支持非整数的range和list
* 分区的最大数目不能超过1024,一般建议对单表的分区不超过150
* 如果含有唯一索引或者主键,则分区列必须包含在所有的唯一索引或主键内
* 不支持外键
* 不支持全文索引,对分区表的分区键创建索引,那么该索引也将被分区
* 按日期进行分区很合适,因为很多日期函数可以用,但是对于字符串来说合适的分区不多
* 只有range和list可以子分区,hash和key不能子分区
* 临时表不能被分区
* 分区表对单条数据的查询没有优势
* 要注意选择分区的成本,每插入一行数据都需要按照表达式筛选插入的分区
* 分区字段尽量不要为null



### 创建分区

```mysql
CREATE TABLE...
# 创建range分区,假设分区中表的主键为int或bigint,column为分区字段
PARTITION BY RANGE(column){
	# pnum1为分区名称,less than表示小于多少,此处表示column小于5的放在pnum1分区
	PARTITION pnum1 VALUES LESS THAN (5),
	# column大于等于5,小于10的放在pnum2分区
	PARTITION pnum2 VALUES LESS THAN (10),
	PARTITION pnum3 VALUES LESS THAN MAXVALUE
}
CREATE TABLE...
# 创建list分区,假设分区中表的主键为int或bigint,column为分区字段
PARTITION BY LIST(column){
	# pnum1为分区名称,存储的值必须是已知晓,不存在该分区列表中的值无法插入
	PARTITION pnum1 VALUES IN (1,3,5),
	# column大于等于5,小于10的放在pnum2分区
	PARTITION pnum2 VALUES IN (2,4,6)
}
CREATE TABLE...
# 创建list分区,假设分区中表的主键为int或bigint,column为分区字段
PARTITION BY HASH(column){
	# num表示分多少个区
	PARTITION num
}
```



### 查看分区

* 查看/usr/bin/myqsl_ocnfig里的ldata值

* 通过sql语句查看

  ```mysql
  # 查看所有分区,没有分区时只会有一组数据
  select * from information_schema.partitions where table_schema='dbname' and table_name ='tablename'\G
  # 单独查询某个分区,pnum1为分区名
  select * from tablename partition(pnum1);
  ```

* 查询数据在那个分区

  ```mysql
  explain partitions select * from tablename where user_id=1;
  ```



### 子分区

```mysql
# 创建range分区,假设分区中表的主键为int或bigint,column为分区字段
CREATE TABLE...
PARTITION BY RANGE(YEAR(createtime)){
	SUBPARTITION BY HASH(TO_DAYS(createtime))
	SUBPARTITION 2
	(
        PARTITION pnum1 VALUES LESS THAN (2009),
        PARTITION pnum2 VALUES LESS THAN (2010),
        PARTITION pnum3 VALUES LESS THAN MAXVALUE
    )
}
```



### 操作

* ALTER TABLE tablename ADD PARTITION(PARTITION pnum1 values less than(50)):添加分区

  * 对于range分区,只可以添加新的分区到分区列表的高端
  * 对于list分区,不能添加已经包含在现有分区列表中的任意值

* ALTER TABLE tablename DROP PARTITION pnum1:删除分区,同时也会删除分区中的数据

* ALTER TABLE tablename REORGANIZE PARTITION pnum1 INTO (partition_definitions):将已经有的分区拆成新的分区,这样拆的分区不会有数据的损失.同样也可以将多分区合成一个分区,数据不会丢失

  ```mysql
  # 原分区为p1(5),p2(10),p3(100),将p3拆掉
  ALTER TABLE tablename REORGANIZE PARTITION pnum3 INTO(
  	PARTITION pnum3_1 VALUES LESS THAN (50),
  	PARTITION pnum3_2 VALUES LESS THAN (100)
  );
  # 分区合并
  ALTER TABLE tablename REORGANIZE PARTITION pnum3_1, pnum3_2 INTO(
  	PARTITION pnum3 VALUES LESS THAN (100)
  );
  ```

* ALTER TABLE tablename REMOVE PARTITIONING:删除所有分区,但是保留数据

* ALTER TABLE tablename REBUILD PARTITION pnum2,pnum3:重建分区,类似于磁盘碎片整理,数据不会丢失,但是可能会重新分配

* ALTER TABLE tablename OPTMIZE PARTITION pnum2,pnum3:优化分区,实际上是对没有使用空间的回收

* ALTER TABLE tablename ANALYZE PARTITION pnum2,pnum3:分析分区

* ALTER TABLE tablename CHECK PARTITION pnum2,pnum3:检查分区中的数据或索引是否被破坏

* ALTER TABLE tablename REPAIR PARTITION pnum2,pnum3:修补分区



## 分库分表

> 数据库的复制能解决访问问题,并不能解决高并发的写问题,解决该问题可以分库分表



### 作用

* 解决磁盘系统最大文件限制
* 减少增量数据写入时的锁对查询的影响,减少长时间查询造成的表锁,影响写入操作等锁竞争情况,节省排队的时间开支,增加吞吐量
* 由于表数据量下降,常见的查询操作由于减少了需要扫描的记录,是的全文检索等查询检索行数变少,减少磁盘IO



### 分库

* 又叫垂直切分,就是把原本存储于一个库的表拆分到多个库上,通常是将表按照功能模块,关系密切程度划分
* 分库实现简单,便于维护,但是不利于频繁跨库操作,单表数据大的为不好解决



### 分表

* 又叫水平切分,是按照一定的业务规则或逻辑,将一个表的数据拆分成多份,分别存储于多个表结构一样的表中,这多个表可以存在1到多个库中,分表友分为垂直分表和水平分表
* 垂直分表:将本来可以在同一表中的内容,分为划分为多个表,如切字段
* 水平分表:把一个表复制成同样表结构的不同表,然后把数据按照一定的规则划分,分别存储到这些表中,从而保证单表的容量不会太大,提升性能,如历史表
* 分表能解决分库的不足,但是缺点是实现起来比较复杂,特别是分表的规则划分,程序的编写,以及后期的维护迁移
* 分区也是单表的水品分,除非数据太多,达到亿级以上时才考虑分表



### 分表实现

* 根据业务功能指定,根据sql解析等
* 分别在数据源和表上执行功能
* 如果涉及到返回结果集的话,就需要做结果集的合并,并按照需要进行2次处理,人排序,分页等
* 若涉及到事务,就要考虑分布式事务,或者实现两阶段事务提交,或者补偿性业务处理

* 分布式全局唯一性id:可使用redis集群或zk集群解决
* 分布式事务,扩库事务:尽量减少类似事务.可使用TCC,atomikos等
* 多数据库sql改写,表链接问题
* 根据某个字段去查询其他数据时,是否需要查多个库,多个表
* 数据异构问题



## 硬件优化

* CPU:选择64位系统,更高的频率,更多个核心数

* 内存:选择足够大的内存,但是如果超出了数据库中数据的大小,也无法起到更快的读写效果

* 磁盘性能:PCI-E>ssd(固态硬盘)>sas>sata,raid:raid0>raid10>raid5>raid1

* 网卡:多网卡bind,以及buffer,tcp优化

  

  

## CentOS系统参数优化

* 内核相关参数配置文件:/etc/sysctl.conf

  * net.core.somaxconn=65535:每个端口最大的监听队列长度.该参数默认值比较小,可以适当增大
  * net.core.netdev_max_backlog=65535:每个网络接口接收数据包的速率比内核接收速率快的时候允许发送到队列包中数据包最大的数量
  * net.ipv4.tcp_max_syn_backlog=65535:还未获得对方连接的请求,可保存在队列中的最大数量,超过的会被抛弃
  * net.ipv4.tcp_fin_timeout=10:控制TCP连接处理等待状态的时间,加快tcp连接的回收速度
  * net.ipv4.tcp_tw_reuse=1:该参数和fin_timeout,recycle参数都是为了加快tcp连接的回收速度
  * net.ipv4.tcp_tw_recycle=1:该参数和fin_timeout,reuse参数都是为了加快tcp连接的回收速度
  * 以下4个参数决定了tcp接收连接和发送缓冲区大小的默认值和最大值
    * net.core.wmem_default=87380
    * net.core.wmem_max=16777216
    * net.core.rmem_default=87380
    * net.core.rmem_max=16777216
  * 减少失效连接占用的TCP系统资源数量
    * net.ipv4.tcp_keepalive_time=120:TCP发送类似心跳检测的时间间隔,单位S
    * net.ipv4.tcp_keepalilve_intvl=30:当未收到心跳时下一次重发心跳检测的时间间隔,单位为S
    * net.ipv4.tcp_keepalive_probes=3:认定TCP失效前重发次数
  * kernel.shmmax=4294967295:定义单个共享内存段的最大值,该参数应该设置的足够大,以便能在一个共享内存段下容纳下整个InnnoDB缓冲池的大小.对于64位系统可取的最大值是物理内存减去1byte,建议值为大于物理内存的一半,一般取值大于InnoDB缓冲池大小即可
  * vm.swappiness=0:当虚拟内存不足时是否需要使用交互分区来存储数据,0为不使用

* 资源限制配置文件:/etc/security/limit.conf

  ```shell
  # 加到该文件的最后即可,加完之后重启系统生效
  * soft nofile 65535
  * hard nofile 65535
  ```

  * \*:表示对所有用户有效
  * soft:指当前系统生效的设置
  * hard:系统中所能设定的最大值
  * nofile:表示所限制的资源是打开文件爱你的最大数目
  * 65535:限制的数量

* 磁盘调度策略配置文件:/sys/block/sda/queue/scheduler

  * cfq:完全公平策略,默认情况下是该模式,查看文件显示的为none
  * noop:电梯式调度策略,是一个FIFO队列,该种策略利于写,对于闪存,RAM以及嵌入式系统比较好
  * deadline:截止时间调度策略,确保在一个截止时间段内服务请求,截止时间是可以调整的,而默认读期限短于写期限.这样就防止了写操作因为不能被读取而饿死的现象,deadline对数据库类应用是最好的选择
  * anticipatory:预料I/O调度策略,本质上和deadline一样,但在最后一次读操作后,要等待6ms,才能继续进行对其他I/O请求进行调度.它会在每个6ms中插入新的I/O操作,而会将一些小写入流合并成一个大写入流,用写入延时换区最大的写入吞吐量.AS适合于写入较多的环境,比如文件爱你服务器,AS对数据库环境表现很差.
  * 直接将三种模式中的一种写入到配置文件中即可修改调度策略



# 分库分表框架

## MyCat



## ShardingSphere

* 是Apache的一套开源的分布式数据中间件解决方案组成的生态圈
* 由Sharing-JDBC,Sharding-Proxy,Sharding-Sidecar(未完成)组成,提供标准化的数据分片,分布式事务和数据库治理功能



### Sharding-JDBC

* 相当于增强版的JDBC驱动,完全兼容各种JBDC个各种ORM框架
* 支持任意实现JDBC规范的数据库,如MySQL,Oracle,SQLServer,PostgreSQL等



### Sharding-Proxy

* 透明化的数据库代理端,提供封装了数据库二进制协议的服务端版本,用于完成异构语言的支持
* 所有对数据库的操作实际上就是连接Proxy



# 日志

## binlog

> 默认是不开启的,是一个顺序读写的日志,记录**所有数据库**增删改,用于主从,数据恢复等

* bin_log的记录会影响数据库的性能,特别是事务条件下
* 有3种模式:Row,Statement,Mixed(Statement为主,特殊条件下切换成row)
  * Row:将每一条sql产生的所有行的变更都记录为一行日志
  * Statement:每条修改的ssql都会记录到master的binlog中
  * sync_binlog=1:每条bin.log都需要记录
* bin_log默认会放在mysql的数据库目录(data)下,以6位数字进行区分,如mysql-bin.000001
* mysqlbinlog [] mysql-bin.000001:只能用mysqlbinlog命令查看bin_log文件,用cat方式会乱码
* mysqlbinlog -d dbname mysql-bin.000001 > test.sql:将bin_log中的dbname数据库数据全部拆出来输出到sql中
* mysqlbinlog mysql-bin.000021 --start-position=30 --stop-position=199 -r pos.sql:从指定的bin_log中拆出从指定位置开始到指定位置结束的日志到sql中.具体的位置点可以直接查看bin_log日志,不能是不存在的位置点,含头不含尾,末尾的点不会放到sql中



## error

> 默认是关闭的,记录严重的警告和错误信息,每次mysqld启动和关闭的详细信息

* Mysql的启动,停止,crash,recovery,错误信息
* --log-warning/log_warnings:记录交互错误和连接中断



## slow

* 记录mysql中响应时间超过阈值的语句,具体指运行时间超过long_query_time值的sql

* 业务日志,慢查询记录,默认不开启,开启会对性能有一定影响
* show variables like '%slow_query_log%':查看是否开启了慢sql以及日志存放地址
* set global slow_query_log=1/on:开启慢sql日志,只对当前数据库生效
* show variables like '%long_query_time%':查看慢日志的默认查询时间,默认10S
* 若要修改相关参数,可以在my.cnf中修改
  * slow_query_log:是否开启慢查询
  * slow_query_log_file:慢查询日志存放地址,注意日志文件的权限
  * long_query_time:慢查询阈值,单位秒,默认10,大于该值的就记录
* mysqldumpslow [] slow_log:分析慢sql日志的命令行工具,slow_log为日志地址
  * --verbose:详细分析
  * -s:按照何种方式排序
  * -c:访问次数
  * -l:锁定时间
  * -r:返回记录
  * -t:查询时间
  * -al:平均锁定时间
  * -ar:平均返回记录数
  * -at:平均查询时间
  * -t:即为返回前面多少条的数据
  * -g:后边搭配一个整个匹配模式,大小写不敏感
* 慢日志格式

```shell
# Time: 2021-10-10T07:18:05.755268Z
# User@Host: root[root] @ localhost []  Id:     8
# Query_time: 12.000224  Lock_time: 0.000000 Rows_sent: 1  Rows_examined:1
use comics;
SET timestamp=1633850273;
select sleep(12);
```

* 第一行:SQL查询时间
* 第二行:执行SQL查询的连接信息,用户和连接IP
* Query_time:SQL执行的时间,越长则越慢
* Lock_time:在MySQL服务器阶段等待表锁时间
* Rows_sent:查询返回的行数
* Rows_examined:查询检查的行数,越大越浪费时间
* 最后一行是执行的SQL语句



## general_log

* 记录客户端连接信息和执行sql语句信息,永远不要在生产环境中开启该功能,严重影响程序

* 配置,在my.cnf中开启

  * general_log=1:开启全局日志

  * general_log_file:记录日志的文件地址

  * log_output=FILE:日志输出格式,以文件(FILE)输出.若是以表(TABLE)输出,可以从用sql查

    ```mysql
    select * from mysql.general_log;
    ```




## tmp目录

* mysql在查询时生成的临时数据文件,性能比较高



## 事务日志

* 



## DoubleWrite数据



# 事务



## 概述

* 高并发环境下,多个线程对同一行数据进行同时更新时可能出现的问题
* ACID:原子性(Atomicity),一致性(Consistent),隔离性(Isolation),持久性(Durable)
* 更新丢失:多个事务同时对同一个数据进行修改,后面的事务会覆盖前面的事务.加锁解决
* 脏读:A事务对数据进行了修改,但是还未进行提交.此时B事务来查询该数据,得到了未提交的新数据,若此时A事务发生异常进行了回滚,那B事务的操作就不符合一致性,数据也是无效的
* 不可重复读:一个事务范围内两个相同的查询却读取到了不同的结果.即在同一个事务中,A事务读取了某个数据,为了校验或其他操作再次读取该数据,发现数据不同,就是不可重复读.这种情况是另外的事务B对该数据进行了更新操作,导致了同一个事务中的2次读取结果不同
* 幻读:当两个完全相同的查询执行时,第二次查询的结果跟第一次查询的结果不同.此种情况发生在2次读中间发生了其他事物对该数据的更新操作
  * 和脏读不同的是事物进行了提交,而不是回退
  * 和不可重复读不同的是,幻读是数据的条数变化,相当于数据的新增或删除更敏感.不可重复读是更新



## 隔离级别

* Read uncommitted:读未提交,级别最低,一个事务可以读取另外一个事务并未提交的数据.可能出现脏读,幻读,不可重复读
* Read Committed:读提交,大部分数据库采用的默认隔离级别.一个事务的更新操作结果只有在该事务提交之后,另外一个事务才可以读取到该数据更新后的结果.可能出现不可重复读和幻读
* Repeatable Read:重复读,mysql的默认级别.整个事务过程中,对同一笔数据的读取结果是相同的,不管其他事务是否在对共享数据进行更新,也不管更新提交与否.**Mysql的高版本的InnoDB已经解决了幻读问题,即该级别已经可以解决赃读,幻读,不可重复读**
* Serializable:序列化,最高隔离级别.所有事务操作依次顺序执行,注意这会导致并发度下降,性能最差.通常会用其他并发级别加上相应的并发锁机制来取代它



## Mysql数据库锁

* show variables like '%tx_isolation%':查看mysql的默认事物隔离级别
* show open tables:查看表上加过的锁,in_use为0表示没加锁
* show status like '%table%':查看数据库表锁情况
  * table_locks_immediate:产生表锁定的次数.立即获取锁的查询次数,每次获取锁值加1
  * table_locks_waited:出现表锁定争用而发生等待的次数(不能立即获取锁的次数,每等待一次锁值加1),此值高则说明存在较严重的表级锁争用情况
* lock table tablename read/write,tablename1 read/write:给表加锁
* unlock tables:将所有的锁解锁
* 从对数据操作的类型(读/写)分
  * 读锁(共享锁):针对同一份数据,多个读操作可以同时进行而不会互相影响
  * 写锁(排他锁):当前写操作没有完成前,会阻断其他写锁和读锁
* 从对数据操作的粒度分
  * 行锁(偏写)
  * 表锁(偏读)



## 表锁

* 偏向MyISAM存储引擎,开销小,加锁快,无死锁,锁定粒度大,发生锁冲突的概率最高,并发低



### 读锁

* 若sesson1对a表设置了读锁,那sesson1就只能读a表,不可更新a表,也不可读写其他没有锁定的表

  sesson2可以读写锁表,也包括a表

  若是sesson2要写a表,此时就是阻塞,直到sesson1解锁

* 读锁会阻塞写,但是不会阻塞读.写锁则会把读写都堵塞
* 读锁只能读加锁了的表,不能读未加锁的表.其他链接可以读所有的表
* 加了读锁不可以对数据进行写操作,若需要写,则需要等解除锁之后才可以



### 写锁

* sesson1给t1表加写锁,则sesson1可以对t1表读写,但不能对其他没有写锁的表进行读写

  sesson2只能读写没有锁的表,读写t1表时都会被阻塞

  等sesson1对t1表的锁释放后才能继续对t1表进行读写



## 行锁

* 偏向InnoDB存储引擎,开销大,加锁慢,会出现死锁,锁定粒度最小,发生锁冲突的概率最低,并发度也高
* InnoDB和MyISAM最大的不同是:支持事务和采用了行锁
* 在行锁情况下,多个事务更新同一行会发生阻塞,只有当前一个事物更新完且commit之后,其他的事物才能继续更新
* 在行锁情况下,多个事务可以对不同的行进行更新,不会发生阻塞
* 读锁,共享锁:select ... lock in share mode,在select默认加lock in share mode即可
* 写锁,排他锁:select .... for update,在select结尾加上for update即可锁定该行,增删改默认加写锁



## 行锁升表锁

* 在行锁的情况下,若更新时索引失效,如where子句中的自动类型转换,将会导致行锁变成表锁,此时整个表都只能有一个更新事务,其他事务都会被阻塞



## 间隙锁

> 当用范围条件而不是相等条件检索数据,并请求共享或排他锁时,InnoDB会给符合条件的已有数据记录的索引项加锁,对于键值在条件范围内但并不存在的记录,叫做间隙.InnoDB会对这个间隙加锁,这种锁机制就是间隙锁.

* 因为查询过程中通过范围查找时,会锁定整个范围内所有的索引键值,即使这个键值并不存在.这样会带来的问题就是,当某些不存在的键值被锁定时,会造成在这些被锁定的键值无法插入数据
* 例如范围更新id为1-9的数据的age,此时id为2的数据不存在,更新之后id为1-9的age都应该为同一个值.若是此时另外一个事务插入了id为2的数据,将会被阻塞,同时在上一个更新完成释放锁之后,插入操作完成,而此时插入的新值可能会和更新后的值不同



## 分析行锁定

* show status like 'innodb_row_lock%':
  * innodb_row_lock_current_waits:当前正在等待锁定的数量
  * innodb_row_lock_time:从系统启动到现在锁定总时间长度,比较重要
  * innodb_row_lock_time_avg:每次等待所花平局时间,比较重要
  * innodb_row_lock_time_max:从系统启动到现在等待最长的一次所花的时间
  * innodb_row_lock_waits:系统启动后到现在总共等待的次数,比较重要



## 优化

* 尽可能让所有数据检索都通过索引来完成,避免无索引行锁升级为表锁
* 合理设计索引,尽量缩小锁的范围
* 尽可能较少检索范围条件,避免间隙锁
* 尽量控制事务大小,减少锁定资源量和时间长度
* 尽可能低级别事务隔离



## Snapshot

生成一个数据请求时间点的一致性数据快照,并用这个快照来提供一定级别的一致性读取(MVCC:Multi Version Concurrency Control)



# 主从



## 原理

1. slave服务器上执行start slave,开启主从复制开关
2. 此时,slave服务器的io线程会通过在master上授权的复制用户权限请求连接master服务器,并请求从指定bin_log日志文件的指定位置(日志文件名和位置就是在配置主从复制服务器时执行的changet master命令指定的)之后发送bin_log日志内容
3. master服务器接收到来自slave服务器的io线程请求后,master服务器上负责复制的io线程根据slave服务器的io线程请求的信息读取指定bin_log日志文件指定位置之后的bin_log日志信息,然后返回给slave端的io线程.返回的信息中除了bin_log日志内容外,还有本次返回日志内容后在master服务器端的新的bin_log文件名称以及在bin_log中的下一个指定更新位置
4. 当slave服务器的io线程获取到来自master服务器上io线程发送的日志内容以及日志文件位置点后,将bin_log日志内容一次写入到slave自身的relaylog(中继日志)文件(mysql-relay-bin.xxxxxx)的最末端,并将新的bin_log文件名和位置记录到master-info文件中,以便下次读取master端新bin_log日志时能够告诉master服务器要从新bin_log的那个文件,那个位置开始请求
5. slave服务器的sql线程会实时的检测本地relaylog中新增加的日志内容,并在吱声slave服务器上按语句的顺序执行应用这些sql语句,应用完毕后清理应用过的日志
6. 由于主从同步是异步执行的,突发情况下仍然会造成数据的丢失
7. 正常的主动同步下,应该主从都开启bin_log,在从库上开启全量和增量方式的备份,可以防止人为对主库的误操作导致数据丢失.确保备份的从库实时和主库是同步状态



## 正常配置

1. 每个slave只有一个master,每个master可以有多个slave

2. mysql主从之间的log复制是异步且串行化的

3. mysql版本一致且后台以服务运行

4. master配置文件/etc/my.cnf的mysqld下添加server-id,这是每个数据库的唯一标识,数字类型,不可重复,主库一般是1

5. mysqld下添加log-bin=xxx-bin,该配置为sql二进制日志的文件名,mysql会将所有执行语句存到xxx-bin中

6. 从库可以开启bin_log也可以不开启bin_log.若是从库需要进行备份时才开bin_log,不需要备份则不开启

7. mysqld下添加log-bin-index=xxx-bin.index,该值为log-bin的值加上index,表示日志文件的索引

8. 重启mysql:service mysqld restart,或者/etc/init.d/mysql stop之后/etc/inti.d/mysql start

9. 从库配置文件中添加server-id

10. mysqld下添加relay-log=slave-relay-bin,开启从库读取主库传到从库的bin-log中数据的线程服务

11. mysqld下read-only=0,表示读写都可以

12. binlog-ignore-db=mysql:设置不要复制的数据库,可选

13. binlog-do-db=需要复制的主数据库名字,设置需要复制的数据库可选

14. mysqld下添加relay-log-index=slave-relay-bin.index,表示当前读取的那一个日志

15. 重启mysql:service mysqld restart

16. 在主库上新建一个专门用来让从库连接的用户

    ```mysql
    # 创建用户
    CREATE  USER  'username'  IDENTIFIED   BY  'password';
    # 赋权其中*.*表示是将主库中所有的库的所有的表REPLICATION权限给username用户
    GRANT REPLICATION SLAVE ON *.* TO 'username'@'slave_ip' IDENTIFIED BY 'password';
    # 刷新
    FLUSH PRIVILEGES;
    ```

17. 查看master状态:show master status;

    1. File:此时正在使用的二进制文件名
    2. Position:此时正在File文件的那个位置
    3. Binlog_Do_DB:需要复制的数据库,为null表示所有数据库都复制
    4. Binlog_Ignore_DB:需要忽略的数据库

18. 在从库中执行主从语句

    ```mysql
    # 主库锁表
    flush table with read lock;
    # 查看当前主库的日志文件以及位置
    show master status;
    # master_log_file:这是从库读取主库的bin-log的文件名,需要从show master status的File获取
    # master_log_pos:从库读取主库文件时,从那一个位置开始读取,需要从show master status的position获取
    change master master_host='主库ip',master_port=主库port,master_user='username', master_password='password',master_log_file='xxx-bin.000001',master_log_pos=0;
    # 完整后解除锁表
    unlock tables;
    ```

19. 开启主从,从库中执行命令:start slave;

20. 停止从库:stop slave;

21. 查询主从状态是否正常

    ```mysql
    # 查看slave装填,\G表示竖行显示,不要加分号,会报错
    SHOW SLAVE STATUS\G
    # 若输出的结果中不报错,且Slave_IO_Running和Slave_SLQ_Running都为yes时,表示主从正常
    ```



## Show Slave Status

* connecting to master:线程正试图连接主服务器
* checking master version:检查版本,建立同主服务器之间的连接后立即临时出现的状态
* registering slave on master:将slave注册到master上,建立同主服务器之间的连接后立即临时出现的状态
* requesting binlog dump:建立同主服务器之间的连接后立即临时出现的状态.线程向主服务器发送一条请求,索取从请求的二进制日志文件名和位置开始的二进制日志的内容
* waiting to reconnect after a failed binlog dump request:如果二进制日志转储请求失败,线程进入睡眠状态,然后定期尝试重新连接,可以使用--master-connect-retry选项指定重试之间的间隔
* reconnecting after a failed binlog dump request:线程正尝试重新连接主服务器
* waiting for master to send event:线程已经连接上主服务器,正等待二进制日志事件到达.如果主服务器正空闲,会持续较长时间.如果等待持续slave_read_timeout秒,则发生超时.此时,线程认为连接被中断并企图重新连接



## 简单配置

* 配置文件的修改同2正常配置,不同的是进行主从复制的方式

* 对主库进行备份,同时加上--master-data=1参数,此时在主库的bin_log日志中会自动写入可执行的change master语句,详见4.1

* 主库锁表

* bin_log同步,此时不再需要加上指定的文件名和文件位置

  ```mysql
  change master master_host='主库ip',master_port=主库port,master_user='username', master_password='password';
  ```



## 主从故障

### 第一种

停止主从,跳过故障点,重新开启主从

```mysql
# 从库上执行
stop slave;
# 跳过一个bin_log的复制点,从下一个位置开始.后面的1可以是其他数字,数字越大丢失越多
set global sql_slave_skip_counter=1;
start slave;
```



### 第二种

配置slave-skip-errors,该参数表示跳过指定错误码的错误,错误码可参考mysql文档



### 第三种

主库损坏,备份不可用.若只有一个从库,直接用从库的数据恢复.若有多个从库,查看每一个从库的master.info文件,判断那一个对主库的复制位置更新,POS更大就用那一个

* 确保所有relay_log全部更新完毕
  * 在每个从库上执行stop slave io_thread;show processlist;直到看到Has read all relay log,表示从库更新都执行完毕
* 登录到从库,将从库切换成主库.同时要注意清理授权表,read-only等参数
  * stop slave;
  * retset master;
  * quit;
* 进到从库数据目录,删除master.info,relay-log.info
* 提升从库为主库
  * 开启从库的bin_log,如果存在log-slave-updates,read-only等一定要注释
  * 重启从库,提升主库完毕
* 如果主服务器没宕机,需要去从库拉去bin-log补全提升主库的从库
* 其他从库操作
  * stop slave;
  * change master to master_host='':如果不同步,就指定位置
  * start slave;
  * show slave status\G



### 半同步



## HA

Keepalived+LVS+MYSQL+GALERA(同步复制)



## 延迟

* 分库,将一个主库拆分为4个主库,每个主库的写并发就500/s,此时主从延迟可忽略
* 打开mysql支持的并行复制,多个库并行复制
* 根据业务重写代码,不要在插入之后即可查询
* 若必须立刻查询,则读取的操作直接连接主库



# 高可用



## 双主

### 解决主键自增长

* master1,在mysqld下配置如下2个参数
  * auto_increment_increment=2:设置自增长的间隔为2,若是3主,设置为3
  * auto_increment_offset=1:设置id的起始值为1
* master2
  * auto_increment_increment=2:设置自增长的间隔为2
  * auto_increment_offset=1:设置id的起始值为2
* 2个主库的bin_log和log-slave-updates,read-only也都要开启
* master1为主,master2为从做一次完整的主从
* master2为主,master1为从做一次完整的主从



## InnoDBCluster

* 支持自动Failover,具有强一致性,读写分离,读库高可用,读请求负载均衡,横向扩展的有点

* 由MySQL,MySQL Router,MySQL Shell组成
* 通常状况下是一主多从,主数据库可读写,从数据库可读
* MySQL Router对集群中的数据库进行管理,监听集群中数据库是否可用,自动切换数据库
* MySQL Shell是为管理人员提供的管理集群数据库的工具



# 重要命令

* SHOW VARIABLES:查看mysql所有运行参数,结合like可以查看模糊参数值
* SHOW GLOBAL/SESSION VARIABLES:查看所有的系统变量或会话变量.系统变量对所有的会话都有效,会话变量只对当前会话有效,重新登录之后由会话变量做出的会话设置就会失效
* SHOW FULL PROCESSLIST:查看系统正在运行状况,包括增删改查类型,消耗时间,是那个用户等
  * sending binlog event to slave:线程已经从二进制日志读取了一个事件并正将它发送到从服务器
  * finished reading one binlog:switching to next binlog:线程已经读完二进制日志文件并正打开下一个要发送到从服务器的日志文件
  * has sent all binlog to slave;waiting for binlog to be updated:线程已经从二进制日志读取所有主要的更新并已经发送到了从服务器.线程现在正空闲,等待由主服务器上新的更新导致的出现在二进制日志中的新事件
  * waiting to finalize termination:线程停止时发生的一个很简单的状态
* SET GLOBAL key_buffer_size=1024\*1024\*16:修改全局参数,不需要重启就可以生效.但是数据库重启就失效,若需要重启数据库也生效,需要在配置文件中修改
* SHOW [global] STATUS:查看数据库状态,包括增删改查等状态



# 故障

* mysql无法启动,启动时显示MySQL is running...,但是查询的时候并没有mysql的服务,造成该情况可能是mysql停止时出现错误导致

  * 解决:删除mysql.sock以及mysql.pid文件之后重新启动,若显示Starting MYSQL表示正常




# HeartBeat

> 将资源从一台计算机快速转移到另外一台机器上继续提供服务,类似keepalived



## 概述

* 通过修改heartbeat的配置文件,可以指定那一台heartbeat服务器作为主服务器,则另外一台将自动改成热备服务器,然后再热备服务器上配置heartbeat守护程序来监听来自主服务器的心跳消息.如果热备服务器在指定时间内没有监听到主服务器的心跳,就会启动故障转义程序,并取得主服务器上的相关资源服务的所有权,替代主服务器继续不间断的提供服务,从而达到高可用的目的
* heartbeat既可以有主从模式,也可以有主主模式
* 实现高可用切换主备的时间大概在5-20秒之间
* 可能会产生裂脑问题



## 脑裂

> 主备都检测到对方发生了故障,然后进行资源转移,实际上主备都是正常的,结果就造成多主的现象,而且主备都使用相同的VIP(virtual ip:虚拟IP),造成ip冲突,即便不IP冲突,也会造成数据在主备上不一致的问题

* 产生原因
  * 主备之间心跳线路故障,导致无法正常通信,但一会之后又恢复
  * 主备开启了防火墙阻挡了心跳
  * 网卡配置不正确
  * 配置错误
* 解决办法
  * 同时使用串行电缆和以太电缆连接,同时用两条心跳线路
  * 检测到脑裂时强行关闭一个心跳节点,需要特殊的设备支持,如stonith,fence.
  * 做好对脑裂的监控报警,比如邮件,手机短信
  * 启动磁盘锁.正在服务的以防锁住共享磁盘,脑裂发生时,让对方完全抢不到共享资源.但是这有一个很大的问题,如果主服务器突然崩溃,解锁命令就完全无法发送,资源就无法解锁
  * 报警后,不直接自动服务器接管,而是由人为人员控制接管



## 消息类型

* 心跳小心:约150字节的数据包,可能为单播,广播或多播,控制心跳频率及出现故障要等待多久进行故障转移

* 集群转换消息:ip-request和ip-request-resp.当主服务器恢复在线状态时,通过ip-request消息要求备服务器释放主服务器失败时备服务器取得的资源,然后备服务器关闭释放主服务器失败时取得的资源以及服务

  备服务器释放主服务器失败时取得的资源以及服务,就会通过ip-request-resp消息通知主服务器它不在拥有该资源以及服务器,主服务器收到来自备服务器的ip-request-resp之后,启动失败时释放的资源以及服务,并开始提供正常的访问服务

* 重传消息:rexmit-request控制重传心跳请求,此消息不太重要.



## 配置文件

* ha.cf:参数配置文件,配置一些基本参数
* authkey:认证文件,高可用服务之间根据对端的authkey,对对端进行认证
* haresource:资源配置文件,如ip等.可以直接调用/etc/ha.d/resource.d里的资源



# DRBD

> 分布式复制快设备,是基于块设备在不同的高可用服务器对之间同步和镜像数据的软件,通过它可以实现在网络中的两台服务器之间基于块设备级别的实时或异步镜像或同步复制,其实就是类似于rsync+inotify这样的架构项目软件.
>
> 只不过drbd是基于文件系统底层的,即block层级同步,而rsync+inotify是在文件系统上的实际物理文件的同步,因此,drbd效率更高,效果更好

## 概述

* drbd工作位置是在文件系统层级以下,比文件系统更加靠近操作系统内核以及IO栈.
* 在基于drbd的高可用(HA)两台服务器主机中,当我们讲数据写入到本地磁盘系统时,数据还会被实时的发送到网络中另外一个主机上,并以相同的形式记录在另一个磁盘系统中,使得主备保持实时数据同步
* 主服务器发生故障时,备服务器就可以立即切换使用,因为和主服务器上的数据一致.
* 实时同步:当数据写到本地磁盘和远端所有服务器磁盘都成功后才会返回成功写入,使用协议C
* 异步同步:当数据写入到本地服务器成功之后就返回成功写入,不管远端服务器是否写入成功,使用协议A,B



## 生产应用模式

* 单主模式:就是主备模式,典型的高可用集群方案
* 复制模式:需要采用共享集群文件系统,如GFS,OCFS2,用于需要从2个节点并发访问数据的场合



## 同步复制协议

* 协议A:异步复制协议,本地写成功之后立即返回,数据放在发送buffer中,可能丢失
* 协议B:内存同步(半同步)复制协议.本地写成功并将数据发动对方后立即返回,如果双机掉电,可能丢失数据
* 协议C:同步复制协议.本地和对方服务器磁盘都成功确认后返回成功
* 工作中一般用C,但是会影响流量,从而影响网络延迟



## 相关数据同步工具

* rsync:实时同步工具sersync,inotify,lsyncd
* scp
* nc
* nfs:网络文件系统
* union:双机同步
* csync2:多机同步
* 软件自身的同步机制
* drbd



# pt-query-digest



* 用于分析mysql慢查询的工具,可以分析binlog,general log,slowlog
* 可以通过showprocesslist或tcpdump抓取的MySQL协议数据进行分析
* 可以把分析结果输出到文件中,分析过程是先对查询语句的条件进行参数化,然后对参数化以后的查询进行分组统计,统计出各查询的执行时间,次数,占比等



# Docker中使用

```shell
docker run -p 3306:3306 --name mysql-master -v /app/mysql/master/log:/var/log/mysql -v /app/mysql/master/data:/var/lib/mysql -v /app/mysql/master/conf:/etc/mysql -e MYSQL_ROOT_PASSWORD=root -d mysql
```

