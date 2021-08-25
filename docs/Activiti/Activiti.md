# Activiti



# 概述

* 流程引擎



# 配置



## ProcessEngine

* Activiti流程引擎的配置文件是名为`activiti.cfg.xml`的XML文件,和[Spring方式创建流程引擎](http://www.mossle.com/docs/activiti/#springintegration) **不**一样
* 获得`ProcessEngine`最简单的办法是使用`org.activiti.engine.ProcessEngines`类

```java
ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
```

* 它会在classpath下搜索`activiti.cfg.xml`,并基于这个文件中的配置构建引擎

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans   http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="processEngineConfiguration" class="org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration">
        <property name="jdbcUrl" value="jdbc:h2:mem:activiti;DB_CLOSE_DELAY=1000" />
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

* `activiti.cfg.xml`必须包含一个id为`processEngineConfiguration`的bean

```xml
 <bean id="processEngineConfiguration" class="org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration">
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
  <property name="url" value="jdbc:mysql://localhost:3306/activiti" />
  <property name="username" value="activiti" />
  <property name="password" value="activiti" />
  <property name="defaultAutoCommit" value="false" />
</bean>
<bean id="processEngineConfiguration" class="org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration">
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

- 把activiti-engine的jar放到classpath下
- 添加对应的数据库驱动
- 把Activiti配置文件(activiti.cfg.xml*)放到classpath下,指向指定数据库
- 执行*DbSchemaCreate*类的main方法
- 只有数据库管理员可以执行DDL语句.SQL DDL语句可以从Activiti下载页或Activiti发布目录里找到,在`database`子目录下.
- 脚本包含在引擎的jar中(*activiti-engine-x.jar*),在*org/activiti/db/create*包下



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
* 要升级,要把下面的配置 放到activiti.cfg.xml配置文件里

```xml
<beans ... >
  <bean id="processEngineConfiguration" class="org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration">
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

* 默认`activiti.cfg.xml`和你自己的Spring配置文件中所有bean都可以在表达式和脚本中使用
* 如果想限制配置文件中的bean的可见性,可以配置流程引擎配置的beans配置
* `ProcessEngineConfiguration`的beans是一个map.当指定了这个参数,只有包含这个map中的bean可以在表达式和脚本中使用.通过在map中指定的名称来决定暴露的bean



## 配置部署缓存

* 所有流程定义都被缓存,避免每次使用前都要访问数据库,因为流程定义数据是不会改变的
* 默认不会限制流程定义缓存,如果想限制流程定义缓存,可以添加如下配置

```xml
<property name="processDefinitionCacheLimit" value="10" />
```

* 这个配置会把默认的hashmap缓存替换成LRU缓存,来提供限制.这个配置的最佳值跟流程定义的总数有关, 实际使用中会具体使用多少流程定义也有关
* 可以注入自己的缓存实现.必须实现org.activiti.engine.impl.persistence.deploy.DeploymentCache

```xml
<property name="processDefinitionCache">
  <bean class="org.activiti.MyCache" />
</property>
```

* 有一个类似的配置叫`knowledgeBaseCacheLimit`和`knowledgeBaseCache`,它们是配置规则缓存的,只有流程中使用规则任务时才会用到



## 日志



* 默认activiti-engine依赖中没有提供SLF4J绑定的jar,需要根据实际需要使用日志框架
* 如果没有添加任何实现jar,SLF4J会使用NOP-logger,不使用任何日志,不会发出警告,而且什么日志都不会记录
* activiti-explorer和activiti-rest应用都使用了Log4j绑定
* 如果容器classpath中存在commons-logging,为了把spring日志转发给SLF4J,需要使用桥接.如果容器提供了commons-logging实现,请参考http://www.slf4j.org/codes.html#release来确保稳定性



## 映射诊断上下文

* activiti支持slf4j的MDC功能,如下的基础信息会传递到日志中记录:
  * 流程定义Id标记为mdcProcessDefinitionID
  * 流程实例Id标记为mdcProcessInstanceID
  * 分支Id标记为mdcexecutionId
* 默认不会记录这些信息.可以配置日志使用期望的格式来显示它们,扩展通常的日志信息

```properties
log4j.appender.consoleAppender.layout.ConversionPattern =ProcessDefinitionId=%X{mdcProcessDefinitionID}
executionId=%X{mdcExecutionId} mdcProcessInstanceID=%X{mdcProcessInstanceID} mdcBusinessKey=%X{mdcBusinessKey} %m%n"

```

* 当系统进行高风险任务,日志必须严格检查时,这个功能就非常有用,比如要使用日志分析的情况



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
        <property name="url" value="jdbc:h2:mem:activiti;DB_CLOSE_DELAY=1000" />
        <property name="username" value="sa" />
        <property name="password" value="" />
    </bean>

    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource" />
    </bean>

    <bean id="processEngineConfiguration" class="org.activiti.spring.SpringProcessEngineConfiguration">
        <property name="dataSource" ref="dataSource" />
        <property name="transactionManager" ref="transactionManager" />
        <property name="databaseSchemaUpdate" value="true" />
        <property name="jobExecutorActivate" value="false" />
    </bean>

    <bean id="processEngine" class="org.activiti.spring.ProcessEngineFactoryBean">
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
  <bean id="userBean" class="org.activiti.spring.test.UserBean">
    <property name="runtimeService" ref="runtimeService" />
  </bean>
  <bean id="printer" class="org.activiti.spring.test.Printer" />
</beans>
```

* 使用任意的一种Spring创建应用上下文的方式创建其Spring应用上下文.在这个例子中你可以使用类路径下面的XML资源来配置我们的Spring应用上下文:

```java
ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("org/activiti/examples/spring/SpringTransactionIntegrationTest-context.xml");
```

* 此时可以得到Activiti的服务beans并且调用该服务上面的方法
* ProcessEngineFactoryBean将会对该服务添加一些额外的拦截器,在Activiti服务上面的方法使用的是 Propagation.REQUIRED事物语义.可以使用repositoryService去部署一个流程:

```java
RepositoryService repositoryService = (RepositoryService) applicationContext.getBean("repositoryService");
String deploymentId = repositoryService.createDeployment()
    .addClasspathResource("org/activiti/spring/test/hello.bpmn20.xml")
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

* 当使用ProcessEngineFactoryBean时,默认情况下,在BPMN流程中的所有[表达式](http://www.mossle.com/docs/activiti/#apiExpressions)都将会看见所有的Spring beans.它可以限制你在表达式中暴露出的beans或者甚至可以在你的配置中使用一个Map不暴露任何beans
* 下面的例子暴露了一个单例bean(printer),可以把printer当作关键字使用
* 想要不暴露任何beans,仅仅只需要在SpringProcessEngineConfiguration中传递一个空的list作为beans的属性
* 当不设置beans的属性时,在应用上下文中Spring beans都是可以使用的

```xml
<bean id="processEngineConfiguration" class="org.activiti.spring.SpringProcessEngineConfiguration">
    ...
    <property name="beans">
        <map>
            <entry key="printer" value-ref="printer" />
        </map>
    </property>
</bean>

<bean id="printer" class="org.activiti.examples.spring.Printer" />

```

* 现在暴露出来的beans就可以在表达式中使用.例如,在SpringTransactionIntegrationTest中的 `hello.bpmn20.xml`展示的是如何使用UEL方法表达式去调用Spring bean的方法 

```xml
<definitions id="definitions" ...>
    <process id="helloProcess">
        <startEvent id="start" />
        <sequenceFlow id="flow1" sourceRef="start" targetRef="print" />
        <serviceTask id="print" activiti:expression="#{printer.printMessage()}" />
        <sequenceFlow id="flow2" sourceRef="print" targetRef="end" />
        <endEvent id="end" />
    </process>
</definitions>
```

* Spring bean的配置

```xml
<beans ...>
  <bean id="printer" class="org.activiti.examples.spring.Printer" />
</beans>
```



## 资源的自动部署

* Spring的集成也有一个专门用于对资源部署的特性
* 在流程引擎的配置中,可以指定一组资源,当流程引擎被创建时,所有在这里的资源都将会被自动扫描与部署
* 自动部署有过滤以防止资源重新部署的功能,只有当这个资源真正发生改变的时候,它才会向Activiti使用的数据库创建新的部署

```xml
<bean id="processEngineConfiguration" class="org.activiti.spring.SpringProcessEngineConfiguration">
    ...
    <property name="deploymentResources" value="classpath*:/org/activiti/spring/test/autodeployment/autodeploy.*.bpmn20.xml" />
</bean>

<bean id="processEngine" class="org.activiti.spring.ProcessEngineFactoryBean">
    <property name="processEngineConfiguration" ref="processEngineConfiguration" />
</bean>
```



## 单元测试

* 当集成Spring时,使用标准的[Activiti测试工具类](http://www.mossle.com/docs/activiti/#apiUnitTesting)是非常容易的对业务流程进行测试

```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:org/activiti/spring/test/junit4/springTypicalUsageTest-context.xml")
public class MyBusinessProcessTest {

  @Autowired
  private RuntimeService runtimeService;

  @Autowired
  private TaskService taskService;

  @Autowired
  @Rule
  public ActivitiRule activitiSpringRule;

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

* 对于这种方式,需要在Spring配置中定义一个org.activiti.engine.test.ActivitiRule

```xml
<bean id="activitiRule" class="org.activiti.engine.test.ActivitiRule">
    <property name="processEngine" ref="processEngine" />
</bean>
```



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
        <bean class="org.activiti.parsing.MyFirstBpmnParseHandler" />
    </list>
</property>

<property name="postBpmnParseHandlers">
    <list>
        <bean class="org.activiti.parsing.MySecondBpmnParseHandler" />
        <bean class="org.activiti.parsing.MyThirdBpmnParseHandler" />
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
    <bean class="org.activiti.engine.impl.persistence.StrongUuidGenerator" />
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







