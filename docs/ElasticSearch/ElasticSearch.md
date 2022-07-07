# ElasticSearch



# 概述

* 用数据库做搜索的效果:见**ElasticSearch.pptx-01**,不太靠谱,性能很差
* 全文检索:指计算机索引程序通过扫描文章中的每一个词,对每一个词建立一个索引,指明该词在文章中出现的次数和位置,当用户查询时,检索程序就根据事先建立的索引进行查找,并将查找的结果反馈给用户的检索方式.这个过程类似于通过字典中的检索字表查字的过程.**见ElasticSearch.pptx-02**
* Lucene,就是一个jar包,里面包含了封装好的各种建立倒排索引,以及进行搜索的代码,包括各种算法
* 倒排索引:通常从文章中搜索关键字是直接全文扫描文章,查看是否有关键字,倒排索引正好相反.先将文章分词建立关键字索引,搜索关键字的时候直接就能通过预先建立好的索引查找文章
* ES是基于Lucene的搜索引擎,是一个实时的分布式搜索和分析引擎,可用于全文搜索,结构化搜索以及分析
  * 全文搜索:将非结构化数据中的一部分信息提取出来,重新组织,使其变得有一定结构,然后对此有一定结构的数据进行搜索,从而达到搜索相对较快的目的
  * 结构化搜索:类似于从数据库中读取数据
  * 数据分析:电商网站,最近7天牙膏这种商品销量排名前10的商家有哪些等
* 高可用,多用户能力,多类型,多API,面相文档,异步写入,不需要其他组件,分发也是实时(push replication)
* 处理多租户(multitenancy)不需要特殊配置,而Solr需要更多的高级配置
* 扩展性好,自带分布式架构,但是只支持json格式文件,可集群处理PB级数据
* 基于restful接口,能够达到实时搜索,稳定,可靠,快速,比Solr更适合实时搜索
* 采用Gateway概念,使得完全备份更加简单
* 近实时的去索引数据,搜索数据,结构化搜索以及分析
* 更注重核心功能,更多的高级功能有插件完成
* 存储数据到ES叫做索引,但是索引之前需要确定文档的存放位置
* 集群可以有多个索引,每个索引可以包含多个类型,每个类型可以存放多个文档,每个文档可以有多个属性
* 当文档很大时,由于内存和硬盘限制,同时也为了提高效率,ES将索引分为若干分片,每个分片可以放在不同的服务器,这样可以实现多个服务器共同对外提供索引以及搜索服务
* 若首次提交数据时,字段类型错误,可能会影响字段的分析效果,进而影响索引,需要修改数据类型之后,再次新增或修改后,覆盖原先的索引
* ES的搜索包括document和field,document类似于表,field是字段.但是在创建document时,在9版本以前还有一个type类型,该type类型用来指定表的名称,实际上已经没有太大意义,但是不能少.type的名称可以是任意非关键字字符串,和field的type并非同一个
* ES在更新映射时,已经定义好的field的type不可修改,唯一的方法是将整个映射删除之后重新添加.查看映射:ip:port/documentname/typename/_mapping
* 在新增一个field时,除了类型(type)之外还需要设置搜索分词器和索引分词器,索引分词器越细粒度越好,而搜索分词器则需要稍微粗颗粒度些,如name:{type:text,analyzer:"ik_max_word",search_analyzer:"ik_smart"}



# 适用场景

* 维基百科,类似百度百科,牙膏,牙膏的维基百科,全文检索,高亮,搜索推荐
* 新闻网站,类似搜狐新闻,用户行为日志(点击,浏览,收藏,评论)+社交网络数据(对某某新闻的相关看法),数据分析,给到每篇新闻文章的作者,让他知道他的文章的公众反馈(好,坏,热门,垃圾等)
* StackOverflow论坛,IT问题,程序的报错,提交上去,有人会跟你讨论和回答,全文检索,搜索相关问题和答案,程序报错了,就会将报错信息粘贴到里面去,搜索有没有对应的答案
* GitHub,开源代码管理,搜索上千亿行代码
* 站内搜索(电商,招聘,门户,等等),IT系统搜索(OA,CRM,ERP,等等),数据分析(ES热门的一个使用场景)



# 核心



## 近实时

* 从写入数据到数据可以被搜索到有一个小延迟(大概1秒);基于es执行搜索和分析可以达到秒级



## 分片

* 一个分片是一个底层的工作单元,保存了全部数据中的一部分,可以认为是一个数据区
* 所有文档被存储和索引到分片内,但应用程序是直接与索引而不是分片进行交互
* 一个分片可以是主分片或副本分片
* 索引内所有文档都是归属于一个分片,所以主分片的数量决定索引能够保证的最大数据量
* 在索引建立的时候就已经确定了主分片数,但副本分片数可随时修改



## Cluster

* 集群:包含多个节点,每个节点属于哪个集群是通过配置(集群名称,默认是elasticsearch)来决定的,对于中小型应用来说,刚开始一个集群就一个节点很正常



## Node

* 节点:集群中的一个节点,节点也有一个名称,默认随机分配
* 节点名称很重要,特别是在执行运维管理操作的时候
* 默认节点会去加入一个名称为“elasticsearch”的集群,如果直接启动一堆节点,那么它们会自动组成一个elasticsearch集群,当然一个节点也可以组成一个elasticsearch集群



## Index

* 索引:类似关系型数据库的数据库,包含一堆有相似结构的文档数据,索引有一个名称
* 一个index包含很多document,一个index就代表了一类类似的或者相同的document



## Type

* 类型:类似数据库中的表,**ES6以上就准备废弃,ES8中移除**
* 每个索引里都可以有一个或多个type,type是index中的一个逻辑数据分类
* 一个type下的document,都有相同的field,比如博客系统,有一个索引,可以定义用户数据type,博客数据type,评论数据type
* 商品index,里面存放了所有的商品数据,商品document
* 但是商品分很多种类,每个种类的document的field可能不太一样,比如说电器商品,可能还包含一些诸如售后时间范围这样的特殊field;生鲜商品,还包含一些诸如生鲜保质期之类的特殊field
* type,日化商品type,电器商品type,生鲜商品type
* 日化商品type:product_id,product_name,product_desc,category_id,category_name
* 电器商品type:product_id,product_name,product_desc,category_id,category_name,service_period
* 生鲜商品type:product_id,product_name,product_desc,category_id,category_name,eat_period
* 每一个type里面,都会包含一堆document

```json
{"product_id": "1","product_name": "长虹电视机","product_desc": "4k高清",   "category_id": "3","category_name": "电器","service_period": "1年"}
{"product_id": "2","product_name": "基围虾","product_desc": "纯天然,冰岛产",   "category_id": "4","category_name": "生鲜","eat_period": "7天"}
```

 

## Document

* 文档,类似关系型数据库中的行
* 文档是es中的最小数据单元,一个document可以是一条客户数据,一条商品分类数据
* 通常用JSON数据结构表示,每个index下的type中,都可以去存储多个document



## Field

* 字段,类似关系型数据库中的字段
* Field是Elasticsearch的最小单位
* 一个document里面有多个field,每个field就是一个数据字段

```json
product document:{"product_id":"1","product_name":"高露洁牙膏","product_desc":"高效美白","category_id":"2","category_name":"日化用品"}
```

* type:字段类型
* analyzer:索引分词器,越细粒度越好
* search_analyzer:搜索分词器,稍微粗粒度
* index:是否索引,默认true,类似链接之类的字段则可以不索引
* store:是否在source之外存储,每个文档索引后会在ES中保存一份原始文档,存放在_source中,一般不需要设置为true,因为在 _source中已经有一份原始文档了
* format:格式化字符串.通常字段为date类型时才会设置,同java,多种格式中间用双竖线(||)
* scaling_factor:比例因子,当type为数字类型时,若是有精度的,如float类型,可设置该值为100,存储时会将需要存储的值乘以该比例因子100存入到ES中,如23.45会在ES中存为2345,若是23.456,则会四舍五入为2346



## Nested

* 嵌入式类型字段.当某个字段的值是数组或list时,会被扁平化,如

  ```json
  {"user":[{"first":["aaa","bbb"]},{"second":["ccc,","ddd"]}]}
  // 在es中存储时会扁平化,不是以层级模式存储
  user.first=["aaa","bbb"]
  user.second=["ccc,","ddd"]
  ```

* 该存储方式会使查找时结果不准,此时就必须规定user的类型为nested



# 字段映射类型

* string:text和keyword,keyword在进行搜索时不会对值存储在ES中的值进行分词,是通过整体搜索
* number:long,integer,short,byte,double,float,half_float,scaled_float
* date:date
* boolean:boolean
* binary:binary
* range:integer_range,float_range,long_range,double_range,date_range



## Mapping

* 映射,类似关系型数据库中的约束.第一次存数据时,若不指定类型,由ES自行判断类型

* 创建映射

  ```json
  // 创建映射,esip:port/indexname,_mapping是固定写法,put
  {
      "mappings":{
          "properties":{
              "age":{"type":"integer"},
              "email":{"type":"keyword"},
              "username":{"type":"text"}
          }
      }
  }
  ```

* 添加映射

  ```json
  // 添加映射,esip:port/indexname/_mapping,_mapping是固定写法,put
  {
      "mappings":{
          "properties":{
              "salary":{"type":"double"}
          }
      }
  }
  ```

* 不能更新映射,因为修改映射会对数据的检索方式造成很大的变动,只能数据迁移.需要先重新新增正确的映射方式,之后使用_reindex对数据重新建立索引

  ```json
  // 重新建立索引,在kibana中直接使用_reindex接口即可
  {
      "source":{
          "index":"oldindexname"
      },
      "dest":{
          "index":"newindexname"
      }
  }
  ```

* 数据如何存放到索引对象上,需要有一个映射配置,包括:数据类型,是否存储,是否分词等

* 这样就创建了一个名为blog的Index.Type不用单独创建,在创建Mapping 时指定就可以

* Mapping用来定义Document中每个字段的类型,即所使用的analyzer,是否索引等属性,非常关键

```json

// 复杂Mapping
client.indices.putMapping({
    index : 'blog',
    type : 'article',
    body : {
        article:{
            properties:{
                id:{
                    type:'string',
                    analyzer:'ik',
                    store:'yes',
                },
                title:{
                    type:'string',
                    analyzer:'ik',
                    store:'no',
                },
                content:{
                    type:'string',
                    analyzer:'ik',
                    store:'yes',
                }
            }
        }
    }
});
```



## 对比数据库

| 关系型数据库(比如Mysql) | 非关系型数据库(Elasticsearch) |
| ----------------------- | ----------------------------- |
| 数据库Database          | 索引Index                     |
| 表Table                 | 类型Type,ES6以上废弃,ES8移除  |
| 数据行Row               | 文档Document                  |
| 数据列Column            | 字段Field                     |
| 约束 Schema             | 映射Mapping                   |



## 存储和搜索数据

* **见ElasticSearch.pptx-03**
* 索引对象(blog):存储数据的表结构 ,任何搜索数据,存放在索引对象上
* 映射(mapping):数据如何存放到索引对象上,需要有一个映射配置, 包括:数据类型,是否存储,是否分词等
* 文档(document):一条数据记录,存在索引对象上



# 安装



* 下载对应JDK版本的Elasticsearch安装包,或直接用yum或web-get下载安装包,解压到/app/es下

* 在es目录下创建data和logs目录

* 配置文件elasticsearch.yml在es/conf下

  ```yaml
  # 数据目录
  path.data:  /app/es/data
  # 日志目录
  path.logs:  /app/es/logs
  ```

* 配置linux进程访问数量,vi /etc/security/limits.conf,添加如下内容:

  ```shell
  * soft nofile 65536
  * hard nofile 131072
  * soft nproc 2048
  * hard nproc 4096
  ```

* 其他相关系统配置

  * vi /etc/security/limits.d/90-nproc.conf,修改如下内容:

  ```shell
  # * soft nproc 1024 修改为
  * soft nproc 2048
  ```

  * vi /etc/sysctl.conf ,添加下面配置:

  ```shell
  vm.max_map_count=655360
  sysctl -p
  ```

* 启动.es/bin/,根据系统的不同,进入不同的文件夹,进入后./sonar.sh start

* 启动测试:curl http://localhost:9200或在网页直接打开改地址

* 可使用elasticsearch-head对es进行可视化查看,主要需要开启es的跨域

* IK分词器,ES默认的分词器对中文支持不太好,使用IK分词器可以更好的查询中文,他分为2种模式:
  * ik_max_word:会对中文做最细粒度的拆分
  * ik_smart:最粗粒度的拆分
  
* 当直接在ElasticSearch建立文档对象时,如果索引不存在的,默认会自动创建,映射采用默认方式
* ElasticSearch服务默认端口9300,Web管理平台端口9200



## 常见问题

* 内存不足

  ```shell
  # 报错:
  # max virtual memory areas vm.max_map_count [65530] is too low, increase to at least [262144]
  # 在/etc/sysctl.conf中添加如下
  vm.max_map_count=655360
  sysctl -p
  # 若是docker,可添加命令参数: -e ES_JAVA_OPTS="-Xms1g -Xmx1g"
  ```

* 内存锁定

  ```shell
  # unable to install syscall filter: 
  # java.lang.UnsupportedOperationException: seccomp unavailable: CONFIG_SECCOMP not compiled into kernel, CONFIG_SECCOMP and CONFIG_SECCOMP_FILTER are needed
  # 在配置文件中添加如下配置
  bootstrap.memory_lock: false
  bootstrap.system_call_filter: false
  ```

* bootstrap checks failed

  ```shell
  # max file descriptors [4096] for elasticsearch process likely too low, increase to at least [65536]
  # max number of threads [1024] for user [lishang] likely too low, increase to at least [2048]
  # 修改/etc/security/limits.conf,添加如下
  * soft nofile 65536
  * hard nofile 131072
  * soft nproc 2048
  * hard nproc 4096
  ```




# 配置



## elasticserch.yml



* 配置文件为es/conf/elasticsearch.yml
* cluster.name:集群名称,如果要配置集群,需要两个以上的elasticsearch节点配置的cluster.name相同,都启动可以自动组成集群,cluster.name默认是elasticsearch
* node.name:当前es节点的名称,集群内部可重复
* network.host:绑定地址,若是0.0.0.0,任何ip都可以访问es
* http.port:http访问端口
* http.cors.enabled:true,是否允许跨域访问
* http.cors.allow-origin:/.*/,允许跨域访问的请求头
* transport.tcp.port:es内部交互接口,用于集群内部通讯,选举等
* node.master:true/false,集群中该节点是否能被选举为master节点,默认为true
* node.data:true/false,指定节点是否存储索引数据,默认为true
* discovery.zen.ping.unicast.hosts:["ip1:port1","ip2:port2"],设置集群中master节点的初始列表
* discovery.zen.ping.timeout:3s,es自动发现节点连接超时时间,默认为3s
* discovery.zen.minimum_master_nodes:2,最小主节点个数,此值的公式为:(master_eligible_nodes/2)+1
* discovery.seed_hosts:设置集群中的master节点的出事列表ip端口地址,逗号分隔
* cluster.initial_master_nodes:新集群初始时的候选主节点
* node.max_local_storage_nodes:2,单机允许的最大存储节点数
* node.ingest:true/false,是否允许成为协调节点
* bootstrap.memeory_lock:true/false,设置true可以锁住es使用的内存,避免内存与swap分区交换数据
* path.data:数据存储目录.默认是es根目录下的data文件夹,可以设置多个存储路径,用逗号隔开
* path.logs:日志存储目录.默认是es根目录下的logs文件夹
* path.conf:设置配置文件的存储路径,tar或zip默认在es根目录下的config,rpm默认在/etc/elasticsearch
* path.plugins:设置插件的存放路径,默认是es根目录下的plugins文件夹
* xpack.ml.enabled:boolean,是否启用机器学习,在windows上不支持,要设置为false



## jvm.options



* 主要设置xms和xmx
* 两个值设置为相等,最大值不超过物理内存的一半



# 插件



## Elasticsearch head



* ES集群可视化操作,可在Web上管理ES集群
* [下载插件](https://github.com/mobz/elasticsearch-head),elasticsearch-head-master.zip
* nodejs[官网](https://nodejs.org/dist/)下载安装包
* 将elasticsearch-head-master.zip和node-linux-x64.tar.xz都导入到linux的/app/software目录
* 安装nodejs,配置环境变量,查看node和npm版本

```shell
vi /etc/profile
export NODE_HOME=/app/nodejs
export PATH=$PATH:$NODE_HOME/bin
source /etc/profile
node -v
npm -v
```

* 解压head插件到/app/eshead目录下
* 查看当前head插件目录下有无node_modules/grunt目录,没有:执行命令创建

```shell
# 替换nodejs源为淘宝镜像源,加速依赖下载
npm install -g cnpm --registry=https://registry.npm.taobao.org
# 安装依赖
npm install grunt --save
npm install -g grunt-cli
```

* 编辑Gruntfile.js:vim Gruntfile.js

```javascript
# 文件93行添加hostname:'0.0.0.0'
options: {
    hostname:'0.0.0.0',
    port: 9100,
    base: '.',
    keepalive: true
}
```

* 检查head根目录下是否存在base文件夹,没有:将_site下的base文件夹及其内容复制到head根目录下
* 启动grunt server:grunt server -d->Started connect web server on http://localhost:9100
* 如果提示grunt的模块没有安装:

```shell
Local Npm module “grunt-contrib-clean” not found. Is it installed? 
Local Npm module “grunt-contrib-concat” not found. Is it installed? 
Local Npm module “grunt-contrib-watch” not found. Is it installed? 
Local Npm module “grunt-contrib-connect” not found. Is it installed? 
Local Npm module “grunt-contrib-copy” not found. Is it installed? 
Local Npm module “grunt-contrib-jasmine” not found. Is it installed? 
Warning: Task “connect:server” not found. Use –force to continue. 
# 执行以下命令,最后一个模块可能安装不成功,但是不影响使用
npm install grunt-contrib-clean grunt-contrib-concat grunt-contrib-watch grunt-contrib-connect grunt-contrib-copy grunt-contrib-jasmine 
```

* 浏览器访问head插件:http://hadoop102:9100
* 启动集群插件后发现集群未连接,提示跨域访问,可在es配置文件添加如下配置

```yaml
http.cors.enabled: true
http.cors.allow-origin: "*"
```

* 再重新启动elasticsearch
* 关闭插件服务:ctrl+c



## IK分词器



* 默认是使用官方的标准分词器,但是对中文支持不友好,需要使用IK分词器

* IK分词器:对中文友好的分词器,不会像标准分词器一样将每个词都拆开,[官网](https://github.com/medcl/elasticsearch-analysis-ik/releases)

* 下载压缩包解压到ES的plugins目录里中即可完成安装,重启ES和Kibana

* 分析使用,在Kibana中的请求地址为:_analyze,post请求

  ```json
  {
  	"analyzer":"ik_max_word",
      "text":"我是中国人"
  }
  ```

  * ik_max_word:最大粒度的对中文词汇进行拆分
  * ik_smart:最粗粒度的对中文进行拆分,智能拆分

* 自定义分词:需要修改IK分词器目录下的config/IKAnalyzer.cfg.xml

  * ext_dict:本地分词文件地址,在值的位置配置自定义分词的文件名称.文件的每行只能定义一个词,最好是将文件的后缀定义为dic,同时该文件的编码模式选择UTF8,配置好后重启ES即可
  * remote_ext_dict:远程分词器地址,是一个远程的http/https地址,可以配置到nginx或其他服务器中,文件形式和内容同ext_dict即可,配置后重启ES即可



## Logstash



* 将mysql中的数据同步到ES中



# API调用



[官方文档](https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-getting-started.html)



## 内置API



* esip:port/_cat/nodes:get,查看所有节点

* esip:port/_cat/health:get,查看es健康状况

* esip:port/_cat/master:get,查看主节点

* esip:port/_cat/indices:get,查看所有索引

* esip:port/\_index/_type/[\_id]:post,在es中添加数据,数据为json格式,请求头为application/json

  * _index:相当于数据库中的数据库,索引名
  * _type:相当于数据库中的表,类型名
  * _id:相当于数据库中的唯一标识.若不带id会自动生成一个随机uuid作为id;若带id,则es中没有该id数据时,created,若存在,则是updated

* `esip:port/_index/_type/_id/_create`:put,强制创建,若存在相同_id的值,不会更新,会报错

* `esip:port/_index/_type/[_id]`:get,在es中根据id进行搜索,得到json数据,下划线开头的统称为元数据,\_source中数据为查看到的真实数据,若没有,则为null

  ```json
  {
  	"_index":"_index",	// 在那个索引
  	"_type":"_type",	// 在哪个类型
  	"_id":"_id",		// 记录id
  	"_version":2,		// 版本号,每修改一次就加1
  	"_seq_no":1,		// 并发控制字段,每次更新就会+1,用来做乐观锁,可以写在url后
  	"_primary_term":1,	// 同上,主分片重新分配,如重启就会变化
  	"found":true,
  	"_source":{			// 真实数据
  		"shuju":"shuju"
  	}
  }
  ```

* `esip:port/_index/_type/_id?_source_includes=column1...`:只查询指定column1的值,_source_includes为固定写法

* `esip:port/{index}/_update/{id}`:post,专用更新数据.该方式和不带`_update`更新方式不同,只能用来更新,不能用来新增.且两种方式的参数格式也不一样,对数据的影响也不一样.将`_update`放在末尾的方式已经废除

  ```json
  // 带_udpate的更新数据传递格式,doc为固定写法
  {
  	"doc":{
  		// 更新内容
  	}
  }
  // 不带_update的更新方式
  {
      // 更新内容
  }
  ```

  * 带_update时若是第一次更新,\_version,\_seq_no等会变化,但是多次更新内容连续且相同时,这些元数据不会再变化.不带\_update的这些元数据都会变化

* esip:port/\_index/\_type/\_id:delete,删除某个资源

* esip:port/\_index/\_type/\_bulk:post,批量新增数据,固定格式,只能在kibana中测试

  ```json
  {"index":"_id":"1"} // index是固定写法,_id是标识,注意不是一个严格的json,没有逗号
  {
  	// 需要更新的内容key-value
  }					// 注意,没有逗号
  {"index":"_id":"2"}
  {
  	// 需要更新的内容key-value
  }
  ```

* esip:port/\_bulk:post,复杂批量操作

  ```json
  {"delete":{"_index":"index1","_type":"type1","_id":1}}
  {"create":{"_index":"index1","_type":"type1","_id":1}}
  // 需要创建内容的json,需要紧挨着create
  {"name":"testee"}
  {"update":{"_index":"index1","_type":"type1","_id":1}}
  // 需要更新的内容,固定写法
  {"doc":{"name":"test2"}}
  ```

* queryDsl:通过json格式的请求从es中查询数据,可以定制各种查询类型,排序,过滤等

  ```json
  {
      // 查询类型
      "query":{
          // 具体查询类型,全文检索,会根据空格等进行分词
          "match_all":{},
          // 匹配具体的字段,该方式默认是全文检索
          "match":{
              // 具体匹配的字段名,需要匹配的值,包含value1或value2的内容都会被搜索到
              "fildname1":"value1 value2",
              "fildname2":"value3",
              // 同match_phrase:不同的是必须精确匹配,不可以是包含
              "fildname3.keyword":"value4 value5"
          },
          // 同match,但不会进行分词,用于精确值的查找
          // 官方不建议该方式用于文本值的检索,而是数值的检索
          "term":{
            "fildname1" :"value1"
          },
          // 短语匹配,不会对空格等进行分词,不会对需要搜索的关键字进行分词
          // 可以匹配包含搜索关键字的字段
          "match_phrase":{
              "fieldname1":"value1 value2"
          },
          // 多字段匹配.只要有一个字段中包含搜索关键字即可,同样会进行分词
          "multi_match":{
              "query":"value1 value2",
              "fields":["fildname1","fildname2"]
          },
          // 符合查询,需要满足多个条件,bool和must,must_not,should是固定写法
          "bool":{
              // 必须符合的条件
              "must":[
                  {
                      "match":{
                          "fildname1":"value1 value2"
                      }
                  },{
                      "match":{
                          "fildname2":"value1 value2"
                      }
                  }
              ],
              // 必须不符合的条件
              "must_not":[
                  {
                      "match":{
                          "fildname1":"value1 value2"
                      }
                  },{
                      "match":{
                          "fildname2":"value1 value2"
                      }
                  }
              ],
              // 可满足,可不满足的条件,只会影响评分和排序
              "should":[
                  {
                      "match":{
                          "fildname1":"value1 value2"
                      }
                  },{
                      "match":{
                          "fildname2":"value1 value2"
                      }
                  }
              ],
              // filter的作用和must_not的作用相同,但是不影响评分
              "filter":[
                  {
                      "match":{
                          "fildname1":"value1 value2"
                      }
                  },{
                      "match":{
                          "fildname2":"value1 value2"
                      }
                  }
              ]
          }
      },
      // 排序,可对多个字段进行排序
      "sort":[{
          // 需要进行排序的字段
      	"balance":{
              // 排序类型
              "order":"desc"
          }
  	}],
      // 分页查询,从第几条数据开始
      "from":0,
      // 分页查询,每页显示条数
      "size":10,
      // 指定查询返回的字段,默认是返回所有
      "_source":["userId","username"]
  }
  ```



## Aggregations



* 聚合,主要对查询提供分组和提取数据的能力,类似于数据库中的max,avg,group by等函数

```json
{
    "query":{
        "match":{
            "fildname1":"value1 value2"
        }
    },
    // 固定写法,表示聚合查询
    "aggs":{
        // 聚合名称,自定义,会返回到结果集中
        "aggTerms":{
            // 聚合类型,可从官网中查看,terms表示统计分组
            "terms":{
                // 从查询结果的字段中进行分析
                "field":"fildname1",
                // 查询条件
                "size":10
            },
            // 在聚合中对上次的结果集再次聚合
            "aggs":{
                "aggAvgTerms":{
                    // 对统计分析的结果再次求平均值
                    "avg":{
                        "field":"fildname1"
                    }
                }
            }
        },
        "aggAvg":{
            // 平均值聚合
            "avg":{
                // 从查询结果的字段中进行分析
                "field":"fildname1",
                // 查询条件
                "size":10
            }
        }
    }
}
```



## Springboot



* Springboot2直接利用JPA整合了Elasticsearch进行操作,repository直接继承ElasticsearchRepository即可
* Java High Level Rest Client调用,官方推荐使用
* DSL:以json格式定义关键字来搜索关键字,调用API查询
  * SearchRequest:主要的查询类,所有个有关查询的设置都需要在这个类里
  * SearchSourceBuilder:源字段过滤,设置那些字段显示,那些字段不显示
  * 分页采用from,size,和mysql类型,from是从第几条开始,不是从第几页开始
  * TermQuery,id:精准查询,TermQuery不会对需要搜索的关键字进行拆分,而是直接利用关键字搜索
  * MatchQuery:全文检索,将搜索的关键字拆分之后进行检索,搜索多个关键字还需要进行占比的计算
  * MultiQuery:同时可对多个关键字进行搜索,而Term和Match只能对一个关键字进行搜索
  * Boolean:多条件查询,可以将Term,Match,Multi3个结合起来查询,必须结合must,should,must_not关键字进行关联,类似and,or的意思
  * HighlightBuilder:高亮,必须根据关键字来搜索,设置在SearchSourceBuilder中
  * 排序:sort,可以在一个字段上添加一个或多个排序,支持在keyword,date,number等类型,但不支持text
  * SearchResponse:经过ES的搜素之后的结果集,包含源字段以及其他信息
  * SearchHists:搜索的匹配结果集,其他相关信息
  * SearchHit:只包含在SearchSourceBuilder中设置的源字段信息
* 搜索单个索引文档,代码见**paradise-study-java/paradise-study-search/com.wy.service**
* 搜索多个索引文档,代码见**paradise-study-java/paradise-study-search/com.wy.service**
* 更新文档update,代码见**paradise-study-java/paradise-study-search/com.wy.service**
* 更新文档upsert,代码见**paradise-study-java/paradise-study-search/com.wy.service**
* 删除文档delete,代码见**paradise-study-java/paradise-study-search/com.wy.service**



## Java原生API



* 所有代码见**paradise-study-java/paradise-study-search/com.wy.service.ElasticSearchService**

* 查询所有(matchAllQuery)

![img](ESMatchallQuery.png)

* 对所有字段分词查询(queryStringQuery)

![img](ESQueryStringQuery.png)

* 通配符查询(wildcardQuery)
  * *:表示多个任意的字符
  * ?:表示单个字符

![img](ESWildcardQuery.png)

* 词条查询(TermQuery)

![img](ESTermQuery.png)

* 模糊查询(fuzzy)




# 过滤器

* 过滤器是针对搜索结果进行的,主要是判断文档是否匹配,并不计算和判断文档的匹配得分,所有比查询的性能要高,且方便缓存,尽量使用过滤器去实现查询或者过滤器和查询共同使用
* 针对范围进行过滤(range),一次只能对一个Field过滤
* 针对项进行精准过滤,一次只能对一个Field过滤



# 优化



## FileSystem Cache



* 就是os cache,操作系统的缓存
* 往es里写的数据,最终都写到磁盘文件里去了,读的时候,ES将磁盘文件里的数据缓存到os cache里面去
* 给os cache更多的内存,就可以让内存容纳更多的index segment file索引数据文件,搜索时基本走内存,性能会非常高
* 从内存查询和从磁盘上查询的性能差距可以有很大,如果走磁盘,搜索性能绝对是秒级别的,1秒,5秒,10秒.但是如果是走os cache,是走纯内存的,一般性能比走磁盘要高一个数量级,基本上就是毫秒级的
* 最佳的情况下,集群机器的分配给es的内存至少可以用容纳总数据量的一半



## 减少搜索字段



* 比如一行数据有30个字段,但是搜索的时候只需要根据id name age三个字段来搜索
* 如果往es里写入一行数据所有的字段,就会导致70%的数据是不用来搜索的,导致os cahce能缓存的数据就越少
* 仅仅只是写入es中要用来检索的字段就可以了,其他的字段数据存在mysql中,或者用es+hbase的架构
* hbase适用于海量数据的在线存储,在hbase写入海量数据,不要做复杂的搜索,就是做很简单的一些根据id或者范围进行查询就可以
* 从es中根据name或age去搜索,拿到结果的doc id,然后根据doc id到hbase里去查询doc id对应的完整数据



## 数据预热



* 将一些搜索比较频繁的数据通过手动或定时任务刷到内存中
* 比如微博,电商等,可以将平时查看最多的大V,商品,热数据等,提前搞个后台程序,每隔1分钟,自己去后台系统搜索一下,刷到os cache里去
* 对于那些比较热的,经常会有人访问的数据,最好做一个专门的缓存预热子系统,每隔一段时间就提前访问一下,让数据进入os cache里面去



## 冷热分离



* es可以做类似于mysql的水平拆分,将访问少,频率低的数据单独写一个索引,访问频繁的数据单独写一个索引,这样可以确保热数据在被预热之后,尽量都让他们留在os cache里,别让冷数据给冲刷掉
* 假设你有6台机器,2个索引,一个放冷数据,一个放热数据,每个索引3个shard,3台机器放热数据index,另外3台机器放冷数据index
* 大量访问热数据index时,热数据可能就占总数据量的10%,此时数据量很少,几乎全都保留在os  cache里面了,就可以确保热数据的访问性能是很高的
* 对于冷数据而言,是在别的index里的,跟热数据index都不在相同的机器上,互相之间都没什么联系.如果有人访问冷数据,可能大量数据是在磁盘上的,此时性能差点



## document模型设计

 

* es里面的复杂的关联查询,复杂的查询语法,尽量别用,一旦用了性能一般都不太好
* 若必须联表查询的数据,可以创建2个索引,大的索引包含小的索引,如
  * order索引,orderItem索引
  * order索引,包含id,order_code,total_price
  * orderItem索引,写入es的时候,就完成join操作.id,order_code,total_price,id,order_id,goods_id,price
* 将关联好的数据直接写入es中,搜索的时候,就不需要利用es的搜索语法去完成join来搜索了
* document模型设计是非常重要的,很多操作,不要在搜索的时候才想去执行各种复杂的操作
* es能支持的操作就是那么多,不要考虑用es做一些它不好操作的事情
* 如果真的有那种操作,尽量在document模型设计的时候,写入的时候就完成
* 另外对于一些太复杂的操作,比如join,nested,parent-child搜索都要尽量避免,性能都很差



## 分页性能优化



* es的分页是较坑的,假如你每页是10条数据,若要查询第100页,实际上是会把每个shard上存储的前1000条数据都查到一个协调节点上,如果你有个5个shard,那么就有5000条数据,接着协调节点对这5000条数据进行一些合并,处理,再获取到最终第100页的10条数据
* 分布式的程序要查第100页的10条数据,不可能说从5个shard,每个shard就查2条数据,最后到协调节点合并成10条数据.必须得从每个shard都查1000条数据过来,然后根据需求进行排序,筛选等操作,最后再次分页,拿到里面第100页的数据
* 翻页的时候,翻的越深,每个shard返回的数据就越多,协调节点处理的时间越长,所以用es做分页的时候,越翻到后面,就越是慢
* 不允许深度分页,默认深度分页性能很惨,超过10页可能数据查询的时间就超过秒级了
* 类似于app里的推荐商品不断下拉出来一页一页的,可以用scroll api
* scroll会一次性生成所有数据的一个快照,然后每次翻页就是通过游标移动获取下一页,性能会比es高很多
* 缺点就是只适合于那种类似微博下拉翻页的,不能随意跳到任何一页的场景
* scroll的数据快照只保存一段时间,如果用户持续翻几个小时也不行
* 无论翻多少页,性能基本上都是毫秒级的



# 集群

* ES自带集群,一个运行中的ES实例称为一个节点,而集群是由一个或多个拥有相同cluster.name配置的节点组成,当有新节点加入集群时,集群将会自动重新平均分布所有的数据
* 当一个节点被选举为主节点时,它将负责管理集群范围内的所有变更.
* 主节点并不需要涉及到文档级别的变更和搜索等操作,所有当集群只有一个主节点时,即使流量的增加它也不会成为瓶颈
* 任何节点都可以成为主节点
* 客户端发送请求到集群中的任何节点都能感知文档所处的位置,并且正确处理请求并返回数据
* 集群健康状态:get,地址/_cluster/\_health,返回结果的status字段中展示为green,yellow,red
  * status:表示当前集群在总体上是否正常工作
  * green:所有的主分片和副本分片都正常运行
  * yellow:所有的主分片都正常运行,但不是所有的副本分片都正常运行
  * red:有主分片没正常运行



## 主节点异常

* 立刻会选举一个新的节点作为主节点
* 由于在其他节点上存在完整的主分片,所以数据不会丢失



## 候选节点

* 当主节点异常时,那些节点可以升级为主节点,只需要配置node.master=true即可



## 数据节点

* 数据节点通常只负责数据的增删改查等,只需要配置node.data=true即可
* 一个节点最好不要同时是候选节点和数据节点,否则大并发情况下对服务器造成的压力较大



## 客户端节点

* node.master和node.data都是false,这样的节点只负责请求的分发,汇总等
* 该类型的节点主要是为了负载均衡



## 脑裂

* 网络不通或其他情况下导致集群中部分节点和另外部分节点无法通信,出现多个主节点
* 当主节点即是master又为data时,若访问量较大,可能造成短暂的无响应而造成延迟
* data节点上的ES进程占用的内存较大,引发JVM大规模内存回收造成ES时区响应
* 减少脑裂应该进行角色分离,主节点和数据节点分开
* 减少误判,延长主节点的心跳超时响应时间:discovery.zen.ping_timeout
* 选举触发:discovery.zen.minimum_master_nodes,该属性定义在集群中有候选节点且相互连接的节点的最小数量为多少时,才能形成新的集群



# ELK



* ELK主要是用来做日志分析,由es,logstash,kibana组成
* Logstash:日志收集工具,可以从本地磁盘,网络服务,消息队列中收集各种日志,然后进行过滤分析,并将日志输出到Elasticsearch中,类似于大数据中的Flume
* Kibana:可视化日志Web展示工具,对ES中存储的日志进行展示,还可以生成相应的图标
* 使用redis或队列作为数据来源的理由
  * 防止Logstash和ES无法正常通信,从而丢失日志
  * 防止日志量过大导致ES无法承受大量写操作从而丢失日志
  * 防止logstash 直接与es操作,产生大量的链接,导致es瓶颈
  * 如果redis使用的消息队列出现扩展瓶颈,可以使用更加强大的kafka,flume来代替
* x-pack:权限管理和邮件服务



# Docker中使用

* docker中安装ES7,启动命令如下

```shell
docker run -p 9200:9200 -p 9300:0300 --name elasticsearch -e "discovery.type=single-node" -e ES_JAVA_OPS="-Xms64m -Xmx128m" -v /app/elasticsearch/config/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml -v /app/elasticsearch/data:/usr/share/elasticsearch/data -v /app/elasticsearch/plugins:/usr/share/elasticsearch/plugins -d elasticsearch:7.4.2
```

* docker中启动ES插件Kibana:ES的可视化插件

```shell
docker run --name kibana -e ELASTICSEARCH_HOSTS=ip:port -p 5601:5601 -d kibana:7.4.2
```

