# Redis

# 概述



* redis主要来做系统缓存,减少程序对数据库的访问,加大程序吞吐量
* redis默认有16384的slots(槽).每个槽可以存储多个hash值
* redis的3种主从模式
  * 普通模式:单主,多从,主从节点数据一致,故障自动切换
  * 哨兵模式:单主,多从,主从节点数据一致,另外一个为哨兵节点,用来检测其他节点的运行状态
  * 集群模式:多主,每一个节点有多个从节点,数据分摊,实现高可用
* redis持久化
  * RDB模式:默认策略,周期性的将内存中的数据写入dump.rdb文件中,可能会造成数据丢失
  * AOF模式:当redis发生了类似数据库的DML操作时,将会实时写入日志文件中,不会造成数据丢失
* redis常用配置文件
  * daemonize:默认no,前台启动.该为yes守护线程启动
  * appendonly:默认no,不开启AOF持久化.改为yes开启,防止数据丢失过多
* redis的发布/订阅
  * 只能在集群中或同一台机器中使用
  * 发布主题:publish topic content:topic为发布的主题名,content为发布的内容
  * 订阅主题:每个redis可监听多个发布的主题
    * subscribe topic1 topic2...:精准订阅,完全符合topic的才会收到消息
    * psubscribe topic*:通过通配符订阅多个主题



# 核心



## 单线程工作

![](REDIS01.PNG)



# 事务

> redis的事务比较简单,multi命令打开事务,之后开始进行设置.设置的值都会放在一个队列中进行保存,当设置完之后,使用exec命令,执行队列中的所有操作

* 仅仅是保证事务里的操作会被连续独占的执行,因为是单线程架构,在执行完事务内所有指令前是不可能再去同时执行其他客户端的请求的
* 没有隔离级别的概念,因为事务提交前任何指令都不会被实际执行,也就不存在事务内的查询要看到事务里的更新,在事务外查询不能看到这种问题了
* 不保证原子性,也就是不保证所有指令同时成功或同时失败,只有决定是否开始执行全部指令的能力,没有执行到一半进行回滚的能力
* watch key1...:监视一组key,当某个key的值发生变动时,事务被打断
* unwatch:取消监视
* multi:开始事务
* exec:执行事务内的所有命令,同时会取消watch的监听
* discard:取消事务
* **若在加入队列过程中发生了异常,整个队列中的操作都将无效.若是在加入队列之后,执行exec时发生异常,那么发生异常的操作会无效,其他未发生异常的操作仍然有效**
* 使用watch监听key时,若事务还未开始,而其他线程对监听的key进行了修改操作,之后再开始事务,此时,事务内所有的操作都将无效.该功能可以认为是一种乐观锁机制,一旦被监听的key值发生了改变,说明事务失效,需要重新查询之后再做操作



# 发布订阅

* Redis的发布订阅模式可以实现进程间的消息传递

* publish:发布消息,格式是publish channel 消息
* subscribe:订阅频道,格式是subscribe channel,可以是多个channel
* psubscribe:订阅频道,格式是psubscribe channel,支持glob风格的通配符
* unsubscribe:取消订阅,格式是unsubscribe channel,不指定频道表示取消所有subscribe命令的订阅
* punsubscribe:取消订阅,格式是punsubscribe channel,不指定频道表示取消所有psubscribe命令的订阅.这里匹配模式的时候,是不会将通配符展开的,是严格进行字符串匹配的.比如:`punsubscribe *`是无法退定c1.\*的,必须严格使用punsubscribe c1.*才可以  



# 命令

* [文档1](http://doc.redisfans.com/),[文档2](http://redisdoc.com/index.html)
* redis-cli:启动redis的命令行,可在其中操作redis
* help command:查看某个命令的帮助文档
* config get timeout:查看redis的配置文件中的某个选项的值
* config set timeout num:设置redis的配置文件中某个选项的值
* slowlog get:获得慢日志
* monitor:监控redis所有的操作,消耗比较大,开发可使用
* select 0/1...15:选择哪一个数据库,有16个,默认是0
* mset key1 val1 key2 val2...:一次性设置多个字符串键值对
* mget key1 key2...:一次性获取多个键值.在集群模式下,若key分布式在多个节点下,可能出错
* lpush key value1 value2...:从左边开始往一个list中放入值,value1先放,在最后
* lrange key start end:获得指定key存储的list中开始和结尾下标的值,start和end超出下标不报错
* sdiff set1 set2:获得2个set集合中的差集,从set1中取出set2中不存在的,从set2中取出set1不存在的
* sinter set1 set2:获得2个set的交集,set1和set2都有的
* sunion set1 set2:获得2个set的并集,重复值去重
* info replication:查看当前redis的主从信息
  * role:是否主从,master是主,slave是从
  * connected_slaves:从redis的数量以及从redis的ip端口,在线状态,同步位置
  * master_repl_offset:
  * repl_backlog_active:
  * repl_backlog_size:
  * repl_backlog_first_byte_offset:
  * repl_backlog_histlen:
  * master_host:只在从redis上显示,主redis的ip
  * master_port:只在从redis上显示,主redis的port
  * master_link_status:只在从redis上显示,主redis是否在线,up表示在线,down表示不在线
  * master_last_io_seconds_ago:
  * masetr_sync_in_progress:
  * slave_repl_offset:
  * slave_priority:
  * slave_read_only:是否只读,1只读
* SLAVEOF IP PORT:在redis-cli中执行,表示当前机器配置为指定ip端口的redis的从redis,但是一旦当前redis停止,那么将不再是从redis
* SLAVE NO ONE:在redis-cli中执行,表示当前机器不再是从redis,而是变成了主redis,将与原来的主redis独立运行,不互相干涉
* 位图
* GEO:地理信息位置



## KEY

* keys *:查看当前数据库所有的键值,\*可以是?或[],类似正则
* set key value:设置字符串类的key-value
* get key:获取字符串类的指定key的值
* exists key:查看当前数据库是否有指定key.存在返回1,不存在返回0
* move key db:将当前数据库中指定key移动到指定数据库中.若当前数据库没有该key或目的数据库已经存在该key,则移动失败,返回0.移动成功返回1
* del key1 key2...:从当前数据库中删除指定key
* randomkey:从当前数据库中随机返回一个key,但不会删除该key
* type key:查看指定key所存储的值的类型,是值,不是key
* rename key nkey:将key改名为新key.当key和nkey相同或key不存在时,返回一个错误;当nkey已经存在时,RENAME命令相当于将原key的value覆盖nkey的value
* renamenx key nkey:当且仅当nkey不存在时,将key改名为nkey.当key不存在时,返回一个错误.如果修改成功,返回1;如果nkey已经存在,返回0



## 原子加减

* INCR key:将key中存储的数字值加1
* DECR key:将key中存储的数字值减1
* INCRBY key num:将key存储的数字值加num
* DECRBY key num:将key存储的数字值减num



## HASH

* hset key field value:设置hash类key-value,field是hash中的key值
* hmset key field1 value1 field2 value2...:同时设置多个hash的键值对
* hget key field:获得指定key存储的hash中的指定field的值
* hmget key field1 field2...:同时获得多个field的值
* hgetall key:获得指定key中的hash键值对
* hdel key field...:删除指定key存储的hash中的field代表的键值
* hlen key:获得指定key里面的键值对的数量
* hexists key:判断键值对是否存在
* hkeys key:获得指定key所存储的所有键值对的field值
* hvals key:获得指定key所存储的所有键值对的val值
* hsetnx key:若key存在什么也不做,若不存在则赋值



## SET

* 无序不重复集合

* SADD key value:往集合key中添加value元素
* SREM key value:从集合key中删除value元素
* SISMEMBER key value:检查集合key中是否有value元素
* SMEMBERS key:获取集合key中所有元素
* SCARD key:获取集合key中元素个数
* SRANDMEMBER key num:从集合key中随机获取num个元素,取出的元素仍然在原集合中
* SPOP key num:从集合key中随机取出2个元素,取出的元素将中原集合中删除
* SINTER key1 key2...:取多个集合的交集,即取出所有集合中都有的元素
* SUNION key1 key2...:取多个集合的并集,即将所有集合中的元素进行合并
* SDIFF key1 key2...:取多个key1集合的差集,即以第一个集合为基准,只保留其他集合中没有的元素



## ZSET

* 有序不重复集合

* ZADD key score value [[score value]...]:往有序集合key中加入带分值元素,score可用来排序

 * ZREM key value [value...]:从有序集合key中删除元素
 * ZSCORE key value:返回有序集合key中元素member的分值
 * ZINCREBY key num value:为有序集合key中元素value的分值加上num
 * ZCARD key:返回有序集合key中元素个数
 * ZRANGE key start end [WITHSCORES]:正序获取有序集合key从start下标到end下标的元素
 * ZREVRANGE key start end [WITHSCORES]:倒序获取有序集合key从start下标到end下标的元素
 * ZUNIONSTORE destkey numkeys key [key...]:并集计算
 * ZINTERSTORE dest key numkeys key [key...]:交集计算



## 持久化

* expire key num:设置指定key多少秒之后过期
* expireat key timestamp:设置过期时间,值是一个到秒的时间戳
* pexpire key num:设置指定key多少毫秒之后过期
* pexpireat key timstamp:设置过期时间,值是一个到毫秒的时间戳
* persist key:设置指定key永不过期,,另外用set或getset命令为key赋值时也会清除过期时间
* ttl key:查看某个key还有多少秒过期.返回-1表示永不过期,-2表示已经过期
* pttl key:同ttl,但是是以毫秒为单位



# 适用场景

* 缓存
* 取最新N个数据的操作:zincrby
* 排行榜类的应用,取TOP N操作,前面操作以时间为权重,这个是以某个条件为权重
* 存储关系,比如社交关系
* 获取某段时间所有数据排重值,使用set,比如某段时间访问的用户id,或者是客户端ip
* 构建对队列系统,list可以构建栈和队列,使用zset构建优先级队列
* 实时分析系统,如访问频率控制
* 模拟类似于httpsession这种需要设定过期时间的功能
* 分布式锁:setnx
* 分布式唯一主键生成:incrby
* 计数器:incr
* 限流:incr
* 购物车
* 用户消息时间线timeline,list,双向链表
* 抽奖:使用SET的spop
* 点赞,签到,打卡:使用SET的sadd,srem,sismember,smembers,scard
  * 点赞:`SADD like:<消息ID> <用户ID>`
  * 取消点赞:`SREM like:<消息ID> <用户ID>`
  * 检查用户是否点过赞:`SISMEMBER like:<消息ID> <用户ID>`
  * 获取点赞的用户列表:`SMEMBERS like:<消息ID>`
  * 获取点赞的用户数:`SCARD like:<消息ID>`
* 商品标签
* 商品筛选:sdiff set1 set20->获取差集;sinter set1 set2->获取交集;sunion set1 set2->获取并集
* 用户关注,推荐模型



# 数据结构



## 编码数据结构

* 编码数据结构主要在对象包含的值数量比较少、或者值的体积比较小时使用



### 压缩列表(zip list)

* 类似于数组
* 压缩列表包含的项都是有序的,列表的两端分 别为表头和表尾
* 每个项可以储存一个字符串、整数或者浮点数
* 可以从表头开始或者从表尾开始遍 历整个压缩列表,复杂度为 O(N) 
* 定位压缩列表中指定索引上的项,复杂度为 O(N) 
* 使用压缩列表来储存值消耗的内存比使用双向链表来储存值消耗的内存要少
* List,Set,ZSet在数据量小时都可能会使用该数据类型



### 整数集合(int set)

* 集合元素只能是整数(最大为64位),并且集合中不会出 现重复的元素
* 集合的底层使用有序的整数数组来表示
* 数组的类型会随着新添加元素的类型而改变.如果集合中位长度最大的元素可以使用16位整数来保存,那么数组的类型就是int16_t,而如果集合中位长度最大的元素可以使用 32 位整数来保存,那么数组的类型就是 int32_t,诸如此类
* 数组的类型只会自动增大,但不会减小
* Set在数据量比较小时可能会使用该数据类型



## 普通数据结构



### 简单动态字符串

* SDS, simple dynamic string
* 可以储存位数组(实现 BITOP 和 HyperLogLog)、字符串、整数和浮点数,其中超过64位的整数和超过 IEEE 754 标准的浮点数使用字符串来表示
* 具有int、embstr和raw三种表示形式可选,其中 int 表示用于储存小于等于 64 位的整数,embstr 用来储存比较短的位数组和字符串,而其他格式的 值则由 raw 格式储存
* 比起 C 语言的字符串格式,SDS 具有以下四个优点:
  * 常数复杂度获取长度值
  * 不会引起缓冲区溢出
  * 通过预分配和惰性释放两种策略来减少内存重分配的 执行次数
  * 可以储存二进制位



### 双向链表

* 双向、无环、带有表头和表尾指针
* 一个链表包含多个项,每个项都是一个字符串对象,即一个链表对象可以包含多个字符串对象
* 可以从表头或者表尾遍历整个链表,复杂度为 O(N)
* 定位特定索引上的项,复杂度为 O(N)
* 链表带有长度记录属性,获取链表的当前长度的复杂度为 O(1)



### 字典

* 查找、添加、删除键值对的复杂度为 O(1),键和值都是字符串对象
* 使用散列表(hash table)为底层实现,使用链地址法(separate chaining)来解决键冲突
* Redis 会在不同的地方使用不同的散列算法，其中最常用的是 MurmurHash2 算法
* 在键值对数量大增或者大减的时候会对散列表进行重新散列(rehash),并且rehash 是渐进式、分多次进行的,不会在短时间内耗费大量 CPU 时间,造成服务器阻塞



### 跳表

* 支持平均 O(log N) 最坏 O(N) 复杂度的节点查找操作,并且可以通过执行范围性(range)操作来批量地获取有序的节点
* 跳表节点除了实现跳表所需的层(level)之外,还具有 score 属性和 obj 属性:
  * score:是一个浮点数,用于记录成员的分值
  * obj:是一个字符串对象,用来记录成员本身
* 和字典一起构成 ZSET 结构,用于实现 Redis的有序集合结构
  * 字典用于快速 获取元素的分值,以及判断元素是否存在
  * 跳表用于执行范围操作,比如实现 ZRANGE 命令



## HyperLogLog

* 接受多个元素作为输入,并给出输入元素的基数估算值,即统计不重复值的个数
* 算法给出的基数并不是精确的,可能会比实际稍微多一些或者稍微少一些,但会控制在合理的范围之内
* 不存储元素值,只存储存储元素之后的基数计算结果
* PFADD key element [element ...]
  将任意数量的元素添加到指定的 HyperLogLog 里面。
  这个命令可能会对 HyperLogLog 进行修改，以便反映新的基数估算 值，如果 HyperLogLog 的基数估算
  值在命令执行之后出现了变化， 那么命令返回 1 ， 否则返回 0 。
  命令的复杂度为 O(N) ，N 为被添加元素的数量
* PFCOUNT key [key ...]
  当只给定一个 HyperLogLog 时，命令返回给定 HyperLogLog 的基数估算值。
  当给定多个 HyperLogLog 时，命令会先对给定的 HyperLogLog 进行并集计算，得出一个合并后的
  HyperLogLog ，然后返回这个合并 HyperLogLog 的基数估算值作为命令的结果（合并得出的
  HyperLogLog 不会被储存，使用之后就会被删掉）。
  当命令作用于单个 HyperLogLog 时， 复杂度为 O(1) ， 并且具有非常低的平均常数 时间。
  当命令作用于多个 HyperLogLog 时， 复杂度为 O(N) ，并且常数时间也比处理单个 HyperLogLog 时要
  大得多
* PFMERGE destkey sourcekey [sourcekey ...]
  将多个 HyperLogLog 合并为一个 HyperLogLog ，合并后的 HyperLogLog 的基数估算值是通过对所有
  给定 HyperLogLog 进行并集计算得出的。
  命令的复杂度为 O(N) ， 其中 N 为被合并的 HyperLogLog 数量， 不过这个命令的常数复杂度比较高



# 配置文件

* timeout:客户端超过多少秒空闲后关闭.0禁止此功能,0表示不关闭
* tcp-keepalive:用于检测tcp连接是否还存活,建议设置300(单位秒),0表示不检测
* protected-mode:当设置为yes后,如果没有通过bind设置address以及没有设置password,那么redis只接受来loopback address 127.0.0.1和::1的连接和unix domain socket
* port:监听端口
* daemonize:是否以守护进程运行,默认no.通常修改为yes
* pidfile:存储redis pid的文件,redis启动后创建,退出后删除
* tcp-backlog:tcp监听队列长度.
  * backlog是一个连接队列,队列总和等于未完成的三次握手队列加上已经完成三次握手的队列
  * 在高并发环境下需要搞的backlog来避免慢客户端连接问题
  * linux内核会将这个值减小到/proc/sys/net/core/somaxconn的值,所以调高此值时应同时关注/proc/sys/net/ipv4/tcp_max_syn_backlog和/proc/sys/net/core/somaxconn的值
* bind:绑定网络ip,默认接受来自所有网络接口的连接,可以绑定多个,最多同时绑定16个
* unixsocket:指定用于监听连接的unix socket的路径
* unixsocketperm:unixsocket path的权限,不能大于777
* dir:数据快照存储的目录,必须是有效并且存在的目录,默认是当前目录
* always-show-logo:总是显示logo
* loglevel:日志级别,取值范围debug,verbose,notice,warning
* logfile:日志文件名
* syslog-enabled:启用写入日志到系统日志
* syslog-ident:输出到系统日志中的标识
* syslog-facility:指定系统日志输出的设备,取值范围,user,local0-local7
* databases:database数量,如果小于1则启动失败
* include:加载其他配置文件
* maxclients:同时最大的连接数,默认10000,如果小于1启动失败
* maxmemory:最大使用内存,超过则触发内存策略
* maxmemory-policy:最大缓存策略.当缓存过多时使用某种策略,删除内存中的数据
  * volatile-lru:在设置了过期的key中通过lru算法查找key删除
  * volatile-lfu:在所有key中通过lfu算法查找key删除
  * volatile-random:在设置了过期的key中随机查找key删除
  * volatile-ttl:最近要超时的key删除
  * allkeys-lru:所有key通过lru算法查找key删除
  * allkeys-lfu:所有key通过lfu算法查找key删除
  * allkeys-random:所有key随机查找key删除
  * noeviction:不过期,对于写操作返回错误
* maxmemory-samples:lru,lfu算法都不是精确的算法,而是估算值.lru和lfu在进行检查时,会从该配置指定的数量中进行运算,设置过高会消耗cpu,小于0则启动失败
* proto-max-bulk-len:批量请求的大小限制
* client-query-buffer-limit:客户端查询缓存大小限制,如果multi/exec 大可以考虑调节
* lfu-log-factor:小于0则启动失败
* lfu-decay-time:小于0则启动失败
* auth-pass:sentinel auth-pass <master-name> <password>,设置用于主从节点通信的密码.如果在监控redis实例中有密码的话是有用的.这个主节点密码同样用于从节点,所以给主节点和从节点实例设置不同的密码是不可能的.可以拥有不开启认证的redis实例和需要认证的redis实例混合(只要需要密码的redis实例密码设置一样),因为当认证关闭时,auth命令将在redis实例中无效
* requirepass password:用于在客户端执行命令前,要求进行密码验证,password为自定义密码
* activerehashing:默认每1秒10次消耗1ms来做rehashing来释放内存,会增加请求的延时,如果你对延时敏感,则设置no,默认yes
* lazyfree-lazy-eviction:同步或异步释放内存
  * no:默认,同步释放内存,停止完成其他请求来做释放内存操作,如果遇到key复杂度很大时(0(n))会增加请求延时
  * yes:先删除dict中的key,然后把释放内存的任务提交给后台线程做
* lazyfree-lazy-expire:同步或异步删除过期key
  * no:默认,同步删除过期key,也就是停止完成其他请求来做删除过期key,如果遇到key复杂度很大时(0(n))会增加请求延时
  * yes:把删除key的任务提交给后台线程做
* lazyfree_lazy_server-del:同步或异步删除key
  * no:默认,同步删除key,也就是停止完成其他请求来做删除key,如果遇到key复杂度很大时(0(n))会增加请求延时
  * yes:先删除dict中的key,然后把删除key的任务提交给后台线程做(如果key很小则暂时不删除,只是减少了引用)
* slave-lazy-flush/replica-lazy-flush:同步或异步清空数据库
  * no:默认同步清空数据库,也就是停止完成其他请求来做清空数据库,如果遇到数据库很大会增加请求延时
  * yes:新建dict等数据结构,然后把清空数据库提交给后台线程做
* activedefrag:如果你遇到内存碎片的问题,那么设置为yes,默认no
* dynamic-hz:设置yes,则根据客户端连接数可以自动调节hz
* hz:调节可以让redis再空闲时间更多的做一些任务(如关闭超时客户端等)
* lua-time-limit:lua脚本的最大执行时间,超过这个时间报错,单位毫秒,0或者负数则无限制
* latency-monitor-threshold:为了收集可能导致延时的数据根源,redis延时监控系统在运行时会采样一些操作
  * 通过 LATENCY命令 可以打印一些图样和获取一些报告
  * 这个系统仅仅记录那个执行时间大于或等于通过latency-monitor-threshold配置来指定的
  * 当设置为0时这个监控系统关闭,单位毫秒
* slowlog-log-slower-than:执行命令大于这个值计入慢日志.0表示所有命令全部记录慢日志.单位毫秒
* slowlog-max-len:最大的慢日志条数,这个会占用内存
  * slowlog reset:释放内存
  * slowlog len:查看当前慢日志条数
* client-output-buffer-limit:0则无限制
  * client-output-buffer-limit <class> <hard limit> <soft limit> <soft seconds>
  * client-output-buffer达到hard limit或者保持超过soft limit 持续sof seconds则断开连接
  * class 分为3种
    * normal:普通客户端包裹monitor客户端
    * replica 从节点
    * pubsub 至少pubsub一个channel或者pattern的客户端
* hll-sparse-max-bytes:大于这个值,hyperloglog使用稠密结构,小于等于这个值,使用稀疏结构,大于16000无意义,建议设置3000
* rename-command:重命名命令,建议重命名一些敏感的命令(如flushall,flushdb),设置为""表示禁用
* notify-keyspace-events:设置是否开启Pub/Sub 客户端关于键空间发生的事件,设置为""表示禁用
  * K:Keyspace events, published with keyspace@<db> prefix
  * E:Keyevent events, published with keyevent@<db> prefix
  * g:Generic commands (non-type specific) like DEL, EXPIRE, RENAME
  * $:String commands
  * l:List commands
  * s:Set commands
  * h:Hash commands
  * z:Sorted set commands
  * x:Expired events (events generated every time a key expires)
  * e:Evicted events (events generated when a key is evicted for maxmemory)
  * A:Alias for g$lshzxe , so that the "AKE" string means all the events.
* supervised:默认no
  * no:没有监督互动
  * upstart:通过将Redis置于SIGSTOP模式来启动信号
  * systemd:signal systemd将READY = 1写入$ NOTIFY_SOCKET
  * auto:检测upstart或systemd方法基于 UPSTART_JOB或NOTIFY_SOCKET环境变量



## RDB

* dbfilename:rdb文件名
* save:保存RDB快照的频率
  * save 900 1:在900秒内有1个key改变了则执行save
  * save "":之前的save 配置无效,不进行RDB持久化
* stop-writes-on-bgsave-error:当save错误后是否停止接受写请求,默认开启.如果设置成no,会造成数据不一致的问题
* rdbcompression:对于存储在磁盘中的快照,是否进行压缩存储.开启会消耗cpu
* rdbchecksum:是否检查rdbchecksum进行数据校验,默认yes,可以设置no
* rdb-save-incremental-fsync:数据是否增量写入rdb文件
  * yes:则每32mb执行fsync一次,增量式,避免一次性大写入导致的延时
  * no:一次性fsync写入到rdb文件



## AOF

* appendonly:是否开启AOF模式,生产环境必然开启
* appendfilename:AOF文件名,默认为appendonly.aof
* no-appendfsync-on-rewrite:设置当redis在rewrite的时候,是否允许appendsync.因为redis进程在进行AOF重写的时候,fsync()在主进程中的调用会被阻止,也就是redis的持久化功能暂时失效.默认为no,这样能保证数据安全  
* appendfsync:将数据同步到磁盘时,执行fynsc()的策略

  * everysec:默认每秒执行fsync
  * always:等到下次执行beforesleep时执行fsync
  * no:不执行fsync,让系统自行决定何时调用
  * 设置always往往比较影响性能,但是数据丢失的风险最低,一般推荐设置everysec
* auto-aof-rewrite-min-size:自动重写AOF的最小大小,比auto-aof-rewrite-percentage优先级高
* auto-aof-rewrite-percentage:相对于上次AOF文件重写时文件大小增长百分比.如果超过这个值,则重写AOF
* aof-rewrite-incremental-fsync:数据是否增量写入aof文件
  * yes:则每32mb执行fsync一次,增量式,避免一次性大写入导致的延时
  * no:则一次性fsync写入AOF文件
* aof-load-truncated:假如aof文件被截断了时的操作
  * yes:redis可以启动并且显示日志告知这个信息
  * no:redis启动失败,显示错误
* active_defrag_threshold_upper:开启内存碎片整理的最小内存碎片百分比,小于0或者大于1000则启动失败
* active_defrag_threshold_upper:内存碎片百分比超过这个值,则使用active-defrag-cycle-max,小于0或者大于1000则启动失败
* active-defrag-ignore-bytes:开启内存碎片整理的最小内存碎片字节数,如果小于等于0则启动失败
* active-defrag-cycle-max:最小努力cpu百分比,用来做内存碎片整理
* active-defrag-cycle-min:最大努力cpu百分比,用来做内存碎片整理
* active-defrag-max-scan-fields:用于主动的内存碎片整理的set/hash/zset/list中的最大数量的项,如果小于1,启动失败
* hash-max-ziplist-value:hash 中的项大小小于或等于这个值使用ziplist,超过这个值使用hash
* stream-node-max-bytes:stream 的最大内存开销字节数
* stream-node-max-entries:stream 的最大项数量



## Mixed



* aof-use-rdb-preamble:AOF前部分用RDB,后面保存缓存时的命令还是用AOF,能够在Redis重启时能更快的恢复之前的数据.yes开启,必须先开启AOF



## List



* list-max-ziplist-size:负值表示节点大小

  * -5:每个list节点大小不能超过64 Kb
  * -4:每个list节点大小不能超过32 Kb
  * -3:每个list节点大小不能超过16 Kb
  * -2:每个list节点大小不能超过8 Kb
  * -1:每个list节点大小不能超过4 Kb
  * 推荐-1,-2,正值表示节点数量
  * 满足设置的值,则使用ziplist表示,节约内存;超过设置的值,则使用普通list
* list-compress-depth:不压缩quicklist,距离首尾节点小于等于这个值的ziplist节点,默认首尾节点不压缩
  * 1:head->next->...->prev->tail,不压缩next,prev,以此类推
  * 0:都不压缩
* list-max-ziplist-entries:设置使用ziplist的最大的entry数
* list-max-ziplist-value:设置使用ziplist的值的最大长度



## Set



* set-max-intset-entries:当set 的元素数量小于这个值且元素可以用int64范围的整型表示时,使用inset,节约内存大于或者元素无法用int64范围的整型表示时用set表示



## ZSet



* zset-max-ziplist-entries:当sorted set 的元素数量小于这个值时,使用ziplist,大于用zset
* zset-max-ziplist-value:当sorted set 的元素大小小于这个值时,使用ziplist,大于用zset



## Hash



* hash-max-ziplist-entries:hash中的项数量小于或等于这个值使用ziplist,超过这个值使用hash
* hash-max-ziplist-value:设置使用ziplist的值的最大长度  



## MasterSlave



* replicaof(slaveof) ip port:主从复制时的主Redis的ip和端口.从redis应该设置一个不同频率的快照持久化的周期,或者为从redis配置一个不同的服务端口
* masterauth:如果主redis设置了验证密码的话,则在从redis的配置中要使用masterauth来设置校验密码
* repl-ping-replica(slave)-period:从发给主的心跳周期,如果小于0则启动失败,默认10秒
* repl-timeout:多少秒没收到心跳的响应认为超时,最好设置的比repl-ping-replica(slave)-period大
* repl-disable-tcp-nodelay:是否禁用tcp-nodelay,如果设置yes,会导致主从同步有40ms滞后(linux默认),如果no,则主从同步更及时
* repl-diskless-sync:主从复制是生成rdb文件,然后传输给从节点,配置成yes后可以不进行写磁盘直接进行复制,适用于磁盘慢网络带宽大的场景
* repl-diskless-sync-delay:当启用diskless复制后,让主节点等待更多从节点来同时复制,设置过小,复制时来的从节点必须等待下一次rdb transfer.单位秒,如果小于0则启动失败
* repl-backlog-size:复制积压大小,解决复制过程中从节点重连后不需要full sync,这个值越大,那么从节点断开到重连的时间就可以更长
* repl-backlog-ttl:复制积压的生命期,超过多长时间从节点还没重连,则释放内存
* slave-priority/replica-priority:当master不在工作后,从节点提升为master的优先级,0则不会提升为master.越小优先级越高
* slave(replica)-announce-ip:从节点上报给master的自己ip,防止nat问题
* slave(replica)-announce-port:从节点上报给master的自己port,防止nat问题
* min-slaves(replicas)-to-write:最少从节点数,不满足该参数和低于min-slaves(replicas)-max-lag时间的从节点,master不再接受写请求.如果小于0则启动失败,默认0,也就是禁用状态
* min-slaves(replicas)-max-lag:最大从节点的落后时间,不满足min-slaves-to-write和低于这个时间的从节点,master不再接受写请求
* masterauth:主从复制时主redis的密码验证
* replica(slave)-serve-stale-data:默认yes,当从节点和主节点的连接断开或者复制正在进行中
  * yes:继续提供服务
  * no:返回sync with master in progress错误
* replica(slave)-read-only:配置从节点数据是否只读,但是配置的修改还是可以的
* replica(slave)-ignore-maxmemory:从节点是否忽略maxmemory配置,默认yes



## Cluster



* cluster-enabled:开启集群模式
* cluster-config-file:集群配置文件名
* cluster-announce-ip:集群的节点的汇报ip,防止nat
* cluster-announce-port:集群的节点的汇报port,防止nat
* cluster-announce-bus-port:集群的节点的汇报bus-port,防止nat
* cluster-require-full-coverage:默认如果不是所有slot已经分配到节点,那么集群无法提供服务,设置为no,则可以提供服务
* cluster-node-timeout:认为集群节点失效状态的时间,如果小于0则启动失败
* cluster-migration-barrier:当存在孤立主节点后(没有从节点),其他主节点的从节点会迁移作为这个孤立的主节点的从节点,前提是这个从节点之前的主节点至少还有这个数个从节点.
  * 不建议设置为0
  * 想禁用可以设置一个非常大的值
  * 如果小于0则启动失败
* cluster-slave-validity-factor:如果从节点和master距离上一次通信超过 (node-timeout * replica-validity-factor) + repl-ping-replica-period时间,则没有资格失效转移为master
* cluster-replica(slave)-no-failover:在主节点失效期间,从节点是否允许对master失效转移



## Sentinel

* sentinel monitor <master-name> <ip> <redis-port> <quorum>:sentinel monitor mymaster 127.0.0.1 6379 2,告知sentinel监控这个ip和redis-port端口的redis,当至少达到quorum数量的sentinel同意才认为他客观离线(O_DOWN)
* sentinel down-after-milliseconds <master-name> <milliseconds>:附属的从节点或者sentinel和他超过milliseconds时间没有达到,则主观离线(S_DOWN)
* sentinel failover-timeout  <master-name> <milliseconds>:
  * 距离被一个给定的Sentiel对同一个master进行过failedover的上一次的时间是此设置值的2倍
  * 从从节点根据sentinel当前配置复制一个错误的主节点到复制新的主节点的时间需要的时间(从sentinel检测到配置错误起计算)
  * 取消一个在进行中但是还没有产生配置变化(slave of no one还没有被提升的从节点确认)的failover需要的时间
  * 进行中的failover等待所有从节点 重新配置为新master的从节点的最大时间.然而 虽然在这个时间后所有从节点将被sentinel重新配置,但并不是指定的正确的parallel-syncs 过程
* sentinel parallel-syncs <master-name> <numreplicas>:制定多少个从节点可以在failover期间同时配置到新的主节点.如果你用从节点服务查询,那么使用一个较低的值来避免所有的从节点都不可达,切好此时他们在和主节点同步
* sentinel notification-script <master-name> <script-path>:对任何生成的在WARNING 级别的sentinel 事件会调用这通知脚本(例如sdown,odown等).
  * 这个脚本应该通过email,sms等其他消息系统通知系统管理员监控的redis系统有错
  * 调用这个脚本带有2个参数,第一个是事件类型,第二个是事件描述
  * 如果这个选项设置的话这个脚本必须存在
* sentinel client-reconfig-script <master-name> <script-path>
  * 当主节点由于failover改变,一个脚本可以执行,用于执行通知客户端配置改变了主节点在不同地址的特定应用的任务
  * 下面的参数将传递给这个脚本
    <master-name> <role> <state> <from-ip> <from-port> <to-ip> <to-port>
  * state:当前总是failover
  * role:不是leader就是observer
  * 从from-ip,from-port,to-ip,to-port 用于和旧主节点和被选举的节点(当前主节点)通信的地址
    这个脚本是可以被多次调用的
* SENTINEL rename-command:如 SENTINEL rename-command mymaster CONFIG GUESSME
  有时,redis服务 有些sentinel工作正常需要的命令,重命名为猜不到的字符串.通常是在提供者提供redis作为服务而且不希望客户重新配置在管理员控制台外修改配置的场景中的config,slaveof,在这个情况,告诉sentinel使用不同的命令名字而不是常规的是可能的.
* sentinel announce-ip <ip>:在nat环境下是有用的
* sentinel announce-port <port>:在nat环境下是有用的
* sentinel deny-scripts-reconfig yes:默认sentinel set 在运行期是不能改变notification-script和 client-reconfig-script.这个避免一些细小的安全问题,在这里客户端可以设置脚本为任何东西而且触发一个failover用于让这个程序执行



# 缓存过期



## 过期策略

* 默认情况下,Redis每100ms随机选取10个key,检查这些key是否过期,如果过期则删除.如果在1S内有25个以上的key过期,立刻再额外随机100个key
* 当Client主动访问key时,会先对key进行超时判断,过期的key会被删除
* 当Redis内存最大值时,会执行相应算法,对内存中的key进行不同的过期操作
* 每次set的时候都会清除key的过期时间



## LRU

* LRU:Least Recently Used,最近最少使用算法,将最近一段时间内,最少使用的一些数据给干掉
* 默认情况下,当内存中数据太大时,redis就会使用LRU算法清理掉部分数据,然后让新的数据写入缓存



## 缓存清理设置

* maxmemory:设置redis用来存放数据的最大的内存大小,一旦超出该值,就会立即使用LRU算法.若maxmemory设置为0,那么就默认不限制内存的使用,直到耗尽机器中所有的内存为止
* maxmemory-policy:可以设置内存达到最大值后,采取什么策略来处理
  * noeviction:如果内存使用达到了maxmemory,client还要继续写入,直接报错给客户端
  * allkeys-lru:就是我们常说的LRU算法,移除掉最近最少使用的那些keys对应的数据,默认策略
  * allkeys-random:随机选择一些key来删除掉
  * volatile-lru:也是采取LRU算法,但是仅仅针对那些设置了指定存活时间(TTL)的key才会清理掉
  * volatile-random:随机选择一些设置了TTL的key来删除掉
  * volatile-ttl:移除掉部分keys,选择那些TTL时间比较短的keys
* redis在写入数据的时候,可以设置TTL,过期时间
* 缓存清理的流程
  * 客户端执行数据写入操作
  * redis接收到写入操作后,检查maxmemory,如果超过就根据对应的policy清理掉部分数据
  * 写入操作完成执行



# 缓存机制

## RDB

* 默认开启,每隔指定时间将内存中的所有数据生成到一份RDB文件中,性能比较高
* 配置save检查点

```shell
# 在配置文件redis.conf中配置,如下
# 每隔60秒时间,若有超过1000个key发生了变更,那么就生成一个新的dump.rdb
save  60  1000
```

* save检查点可以有多个,只要满足其中之一就会检查,发现变更就会生成新的dump.rdb文件
* 可以手动调用save或bgsave进行快照备份
  * save:冷备时只管备份,不管其他,全部阻塞
  * bgsave:异步备份,不阻塞redis的读写操作,可用lastsave获得最近一次备份的时间
* 若在生成快照期间发生故障,可能会丢失比较多的数据,适合做冷备份
* redis在优雅退出的时候,会立即将内存中的数据生成完整的RDB快照,强制退出则不会
* RDB对redis对外提供的读写服务,影响非常小,可以让redis保持高性能,因为redis主进程只需要fork一个子进程,让子进程执行磁盘IO操作来进行RDB持久化即可
* 相对于AOF持久化机制来说,直接基于RDB数据文件来重启和恢复redis进程,更加快速
* redis会单独创建(fork)一个子进程来进行持久化,会先将数据写入到一个临时文件中,当持久化过程都结束时,再用这个临时文件替换上次持久化好的文件.整个过程中,主进程是不进行任何IO操作的,这就确保了极高的性能.如果需要进行大规模数据的恢复,且对数据恢复的完整性不是非常敏感,则RDB比较高效,但是可能丢失最后一次持久化后的数据



### BGSAVE机制

* Redis借助操作系统提供的写时复制技术(Copy-On-Write, COW),在生成快照的同时,依然可以正常处理写命令.即bgsave子进程是由主线程fork生成的,可以共享主线程的所有内存数据
* bgsave子进程运行后,开始读取主线程的内存数据,并把它们写入RDB文件
* 如果主线程对这些数据也都是读操作,那么主线程和bgsave子进程相互不影响
* 如果主线程要修改一块数据,那么这块数据就会被复制一份,生成该数据的副本.然后,bgsave子进程会把这个副本数据写入RDB文件,而在这个过程中,主线程仍然可以直接修改原来的数据  



## AOF

* 生成一份修改记录日志文件(appendonly.aof),每次执行操作都会将命令先写入os cache,然后每隔一定时间再fsync写到AOF文件中

* 配置AOF持久化,生产环境中,AOF一般都是开启的:将redis.conf中的appendonly no改为appendonly yes即可开启
* 同时开启了RDB和AOF时,redis重启之后,**仍然优先读取AOF中的数据**,但是AOF数据恢复比较慢
* fsync策略,在配置文件中修改appendfsync
* AOF文件只有一份,当文件增加到一定大小时,AOF会进行rewrite操作,会基于当前redis内存中的数据,重新构造一个更小的AOF文件,然后将大的文件删除
* 可以使用bgrewriteaof强制进行AOF文件重写
* rewrite是另外一线程来写,对redis本身的性能影响不大
  * auto-aof-rewrite-percentage:redis每次rewrite都会记住上次rewrite时文件大小,下次达到上次rewrite多少时会再次进行rewrite,默认是100,可以不改
  * auto-aof-rewrite-min-size:redis进行rewrite的最小内存,默认是64M,几乎不用改
* 如果AOF文件有破损,备份之后,可以用**redis-check-aof  --fix appendonly.aof**命令进行修复,命令在redis的bin目录下
* 修复后可以用diff -u查看两个文件的差异,确认问题点
* RDB的快照和AOF的fsync不会同时进行,必须先等其中一个执行完之后才会执行另外一个
* 热启动appendonly,数据恢复时可用,但并没有修改配置文件,仍需手动修改:**config set appendonly yes**



## Mixed

* 混合持久化模式,需要同时开启RDB和AOF
* 重启Redis时,RDB恢复更快,但是会丢失大量数据.使用AOF重启,性能相对RDB要慢,在Redis实例很大的情况下,启动需要花费很长的时间.Redis 4.0带来了一个新的持久化选项—混合持久化
* 通过如下配置可以开启混合持久化:`aof-use-rdb-preamble yes`
* 如果开启了混合持久化,AOF在重写时,不再是单纯将内存数据转换为RESP命令写入AOF文件,而是将
  重写这一刻之前的内存做RDB快照处理,并将RDB快照内容和增量的AOF修改内存数据的命令存在一
  起写入新的AOF文件,新文件一开始不叫appendonly.aof,重写完新的AOF文件才会进行改名,覆盖原有的AOF文件
* 在Redis重启的时候,可以先加载RDB的内容,然后再重放增量AOF日志就可以完全替代之前的AOF全量文件重放,重启效率大幅得到提升 



# 安装



* 安装相关依赖:yum install -y gcc-c++
* 下载或上传redis压缩包到linux服务上,解压:tar zxvf redis-5.0.14.tar.gz
* 编译,设置安装路径为/app/software/redis下:make install PREFIX=/app/software/redis
* 修改配置文件:vi redis.conf
* 将daemonize修改成yes,守护进程启动
* 启动:../redis-server redis.conf
* ./redis-cli:进入Redis自带客户端中



# 备份还原



## 备份

1. 写crontab定时调度脚本去做数据备份

2. 每小时都copy一份rdb的备份到一个目录中,仅仅保留3天的备份

   ```shell
   crontab -e
   0 * * * * sh /usr/local/redis/copy/redis_rdb_copy_hourly.sh
   vi redis_rdb_copy_hourly.sh
   #!/bin/sh 
   cur_date=`date +%Y%m%d%k`
   rm -rf /usr/local/redis/snapshotting/$cur_date
   mkdir /usr/local/redis/snapshotting/$cur_date
   cp /var/redis/6379/dump.rdb /usr/local/redis/snapshotting/$cur_date
   del_date=`date -d -48hour +%Y%m%d%k`
   rm -rf /usr/local/redis/snapshotting/$del_date
   ```

3. 每天都保留一份当天的rdb备份到一个目录中,保留1个月的备份

   ```shell
   crontab -e
   0 0 * * * sh /usr/local/redis/copy/redis_rdb_copy_daily.sh
   vi redis_rdb_copy_daily.sh
   #!/bin/sh 
   cur_date=`date +%Y%m%d`
   rm -rf /usr/local/redis/snapshotting/$cur_date
   mkdir /usr/local/redis/snapshotting/$cur_date
   cp /var/redis/6379/dump.rdb /usr/local/redis/snapshotting/$cur_date
   del_date=`date -d -1month +%Y%m%d`
   rm -rf /usr/local/redis/snapshotting/$del_date
   ```

4. 每次复制备份时,都把太久的删掉

5. 每天晚上定时将当前服务器上所有数据备份,发送一份到远程服务器上



## 还原

1. 若redis进程挂掉,那么重启redis进程即可,直接基于RDB或AOF日志文件恢复数据
2. 若redis进程所在机器挂掉,那么重启机器后,尝试重启redis,尝试基于AOF日志文件进行恢复,若AOF文件破损,那么用redis-check-aof fix
3. 若redis最新的AOF和RDB文件出现了丢失/损坏,可以尝试基于该机器上当前的某个最新的RDB数据副本进行数据恢复
   1. 当前最新的AOF和RDB文件都出现了丢失/损坏到无法恢复,找到RDB最新的一份备份,小时级的备份可以了,小时级的肯定是最新的,copy到redis里面去,就可以恢复到某一个小时的数据
   2. 由于appendonly已损坏,若复制了RDB文件之后就启动redis,会发现自动生成的appendonly是没有数据的,因为redis优先使用appendonly的数据,发现没有,会直接新建一个appendonly文件,同时rdb会基于内存生成一份新的快照,导致2个文件中都没有数据,所以**不可以直接启动redis**
   3. 先停止redis,删除appendonly.aof,暂时在配置中关闭aof,然后拷贝一份rdb过来,再重启redis,此时数据可恢复
   4. 确认数据恢复后,直接在redis的命令模式中热修改redis配置,开启aof:**config set appendonly yes**,此时redis就会将内存中的数据对应的日志,写入aof文件中
   5. 热修改配置参数时,配置文件中的实际参数并没有被修改,停止redis后修改配置文件,打开aof,重启redis
4. 如果当前机器上的所有RDB文件全部损坏,那么从远程的云服务上拉取最新的RDB快照回来恢复数据
5. 如果是发现有重大的数据错误,比如某个小时上线的程序一下子将数据全部污染了,数据全错了,那么可以选择某个更早的时间点,对数据进行恢复



## 瓶颈

1. redis不能支撑高并发的瓶颈是单机,一般单机的redis集合不太可能QPS超过10W
2. 读写分离,因为写比较少,但是消耗时间.读很多,也比较快



# 主从

> redis replication -> 主从架构 -> 读写分离 -> 水平扩容支撑读高并发



## 复制流程

* 开启主从复制,只需要在当作slave的redis配置文件中添加配置即可
  * replicaof:打开注释,只需要将master的ip和port填进去即可.5.0以前是slaveof
  * masterauth:若master开启了密码验证,此处需要填写master的登录密码
  * requirepass:连接当前redis需要的密码
  * auth pwd:当用redis-cli连接redis时,若开启了密码验证,则需要使用该命令,接上密码才能登录
  * replica-read-only:yes,默认开启了只读,该配置只有在打开了主从模式时才有效
  * 在同一台机器上开多个redis实例时,还需要修改以下配置
    * port
    * pidfile
    * logfile
    * dir
* 当启动一个slave的时候,它会发送一个PSYNC命令给master
* 如果是slave第一次连接master,那么会触发一次全量复制(full resynchronization)
* 如果是slave重新连接master,那么master仅仅会复制给slave部分缺少的数据
* 开始全量复制时,master会启动一个后台进程生成一份RDB快照,同时还会将从客户端收到的所有写命令缓存在内存中
* RDB文件生成完之后,master会将这个RDB发送给slave,slave会先写入本地磁盘,然后再从本地磁盘加载到内存中,然后master会将内存中缓存的写命令发送给slave,slave也会同步这些数据
* slave如果跟master有网络故障,断开了连接,会自动重连
* master如果发现有多个slave都来重新连接,仅仅会启动一个rdb save操作,用一份数据服务所有slave
* 如果主从复制过程中,网络断开,那么可以接着上次复制的地方继续复制,而不是从头开始复制
  * redis会在内存中创建一个backlog,master和slave都会保存一个replica offset和master id,offset在backlog中
  * 如果master和slave网络连接断开,slave会让master从上次的replica offset开始继续复制,如果没有找到对应的offset,就会执行一次full resynchronization
* 采用异步方式复制数据到slave节点,同时slave会周期性地确认自己每次复制的数据量
* 无磁盘化复制
  * master在内存中直接创建rdb,通过内部的socket方式发送给slave,不会在本地磁盘落地
  * repl-diskless-sync:是否开启无磁盘化,默认是no,不开启
  * repl-diskless-sync-delay:等待一定时长再开始复制,因为要等更多slave重新连接过来
* 一个master可以配置多个slave,slave也可以连接其他slave
* slave做复制的时候,是不会打断master的正常工作的
* slave在做复制的时候,也不会打断对自己的查询操作,它会用旧的数据集来提供服务.但是复制完成之后,需要删除旧数据集,加载新数据集,这个时候就会暂停对外服务
* slave主要用来进行横向扩容,做读写分离,扩容的slave可以提高读的吞吐量
* slave不会过期key,只会等待master过期key.如果master过期了一个key,或通过LRU淘汰了一个key,就会模拟一条del命令发送给slave
* master必须开启持久化,否则一旦发生故障,可能造成所有数据丢失



## 核心流程

* slave启动,只保存master的信息,包括host和ip,在slave的redis.conf里的slaveof配置,但是复制流程没开始
* slave内部有个定时任务,每秒检查是否有新的master要连接和复制,如果发现,就跟master建立socket连接
* slave发送ping命令给master
* 口令认证,如果master设置了requirepass,那么salve必须发送masterauth的口令过去进行认证
* master第一次执行全量复制,将所有数据发给slave
* master后续持续将写命令,异步复制给slave



## 数据同步

* 指的就是第一次slave连接msater的时候,执行的全量复制
* offset
  * master和slave都会在自身不断累加offset
  * slave每秒都会上报自己的offset给master,同时master也会保存每个slave的offset
  * offset不仅用于全量复制,主要是master和slave都要知道各自的offset,才能知道主从的数据不一致的情况
* backlog
  * master有一个backlog,默认是1MB大小
  * master给slave复制数据时,也会将数据在backlog中同步写一份
  * backlog主要是用来做全量复制中断时的增量复制
* master run id
  * info server,可以看到master run id
  * 根据host+ip定位master是不靠谱的,如果master重启或者数据出现了变化,那么slave应该根据不同的run id区分,run id不同就做全量复制
  * 如果需要不更改run id重启redis,可以使用redis-cli debug reload命令
* psync
  * 从节点使用psync从master进行复制,psync runid offset
  * master会根据自身的情况返回响应信息,可能是FULLRESYNC runid offset触发全量复制,可能是CONTINUE触发增量复制



## 全量复制

* master执行bgsave,在本地生成一份rdb快照文件
* master node将rdb快照文件发送给salve node,如果rdb复制时间超过60秒(repl-timeout),那么slave就会认为复制失败,可以适当调节大这个参数
* 对于千兆网卡的机器,一般每秒传输100MB,6G文件,很可能超过60s
* master在生成rdb时,会将所有新的写命令缓存在内存中,在salve保存了rdb之后,再将新的写命令复制给salve node
* client-output-buffer-limit slave 256MB 64MB 60,如果在复制期间,内存缓冲区持续消耗超过64MB,或者一次性超过256MB,那么停止复制,复制失败
* slave接收到rdb之后,清空自己的旧数据,然后重新加载rdb到自己的内存中,同时基于旧的数据版本对外提供服务
* 如果slave开启了AOF,那么会立即执行BGREWRITEAOF,重写AOF
* rdb生成,rdb通过网络拷贝,slave旧数据的清理,slave aof rewrite,很耗费时间
* 如果复制的数据量在4G~6G之间,那么很可能全量复制时间消耗到1分半到2分钟



## 增量复制

* 如果全量复制过程中,master-slave网络连接断掉,那么salve重新连接master时,会触发增量复制
* master直接从自己的backlog中获取部分丢失的数据,发送给slave,默认backlog就是1MB
* msater就是根据slave发送的psync中的offset来从backlog中获取数据的



## heartbeat

* 主从节点互相都会发送heartbeat信息
* master默认每隔10秒发送一次heartbeat,salve每隔1秒发送一个heartbeat



## 异步复制

* master每次接收到写命令之后,现在内部写入数据,然后异步发送给slave



## 相关命令

* info replication:查看复制节点的相关信息
* slaveof ip:port:将当前从Redis的主Redis地址切换成另外一个Redis地址,重新同步数据
* slaveof on one:使当前Redis停止和其他Redis的同步,同时将当前Redis转为主Redis



# 集群(Cluster)

* 多个master节点,每个master节点又带多个slave节点.master节点根据算法来分担所有的数据
* 集群模式下不要做物理的读写分离



## 安装部署



* 安装依赖:`yum install -y gcc-c++ redis-3.3.5.gem ruby  rubygems`
* 下载解压Redis到/app/redis
* 进入Redis目录中进行编译:`make distclean && make`
* 安装redis集群:gem install redis
* 每个master节点应该部署至少2个slave节点,且2个slave节点不能部署在同一台机器上
* 修改配置文件redis.conf
  * 去掉密码
  * cluster-enabled:yes,开启集群模式,打开该配置的注释
  * cluster-config-file:设置当前redis集群的配置文件地址,默认是redis.conf同层的nodes-6379.conf,该文件由redis节点自动生成,非手动修改,只修改地址即可
  * cluster-node-timeout:集群通讯超时时间,默认是15000毫秒,单位是毫秒
  * daemonize:yes,守护进程运行
  * pidfile:pid文件地址,默认为/var/run/redis_6379.pid,该文件是redis集群节点标志,自动生成
  * dir:数据目录,必须是一个目录,默认redis.conf同级目录
  * logfile:日志文件,非目录
  * bind:默认绑定127.0.0.1,根据需求可写多个访问地址,中间用空格隔开
  * appendonly:改为yes,开启AOF持久化功能
* 安装redis-trib.rb,该脚本为启动redis集群的脚本
  * redis-trib.rb add-node redisip1:port1  redisip2:port2:添加集群的master节点
  * redis-trib.rb check redisip1:port1:检查集群状态,可以查看master,slave等节点的id
  * redis-trib.rb reshard redisip1:port:将redis集群的slot部分迁移到redisip1上,redis总共有16384个slot,可以平均分布到每个master上
  * redis-trib.rb add-node --slave --master-id master的id  slaveip1:port1 slaveip2:port2:添加slave节点
  * 节点删除
    * redis-trib.rb reshard 需要删除的节点ip:port --> 其他master节点:清空节点上的slot
    * redis-trib.rb del-node 需要删除的节点ip:port 需要删除的节点id:删除节点
    * 当清空了某个master上的slot时,cluster会自动将该master的slave挂载到其他master上
* 启动集群:redis-trib.rb create --replicas 1 ip1:port1 ip2:port2.....
  * --replicas num:每个master有个num个slave
* 集群启动之后在,若是在某个master上做写入操作时,根据CRC16算法,若是得到的slot值在当前master,就会直接写入,若是在其他master上,则会报错moved error,使用JAVA API操作不会有这个问题
* 在cluster上读取数据时,需要先readonly,否则报错,每次读取都要readonly,最好是redis-cli -c启动
* cluster模式下,不要手动做读写分离,cluster默认的读写都是在master上
* cluster集群扩容:先用redis-trib.rb的add-node命令添加新的redis节点,之后用reshard命令将部分slot迁移到新的节点上,添加slave节点同样,但是不需要reshard slot
* 查看redis:./redis01/redis-cli -h 127.0.0.1 -p 6381 -c,c必须要加.若是在其中增加了key,会随机存到redis中,而不是一定会存到当前测试的redis中
* 关闭redis,./redis-cli shutdown
* 若是修改了配置文件中的端口,则需要先删除各个集群中的.rdb,nodes.conf,.aof文件,否则启动集群报错



## 相关命令

* cluster info:获取集群的信息
* cluster slots:查看集群信息
* cluster nodes:获取集群当前已知的所有节点,以及这些节点的相关信息
* cluster meet ip port:将ip和port所指定的节点添加到集群中
* cluster forget <node_id>:将指定node_id的节点从集群中移除
* cluster replicate <node_id>:将当前节点设置为node_id节点的从节点
* cluster saveconfig:将节点的配置文件保存到硬盘中
* cluster addslots <slot>...:将一个或多个槽分配给当前节点
* cluster delslots <slot>...:从当前节点移除一个或多个槽
* cluster flushslots:移除分配给当前节点的所有槽
* cluster setslot <slot> node <node_id>:将slot槽分配给node_id指定的节点,如果槽已经分配给另外一个节点,那么先让另外一个节点删除该槽,然后再进行安装
* cluster setslot <slot> migrating <node_id>:将本节点的槽迁移到指定节点中
* cluster setslot <slot> importing <node_id>:从指定节点导入槽到本节点
* cluster setslot <slot> stable:取消对槽的导入或迁移
* cluster keyslot key:计算键key应该被放置在那个槽
* cluster countkeysinslot <slot>:返回槽目前包含的键值对数量
* cluster getkeysinslot <slot> count:返回count个槽中的键



## 核心原理

1. redis cluster之间采用gossip协议进行通信,即不是将所有的集群元数据(故障,节点信息等)存储在某一个单独的节点上,而是每个master上都会存在.当某个master上的数据发生变更时,会和其他master进行通讯,相互之间传递最新的元数据,保持整个集群所有节点的数据完整性

2. goosip协议包含多种信息:ping,pong,meet,fail等

   > meet:某个节点发送meet给新加入节点,让新节点加入到集群中,然后新节点就开始和其他节点进行通讯
   >
   > ping:每个节点都会频繁的给其他节点发送ping信息,其中包括自己的状态和其他需要维护的信息,和其他节点互相交换信息
   >
   > pong:当接收到其他节点的ping信息时,返回自己的ping和meet信息
   >
   > fail:某个节点判断另外一个节点fail之后,就会通知其他节点

3. 10000以上的端口进行相互通讯,通常是16379,每隔一段时间发送ping消息,保证节点的正常运行

4. 如果一个节点认为另外一个节点宕机,那么就是pfail,主观宕机

5. 若超过半数节点认为另外一个节点宕机,那么就是fail,客观宕机,跟哨兵的原理一样,sdown,odown

6. 在cluster-node-timeout内,某个节点没有返回pong,就认为是pfail.之后会在goosip的ping消息中,ping给其他节点,其他节点超半数认为该节点宕机,就会编程fail

7. slave节点根据从master复制数据的offset来设置选举权重,offset越大,权重越大,超过半数的节点投票给某节点时,该节点就自动成为master

8. cluster和sentinel非常类似,若对缓存的要求更高,数据量更大,则用cluster更好

9. cluster会自动将数据进行分片,每个master上放一部分数据

10. cluster会自动进行读写分离,自动进行主备切换,支持多个master的slot分布式存储



## hash slot

* cluster有固定的16384个hash slot,对每个key计算CRC16值,然后对16384取模,可以获取key对应的hash slot
* cluster中每个master都会持有部分slot,比如有3个master,那么可能每个master持有5000多个slot
* slot让node的增加和移除很简单,增加一个master,就将其他master的slot移动部分过去,减少一个master,就将它的slot移动到其他master上去
* 移动slot的成本是非常低的
* 客户端的api,可以对指定的数据,让他们走同一个slot,通过hash tag来实现
* 如果键名中包含{},则用来进行分片计算的有效值是{}中的值.若果没有,则取整个键名



## 移动已分配的Slot

* 假设要迁移123号Slot,从A到B
* 在B上执行`cluster setslot 123 importing A`
* 在A上执行`cluster setslot 123 migrating B`
* 在A上执行`cluster getkeysinslog 123`,获得要返回的数量
* 对上一步获取的每个键执行migrate命令,将其从A迁移到B
* 在集群中每个服务器上执行`cluster setslot 123 node B`



## 集群缺点

* 不支持批量操作的命令,如mget等.因为数据可能在不同的分片节点上,批量操作只能对单个分片有效
* 分片的粒度是键,所以键对应的值不要太大
* 数据备份比较麻烦,节点越大越麻烦.同时恢复起来也很麻烦
* 扩容的处理比较麻烦
* 数据不保证强一致性



# 哨兵(Sentinel)

## 主要功能

1. 集群监控,负责监控master和slave进程是否正常工作
2. 消息通知,如果某个redis实例有故障,那么哨兵负责发送消息作为报警通知给管理员
3. 故障转移,如果master挂掉了,会自动转移到slave上
4. 配置中心,如果故障转移发生了,通知client客户端新的master地址
5. 哨兵本身也是分布式的,作为一个哨兵集群去运行,互相协同工作
6. 故障转移时,判断一个master是否宕机,需要超过半数的哨兵都同意才行,涉及到了分布式选举的问题
7. 即使部分哨兵节点挂掉了,哨兵集群还是能正常工作



## 核心原理

1. 哨兵至少需要3个实例,来保证自己的健壮性,以便进行master故障时的选举.如果哨兵季芹仅仅部署了2个哨兵实例,quorum=1,当master宕机时,哨兵(S1)哨兵(S2)只要有1个哨兵认为master宕机就可以切换,同时S1和S2中会选举一个哨兵进行故障转义,而选举需要哨兵中过半数的实例(majority)运行.如果是部署2个哨兵,一台哨兵刚好在master那台机器上,而master那台机器整体瘫痪了,那么就没有足够的实例来同意选举进行故障转移.所以至少要3个哨兵,而且不能部署在同一台机器
2. 哨兵+redis主从的部署架构,是不会保证数据零丢失的,只能保证redis集群的高可用性
3. 哨兵+redis主从这种复杂的部署架构,需要在测试环境和生产环境,都进行充足的测试和演练
4. master故障的时候,因为选举的问题,可能会在极短时间内redis集群无法提供缓存服务
5. sentinel之间是通过redis的pub/sub系统实现的,每个sentinel会往__sentinel\_\_:hello这个channel里发送一个消息,其他sentinel都可以消费这个消息,并感知其他sentinel的存在
6. 每隔两秒钟,每个sentinel都会往自己监控的某个master+slaves对应的\_\_sentinel\_\_:hello channel里发送一个消息,内容是自己的host,ip和runid还有对这个master的监控配置
7. 每个sentinel也会去监听自己监控的每个master+slaves对应的\_\_sentinel\_\_:hello channel,然后去感知到同样在监听这个master+slaves的其他哨兵的存在
8. 每个哨兵还会跟其他哨兵交换对master的监控配置,互相进行监控配置的同步
9. 哨兵会负责自动纠正slave的一些配置,比如slave如果要成为潜在的master候选人,哨兵会确保slave在复制现有master的数据;如果slave连接到了一个错误的master上,比如故障转移之后,那么哨兵会确保它们连接到正确的master上



## 选举

* 跟master断开连接的时长:如果一个slave跟master断开连接已经超过了down-after-milliseconds的10倍,外加master宕机的时长,那么slave就被认为不适合选举为master.

  (down-after-milliseconds*10)+milliseconds_since_master_is_in_SDOWN_state

* slave优先级:按照slave优先级进行排序,slave priority越低,优先级就越高
* 复制offset:如果slave priority相同,那么看replica offset,哪个slave复制了越多的数据,offset越靠后,优先级就越高
* runid:如果上面两个条件都相同,那么选择一个run id比较小的那个slave



## sdown,odown

1. sdown和odown是2种状态,sdown是主观宕机,即一个sentinel认为master宕机了,就是主观宕机
2. odown是客观宕机,当sentinel中超过半数上认为某个master宕机了,就是客观宕机
3. 当一个哨兵ping另外一个master,超过 了is-master-down-after-milliseconds的值,就认为master主观宕机
4. 当一个sentinel判断了一个master主观宕机后,会通知其他sentinel判断该master是否宕机,若超过半数认为该master宕机,那么sdown就转换为odown



## quorum和majority

1. quorum:至少多少哨兵同意时,才能确认master,或者slave进程挂掉了,或者要启动一个故障转移操作

2. quorum是用来识别故障的,真正执行故障转移的时候,还是要在哨兵集群执行选举

   > 若有5个哨兵,quorum设置了2,那么5个哨兵中的2个都认为master挂掉了,那master就是odown了

> 每次一个哨兵要做主备切换,首先需要quorum数量的哨兵认为odown,然后选举出一个哨兵来做切换,这个哨兵还得得到majority哨兵的授权,才能正式执行切换
>
> 如果quorum < majority,比如5个哨兵,majority就是3,quorum设置为2,那么就3个哨兵授权就可以执行切换
>
> 但是如果quorum >= majority,那么必须quorum数量的哨兵都授权,比如5个哨兵,quorum是5,那么必须5个哨兵都同意授权,才能执行切换



## 数据丢失

* 主备切换的时候,可能会导致数据丢失,因为主从的数据复制是异步的,部分master的数据还没复制到slave
* 脑裂导致的数据丢失:当master因为网络问题,无法和slave正常通讯,但是和client仍然可以正常通讯,此时sentinel会重新选举一个slave作为master,但是client仍然往旧的master中写数据,此时集群里就会有2个master.当旧的master和其他slave之间的通讯恢复时,旧的master会被作为slave挂载到新的master上,而旧master上的数据会被清空,重新从新的master上复制数据
* 解决脑裂和数据丢失问题:
  * min-slaves-to-write 1:最少要一个slave
  * min-slaves-max-lag 10:数据和同步的延迟不能超过10s,即master和slave之间的数据差异不能超过10s
* 一旦slave复制数据和ack延迟太大,就认为可能master宕机后损失的数据太多,那么就拒绝写请求,此时即使出现了脑裂问题,但是由于拒绝了客户端的些请求,旧master和新master的数据仍然是同步的



## 配置sentinel

* 配置文件在redis安装目录下的sentinel.conf,端口默认是26379,只能本地访问
* sentinel monitor mymaster 127.0.0.1 6379 2:mymaster自定义,指定监听的master-slave的名称,后面紧接的是master的ip和端口,最后一个参数是quorum,自定义
* sentinel down-after-milliseconds mymaster 60000:超过多少毫秒跟一个redis实例断了连接,哨兵就可能认为这个redis实例挂了
* sentinel failover-timeout mymaster 180000:执行故障转移的timeout超时时长
* sentinel parallel-syncs mymaster 1:新的master切换之后,同时有多少个slave被切换到去连接新master,重新做同步,数字越低,花费的时间越多.挂载完一批之后再挂载余下的,直到挂载完
* 启动哨兵:
* sentinel相关操作
  * ./src/redis-sentinel:启动sentinel
  * sentinel master mymaster:检查master状态
  * sentinel slaves mymaster:检查slave状态
  * sentinel sentinels mymaster:检查sentinel状态
  * sentinel get-master-addr-by-name mymaster:获得master信息
  * 增加sentinel,会自动发现,只要配置好配置文件,启动sentinel即可
  * 删除哨兵:
    * 停止sentinal进程
    * SENTINEL RESET *,在所有sentinal上执行,清理所有的master状态
    * SENTINEL MASTER mastername,在所有sentinal上执行,查看所有sentinal对数量是否达成了一致



# 压测

redis自己提供的redis-benchmark压测工具,在redis/src下

./redis-benchmark -h localhost -c 10000  -n 10000000  -d 50

> -c:连接数,默认50
> -n: 请求数,默认100000
> -d:set/get时的key/value字节数,默认2个字节

影响QPS的因素:复杂操作,lrange,value很大



# 性能监控

* info []:查看 Redis 服务器的各种信息和统计数值
  * all:所有服务器信息
  * default:默认值,最常见也最重要的一些服务器信息
  * server:服务器本身的信息.比如版本号, 监听端口号,服务器 ID 等等
  * clients:已连接客户端的信息.比如已连接客户端的数量,正在被阻塞的客 户端数量等等
  * memoery:内存信息.比如内存占用数量,使用的内存分配器等等
  * persistence:和RDB以及AOF持久化有关的信息.比如RDB是否正在进行,AOF重写是否正在进行等
  * stats:服务器的统计信息.比如已处理的命令请求数量,每秒钟处理的命令请求数量等等
  * replication:和主从复制有关的信息.比如服务器的角色,主从服务器的连接状态是否正常等等
  * cpu:服务器的系统 CPU 占用量和用户 CPU 占用量
  * commandstats:命令执行的统计信息.比如命令执行的次数,命令耗费的 CPU 时间,执行每个命令耗费的平均 CPU 时间等等
  * cluster:集群功能的相关信息
  * keyspace:和数据库键空间有关的信息.比如数据库的键数量,数据库已经被删除的过期键数量等等

* slowlog get:获取慢日志,可以通过配置文件的slowlog-log-slower-than来设置时间限制,默认是10000微秒,slowlog-max-len来限制记录条数.返回的记录包含四个部分
  * 日志的id
  * 该命令执行的unix时间
  * 该命令消耗的时间,单位微秒
  * 命令和参数
* slowlog len:查看目前已有的慢查询日志数量
* slowlog reset:删除所有慢查询日志
* monitor:监控Redis执行的所有命令,这个命令比较耗性能,仅用在开发调试阶段.格式为`时间戳 [数据库号码 IP地址和端口号] 被执行的命令`  



# 优化



## 通用

* 精简键值名
* 使用管道(pipeline),可以减少客户端和redis的通信次数,降低网络延迟
* 减少存储的冗余数据
* 尽量使用mset来赋值,比set效率高点
* 尽量使用hash来存储对象
* 使用hash时尽量保证每个key下面的键值数目不超过64
* 配置使用ziplist以优化list
  * 如果list的元素个数小于list-max-ziplist-entries且元素值的长度小于list-max-ziplist-value,则可以编码成ziplist类型存储,否则采用Dict存储.Dict实际是Hash Table的一种实现
* 配置使用intset以优化set
  * 当set集合中的元素为整数且元素个数小于set-max-intset-entries时,使用intset数据结构存储,否则转化为Dict结构
* 配置使用ziplist以优化sorted set
  * 当sorted set的元素个数小于zset-max-ziplist-entries且元素值长度小于zset-max-ziplist-value时,它是用ziplist来存储
* 配置使用zipmap以优化hash
  * 当entry数量小于hash-max-ziplist-entries且entry值的长度小于hash-max-ziplist-value时,会用zipmap来编码
  * HashMap的查找和操作的时间复杂度都是O(1),而放弃Hash采用一维存储则是O(n).如果成员数量很少,则影响不大,否则会严重影响性能.所以要权衡好这些值的设置,在时间成本和空间成本上进行权衡
* 一定要设置maxmemory,该参数能保护Redis不会因为使用了过多的物理内存而严重影响性能甚至崩溃
* 排序优化

  * 尽量让要排序的Key存放在一个Server上
    * 如果采用客户端分片,是由client的算法来决定哪个key存在哪个服务器上的,因此可以通过只对key的部分进行hash.比如client如果发现key中包含{},那么只对key中{}包含的内容进行hash
    * 如果采用服务端分片,也可以通过控制key的有效部分,来让这些数据分配到同一个插槽中
  * 尽量减少Sort的集合大小
    * 如果要排序的集合非常大,会消耗很长时间,Redis单线程的,长时间的排序操作会阻塞其它client的请求
    * 解决办法是通过主从复制,将数据复制到多个slave上,然后只在slave上做排序操作,并尽可能的对排序结果缓存



## fork

* RDB和AOF时会产生rdb快照,aof的rewrite,消耗io,主进程fork子进程
* 通常状态下,如果1个G内存数据,fork需要20m左右,一般控制内存在10G以内
* 从info的stats中的latest_fork_usec可以查看最近一个fork的时长



## 阻塞

* redis将数据写入AOF缓冲区需要单个开个线程做fsync操作,每秒一次
* redis每个进行fsync操作时,会检查2次fsync之间的时间间隔,若超过了2秒,写请求就会阻塞
* everysec:最多丢失2秒的数据,若fsync超过2秒,整个redis就会被拖慢
* 优化写入速度,最好用ssd硬盘



## 主从延迟

* 主从复制可能会超时严重,需要进行良好的监控和报警机制
* 在info replication中,可以看到master和slave复制的offset,做一个差值就可以看到对应的延迟
* 如果延迟过多就报警



## 主从复制风暴

* 主从之间,若是slave过多,在进行全量复制时,同样会导致网络带宽被占用,导致延迟
* 尽量使用合适的slave数,若必须挂多个slave,则采用树状结构,slave下再挂slave



## overcommit_memory

* 修改Linux系统内存参数设置,该值为liunx系统内存设置参数,有3个值
  * 0:检查有没有足够内存,若没有的话申请内存失败
  * 1:允许使用内存直到内存用完
  * 2:内存地址空间不能超过swap+50%
* cat /proc/sys/vm/overcommit_memory,默认是0



## swappiness

* 查看内核版本:cat /proc/version

* 如果版本小于3.5,那么swappiness设置为0,表示系统宁愿swap也不会kill进程

* 如果版本大于3.5,那么swappiness设置为1,表示系统宁愿swap也不会kill进程

* 如此设置可以保证redis不会被进行kill

  ```shell
  echo vm.swapiness=0 >> /etc/sysctl.conf
  echo 0 > /proc/sys/vm/swappiness
  ```

* 打开最大文件句柄

  ```shell
  ulimit -Sn 10032 10032
  ```

* 设置tcp backlog

  ```shell
  cat /proc/sys/net/core/somaxconn
  echo 511 > /proc/sys/net/core/somaxconn
  ```

  



# 其他



## 自启动

* 在redis目录里的utils目录下有个redis_init_script脚本,将该脚本拷贝到/etc/init.d中,并改名,将后缀改为redis的端口号:cp redis_init_script /etc/init.d/redis_6379
* REDISPORT:redis_6379中的变量指定redis运行时的端口号,默认为6379
* EXEC:redis-server的地址,需要指向redis-server所在的目录
* CLIEXEC:redis-cli的地址,需要指向redis-cli所在目录
* PIDFILE:pidfile地址,需要和redis安装目录中的redis.conf中的pidfile地址相同,可不修改,默认都为/var/run/redis_${REDISPORT}.pid
* CONF:redis安装目录中的redis.conf的地址,实际上是redis运行时的具体配置文件地址
* 在redis_6379最上面添加# chkconfig:2345 90 10,另起一行chkconfig  redis_6379 on



## 内置管理工具



### redis-benchmark

* 性能测试工具,测试Redis在你的系统及配置下的读写性能



### redis-check-aof

* 用于修复出问题的AOF文件



### redis-check-dump

* 用于修复出问题的dump.rdb文件



### redis-cli

* 在redis安装目录的src下,执行./redis-cli,可进入redis控制台
* redis-cli -h ip -p port:连接指定ip地址的redis控制台



### redis-sentinel

* Redis集群的管理工具



## 第三方管理工具



### CacheCloud

* 一个管理Redis主从,哨兵,集群的平台





## docker中使用

docker中启动redis

```
docker run -d -p 6379:6379 --requirepass '123456' -v /app/redis/conf/redis.conf:/usr/local/etc/reids/redis.conf -v /app/redis/data:/data --name redis-single redis redis-server /usr/local/etc/redis/redis.conf --appendonly yes --restart=always
```

* --requirepass:使用密码进入redis-cli
* -p localport:dockerport:将docker中的端口映射到本地端口
* -v /localdir:/dockerdir:将docker中的目录映射到本地的目录中
* --name:容器的名称,自定义
* redis:镜像的名称,若不是最新版本的redis,需要加上版本号,如redis:4.0.1
* --appendonly:开启AOF
* --restart=always:总是随着docker的启动而启动



# Lua脚本



* 使用脚本的好处
  * 减少网络开销
  * 原子操作:Redis会把脚本当作一个整体来执行,中间不会插入其它命令
  * 复用功能
* 在Redis脚本中不允许使用全局变量,以防止脚本之间相互影响
* Redis脚本中不能使用Lua的模块化功能



## Lua标准库

* Lua的标准库提供了很多使用的功能,Redis支持其中大部分
* Base:提供一些基础函数
* String:提供用于操作字符串的函数
* Table:提供用于表操作的函数
* Math:提供数据计算的函数
* Debug:提供用于调试的函数



## Redis常用函数

* string.len(string):字符串长度
* string.lower(string)/string.upper(string):字符串转为小写/大写
* string.rep(s, n):返回重复s字符串n次的字符串
* string.sub(string,start[,end]),索引从1开始,-1表示最后一个
* string.char(n…):把数字转换成字符
* string.byte (s [, i [, j]]):用于把字符串转换成数字
* string.find (s, pattern [, init [, plain]]):查找目标模板在给定字符串中出现的位置,找到返回起始和结束位置,没找到返回nil
* string.gsub (s, pattern, repl [, n]):将所有符合匹配模式的地方都替换成替代字符串,并返回替换后的字符串,以及替换次数.四个参数:给定字符串,匹配模式,替代字符串和要替换的次数
* string.match (s, pattern [, init]):将返回第一个出现在给定字符串中的匹配字符串,基本的模式有:
  * .:所有字符
  * %a:字母
  * %c:控制字符
  * %d:数字
  * %l:小写字母
  * %p:标点符号字符
  * %s:空格
  * %u:大写字母
  * %w:文字数字字符
  * %x:16进制数字等
* string.reverse (s):逆序输出字符串
* string.gmatch (s, pattern):返回一个迭代器,用于迭代所有出现在给定字符串中的匹配字符串
* table.concat(table[,sep[,i[,j]]]):将数组转换成字符串,以sep指定的字符串分割,默认是空,i和j用来限制要转换的表索引的范围,默认是1和表的长度,不支持负索引
* table.insert(table,[pos,]value):向数组中插入元素,pos为指定插入的索引,默认是数组长度加1,会将索引后面的元素顺序后移
* table.remove(table[,pos]):从数组中弹出一个元素,也就是删除这个元素,将后面的元素前移,返回删除的元素值,默认pos是数组的长度
* table.sort(table[,sortFunction]):对数组进行排序,可以自定义排序函数
* Math库里面常见的:abs、ceil、floor、max、min、pow、sqrt、sin、cos、tan等
* math.random([m[,n]]):获取随机数,如果是同一个种子的话,每次获得的随机数是一样的,没有参数,返回0-1的小数;只有m,返回1-m的整数;设置了m和n,返回m-n的整数
* math.randomseed(x):设置生成随机数的种子



## 其它库

* 除了标准库外,Redis还会自动加载cjson和cmsgpack库,以提供对Json和MessagePack的支持,在脚本中分别通过cjson和cmsgpack两个全局变量来访问相应功能
* cjson.encode(表):把表序列化成字符串
* cjson.decode(string):把字符串还原成为表
* cmsgpack.pack(表):把表序列化成字符串
* cmsgpack.unpack(字符串):把字符串还原成为表  



## Lua中调用Redis

* redis.call:在脚本中调用Redis命令,遇到错误会直接返回
* redis.pcall:在脚本中调用Redis命令,遇到错误会记录错误并继续执行



## Lua和Redis返回值类型对应

* 数字——整数
* 字符串——字符串
* 表类型——多行字符串
* 表类型(只有一个ok字段存储状态信息)——状态回复
* 表类型(只有一个err字段存储错误信息)——错误回复



## 相关脚本命令

### eval

* 在Redis中执行脚本
* eval 脚本内容 key参数数量 [key…] [arg…]:通过key和arg两类参数来向脚本传递数据,在脚本中分别用KEYS[index]和ARGV[index]来获取,index从1开始
* 对于KEYS和ARGV的使用并不是强制的,也可以不从KEYS去获取键,而是在脚本中硬编码,但是这种写法无法兼容集群



### evalsha

* 可以通过脚本摘要来运行,其他同eval.执行的时候会根据摘要去找缓存的脚本,找到了就执行,否则返回错误



### script load

* 将脚本加入缓存,返回值就是SHA1摘要



### script exists

* 判断脚本是否已经缓存



### script flush

* 清空脚本缓存



### script kill

* 强制终止脚本的执行,如果脚本中修改了某些数据,那么不会终止脚本的执行,以保证脚本执行的原子性
  

## 沙箱

* 为了保证Redis服务器的安全,并且要确保脚本的执行结果只和脚本执行时传递的参数有关,Redis禁止脚本中使用操作文件或系统调用相关的函数,脚本中只能对Redis数据进行操作
* Redis会禁用脚本的全局变量,以保证脚本之间是隔离的,互不相干的



## 随机数和随机结果的处理

* 为了确保执行结果可以重现,Redis对随机数的功能进行了处理,以保证每次执行脚本生成的随机数列都相同
* Redis还对产生随机结果进行了处理,比如smembers或hkeys等,数据都是无序的,Redis会对结果按照字典进行顺序排序
* 对于会产生随机结果但无法排序的命令,Redis会在这类命令执行后,把该脚本标记为lua_random_dirty,此后只允许读命令,不可改,否则返回错误.这类Redis命令有:spop,srandmember,randomkey,time



# 缓存穿透



* 高并发下去查询一个没有的数据,缓存和数据库中都没有该值,此时就会造成缓存穿透
* 为避免这种情况可以在缓存中存null或一个特定的值表示该值不存在,同时设置较短过期时间
* 若对准确率要求不高,可以使用布隆过滤器,但是有失败率



# 缓存雪崩



* 大量相同过期时间的key同时过期或缓存服务器崩溃,造成请求全部转到数据库,数据库压力过大而崩溃
* 在原有的过期时间上增加一个随机值,这样每个缓存的过期时间重复率就会降低,就很难引发缓存集体失效
* 加上本地缓存ehcache以及降级组件(hystrix或sentinel),先走流量降级,再走本地ehcache,最后走redis



# 缓存击穿



* 对于一些设置了过期时间的key,如果这些key可能会在某些时间点被超高并发地访问,说明这些数据是非常热点的数据
* 如果这个key在大量请求同时进来前正好失效,那么所有对这个key的数据查询都将到数据库,此时就会造成缓存击穿
* 加锁可以解决该问,大量并发只让一个去查,其他人等待,查到后释放锁,其他请求获得锁,再次从缓存中查询数据,此时就会有数据,不用去数据库查询



# 缓存一致性



* 数据库和redis中缓存不一致,先删缓存,再修改数据库
* 若是先修改数据库,再删缓存,当缓存删除失败时,会造成数据不一致问题
* 先删缓存,再更新数据库,即使数据库更新失败,redis中无缓存,拿到的只有数据库的数据,不存在不一致问题
* 在redis中修改消耗的性能要稍高于删除
* 缓存删除之后,需要修改数据库,而此时又来了查询该数据的请求,redis中没有,去查数据库,而数据库的该数据仍然是原数据,此时刚好修改的请求已经完成,将新的数据写入缓存中.之后查询的请求也完成了,再次写入数据,此时缓存中的数据仍然是旧数据,此时可以使用加锁或队列来完成操作,请求放入队列中完成
* 若缓存对业务影响不高,如商品介绍,菜单修改等,可以通过添加缓存过期时间来减少数据一致性问题
* 使用加锁的机制保证数据的一致性,会稍微降低程序的性能,若不经常变更的数据,不建议存缓存



## Canal

* [官网](https://github.com/alibaba/canal)

* 阿里开源的缓存数据一致性解决方案,可以伪装成MySQL等,监听数据库的修改,更新缓存



# 无底洞



* 增加了机器,但是已经到了极限,再增加机器也缓解不了缓存的压力
* 尽量少使用keys,hgetall bigkey等操作
* 降低接入成本,例如NIO,客户端长连接



# 缓存热点



* 减少重缓存的次数
* 数据尽可能一致
* 减少潜在危险



# 分布式锁



## 自定义分布式锁

* 在redis中使用setnx存储一个值,setnx是一个原子操作,多线程同时只有一成功.需要设置过期时间

  * 当线程拿到锁之后,完成了其他操作,此时需要删除锁,否则其他线程永远拿不到锁
  * 若删除锁时失败了,则过期时间的指定就能防止死锁问题,锁自动失效

* setnx的值需要是一个uuid

  * 当线程拿到锁之后并完成操作需要删除锁时,若是锁已经过期,则删除的就是其他线程的锁
  * 删除之前需要先拿到锁进行比对,确定是自己的锁才能删除,所以setnx的值必须是不同的

* 上述操作仍然会有一个问题:即因为网络问题,当前线程从redis中拿到的锁的值是对的,但是锁刚好过期,另外一个线程同时又设置了锁,此时删除的锁是另外一个线程的锁,此时仍然会有2个线程同时运行,若是在并发量不高的情况下,允许该操作,则锁的功能全部完成

* 若同时只能有一个线程进行操作,则获取锁进行到锁的值进行比对,到最后删除锁,整个过程必须是原子操作,redis官网中推荐使用Lua脚本进行操作,详见[官网](http://www.redis.cn/commands/set.html)

* 具体的脚本如下:

  ```lua
  if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end
  ```

  * 其中KEYS[1]表示传参的key值,ARGV[1]表示需要进行比对的值
  * 若删除成功,返回1;若删除失败,返回0.java中0和1返回的都是long类型

* 在Java中的使用

  ```java
  public Map<String,Object> getDataUseRedisLock() {
      // 生成的随机uuid,避免删除锁时删除其他线程的锁
      String token = CryptoUtils.UUID();
      // redis占分布式锁,使用setnx命令,key可自定义,随意起名,过期时间根据需求而定
      Boolean setIfAbsent = redisTemplate.opsForValue().setIfAbsent("lock", token, 10, TimeUnit.SECONDS);
      if (setIfAbsent) {
          Map<String,Object> result= null;
          try {
              // 占锁成功,进行业务操作
              // dosomething
          } finally {
              // 利用redis的脚本功能执行删除的操作
              String script = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
              // 该操作可以返回操作是否成功,1成功,0失败
              redisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class), Arrays.asList("lock"),token);
          }
          return result;
      } else {
          // 占锁失败,自旋,必须要休眠一定时间,否则对cpu消耗极大,且容易抛异常
          try {
              TimeUnit.MICROSECONDS.sleep(200);
              // 再次调用自己进行操作
              return getDataUseRedisLock();
          } catch (InterruptedException e) {
              e.printStackTrace();
              log.error("占锁等待失败:" + e.getMessage());
          }
      }
  }
  ```

  



## Redisson

* [官网](https://github.com/redisson/redisson/)

* Redisson是redis对分布式锁的封装,需要添加相关依赖,JDK8以上才可使用

* 如何使用分布式锁可参照[官方文档](https://github.com/redisson/redisson/wiki/8.-%E5%88%86%E5%B8%83%E5%BC%8F%E9%94%81%E5%92%8C%E5%90%8C%E6%AD%A5%E5%99%A8)

* Java示例

  ```java
  @Autowired
  private RedissonClient redissonClient;
  
  public void test() throws Exception {
      // 获取一把锁,只要锁的名称一样就是同一把锁
      RLock lock = redissonClient.getLock("lock");
      try {
          // 加锁,阻塞等待,默认30秒过期
          // redisson的锁会自动续期,即业务超长时间,不用担心锁过期,默认是续期30秒
          // 若业务完成或线程突然断开,redisson将不会自动续期,即使不手动解锁,锁默认在30秒之后也会自动删除
          // 在拿到锁之后会设置一个定时任务,每10秒刷新一次过期时间,会自动续期,若线程断开,自然无法自动续期
          lock.lock();
          // 自定义过期时间,但是该方法不会自动续期,即业务时间超长锁就会自动删除
          // lock.lock(10, TimeUnit.SECONDS);
          // 加读写锁,读数据时加读锁,写的时候加写锁
          // 写锁存在时,不管其他线程是读或写,都需要等待
          // 读锁存在时,其他线程若是读,则无需等待,若是写,则写需要等待
          RReadWriteLock readWriteLock = redissonClient.getReadWriteLock("rw-lock");
          // 读锁
          RLock readLock = readWriteLock.readLock();
          readLock.lock();
          // dosomething
          readLock.unlock();
          // 写锁
          RLock writeLock = readWriteLock.writeLock();
          writeLock.lock();
          // dosomething
          writeLock.unlock();
          // 信号量,用来限流
          // 参数是redis中的一个key,该key的值必须是一个正整数
          RSemaphore semaphore = redissonClient.getSemaphore("semaphore");
          // 默认获取一个信号量,则该key表示的值将减1
          // 若该key表示的值已经等于0,则无法获取信号量,此时就会阻塞等待
          semaphore.acquire();
          // 一次获取2个信号量
          semaphore.acquire(2);
          // 尝试获取信号量,能获取就返回true,获取不到就返回false
          boolean tryAcquire = semaphore.tryAcquire();
          System.out.println(tryAcquire);
          // 释放信号量,相当于该key的值加1
          semaphore.release();
          // 释放2个信号量
          semaphore.release(2);
      } finally {
          // 解锁
          lock.unlock();
      }
  }
  ```

  



# 并发设置同KEY



* 并发设置同key可以在设置值时传递一个时间戳,时间戳大的覆盖时间戳小的,时间戳小的不覆盖大的