# Redis命令

# Key(键)

## DEL

* del key [key ...]:删除指定的一个或多个key,不存在的 key 会被忽略
* 时间复杂度:O(N),N为被删除的 key 的数量
* 删除单个字符串类型的key,时间复杂度为O(1)
* 删除单个列表,集合,有序集合或哈希表类型的 key,时间复杂度为O(N),N为数据结构内的元素数量
* 返回值:被删除 key 的数量,删除失败返回0

## KEYS

* keys pattern:查找所有符合指定模式 pattern 的 key,特殊符号用\隔开.KEYS的速度非常快,但在一个大的数据库中使用它仍然可能造成性能问题,可以用 Redis 的集合结构(set)来代替特定情况
* `KEYS *`:匹配数据库中所有 key
* `KEYS h?llo`:匹配 hello , hallo 和 hxllo 等
* `KEYS h*llo`: 匹配 hllo 和 heeeeello 等
* `KEYS h[ae]llo`:匹配 hello 和 hallo ,但不匹配 hillo 
* 时间复杂度:O(N), N 为数据库中 key 的数量
* 返回值:符合指定模式的 key 列表

## RANDOMKEY

* randomkey:当前数据库中随机返回(不删除)一个 key
* 时间复杂度:O(1)
* 返回值:当数据库不为空时,返回一个 key;当数据库为空时,返回 nil

## TTL

* ttl key:以秒为单位,返回指定 key 的剩余生存时间(TTL, time to live)
* 时间复杂度:O(1)
* 返回值:key不存在时,返回-2;key为永久存在时,返回-1;否则,以秒为单位返回剩余过期时间

## PTTL

* pttl key:类似于 TTL,但它以毫秒为单位返回 key 的剩余生存时间
* 时间复杂度:O(1)
* 返回值:key不存在时,返回-2;key为永久存在时,返回-1;否则,以毫秒为单位返回剩余过期时间

## EXISTS

* exists key:检查指定 key 是否存在
* 时间复杂度:O(1)
* 返回值:若 key 存在,返回 1 ,否则返回 0

## MOVE

* move key db:将当前数据库的key移动到指定的数据库db中.若当前数据库和指定数据库有同名key,或key不存在于当前数据库时,MOVE无效.可以利用这一特性,将MOVE当作锁(locking)原语(primitive)
* 时间复杂度:O(1)
* 返回值:移动成功返回 1 ,失败则返回 0

## RENAME

* rename key newkey:将key改名为newkey.当key和newkey相同,或key不存在时,返回一个错误;当 newkey已经存在时,原key指向的value将覆盖newkey指向的value
* 时间复杂度:O(1)
* 返回值:改名成功时提示 OK ,失败时候返回一个错误

## RENAMENX

* renamenx key newkey:只有当newkey不存在时,将key改名为newkey.当key不存在时,返回错误
* 时间复杂度:O(1)
* 返回值:修改成功时,返回 1;如果 newkey 已经存在,返回 0

## TYPE

* type key:返回 key 所储存的值的类型
* 时间复杂度:O(1)
* 返回值:none (key 不存在);string (字符串);list (列表);set (集合);zset (有序集);hash (哈希表)

## EXPIRE

* expire key seconds:为指定 key 设置生存时间,当 key 过期时(生存时间为0),会被自动删除.
  * 生存时间可以通过使用DEL命令来删除整个 key 来移除,或者被 SET 和 GETSET 命令覆写(overwrite)
  * 如果一个命令只是修改(alter)一个带生存时间的 key 的值而不是用一个新的 key 值来代替(replace)它的话,那么生存时间不会被改变
  * 对一个 key 执行 INCR 命令,对一个列表进行 LPUSH 命令,或者对一个哈希表执行 HSET 命令,这类操作都不会修改 key 本身的生存时间
  * 如果使用 RENAME 对一个 key 进行改名,那么改名后的 key 的生存时间和改名前一样
  * RENAME 命令的另一种可能是,尝试将一个带生存时间的 key 改名成另一个带生存时间的 another_key ,这时旧的 another_key (以及它的生存时间)会被删除,然后旧的 key 会改名为 another_key ,因此,新的 another_key 的生存时间也和原本的 key 一样
  * 使用 PERSIST 命令可以在不删除 key 的情况下,移除 key 的生存时间,让 key 重新成为一个持久化(persistent) key
* 时间复杂度:O(1)
* 返回值:设置成功返回 1;当key不存在或不能为key设置生存时间时,返回0

### 更新生存时间

* 可以对一个已经带有生存时间的 key 执行 EXPIRE 命令,新指定的生存时间会取代旧的生存时间

### 过期时间的精确度

* 就算 key 已经过期,但它还是可能在过期之后1MS之内被访问到

### 应用场景

#### 导航会话

* 假设有一项服务打算根据用户最近访问的N个页面来进行物品推荐,并且假设用户停止阅览超过60秒,那么就清空阅览记录(为了减少物品推荐的计算量,并且保持推荐物品的新鲜度).这些最近访问的页面记录,称之为导航会话(Navigation session),可以用INCR和RPUSH在Redis中实现:每当用户阅览一个网页的时候,执行以下代码
  
  ```shell
  MULTI
  RPUSH pagewviews.user:<userid> http://.....
  EXPIRE pagewviews.user:<userid> 60
  EXEC
  ```

* 如果用户停止阅览超过 60 秒,那么它的导航会话就会被清空,当用户重新开始阅览的时候,系统又会重新记录导航会话,继续进行物品推荐

## PEXPIRE

* pexpire key milliseconds:和EXPIRE作用类似,但是它以毫秒为单位设置 key 的生存时间
* 时间复杂度:O(1)
* 返回值:设置成功,返回 1;key 不存在或设置失败,返回 0

## EXPIREAT

* expireat key timestamp:作用和EXPIRE 类似,但EXPIREAT接受的时间参数是UNIX时间戳
* 时间复杂度:O(1)
* 返回值:如果生存时间设置成功,返回 1;当 key 不存在或没办法设置生存时间,返回 0

## PEXPIREAT

* pexpireat key milliseconds:和EXPIREAT类似,但它以毫秒为单位设置 key 的过期 unix 时间戳
* 时间复杂度:O(1)
* 返回值:如果生存时间设置成功,返回 1;当 key 不存在或没办法设置生存时间时,返回 0

## PERSIST

* persist key:将指定key转换为一个不带生存时间,永不过期的 key
* 时间复杂度:O(1)
* 返回值:当生存时间移除成功时,返回 1;如果 key 不存在或 key 没有设置生存时间,返回 0

## SORT

* `sort key [BY pattern] [LIMIT offset count] [GET pattern [GET pattern ...]]
  [ASC | DESC] [ALPHA] [STORE destination]`:返回或保存指定列表,集合,有序集合 key 中经过排序的元素,排序默认以数字作为对象,值被解释为双精度浮点数,然后进行比较
  * 最简单的SORT使用方法是 SORT key.假设test是一个保存数字的列表,SORT 命令默认会返回该列表值的递增(从小到大)排序结果
  * 当数据集中保存的是字符串值时,可以用 ALPHA进行排序,如`SORT test ALPHA`
  * 如果正确设置了 !LC_COLLATE 环境变量的话, Redis 能识别 UTF-8 编码
  * 排序之后返回的元素数量可以通过 LIMIT 修饰符进行限制,接受两个参数: offset 和 count
    * offset:指定要跳过的元素数量,为0时表示没有元素被跳过
    * count 指定跳过 offset 个指定的元素之后,要返回多少个对象
  * 修饰符可以组合使用,如`SORT test LIMIT 0 5 DESC`
* 时间复杂度:O(N+M*log(M)),N为要排序的列表或集合内的元素数量,M为要返回的元素数量.如果只是使用 SORT 命令的 GET 选项获取数据而没有进行排序,时间复杂度 O(N)
* 返回值:没有使用 STORE 参数,返回列表形式的排序结果;使用 STORE 参数,返回排序结果的元素数量

### 使用外部 key 进行排序

* 如按level从大到小排序user_id:`SORT user_id BY user_level_* DESC`

* 获得排序后的用户名:`SORT user_id BY user_level_* DESC GET user_name_*`

* 可以多次有序地使用GET操作来获取更多外部 key:
  
  * 获取用户名和密码:`SORT user_id BY user_level_* DESC GET user_name_* GET user_password_*`
  * GET 操作是有序的,`GET user_name_* GET user_password_*`和`GET
    user_password_* GET user_name_*`返回的结果位置不同
  * `GET #`:用于获取被排序对象(user_id )的当前元素

### 只获取对象不排序

* BY修饰符可以将一个不存在的key当作权重,让SORT 跳过排序操作
* 该方法用于希望获取外部对象而又不希望引起排序开销时使用

### 保存排序结果

* 默认情况下,SORT只是简单地返回排序结果,如果希望保存排序结果,可以给STORE指定一个key作为参数,排序结果将以列表的形式被保存到这个key中(若key已存在,则覆盖)

### 在GET和BY中使用哈希表

* 可以使用哈希表特有的语法,在SORT命令中进行GET和BY操作
* 符号`->`用于分割哈希表的键名(key name)和索引域(hash field),格式为`key->field`
* 除此之外,哈希表的BY和GET操作和上面介绍的其他数据结构(列表,集合,有序集合)没有什么不同  

```shell
# 假设用户表新增了一个serial项来为作为每个用户的序列号,序列号以哈希表的形式保存在serial哈希域
redis> HMSET serial 1 23131283 2 23810573 222 502342349 59230
2435829758
OK
# 用serial中值的大小为根据,对user_id进行排序
redis> SORT user_id BY *->serial
1) "59230
2) "222"
3) "2"
4) "1"
```

## OBJECT

* object subcommand [arguments [arguments]]:从内部察看指定key的对象类型.通常用在除错或了解为了节省空间而对 key 使用特殊编码的情况,当将Redis用作缓存时,也可以通过OBJECT中的信息,决定 key 的驱逐策略(eviction policies)
* OBJECT 命令有多个子命令:
  * OBJECT REFCOUNT <key>:返回指定key引用所储存的值的次数
  * OBJECT ENCODING <key>:返回指定key锁储存的值所使用的内部表示(representation)
  * OBJECT IDLETIME <key>:返回指定 key自储存以来的空转时间(idle, 没有被读取也没有被写入),以秒为单位
* 对象可以以多种方式编码:
  * 字符串可以被编码为raw(一般字符串)或 int (用字符串表示 64 位数字是为了节约空间)
  * 列表可以被编码为 ziplist 或 linkedlist.ziplist是为节约大小较小的列表空间而作的特殊表示
  * 集合可以被编码为 intset 或者 hashtable.intset 是只储存数字的小集合的特殊表示
  * 哈希表可以编码为 zipmap 或者 hashtable.zipmap 是小哈希表的特殊表示
  * 有序集合可以被编码为 ziplist 或者 skiplist 格式.ziplist 用于表示小的有序集合,而 skiplist 则用于表示任何大小的有序集合
  * 假如Redis没办法再使用节省空间的编码时(比如将一个只有1个元素的集合扩展为一个有100万个元素的集合),特殊编码类型自动转换成通用类型
* 时间复杂度:O(1)
* 返回值:REFCOUNT 和 IDLETIME 返回数字;ENCODING 返回相应的编码类型

## MIGRATE

* migrate host port key destination-db timeout:将key原子性地从当前实例传送到目标实例的数据库上,一旦传送成功, key保证会出现在目标实例上,而当前实例上的 key 会被删除
  * 该命令是原子操作,它在执行的时候会阻塞进行迁移的两个实例,直到以下任意结果发生:
    * 迁移成功,迁移失败,等到超时
  * 实现原理:它在当前实例对指定 key 执行 DUMP 命令 ,将它序列化,然后传送到目标实例,目标实例再使用 RESTORE 对数据进行反序列化,并将反序列化所得的数据添加到数据库中;当前实例就像目标实例的客户端那样,只要看到 RESTORE 命令返回 OK,就会调用 DEL 删除自己数据库上的 key 
  * timeout:以毫秒为格式,指定当前实例和目标实例进行沟通的最大间隔时间
  * MIGRATE需要在指定的时间完成 IO 操作,如果在传送数据时发生 IO 错误或达到了超时时间,那么命令会停止执行,并返回一个特殊的错误: IOERR.当 IOERR 出现时,有以下两种可能:
    * key 可能存在于两个实例
    * key 可能只存在于当前实例
  * 唯一不可能发生的情况就是丢失key,因此,如果一个客户端执行MIGRATE,并且遇上IOERR,那么这个客户端要做的就是检查自己数据库上的 key 是否已经被正确地删除
  * 如果有其他错误发生,MIGRATE 保证 key 只会出现在当前实例中.目标实例的数据库上可能有和 key 同名的键,不过这和 MIGRATE没有关系
* 时间复杂度:该命令在源实例上执行DUMP和DEL,在目标实例执行RESTORE,查看相应命令可以看到复杂度说明.key 数据在两个实例之间传输的复杂度为 O(N)
* 返回值:迁移成功时返回 OK ,否则返回相应的错误

## DUMP

* dump key:序列化key,并返回被序列化的值,RESTORE可以将该值反序列化.序列化值有以下几个特点:
  * 带有64位的校验和,用于检测错误,RESTORE在进行反序列化之前会先检查校验和
  * 值的编码格式和 RDB 文件保持一致
  * RDB版本会被编码在序列化值当中,如果因为Redis版本不同造成RDB格式不兼容,Redis会拒绝对这个值进行反序列化
  * 序列化的值不包括任何生存时间信息
* 时间复杂度:查找指定键的复杂度为O(1),对键进行序列化的复杂度为O(N*M),其中N是构成key的Redis对象的数量,M是这些对象的平均大小.如果序列化的对象是比较小的字符串,则复杂度为O(1)
* 返回值:如果 key 不存在,那么返回 nil;否则,返回序列化之后的值

## RESTORE

* restore key ttl serialized-value:反序列化指定的序列化值,并将它和指定的 key 关联
  * ttl:以毫秒为单位为 key 设置生存时间.如果ttl为0,那么不设置生存时间
  * RESTORE在执行反序列化之前会先对序列化值的RDB版本和数据校验和进行检查,如果RDB版本不相同或者数据不完整,RESTORE会拒绝进行反序列化,并返回错误
* 时间复杂度:
  * 查找指定键的复杂度为O(1),对键进行反序列化的复杂度为O(N*M),其中N是构成key的 Redis对象的数量,而M则是这些对象的平均大小
  * 有序集合的反序列化复杂度为 O(N\*M\*log(N)),因为有序集合每次插入的复杂度为 O(log(N))
  * 如果反序列化的对象是比较小的字符串,那么复杂度为 O(1)
* 返回值:如果反序列化成功那么返回 OK ,否则返回一个错误

# String(字符串)

## SET

* set key value:将字符串值 value 关联到 key.如果 key 已经持有其他值, SET 就覆写旧值,无视类型
* 时间复杂度:O(1)
* 返回值:总是返回 OK ,因为 SET 不可能失败

## SETNX

* setnx key value:将 key 的值设为 value ,当且仅当 key 不存在;若指定的 key 已经存在,则 SETNX 不做任何动作.SETNX 是`SET if Not eXists`(如果不存在,则 SET)的简写
* 时间复杂度:O(1)
* 返回值:设置成功,返回 1;设置失败,返回 0

### 应用场景

#### 加锁(locking)

* 该加锁算法带有竞争条件,在特定情况下会造成错误,请不要使用这个加锁算法
* `SETNX lock.foo <current Unix time + lock timeout + 1>`:
  * 返回1,说明获得了锁,unix时间指定锁失效的时间.可以通过 DEL lock.foo释放锁
  * 返回0,说明key已经被其他客户端上锁了.如果锁是非阻塞的,可以选择返回调用,或者循环重试

#### 处理死锁(deadlock)

* 该锁算法有一个问题:如果客户端失败,崩溃或其他原因导致无法释放锁时,这种状况可以通过检测发现:因为上锁的 key 保存的是 unix 时间戳,假如 key 值的时间戳小于当前的时间戳,表示锁已经不再有效
* 另外的问题是,当有多个客户端同时检测一个锁是否过期并尝试释放它的时候,不能简单粗暴地删除死锁的 key ,再用 SETNX 上锁,因为这时竞争条件(race condition)已经形成了:
  * C1 和 C2 读取 lock.foo并检查时间戳,SETNX都返回0,因为C3已经上锁,但C3在上锁之后就崩溃了
  * C1 向 lock.foo 发送 DEL,接着C1 向 lock.foo 发送 SETNX 并成功
  * C2 向 lock.foo 发送 DEL,接着C2 向 lock.foo 发送 SETNX 并成功
  * 出错:因为竞争条件的关系, C1 和 C2 两个都获得了锁.以下算法可以避免以上问题
    * C4 向 lock.foo 发送 SETNX.因为C3还锁着 lock.foo,所以 Redis 向 C4 返回 0
    * C4 向 lock.foo 发送GET,查看lock.foo的锁是否过期.如果不,则休眠一段时间,并在之后重试
    * 另一方面,如果 lock.foo 内的 unix 时间戳比当前时间戳老,C4 执行以下命令:`GETSET lock.foo <current Unix timestamp + lock timeout + 1>`.因为 GETSET 的作用, C4 可以检查看 GETSET 的返回值,确定 lock.foo 之前储存的旧值仍是那个过期时间戳,如果是的话,那么 C4 获得锁
    * 如果其他客户端C5,比C4更快地执行了 GETSET 操作并获得锁,那么C4的GETSET操作返回的就是一个未过期的时间戳(C5 设置的时间戳),C4只好从第一步开始重试
    * 即便 C4 的 GETSET 操作对 key 进行了修改,这对未来也没什么影响
  * 为了让这个加锁算法更健壮,获得锁的客户端应该常常检查过期时间以免锁因诸如DEL等命令的执行而被意外解开,因为客户端失败的情况非常复杂,不仅仅是崩溃这么简单,还可能是客户端因为某些操作被阻塞了相当长时间,紧接着 DEL 命令被尝试执行(但这时锁却在另外的客户端手上)

## SETEX

* setex key seconds value:将值 value 关联到 key ,并设置生存时间(秒).如果key已经存在,将覆写旧值.这是一个原子性操作,可以代替`SET key value`和`EXPIRE key seconds`
* 时间复杂度:O(1)
* 返回值:设置成功时返回 OK;当 seconds 参数不合法时,返回一个错误

## PSETEX

* psetex key milliseconds value:和 SETEX相似,但它以毫秒为单位设置 key 的生存时间
* 时间复杂度:O(1)
* 返回值:设置成功时返回 OK

## SETRANGE

* setrange key offset value:用 value覆写指定 key 所储存的字符串值,从偏移量 offset 开始,不存在的 key 当作空白字符串处理
  * 该命令会确保字符串足够长以便将 value 设置在指定的偏移量上,如果指定key 原来储存的字符串长度比偏移量小(比如字符串只有 5 个字符长,但设置的 offset是 10),那么原字符和偏移量之间的空白将用零字节(zerobytes, "\x00" )来填充
  * 能使用的最大偏移量是 2^29-1(536870911),因为 Redis 字符串的大小被限制在 512兆以内
  * 如果需要使用比这更大的空间,可以使用多个 key
  * 当生成一个很长的字符串时,Redis需要分配内存空间,该操作有时候可能会造成服务器阻塞
  * 若首次内存分配成功之后,再对同一个 key 调用 SETRANGE操作,无须再重新内存
* 时间复杂度:对小(small)的字符串,平摊复杂度 O(1);否则为 O(M), M 为 value 参数的长度
* 返回值:被 SETRANGE 修改之后,字符串的长度

### 应用场景

#### 线性数组

* 因为有了 SETRANGE 和 GETRANGE,可以将 Redis 字符串用作具有 O(1)随机访问时间的线性数组,这在很多真实用例中都是非常快速且高效的储存方式,具体可参考APPEND 命令的时间序列

## MSET

* mset key value [key value ...]:同时设置一个或多个 key-value 对
  * 如果某个指定 key 已经存在,那么 MSET 会用新值覆盖原来的旧值
  * 如果这不是所希望的效果,可以考虑使用MSETNX:它只会在所有指定 key 都不存在的情况下进行设置操作
  * MSET 是一个原子性操作,所有指定 key 都会在同一时间内被设置,某些指定key 被更新而另一些指定 key 没有改变的情况,不可能发生
* 时间复杂度:O(N), N 为要设置的 key 数量
* 返回值:总是返回 OK (因为 MSET 不可能失败)

## MSETNX

* msetnx key value [key value ...]:同时设置一个或多个 key-value 对,仅当所有指定 key 都不存在.即使只有一个指定 key 已存在, MSETNX 也会拒绝执行所有指定 key 的设置操作
  * MSETNX 是原子性的,因此它可以用作设置多个不同 key 表示不同字段(field)的唯一性逻辑对象,所有字段要么全被设置,要么全不被设置
* 时间复杂度:O(N), N 为要设置的 key 的数量
* 返回值:所有key都成功设置,返回 1;如果所有指定key都设置失败(至少有一个key已经存在),返回0

## APPEND

* append key value:如果 key 已经存在并且是一个字符串, APPEND 命令将 value 追加到 key 原来的值的末尾.如果 key 不存在, APPEND 就简单地将指定 key 设为 value ,就像执行 SET key value 一样
* 时间复杂度:平摊 O(1)
* 返回值:追加 value 之后, key 中字符串的长度

### 应用场景

#### 时间序列(Time series)

* APPEND可以为一系列定长(fixed-size)数据(sample)提供一种紧凑的表示方式,通常称之为时间序列
* 每当一个新数据到达的时候,执行以下命令:`APPEND timeseries "fixed-size sample"`,然后可以通过以下的方式访问时间序列的各项属性:
  * STRLEN 给出时间序列中数据的数量
  * GETRANGE可以用于随机访问,只要有相关的时间信息,就可以使用Lua脚本和GETRANGE实现二分查找
  * SETRANGE 可以用于覆盖或修改已存在的的时间序列
* 缺陷是只能增长时间序列,而不能对时间序列进行缩短,因为Redis还没有对字符串进行修剪的命令,但是,这个模式的储存方式还是可以节省下大量的空间
* 可以考虑使用UNIX时间戳作为时间序列的键名,这样可以避免单个key因为保存过大的时间序列而占用大量内存,也可以节省下大量命名空间

```shell
redis> APPEND ts "0043"
(integer) 4
redis> APPEND ts "0035"
(integer) 8
redis> GETRANGE ts 0 3
"0043"
redis> GETRANGE ts 4 7
"0035"
```

## GET

* get key:返回 key存储的字符串值.如果 key 不存在返回nil;如果key储存的值不是字符串,返回错误
* 时间复杂度:O(1)
* 返回值:key不存在时返回 nil;否则返回 key 的值;如果 key 不是字符串,那么返回错误

## MGET

* mget key [key ...]:返回所有key的值.如果某个key不存在,只有该key返回nil,其他正常返回
* 时间复杂度:O(N) , N 为指定 key 的数量
* 返回值:一个包含所有指定 key 的值的列表

## GETRANGE

* getrange key start end:返回 key 中字符串值的子字符串,字符串的截取范围由 start 和 end 两个偏移量决定(包括 start 和 end 在内).负数偏移量表示从字符串最后开始计数, -1 表示最后一个字符, -2 表示倒数第二个,以此类推
  * GETRANGE通过保证子字符串的值域不超过实际字符串的值域来处理超出范围的值域请求
* 时间复杂度:O(N), N 为要返回的字符串的长度.复杂度最终由字符串的返回值长度决定,但因为从已有字符串中取出子字符串的操作非常廉价,所以对于长度不大的字符串,该操作的复杂度也可看作 O(1)
* 返回值:截取得出的子字符串

## GETSET

* getset key value:将key的值设为 value ,并返回 key 的旧值.当key存在但不是字符串时,返回错误
* 时间复杂度:O(1)
* 返回值:返回指定 key 的旧值;当 key 没有旧值时,也即是, key 不存在时,返回 nil

### 应用场景

#### 计数器

* GETSET 可以和 INCR 组合使用,实现一个有原子性(atomic)复位操作的计数器(counter)

* EX:每次当某个事件发生时,进程可能对一个名为 mycount 的 key 调用 INCR操作,还要在一个原子时间内同时完成获得计数器的值和将计数器值复位为 0 两个操作

* 可以用命令 GETSET mycounter 0 来实现这一目标
  
  ```shell
  redis> INCR mycount
  (integer) 11
  # 一个原子内完成 GET mycount 和 SET mycount 0 操作
  redis> GETSET mycount 0 
  "11"
  # 计数器被重置
  redis> GET mycount 
  "0"
  ```

## STRLEN

* strlen key:返回 key 所储存的字符串值的长度.当 key 储存的不是字符串值时,返回错误
* 事件复杂度:O(1)
* 返回值:字符串值的长度;当 key 不存在时,返回 0

## DECR

* decr key:将 key 中储存的数字值减一
  * 如果 key 不存在,那么 key 的值会先被初始化为 0 ,然后再执行 DECR 操作
  * 如果值包含错误的类型,或字符串类型的值不能表示为数字,那么返回一个错误
  * 本操作的值限制在 64 位(bit)有符号数字表示之内
* 时间复杂度:O(1)
* 返回值:执行 DECR 命令之后 key 的值

## DECRBY

* decrby key decrement:将 key 所储存的值减去减量 decrement
  * 如果 key 不存在,那么 key 的值会先被初始化为 0 ,然后再执行 DECRBY 操作
  * 如果值包含错误的类型,或字符串类型的值不能表示为数字,那么返回一个错误
  * 本操作的值限制在 64 位(bit)有符号数字表示之内
* 时间复杂度:O(1)
* 返回值:减去 decrement 之后, key 的值

## INCR

* incr key:将 key 中储存的数字值增一
  * 如果 key 不存在,那么 key 的值会先被初始化为 0 ,然后再执行 INCR 操作
  * 如果值包含错误的类型,或字符串类型的值不能表示为数字,那么返回一个错误
  * 本操作的值限制在 64 位(bit)有符号数字表示之内
  * 这是一个针对字符串的操作,因为 Redis没有整数类型,所以 key 内储存的字符串被解释为十进制 64 位有符号整数来执行 INCR 操作
* 时间复杂度:O(1)
* 返回值:执行 INCR 命令之后 key 的值

### 应用场景

#### 计数器

* 计数器是 Redis 的原子性自增操作可实现的最直观的模式了,它的想法相当简单:每当某个操作发生时,向 Redis 发送一个 INCR 命令
* EX:在一个 web 应用程序中,如果想知道用户在一年中每天的点击量,那么只要将用户 ID 以及相关的日期信息作为键,并在每次用户点击页面时,执行一次自增操作即可
* 比如用户名是 peter ,点击时间是 2012 年 3 月 22 日,那么执行命令:`INCR peter::2012.3.22`
* 可以用以下几种方式扩展这个简单的模式:
  * 可以通过组合使用 INCR 和 EXPIRE ,来达到只在规定的生存时间内进行计数的目的
  * 客户端可以通过使用 GETSET 命令原子性地获取计数器的当前值并将计数器清零
  * 使用其他自增/自减操作,比如 DECR 和 INCRBY ,用户可以通过执行不同的操作增加或减少计数器的值,比如在游戏中的记分器就可能用到这些命令

#### 限速器

* 限速器是特殊化的计算器,它用于限制一个操作可以被执行的速率(rate)

* 限速器的典型用法是限制公开 API 的请求次数,以下是一个限速器实现示例,它将 API的最大请求数限制在每个 IP 地址每秒钟十个之内:
  
  ```shell
  FUNCTION LIMIT_API_CALL(ip)
  ts = CURRENT_UNIX_TIME()
  keyname = ip+":"+ts
  current = GET(keyname)
  IF current != NULL AND current > 10 THEN
  ERROR "too many requests per second"
  END
  IF current == NULL THEN
  MULTI
  INCR(keyname, 1)
  EXPIRE(keyname, 1)
  EXEC
  ELSE
  INCR(keyname, 1)
  END
  PERFORM_API_CALL()
  ```

* 这个实现每秒钟为每个 IP 地址使用一个不同的计数器,并用 EXPIRE 命令设置生存时间

* 使用事务打包执行 INCR 命令和 EXPIRE 命令,避免引入竞争条件,保证每次调用 API 时都可以正确地对计数器进行自增操作并设置生存时间

* 另一个限速器实现:
  
  ```shell
  FUNCTION LIMIT_API_CALL(ip):
  current = GET(ip)
  IF current != NULL AND current > 10 THEN
  ERROR "too many requests per second"
  ELSE
  value = INCR(ip)
  IF value == 1 THEN
  EXPIRE(ip,1)
  END
  PERFORM_API_CALL()
  END
  ```

* 这个限速器只使用单个计数器,它的生存时间为一秒钟,如果在一秒钟内,这个计数器的值大于 10 的话,那么访问就会被禁止

* 这个新的限速器在INCR和EXPIRE之间存在着一个竞争条件,假如客户端在执行INCR之后,因为某些原因而忘记设置 EXPIRE 的话,那么这个计数器就会一直存在下去,造成每个用户只能访问 10 次

* 要消灭这个实现中的竞争条件,可以将它转化为一个 Lua 脚本,并放到 Redis 中运行:
  
  ```lua
  local current
  current = redis.call("incr",KEYS[1])
  if tonumber(current) == 1 then
  redis.call("expire",KEYS[1],1)
  end
  ```

* 通过将计数器作为脚本放到 Redis 上运行,保证了 INCR 和 EXPIRE的原子性,现在这个脚本实现不会引入竞争条件,它可以运作的很好

* 另一种消灭竞争条件的方法,就是使用Redis的列表结构来代替 INCR 命令,这个方法无须脚本支持
  
  ```shell
  FUNCTION LIMIT_API_CALL(ip)
  current = LLEN(ip)
  IF current > 10 THEN
  ERROR "too many requests per second"
  ELSE
  IF EXISTS(ip) == FALSE
  MULTI
  RPUSH(ip,ip)
  EXPIRE(ip,1)
  EXEC
  ELSE
  RPUSHX(ip,ip)
  END
  PERFORM_API_CALL()
  END
  ```

* 新的限速器使用列表作为容器, LLEN 用于对访问次数进行检查,一个事务包裹 RPUSH 和 EXPIRE 两个命令,用于在第一次执行计数时创建列表,并正确设置地设置过期时间,最后, RPUSHX 在后续的计数操作中进行增加操作

## INCRBY

* incrby key increment:将 key 所储存的值加上增量 increment
  * 如果 key 不存在,那么 key 的值会先被初始化为 0 ,然后再执行 INCRBY 命令
  * 如果值包含错误的类型,或字符串类型的值不能表示为数字,那么返回一个错误
  * 本操作的值限制在 64 位(bit)有符号数字表示之内
* 时间复杂度:O(1)
* 返回值:加上 increment 之后, key 的值

## INCRBYFLOAT

* incrbyfloat key increment:为key 中所储存的值加上浮点数增量increment
  * 如果key不存在,那么INCRBYFLOAT会先将key的值设为0,再执行加法操作
  * 如果命令执行成功,那么key的值会被更新为新值,并且新值会以字符串的形式返回给调用者
  * 无论是key的值,还是增量 increment ,都可以使用像2.0e7,3e5,90e-2那样的指数符号来表示,但是,执行 INCRBYFLOAT 命令之后的值总是以同样的形式储存,即它们总是由一个数字,一个(可选的)小数点和一个任意位的小数部分组成(如3.14,69.768).小数部分尾随的 0 会被移除,如果有需要的话,还会将浮点数改为整数(如3.0会被保存成 3)
  * 无论加法计算所得的浮点数的实际精度有多长, 计算结果最多只能表示小数点的后十七位
  * 当以下任意一个条件发生时,返回一个错误:
    * key 的值不是字符串类型,因为Redis中的数字和浮点数都以字符串的形式保存,所以它们都属于字符串类型
    * key 当前的值或者指定的增量 increment 不能解释为双精度浮点数
* 时间复杂度:O(1)
* 返回值:执行命令之后 key 的值

## SETBIT

* setbit key offset value:对 key 所储存的字符串值,设置或清除指定偏移量上的位.位的设置或清除取决于 value 参数,可以是 0 也可以是 1
  * 当 key 不存在时,自动生成一个新的字符串值
  * 字符串会进行伸展(grown)以确保它可以将 value 保存在指定的偏移量上。当字符串值进行伸展时,空白位置以 0 填充
  * offset 参数必须大于或等于 0 ,小于 2^32 (bit 映射被限制在 512 MB 之内)
  * 对使用大的 offset 的 SETBIT 操作来说,内存分配可能造成 Redis 服务器被阻
* 时间复杂度:O(1)
* 返回值:指定偏移量原来储存的位

## GETBIT

* getbit key offset:对 key 所储存的字符串值,获取指定偏移量上的位.当 offset 比字符串值的长度大,或者 key 不存在时,返回 0
* 时间复杂度:O(1)
* 返回值:字符串值指定偏移量上的位(bit)

## BITOP

* bitop operation destkey key [key ...]:对一个或多个保存二进制位的字符串 key 进行位元操作,并将结果保存到 destkey 上.operation 可以是 AND 、 OR 、 NOT 、 XOR 这四种操作中的任意一种:
  * BITOP AND destkey key [key ...] ,对一个或多个 key 求逻辑并,并将结果保存到 destkey
  * BITOP OR destkey key [key ...] ,对一个或多个 key 求逻辑或,并将结果保存到 destkey
  * BITOP XOR destkey key [key ...] ,对一个或多个 key 求逻辑异或,并将结果保存到 destkey
  * BITOP NOT destkey key ,对指定 key 求逻辑非,并将结果保存到 destkey
  * 除了 NOT 操作之外,其他操作都可以接受一个或多个 key 作为输入
  * 处理不同长度的字符串:当 BITOP 处理不同长度的字符串时,较短的那个字符串所缺少的部分会被看作 0
  * 空的 key 也被看作是包含 0 的字符串序列
* 时间复杂度:O(N).当处理大型矩阵(matrix)或者进行大数据量的统计时,最好将任务指派到附属节点(slave)进行,避免阻塞主节点
* 返回值:保存到 destkey 的字符串的长度,和输入 key 中最长的字符串长度相等

## BITCOUNT

* bitcount key [start] [end]:计算指定字符串中,被设置为 1 的比特位的数量
  * 一般情况下,指定的整个字符串都会被进行计数,通过指定额外的 start 或 end 参数,可以让计数只在特定的位上进行
  * start 和 end 参数的设置和 GETRANGE 命令类似,都可以使用负数值:比如 -1 表示最后一个位,而 -2 表示倒数第二个位,以此类推
  * 不存在的key被当成是空字符串来处理,因此对一个不存在的 key 进行 BITCOUNT 操作,结果为 0
* 时间复杂度:O(N)
* 返回值:被设置为 1 的位的数量

### 应用场景

#### 用户上线次数统计

* 假设要记录自己网站上的用户的上线频率,比如说,计算用户 A 上线了多少天,用户 B 上线了多少天,诸如此类,以此作为数据,从而决定让哪些用户参加 beta 测试等活动 —— 这个模式可以使用 SETBIT 和 BITCOUNT 来实现
* 比如说,每当用户在某一天上线的时候,我们就使用 SETBIT ,以用户名作为 key ,将那天所代表的网站的上线日作为 offset 参数,并将这个 offset 上的为设置为 1
* 如果今天是网站上线的第 100 天,而用户 peter 在今天阅览过网站,那么执行命令 SETBIT peter 100 1;如果明天 peter 也继续阅览网站,那么执行命令 SETBITpeter 101 1 ,以此类推
* 当要计算 peter 总共以来的上线次数时,就使用 BITCOUNT 命令:执行 BITCOUNTpeter ,得出的结果就是 peter 上线的总天数
* 前面的上线次数统计例子,即使运行 10 年,占用的空间也只是每个用户3650bit,即每个用户456字节,对于这种大小的数据来说, BITCOUNT 的处理速度就像 GET 和 INCR 这种 O(1) 复杂度的操作一样快
* 如果bitmap数据非常大,那么可以考虑使用以下两种方法:
  * 将一个大的 bitmap 分散到不同的 key 中,作为小的 bitmap 来处理。使用 Lua脚本可以很方便地完成这一工作
  * 使用 BITCOUNT 的 start 和 end 参数,每次只对所需的部分位进行计算,将位的累积工作放到客户端进行,并且对结果进行缓存

# Hash(哈希表)

## HSET

* hset key field value:将哈希表 key 中的域 field 的值设为 value
  * 如果 key 不存在,一个新的哈希表被创建并进行 HSET 操作
  * 如果域 field 已经存在于哈希表中,旧值将被覆盖
* 时间复杂度:O(1)
* 返回值:如果 field是一个新建域,并且值设置成功,返回 1;如果field已经存在且旧值已被新值覆盖,返回 0

## HSETNX

* hsetnx key field value:将哈希表 key 中的域 field 的值设置为 value ,当且仅当域 field 不存在
  * 若域 field 已经存在,该操作无效
  * 如果 key 不存在,一个新哈希表被创建并执行 HSETNX 命令
* 时间复杂度:O(1)
* 返回值:设置成功,返回 1;如果指定域已经存在且没有操作被执行,返回 0

## HMSET

* hmset key field value [field value ...]:同时将多个 field-value (域-值)对设置到哈希表 key 中
  * 此命令会覆盖哈希表中已存在的域
  * 如果 key 不存在,一个空哈希表被创建并执行 HMSET 操作
* 时间复杂度:O(N), N 为 field-value 对的数量
* 返回值:如果命令执行成功,返回 OK;当 key 不是哈希表(hash)类型时,返回一个错误

## HGET

* hget key field:返回哈希表 key 中指定域 field 的值
* 时间复杂度:O(1)
* 返回值:指定域的值;当指定域不存在或是指定 key 不存在时,返回 nil

## HMGET

* hmget key field [field ...]:返回哈希表 key 中,一个或多个指定域的值
  * 如果指定的域不存在于哈希表,那么返回一个 nil 值
  * 因为不存在的 key 被当作一个空哈希表来处理,所以对一个不存在的 key 进行 HMGET操作将返回一个只带有 nil 值的表
* 时间复杂度:O(N), N 为指定域的数量
* 返回值:一个包含多个指定域的关联值的表,表值的排列顺序和指定域参数的请求顺序一样

## HGETALL

* hgetall key:返回哈希表 key 中,所有的域和值.在返回值里,紧跟每个域名(field name)之后是域的值(value),所以返回值的长度是哈希表大小的两倍
* 时间复杂度:O(N), N 为哈希表的大小
* 返回值:以列表形式返回哈希表的域和域的值;若 key 不存在,返回空列表

## HDEL

* hdel key field [field ...]:删除哈希表 key 中的一个或多个指定域,不存在的域将被忽略
* 时间复杂度:O(N), N 为要删除的域的数量
* 返回值:被成功移除的域的数量,不包括被忽略的域

## HLEN

* hlen key:返回哈希表 key 中域的数量
* 时间复杂度:O(1)
* 返回值:哈希表中域的数量;当 key 不存在时,返回 0

## HEXISTS

* hexists key field:查看哈希表 key 中,指定域 field 是否存在
* 时间复杂度:O(1)
* 返回值:如果哈希表含有指定域,返回 1;如果哈希表不含有指定域,或 key 不存在,返回 0

## HINCRBY

* hincrby key field increment:为哈希表 key 中的域 field 的值加上增量 increment
  * 增量也可以为负数,相当于对指定域进行减法操作
  * 如果 key 不存在,一个新的哈希表被创建并执行 HINCRBY 命令
  * 如果域 field 不存在,那么在执行命令前,域的值被初始化为 0
  * 对一个储存字符串值的域 field 执行 HINCRBY 命令将造成一个错误
  * 本操作的值被限制在 64 位(bit)有符号数字表示之内
* 时间复杂度:O(1)
* 返回值:执行 HINCRBY 命令之后,哈希表 key 中域 field 的值

## HINCRBYFLOAT

* hincrbyfloat key field increment:为哈希表 key 中的域 field 加上浮点数增量 increment
  * 如果哈希表中没有field ,HINCRBYFLOAT 会先将域 field 的值设为0 ,然后再执行加法操作
  * 如果键 key 不存在,HINCRBYFLOAT 会先创建一个哈希表,再创建域 field ,最后再执行加法操作
  * 当以下任意一个条件发生时,返回一个错误:
    * 域 field 的值不是字符串类型(因为 redis 中的数字和浮点数都以字符串的形式保存,所以它们都属于字符串类型)
    * 域 field 当前的值或指定的增量 increment 不能解释(parse)为双精度浮点数
    * HINCRBYFLOAT 命令的详细功能和 INCRBYFLOAT 命令类似
* 时间复杂度:O(1)
* 返回值:执行加法操作之后 field 域的值

## HKEYS

* hkeys key:返回哈希表 key 中的所有域
* 时间复杂度:O(N), N 为哈希表的大小
* 返回值:一个包含哈希表中所有域的表;当 key 不存在时,返回一个空表

## HVALS

* hvals key:返回哈希表 key 中所有域的值
* 时间复杂度:O(N), N 为哈希表的大小
* 返回值:一个包含哈希表中所有值的表;当 key 不存在时,返回一个空表

# List(列表)

## LPUSH

* lpush key value [value ...]:将一个或多个值 value 插入到列表 key 的表头
  * 如果有多个 value 值,那么各个 value 值按从左到右的顺序依次插入到表头
  * 如果 key 不存在,一个空列表会被创建并执行 LPUSH 操作
  * 当 key 存在但不是列表类型时,返回一个错误
  * 在 Redis 2.4 版本以前的 LPUSH 命令,都只接受单个 value 值
* 时间复杂度:O(1)
* 返回值:执行 LPUSH 命令后,列表的长度

## LPUSHX

* lpushx key value:将值 value 插入到列表 key 的表头,当且仅当 key 存在并且是一个列表.和 LPUSH 命令相反,当 key 不存在时, LPUSHX 命令什么也不做
* 时间复杂度:O(1)
* 返回值:LPUSHX 命令执行之后,表的长度
  示例代码:

## RPUSH

* rpush key value [value ...]:将一个或多个值 value 插入到列表 key 的表尾(最右边)
  * 如果有多个 value 值,那么各个 value 值按从左到右的顺序依次插入到表尾
  * 如果 key 不存在,一个空列表会被创建并执行 RPUSH 操作
  * 当 key 存在但不是列表类型时,返回一个错误
  * 在 Redis 2.4 版本以前的 RPUSH 命令,都只接受单个 value 值
* 时间复杂度:O(1)
* 返回值:执行 RPUSH 操作后,表的长度

## RPUSHX

* rpushx key value:将值 value 插入到列表 key 的表尾,当且仅当 key 存在并且是一个列表.和 RPUSH 命令相反,当 key 不存在时, RPUSHX 命令什么也不做

* 时间复杂度:O(1)

* 返回值:RPUSHX 命令执行之后,表的长度

## LPOP

* lpop key:移除并返回列表 key 的头元素
* 时间复杂度:O(1)
* 返回值:列表的头元素;当 key 不存在时,返回 nil

## RPOP

* rpop key:移除并返回列表 key 的尾元素
* 时间复杂度:O(1)
* 返回值:列表的尾元素;当 key 不存在时,返回 nil

## BLPOP

* blpop key [key ...] timeout:BLPOP 是列表的阻塞式(blocking)弹出原语
  * 它是 LPOP 命令的阻塞版本,当指定列表内没有任何元素可供弹出的时候,连接将被BLPOP 命令阻塞,直到等待超时或发现可弹出元素为止
  * 当指定多个 key 参数时,按参数 key 的先后顺序依次检查各个列表,弹出第一个非空列表的头元素
* 时间复杂度:O(1)
* 返回值:如果列表为空,返回一个 nil;否则,返回一个含有两个元素的列表,第一个元素是被弹出元素所属的 key ,第二个元素是被弹出元素的值

### 非阻塞行为

* 当 BLPOP 被调用时,如果指定 key 内至少有一个非空列表,那么弹出遇到的第一个非空列表的头元素,并和被弹出元素所属的列表的名字一起,组成结果返回给调用者
* 当存在多个指定 key 时, BLPOP 按指定 key 参数排列的先后顺序,依次检查各个列表
* 假设有 job 、 command 和 request 三个列表,其中 job 不存在, command 和request 都持有非空列表,考虑以下命令:`BLPOP job command request 0`
* BLPOP 保证返回的元素来自 command,因为它是按查找 job -> 查找 command -> 查找 request这样的顺序,第一个找到的非空列表

### 阻塞行为

* 如果所有指定 key 都不存在或包含空列表,那么 BLPOP 命令将阻塞连接,直到等待超时,或有另一个客户端对指定 key 的任意一个执行 LPUSH 或 RPUSH 命令为止
* 超时参数 timeout 接受一个以秒为单位的数字作为值。超时参数设为 0 表示阻塞时间可以无限期延长

### 相同key被多个客户端同时阻塞

* 相同的 key 可以被多个客户端同时阻塞
* 不同的客户端被放进一个队列中,按先阻塞先服务的顺序为 key 执行 BLPOP 命令

### 在 MULTI/EXEC 事务中的 BLPOP

* BLPOP 可以用于流水线(pipline,批量地发送多个命令并读入多个回复),但把它用在MULTI / EXEC 块当中没有意义。因为这要求整个服务器被阻塞以保证块执行时的原子性,该行为阻止了其他客户端执行 LPUSH 或 RPUSH 命令
* 因此,一个被包裹在 MULTI / EXEC 块内的 BLPOP 命令,行为表现得就像 LPOP 一样,对空列表返回 nil ,对非空列表弹出列表元素,不进行任何阻塞操作

### 应用场景

#### 事件提醒

* 有时候,为了等待一个新元素到达数据中,需要使用轮询的方式对数据进行探查.另一种更好的方式是,使用系统提供的阻塞原语,在新元素到达时立即进行处理,而新元素还没到达时,就一直阻塞住,避免轮询占用资源

* 对于 Redis ,似乎需要一个阻塞版的 SPOP 命令,但实际上,使用BLPOP或BRPOP就能解决这个问题
  
  ```shell
  # 使用元素的客户端(消费者)可以执行类似以下的代码
  LOOP forever
      WHILE SPOP(key) returns elements
      ... process elements ...
      END
      BRPOP helper_key
  END
  #添加元素的客户端(消费者)则执行以下代码:
  MULTI
      SADD key element
      LPUSH helper_key x
  EXEC
  ```

### BRPOP

* brpop key [key ...] timeout:BRPOP 是列表的阻塞式(blocking)弹出原语
  
  * 它是 RPOP 命令的阻塞版本,当指定列表内没有任何元素可供弹出的时候,连接将被BRPOP 命令阻塞,直到等待超时或发现可弹出元素为止
  * 当指定多个 key时,按参数 key 的先后顺序依次检查各个列表,弹出第一个非空列表的尾部元素
  * BRPOP除了弹出元素的位置和 BLPOP不同之外,其他表现一致

* 时间复杂度:O(1)

* 返回值:假如在指定时间内没有任何元素被弹出,则返回一个 nil 和等待时长;反之,返回一个含有两个元素的列表,第一个元素是被弹出元素所属的 key ,第二个元素是被弹出元素的值

## LLEN

* llen key:返回列表 key 的长度;
  * 如果 key 不存在,则 key 被解释为一个空列表,返回 0
  * 如果 key 不是列表类型,返回一个错误
* 时间复杂度:O(1)
* 返回值:列表 key 的长度

## LRANGE

* lrange key start stop:返回列表 key 中指定区间内的元素,区间以偏移量 start 和 stop 指定
  * 下标(index)参数 start 和 stop 都以 0 为底,也就是说,以 0 表示列表的第一个元素,以 1 表示列表的第二个元素,以此类推
  * 也可以使用负数下标,以 -1 表示列表的最后一个元素, -2 表示列表的倒数第二个元素,以此类推
  * LRANGE和编程语言区间函数的区别:
    * 假如有一个包含一百个元素的列表,对该列表执行 LRANGE list 0 10 ,结果是一个包含 11 个元素的列表,这表明 stop 下标也在 LRANGE 命令的取值范围之内(闭区间),这和某些语言的区间函数可能不一致
  * 超出范围的下标值不会引起错误.如果 start 下标比列表的最大下标 end ( LLEN list 减去 1 )还要大,或者 start >stop , LRANGE 返回一个空列表.如果 stop 下标比end下标还要大,Redis将 stop 的值设置为 end
* 时间复杂度:O(S+N), S 为偏移量 start , N 为指定区间内元素的数量
* 返回值:一个列表,包含指定区间内的元素

## LREM

* lrem key count value:根据参数 count 的值,移除列表中与参数 value 相等的元素.count 的值可以是以下几种:
  * count > 0 : 从表头开始向表尾搜索,移除与 value 相等的元素,数量为 count
  * count < 0 : 从表尾开始向表头搜索,移除与 value 相等的元素,数量为 count 的绝对值
  * count = 0 : 移除表中所有与 value 相等的值
* 时间复杂度:O(N), N 为列表的长度
* 返回值:被移除元素的数量;因为不存在的 key 被视作空表,所以当 key 不存在时,LREM命令总是返回 0

## LSET

* lset key index value:将列表 key 下标为 index 的元素的值设置为 value
  * 当 index 参数超出范围,或对一个空列表( key 不存在)进行 LSET 时,返回一个错误
* 时间复杂度:对头元素或尾元素进行 LSET 操作,复杂度为 O(1);其他情况下,为 O(N), N 为列表的长度
* 返回值:操作成功返回 ok ,否则返回错误信息

## LTRIM

* ltrim key start stop:对一个列表进行修剪(trim),就是说,让列表只保留指定区间内的元素,不在指定区间之内的元素都将被删除
  * 执行命令 LTRIM list 0 2 ,表示只保留列表 list 的前三个元素,其余元素全部删除
  * 下标(index)参数 start 和 stop 都以 0 为底,也就是说,以 0 表示列表的第一个元素,以 1 表示列表的第二个元素,以此类推
  * 也可以使用负数下标,以 -1 表示列表的最后一个元素, -2 表示列表的倒数第二个元素,以此类推
  * 当 key 不是列表类型时,返回一个错误
  * LTRIM 命令通常和 LPUSH 命令或 RPUSH 命令配合使用,如`LPUSH log test_log`,`LTRIM log 0 99`
    * 这个例子模拟了一个日志程序,每次将最新日志test_log放到 log 列表中,并且只保留最新的 100 项.当这样使用 LTRIM时,时间复杂度是 O(1),因为平均情况下,每次只有一个元素被移除
  * LTRIM 命令和编程语言区间函数的区别
  * 超出范围的下标值不会引起错误.如果 start 下标比列表的最大下标 end ( LLEN list 减去 1 )还要大,或者 start >stop , LTRIM 返回一个空列表(因为 LTRIM 已经将整个列表清空).如果 stop 下标比 end 下标还要大, Redis 将 stop 的值设置为 end
* 时间复杂度:O(N), N 为被移除的元素的数量
* 返回值:命令执行成功时,返回 ok 

## LINDEX

* lindex key index:返回列表 key 中,下标为 index 的元素
  * 下标(index)参数 start 和 stop 都以 0 为底,也就是说,以 0 表示列表的第一个元素,以 1 表示列表的第二个元素,以此类推
  * 也可以使用负数下标,以 -1 表示列表的最后一个元素, -2 表示列表的倒数第二个元素,以此类推
  * 如果 key 不是列表类型,返回一个错误
* 时间复杂度:O(N), N 为到达下标 index 过程中经过的元素数量;因此,对列表的头元素和尾元素执行 LINDEX 命令,复杂度为 O(1)
* 返回值:列表中下标为 index 的元素;如果 index 参数的值不在列表的区间范围内,返回 nil

## LINSERT

* linsert key BEFORE|AFTER pivot value:将值 value 插入到列表 key 当中,位于值 pivot 之前或之后
  
  * 当 pivot 不存在于列表 key 时,不执行任何操作
  * 当 key 不存在时, key 被视为空列表,不执行任何操作
  * 如果 key 不是列表类型,返回一个错误

* 时间复杂度:O(N), N 为寻找 pivot 过程中经过的元素数量

* 返回值:如果成功,返回列表长度;如果没有找到pivot,返回-1;如果key不存在或为空列表,返回0

## RPOPLPUSH

* rpoplpush source destination:命令 RPOPLPUSH 在一个原子时间内,执行以下两个动作:
  
  * 将列表 source 中的最后一个元素(尾元素)弹出,并返回给客户端
  * 将 source 弹出的元素插入到列表 destination ,作为 destination 列表的的头元素
  * 有两个列表 source 和 destination , source 列表有元素 a, b, c ,destination 列表有元素 x, y, z ,执行 RPOPLPUSH source destination 之后, source列表包含元素 a, b , destination 列表包含元素 c, x, y, z ,并且元素 c 会被返回给客户端
    * 如果 source 不存在,值 nil 被返回,并且不执行其他动作
    * 如果 source 和 destination 相同,则列表中的表尾元素被移动到表头,并返回该元素,可以把这种特殊情况视作列表的旋转(rotation)操作

* 时间复杂度:O(1)

* 返回值:被弹出的元素

### 应用场景

#### 安全队列

* Redis 的列表经常被用作队列(queue),用于在不同程序之间有序地交换消息(message)
* 一个客户端通过 LPUSH 命令将消息放入队列中,而另一个客户端通过 RPOP 或者 BRPOP 命令取出队列中等待时间最长的消息
* 上面的队列方法是不安全的,因为在这个过程中,一个客户端可能在取出一个消息之后崩溃,而未处理完的消息也就因此丢失
* 使用 RPOPLPUSH(或BRPOPLPUSH)可以解决这个问题:因为它不仅返回一个消息,同时还将这个消息添加到另一个备份列表当中,如果一切正常的话,当一个客户端完成某个消息的处理之后,可以用LREM命令将这个消息从备份表删除
* 还可以添加一个客户端专门用于监视备份表,它自动地将超过一定处理时限的消息重新放入队列中去(负责处理该消息的客户端可能已经崩溃),这样就不会丢失任何消息了

#### 循环列表

* 通过使用相同的 key 作为 RPOPLPUSH 命令的两个参数,客户端可以用一个接一个地获取列表元素的方式,取得列表的所有元素,而不必像 LRANGE 命令那样一下子将所有列表元素都从服务器传送到客户端中(两种方式的总复杂度都是 O(N))
* 以上的模式甚至在以下的两个情况下也能正常工作:
  * 有多个客户端同时对同一个列表进行旋转(rotating),它们获取不同的元素,直到所有元素都被读取完,之后又从头开始
  * 有客户端在向列表尾部(右边)添加新元素
* 这个模式可以很容易实现这样一类系统:有 N 个客户端,需要连续不断地对一些元素进行处理,而且处理的过程必须尽可能地快。一个典型的例子就是服务器的监控程序:它们需要在尽可能短的时间内,并行地检查一组网站,确保它们的可访问性
* 使用这个模式的客户端是易于扩展且安全的,因为就算接收到元素的客户端失败,元素还是保存在列表里面,不会丢失,等到下个迭代来临的时候,别的客户端又可以继续处理这些元素了

## BRPOPLPUSH

* brpoplpush source destination timeout:BRPOPLPUSH 是 RPOPLPUSH 的阻塞版本,当指定列表 source 不为空时, BRPOPLPUSH的表现和 RPOPLPUSH 一样
  * 当列表 source 为空时, BRPOPLPUSH 命令将阻塞连接,直到等待超时,或有另一个客户端对 source 执行 LPUSH 或 RPUSH 命令为止
  * 超时参数 timeout 接受一个以秒为单位的数字作为值。超时参数设为 0 表示阻塞时间可以无限期延长(block indefinitely)
* 时间复杂度:O(1)
* 返回值:假如在指定时间内没有任何元素被弹出,则返回一个 nil 和等待时长;反之,返回一个含有两个元素的列表,第一个元素是被弹出元素的值,第二个元素是等待时长

### 应用场景

#### 安全队列

* 同RPOPLPUSH

#### 循环列表

* 同RPOPLPUSH

# Set(集合)

## SADD

* sadd key member [member ...]:将一个或多个 member 元素加入到集合 key 当中,已经存在于集合的 member 元素将被忽略
  * 假如 key 不存在,则创建一个只包含 member 元素作成员的集合
  * 当 key 不是集合类型时,返回一个错误
* 时间复杂度:O(N), N 是被添加的元素的数量
* 返回值:被添加到集合中的新元素的数量,不包括被忽略的元素

## SREM

* srem key member [member ...]:移除集合 key 中的一个或多个 member 元素,不存在的 member 元素会被忽略.当 key 不是集合类型,返回一个错误
* 时间复杂度:O(N), N 为指定 member 元素的数量
* 返回值:被成功移除的元素的数量,不包括被忽略的元素

## SMEMBERS

* smembers key:返回集合 key 中的所有成员,不存在的 key 被视为空集合
* 时间复杂度:O(N), N 为集合的基数
* 返回值:集合中的所有成员

## SISMEMBER

* sismember key member:判断 member 元素是否集合 key 的成员
* 时间复杂度:O(1)
* 返回值:如果member是集合的成员,返回 1;如果 member不是集合的成员,或 key 不存在,返回0

## SCARD

* scard key:返回集合 key 的基数(集合中元素的数量)
* 时间复杂度:O(1)
* 返回值:集合的基数;当 key 不存在时,返回 0

## SMOVE

* smove source destination member:将 member 元素从 source 集合移动到 destination 集合
  * SMOVE 是原子性操作
  * 如果 source 集合不存在或不包含指定的 member 元素,则 SMOVE 命令不执行任何操作,仅返回 0.否则, member 元素从 source 集合中被移除,并添加到 destination 集合中去
  * 当destination集合已经包含member时,SMOVE只是简单地将source集合中的 member删除
  * 当 source 或 destination 不是集合类型时,返回一个错误
* 时间复杂度:O(1)
* 返回值:如果 member 元素被成功移除,返回1;如果 member 元素不是 source 集合的成员,并且没有任何操作对 destination 集合执行,那么返回 0

## SPOP

* spop key:移除并返回集合中的一个随机元素.如果只想获取一个随机元素,但不想该元素从集合中被移除的话,可以使用SRANDMEMBER
* 时间复杂度:O(1)
* 返回值:被移除的随机元素;当 key 不存在或 key 是空集时,返回 nil

## SRANDMEMBER

* srandmember key [count]:如果命令执行时,只提供了 key 参数,那么返回集合中的一个随机元素
  * 如果 count 为正数,且小于集合基数,那么命令返回一个包含 count 个元素的数组,数组中的元素各不相同。如果 count 大于等于集合基数,那么返回整个集合
  * 如果 count 为负数,那么命令返回一个数组,数组中的元素可能会重复出现多次,而数组的长度为 count 的绝对值
  * 该操作和 SPOP 相似,但 SPOP 将随机元素从集合中移除并返回,而 SRANDMEMBER 则仅仅返回随机元素,而不对集合进行任何改动
* 时间复杂度:只提供 key 参数时为 O(1);如果提供了 count 参数,那么为 O(N) ,N 为返回数组的元素个数
* 返回值:只提供 key 参数时,返回一个元素；如果集合为空,返回 nil;如果提供了 count 参数,那么返回一个数组；如果集合为空,返回空数组

## SINTER

* sinter key [key ...]:返回一个集合的全部成员,该集合是所有指定集合的交集
  * 不存在的 key 被视为空集
  * 当指定集合当中有一个空集时,结果也为空集(根据集合运算定律)
* 时间复杂度:O(N * M), N 为指定集合当中基数最小的集合, M 为指定集合的个数
* 返回值:交集成员的列表

## SINTERSTORE

* sinterstore destination key [key ...]:这个命令类似于 SINTER 命令,但它将结果保存到 destination 集合,而不是简单地返回结果集
  * 如果 destination 集合已经存在,则将其覆盖
  * destination 可以是 key 本身
* 时间复杂度:O(N * M), N 为指定集合当中基数最小的集合, M 为指定集合的个数
* 返回值:结果集中的成员数量

## SUNION

* sunion key [key ...]:返回一个集合的全部成员,该集合是所有指定集合的并集,不存在的key被视为空集
* 时间复杂度:O(N), N 是所有指定集合的成员数量之和
* 返回值:并集成员的列表

## SUNIONSTORE

* sunionstore dest key [key ...]:这个命令类似于 SUNION 命令,但它将结果保存到 dest集合,而不是简单地返回结果集
  * 如果 destination 已经存在,则将其覆盖
  * destination 可以是 key 本身
* 时间复杂度:O(N), N 是所有指定集合的成员数量之和
* 返回值:结果集中的元素数量

## SDIFF

* sdiff key [key ...]:返回一个集合的全部成员,该集合是所有指定集合的差集,不存在的 key 被视为空集
* 时间复杂度:O(N), N 是所有指定集合的成员数量之和
* 返回值:交集成员的列表

## SDIFFSTORE

* sdiffstore dest key [key ...]:和SDIFF 类似,但它将结果保存到dest集合,而不是简单地返回结果集
  * 如果 dest集合已经存在,则将其覆盖
  * dest可以是 key 本身
* 时间复杂度:O(N), N 是所有指定集合的成员数量之和
* 返回值:结果集中的元素数量

# Sorted Set(有序集)

## ZADD

* zadd key score member [[score member] [score member] ...]:将一个或多个 member 元素及其 score 值加入到有序集 key 当中
  * 如果某个 member 已经是有序集的成员,那么更新这个 member 的 score 值,并通过重新插入这个 member 元素,来保证该 member 在正确的位置上
  * score 值可以是整数值或双精度浮点数
  * 如果 key 不存在,则创建一个空的有序集并执行 ZADD 操作
  * 当 key 存在但不是有序集类型时,返回一个错误
* 时间复杂度:O(M*log(N)), N 是有序集的基数, M 为成功添加的新成员的数量
* 返回值:被成功添加的新成员的数量,不包括那些被更新的,已经存在的成员

## ZREM

* zrem key member [member ...]:移除有序集 key 中的一个或多个成员,不存在的成员将被忽略.当 key 存在但不是有序集类型时,返回一个错误
* 时间复杂度:O(M*log(N)), N 为有序集的基数, M 为被成功移除的成员的数量
* 返回值:被成功移除的成员的数量,不包括被忽略的成员

## ZCARD

* zcard key:返回有序集 key 的基数
* 时间复杂度:O(1)
* 返回值:当 key 存在且是有序集类型时,返回有序集的基数;当 key 不存在时,返回 0

## ZCOUNT

* zcount key min max:返回有序集 key 中, score 值在 min 和 max 之间(默认包括 score 值等于 min 或max )的成员的数量
* 时间复杂度:O(log(N)+M), N 为有序集的基数, M 为值在 min 和 max 之间的元素的数量
* 返回值:score 值在 min 和 max 之间的成员的数量

## ZSCORE

* zscore key member:返回有序集 key 中,成员 member 的 score 值
  * 如果 member 元素不是有序集 key 的成员,或 key 不存在,返回 nil
* 时间复杂度:O(1)
* 返回值:member 成员的 score 值,以字符串形式表示

## ZINCRBY

* zincrby key increment member:为有序集 key 的成员 member 的 score 值加上增量 increment
  * 可以通过传递一个负数值 increment ,让 score 减去相应的值
  * 当 key 不存在或member不是key的成员时,ZINCRBY key increment member 等同于 ZADD key increment member
  * 当 key 不是有序集类型时,返回一个错误
  * score 值可以是整数值或双精度浮点数
* 时间复杂度:O(log(N))
* 返回值:member 成员的新 score 值,以字符串形式表示

## ZRANGE

* zrange key start stop [WITHSCORES]:返回有序集 key 中,指定区间内的成员
  
  * 成员的位置按 score 值递增(从小到大)来排序
  * 具有相同 score 值的成员按字典序(lexicographical order )来排列
  * 如果需要成员按 score 值递减(从大到小)来排列,可使用 ZREVRANGE
  * 下标start 和 stop 都以 0 为底,以 0 表示有序集第一个成员,以 1表示有序集第二个成员,以此类推
  * 也可以使用负数下标,以 -1 表示最后一个成员, -2 表示倒数第二个成员,以此类推
  * 超出范围的下标并不会引起错误.当 start 的值比有序集的最大下标还要大,或是 start > stop 时, ZRANGE命令只是简单地返回一个空列表
  * 假如 stop 参数的值比有序集的最大下标还要大,那么 Redis 将 stop 当作最大下标来处理
  * 可以通过使用 WITHSCORES,来让成员和它的 score 值一并返回,返回列表以value1,score1, ..., valueN,scoreN 的格式表示
  * 客户端库可能会返回一些更复杂的数据类型,比如数组,元组等

* 时间复杂度:O(log(N)+M), N 为有序集的基数,而 M 为结果集的基数

* 返回值:指定区间内,带有 score 值(可选)的有序集成员的列表

## ZREVRANGE

* zrevrange key start stop [WITHSCORES]:返回有序集 key 中,指定区间内的成员
  
  * 其中成员的位置按 score 值递减(从大到小)来排列
  * 具有相同 score 值的成员按字典序的逆序(reverse lexicographical order)排列
  * 除了成员按 score 值递减的次序排列这外, ZREVRANGE的其他方面和ZRANGE 命令一样

* 时间复杂度:O(log(N)+M), N 为有序集的基数,而 M 为结果集的基数

* 返回值:指定区间内,带有 score 值(可选)的有序集成员的列表

## ZRANGEBYSCORE

* zrangebyscore key min max [WITHSCORES] [LIMIT offset count]:返回key中所有 score介于 min 和 max 之间(包括等于min或max )的成员.有序集成员按 score 值递增(从小到大)次序排列
  
  * 具有相同 score 值的成员按字典序(lexicographical order)来排列
  
  * LIMIT 参数指定返回结果的数量及区间(就像SQL中的SELECT LIMIT offset,count ),当 offset 很大时,定位 offset 的操作可能需要遍历整个有序集,此过程最坏复杂度为 O(N) 时间
  
  * WITHSCORES决定结果集是单单返回有序集的成员,还是将有序集成员及其score 值一起返回
  
  * 区间及无限
    
    * min 和 max 可以是 -inf 和 +inf ,这样一来,你就可以在不知道有序集的最低和最高 score 值的情况下,使用 ZRANGEBYSCORE 这类命令
    
    * 默认情况下,区间的取值使用闭区间 (小于等于或大于等于),你也可以通过给参数前增加 ( 符号来使用可选的开区间 (小于或大于)
      
      ```shell
      ZRANGEBYSCORE zset (1 5
      # 返回所有符合条件 1 < score <= 5 的成员
      ZRANGEBYSCORE zset (5 (10
      # 则返回所有符合条件 5 < score < 10 的成员
      ```

* 时间复杂度:O(log(N)+M), N 为有序集的基数, M 为被结果集的基数

* 返回值:指定区间内,带有 score 值(可选)的有序集成员的列表

## ZREVRANGEBYSCORE

* zrevrangebyscore key max min [WITHSCORES] [LIMIT offset count]:返回key中score 值介于 max 和 min 之间(默认包括等于 max 或 min )的所有的成员.有序集成员按 score 值递减(从大到小)的次序排列
  
  * 具有相同 score 值的成员按字典序的逆序(reverse lexicographical order )排列
  * 除了成员按 score 值递减的次序排列这一点外, 其他方面和 ZRANGEBYSCORE 命令一样

* 时间复杂度:O(log(N)+M), N 为有序集的基数, M 为结果集的基数

* 返回值:指定区间内,带有 score 值(可选)的有序集成员的列表
  示例代码:

## ZRANK

* zrank key member:返回key 中member 的排名.其中有序集成员按 score 值递增顺序排列
  * 排名以 0 为底,也就是说, score 值最小的成员排名为 0
  * 使用 ZREVRANK 命令可以获得成员按 score 值递减(从大到小)排列的排名
* 时间复杂度:O(log(N))
* 返回值:如果 member 是key 的成员,返回 member排名;如果member不是key的成员,返回 nil

## ZREVRANK

* zrevrank key member:返回key 中member 的排名,其中有序集成员按 score 值递减排序
  * 排名以 0 为底,也就是说, score 值最大的成员排名为 0
  * 使用 ZRANK 命令可以获得成员按 score 值递增(从小到大)排列的排名
* 时间复杂度:O(log(N))
* 返回值:如果 member 是key 的成员,返回 member 的排名;如果 member 不是key 的成员,返回 nil

## ZREMRANGEBYRANK

* ZREMRANGEBYRANK key start stop:移除有序集 key 中,指定排名(rank)区间内的所有成员
  * 区间分别以下标参数 start 和 stop 指出,包含 start 和 stop 在内
  * start和stop都以0 为底,0表示有序集第一个成员,1表示有序集第二个成员,以此类推
  * 也可以使用负数下标,以 -1 表示最后一个成员, -2 表示倒数第二个成员,以此类推
* 时间复杂度:O(log(N)+M), N 为有序集的基数,而 M 为被移除成员的数量
* 返回值:被移除成员的数量

## ZREMRANGEBYSCORE

* zremrangebyscore key min max:移除有序集 key 中,所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员
* 时间复杂度:O(log(N)+M), N 为有序集的基数,而 M 为被移除成员的数量
* 返回值:被移除成员的数量

## ZINTERSTORE

* ZINTERSTORE destination numkeys key [key ...] [WEIGHTS weight [weight ...]]
  \[AGGREGATE SUM|MIN|MAX\]:计算指定的一个或多个有序集的交集,其中指定 key 的数量必须以 numkeys 参数指定,并将该交集(结果集)储存到 destination.默认情况下,结果集中某个成员的 score 值是所有指定集下该成员 score 值之和

* 时间复杂度:O(N\*K)+O(M\*log(M)),N为指定 key 中基数最小的有序集,K为指定有序集的数量,M 为结果集的基数

* 返回值:保存到 destination 的结果集的基数

## ZUNIONSTORE

* ZUNIONSTORE destination numkeys key [key ...] [WEIGHTS weight [weight ...]]
  \[AGGREGATE SUM|MIN|MAX]:计算指定的一个或多个有序集的并集,其中指定 key 的数量必须以 numkeys 参数指定,并将该并集(结果集)储存到 destination.默认情况下,结果集中某个成员的 score 值是所有指定集下该成员 score 值之 和
  * 使用 WEIGHTS 选项,可以为 每个 指定有序集 分别 指定一个乘法因子,每个指定有序集的所有成员的 score 值在传递给聚合函数之前都要先乘以该有序集的因子
  * 如果没有指定 WEIGHTS 选项,乘法因子默认设置为 1
  * 使用 AGGREGATE 选项,可以指定并集的结果集的聚合方式.默认使用的参数 SUM ,可以将所有集合中某个成员的 score 值之 和 作为结果集中该成员的 score 值
  * 使用MIN ,可以将所有集合中某个成员的 最小 score 值作为结果集中该成员的 score 值
  * 参数 MAX 则是将所有集合中某个成员的 最大 score 值作为结果集中该成员的 score 值
* 时间复杂度:O(N)+O(M log(M)), N 为指定有序集基数的总和, M 为结果集的基数
* 返回值:保存到 destination 的结果集的基数



# Bitmaps(位图)



## getbit



* getbit key offset:获取指定key对应偏移量上的bit值



## setbit



* setbit key offset value:设置指定key对应偏移量上的bit值,value只能是1或0



## bitop



* bitop op destKey key1 [key2...]:对指定key按位进行交、并、非、异或操作,并将结果保存到destKey中
  * and:交
  * or:并
  * not:非
  * xor:异或



## bitcount



* bitcount key [start end]:统计指定key中1的数量



# HyperLogLog(基数)



## PFADD



* PFADD key element [element ...]:将任意数量的元素添加到指定的 HyperLogLog 里面.命令可能会对HyperLogLog 进行修改,以便反映新的基数估算值,如果 HyperLogLog 的基数估算值在命令执行之后出现了变化,那么命令返回 1 ,否则返回 0
* 命令的复杂度为 O(N) ,N 为被添加元素的数量
* 返回值:基数估算值在命令执行之后出现了变化,则返回 1 ,否则返回 0



## PFCOUNT



* PFCOUNT key [key ...]:当只给定一个 HyperLogLog 时,命令返回给定 HyperLogLog 的基数估算值.当给定多个 HyperLogLog 时,命令会先对给定的 HyperLogLog 进行并集计算,得出一个合并后的HyperLogLog,然后返回这个合并 HyperLogLog 的基数估算值作为命令的结果.合并得出的HyperLogLog 不会被储存,使用之后就会被删掉
* 作用于单个 HyperLogLog 时, 复杂度为 O(1) , 并且具有非常低的平均常数时间
* 作用于多个 HyperLogLog 时,复杂度为 O(N) ,并且常数时间也比处理单个 HyperLogLog 时要大得多



## PFMERGE



* PFMERGE destkey sourcekey [sourcekey ...]
  将多个 HyperLogLog 合并为一个 HyperLogLog ，合并后的 HyperLogLog 的基数估算值是通过对所有
  给定 HyperLogLog 进行并集计算得出的。
  命令的复杂度为 O(N) ， 其中 N 为被合并的 HyperLogLog 数量， 不过这个命令的常数复杂度比较高



# GEO(地理位置)



## geoadd



* geoadd key longitude latitude member [longitude latitude member ...]:添加坐标点



## geopos



* geopos key member [member ...]:获取坐标点



## geodist



* geodist key member1 member2 [unit]:计算坐标点距离



## georadius



* georadius key longitude latitude radius m|km|ft|mi [withcoord] [withdist] [withhash] [count count]:添加坐标点



## georadiusbymember



* georadiusbymember key member radius m|km|ft|mi [withcoord] [withdist] [withhash] [count count]:获取坐标点



## geohash



* geohash key member [member ...]:计算经纬度



# Pub/Sub(发布/订阅)



## PUBLISH



* publish channel message:将信息 message 发送到指定的频道 channel
* 时间复杂度:O(N+M),其中 N 是频道 channel 的订阅者数量,而 M 则是使用模式订阅的客户端的数量
* 返回值:接收到信息 message 的订阅者数量



## SUBSCRIBE



* SUBSCRIBE channel [channel ...]:订阅指定的一个或多个频道的信息
* 时间复杂度:O(N),其中 N 是订阅的频道的数量
* 返回值:接收到的信息(请参见下面的代码说明)



## PSUBSCRIBE



* psubscribe pattern [pattern ...]:订阅一个或多个符合指定模式的频道
  * 每个模式以 * 作为匹配符,比如 it* 匹配所有以 it 开头的频道( it.news,it.blog等), news.* 匹配所有以 news. 开头的频道( news.it ,news.global.today 等等),诸如此类
* 时间复杂度:O(N), N 是订阅的模式的数量
* 返回值:接收到的信息(请参见下面的代码说明)



## UNSUBSCRIBE



* unsubscribe [channel [channel ...]]:指示客户端退订指定的频道
  * 如果没有频道被指定,一个无参的 UNSUBSCRIBE被执行,那么客户端使用 SUBSCRIBE 命令订阅的所有频道都会被退订.在这种情况下,命令会返回一个信息,告知客户端所有被退订的频道
* 时间复杂度:O(N) , N 是客户端已订阅的频道的数量
* 返回值:这个命令在不同的客户端中有不同的表现



## PUNSUBSCRIBE



* punsubscribe [pattern [pattern ...]]:指示客户端退订所有指定模式
  * 如果没有模式被指定,一个无参的 PUNSUBSCRIBE被执行,那么客户端使用 PSUBSCRIBE 命令订阅的所有模式都会被退订.在这种情况下,命令会返回一个信息,告知客户端所有被退订的模式
* 时间复杂度:O(N+M) ,其中 N 是客户端已订阅的模式的数量, M 则是系统中所有客户端订阅的模式的数量
* 返回值:这个命令在不同的客户端中有不同的表现



# Transaction(事务)



## WATCH



* WATCH key [key ...]:监视一个key,如果在事务执行之前这个key被其他命令所改动,那么事务将被打断

* 时间复杂度:O(1)

* 返回值:总是返回 OK



## UNWATCH



* 取消 WATCH 命令对所有 key 的监视
  
  * 如果在执行 WATCH 命令之后, EXEC 命令或 DISCARD 命令先被执行了的话,那么就不需要再执行 UNWATCH 了
  * EXEC 命令会执行事务,因此 WATCH 命令的效果已经产生了;而 DISCARD 命令在取消事务的同时也会取消所有对 key 的监视,因此这两个命令执行之后,就没有必要执行UNWATCH 了

* 时间复杂度:O(1)

* 返回值:总是 OK



## MULTI



* 标记一个事务块的开始.事务块内的多条命令会按照先后顺序被放进一个队列当中,最后由 EXEC 命令原子性(atomic)地执行
* 时间复杂度:O(1)
* 返回值:总是返回 OK



## DISCARD



* 取消事务,放弃执行事务块内的所有命令.如果正在使用 WATCH 命令监视某个(或某些) key,那么取消所有监视,等同于执行命令 UNWATCH
* 时间复杂度:O(1)
* 返回值:总是返回 OK



## EXEC



* 执行所有事务块内的命令
  * 假如某个(或某些)key正处于 WATCH的监视下,且事务块中有和这个key 相关的命令,那么 EXEC 只在这个key没有被其他命令所改动的情况下执行并生效,否则该事务被打断
* 时间复杂度:事务块内所有命令的时间复杂度的总和
* 返回值:事务块内所有命令的返回值,按命令执行的先后顺序排列;当操作被打断时,返回空值 nil



# Script(脚本)



## EVAL

* eval script numkeys key [key ...] arg [arg ...]:通过Lua解释器,使用 EVAL对Lua脚本进行求值
  * script 参数是一段 Lua 5.1 脚本程序,它会被运行在 Redis 服务器上下文中,这段脚本不必定义为一个 Lua 函数
  * numkeys 参数用于指定键名参数的个数
  * 键名参数 key [key ...] 从 EVAL 的第三个参数开始算起,表示在脚本中所用到的那些 Redis 键,这些键名参数可以在 Lua 中通过全局变量 KEYS 数组,用 1 为基址的形式访问( KEYS[1] , KEYS[2] ,以此类推)
  * 在命令的最后,那些不是键名参数的附加参数 arg [arg ...] ,可以在 Lua 中通过全局变量 ARGV 数组访问,访问的形式和 KEYS 变量类似( ARGV[1] 、 ARGV[2] ,诸如此类)
* 时间复杂度:EVAL 和 EVALSHA 可以在 O(1) 复杂度内找到被执行的脚本,其余的复杂度取决于执行的脚本本身

```shell
> eval "return {KEYS[1],KEYS[2],ARGV[1],ARGV[2]}" 2 key1 key2 first
second
1) "key1"
2) "key2"
3) "first"
4) "second"
```

* 其中`return {KEYS[1],KEYS[2],ARGV[1],ARGV[2]}`是被求值的Lua脚本,数字2 指定了键名参数的数量, key1 和 key2 是键名参数,分别使用 KEYS[1] 和 KEYS[2] 访问,而最后的 first 和 second 则是附加参数,可以通过 ARGV[1] 和 ARGV[2] 访问它们
* 在 Lua 脚本中,可以使用两个不同函数来执行 Redis 命令,它们分别是:
  * redis.call()
  * redis.pcall()
  * 这两个函数的唯一区别在于处理执行命令所产生错误的方式不同,它们的参数可以是任何格式良好的Redis 命令:`eval "return redis.call('set',KEYS[1],'bar')" 1 foo`
* 要求使用正确的形式来传递键(key)是有原因的,因为不仅仅是 EVAL 这个命令,所有的 Redis 命令,在执行之前都会被分析,籍此来确定命令会对哪些键进行操作.因此,对于 EVAL 命令来说,必须使用正确的形式来传递键,才能确保分析工作正确地执行
* 使用正确的形式来传递键还有很多其他好处,它的一个特别重要的用途就是确保 Redis 集群可以将你的请求发送到正确的集群节点(对 Redis 集群的工作还在进行当中,但是脚本功能被设计成可以与集群功能保持兼容).不过,这条规矩并不是强制性的,从而使得用户有机会滥用(abuse) Redis 单实例配置(single instance configuration),代价是这样写出的脚本不能被 Redis 集群所兼容

### Lua和Redis数据类型转换

* 当 Lua 通过 call() 或 pcall() 函数执行 Redis 命令的时候,命令的返回值会被转换成 Lua 数据结构
* 当 Lua 脚本在 Redis 内置的解释器里运行时, Lua 脚本的返回值也会被转换成 Redis 协议,然后由 EVAL 将值返回给客户端
* 数据类型之间的转换遵循这样一个设计原则:如果将一个 Redis 值转换成 Lua 值,之后再将转换所得的 Lua 值转换回 Redis 值,那么这个转换所得的 Redis 值应该和最初时的 Redis 值一样
* Lua 类型和 Redis 类型之间存在着一一对应的转换关系,从 Redis 转换到 Lua:
  * Redis integer reply -> Lua number / Redis 整数转换成 Lua 数字
  * Redis bulk reply -> Lua string / Redis bulk 回复转换成 Lua 字符串
  * Redis multi bulk reply -> Lua table (may have other Redis data types nested)/Redis 多条 bulk 回复转换成 Lua 表,表内可能有其他别的 Redis 数据类型
  * Redis status reply -> Lua table with a single ok field containing the status/ Redis 状态回复转换成 Lua 表,表内的 ok 域包含了状态信息
  * Redis error reply -> Lua table with a single err field containing the error/ Redis 错误回复转换成 Lua 表,表内的 err 域包含了错误信息
  * Redis Nil bulk reply and Nil multi bulk reply -> Lua false boolean type /Redis 的 Nil 回复和 Nil 多条回复转换成 Lua 的布尔值 false
* 从 Lua 转换到 Redis:
  * Lua number -> Redis integer reply / Lua 数字转换成 Redis 整数
  * Lua string -> Redis bulk reply / Lua 字符串转换成 Redis bulk 回复
  * Lua table (array) -> Redis multi bulk reply / Lua 表(数组)转换成 Redis 多条 bulk 回复
  * Lua table with a single ok field -> Redis status reply / 一个带单个 ok 域的 Lua 表,转换成Redis 状态回复
  * Lua table with a single err field -> Redis error reply / 一个带单个 err 域的 Lua 表,转换成 Redis 错误回复
  * Lua boolean false -> Redis Nil bulk reply / Lua的布尔值 false 转换成 Redis的 Nil bulk 回复
* 从 Lua 转换到 Redis 有一条额外的规则,这条规则没有和它对应的从 Redis 转换到 Lua的规则:
  * Lua boolean true -> Redis integer reply with value of 1 / Lua 布尔值 true转换成 Redis 整数回复中的 1

```shell
> eval "return 10" 0
(integer) 10
> eval "return {1,2,{3,'Hello World!'}}" 0
1) (integer) 1
2) (integer) 2
3) 1) (integer) 3
2) "Hello World!"
> eval "return redis.call('get','foo')" 0
"bar"
```

* 在上面的三个代码示例里,前两个演示了如何将 Lua 值转换成 Redis 值,最后一个例子更复杂一些,它演示了一个将 Redis 值转换成 Lua 值,然后再将 Lua 值转换成 Redis值的类型转过程

### 脚本的原子性

* Redis 使用单个 Lua 解释器去运行所有脚本,并且, Redis 也保证脚本会以原子性(atomic)的方式执行:当某个脚本正在运行的时候,不会有其他脚本或 Redis 命令被执行
* 这和使用 MULTI / EXEC 包围的事务很类似
* 在其他别的客户端看来,脚本的效果要么是不可见的,要么就是已完成的
* 执行一个运行缓慢的脚本并不是一个好主意,写一个跑得很快很顺溜的脚本并不难,因为脚本的运行开销非常少,但是当不得不使用一些跑得比较慢的脚本时,其他客户端会因为服务器正忙而无法执行命令

### 错误处理

* redis.call() 在执行命令的过程中发生错误时,脚本会停止执行,并返回一个脚本错误,错误的输出信息会说明错误造成的原因
* redis.pcall() 出错时并不引发(raise)错误,而是返回一个带 err 域的 Lua 表(table),用于表示错误

### 带宽和 EVALSHA

* EVAL 命令要求在每次执行脚本的时候都发送一次脚本主体,Redis有一个内部的缓存机制,因此它不会每次都重新编译脚本,不过在很多场合,付出无谓的带宽来传送脚本主体并不是最佳选择
* 为了减少带宽的消耗, Redis 实现了 EVALSHA 命令,它的作用和 EVAL 一样,都用于对脚本求值,但它接受的第一个参数不是脚本,而是脚本的 SHA1 校验和(sum)
* EVALSHA 命令的表现如下:
  * 如果服务器还记得指定的 SHA1 校验和所指定的脚本,那么执行这个脚本
  * 如果服务器不记得指定的 SHA1 校验和所指定的脚本,那么它返回一个特殊的错误,提醒用户使用 EVAL 代替 EVALSHA
* 客户端库的底层实现可以一直乐观地使用 EVALSHA 来代替 EVAL ,并期望着要使用的脚本已经保存在服务器上了,只有当 NOSCRIPT 错误发生时,才使用 EVAL 命令重新发送脚本,这样就可以最大限度地节省带宽
* 这也说明了执行 EVAL 命令时,使用正确的格式来传递键名参数和附加参数的重要性:因为如果将参数硬写在脚本中,那么每次当参数改变的时候,都要重新发送脚本,即使脚本的主体并没有改变,相反,通过使用正确的格式来传递键名参数和附加参数,就可以在脚本主体不变的情况下,直接使用 EVALSHA 命令对脚本进行复用,免去了无谓的带宽消耗

### 脚本缓存

* Redis 保证所有被运行过的脚本都会被永久保存在脚本缓存当中,这意味着,当 EVAL命令在一个 Redis 实例上成功执行某个脚本之后,随后针对这个脚本的所有 EVALSHA 命令都会成功执行
* 刷新脚本缓存的唯一办法是显式地调用 SCRIPT FLUSH,这个命令会清空运行过的所有脚本的缓存.通常只有在云计算环境中, Redis 实例被改作其他客户或者别的应用程序的实例时,才会执行这个命令
* 缓存可以长时间储存而不产生内存问题的原因是,它们的体积非常小,而且数量也非常少,即使脚本在概念上类似于实现一个新命令,即使在一个大规模的程序里有成百上千的脚本,即使这些脚本会经常修改,即便如此,储存这些脚本的内存仍然是微不足道的
* Redis不移除缓存中的脚本实际上是一个好主意.比如说,对于一个和 Redis 保持持久化链接的程序来说,执行过一次的脚本会一直保留在内存当中,因此它可以在流水线中使用 EVALSHA 命令而不必担心因为找不到所需的脚本而产生错误

### SCRIPT 命令

* Redis 提供了以下几个 SCRIPT 命令,用于对脚本子系统(scripting subsystem)进行控制:
  * SCRIPT FLUSH:清除所有脚本缓存
  * SCRIPT EXISTS:根据指定的脚本校验和,检查指定的脚本是否存在于脚本缓存
  * SCRIPT LOAD:将一个脚本装入脚本缓存,但并不立即运行它
  * SCRIPT KILL :杀死当前正在运行的脚本

### 纯函数脚本

* 在编写脚本方面,一个重要的要求就是,脚本应该被写成纯函数,具有以下属性:
  * 对于同样的数据集输入,指定相同的参数,脚本执行的 Redis 写命令总是相同的.脚本执行的操作不能依赖于任何隐藏(非显式)数据,不能依赖于脚本在执行过程中,或脚本在不同执行时期之间可能变更的状态,并且它也不能依赖于任何来自 I/O设备的外部输入
* 使用系统时间,调用像 RANDOMKEY 那样的随机命令,或者使用 Lua 的随机数生成器,类似以上的这些操作,都会造成脚本的求值无法每次都得出同样的结果.为了确保脚本符合上面所说的属性, Redis 做了以下工作:
  * Lua 没有访问系统时间或者其他内部状态的命令
  * Redis 会返回错误,阻止这样的脚本运行:这些脚本在执行随机命令之后(比如 RANDOMKEY 、 SRANDMEMBER 或 TIME 等),还会执行可以修改数据集的 Redis命令.如果脚本只是执行只读操作,那么就没有这一限制。注意,随机命令并不一定就指那些带 RAND 字眼的命令,任何带有非确定性的命令都会被认为是随机命令,比如 TIME 命令就是这方面的一个很好的例子
  * 每当从 Lua 脚本中调用那些返回无序元素的命令时,执行命令所得的数据在返回给 Lua 之前会先执行一个静默的字典序排序.举个例子,因为 Redis 的 Set 保存的是无序的元素,所以在 Redis 命令行客户端中直接执行 SMEMBERS ,返回的元素是无序的,但是,假如在脚本中执行 redis.call("smembers", KEYS[1]) ,那么返回的总是排过序的元素
  * 对 Lua 的伪随机数生成函数 math.random 和 math.randomseed 进行修改,使得每次在运行新脚本的时候,总是拥有同样的 seed 值。这意味着,每次运行脚本时,只要不使用math.randomseed ,那么 math.random 产生的随机数序列总是相同的
  * 尽管有那么多的限制,但用户还是可以用一个简单的技巧写出带随机行为的脚本
  * Redis 实现保证 math.random 和 math.randomseed 的输出和运行 Redis 的系统架构无关,无论是 32 位还是 64 位系统,无论是小端还是大端系统,这两个函数的输出总是相同的

### 全局变量保护

* 为了防止不必要的数据泄漏进 Lua 环境, Redis 脚本不允许创建全局变量
* 如果一个脚本需要在多次执行之间维持某种状态,它应该使用 Redis key 来进行状态保存
* 企图在脚本中访问一个全局变量将引起脚本停止, EVAL 命令会返回一个错误
* Lua 的 debug 工具或其他设施,比如meta table ,都可以用于实现全局变量保护
* 一旦用户在脚本中混入了Lua 全局状态,那么 AOF 持久化和复制都会无法保证
* 将脚本中用到的所有变量都使用 local 关键字定义为局部变量可避免引入全局变量

### 库

* Redis 内置的 Lua 解释器加载了以下 Lua 库:
  * base
  * table
  * string
  * math
  * debug
  * cjson
  * cmsgpack
* cjson 库可以让 Lua 以非常快的速度处理 JSON 数据,除此之外,其他别的都是Lua 的标准库
* 每个 Redis 实例都保证会加载上面列举的库,从而确保每个 Redis 脚本的运行环境都是相同的

### 使用脚本散发 Redis 日志

* 在 Lua 脚本中,可以通过调用 redis.log 函数来写 Redis 日志(log):redis.log(loglevel, message)
* message 参数是一个字符串,而 loglevel 参数可以是以下任意一个值:
  * redis.LOG_DEBUG
  * redis.LOG_VERBOSE
  * redis.LOG_NOTICE
  * redis.LOG_WARNING
* 上面的这些等级(level)和标准 Redis 日志的等级相对应,对于脚本散发的日志,只有那些和当前 Redis 实例所设置的日志等级相同或更高级的日志才会被散发

### 沙箱和最大执行时间

* 脚本应该仅仅用于传递参数和对 Redis 数据进行处理,它不应该尝试去访问外部系统(比如文件系统),或者执行任何系统调用
* 脚本还有一个最大执行时间限制,它的默认值是 5 秒钟,一般正常运作的脚本通常可以在几分之几毫秒之内完成,花不了那么多时间,这个限制主要是为了防止因编程错误而造成的无限循环而设置的
* 最大执行时间的长短由 lua-time-limit 选项来控制(以毫秒为单位),可以通过编辑redis.conf 文件或者使用 CONFIG GET 和 CONFIG SET 命令来修改它
* 当一个脚本达到最大执行时间的时候,它并不会自动被 Redis 结束,因为 Redis 必须保证脚本执行的原子性,而中途停止脚本的运行意味着可能会留下未处理完的数据在数据集(data set)里面
* 当脚本运行的时间超过最大执行时间后,以下动作会被执行:
  * Redis 记录一个脚本正在超时运行
  * Redis 开始重新接受其他客户端的命令请求,但是只有 SCRIPT KILL 和 SHUTDOWN NOSAVE 两个命令会被处理,对于其他命令请求, Redis 服务器只是简单地返回BUSY 错误
  * 可以使用 SCRIPT KILL 命令将一个仅执行只读命令的脚本杀死,因为只读命令并不修改数据,因此杀死这个脚本并不破坏数据的完整性
  * 如果脚本已经执行过写命令,那么唯一允许执行的操作就是 SHUTDOWN NOSAVE ,它通过停止服务器来阻止当前数据集写入磁盘

### pipeline context中的 EVALSHA

* 在pipeline请求的上下文中使用 EVALSHA 命令时,要特别小心,因为在流水线中,必须保证命令的执行顺序
* 一旦在流水线中因为 EVALSHA 命令而发生 NOSCRIPT 错误,那么这个流水线就再也没有办法重新执行了,否则的话,命令的执行顺序就会被打乱
* 为了防止出现以上所说的问题,客户端库实现应该实施以下的其中一项措施:
  * 总是在流水线中使用 EVAL 命令
  * 检查流水线中要用到的所有命令,找到其中的 EVAL 命令,并使用 SCRIPT EXISTS命令检查要用到的脚本是不是全都已经保存在缓存里面了.如果所需的全部脚本都可以在缓存里找到,那么就可以放心地将所有 EVAL 命令改成 EVALSHA 命令,否则的话,就要在流水线的顶端(top)将缺少的脚本用 SCRIPT LOAD 命令加上去

## EVALSHA

* evalsha sha1 numkeys key [key ...] arg [arg ...]:根据指定的 sha1 校验码,对缓存在服务器中的脚本进行求值
  
  * 将脚本缓存到服务器的操作可以通过 SCRIPT LOAD 命令进行
  * 这个命令的其他地方,比如参数的传入方式,都和 EVAL 命令一样

* 时间复杂度:根据脚本的复杂度而定
  
  ```shell
  redis> SCRIPT LOAD "return 'hello moto'"
  "232fd51614574cf0867b83d384a5e898cfd24e5a"
  redis> EVALSHA "232fd51614574cf0867b83d384a5e898cfd24e5a" 0
  "hello moto"
  ```

## SCRIPT LOAD

* script load script:将脚本 script 添加到脚本缓存中,但并不立即执行这个脚本
  
  * EVAL 命令也会将脚本添加到脚本缓存中,但是它会立即对输入的脚本进行求值
  * 如果指定的脚本已经在缓存里面了,那么不做动作
  * 在脚本被加入到缓存之后,通过 EVALSHA 命令,可以使用脚本的 SHA1 校验和来调用这个脚本
  * 脚本可以在缓存中保留无限长的时间,直到执行 SCRIPT FLUSH 为止

* 时间复杂度:O(N) , N 为脚本的长度(以字节为单位)

* 返回值:指定 script 的 SHA1 校验和
  
  ```shell
  redis> SCRIPT LOAD "return 'hello moto'"
  "232fd51614574cf0867b83d384a5e898cfd24e5a"
  redis> EVALSHA 232fd51614574cf0867b83d384a5e898cfd24e5a 0
  "hello moto"
  ```

## SCRIPT EXISTS

* script exists script [script ...]:指定一个或多个脚本的 SHA1 校验和,返回一个包含 0 和 1 的列表,表示校验和所指定的脚本是否已经被保存在缓存当中

* 时间复杂度:O(N) , N 为指定的 SHA1 校验和的数量

* 返回值:一个列表,包含 0 和 1 ,前者表示脚本不存在于缓存,后者表示脚本已经在缓存里面了;列表中的元素和指定的 SHA1 校验和保持对应关系,比如列表的第三个元素的值就表示第三个 SHA1 校验和所指定的脚本在缓存中的状态
  
  ```shell
  redis> SCRIPT LOAD "return 'hello moto'" # 载入一个脚本
  "232fd51614574cf0867b83d384a5e898cfd24e5a"
  redis> SCRIPT EXISTS 232fd51614574cf0867b83d384a5e898cfd24e5a
  \1) (integer) 1
  redis> SCRIPT FLUSH # 清空缓存
  OK
  redis> SCRIPT EXISTS 232fd51614574cf0867b83d384a5e898cfd24e5a
  \1) (integer) 0
  ```

## SCRIPT KILL

* 杀死当前正在运行的 Lua 脚本,当且仅当这个脚本没有执行过任何写操作时,这个命令才生效

* 这个命令主要用于终止运行时间过长的脚本,比如一个因为 BUG 而发生无限 loop 的脚本,诸如此类

* SCRIPT KILL 执行之后,当前正在运行的脚本会被杀死,执行这个脚本的客户端会从EVAL 命令的阻塞当中退出,并收到一个错误作为返回值

* 假如当前正在运行的脚本已经执行过写操作,那么即使执行 SCRIPT KILL ,也无法将它杀死,因为这是违反 Lua 脚本的原子性执行原则的。在这种情况下,唯一可行的办法是使用 SHUTDOWN NOSAVE 命令,通过停止整个 Redis 进程来停止脚本的运行,并防止不完整(half-written)的信息被写入数据库中

* 时间复杂度:O(1)

* 返回值:执行成功返回 OK ,否则返回一个错误
  
  ```shell
  # 没有脚本在执行时
  redis> SCRIPT KILL
  (error) ERR No scripts in execution right now.
  # 成功杀死脚本时
  redis> SCRIPT KILL
  OK
  (1.30s)
  # 尝试杀死一个已经执行过写操作的脚本,失败
  redis> SCRIPT KILL
  (error) ERR Sorry the script already executed write commands against
  the dataset. You can either wait the script termination or kill the
  server in an hard way using the SHUTDOWN NOSAVE command.
  (1.69s)
  # 以下是脚本被杀死之后,返回给执行脚本的客户端的错误:
  redis> EVAL "while true do end" 0
  (error) ERR Error running script (call to
  f_694a5fe1ddb97a4c6a1bf299d9537c7d3d0f84e7): Script killed by user
  with SCRIPT KILL...
  (5.00s)
  ```

## SCRIPT FLUSH

* 清除所有 Lua 脚本缓存
* 时间复杂度:O(N) , N 为缓存中脚本的数量
* 返回值:总是返回 OK

# Connection(连接)

## AUTH

* AUTH password:通过设置配置文件中 requirepass 项的值(使用命令 CONFIG SET requirepass
  password ),可以使用密码来保护 Redis 服务器
  * 如果开启了密码保护,在每次连接 Redis 服务器之后,就要使用 AUTH 命令解锁,解锁之后才能使用其他 Redis 命令
  * 如果 AUTH 命令指定的密码 password 和配置文件中的密码相符的话,服务器会返回OK 并开始接受命令输入
  * 假如密码不匹配的话,服务器将返回一个错误,并要求客户端需重新输入密码
  * 因为 Redis 高性能的特点,在很短时间内尝试猜测非常多个密码是有可能的,因此要确保使用的密码足够复杂和足够长,以免遭受密码猜测攻击
* 时间复杂度:O(1)
* 返回值:密码匹配时返回 OK ,否则返回一个错误

## PING

* 使用客户端向 Redis 服务器发送一个 PING ,如果服务器运作正常的话,会返回一个PONG
* 通常用于测试与服务器的连接是否仍然生效,或者用于测量延迟值
* 时间复杂度:O(1)
* 返回值:如果连接正常就返回一个 PONG ,否则返回一个连接错误

## SELECT

* select index:切换到指定的数据库,数据库索引号 index 用数字值指定,以 0 作为起始索引值
* 时间复杂度:O(1)
* 返回值:OK

## ECHO

* echo message:打印一个特定的信息 message ,测试时使用
* 时间复杂度:O(1)
* 返回值:message 自身

## QUIT

* 关闭与当前客户端的连接,一旦所有等待中的回复(如果有的话)顺利写入到客户端,连接就会被关闭
* 时间复杂度:O(1)
* 返回值:总是返回 OK (但是不会被打印显示,因为当时 Redis-cli 已经退出)

# Server(服务器)

## TIME

* 返回当前服务器时间
* 时间复杂度:O(1)
* 返回值:一个包含两个字符串的列表: 第一个字符串是当前时间(以 UNIX 时间戳格式表示),而第二个字符串是当前这一秒钟已经逝去的微秒数

## DBSIZE

* 返回当前数据库的 key 的数量
* 时间复杂度:O(1)
* 返回值:当前数据库的 key 的数量

## BGREWRITEAOF

* 执行一个 AOF 文件 重写操作
  * 重写会创建一个当前 AOF 文件的体积优化版本
  * 即使该命令执行失败,也不会有任何数据丢失,因为旧的 AOF 文件在重写成功之前不会被修改
  * 重写操作只会在没有其他持久化工作在后台执行时被触发:如果 Redis 的子进程正在执行快照的保存工作, 那么 AOF 重写的操作会被预定,等到保存工作完成之后再执行 AOF 重写.这种情况下, BGREWRITEAOF的返回值仍然是 OK ,但还会加上一条额外的信息,说明重写要等到保存操作完成之后才能执行
  * 可以使用 INFO 命令查看 BGREWRITEAOF 是否被预定
  * 如果已经有别的 AOF 文件重写在执行,BGREWRITEAOF 返回错误,并且新的 BGREWRITEAOF 请求也不会被预定到下次执行
  * AOF 重写由 Redis 自行触发, BGREWRITEAOF 仅仅用于手动触发重写操作
* 时间复杂度:O(N), N 为要追加到 AOF 文件中的数据数量
* 返回值:反馈信息

## BGSAVE

* 在后台异步(Asynchronously)保存当前数据库的数据到磁盘
  * BGSAVE 命令执行之后立即返回 OK ,然后 Redis fork 出一个新子进程,原来的 Redis进程(父进程)继续处理客户端请求,而子进程则负责将数据保存到磁盘,然后退出
  * 客户端可以通过 LASTSAVE 命令查看相关信息,判断 BGSAVE 命令是否执行成功
* 时间复杂度:O(N), N 为要保存到数据库中的 key 的数量
* 返回值:反馈信息
  示例代码:

## SAVE

* 执行一个同步保存操作,将当前 Redis 实例的所有数据快照(snapshot)以RDB 文件的形式保存到硬盘
  * 在生产环境很少执行 SAVE 操作,因为它会阻塞所有客户端,保存数据库的任务通常由 BGSAVE 命令异步地执行.然而,如果负责保存数据的后台子进程不幸出现问题时, SAVE 可以作为保存数据的最后手段来使用
* 时间复杂度:O(N), N 为要保存到数据库中的 key 的数量
* 返回值:保存成功时返回 OK 

## LASTSAVE

* 返回最近一次 Redis 成功将数据保存到磁盘上的时间,以 UNIX 时间戳格式表示
* 时间复杂度:O(1)
* 返回值:一个 UNIX 时间戳

## SLAVEOF

* slaveof host port:在 Redis 运行时动态地修改复制(replication)功能的行为
  * SLAVEOF host port可以将当前服务器转变为指定服务器的从属服务器(slave server)
  * 如果当前服务器已经是某个主服务器(master)的从服务器,那么执行SLAVEOF host port 将使当前服务器停止对旧主服务器的同步,丢弃旧数据集,转而开始对新主服务器进行同步
  * 对一个从属服务器执行命令 SLAVEOF NO ONE 将使得这个从属服务器关闭复制功能,并从从属服务器转变回主服务器,原来同步所得的数据集不会被丢弃
  * 利用SLAVEOF NO ONE 不会丢弃同步所得数据集这个特性,可以在主服务器失败的时候,将从属服务器用作新的主服务器,从而实现无间断运行
* 时间复杂度:SLAVEOF host port , O(N), N 为要同步的数据数量;SLAVEOF NO ONE , O(1)
* 返回值:总是返回 OK

## FLUSHALL

* 清空整个 Redis 服务器的数据(删除所有数据库的所有 key ).此命令从不失败
* 时间复杂度:尚未明确
* 返回值:总是返回 OK

## FLUSHDB

* 清空当前数据库中的所有 key,此命令从不失败
* 时间复杂度:O(1)
* 返回值:总是返回 OK

## SHUTDOWN

* shutdown [save|nosave]:停止所有客户端
  * 如果有至少一个保存点在等待,执行 SAVE 命令
  * 如果 AOF 选项被打开,更新 AOF 文件
  * 关闭 redis 服务器(server)
  * 如果持久化被打开的话, SHUTDOWN 命令会保证服务器正常关闭而不丢失任何数据
  * 假如只是单纯地执行 SAVE 命令,然后再执行 QUIT 命令,则没有这一保证—因为在执行 SAVE 之后、执行 QUIT 之前的这段时间中间,其他客户端可能正在和服务器进行通讯,这时如果执行 QUIT 就会造成数据丢失
  * SAVE:强制让数据库执行保存操作,即使没有设定(configure)保存点
  * NOSAVE:会阻止数据库执行保存操作,即使已经设定有一个或多个保存点
* 时间复杂度:不明确
* 返回值:执行失败时返回错误;执行成功时不返回任何信息,服务器和客户端的连接断开,客户端自动退出

## SLOWLOG

* slowlog subcommand [argument]:记录查询执行时间的日志系统
  * 查询执行时间指的是不包括像客户端响应(talking)、发送回复等 IO 操作,而单单是执行一个查询命令所耗费的时间
  * slow log 保存在内存里面,读写速度非常快,因此你可以放心地使用它,不必担心因为开启 slow log 而损害 Redis 的速度
* 时间复杂度:O(1)
* 返回值:取决于不同命令,返回不同的值

### 设置

* 可以通过改写redis.conf 文件或者用 CONFIG GET 和 CONFIG SET 命令对它们动态地进行修改

* slowlog-log-slower-than:决定要对执行时间大于多少微秒的查询进行记录
  
  ```shell
  # 让 slow log 记录所有查询时间大于等于 100 微秒的查询
  CONFIG SET slowlog-log-slower-than 100
  # 记录所有查询时间大于 1000 微秒的查询
  CONFIG SET slowlog-log-slower-than 1000
  ```

* slowlog-max-len:决定 slow log 最多能保存多少条日志, slow log本身是一个 FIFO 队列,当队列大小超过 slowlog-max-len 时,最旧的一条日志将被删除,而最新的一条日志加入到 slow log ,以此类推
  
  ```shell
  # 以下命令让 slow log 最多保存 1000 条日志
  CONFIG SET slowlog-max-len 1000
  # 使用 CONFIG GET 命令可以查询两个选项的当前值
  redis> CONFIG GET slowlog-log-slower-than
  1) "slowlog-log-slower-than"
  2) "1000"
  redis> CONFIG GET slowlog-max-len
  1) "slowlog-max-len"
  2) "1000"
  ```

### 查看

* 使用 SLOWLOG GET 或者 SLOWLOG GET number 命令,前者打印所有 slow log ,最大长度取决于 slowlog-max-len 选项的值,而 SLOWLOG GET number则只打印指定数量的日志

* 最新的日志会最先被打印
  
  ```shell
  redis> SLOWLOG GET
  1) (integer) 12 # 唯一性(unique)的日志标识符
  2) (integer) 1324097834 #被记录命令的执行时间点,以 UNIX 时间戳格式表示
  3) (integer) 16 # 查询执行时间,以微秒为单位
  4) 1) "CONFIG" # 执行的命令,以数组的形式排列
  2) "GET" #里完整的命令是 CONFIG GET slowlog-log-slower-than
  3) "slowlog-log-slower-than"
  2) 1) (integer) 11
  2) (integer) 1324097825
  3) (integer) 42
  4) 1) "CONFIG"
  2) "GET"
  3) "*"
  3) 1) (integer) 10
  2) (integer) 1324097820
  3) (integer) 11
  4) 1) "CONFIG"
  2) "GET"
  3) "slowlog-log-slower-than"
  ```

* 日志的唯一 id 只有在 Redis 服务器重启的时候才会重置,这样可以避免对日志的重复处理

* SLOWLOG LEN:查看当前日志的数量,和 slower-max-len不同的是,一个是当前日志的数量,一个是允许记录的最大日志的数量

* SLOWLOG RESET:清空slow log

## INFO

* INFO [section]:以一种易于解释且易于阅读的格式,返回关于 Redis 服务器的各种信息和统计数值,通过指定可选的参数 section ,可以让命令只返回某一部分的信息:
  * server:一般 Redis 服务器信息,包含以下域:
    * redis_version : Redis 服务器版本
    * redis_git_sha1 : Git SHA1
    * redis_git_dirty : Git dirty flag
    * os : Redis 服务器的宿主操作系统
    * arch_bits : 架构(32 或 64 位)
    * multiplexing_api : Redis 所使用的事件处理机制
    * gcc_version : 编译 Redis 时所使用的 GCC 版本
    * process_id : 服务器进程的 PID
    * run_id : Redis 服务器的随机标识符(用于 Sentinel 和集群)
    * tcp_port : TCP/IP 监听端口
    * uptime_in_seconds : 自 Redis 服务器启动以来,经过的秒数
    * uptime_in_days : 自 Redis 服务器启动以来,经过的天数
    * lru_clock : 以分钟为单位进行自增的时钟,用于 LRU 管理
  * clients : 已连接客户端信息,包含以下域:
    * connected_clients : 已连接客户端的数量(不包括通过从属服务器连接的客户端)
    * client_longest_output_list : 当前连接的客户端当中,最长的输出列表
    * client_longest_input_buf : 当前连接的客户端当中,最大输入缓存
    * blocked_clients:正在等待阻塞命令(BLPOP,BRPOP,BRPOPLPUSH)的客户端的数量
  * memory : 内存信息,包含以下域:
    * used_memory : 由 Redis 分配器分配的内存总量,以字节为单位
    * used_memory_human : 以人类可读的格式返回 Redis 分配的内存总量
    * used_memory_rss : 返回 Redis 已分配的内存总量,这个值和 top,ps 等命令的输出一致
    * used_memory_peak : Redis 的内存消耗峰值,单位为字节
    * used_memory_peak_human : 以人类可读的格式返回 Redis 的内存消耗峰值
    * used_memory_lua : Lua 引擎所使用的内存大小,单位字节
    * mem_fragmentation_ratio : used_memory_rss 和 used_memory 之间的比率
    * mem_allocator : 编译时指定,Redis使用的内存分配器,可以是libc,jemalloc或tcmalloc
  * 在理想情况下, used_memory_rss 的值应该只比 used_memory 稍微高一点儿
  * 当 rss > used ,且两者的值相差较大时,表示存在(内部或外部的)内存碎片
  * 内存碎片的比率可以通过 mem_fragmentation_ratio 的值看出
  * 当 used > rss 时,表示 Redis 的部分内存被操作系统换出到交换空间了,在这种情况下,操作可能会产生明显的延迟
  * 当 Redis 释放内存时,分配器可能会,也可能不会,将内存返还给操作系统
  * 如果 Redis 释放了内存,却没有将内存返还给操作系统,那么 used_memory 的值可能和操作系统显示的 Redis 内存占用并不一致
  * 查看 used_memory_peak 的值可以验证这种情况是否发生
    * persistence : RDB 和 AOF 的相关信息
    * stats : 一般统计信息
    * replication : 主/从复制信息
    * cpu : CPU 计算量统计信息
    * commandstats : Redis 命令统计信息
    * cluster : Redis 集群信息
    * keyspace : 数据库相关的统计信息
  * 除上面给出的这些值以外,参数还可以是下面这两个:
    * all : 返回所有信息
    * default : 返回默认选择的信息
  * 当不带参数直接调用 INFO 命令时,使用 default 作为默认参数
* 时间复杂度:O(1)

## CONFIG GET

* CONFIG GET parameter:取得运行中的 Redis 服务器的配置参数
  * parameter 作为搜索关键字,查找所有匹配的配置参数,其中参数和值以键值对的方式排列
  * CONFIG GET * ,可以列出 CONFIG GET 命令支持的所有参数
  * 所有被 CONFIG SET 所支持的配置参数都可以在配置文件 redis.conf 中找到,不过CONFIG GET 和 CONFIG SET 使用的格式和 redis.conf 文件所使用的格式有以下两点不同:
    * 10kb,2gb 这些在配置文件中所使用的储存单位缩写,不可以用在 CONFIG 命令中, CONFIG SET 的值只能通过数字值显式地设定.像 CONFIG SET xxx 1k 这样的命令是错误的,正确的格式是 CONFIG SET xxx 1000
    * save 选项在 redis.conf 中是用多行文字储存的,但在 CONFIG GET中,它只打印一行文字
* 时间复杂度:不明确
* 返回值:指定配置参数的值

## CONFIG SET

* CONFIG SET parameter value:动态地调整 Redis 服务器的配置而无须重启
* 时间复杂度:不明确
* 返回值:当设置成功时返回 OK ,否则返回一个错误

## CONFIG RESETSTAT

* 重置 INFO 命令中的某些统计数据,包括:
  * Keyspace hits (键空间命中次数)
  * Keyspace misses (键空间不命中次数)
  * Number of commands processed (执行命令的次数)
  * Number of connections received (连接服务器的次数)
  * Number of expired keys (过期 key 的数量)
  * Number of rejected connections (被拒绝的连接数量)
  * Latest fork(2) time(最后执行 fork(2) 的时间)
  * The aof_delayed_fsync counter(aof_delayed_fsync 计数器的值)
* 时间复杂度:O(1)
* 返回值:总是返回 OK

## DEBUG OBJECT

* debug object key:一个调试命令,它不应被客户端所使用,可查看 OBJECT
* 时间复杂度:O(1)
* 返回值:当 key 存在时,返回有关信息;当 key 不存在时,返回一个错误

## DEBUG SEGFAULT

* 执行一个不合法的内存访问从而让 Redis 崩溃,仅在开发时用于 BUG 模拟
* 时间复杂度:不明确
* 返回值:无

## MONITOR

* 实时打印出 Redis 服务器接收到的命令,调试用
* 时间复杂度:不明确
* 返回值:总是返回 OK

## SYNC

* 用于复制功能(replication)的内部命令
* 时间复杂度:不明确
* 返回值:不明确

## CLIENT LIST

* 以人类可读的格式,返回所有连接到服务器的客户端信息和统计数据
* 时间复杂度:O(N) , N 为连接到服务器的客户端数量
* 返回值:命令返回多行字符串,这些字符串按以下形式被格式化:
  * 每个已连接客户端对应一行,以LF分割
  * 每行字符串由一系列 属性=值 形式的域组成,每个域之间以空格分开,以下是域的含义:
    * addr : 客户端的地址和端口
    * fd : 套接字所使用的文件描述符
    * age : 以秒计算的已连接时长
    * idle : 以秒计算的空闲时长
    * flags : 客户端 flag
    * db : 该客户端正在使用的数据库 ID
    * sub : 已订阅频道的数量
    * psub : 已订阅模式的数量
    * multi : 在事务中被执行的命令数量
    * qbuf : 查询缓存的长度,0 表示没有查询在等待
    * qbuf-free : 查询缓存的剩余空间, 0 表示没有剩余空间
    * obl : 输出缓存的长度
    * oll : 输出列表的长度,当输出缓存没有剩余空间时,回复被入队到这个队列里
    * omem : 输出缓存的内存占用量
    * events : 文件描述符事件
    * cmd : 最近一次执行的命令
  * 客户端 flag 可以由以下部分组成:
    * O : 客户端是 MONITOR 模式下的从节点
    * S : 客户端是一般模式下的附属节点
    * M : 客户端是主节点
    * x : 客户端正在执行事务
    * b : 客户端正在等待阻塞事件
    * i : 客户端正在等待 VM I/O 操作
    * d : 一个受监视的键已被修改, EXEC 命令将失败
    * c : 在将回复完整地写出之后,关闭链接
    * u : 客户端未被阻塞
    * A : 尽可能快地关闭连接
    * N : 未设置任何 flag
  * 文件描述符事件可以是:
    * r : 客户端套接字(在事件 loop 中)是可读的
    * w : 客户端套接字(在事件 loop 中)是可写的
  * 为了 debug 的需要,经常会对域进行添加和删除,一个安全的 Redis 客户端应该可以对 CLIENT LIST 的输出进行相应的处理,比如忽略不存在的域,跳过未知域,诸如此类

## CLIENT KILL

* client kill ip:port:关闭地址为 ip:port 的客户端
  * ip:port 应该和 CLIENT LIST 命令输出的其中一行匹配
  * 因为 Redis 使用单线程设计,所以当 Redis 正在执行命令的时候,不会有客户端被断开连接
  * 如果要被断开连接的客户端正在执行命令,那么当这个命令执行之后,在发送下一个命令的时候,它就会收到一个网络错误,告知它自身的连接已被关闭
* 时间复杂度:O(N) , N 为已连接的客户端数量
* 返回值:当指定的客户端存在,且被成功关闭时,返回 OK

## CLIENT SETNAME

* client setname connection-name:为当前连接分配一个名字,这个名字会显示在 CLIENT LIST 命令的结果中,用于识别当前正在与服务器进行连接的客户端
  * Redis构建队列时,可以根据连接负责的任务,为信息生产者和信息消费者分别设置不同的名字
  * 名字使用 Redis 的字符串类型来保存,最大可以占用 512 MB 
  * 为了避免和CLIENT LIST 命令的输出格式发生冲突,名字里不允许使用空格
  * 要移除一个连接的名字,可以将连接的名字设为空字符串
  * 使用 CLIENT GETNAME 命令可以取出连接的名字
  * 新创建的连接默认是没有名字的
  * 在 Redis 应用程序发生连接泄漏时,为连接设置名字是一种很好的 debug 手段
* 时间复杂度:O(1)
* 返回值:设置成功时返回 OK

## CLIENT GETNAME

* 返回 CLIENT SETNAME 命令为连接设置的名字,因为新创建的连接默认是没有名字的,对于没有名字的连接, CLIENT GETNAME 返回空白回复
* 时间复杂度:O(1)
* 返回值:如果连接没有设置名字,那么返回空白回复;如果有设置名字,那么返回名字