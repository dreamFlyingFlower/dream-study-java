# Solr



# 概述

	Solr是基于apache的Lucene开发的搜索引擎,主要包括全文检索,命中标示,分娩搜索,动态聚类,数据库集成以及富文本的处理,是高度可扩展的,并提供了分布式搜索和索引复制.

* 支持添加多种格式的索引,如html,pdf,office软件,json,xml,csv等纯文本格式
* 不考虑建索引的时候的同时进行搜索,速度更快.建立索引时,搜索效率不高
* 对单纯已有的数据进行搜索,solr更快,建立索引时Elasticsearch更快
* 当数据逐渐增加,solr的效率会逐渐变低,而Elasticsearch却没有明显变化
* Solr利用zk进行分布式管理,在传统的搜索中表现好于Elasticsearch



# 单机部署

* 下载安装包

```shell
wget https://mirrors.tuna.tsinghua.edu.cn/apache/lucene/solr/7.7.1/solr-7.7.1.tgz
tar -zxvf solr-7.7.1.tgz -C /app/solr7
```

* 配置Java环境变量



## 第一种

* 下载到压缩包后直接解压,进入bin目录,执行./solr start -force直接运行

* 部署完成后可在浏览器访问ip:8983/solr,8983是默认端口



## 第二种

* 部署到tomcat中

* 新建目录/app/solr,将tomcat8复制到该文件夹下

* 复制并重命名solr目录里的server/solr-webapp/webapp文件夹到/app/solr/tomcat8/webapps/solr

* 将server/lib/ext/下的所有jar复制到/app/solr/tomcat8/webapps/solr/WEB-INF/lib/下

* 将server/lib下以metrics开头的5个包复制到/app/solr/tomcat8/webapps/solr/WEB-INF/lib/下

* 在tomcat里的solr/WEB-INF下新建classes文件夹,将server/resources下的jetty-logging.properties复制到tomcat8/webapps/solr/WEB-INF/classes下,若没有jetty-logging文件,可复制其他日志文件

* 将server/solr复制到/app/solr/solrhome,cp -r server/solr /app/solr/solrhome

* 修改tomcat里的solr的web.xml,加上以下语句,并将其中的value改为自己的home地址

  ```xml
  <env-entry>
  	<env-entry-name>solr/home</env-entry-name>
  	<env-entry-value>/app/solr/solrhome</env-entry-value>
  	<env-entry-type>java.lang.String</env-entry-type>
  </env-entry>
  ```

* 将security-constraint标签中的内容都注释,否则会有安全问题,前端不能访问
* 前端ip:8080/solr/index.html访问即可



# 中文分词器IKAnalyzer

* [下载地址](http://search.maven.org/#search%7Cga%7C1%7Ccom.github.magese)

* [源码地址](https://github.com/magese/ik-analyzer-solr7)

* 将下载好的jar包放入到solr/tomcat80/webapps/solr/WEB-INF/lib下

* 打开collection1下的conf/mamaged-schema文件,添加以下代码

  ```xml
  <fieldType name="text_ik" class="solr.TextField">
      <analyzer type="index">
          <tokenizer class="org.wltea.analyzer.lucene.IKTokenizerFactory" useSmart="false" conf="collection1.conf"/>
          <filter class="solr.LowerCaseFilterFactory"/>
      </analyzer>
      <analyzer type="query">
          <tokenizer class="org.wltea.analyzer.lucene.IKTokenizerFactory" useSmart="true" conf="collection1.conf"/>
          <filter class="solr.LowerCaseFilterFactory"/>
      </analyzer>
  </fieldType>
  ```

* 重启tomcat



# 自带的中文分词器

* 复制jar包,cp solr7.7.1/contrib/analysis-extras/lucene-libs/lucene-analyzers-smartcn-7.7.1.jar到tomcat80下的webapps/solr/WEB-INF/lib

* 其他步骤同上,只有添加的代码不同

  ```xml
  <fieldType name="text_hmm_chinese" class="solr.TextField" positionIncrementGap="100">
     <analyzer type="index">
         <tokenizer class="org.apache.lucene.analysis.cn.smart.HMMChineseTokenizerFactory"/>
     </analyzer>
     <analyzer type="query">
         <tokenizer class="org.apache.lucene.analysis.cn.smart.HMMChineseTokenizerFactory"/>
     </analyzer>
  </fieldType>
  ```

* 重启tomcat



# 运行

* 需要先在solrhome中新建文件夹,名字任意,例如collection1
* 复制solrhome/configsets/_default/conf整个文件夹到collection1中
* 在solr的web管理页面,首先需要新建一个core,名字就是2中在solrhome中新建的文件夹名,在后台访问的时候需要用到这个名字,其他可不变
* Solr在存储的时候,数据里必须有id这个字段,也可以是映射到id上,solr会根据id来判断是新增还是更新数据
* 若是在solr中存实体类,则该类中所有字段都必须有Field字段,若没有将不会存到solr中,也不会取出
* 当往solr中存数据时,会自动将没有存储过的字段加入到solr的配置文件中,除了主键id会有指定属性外,其他字段都没有默认属性.默认位置是solrhome/collection1/conf/managed-schema
* 查询所有数据的表达式:\*:\*



# Schema配置文件

* fieldType:存储在solr中的字段类型解释.注意string类型和text_general类型,string类型是不会分词的,只能整个整体进行查询,而text_general才会进行分词
* field:该标签是字段表述,name字段名,multiValued是否唯一,默认true,indexed是否建立索引,默认false,required是否必须,默认false,stored是否显示,即存了数据但是否显示到前端,默认true;type是该字段的类型,若是配置了中文分词器,可以将分词器的name写入其中
* copyfield:当有多个值查询的时候,可以配置该值



# 可视化界面

* CoreAdmin:所有core的管理界面,新建的core会在下面的下拉框中展示,开始的时候core中为空

* Analysis:对某个需要查询的值进行分析,查看分词时会分成那些部分,需要选择字段名或类型

* Dataimport:数据批量导入

* Documents:对数据进行增删改操作.DocumentType:选择数据传递类型,json或xml或其他.在删除全部数据的时候,xml书写方式应该是

  ```xml
  <delete><query>*:*</query></delete><commit/>
  ```

* Files:查看当前core中的配置文件等信息

* Ping:顾名思义

* Query:查询;各个条件的查询含义见docs文件夹的Solr查询含义



# SolrCloud集群

* 先安装zookeeper集群
* 修改端口配置等,复杂,网上搜,此处不搭建,可参考docs文件夹中的solrCloud安装步骤



# 注意

当第一次加入的字符串中都是数字时,会默认将他设置为plongs,即数字类型,类型并非是从实体类的类型中转化而来,而是根据值自动判断,需要到配置文件中进行确认修改



