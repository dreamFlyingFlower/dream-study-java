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
  
  ```shell
  # watch必须在事务开启之前使用
  watct name
  multi
  set name test
  # 若在执行exec之前另外一个线程改变了name的值,那么事务全部无效
  exec
  ```



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
* debug reload:服务器运行中重启
* shotdown save:关闭服务器时指定保存数据



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
* 应用于抢购,限购类,限量发放优惠卷,激活码等业务的数据存储设计
* 应用于具有操作先后顺序的数据控制
* 应用于最新消息展示
* 应用于同类信息的关联搜索,二度关联搜索,深度关联搜索
* 应用于基于黑名单与白名单设定的服务控制
* 应用于计数器组合排序功能对应的排名
* 应用于即时任务/消息队列执行管理



## 微博



### 用户账号



#### 帐号唯一性检查



* 使用集合存储所有的帐号,新增用户的同时更新缓存和数据库



#### 用户信息存储



* 使用Map存储,key为用户唯一标识,value可根据情况尽量少存储信息



### 关注和被关注



* 被关注用户的唯一标识作为key,使用集合存储关注用户的唯一标识



### 时间线



* 每条时间线都是一个有序集合,有序集合的元素 为微博的 ID,分值为微博的发布时间
* 用户发送新的微博时,程序就会使用 ZADD 命令,将新微博的 ID 以及发布时间添加到有序集合里



### 点赞



* 同关注和被关注,不过key换成被点赞的消息ID



## String



* 主页高频访问信息显示控制,例如新浪微博大V主页显示粉丝数与微博数量
* 如set `user:id:222111:focus` 123.以表名:主键字段:主键值:表中需要存储字段为key



## Hash



* 电商网站购物车的商品添加,浏览,更改,删除,清空等
  
  * 以客户id作为key,每位客户创建一个hash存储结构存储对应的购物车信息
  
  * 将商品编号作为field,购买数量作为value进行存储
  
  * 添加商品:追加全新的field与value
  
  * 浏览:遍历hash
  
  * 更改数量:自增/自减,设置value值
  
  * 删除商品:删除field
  
  * 清空:删除key
  
  * 每条购物车中的商品记录保存成两条field
    
    * field1专用于保存购买数量
      
      * 命名格式:商品id:nums
      * 保存数据:数值
    
    * field2专用于保存购物车中显示的信息,包含文字描述,图片地址,所属商家信息等
      
      * 命名格式:商品id:info
      * 保存数据:json

* 应用于抢购,限购类,限量发放优惠卷,激活码等业务的数据存储设计
  
  * 以商家id作为key
  * 将参与抢购的商品id作为field
  * 将参与抢购的商品数量作为对应的value
  * 抢购时使用降值的方式控制产品数量



## List



* 微信朋友圈点赞,要求按照点赞顺序显示点赞好友信息,如果取消点赞,移除对应好友信息
* 应用于最新消息展示,如微博中个人用户的关注列表需要按照用户的关注顺序进行展示,粉丝列表需要将最近关注的粉丝列在前面
  * 依赖list的数据具有顺序的特征对信息进行管理
  * 使用队列模型解决多路信息汇总合并的问题
  * 使用栈模型解决最新消息的问题



## Set



* 应用于随机推荐类信息检索,例如热点歌单推荐,热点新闻推荐,应用APP推荐,大V推荐等
  * 随机获取集合中指定数量的数据:srandmember key [count]
  * 随机获取集合中的某个数据并将该数据移出集合:spop key [count]
* 应用于同类信息的关联搜索,二度关联搜索,深度关联搜索
  * 显示共同关注(一度)
  * 显示共同好友(一度)
  * 由用户A出发,获取到好友用户B的好友信息列表(一度)
  * 由用户A出发,获取到好友用户B的购物清单列表(二度)
  * 由用户A出发,获取到好友用户B的游戏充值列表(二度)
  * 求两个集合的交、并、差集
    * sinter key1 [key2]
    * sunion key1 [key2]
    * sdiff key1 [key2]
  * 求两个集合的交、并、差集并存储到指定集合中
    * sinterstore destination key1 [key2]
    * sunionstore destination key1 [key2]
    * sdiffstore destination key1 [key2]
  * 将指定数据从原始集合中移动到目标集合中
    * smove source destination member
* 应用于同类型数据的快速去重
  * 公司对旗下新的网站做推广,统计网站的PV(访问量) ,UV(独立访客) ,IP(独立IP)
    * PV:网站被访问次数,可通过刷新页面提高访问量
    * UV:网站被不同用户访问的次数,可通过cookie统计访问量,相同用户切换IP地址, UV不变
    * IP网站被不同IP地址访问的总次数,可通过IP地址统计访问量,相同IP不同用户访问, IP不变
  * 利用set集合的数据去重特征,记录各种访问数据
  * 建立string类型数据,利用incr统计日访问量(PV)
  * 建立set模型,记录不同cookie数量(UV)
  * 建立set模型,记录不同IP数量(IP)
* 应用于基于黑名单与白名单设定的服务控制
  * 基于经营战略设定问题用户发现、鉴别规则
  * 周期性更新满足规则的用户黑名单,加入set集合
  * 用户行为信息达到后与黑名单进行比对,确认行为去向
  * 黑名单过滤IP地址:应用于开放游客访问权限的信息源
  * 黑名单过滤设备信息:应用于限定访问设备的信息源
  * 黑名单过滤用户:应用于基于访问权限的信息源



## ZSet



* 应用于计数器组合排序功能对应的排名
  * 获取数据对应的索引(排名)
    * zrank key member
    * zrevrank key member
  * score值获取与修改
    * zscore key member
    * zincrby key increment member
* 应用于即时任务/消息队列执行权重管理
  * 对于带有权重的任务,优先处理权重高的任务,采用score记录权重即可
  * 如果权重条件过多时,需要对排序score值进行处理,保障score值能够兼容2条件或者多条件
  * 因score长度受限,需要对数据进行截断处理,尤其是时间设置为小时或分钟级即可
  * 先设定订单类别,后设定订单发起角色类别,整体score长度必须是统一的,不足位补0.第一排序规则首
    位不得是0



## 限时按次结算的服务控制



* 设计计数器,记录调用次数,用于控制业务执行次数.以用户id作为key,使用次数作为value
* 利用incr操作超过最大值抛出异常的形式替代每次判断是否大于最大值
  * 判断是否为nil,如果是,设置为Max-次数
  * 如果不是,计数+1
  * 业务调用失败,计数-1
* 遇到异常即+1操作超过上限,视为使用达到上限
* 为计数器设置生命周期为指定周期,例如1秒/分钟,自动清空周期内使用次数



## 基于时间顺序的数据操作



* 例子:微信消息排序
* 依赖list的数据具有顺序的特征对消息进行管理,将list结构作为栈使用
* 对置顶与普通会话分别创建独立的list分别管理
* 当某个list中接收到用户消息后,将消息发送方的id从list的一侧加入list,此处设定左侧
* 多个相同id发出的消息反复入栈会出现问题,在入栈之前无论是否具有当前id对应的消息,先删除对应id
* 推送消息时先推送置顶会话list,再推送普通会话list,推送完成的list清除所有数据
* 消息的数量,也就是微信用户对话数量采用计数器的思想另行记录,伴随list操作同步更新



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
* Redis 会在不同的地方使用不同的散列算法,其中最常用的是 MurmurHash2 算法
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
* 不存储元素值,只存储存储元素之后的基数计算结果
* 算法给出的基数并不是精确的,误差范围是一个带有 0.81% 标准错误的近似值
* 耗空间极小,每个hyperloglog key占用了12K的内存用于标记基数
* pfadd命令不是一次性分配12K内存使用,会随着基数的增加内存逐渐增大
* Pfmerge命令合并后占用的存储空间为12K,无论合并之前数据量多少  



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



## 定时删除



* 创建一个定时器,当key设置有过期时间,且过期时间到达时,由定时器任务立即执行对键的删除操作
* 节约内存,到时就删除,快速释放掉不必要的内存占用
* CPU压力很大,无论CPU此时负载量多高,均占用CPU,会影响redis服务器响应时间和指令吞吐量



## 惰性删除



* 数据到达过期时间,不做处理,等下次访问该数据时
  * 如果未过期,返回数据
  * 发现已过期,删除,返回不存在
* 节约CPU性能,发现必须删除的时候才删除
* 内存压力很大,出现长期占用内存的数据



## 定期删除



* Redis启动服务器初始化时,读取配置server.hz的值,默认为10
* 每秒钟执行server.hz次serverCron()->databasesCron()->activeExpireCycle()
* activeExpireCycle()对每个expires[*]逐一进行检测,每次执行250ms/server.hz
* 对某个expires[*]检测时,随机挑选W个key检测
  * 如果key超时,删除key
  * 如果一轮中删除的key的数量>W*25%,循环该过程
  * 如果一轮中删除的key的数量≤W*25%,检查下一个expires[\*],0-15循环
  * W取值=ACTIVE_EXPIRE_CYCLE_LOOKUPS_PER_LOOP属性值
* 参数current_db用于记录activeExpireCycle() 进入哪个expires[*] 执行
* 如果activeExpireCycle()执行时间到期,下次从current_db继续向下执行
* 周期性轮询redis库中的时效性数据,采用随机抽取的策略,利用过期数据占比的方式控制删除频度
* CPU性能占用设置有峰值,检测频度可自定义设置
* 内存压力不是很大,长期占用内存的冷数据会被持续清理



## 逐出算法



* Redis使用内存存储数据,在执行每一个命令前,会调用freeMemoryIfNeeded()检测内存是否充足.如果内存不满足新加入数据的最低存储要求,redis要临时删除一些数据为当前指令清理存储空间.清理数据的策略称为逐出算法
* 逐出数据的过程不是100%能够清理出足够的可使用的内存空间,如果不成功则反复执行.当对所有数据尝试完毕后,如果不能达到内存清理的要求,将抛出异常`(error) OOM command not allowed when used memory >'maxmemory`
* 相关配置
  * maxmemory:最大可使用内存.占用物理内存的比例,默认值为0,表示不限制.生产环境中根据需求设定,通常设置在50%以上
  * maxmemory-samples:每次选取待删除数据的个数.选取数据时并不会全库扫描,导致严重的性能消耗,降低读写性能,因此采用随机获取数据的方式作为待检测删除数据
  * maxmemory-policy:删除策略.达到最大内存后的,对被挑选出来的数据进行删除的策略
  * 检测易失数据,可能会过期的数据集server.db[i].expires
    * volatile-lru:挑选最近最少使用的数据淘汰
    * volatile-lfu:挑选最近使用次数最少的数据淘汰
    * volatile-ttl:挑选将要过期的数据淘汰
    * volatile-random:任意选择数据淘汰
  * 检测全库数据,所有数据集server.db[i].dict
    * allkeys-lru:挑选最近最少使用的数据淘汰
    * allkeys-lfu:挑选最近使用次数最少的数据淘汰
    * allkeys-random:任意选择数据淘汰
  * 放弃数据驱逐
    * no-enviction:禁止驱逐数据,会引发错误OOM



# 持久化



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
* 白名单策略
  * 提前预热各种分类数据id对应的bitmaps, id作为bitmaps的offset,相当于设置了数据白名单.当加载正常数据时,放行,加载异常数据时直接拦截,此法效率偏低
  * 使用布隆过滤器

* 实施监控redis命中率(业务正常范围时,通常会有一个波动值)与null数据的占比
  * 非活动时段波动:通常检测3-5倍,超过5倍纳入重点排查对象
  * 活动时段波动:通常检测10-50倍, 超过50倍纳入重点排查对象
  * 根据倍数不同,启动不同的排查流程,然后使用黑名单进行防控

* key加密:问题出现后,临时启动防灾业务key,对key进行业务层传输加密服务,设定校验程序,过来的key校验.例如每天随机分配60个加密串,挑选2到3个,混淆到页面数据id中,发现访问key不满足规则,驳回数据访问



# 缓存雪崩



* 大量相同过期时间的key同时过期或缓存服务器崩溃,造成请求全部转到数据库,数据库压力过大而崩溃
* 在原有的过期时间上增加一个随机值,这样每个缓存的过期时间重复率就会降低,就很难引发缓存集体失效
* 加上本地缓存ehcache以及降级组件(hystrix或sentinel),先走流量降级,再走本地ehcache,最后走redis
* 超热数据使用永久key
* 定期维护:自动+人工.对即将过期数据做访问量分析,确认是否延时,配合访问量统计,做热点数据的延时  



# 缓存击穿



* 对于一些设置了过期时间的key,如果这些key可能会在某些时间点被超高并发地访问,说明这些数据是非常热点的数据
* 如果这个key在大量请求同时进来前正好失效,那么所有对这个key的数据查询都将到数据库,此时就会造成缓存击穿
* 加锁
* 二级缓存:设置不同的失效时间,保障不会被同时淘汰就行
* 后台刷新数据:启动定时任务,高峰期来临之前,刷新数据有效期,确保不丢失  



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



# 缓存预热



* 在请求量极大或主从之间数据吞吐量较大,数据同步操作频度较高时,出现服务器宕机
* 日常例行统计数据访问记录,统计访问频度较高的热点数据
* 利用LRU数据删除策略,构建数据留存队列.如storm与kafka配合
* 将统计结果中的数据分类,根据级别, redis优先加载级别较高的热点数据
* 利用分布式多服务器同时进行数据读取, 提速数据加载过程
* 热点数据主从同时预热
* 使用脚本程序固定触发数据预热过程
* 如果条件允许, 使用CDN



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