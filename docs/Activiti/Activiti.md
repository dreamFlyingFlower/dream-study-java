# Activiti



# 概述



* 流程引擎
* [官网](http://activiti.org),7以下版本中包含大部分已经打好的WAR源码

![](000.png)



# 安装



* 从Activiti网站下载activit-6.0.0.zip,解压后直接将wars文件夹中的war复制到tomcat/webapps下

- 执行tomcat的bin目录下的startup.bat或startup.sh启动服务器
- tomcat启动后,打开浏览器访问相应地址:
  - http://localhost:8080/activiti-app,默认admin/test登录
  - http://localhost:8080/activit-admin,默认admin/admin登录
  - http://localhost:8080/activit-rest,默认admin/test登录,这不是一个网页,是API
- activiti-app默认使用H2内存数据库,可修改WEB-INF下相关数据库文件地址 
- activiti-app是流程引擎的用户控制台,使用它来启动新流程,分配任务,查看并认领任务等.这个工具也可以用来管理Activiti引擎
- activiti-app的Kickstart App是制作新流程图的页面,Task App是测试流程的页面,Identity management是用户管理界面
- Activiti App演示实例只是一种简单快速展示Activiti的功能的方式,但是并不是说只能使用这种方式使用Activiti
- Activiti只是一个jar,可以内嵌到任何Java环境中:swing或者Tomcat, JBoss, WebSphere等.也可以把Activiti作为一个典型的单独运行的BPM服务器运行,只要java可以做的,Activiti也可以



# 配置



## ProcessEngine

* Activiti流程引擎的配置文件是名为`Activiti.cfg.xml`的XML文件,和[Spring方式创建流程引擎](http://www.mossle.com/docs/Activiti/#springintegration) **不**一样
* 获得`ProcessEngine`最简单的办法是使用`org.Activiti.engine.ProcessEngines`类

```java
ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
```

* 它会在classpath下搜索`Activiti.cfg.xml`,并基于这个文件中的配置构建引擎

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans   http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="processEngineConfiguration" class="org.Activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration">
        <property name="jdbcUrl" value="jdbc:h2:mem:Activiti;DB_CLOSE_DELAY=1000" />
        <property name="jdbcDriver" value="org.h2.Driver" />
        <property name="jdbcUsername" value="sa" />
        <property name="jdbcPassword" value="" />
        <property name="databaseSchemaUpdate" value="true" />
        <property name="jobExecutorActivate" value="false" />
        <property name="mailServerHost" value="mail.my-corp.com" />
        <property name="mailServerPort" value="5025" />
    </bean>
</beans>
```

* 配置文件中的ProcessEngineConfiguration可以通过编程方式创建,可以配置不同的bean id

```java
ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault();
ProcessEngineConfiguration.createProcessEngineConfigurationFromResource(String resource);
ProcessEngineConfiguration.createProcessEngineConfigurationFromResource(String resource, String beanName);
ProcessEngineConfiguration.createProcessEngineConfigurationFromInputStream(InputStream inputStream);
ProcessEngineConfiguration.createProcessEngineConfigurationFromInputStream(InputStream inputStream, String beanName);
```

* 也可以不使用配置文件,基于默认创建配置

```java
ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration();
```

* 所有这些`ProcessEngineConfiguration.createXXX()`都返回 `ProcessEngineConfiguration`,后续可以调整成所需的对象. 在调用`buildProcessEngine()`后,就会创建`ProcessEngine`

```java
ProcessEngine processEngine = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration()
    .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_FALSE)
    .setJdbcUrl("jdbc:h2:mem:my-own-db;DB_CLOSE_DELAY=1000")
    .setJobExecutorActivate(true).buildProcessEngine();
```



## ProcessEngineConfiguration

* `Activiti.cfg.xml`必须包含一个id为`processEngineConfiguration`的bean

```xml
 <bean id="processEngineConfiguration" class="org.Activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration">
```

* 这个bean会用来构建`ProcessEngine`.有多个类可以用来定义`processEngineConfiguration`.
* 这些类对应不同的环境,并设置了对应的默认值.最好选择适用于程序的类,这样可以少配置几个引擎的参数
  * StandaloneProcessEngineConfiguration:单独运行的流程引擎.Activiti会自己处理事务,默认数据库只在引擎启动时检测.如果没有Activiti的表或者表结构不正确就会抛出异常
  * StandaloneInMemProcessEngineConfiguration:单元测试时的辅助类.Activiti会自己控制事务. 默认使用H2内存数据库.数据库表会在引擎启动时创建,关闭时删除.使用它时,不需要其他配置,除非使用job执行器或邮件功能
  * JtaProcessEngineConfiguration:单独运行流程引擎,并使用JTA事务
  * SpringProcessEngineConfiguration:在Spring环境下使用流程引擎.参考Spring集成



## 数据库配置

* Activiti可能使用两种方式配置数据库
* 第一种方式是定义数据库配置参数
  * jdbcUrl:数据库的JDBC URL
  * jdbcDriver:对应不同数据库类型的驱动
  * jdbcUsername:连接数据库的用户名
  * jdbcPassword:连接数据库的密码
* 基于JDBC参数配置的数据库连接会使用默认的MyBatis连接池
  * jdbcMaxActiveConnections:连接池中处于被使用状态的连接的最大值.默认为10
  * jdbcMaxIdleConnections:连接池中处于空闲状态的连接的最大值
  * jdbcMaxCheckoutTime:连接被取出使用的最长时间,超过时间会被强制回收,默认20000毫秒
  * jdbcMaxWaitTime:这是一个底层配置,让连接池可以在长时间无法获得连接时,打印一条日志,并重新尝试获取一个连接.避免因为错误配置导致沉默的操作失败,默认为20000毫秒
* 第二种方式,使用`javax.sql.DataSource`

```xml
<bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource" >
  <property name="driverClassName" value="com.mysql.jdbc.Driver" />
  <property name="url" value="jdbc:mysql://localhost:3306/Activiti" />
  <property name="username" value="Activiti" />
  <property name="password" value="Activiti" />
  <property name="defaultAutoCommit" value="false" />
</bean>
<bean id="processEngineConfiguration" class="org.Activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration">
    <property name="dataSource" ref="dataSource" />
```

* Activiti的发布包中没有这些类,需要自己把对应的类放到classpath下
* 无论使用JDBC还是DataSource的方式,都可以设置下面的配置
  * databaseType:一般不用设置,因为可以自动通过数据库连接的元数据获取.只有自动检测失败时才需要设置.可能的值有:h2,mysql,oracle,postgres,mssql,db2.如果没使用默认的H2数据库就必须设置这项.这个配置会决定使用哪些创建/删除脚本和查询语句
  * databaseSchemaUpdate:设置流程引擎启动和关闭时如何处理数据库表
    * `false`:默认值,检查数据库表的版本和依赖库的版本,如果版本不匹配就抛出异常
    * `true`:构建流程引擎时,执行检查,如果需要就执行更新.如果表不存在,就创建
    * `create-drop`:构建流程引擎时创建数据库表,关闭流程引擎时删除这些表



## 支持的数据库

| 类型     | 版本                    | JDBC URL实例                                            |
| -------- | ----------------------- | ------------------------------------------------------- |
| h2       | 1.3.168                 | jdbc:h2:tcp://localhost/activiti                        |
| mysql    | 5.1.21                  | jdbc:mysql://localhost:3306/activiti?autoReconnect=true |
| oracle   | 11.2.0.1.0              | jdbc:oracle:thin:@localhost:1521:xe                     |
| postgres | 8.1                     | jdbc:postgresql://localhost:5432/activiti               |
| db2      | DB2 10.1 using  db2jcc4 | jdbc:db2://localhost:50000/activiti                     |
| mssql    | 2008 using sqljdbc4     | jdbc:sqlserver://localhost:1433/activiti                |

 

## 创建数据库表

- 把Activiti-engine的jar放到classpath下
- 添加对应的数据库驱动
- 把Activiti配置文件(Activiti.cfg.xml*)放到classpath下,指向指定数据库
- 执行*DbSchemaCreate*类的main方法
- 只有数据库管理员可以执行DDL语句.SQL DDL语句可以从Activiti下载页或Activiti发布目录里找到,在`database`子目录下.
- 脚本包含在引擎的jar中(*Activiti-engine-x.jar*),在*org/Activiti/db/create*包下



## 理解数据库表的命名

* Activiti的表都以**ACT_**开头,第二部分是表示表的用途的两个字母标识,用途也和服务的API对应

- **ACT_RE_\***:RE表示`repository`. 这个前缀的表包含了流程定义和流程静态资源(图片,规则等)
- **ACT_RU_\***:RU表示`runtime`.这些运行时的表,包含流程实例,任务,变量,异步任务,等运行中的数据.Activiti只在流程实例执行过程中保存这些数据,在流程结束时就会删除这些记录,这样运行时表可以一直很小速度很快
- **ACT_ID_\***:ID表示`identity`. 这些表包含身份信息,比如用户,组等等
- **ACT_HI_\***:HI表示`history`. 这些表包含历史数据,比如历史流程实例, 变量,任务等等
- **ACT_GE_\***:`通用`数据, 用于不同场景下



## 数据库升级

* 在执行更新之前要先备份数据库
* 默认每次构建流程引擎时都会进行版本检测,都在应用启动或Activiti webapp启动时发生.如果Activiti发现数据库表的版本与依赖库的版本不同,就会抛出异常
* 要升级,要把下面的配置 放到Activiti.cfg.xml配置文件里

```xml
<beans ... >
  <bean id="processEngineConfiguration" class="org.Activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration">
    <property name="databaseSchemaUpdate" value="true" />
  </bean>
</beans>
```

* 把对应的数据库驱动放到classpath里,升级应用的Activiti依赖
* 启动一个新版本的Activiti指向包含旧版本的数据库,将`databaseSchemaUpdate`设置为`true`,Activiti会自动将数据库表升级到新版本,当发现依赖和数据库表版本不通过时
* 也可以执行更新升级DDL语句,也可以执行数据库脚本,可以在Activiti下载页找到



## 启用Job执行器

* `JobExecutor`是管理一系列线程的组件,可以触发定时器,包含后续的异步消息.
* 在单元测试中,很难使用多线程.因此API允许查询(`ManagementService.createJobQuery`)和执行job (`ManagementService.executeJob`),所以job可以在单元测试中控制.要避免与job执行器冲突,可以关闭它
* 默认`JobExecutor`在流程引擎启动时就会激活.如果不想在流程引擎启动后自动激活`JobExecutor`,可以设置

```xml
<property name="jobExecutorActivate" value="false" />
```



## 配置邮件服务器

* 可以选择配置邮件服务器.想真正的发送一个email,必须配置一个真实的SMTP邮件服务器



## 配置历史

* 可以选择定制历史存储的配置.通过配置影响引擎的历史功能,参考历史配置

```xml
<property name="history" value="audit" />
```



## 为表达式和脚本暴露配置

* 默认`Activiti.cfg.xml`和你自己的Spring配置文件中所有bean都可以在表达式和脚本中使用
* 如果想限制配置文件中的bean的可见性,可以配置流程引擎配置的beans配置
* `ProcessEngineConfiguration`的beans是一个map.当指定了这个参数,只有包含这个map中的bean可以在表达式和脚本中使用.通过在map中指定的名称来决定暴露的bean



## 配置部署缓存

* 所有流程定义都被缓存,避免每次使用前都要访问数据库,因为流程定义数据是不会改变的
* 默认不会限制流程定义缓存,如果想限制流程定义缓存,可以添加如下配置

```xml
<property name="processDefinitionCacheLimit" value="10" />
```

* 这个配置会把默认的hashmap缓存替换成LRU缓存,来提供限制.这个配置的最佳值跟流程定义的总数有关, 实际使用中会具体使用多少流程定义也有关
* 可以注入自己的缓存实现.必须实现org.Activiti.engine.impl.persistence.deploy.DeploymentCache

```xml
<property name="processDefinitionCache">
  <bean class="org.Activiti.MyCache" />
</property>
```

* 有一个类似的配置叫`knowledgeBaseCacheLimit`和`knowledgeBaseCache`,它们是配置规则缓存的,只有流程中使用规则任务时才会用到



## 日志



* 默认activiti-engine依赖中没有提供SLF4J绑定的jar,需要根据实际需要使用日志框架
* 如果没有添加任何实现jar,SLF4J会使用NOP-logger,不使用任何日志,不会发出警告,而且什么日志都不会记录
* activiti-app和activiti-rest应用都使用了Log4j绑定
* 如果容器classpath中存在commons-logging,为了把spring日志转发给SLF4J,需要使用桥接.如果容器提供了commons-logging实现,请参考http://www.slf4j.org/codes.html#release来确保稳定性



## 映射诊断上下文

* Activiti支持slf4j的MDC功能,如下的基础信息会传递到日志中记录:
  * 流程定义Id标记为mdcProcessDefinitionID
  * 流程实例Id标记为mdcProcessInstanceID
  * 分支Id标记为mdcexecutionId
* 默认不会记录这些信息.可以配置日志使用期望的格式来显示它们,扩展通常的日志信息

```properties
log4j.appender.consoleAppender.layout.ConversionPattern =ProcessDefinitionId=%X{mdcProcessDefinitionID}
executionId=%X{mdcExecutionId} mdcProcessInstanceID=%X{mdcProcessInstanceID} mdcBusinessKey=%X{mdcBusinessKey} %m%n"
```

* 当系统进行高风险任务,日志必须严格检查时,这个功能就非常有用,比如要使用日志分析的情况



# Activiti API



## 流程引擎的API和服务



### ProcessEngine

* ProcessEngine可以获得很多囊括工作流/BPM方法的服务,ProcessEngine和服务类都是线程安全的,可以在整个服务器中仅保持一个引用就可以了

```java
ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
RuntimeService runtimeService = processEngine.getRuntimeService();
RepositoryService repositoryService = processEngine.getRepositoryService();
TaskService taskService = processEngine.getTaskService();
ManagementService managementService = processEngine.getManagementService();
HistoryService historyService = processEngine.getHistoryService();
// 在7以上版本中废弃
IdentityService identityService = processEngine.getIdentityService();
FormService formService = processEngine.getFormService();
```

* `ProcessEngines.getDefaultProcessEngine()`会在第一次调用时初始化并创建一个流程引擎,可以创建和关闭所有流程引擎:`ProcessEngines.init()`和 `ProcessEngines.destroy()`
* ProcessEngines会扫描所有`Activiti.cfg.xml` 和 `Activiti-context.xml` 文件
* `Activiti.cfg.xml`文件流程引擎会使用Activiti的经典方式构建: `ProcessEngineConfiguration.createProcessEngineConfigurationFromInputStream(inputStream).buildProcessEngine()`
* `Activiti-context.xml`文件流程引擎会使用Spring方法构建:先创建一个Spring的环境, 然后通过环境获得流程引擎
* 所有服务都是无状态的.这意味着可以在多节点集群环境下运行Activiti,每个节点都指向同一个数据库, 不用担心哪个机器实际执行前端的调用.无论在哪里执行服务都没有问题



### RepositoryService

* **RepositoryService**是使用Activiti引擎时最先接触的服务.它提供了管理和控制`发布包`和`流程定义`的操作
* 流程定义是BPMN 2.0流程的java实现,它包含了一个流程每个环节的结构和行为
* `发布包`是Activiti引擎的打包单位,一个发布包可以包含多个BPMN 2.0 xml文件和其他资源
* 开发者可以自由选择把任意资源包含到发布包中.既可以把一个单独的BPMN 2.0 xml文件放到发布包里,也可以把整个流程和相关资源都放在一起(比如,hr-processes实例可以包含hr流程相关的任何资源),可以通过`RepositoryService`来`部署`这种发布包.
* 发布一个发布包,意味着把它上传到引擎中,所有流程都会在保存进数据库之前分析解析好.从这点来说,系统知道这个发布包的存在,发布包中包含的流程就已经可以启动了
* 除此之外,服务可以查询引擎中的发布包和流程定义;暂停或激活发布包,对应全部和特定流程定义. 暂停意味着它们不能再执行任何操作了,激活是对应的反向操作

- 获得多种资源,像是包含在发布包里的文件, 或引擎自动生成的流程图
- 获得流程定义的pojo版本, 可以用来通过java解析流程,而不必通过xml



### RuntimeService

* 正如`RepositoryService`负责静态信息(比如,不会改变的数据,至少是不怎么改变的),**RuntimeService**正好是完全相反的,它负责启动一个流程定义的新实例
* 流程定义定义了流程各个节点的结构和行为,流程实例就是这样一个流程定义的实例
* 对每个流程定义来说,同一时间会有很多实例在执行
* `RuntimeService`也可以用来获取和保存`流程变量`,这些数据是特定于某个流程实例的,并会被很多流程中的节点使用(比如一个排他网关常常使用流程变量来决定选择哪条路径继续流程)
* `Runtimeservice`也能查询流程实例和执行,执行对应BPMN 2.0中的`'token'`,基本上执行指向流程实例当前在哪里
* `RuntimeService`可以在流程实例等待外部触发时使用,这时可以用来继续流程实例
* 流程实例可以有很多`暂停状态`,而服务提供了多种方法来触发实例,接受外部触发后,流程实例就会继续执行



### TaskService

* 任务是由真实人员执行的,是Activiti引擎的核心功能.所有与任务有关的功能都包含在**TaskService**中

- 查询分配给用户或组的任务
- 创建*独立运行*任务.这些任务与流程实例无关
- 手工设置任务的执行者,或者这些用户通过何种方式与任务关联
- 认领并完成一个任务.认领意味着一个人期望成为任务的执行者,即这个用户会完成这个任务通



### IdentityService

* Activiti7及以上版本中已经剔除了该组件
* **IdentityService**非常简单,它可以管理(创建,更新,删除,查询...)群组和用户
* Activiti执行时并没有对用户进行检查.例如,任务可以分配给任何人,但是引擎不会校验系统中是否存在这个用户.这是Activiti引擎也可以使用外部服务,比如ldap,活动目录,等等



### FormService

* Activiti7及以上版本中已经剔除了该组件
* **FormService**是一个可选服务.即使不使用它,Activiti也可以完美运行,不会损失任何功能
* 这个服务提供了*启动表单*和*任务表单*两个概念.*启动表单*会在流程实例启动之前展示给用户, *任务表单*会在用户完成任务时展示
* Activiti支持在BPMN 2.0流程定义中设置这些表单,这个服务以一种简单的方式将数据暴露出来



### HistoryService

* **HistoryService**提供了Activiti引擎手机的所有历史数据.
* 在执行流程时,引擎会保存很多数据（根据配置）,比如流程实例启动时间,任务的参与者, 完成任务的时间,每个流程实例的执行路径,等等. 这个服务主要通过查询功能来获得这些数据



### ManagementService

* **ManagementService**在使用Activiti的定制环境中基本上不会用到,它可以查询数据库的表和表的元数据
* 它提供了查询和管理异步操作的功能.Activiti的异步操作用途很多,比如定时器,异步操作, 延迟暂停,激活等



## 异常策略

* Activiti中的基础异常为ActivitiException,一个非检查异常,这个异常可以在任何时候被API抛出
* 为避免过多的异常继承,下面的子类用于特定的场合. 流程引擎和API调用的其他场合不会使用下面的异常, 它们会抛出一个普通的`ActivitiExceptions`
  * `ActivitiWrongDbException`:当Activiti引擎发现数据库版本号和引擎版本号不一致时抛出
  * `ActivitiOptimisticLockingException`:对同一数据进行并发方法并出现乐观锁时抛出
  * `ActivitiClassLoadingException`:当无法找到需要加载的类或在加载类时出现了错误
  * `ActivitiObjectNotFoundException`:当请求或操作的对应不存在时抛出
  * `ActivitiIllegalArgumentException`:这个异常表示调用Activiti API时传入了一个非法的参数,可能是引擎配置中的非法值,或提供了一个非法制,或流程定义中使用的非法值
  * `ActivitiTaskAlreadyClaimedException`:当任务已被认领,再调用`taskService.claim()`就会抛出



## 使用Activiti的服务

* 这个小例子的最终目标是做一个工作业务流程, 演示公司中简单的请假申请:

![](001.png)



### 发布流程



* 任何与静态资源有关的数据都可以通过**RepositoryService**访问
* 创建一个新的xml文件 `Test.bpmn20.xml`

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<definitions id="definitions"
             targetNamespace="http://Activiti.org/bpmn20"
             xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xmlns:Activiti="http://Activiti.org/bpmn">
    <process id="vacationRequest" name="Vacation request">
        <startEvent id="request" Activiti:initiator="employeeName">
            <extensionElements>
                <Activiti:formProperty id="numberOfDays" name="Number of days" type="long" value="1" required="true"/>
                <Activiti:formProperty id="startDate" name="First day of holiday (dd-MM-yyy)" datePattern="dd-MM-yyyy hh:mm" type="date" required="true" />
                <Activiti:formProperty id="vacationMotivation" name="Motivation" type="string" />
            </extensionElements>
        </startEvent>
        <sequenceFlow id="flow1" sourceRef="request" targetRef="handleRequest" />

        <userTask id="handleRequest" name="Handle vacation request" >
            <documentation>
                ${employeeName} would like to take ${numberOfDays} day(s) of vacation (Motivation: ${vacationMotivation}).
            </documentation>
            <extensionElements>
                <Activiti:formProperty id="vacationApproved" name="Do you approve this vacation" type="enum" required="true">
                    <Activiti:value id="true" name="Approve" />
                    <Activiti:value id="false" name="Reject" />
                </Activiti:formProperty>
                <Activiti:formProperty id="managerMotivation" name="Motivation" type="string" />
            </extensionElements>
            <potentialOwner>
                <resourceAssignmentExpression>
                    <formalExpression>management</formalExpression>
                </resourceAssignmentExpression>
            </potentialOwner>
        </userTask>
        <sequenceFlow id="flow2" sourceRef="handleRequest" targetRef="requestApprovedDecision" />
        <exclusiveGateway id="requestApprovedDecision" name="Request approved?" />
        <sequenceFlow id="flow3" sourceRef="requestApprovedDecision" targetRef="sendApprovalMail">
            <conditionExpression xsi:type="tFormalExpression">${vacationApproved == 'true'}</conditionExpression>
        </sequenceFlow>
        <task id="sendApprovalMail" name="Send confirmation e-mail" />
        <sequenceFlow id="flow4" sourceRef="sendApprovalMail" targetRef="theEnd1" />
        <endEvent id="theEnd1" />
        <sequenceFlow id="flow5" sourceRef="requestApprovedDecision" targetRef="adjustVacationRequestTask">
            <conditionExpression xsi:type="tFormalExpression">${vacationApproved == 'false'}</conditionExpression>
        </sequenceFlow>
        <userTask id="adjustVacationRequestTask" name="Adjust vacation request">
            <documentation>
                Your manager has disapproved your vacation request for ${numberOfDays} days.
                Reason: ${managerMotivation}
            </documentation>
            <extensionElements>
                <Activiti:formProperty id="numberOfDays" name="Number of days" value="${numberOfDays}" type="long" required="true"/>
                <Activiti:formProperty id="startDate" name="First day of holiday (dd-MM-yyy)" value="${startDate}" datePattern="dd-MM-yyyy hh:mm" type="date" required="true" />
                <Activiti:formProperty id="vacationMotivation" name="Motivation" value="${vacationMotivation}" type="string" />
                <Activiti:formProperty id="resendRequest" name="Resend vacation request to manager?" type="enum" required="true">
                    <Activiti:value id="true" name="Yes" />
                    <Activiti:value id="false" name="No" />
                </Activiti:formProperty>
            </extensionElements>
            <humanPerformer>
                <resourceAssignmentExpression>
                    <formalExpression>${employeeName}</formalExpression>
                </resourceAssignmentExpression>
            </humanPerformer>
        </userTask>
        <sequenceFlow id="flow6" sourceRef="adjustVacationRequestTask" targetRef="resendRequestDecision" />
        <exclusiveGateway id="resendRequestDecision" name="Resend request?" />
        <sequenceFlow id="flow7" sourceRef="resendRequestDecision" targetRef="handleRequest">
            <conditionExpression xsi:type="tFormalExpression">${resendRequest == 'true'}</conditionExpression>
        </sequenceFlow>
        <sequenceFlow id="flow8" sourceRef="resendRequestDecision" targetRef="theEnd2">
            <conditionExpression xsi:type="tFormalExpression">${resendRequest == 'false'}</conditionExpression>
        </sequenceFlow>
        <endEvent id="theEnd2" />
    </process>
</definitions>
```

* 为了让Activiti引擎知道这个流程,必须先进行发布.发布意味着引擎会把BPMN 2.0 xml解析成可以执行的东西,发布包中的所有流程定义都会添加到数据库中. 这样,当引擎重启时,它依然可以获得已发布的流程

```java
ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
RepositoryService repositoryService = processEngine.getRepositoryService();
repositoryService.createDeployment()
    .addClasspathResource("org/Activiti/test/VacationRequest.bpmn20.xml")
    .deploy();
Log.info("Number of process definitions: " + repositoryService.createProcessDefinitionQuery().count());       
```



### 启动一个流程实例

* 把流程定义发布到Activiti引擎后,可以基于它发起新流程实例.对每个流程定义,都可以有很多流程实例.流程定义是蓝图,流程实例是它的一个运行的执行
* 所有与流程运行状态相关的东西都可以通过**RuntimeService**获得.有很多方法可以启动一个新流程实例
* 在下面的代码中,使用定义在流程定义xml 中的key来启动流程实例.也可以在流程实例启动时添加一些流程变量,因为第一个用户任务的表达式需要这些变量.流程变量经常会被用到,因为它们赋予来自同一个流程定义的不同流程实例的特别含义.简单来说,流程变量是区分流程实例的关键

```java
Map<String, Object> variables = new HashMap<String, Object>();
variables.put("employeeName", "Kermit");
variables.put("numberOfDays", new Integer(4));
variables.put("vacationMotivation", "I'm really tired!");
RuntimeService runtimeService = processEngine.getRuntimeService();
ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("vacationRequest", variables);
// Verify that we started a new process instance
Log.info("Number of process instances: " + runtimeService.createProcessInstanceQuery().count());
```



### 完成任务

* 流程启动后,第一步就是用户任务.这是必须由系统用户处理的一个环节
* 通常,用户会有一个任务列表,展示了所有必须由整个用户处理的任务

```java
TaskService taskService = processEngine.getTaskService();
List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("management").list();
for (Task task : tasks) {
    Log.info("Task available: " + task.getName());
}
```

* 为了让流程实例继续运行,需要完成整个任务.对Activiti来说,就是需要`complete`任务

```java
Task task = tasks.get(0);
Map<String, Object> taskVariables = new HashMap<String, Object>();
taskVariables.put("vacationApproved", "false");
taskVariables.put("managerMotivation", "We have a tight deadline!");
taskService.complete(task.getId(), taskVariables);
```

* 流程实例会进入到下一个环节.在这里例子中,下一环节允许员工通过表单调整原始的请假申请.员工可以重新提交请假申请, 这会使流程重新进入到第一个任务



### 挂起/激活一个流程

* 当挂起流程定义时,就不能创建新流程了(会抛出一个异常).可以通过`RepositoryService`挂起一个流程

```java
repositoryService.suspendProcessDefinitionByKey("vacationRequest");
try {
    runtimeService.startProcessInstanceByKey("vacationRequest");
} catch (ActivitiException e) {
    e.printStackTrace();
}
```

* 要想重新激活一个流程定义,可以调用`repositoryService.activateProcessDefinitionXXX`方法
* 挂起流程实例时,流程不能继续执行,异步操作也不会执行
  * 挂起流程实例可以调用 `runtimeService.suspendProcessInstance`方法
  * 激活流程实例可以调用`runtimeService.activateProcessInstanceXXX`方法



## 查询API

* 有两种方法可以从引擎中查询数据:查询API和原生查询
* 查询API提供了完全类型安全的API,可以为自己的查询条件添加很多条件,所有条件都以AND组合和精确的排序条件

```java
List<Task> tasks = taskService.createTaskQuery()
    .taskAssignee("kermit")
    .processVariableValueEquals("orderId", "0815")
    .orderByDueDate().asc()
    .list();
```

* 需要更强大的查询时,比如使用OR条件或不能使用查询API实现的条件.这时,可是使用原生查询,它可以编写自己的SQL查询,返回类型由使用的查询对象决定,数据会映射到正确的对象上.比如,任务,流程实例,执行等等.因为查询会作用在数据库上,必须使用数据库中定义的表名和列名.这要求了解内部数据结构,因此使用原生查询时一定要注意.表名可以通过API获得,可以尽量减少对数据库的依赖

```java
List<Task> tasks = taskService.createNativeTaskQuery()
    .sql("SELECT count(*) FROM " + managementService.getTableName(Task.class) + " T WHERE T.NAME_ = #{taskName}")
    .parameter("taskName", "gonzoTask").list();
long count = taskService.createNativeTaskQuery()
    .sql("SELECT count(*) FROM " + managementService.getTableName(Task.class)+" T1,"
         + managementService.getTableName(VariableInstanceEntity.class) + " V1 WHERE V1.TASK_ID_ = T1.ID_").count();
```



## 表达式



* Activiti使用UEL处理表达式
* UEL即*统一表达式语言*,它是EE6规范的一部分([EE6规范](http://docs.oracle.com/javaee/6/tutorial/doc/gjddd.html)).为了在所有运行环境都支持最新UEL的所有共嫩个,我们使用了一个JUEL的修改版本
* 表达式可以用在很多场景下,比如[Java服务任务](http://www.mossle.com/docs/Activiti/#bpmnJavaServiceTaskXML),[执行监听器](http://www.mossle.com/docs/Activiti/#executionListeners),[任务监听器](http://www.mossle.com/docs/Activiti/#taskListeners)和[条件流](http://www.mossle.com/docs/Activiti/#conditionalSequenceFlowXml).虽然有两重表达式,值表达式和方法表达式,Activiti进行了抽象,所以两者可以同样使用在需要`表达式`的场景中

- **Value expression**:解析为值.默认所有流程变量,所有spring bean可以使用
  - `${myVar}`,`${myBean.myProperty}`

- **Method expression**:调用一个方法,使用或不使用参数.**当调用一个无参数的方法时,要在方法名后添加空的括号(以区分值表达式).**传递的参数可以是字符串也可以是表达式,它们会被自动解析
  - `${printer.print()}`,`${myBean.addNewOrder('orderName')}`,`${myBean.doSomething(myVar, execution)}`
- 注意这些表达式支持解析原始类型(包括比较),bean,list,数组和map
- 在所有流程实例中,表达式中还可以使用一些默认对象
  - `execution`:`DelegateExecution`提供外出执行的额外信息
  - `task`:`DelegateTask`提供当前任务的额外信息.**注意,只对任务监听器的表达式有效.**
  - `authenticatedUserId`:当前登录的用户id.如果没有用户登录,这个变量就不可用



## 单元测试



* 业务流程是软件项目的一部分,它也应该和普通的业务流程一样进行测试:使用单元测试. 因为Activiti是一个嵌入式的java引擎, 为业务流程编写单元测试和写普通单元测试完全一样
* Activiti支持JUnit 3和4进行单元测试.使用JUnit 3时, 必须集成`ActivitiTestCase`. 它通过保护的成员变量提供ProcessEngine和服务,在测试的`setup()`中, 默认会使用classpath下的`Activiti.cfg.xml`初始化流程引擎.想使用不同的配置文件,可以重写*getConfigurationResource()*方法. 如果配置文件相同的话,对应的流程引擎会被静态缓存, 就可以用于多个单元测试
* 继承`ActivitiTestCase`,可以在测试方法上使用 `org.Activiti.engine.test.Deployment`注解
* 测试执行前,与测试类在同一个包下的,格式为`testClassName.testMethod.bpmn20.xml`的资源文件会被部署
* 测试结束后,发布包也会被删除,包括所有相关的流程实例,任务,等等
* `Deployment`注解可以直接设置资源的位置

```java
public class MyBusinessProcessTest extends ActivitiTestCase {
    @Deployment
    public void testSimpleProcess() {
        runtimeService.startProcessInstanceByKey("simpleProcess");
        Task task = taskService.createTaskQuery().singleResult();
        assertEquals("My Task", task.getName());
        taskService.complete(task.getId());
        assertEquals(0, runtimeService.createProcessInstanceQuery().count());
    }
}
```

* 要想在使用JUnit 4编写单元测试时获得同样的功能,可以使用`ActivitiRule`.通过它,可以通过getter方法获得流程引擎和各种服务
* 和 `ActivitiTestCase`一样,使用这个`Rule`也会启用`org.Activiti.engine.test.Deployment`注解,它会在classpath下查找默认的配置文件.如果配置文件相同的话,对应的流程引擎会被静态缓存, 就可以用于多个单元测试

```java
public class MyBusinessProcessTest {

    @Rule
    public ActivitiRule ActivitiRule = new ActivitiRule();

    @Test
    @Deployment
    public void ruleUsageExample() {
        RuntimeService runtimeService = ActivitiRule.getRuntimeService();
        runtimeService.startProcessInstanceByKey("ruleUsage");
        TaskService taskService = ActivitiRule.getTaskService();
        Task task = taskService.createTaskQuery().singleResult();
        assertEquals("My Task", task.getName());
        taskService.complete(task.getId());
        assertEquals(0, runtimeService.createProcessInstanceQuery().count());
    }
}
```



## Web应用中的流程引擎



* `ProcessEngine`是线程安全的,可以在多线程下共享.在web应用中, 意味着可以在容器启动时创建流程引擎, 在容器关闭时关闭流程引擎

```java
public class ProcessEnginesServletContextListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ProcessEngines.init();
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        ProcessEngines.destroy();
    }
}
```

* `contextInitialized`方法会执行`ProcessEngines.init()`,这会查找classpath下的`Activiti.cfg.xml`文件,根据配置文件创建一个`ProcessEngine`.如果classpath中包含多个配置文件,确认它们有不同的名字,当需要使用流程引擎时,可以通过`ProcessEngines.getDefaultProcessEngine()`或`ProcessEngines.getProcessEngine("myName")`
* 当然,也可以使用其他方式创建流程引擎
* ContextListener中的`contextDestroyed`方法会执行`ProcessEngines.destroy()`.这会关闭所有初始化的流程引擎

# 部署



## 业务文档



* 为了部署流程,它们不得不包装在一个业务文档中
* 一个业务文档是Activiti引擎部署的单元,相当与一个压缩文件,它包含BPMN2.0流程,任务表单,规则和其他任意类型的文件.大体上,业务文档是包含命名资源的容器
* 当一个业务文档被部署,它将会自动扫描`.bpmn20.xml` 或者`.bpmn`结尾的BPMN文件,每个文件都将会被解析并且可能会包含多个流程定义
* 业务归档中的Java类**将不能够添加到类路径下**.为了能够让流程运行,必须把存在于业务归档程中的流程定义使用的所有自定义的类(例如Java服务任务或者实现事件的监听器)放在Activiti引擎的类路径下



### 编程式部署



* 通过一个压缩文件(支持Zip和Bar)部署业务归档

```java
String barFileName = "path/to/process-one.bar";
ZipInputStream inputStream = new ZipInputStream(new FileInputStream(barFileName));
repositoryService.createDeployment()
    .name("process-one.bar")
    .addZipInputStream(inputStream)
    .deploy();
```

* 也可以通过一个独立资源如bpmn,xml等构建部署



### Activiti War部署



* activit-admin,activiti-app允许通过Web界面的用户接口上传一个bar格式的压缩文件(或`bpmn20.xml`格式的文件),选择*Management* *标签* 和 点击 *Deployment*

![](002.png)

* 从电脑上面选择一个文件,或者拖拽到指定的区域

![](003.png)



## 外部资源

流程定义保存在Activiti所支持的数据库中,当使用服务任务,执行监听器或Activiti中配置的Spring beans时,流程定义能够引用这些委托类.这些类或者Spring配置文件对于所有流程引擎中可能执行的流程定义必须是可用的



### Java类

* 当流程实例被启动的时候,在流程中被使用的所有自定义类(如服务任务中使用的JavaDelegates,事件监听器,任务监听器等)应该存在与流程引擎的类路径下
* 然后,在部署业务文档时,这些类不必都存在于类路径下.当使用Ant部署一个新的业务文档时,这意味着委托类不必存在与类路径下
* 当使用示例设置并添加自定义类时,应该添加包含自定义类的jar包到activiti-admin控制台或activiti-rest 的webapp lib文件夹中
* 不要忽略自定义类的依赖.另外,还可以包含自己的依赖添加到Tomcat目录中的`${tomcat.home}/lib`



### 在流程中使用Spring beans

当表达式或者脚本使用Spring beans时,这些beans对于引擎执行流程定义时必须是可用的.如果要构建自己的Web应用并且按照Spring集成中描述那样在应用上下文配置流程引擎是非常简单的.但是如果要使用 Activiti rest web应用,那么也应该更新 Activiti rest web应用的上下文.可以把在`Activiti-rest/lib/Activiti-cfg.jar` 文件中的`Activiti.cfg.xml`替换成Spring上下文配置的`Activiti-context.xml`文件



### 创建独立应用



可以把Activiti rest web应用加入到自己的web应用之中,仅仅只需要配置一个 `ProcessEngine`,从而不用确保所有的流程引擎的所有委托类在类路径下面并且是否使用正确的spring配置



## 流程定义的版本



* BPMN中并没有版本的概念,没有版本也是不错的,因为可执行的BPMN流程作为你开发项目的一部分存在版本控制系统的知识库中
* 在Activiti中,流程定义的版本是在部署时创建的.在部署时,流程定义被存储到数据库之前,Activiti将会自动给 `流程定义` 分配一个版本号
* 对于业务文档中每一个的流程定义,都会通过下列部署执行初始化属性`key`, `version`, `name` 和 `id`
  * XML文件中流程定义(流程模型)的`id`属性被当做是流程定义的 `key`属性
  * XML文件中的流程模型的`name`属性被当做是流程定义的`name`属性.如果该name属性并没有指定,那么id属性被当做是name
  * 带有特定key的流程定义在第一次部署时,将会自动分配版本号为1,对于之后部署相同key的流程定义时,版本号将会加1.该key属性被用来区别不同的流程定义
  * 流程定义中的id属性为{processDefinitionKey}:{processDefinitionVersion}:{generated-id},`generated-id`是一个唯一的数字,用于确保在集群环境中流程定义的唯一性



### 一个例子



```xml
<definitions id="myDefinitions" >
  <process id="myProcess" name="My important process" >
    ...
```

* 当部署了这个流程定义之后,在数据库中的流程定义如下

| id              | key       | name                 | version |
| --------------- | --------- | -------------------- | ------- |
| myProcess:1:676 | myProcess | My important process | 1       |

* 假设现在部署用一个流程的最新版本号,但是流程定义的`id`保持不变. 流程定义表将包含以下列表信息



| id              | key       | name                 | version |
| --------------- | --------- | -------------------- | ------- |
| myProcess:1:676 | myProcess | My important process | 1       |
| myProcess:2:870 | myProcess | My important process | 2       |

* 当`runtimeService.startProcessInstanceByKey("myProcess")`方法被调用时,它将会使用流程定义版本最新的2.每次流程定义创建流程实例时,都会默认使用最新版本的流程定义
* 创建第二个流程,在Activiti中,定义并且部署它,该流程定义会添加到流程定义表中

```xml
<definitions id="myNewDefinitions" >
  <process id="myNewProcess" name="My important process" >
    ...
```

* 表信息如下

| id                  | key          | name                 | version |
| ------------------- | ------------ | -------------------- | ------- |
| myProcess:1:676     | myProcess    | My important process | 1       |
| myProcess:2:870     | myProcess    | My important process | 2       |
| myNewProcess:1:1033 | myNewProcess | My important process | 1       |

* 新流程的key与我们的第一个流程是不同的,尽管流程定义的名称是相同的,Activiti仅仅只考虑`id`属性判断流程.因此,新的流程定义部署的版本号为1



## 提供流程图片



* 流程定义的流程图可以被添加到部署中,该流程图将会持久化到数据库中并且可以通过API进行访问.该流程图也可以被用来在Activiti Explorer控制台中的流程中进行显示

* 如果在我们的类路径下面有一个流程,`org/Activiti/expenseProcess.bpmn20.xml`,该流程定义有一个流程key expense. 以下遵循流程定义图片的命名规范
  * 如果在部署时一个图片资源已经存在,它是BPMN2.0的XML文件名后面是流程定义的key并且是一个图片的后缀,那么该图片将被使用.如果在一个BPMN2.0 XML文件中定义多个流程定义图片,这种方式更有意义.每个流程定义图片的文件名中都将会有一个流程定义key
  * 如果并没有这样的图片存在,部署的时候寻找与匹配BPMN2.0 XML文件的名称的图片资源.这意味着在同一个BPMN2.0 XML文件夹中的**每个流程定义**都会有相同的流程定义图片

* 使用编程式的部署方式

```java
repositoryService.createDeployment()
    .name("expense-process.bar")
    .addClasspathResource("org/Activiti/expenseProcess.bpmn20.xml")
    .addClasspathResource("org/Activiti/expenseProcess.png")
    .deploy();
```

* 通过API来获取流程定义图片资源

```java
ProcessDefinition processDefinition=repositoryService.createProcessDefinitionQuery()
    .processDefinitionKey("expense").singleResult();
String diagramResourceName = processDefinition.getDiagramResourceName();
InputStream imageStream = repositoryService
    .getResourceAsStream(processDefinition.getDeploymentId(), diagramResourceName);
```



## 自动生成流程图片



* 部署没有提供图片时,如果流程定义中包含必要的图像交换信息时,Activiti流程引擎竟会自动生成一个图像
* 该资源可以按照部署时提供流程图片完全相同的方式获取
* 如果部署时,并不需要生成流程定义图片,那么就需要在流程引擎配置`isCreateDiagramOnDeploy`

```xml
<property name="createDiagramOnDeploy" value="false" />
```



## 类别



* 部署和流程定义都是用户定义的类别.流程定义类别在BPMN文件中属性的初始化的值`<definitions ... targetNamespace="yourCategory" ...` 

* 部署类别是可以直接使用API进行指定的

```java
repositoryService
    .createDeployment()
    .category("yourCategory")
    ...
    .deploy();
```



# Spring集成



* Activiti6及以下版本和Spring集成



## 事务

* 在Spring examples的`SpringTransactionIntegrationTest`例子中,Spring配置文件展示了包括数据源(dataSource),事务管理器(transactionManager),流程引擎(processEngine)和Activiti引擎服务
* 当把数据源(DataSource)传递给`SpringProcessEngineConfiguration`(使用dataSource属性)之后,Activiti内部使用了一个`TransactionAwareDataSourceProxy`代理来封装传递进来的数据源.这样做是为了确保从数据源获取的SQL连接能够与Spring的事物结合在一起发挥得更出色.这意味它不再需要在你的Spring配置中代理数据源了.然而它仍然允许你传递一个`TransactionAwareDataSourceProxy`到`SpringProcessEngineConfiguration`中.在这个例子中并不会发生多余的包装
* 为了确保在Spring配置中申明的`TransactionAwareDataSourceProxy`,不能把使用它的应用交给Spring事物控制的资源.(例如DataSourceTransactionManager和JPATransactionManager需要非代理的数据源)

```xml
<beans xmlns="http://www.springframework.org/schema/beans" 
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans   http://www.springframework.org/schema/beans/spring-beans.xsd
                           http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-2.5.xsd
                           http://www.springframework.org/schema/tx      http://www.springframework.org/schema/tx/spring-tx-3.0.xsd">

    <bean id="dataSource" class="org.springframework.jdbc.datasource.SimpleDriverDataSource">
        <property name="driverClass" value="org.h2.Driver" />
        <property name="url" value="jdbc:h2:mem:Activiti;DB_CLOSE_DELAY=1000" />
        <property name="username" value="sa" />
        <property name="password" value="" />
    </bean>

    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource" />
    </bean>

    <bean id="processEngineConfiguration" class="org.Activiti.spring.SpringProcessEngineConfiguration">
        <property name="dataSource" ref="dataSource" />
        <property name="transactionManager" ref="transactionManager" />
        <property name="databaseSchemaUpdate" value="true" />
        <property name="jobExecutorActivate" value="false" />
    </bean>

    <bean id="processEngine" class="org.Activiti.spring.ProcessEngineFactoryBean">
        <property name="processEngineConfiguration" ref="processEngineConfiguration" />
    </bean>

    <bean id="repositoryService" factory-bean="processEngine" factory-method="getRepositoryService" />
    <bean id="runtimeService" factory-bean="processEngine" factory-method="getRuntimeService" />
    <bean id="taskService" factory-bean="processEngine" factory-method="getTaskService" />
    <bean id="historyService" factory-bean="processEngine" factory-method="getHistoryService" />
    <bean id="managementService" factory-bean="processEngine" factory-method="getManagementService" />
```

* Spring配置文件的其余部分beans:

```xml
<beans>  
  ...
  <tx:annotation-driven transaction-manager="transactionManager"/>
  <bean id="userBean" class="org.Activiti.spring.test.UserBean">
    <property name="runtimeService" ref="runtimeService" />
  </bean>
  <bean id="printer" class="org.Activiti.spring.test.Printer" />
</beans>
```

* 使用任意的一种Spring创建应用上下文的方式创建其Spring应用上下文.在这个例子中你可以使用类路径下面的XML资源来配置我们的Spring应用上下文:

```java
ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("org/Activiti/examples/spring/SpringTransactionIntegrationTest-context.xml");
```

* 此时可以得到Activiti的服务beans并且调用该服务上面的方法
* ProcessEngineFactoryBean将会对该服务添加一些额外的拦截器,在Activiti服务上面的方法使用的是 Propagation.REQUIRED事物语义.可以使用repositoryService去部署一个流程:

```java
RepositoryService repositoryService = (RepositoryService) applicationContext.getBean("repositoryService");
String deploymentId = repositoryService.createDeployment()
    .addClasspathResource("org/Activiti/spring/test/hello.bpmn20.xml")
    .deploy()
    .getId();
```

* 其他相同的服务也是同样可以这么使用.在这个例子中,Spring的事物将会围绕在userBean.hello()上,并且调用Activiti服务的方法也会加入到这个事物中

```java
UserBean userBean = (UserBean) applicationContext.getBean("userBean");
userBean.hello();
```

* UserBean

```java
public class UserBean {

    private RuntimeService runtimeService;

    @Transactional
    public void hello() {
        //这里,可以在领域模型中做一些事物处理
        //当在调用Activiti RuntimeService的startProcessInstanceByKey方法时,
        //它将会结合到同一个事物中
        runtimeService.startProcessInstanceByKey("helloProcess");
    }

    public void setRuntimeService(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }
}
```



## 表达式



* 当使用ProcessEngineFactoryBean时,默认情况下,在BPMN流程中的所有[表达式](http://www.mossle.com/docs/Activiti/#apiExpressions)都将会看见所有的Spring beans.它可以限制你在表达式中暴露出的beans或者甚至可以在你的配置中使用一个Map不暴露任何beans
* 下面的例子暴露了一个单例bean(printer),可以把printer当作关键字使用
* 想要不暴露任何beans,仅仅只需要在SpringProcessEngineConfiguration中传递一个空的list作为beans的属性
* 当不设置beans的属性时,在应用上下文中Spring beans都是可以使用的

```xml
<bean id="processEngineConfiguration" class="org.Activiti.spring.SpringProcessEngineConfiguration">
    ...
    <property name="beans">
        <map>
            <entry key="printer" value-ref="printer" />
        </map>
    </property>
</bean>

<bean id="printer" class="org.Activiti.examples.spring.Printer" />

```

* 现在暴露出来的beans就可以在表达式中使用.例如,在SpringTransactionIntegrationTest中的 `hello.bpmn20.xml`展示的是如何使用UEL方法表达式去调用Spring bean的方法 

```xml
<definitions id="definitions" ...>
    <process id="helloProcess">
        <startEvent id="start" />
        <sequenceFlow id="flow1" sourceRef="start" targetRef="print" />
        <serviceTask id="print" Activiti:expression="#{printer.printMessage()}" />
        <sequenceFlow id="flow2" sourceRef="print" targetRef="end" />
        <endEvent id="end" />
    </process>
</definitions>
```

* Spring bean的配置

```xml
<beans ...>
  <bean id="printer" class="org.Activiti.examples.spring.Printer" />
</beans>
```



## 资源的自动部署



* Spring的集成也有一个专门用于对资源部署的特性
* 在流程引擎的配置中,可以指定一组资源,当流程引擎被创建时,所有在这里的资源都将会被自动扫描与部署
* 自动部署有过滤以防止资源重新部署的功能,只有当这个资源真正发生改变的时候,它才会向Activiti使用的数据库创建新的部署

```xml
<bean id="processEngineConfiguration" class="org.Activiti.spring.SpringProcessEngineConfiguration">
    ...
    <property name="deploymentResources" value="classpath*:/org/Activiti/spring/test/autodeployment/autodeploy.*.bpmn20.xml" />
</bean>

<bean id="processEngine" class="org.Activiti.spring.ProcessEngineFactoryBean">
    <property name="processEngineConfiguration" ref="processEngineConfiguration" />
</bean>
```



## 单元测试



* 当集成Spring时,使用标准的[Activiti测试工具类](http://www.mossle.com/docs/Activiti/#apiUnitTesting)是非常容易的对业务流程进行测试

```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:org/Activiti/spring/test/junit4/springTypicalUsageTest-context.xml")
public class MyBusinessProcessTest {

  @Autowired
  private RuntimeService runtimeService;

  @Autowired
  private TaskService taskService;

  @Autowired
  @Rule
  public ActivitiRule ActivitiSpringRule;

  @Test
  @Deployment
  public void simpleProcessTest() {
    runtimeService.startProcessInstanceByKey("simpleProcess");
    Task task = taskService.createTaskQuery().singleResult();
    assertEquals("My Task", task.getName());
    taskService.complete(task.getId());
    assertEquals(0, runtimeService.createProcessInstanceQuery().count());
  }
}
```

* 对于这种方式,需要在Spring配置中定义一个org.Activiti.engine.test.ActivitiRule

```xml
<bean id="ActivitiRule" class="org.Activiti.engine.test.ActivitiRule">
    <property name="processEngine" ref="processEngine" />
</bean>
```



# 表单



* Activiti7之后无表单模块
* Activiti提供了一种方便而且灵活的方式在业务流程中以手工方式添加表单
* 对表单的支持有2种方式:通过表单属性对内置表单进行渲染和外置表单进行渲染



## 表单属性



* 业务流程相关联的所有信息要么是包含自身的流程变量,要么是通过流程变量的引用.Activiti支持存储复杂的Java对象作为流程变量,如`序列化`对象,Jpa实体对象或者整个XML文档作为`字符串`
* 用户是在启动一个流程和完成用户任务时,与流程进行交互的.表单需要某个UI技术渲染之后才能够与用户进行交互.为了能够使用不同UI技术变得容易,流程定义包含一个对流程变量中复杂的Java类型对象到一个properties的`Map<String,String>`类型的转换逻辑
* 使用Activiti API的方法查看公开的属性信息.然后,任意UI技术都能够在这些属性上面构建一个表单.该属性专门(并且更多局限性)为流程变量提供了一个视图. 表单所需要显示的属性可以从FormData中获取

```java
StartFormData FormService.getStartFormData(String processDefinitionId);
// 或者
TaskFormdata FormService.getTaskFormData(String taskId);
```

* 在默认情况下,内置的表单引擎,sees这些变量就像对待流程变量一样.如果任务表单属性和流程变量是一对一的关系,那么任务表单属性就不需要进行申明了

```xml
<startEvent id="start" />
```

* 当执行到开始事件时,所有的流程变量都是可用的,但会是一个空值,因为没有定义具体的映射

```java
formService.getStartFormData(String processDefinitionId).getFormProperties();
```

* 在上面的实例中,所有被提交的属性都将会作为流程变量被存储在Activiti使用的数据库中.这意味着在一个表单中新添加一个简单的input输入字段,也会作为一个新的变量被存储
* 属性来自于流程变量,但不一定非要作为流程变量存储起来.例如,流程变量可能是JPA实体Address,在某种UI技术中使用的表单属性`StreetName`可能会关联到一个表达式 `#{address.street}`
* 类似的,用户提交的表单属性应该作为流程变量进行存储或者使用UEL值表达式将其作为流程变量的一个嵌套属性进行存储,例如`#{address.street}`
* 同样的,提交的表单属性默认的行为是作为流程变量进行存储,除非一个`formProperty` 申明了其他规则
* 类型转换同样也可以应用于表单数据和流程变量之间处理的一部分
  * 表单属性`room`将会被映射为String类型流程变量 `room`
  * 表单属性`duration`将会被映射为java.lang.Long类型流程变量 `duration`
  * 表单属性`speaker`将会被映射为流程变量 `SpeakerName`.它只能够在TaskFormData对象中使用.如果属性speaker提交,将会抛出一个ActivitiException的异常.类似的,将其属性设置为`readable="false"`,该属性就会在FormData进行排除,但是在提交后仍然会对其进行处理
  * 表单属性`street`将会映射为JavaBean `address`的属性`street`,作为String类型的流程变量.当提交的表单属性并没有提供并且required="true"时,那么就会抛出一个异常

```xml
<userTask id="task">
  <extensionElements>
    <Activiti:formProperty id="room" />
    <Activiti:formProperty id="duration" type="long"/>
    <Activiti:formProperty id="speaker" variable="SpeakerName" writable="false" />
    <Activiti:formProperty id="street" expression="#{address.street}" required="true" />
  </extensionElements>
</userTask>
```

- 表单数据也可以作为FormData的一部分提供类型元数据.该FormData可以从`StartFormData FormService.getStartFormData(String processDefinitionId)`和`TaskFormdata FormService.getTaskFormData(String taskId)`方法的返回值中获取
- 支持以下的几种表单属性类型
  - `string` (org.Activiti.engine.impl.form.StringFormType)
  - `long` (org.Activiti.engine.impl.form.LongFormType)
  - `enum`     (org.Activiti.engine.impl.form.EnumFormType)
  - `date`     (org.Activiti.engine.impl.form.DateFormType)
  - `boolean`     (org.Activiti.engine.impl.form.BooleanFormType)
- 对于申明每一个表单属性,以下的`FormProperty`信息可以通过`formService.getStartFormData(processDefinitionId).getFormProperties()` 和 `formService.getTaskFormData(taskId.getFormProperties()`获取

```java
public interface FormProperty {
    /**

  the key used to submit the property in {@link FormService#submitStartFormData(String, java.util.Map)}
   * or {@link FormService#submitTaskFormData(String, java.util.Map)} */
    String getId();
    /** the display label */
    String getName();
    /** one of the types defined in this interface like e.g. {@link #TYPE_STRING} */
    FormType getType();
    /** optional value that should be used to display in this property */
    String getValue();
    /** is this property read to be displayed in the form and made accessible with the methods
   * {@link FormService#getStartFormData(String)} and {@link FormService#getTaskFormData(String)}. */
    boolean isReadable();
    /** is this property expected when a user submits the form? */
    boolean isWritable();
    /** is this property a required input field */
    boolean isRequired();
}
```



```xml
<startEvent id="start">
    <extensionElements>
        <Activiti:formProperty id="speaker"
                               name="Speaker"
                               variable="SpeakerName"
                               type="string" />
        <Activiti:formProperty id="start"
                               type="date"
                               datePattern="dd-MMM-yyyy" />
        <Activiti:formProperty id="direction" type="enum">
            <Activiti:value id="left" name="Go Left" />
            <Activiti:value id="right" name="Go Right" />
            <Activiti:value id="up" name="Go Up" />
            <Activiti:value id="down" name="Go Down" />
        </Activiti:formProperty>
    </extensionElements>
</startEvent>
```

* 所有的表单属性信息都可以通过API访问
  * `formProperty.getType().getName()`:获取类型的名称
  * `formProperty.getType().getInformation("datePattern")`:获取日期的匹配方式
  * `formProperty.getType().getInformation("values")`:获取枚举值
* Activiti控制台支持表单属性并且可以根据表单定义对表单进行渲染.例如下面的XML片段

```xml
<startEvent ... >
    <extensionElements>
        <Activiti:formProperty id="numberOfDays" name="Number of days" value="${numberOfDays}" type="long" required="true"/>
        <Activiti:formProperty id="startDate" name="First day of holiday (dd-MM-yyy)" value="${startDate}" datePattern="dd-MM-yyyy hh:mm" type="date" required="true" />
        <Activiti:formProperty id="vacationMotivation" name="Motivation" value="${vacationMotivation}" type="string" />
    </extensionElements>
    </userTask>
```

* 当使用 Activiti控制台时,它将会被渲染成流程的启动表单



## 外置表单的渲染



* 该API同样也允许你执行Activiti流程引擎之外的方式渲染你自己的任务表单.这些步骤说明你可以用你自己的方式对任务表单进行渲染
* 本质上,所有需要渲染的表单属性都是通过2个服务方法中的一个进行装配的:
  * `StartFormData FormService.getStartFormData(processDefinitionId)` 
  * `TaskFormdata FormService.getTaskFormData(taskId)`
* 表单属性可以通过`FormService.submitStartFormData(processDefinitionId,properties)`, `FormService.submitStartFormData(taskId,properties)`2种方式进行提交
* 可以将任何表单模版资源放进要部署的业务文档之中,如果你想要将它们按照流程的版本进行存储.它将会在部署中作为一种可用的资源
* 使用`ProcessDefinition.getDeploymentId()`和` RepositoryService.getResourceAsStream(deploymentId,resourceName)` 获取部署上去的表单模版.这样就可以获取表单模版定义文件 ,就可以在自己的应用中渲染/显示表单
* 也可以使用该功能获取任务表单之外的其他的部署资源用于其他的目的
* 属性`<userTask Activiti:formKey="..."`可通过 ` FormService.getStartFormData( processDefinitionId).getFormKey()` 和 `FormService.getTaskFormData( taskId).getFormKey()`暴露出来.可以使用这个存储部署的模版中的全名(例如`org/Activiti/example/form/my-custom-form.xml`),但这并不是必须的.例如,可以在表单属性中存储一个通用的key,然后运用一种算法或者换转去得到你实际使用的模版.当你想要通过不同UI技术渲染不能的表单,这可能更加方便.例如,使用正常屏幕大小的web应用程序的表单,移动手机小屏幕的表单和甚至可能是IM表单和email表单模版



# 监听流程解析



* bpmn2.0 xml文件需要被解析为Activiti内部模型,然后才能在Activiti引擎中运行.解析过程发生在发布流程或在内存中找不到对应流程的时候,这时会从数据库查询对应的xml
* 对于每个流程,`BpmnParser`类都会创建一个新的`BpmnParse`实例.该实例会作为解析过程中的容器来使用
* 解析过程:对于每个BPMN 2.0元素,引擎中都会有一个对应的`BpmnParseHandler`实例,这样,解析器会保存一个BPMN 2.0元素与`BpmnParseHandler`实例的映射
* Activiti默认使用`BpmnParseHandler`处理所有支持的元素,也使用它来提供执行监听器,以支持流程历史
* 可以向Activiti引擎中添加自定义的`BpmnParseHandler`实例.经常看到的用例是把执行监听器添加到对应的环节,来处理一些事件队列
* 要想添加这种自定义处理器,需要为Activiti添加如下配置:

```xml
<property name="preBpmnParseHandlers">
    <list>
        <bean class="org.Activiti.parsing.MyFirstBpmnParseHandler" />
    </list>
</property>

<property name="postBpmnParseHandlers">
    <list>
        <bean class="org.Activiti.parsing.MySecondBpmnParseHandler" />
        <bean class="org.Activiti.parsing.MyThirdBpmnParseHandler" />
    </list>
</property>
```

* `preBpmnParseHandlers`:添加该该属性的`BpmnParseHandler`实例会添加在默认处理器的前面,而`postBpmnParseHandlers`会加在后面.当自定义处理器内部逻辑对处理顺序有要求时就很重要了



## BpmnParseHandler



```java
public interface BpmnParseHandler {

    Collection<Class>? extends BaseElement>> getHandledTypes();

    void parse(BpmnParse bpmnParse, BaseElement element);
}
```

* `getHandledTypes()`:翻译这个解析器处理的所有类型的集合.可以继承`AbstractBpmnParseHandler`并重写`getHandledType()`, 这样就只需要返回一个类型,而不是一个集合.这个类也包含了需要默认解析处理器所需要的帮助方法.`BpmnParseHandler`实例只有在解析器访问到这个方法返回的类型时才会被调用

```java
// 当BPMN 2.0 xml包含process元素时,就会执行executeParse方法中的逻辑
public class TestBPMNParseHandler extends AbstractBpmnParseHandler<Process> {

    protected Class<? extends BaseElement> getHandledType() {
        return Process.class;
    }

    protected void executeParse(BpmnParse bpmnParse, Process element) {
        ..
    }
}
```

* 在编写自定义解析处理器时,不要使用任何解析BPMN 2.0结构的内部类,这会很难找到问题.应该实现*BpmnParseHandler*接口或继承内部抽象类AbstractBpmnParseHandler
* 可以替换默认的`BpmnParseHandler`实例:

```xml
<property name="customDefaultBpmnParseHandlers">
    <list>
        ...
    </list>
</property>
```

* 强行把所有服务任务都设置为异步的:

```java
public class CustomUserTaskBpmnParseHandler extends ServiceTaskParseHandler {
    protected void executeParse(BpmnParse bpmnParse, ServiceTask serviceTask) {
        // Do the regular stuff
        super.executeParse(bpmnParse, serviceTask);
        // Make always async
        ActivityImpl activity = findActivity(bpmnParse, serviceTask.getId());
        activity.setAsync(true);
    }
}
```



## 支持高并发的UUID生成器



* 在一些高并发的场景,默认的id生成器可能因为无法很快的获取新id区域而导致异常
* 所有流程引擎都有一个id生成器,默认的id生成器会在数据库划取一块id范围,这样其他引擎就不能使用相同范围的id.在引擎运行期间,当默认的id生成器发现已经越过id范围时,就会启动一个新事务来获得新范围.在非常极限的情况下,这会在非常高负载的情况下导致问题
* 对于大部分情况,默认id生成就足够了.默认的`DbIdGenerator` 也有一个`idBlockSize`属性,可以配置获取id范围的大小,这样可以改变获取id的行为
* 另一个可选的默认id生成器是StrongUuidGenerator,它会在本地生成一个唯一的UUID, 把它作为所有实体的标识.因为生成UUID不需要访问数据库,所以它在高并发环境下的表现比较好
* 要注意默认id生成器的性能都依赖于运行硬件
* UUID生成器配置:

```xml
<property name="idGenerator">
    <bean class="org.Activiti.engine.impl.persistence.StrongUuidGenerator" />
</property>
```

* 使用UUID id生成器需要以下依赖:

```xml
<dependency>
    <groupId>com.fasterxml.uuid</groupId>
    <artifactId>java-uuid-generator</artifactId>
    <version>3.1.3</version>
</dependency>
```



## 启用安全的BPMN 2.0 xml



* 大多数情况下,BPMN 2.0流程发布到Activiti引擎是在严格的控制下的,但是某些时候也可能需要把比较随意的BPMN 2.0 xml上传到引擎.这种情况,要考虑恶意用户会攻击服务器
* 为了避免上面链接描述的攻击, 可以在引擎配置中设置:*enableSafeBpmnXml*:

```xml
<property name="enableSafeBpmnXml" value="true"/>
```

* 默认这个功能没有开启,这样做的原因是它需要使用[StaxSource](http://download.java.net/jdk7/archive/b123/docs/api/javax/xml/transform/stax/StAXSource.html)类,但是一些平台,如JDK 6,JBoss等等不能用这个类,因为老的xml解析实现,所以不能启用安全BPMN 2.0 xml
* 如果Activiti运行的平台支持这项功能,请打开这个功能



# Eclipse Designer



* Eclipse的Activiti插件,安装略过
* Perferences->Activiti->勾选Create process definitaion image when saving the diagram
* 支持开始没有事件,启动错误事件,定时器启动事件,最后没有一个错误事件
* 支持事件,结束序列流,并行网关,网关,网关,独家包容事件网关
* 支持嵌入式子流程,事件子流程,调用活动,游泳池,车道
* 支持脚本任务,用户任务,服务任务,邮件任务,手动任务,业务规则任务,获得任务
* 支持定时器边界事件,错误边界事件,信号边界事件,定时器捕获事件,信号捕捉事件
* 支持信号投掷项目,和四个Alfresco特定的元素(用户,脚本,邮件任务和开始事件)



## 改变任务类型



* 改变一个任务的类型,只需鼠标悬停在该元素上,然后选择新的任务类型

![](072.png)



## 添加新元素



* 添加一个新元素,只需鼠标悬停某个元素上,然后选择一个新的元素类型

![](073.png)



## 支持Java服务



* 支持对Java 服务任务的Java 类,表达式以及代理表达式的配置.此外,也可以配置字段扩展

![](074.png)



## Pools和Lanes



* 支持pools和lanes,Activiti能根据不同的流程定义读取不同的存储池,这使仅用一个POOL意义重大
* 如果使用多个池,要注意储存池的图序列流动,它可能会在部署AD引擎的过程中发生问题.当然,只要愿意,仍可以添加尽可能多的POOLS,LANES

![](075.png)



## 添加标签



* 可以通过填写名称属性添加标签序列流,可以自己定位标签的位置保存为BPMN 2 XML DI部分信息

![](076.png)



## 子流程



* 支持子流程事件

![](077.png)



## 嵌入式子流程



* 支持扩嵌入式子流程

![](078.png)



## 边界事件



* 支持任务和嵌入子过程上的定时器边界事件.定时器边界事件最大的意义在于可以在用户任务或嵌入子过程上使用

![](079.png)



## 扩展



* 支持附加的Activiti的扩展,如邮件任务,用户任务候选者配置以及脚本任务的配置

![](080.png)



## 监听



* 支持Activiti 执行监听和任务监听,可以给执行监听添加字段扩展

![](081.png)



## 顺序流条件



* 支持顺序流上的条件

![](082.png)





# Activiti Admin



* Activiti Admin,也称之为Activiti控制台
* Activiti控制台是一个web应用程序,当我们从Activiti的官方网站下载Activiti的压缩zip文件时候,Activiti控制台在${Activiti_home}/wars文件夹下面
* 控制台的目的并不是创建一个完善的web应用程序,仅仅是为客户端用户准备的应用程序,但是却能够练习和展示Activiti的功能
* 控制台仅仅只是一个示例Demo,使用了一个内存数据库,可以很容易换成自己的数据库(WEB-INF下的applicationContext.xml)

![](083.png)

* 登录进该控制台会看见四个比较大的图标按钮用于显示主要功能
  * **Tasks**:任务管理功能.如果你是办理人,可以看见运行中流程实例的自己的待办任务,或者你可以拾取组任务.控制台涉及的功能,子任务的工作,不同角色的人等等.控制台也可以允许创建一个独立的任务,该任务并没有关联任何流程实例
  * **Process**:显示部署的流程定义列表,并且可以启动一个新的流程实例
  * **Reporting**:生成报表和显示之前保存历史的结果数据,在版本6中取消
  * **Manage**:当登录的用户具有超级管理员权限才能够看见.用于管理Activiti的流程引擎:管理用于和组,执行和查看停止的jobs,查看数据库和部署新的流程定义



## 流程图



* 控制台包含的功能,使用JS框架自动生成一张流程图.当流程定义XML包含BPMN注入信息时,该流程图才能生成.当流程定义XML中并没有BPMN注入信息但部署时包含一张流程图,该图片也将会被显示

![](084.png)

* 若并不想使用Javascript生成流程图,你可以在ui.properties文件中禁用它

```properties
activiti.ui.jsdiagram = false
```

* 除了在控制台上面显示流程图,控制台也会很容易的包含你想要查看的流程图.下面的URL将会显示流程定义图片,根据留存定义的ID:

```http
http://localhost:8080/activiti-explorer/diagram-viewer/index.html?processDefinitionId=reviewSaledLead:1:36
```

* 也可以显示当前流程实例的状态,通过添加一个processInstanceId的请求参数,如下:

```http
http://localhost:8080/activiti-explorer/diagram-viewer/index.html?processDefinitionId=reviewSaledLead:1:36&processInstanceId=41
```



## 任务



![](085.png)

- **Inbox:**显示登录用户需要办理的所有任务列表
- **My tasks:**显示登录用户任务拥有者的任务列表.当你创建一个独立的任务,你可以自动化操作该任务
- **Queued:**显示不用的组任务列表,并且登录用户在该组中.这里的所有任务都必须先拾取然后才能够完成
- **Involved:**显示登录用户被参与的任务(即不是办理人和任务拥有者)
- **归档**包含已经完成的(历史的)任务



## 启动流程实例



* 在**流程定义**选项卡,允许你查看Activiti流程引擎部署的所有流程定义.可以使用页面顶部右边的按钮启动一个新的流程实例.如果该流程定义有一个启动[表单](http://www.mossle.com/docs/activiti/#forms),那么在启动流程实例之前就先显示表单

![](086.png)



## 我的流程实例



* 在**我的流程** 选项卡,显示当前登录用户未完成的用户任务的所有流程实例

![](087.png)



## 管理



* 该功能只有当登录用户是权限组*admin*中的成员时才会显示.点击 *Manage* 图标,提供以下选项列表 
* **数据库**:在数据库中显示Activiti有关内容.当开发流程或者排除故障等问题的时候是非常有用的

![](088.png)

- **部署**:显示当前流程引擎的部署,并且可以看见部署的内容,包括流程定义,流程图,业务规则等等

![](089.png)

* 当点击*部署*时,也可以上传新的部署.从计算机中选择一个业务文档或者一个BPMN20.XML文件,或者简单的拖拽到指定的区域就可以部署一个新的业务流程

![](090.png)

- **Jobs**:在左边显示当前的作业(定时器等)并且运行手动执行他们.如果作业失败,就会显示所有的异常

![](091.png)

- **用户和组管理用户和组**:7以上版本已废弃.创建,修改和删除用户和组.关联用户和组,这样他们就会有更多的权限或者他们能够看见在任务分配给特地的组

![](092.png)



## 报表



* 控制台附带了一些报表例子且能很轻松的在系统中添加新的报表.报表功能是位于主功能中的**报表**按钮

![](093.png)

* 如果要让报表工作,控制台需要配置历史的级别不能够没有.这默认的配置是满足这一要求的
* 目前,该报表选项卡会显示2个子选项卡:
  * **生成报表**:显示系统中已知的报表列表.允许运行生成的报表
  * **保存报表**:显示之前保存的所有报表列表.注意,这里仅仅显示的是个人保存的报表,并且不能看见其他人保存的报表
* 流程的数据被用于生成报表中的列表和图标,使用流程生成报表数据有几个优势:
  * 该流程能够直接访问Activiti流程引擎的内部,他直接可以使用流程引擎访问数据库
  * 作业执行器能够用于任何其他的流程.这意味着能够异步生存流程或者仅仅异步执行某些步骤.这也意味着可以使用定时器
  * 可以用已知的工具和已知的概念创建一个新的报表.同时,没有新的概念,服务或者应用被需要.部署或者上传一个新的报表与部署一个新的流程是一样的
  * 可以使用BPMN2.0结构.这意味着所有的东西,比如并行网关,可以实现基于数据或用户请求输入生成分支
* 生成报表数据的流程定义需要把activiti-report设置为分类,这样就能在Explorer的报表列表中显示出来
* 能够看到报表的唯一要求是,流程会创建一个名为**reportData**的流程变量.这个变量必须是json对象的二进制数组,必须保存到Activiti的历史表中 (所以要求引擎必须启用历史功能),可以在后面报表保存时获取

![](094.png)



### 报告数据JSON



* 报表流程必须生成一个变量*reportData*,这是一个要展现给用户的JSON数据. 这个json看起来像这样:

```json
{
    "title": "My Report",
    "datasets": [
        {
            "type" : "lineChart",
            "description" : "My first chart",
            "xaxis" : "Year"
            "yaxis" : "Total sales"
            "data" :
            {
            "2010" : 50,
            "2011" : 33,
            "2012" : 17,
            "2013" : 87,
        }
        }
    ]
}
```

* json数据会在Explorer中获取,并用来生成图表或列表. json的元素为:
  * **title**:这个报表的标题
  * **datasets**:是数据集的数组,对应报表中不同的图表和列表
  * **type**:每个数据集都有一个类型,这个类型用来决定如何渲染数据.支持的值:pieChart,lineChart,barChart和list
  * **description**:每个图表可以 在报表中显示一个可选的描述
  * **x 和yaxis**:只对 *lineChart*类型起作用.这个可选参数可以修改图表坐标系的名称
  * **data**:这是实际的数据. 数据是一个key-value格式的json对象



### 实例流程



* 下面的例子演示了一个流程实例总览报表.流程本身只包含一个脚本任务使用js生成json数据集.虽然所有Explorer中的例子都使用js,但也可以使用java服务任务.执行流程最后的结果就是*reportData*变量保存着数据
* 下面的例子只能运行在JDK 7+环境中,因为使用了JS引擎

```xml
<?xml version="1.0" encoding="UTF-8"?>
<definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:activiti="http://activiti.org/bpmn"
             xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI" xmlns:omgdc="http://www.omg.org/spec/DD/20100524/DC"
             xmlns:omgdi="http://www.omg.org/spec/DD/20100524/DI" typeLanguage="http://www.w3.org/2001/XMLSchema"
             expressionLanguage="http://www.w3.org/1999/XPath"
             targetNamespace="activiti-report">
    <process id="process-instance-overview-report" name="Process Instance Overview" isExecutable="true">
        <startEvent id="startevent1" name="Start" />
        <sequenceFlow id="flow1" sourceRef="startevent1" targetRef="generateDataset" />
        <scriptTask id="generateDataset" name="Execute script" scriptFormat="JavaScript" activiti:autoStoreVariables="false">
            <script><![CDATA[
               importPackage(java.sql);
               importPackage(java.lang);
               importPackage(org.activiti.explorer.reporting);

               var result = ReportingUtil.executeSelectSqlQuery("SELECT PD.NAME_, PD.VERSION_ , count(*) FROM ACT_HI_PROCINST PI inner join ACT_RE_PROCDEF PD on PI.PROC_DEF_ID_ = PD.ID_ group by PROC_DEF_ID_");

               var reportData = {};
               reportData.datasets = [];

               var dataset = {};
               dataset.type = "pieChart";
               dataset.description = "Process instance overview (" + new java.util.Date() + ")";
               dataset.data = {};

               while (result.next()) { // process results one row at a time
                 var name = result.getString(1);
                 var version = result.getLong(2)
                 var count = result.getLong(3);
                 dataset.data[name + " (v" + version + ")"] = count;
               }
               reportData.datasets.push(dataset);

               execution.setVariable("reportData", new java.lang.String(JSON.stringify(reportData)).getBytes("UTF-8"));
          ]]></script>
        </scriptTask>
        <sequenceFlow id="flow3" sourceRef="generateDataset" targetRef="theEnd" />
        <endEvent id="theEnd" />
    </process>
</definitions>
```

* 除了流程xml顶部的标准xml,主要区别是*targetNamespace*设置为**activiti-report**, 把分类设置为与部署的流程定义一样的名称
* 脚本的第一行只是进行一些导入,避免每次使用时,都要写包名
* 第一个有意义的代码是使用*ReportingUtil*读取activiti数据库,返回结果是*JDBC结果集*.查询语句下面,JS创建了使用的json
* 最后一行脚本有一点儿奇怪.首先需要把json对象转换成字符串,使用JS函数*JSON.stringify()*.字符串需要保存为二进制数组类型的变量.这是一个技术问题:二进制数组的大小是无限的,但是字符串的长度有限制.这就是为什么JS字符串必须转换成一个java字符串,以获得转换成二进制的功能
* 兼容JDK 6及更高版本的流程有一些区别,原生json无法使用,因此提供了帮助类(*ReportData* 和 *Dataset*)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:activiti="http://activiti.org/bpmn"
             xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI" xmlns:omgdc="http://www.omg.org/spec/DD/20100524/DC"
             xmlns:omgdi="http://www.omg.org/spec/DD/20100524/DI" typeLanguage="http://www.w3.org/2001/XMLSchema"
             expressionLanguage="http://www.w3.org/1999/XPath"
             targetNamespace="activiti-report">
    <process id="process-instance-overview-report" name="Process Instance Overview" isExecutable="true">
        <startEvent id="startevent1" name="Start" />
        <sequenceFlow id="flow1" sourceRef="startevent1" targetRef="generateDataset" />
        <scriptTask id="generateDataset" name="Execute script" scriptFormat="js" activiti:autoStoreVariables="false">
            <script><![CDATA[
               importPackage(java.sql);
               importPackage(java.lang);
               importPackage(org.activiti.explorer.reporting);

               var result = ReportingUtil.executeSelectSqlQuery("SELECT PD.NAME_, PD.VERSION_ , count(*) FROM ACT_HI_PROCINST PI inner join ACT_RE_PROCDEF PD on PI.PROC_DEF_ID_ = PD.ID_ group by PROC_DEF_ID_");
               var reportData = new ReportData;
               var dataset = reportData.newDataset();
               dataset.type = "pieChart";
               dataset.description = "Process instance overview (" + new java.util.Date() + ")"
               while (result.next()) { // process results one row at a time
                 var name = result.getString(1);
                 var version = result.getLong(2);
                 var count = result.getLong(3);
                 dataset.add(name + " (v" + version + ")", count);
               }
               execution.setVariable("reportData", reportData.toBytes());
          ]]></script>
        </scriptTask>
        <sequenceFlow id="flow3" sourceRef="generateDataset" targetRef="theEnd" />
        <endEvent id="theEnd" />
    </process>
</definitions>
```



### 报告开始表单



* 因为报表是使用普通流程来生成的,所以表单功能也可以使用.直接在开始事件里加一个开始表单,Explorer就会在生成报表之前 把它展示给用户

```xml
<startEvent id="startevent1" name="Start">
    <extensionElements>
        <activiti:formProperty id="processDefinition" name="Select process definition" type="processDefinition" required="true" />
        <activiti:formProperty id="chartType" name="Chart type" type="enum" required="true">
            <activiti:value id="pieChart" name="Pie chart" />
            <activiti:value id="barChart" name="Bar chart" />
        </activiti:formProperty>
    </extensionElements>
</startEvent>
```

* 这会为用户渲染一个普通的表单:

![](095.png)

* 表单属性会在启动流程时提交,然后它们就可以像普通流程变量一样使用,脚本中可以使用它们来生成数据

```js
 var processDefinition = execution.getVariable("processDefinition");
```



### 流程例子



对于默认的,控制台包含4个报表例子:

- **Employee productivity**:员工的工作效率.报表演示使用折线图和开始表单,报表的脚本也比其他例子要复杂,因为数据会在脚本中先进行解释,再保存到报表数据中
- **Helpdesk**:一线与升级.使用饼图进行展示,结合两个不同的数据库查询结果
- **Process instance overview**:流程实例总览,使用多个数据集的报表实例.报表包含使用相同数据的饼图和列表视图,展示多种数据集可以用来在一个页面中生成不同图表
- **Task duration**:任务持续时间,另一个使用开始表单的例子,会使用对应的变量来动态生成SQL查询语句



## 修改数据库



* 如果修改控制台例子所用的数据库,改变属性文件`apps/apache-tomcat-8.x/webapps/activiti-explorer/WEB-INF/classes/db.properties`.同样,在类路径下放上合适的数据库驱动(Tomcat 共享类库或者在 `apps/apache-tomcat-8.x/webapps/activiti-explorer/WEB-INF/lib/`中



# Activiti Modeler



* Activiti Modeler是一个BPMN web建模组件,它是Activiti Explorer应用的一部分
* 当运行Activiti Explorer使用默认配置时,模型工作台中会有一个示例流程

![](096.png)



## 编辑模型



* 点击模型工作区的编辑按钮,会打开Modeler.屏幕左侧是BPMN元素工具面板,也可以使用Activiti的扩展组件,可以在需要时把新元素拖拽到画布中,屏幕右侧是选中额元素的和苏醒
* 例子中选中了一个用户任务,可以填写用户任务的属性,比如分配,表单属性和持续时间.点击屏幕右上方的关闭按钮就可以返回Activiti Exporer

![](097.png)



## 导入模型



* 也可以把模型导入到模型工作台中,然后就可以在Activiti Modeler中进行编辑.点击导入按钮,选择.bpmn或.bpmn20.xml文件,BPMN XML文件必须包含BPMN DI(坐标)信息

![](098.png)



## 流程定义转成可编辑模型



* 发布的流程定义可以转换成模型,然后就可以在Activiti Modeler中编辑了,注意流程定义必须包含BPMN DI(坐标)信息

![](099.png)



## 模型导出



* 模型工作区中的模型可以导出成BPMN XML文件.选择模型操作选项中的导出选项

![](100.png)



## 模型部署到Activiti



* 在模型设置好所有运行所需的属性之后,它就可以发布到Activiti引擎里. 选择模型操作选项中的发布选项

![](101.png)



# 集成CDI



* Activiti-cdi模块提供Activiti的可配置型和cdi扩展,需要手动添加依赖,7以上版本已废弃

- 支持@BusinessProcessScoped,绑定到流程实例的cdi bean
- 流程为cdi bean支持自定义EL处理器
- 使用注解为流程实例提供声明式控制
- Activiti可以挂接在cdi事件总线上
- 支持Java EE和Java SE,支持Spring
- 支持单元测试
- 添加依赖:

```xml
<dependency>
    <groupId>org.activiti</groupId>
    <artifactId>activiti-cdi</artifactId>
    <version>6.0.0</version>
</dependency>
```



## 设置Activiti-cdi



### 查找流程引擎



* cdi需要访问ProcessEngine,为实现此功能,使用`org.activiti.cdi.spi.ProcessEngineLookup`在运行期进行查找
* cdi模块使用默认的名为`org.activiti.cdi.impl.LocalProcessEngineLookup`的实现,它使用`ProcessEngines`这个工具类来查找ProcessEngine
* 默认配置下,使用`ProcessEngines#NAME_DEFAULT`来查找ProcessEngine.这个类可能是使用了自定义名称的子类.需要把activiti.cfg.xml`放在classpath下
* Activiti cdi使用java.util.ServiceLoader处理`org.activiti.cdi.spi.ProcessEngineLookup`的实例.接口实现需写在`META-INF/services/org.activiti.cdi.spi.ProcessEngineLookup`的文本文件中,写全类名
* 如果没有提供自定义的`org.Activiti.cdi.spi.ProcessEngineLookup`实现,Activiti会使用默认的`LocalProcessEngineLookup`实现.这时需要将activiti.cfg.xml放到classpath下



### 配置ProcessEngine



* 实际的配置依赖于选用的ProcessEngineLookup策略.主要结合LocalProcessEngineLookup讨论可用的配置, 这需要在classpath下提供一个spring的activiti.cfg.xml
* Activiti提供了不同的ProcessEngineConfiguration实现,主要是依赖实际使用的事务管理策略
* activiti-cdi模块对事务的要求不严格,意味着任何事务管理策略都可以使用.cdi模块提供两种自定义ProcessEngineConfiguration实现:
  * `org.Activiti.cdi.CdiJtaProcessEngineConfiguration`:Activiti的JtaProcessEngineConfiguration的子类,用于在Activiti使用JTA管理的事务环境
  * `org.Activiti.cdi.CdiStandaloneProcessEngineConfiguration`:Activiti的StandaloneProcessEngineConfiguration的子类,用于在Activiti使用简单JDBC事务环境
* JBoss 7下的Activiti.cfg.xml文件的例子:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <!-- lookup the JTA-Transaction manager -->
    <bean id="transactionManager" class="org.springframework.jndi.JndiObjectFactoryBean">
        <property name="jndiName" value="java:jboss/TransactionManager"></property>
        <property name="resourceRef" value="true" />
    </bean>

    <!-- process engine configuration -->
    <bean id="processEngineConfiguration"
          class="org.Activiti.cdi.CdiJtaProcessEngineConfiguration">
        <!-- lookup the default Jboss datasource -->
        <property name="dataSourceJndiName" value="java:jboss/datasources/ExampleDS" />
        <property name="databaseType" value="h2" />
        <property name="transactionManager" ref="transactionManager" />
        <!-- using externally managed transactions -->
        <property name="transactionsExternallyManaged" value="true" />
        <property name="databaseSchemaUpdate" value="true" />
    </bean>
</beans>
```

* Glassfish 3.1.1下的例子,假设已经配置了名为jdbc/Activiti的datasource:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <!-- lookup the JTA-Transaction manager -->
    <bean id="transactionManager" class="org.springframework.jndi.JndiObjectFactoryBean">
        <property name="jndiName" value="java:appserver/TransactionManager"></property>
        <property name="resourceRef" value="true" />
    </bean>

    <!-- process engine configuration -->
    <bean id="processEngineConfiguration"
          class="org.Activiti.cdi.CdiJtaProcessEngineConfiguration">
        <property name="dataSourceJndiName" value="jdbc/Activiti" />
        <property name="transactionManager" ref="transactionManager" />
        <!-- using externally managed transactions -->
        <property name="transactionsExternallyManaged" value="true" />
        <property name="databaseSchemaUpdate" value="true" />
    </bean>
</beans>
```

* 上面的额配置需要spring-context模块
* 在Java SE环境下的配置和创建ProcessEngine章节中一样,使用CdiStandaloneProcessEngineConfiguration替换StandaloneProcessEngineConfiguration



### 发布流程



* 可以使用标准的activiti-api发布流程(`RepositoryService`).另外,activiti-cdi提供自动发布 classpath下`processes.xml`中列出的流程的方式.下面是一个processes.xml文件的例子:

```xml
<?xml version="1.0" encoding="utf-8" ?>
<!-- list the processes to be deployed -->
<processes>
    <process resource="diagrams/myProcess.bpmn20.xml" />
    <process resource="diagrams/myOtherProcess.bpmn20.xml" />
</processes>
```



## 基于CDI环境的流程执行



* BPMN业务流程通常是一个长时间运行的操作,包含了用户和系统任务的操作. 运行过程中,流程会分成多个单独的工作单元,由用户和应用逻辑执行
* 在activiti-cdi中,流程实例可以分配到cdi环境中,关联展现成一个工作单元.如果工作单元太复杂,比如如果实现的用户任务是不同形式的复杂顺序,可以在这个操作中保持non-process-scoped状态. 默认配置下,流程实例分配到broadest激活环境,就会启动交互,如果交互环境没有激活,就会返回到请求中



### 与流程实例进行关联交互



* 处理@BusinessProcessScoped,或注入流程变量时,我们实现了激活的cdi环境与流程实例的关联.activiti-cdi提供了`org.Activiti.cdi.BusinessProcess`来控制关联,特别是:
  * `startProcessBy*(...)`:对应Activiti的`RuntimeService`中的相关方法,允许启动和随后向关联的业务流程
  * `resumeProcessById(processInstanceId)`:允许通过提供的id来关联流程实例
  * `resumeTaskById(taskId)`:允许通过提供的id来关联任务,扩展情况下,也关联相应的流程实例
* 一个工作单元完成后,`completeTask()`可以调用来解除流程实例和会话/请求的关联.这会通知activiti当前任务已经完成,并让流程实例继续执行
* `BusinessProcess`是`@Named` bean,意思是导出的方法可以通过表达式语言调用,比如在JSF页面中
* 下面的JSF 2代码启动一个新的交互,把它分配给一个用户任务实例,id作为一个请求参数传递,比如`pageName.jsf?taskId=XX`

```jsp
<f:metadata>
    <f:viewParam name="taskId" />
    <f:event type="preRenderView" listener="#{businessProcess.startTask(taskId, true)}" />
</f:metadata>
```



### 声明式流程控制



* activiti-cdi允许通过注解声明启动流程实例和完成任务
* `org.activiti.cdi.annotation.StartProcess`注解允许通过key或name启动流程实例.流程实例会在注解的方法返回*之后*启动.比如:

```java
@StartProcess("authorizeBusinessTripRequest")
public String submitRequest(BusinessTripRequest request) {
    // do some work
    return "success";
}
```

* 根据activiti的配置,注解方法的代码和启动流程实例 会在同一个事务中执行
* `org.Activiti.cdi.annotation.CompleteTask`事务的使用方式相同:

```java
@CompleteTask(endConversation=false)
public String authorizeBusinessTrip() {
    // do some work
    return "success";
}
```

* `CompleteTask`注解可以结束当前会话.默认行为会在Activiti返回后结束会话.可以禁用结束会话的功能, 实例可以参考上述代码



### 在流程中引用bean



* activiti-cdi使用自定义解析器把CDI bean暴露到Activiti El中.这就可以在流程中引用这些bean:

```xml
<userTask id="authorizeBusinessTrip" name="Authorize Business Trip"
                       Activiti:assignee="#{authorizingManager.account.username}" />
```

* authorizingManager可以是生产者方法提供的bean:

```java
@Inject
@ProcessVariable
Object businessTripRequesterUsername;

@Produces
@Named
public Employee authorizingManager() {
    TypedQuery<Employee> query = entityManager.createQuery("SELECT e FROM Employee e WHERE e.account.username='"+ businessTripRequesterUsername + "'", Employee.class);
    Employee employee = query.getSingleResult();
    return employee.getManager();
}
```

* 可以使用同样的方法在服务任务中调用EJB的业务方法, 使用`Activiti:expression="myEjb.method()"`扩展,这要求在`MyEjb`类中使用`@Named`注解



### BusinessProcessScoped



* 使用activiti-cdi,bean的生命周期可以绑定到流程实例上
* 为了扩展,可以提供一个自定义的环境实现,命名为BusinessProcessContext.BusinessProcessScoped bean的实例会作为流程变量保存到当前流程实例中
* *BusinessProcessScoped bean需要是**PassivationCapable**(比如序列化).* 下面是例子:

```java
@Named
@BusinessProcessScoped
public class BusinessTripRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String startDate;
    private String endDate;
    // ...
}
```

* 有时我们需要使用流程作用域bean,没有与流程实例关联,比如启动流程之前
  * 如果当前流程实例没有激活,BusinessProcessScoped bean实例会暂时保存在局部作用域里
  * 如果作用域后来与业务流程实例关联了,bean实例会刷新到流程实例里



### 注入流程变量



* 流程变量可以实现用于注入,activiti-CDI支持以下方式:
  * `@BusinessProcessScoped`使用`@Inject属性名`实现类型安全的注入
  * 使用`@ProcessVariable(name?)`修饰符实现对类型不安全的流程变量的注入

```java
@Inject 
@ProcessVariable 
Object accountNumber;

@Inject 
@ProcessVariable("accountNumber") 
Object account;
```

* 为了通过EL引用流程变量,可以简单实用如下方式:
  * `@Named @BusinessProcessScoped` beans可以直接引用
  * 其他流程变量可以使用`ProcessVariables` bean来使用

```java
#{processVariables['accountNumber']}
```



### 接收流程事件



* Activiti可以挂在CDI的事件总线上.这样就可以使用标准CDI事件机制来监听流程事件,为了启用Activiti的CDI事件支持,需要在配置中启用对应的解析监听器:

```xml
<property name="postBpmnParseHandlers">
    <list>
        <bean class="org.Activiti.cdi.impl.event.CdiEventSupportBpmnParseHandler" />
    </list>
</property>
```

* 现在Activiti已经配置成使用CDI事件总线发布事件.下面给出了如何在CDI bean中处理事件的方式.在CDI,可以使用`@Observes`注解声明特定的事件监听器.事件监听是类型安全的.流程事件类型是`org.Activiti.cdi.BusinessProcessEvent`.下面是一个简单事件监听方法的例子:

```java
public void onProcessEvent(@Observes BusinessProcessEvent businessProcessEvent) {
    // handle event
}
```

* 监听器可以监听所有事件.如果想限制监听器接收的事件类型,我们可以添加修饰注解:
  * `@BusinessProcess`:限制指定流程定义的事件.比如:`@Observes @BusinessProcess("billingProcess") BusinessProcessEvent evt;`
  * `@StartActivity`:限制指定环节的事件.比如:`@Observes @StartActivity("shipGoods") BusinessProcessEvent evt;` 在进入id为"shipGoods"的环节时会触发
  * `@EndActivity`:限制指定环节的事件.比如:`@Observes @EndActivity("shipGoods") BusinessProcessEvent evt;`.在离开id为shipGoods的环节时会触发     
  * `@TakeTransition`:限制指定连线的事件
* 修饰命名可以自由组合.比如,为了接收shipmentProcess流程中所有离开shipGoods环节的事件,可以编写如下监听方法:

```java
public void beforeShippingGoods(@Observes @BusinessProcess("shippingProcess") @EndActivity("shipGoods") BusinessProcessEvent evt) {
    // handle event
}
```

* 默认配置下,事件监听器是同步调用,并在同一个事务环境中.CDI事务性监听器(只在JavaEE / EJB环境下有效),可以控制监听器什么时候处理事件,比如,可以保证监听器只在事件中的事务成功之后才处理:

```java
public void onShipmentSuceeded(@Observes(during=TransactionPhase.AFTER_SUCCESS) @BusinessProcess("shippingProcess") @EndActivity("shipGoods") BusinessProcessEvent evt) {
    // send email to customer.
}
```



### 更多功能



- 流程引擎和服务都可以注入:`@Inject ProcessEngine, RepositoryService, TaskService`, ... 
- 当前流程实例和任务可以注入:`@Inject ProcessInstance, Task`
- 当前业务标识可以注入:`@Inject @BusinessKey String businessKey`
- 当前流程实例id可以注入 :`@Inject @ProcessInstanceId String pid`



# 集成LDAP



* Activit7以下版本中才会该功能,非重点
* LDAP是一种轻量级目录访问协议,主要用来保存描述性的,基于属性的详细信息,主要用于统一认证服务



## 用法



* 添加依赖

```xml
<dependency>
    <groupId>org.Activiti</groupId>
    <artifactId>Activiti-ldap</artifactId>
    <version>latest.version</version>
</dependency>
```



## 用例



* 集成LDAP目前有两大用例:
  * 通过IdentityService进行认证.比如使用Activiti Explorer通过LDAP登录
  * 获得用户的组.这在查询用户可以看到哪些任务时非常重要(比如任务分配给一个候选组)



## 配置



* 集成LDAP需要通过向流程引擎中的`configurators`注入`org.Activiti.ldap.LDAPConfigurator`的实例来实现.这个类是高度可扩展的:如果默认的实现不符合用例的话,可以很容易的重写方法,很多依赖的bean都是可插拔的
* 一个实例配置

```xml
<bean id="processEngineConfiguration" class="...SomeProcessEngineConfigurationClass">
    ...
    <property name="configurators">
        <list>
            <bean class="org.Activiti.ldap.LDAPConfigurator">
                <!-- Server connection params -->
                <property name="server" value="ldap://localhost" />
                <property name="port" value="33389" />
                <property name="user" value="uid=admin, ou=users, o=Activiti" />
                <property name="password" value="pass" />
                <!-- Query params -->
                <property name="baseDn" value="o=Activiti" />
                <property name="queryUserByUserId" value="(&(objectClass=inetOrgPerson)(uid={0}))" />
                <property name="queryUserByFullNameLike" value="(&(objectClass=inetOrgPerson)(|({0}=*{1}*)({2}=*{3}*)))" />
                <property name="queryGroupsForUser" value="(&(objectClass=groupOfUniqueNames)(uniqueMember={0}))" />
                <!-- Attribute config -->
                <property name="userIdAttribute" value="uid" />
                <property name="userFirstNameAttribute" value="cn" />
                <property name="userLastNameAttribute" value="sn" />
                <property name="groupIdAttribute" value="cn" />
                <property name="groupNameAttribute" value="cn" />
            </bean>
        </list>
    </property>
</bean>
```



## 属性



* `org.Activiti.ldap.LDAPConfigurator`可以配置的属性:
  * server:LDAP服务器地址,如ldap://localhost:33389
  * port:LDAP运行的端口
  * user:连接LDAP使用的账号
  * password:连接LDAP使用的密码
  * initialContextFactory:连接LDAP的InitialContextFactory名称,默认为com.sun.jndi.ldap.LdapCtxFactory
  * securityAuthentication:连接LDAP时设置的'java.naming.security.authentication'属性值,默认simple
  * customConnectionParameters:可以设置那些没有对应setter的连接参数.参考http://docs.oracle.com/javase/tutorial/jndi/ldap/jndi.html中的自定义属性,这些属性用来配置连接池,特定的安全设置等等.所有提供的参数都会用来创建LDAP连接,是一个Map<String,String>
  * baseDn:搜索用户和组的基显著名称(DN)
  * userBaseDn:搜索用户基于的distinguished name(DN).如果没有提供,会使用baseDn
  * groupBaseDn:搜索群组基于的distinguished name(DN).如果没有提供,会使用baseDn
  * searchTimeLimit:搜索LDAP的超时时间,单位毫秒,默认一小时
  * queryUserByUserId:使用用户id搜索用户的查询语句.如(&(objectClass=inetOrgPerson)(uid={0})).  这里,LDAP中所有包含inetOrgPerson类的匹配uid属性的值都会返回.如例子中所示,{0}会被用户id替换.如果只设置一个查询无法满足特定的LDAP设置,可以选择使用LDAPQueryBuilder,这样就会提供比单纯使用查询增加更多功能
  * queryUserByFullNameLike:使用全名搜索用户的查询语句.如(& (objectClass=inetOrgPerson)(\|({0}=*{1}*)({2}=*{3}*))).这里,LDAP中所有包含inetOrgPerson类的匹配first name和last name的值都会返回.注意{0}会替换为firstNameAttribute,{1}和{3}是搜索内容,{2}是lastNameAttribute.如果只设置一个查询无法满足特定的LDAP设置,可以选择使用LDAPQueryBuilder,这样就会提供比单纯使用查询增加更多功能
  * queryGroupsForUser:使用搜索指定用户的组的查询语句.如(&(objectClass=groupOfUniqueNames)(uniqueMember={0})).这里,LDAP中所有包含groupOfUniqueNames类的提供的DN(匹配用户的DN)是uniqueMember的记录都会返回.像例子中演示的那样,{0}会替换为用户id.如果只设置一个查询无法满足特定的LDAP设置,可以选择使用LDAPQueryBuilder,这样就会提供比单纯使用查询增加更多功能
  * userIdAttribute:匹配用户id的属性名.这个属性用来在查找用户对象时关联LDAP对象与Activiti用户对象之间的关系
  * userFirstNameAttribute:匹配first name的属性名.这个属性用来在查找用户对象时关联LDAP对象与Activiti用户对象之间的关系
  * userLastNameAttribute:匹配last name的属性名.这个属性用来在查找用户对象时关联LDAP对象与Activiti用户对象之间的关系
  * groupIdAttribute:匹配组id的属性名.这个属性用来在查找组对象时关联LDAP对象与Activiti组对象之间的关系
  * groupNameAttribute:匹配组名的属性名.这个属性用来在查找组对象时关联LDAP对象与Activiti组对象之间的关系
  * groupTypeAttribute:匹配组名的属性类型.这个属性用来在查找组对象时关联LDAP对象与Activiti组对象之间的关系 

* 下列属性用在希望修改默认行为 或修改组缓存的情况
  *  ldapUserManagerFactory:设置LDAPUserManagerFactory的自定义实例,如果默认实现不满足需求.LDAPUserManagerFactory的实例 
  *   ldapGroupManagerFactory:设置LDAPGroupManagerFactory的自定义实例,如果默认实现不满足需求.LDAPGroupManagerFactory的实例  
  *   ldapMemberShipManagerFactory:设置LDAPMembershipManagerFactory的自定义实例,如果默认实现不满足需求.不常用,因为正常情况下LDAP会自己管理关联关系.LDAPMembershipManagerFactory的实例  
  *  ldapQueryBuilder:设置自定义查询构造器,如果默认实现不满足需求.LDAPQueryBuilder实例用在LDAPUserManager和LDAPGroupManager中,执行对LDAP的查询.默认实现会使用配置的queryGroupsForUser和queryUserById属性.org.Activiti.ldap.LDAPQueryBuilder的实例  
  *  groupCacheSize:组缓存的大小.这是一个LRU缓存,用来缓存用户的组,可以避免每次查询用户的组时,都要访问LDAP.如果值小于0,就不会创建缓存.默认为-1,所以不会进行缓存
  *   groupCacheExpirationTime:设置组缓存的过期时间,单位为毫秒.当获取特定用户的组时,并且组缓存也启用了,组会保存到缓存中,并使用这个属性设置的时间.例如,当组在00:00被获取,过期时间为30分钟,那么所有在00:30之后进行的查询都不会使用缓存,而是再次去LDAP查询.因此所有在00:00 - 00:30 进行的查询都会使用缓存.默认1小时
* 在使用活动目录(AD)时,Activiti论坛中的人们反映对于活动目录(AD),InitialDirContext需要设置为Context.REFERRAL,可以通过customConnectionParameters传递



## Explorer集成LDAP



- 将上面的LDAP配置添加到`Activiti-standalone-context.xml`中
- 把Activiti-ldap jar放到WEB-INF/lib目录下
- 删除`demoDataGenerator` bean,因为它会尝试插入数据(集成LDAP不允许这么做)
- 将下面的配置添加到`Activiti-ui.context`的`explorerApp` bean中:

```xml
<property name="adminGroups">
    <list>
        <value>admin</value>
    </list>
</property>
<property name="userGroups">
    <list>
        <value>user</value>
    </list>
</property>
```

* 需要用到的数据是组的id(通过`groupIdAttribute`配置).上述配置会让admin组下的所有用户都成为Activiti Explorer的管理员,用户组也一样.所有不匹配的组都会当做分配组,这样任务就可以分配给他们