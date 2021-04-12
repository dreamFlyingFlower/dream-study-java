# SpringBoot

# 一.初识

-------
1. springboot是将spring以及相关的配置等都集中管理开发框架,类似于springmvc,但比springmvc更方便管理

2. springboot采用约定的方式来进行开发,抛弃了xml配置的方法,简化开发

3. 导入本地jar包

   ```shell
   # 需要先安装好maven,并配置好环境变量
   mvn install:install-file -DgroupId=com.wy -DartifactId=java-utils -Dversion=0.1 -Dfile=E:\xxxx-0.0.1.jar -Dpackaging=jar
   # DgroupId,DartifactId,Dversion:顾名思义是值依赖里的三项,随意填写
   # Dfile:指明需要导入的jar包的本地地址
   ```



# 二.主要注解

1. @SpringBootApplication:该注解为启动类注解,其中又包含多个其他注解,包括@SpringBootConfiguration和@EnableAutoConfiguration
   1. exclude:启动时需要排除的自动注入类,同EnableAutoConfiguration的exclude
   2. excludeName:启动时需要排除的自动注入类名,同EnableAutoConfiguration的excludeName
   3. scanBasePackages:启动时进行扫描的包名,不配置则默认扫描当前类以及子类,同@ComponentScan的basePackages方法
   4. scanBasePackageClasses:启动时进行扫描的特殊类,同@ComponentScan的scanBasePackageClasses方法
2. @SpringBootConfiguration:被@SpringBootApplication包含,一个项目中只能有一个,作用等同于@Configuration,只是起一个标识作用
3. @EnableAutoConfiguration:自动配置,主要是程序在运行中会扫描classpath下的META-INF/spring.factories文件,扫描该文件中类进行自动加载

Import:注入一个类到spring的环境中,该类需要添加Configuration注解

* ApplicationContextInitializer:在spring调用refreshed方法之前调用该方法.是为了对spring容器做进一步的控制

	注入实现了该类的方法有2种:@Configuration或者在META-INF的spring.factories中添加该类,
	可参照spring-autoconfigure包里的添加
	
* CommandLineRunner:在容器启动成功之后的最后一个回调,该回调执行之后容器就成功启动

* ApplicationEvent:自定义事件,需要发布的事件继承该接口

* ApplicationListener:事件监听.可以直接在listener上添加注解或者使用上下文添加到容器中

* publishEvent:发布事件,必须在refreshed之后调用.使用任何继承了上下文的context调用,传入ApplicationEvent



# 三.运行流程

* 1.判断是否web环境
* 2加载所有classpath下的META-INF/spring.factories的ApplicationContextInitizlizer
* 3.加载所有classpath下的META-INF/spring.factories的ApplicationListener
* 4.加载main方法所在的类
* 5.开始执行run方法
* 6.设置java.awt.headless系统变量
* 7.加载所有META-INF/spring.factories的SpringApplicationRunListener
* 8.执行所有SpringApplicationRunListener的started方法
* 9.实例化ApplicationArguments对象
* 10.创建environment
* 11.配置environment,将run方法的参数配置到environment
* 12.执行SpringApplicationRunListener的environmentPrepared方法
* 13.如果不是web环境,但是是web的environment,把这个web才environment转换成标准的environment
* 14.打印Banner
* 15.初始化ApplicationContext,如果是web环境,则实例化AnnotationConfigEmbeddedWebApplicationContext对象,否则实例化AnnotationConfigApplicationContext
* 16.如果beanNameGenerator不为空,将beanNameGenerator注入到context中
* 17.回调所有的ApplicationContextInitializer方法
* 18.执行SpringApplicationRunListener的contextPrepared方法
* 19.依次往spring容器中注入ApplicationArguments,Banner
* 20.加载所有的源到context中
* 21.执行所有的SpringApplicationRunListener的contextLoaded方法
* 22.执行context的refresh方法,并且调用contenxt的registerShutdownHook方法
* 23.回调,获取容器中所有的ApplicationRunner,CommandLineRunner接口,然后排序,依次调用
* 24.执行所有的SpringApplicationRunListener的finished方法



# 四.开始开发

---------
* spring需要先在官网上下载依赖以及相关的配置,如果已经有过相同的代码,则不需要再下载[样例下载地址](https://start.spring.io/)
* 在下载样例的网页上,选择依赖的时候,可以点击下方的switch to the full version来查看spring关联的主流依赖,也可直接搜索
* 配置maven的依赖镜像仓库:maven文件夹->conf->settings.xml

> ```
> <mirror>
> <id>Central</id>
> <mirrorOf>*</mirrorOf>
> <name>maven</name>
> <url>http://repo1.maven.org/maven2/</url>
> </mirror>
> ```
```

##### 目录结构
---------
* src/mian/java:主要的代码书写资源文件夹,源码目录
* src/main/resouces:资源配置文件存放目录
* static:静态资源存放目录,如html.js等文件
* templates:模版配置文件存放目录,freemarker使用

##### 配置文件
----------
* 若需要详细查看配置文件有那些固定属性,可查看[文档](https://docs.spring.io/spring-boot/docs/2.0.4.BUILD-SNAPSHOT/reference/htmlsingle)
* springboot的配置文件只能是application.properties或application.yml.可安装yml文件提示插件,点开eclipse的help->about eclipse查看eclipse的版本号
* 打开[eclipse的下载地址](https://spring.io/tools/sts/legacy),下载已经集成了sts的eclipse
* 也可以直接下载[sts的插件](https://spring.io/tools/sts/all),需要根据相应的版本来选择,否则插件会出错
* 在配置文件中,可以使用---表示一种情况,profiles表示配置多种启动环境

> java -jar xxxxx.jar --spring.profiles.active=development  表示加载开发环境的配置
> java -jar xxxxx.jar --spring.profiles.active=production 表示加载生产环境的配置

* yml文件相比于properties文件的缺点在于不能使用@PropertySource注解,优点就是少写点代码

##### jpa
* jpa是一种数据库规范,是为了整合hibernate等框架,简化数据库等持久层操作的规范,但是jpa无法满足随时随地使用sql的需求.
如果只是需要使用一次sql,且sql中的参数,条件等都不确定的时候无法使用jpa
* 使用jpa的实体类必须标有注解@Entity,且必须继承Serializable,否则启动报错

##### springboot启动
* springboot的启动类必须是包的最高级,因为默认会从启动类的层级开始找spring直接,例如controller等;
若不是最高层级,会找不到controller;或者在启动类上的@SpringBootApplication注解的scanBasePackages属性修改成想要的扫描路径

##### lombok的使用
* 项目中的lombok的使用,先下载lombok的jar包,进入jar包目录,执行java -jar lombok.jar.运行后会跳出一个对话框来指定开发环境的目录,选择自己的即可
	如eclipse.exe目录

##### druid的使用
	<dependency>
		<groupId>com.alibaba</groupId>
		<artifactId>druid-spring-boot-starter</artifactId>
		<version>${druid-spring-boot-starter.version}</version>
	</dependency>
* 需要使用指定的包,版本号可自己指定
* 该包可以自动监控程序的一些数据库信息,可以更好的掌握程序的运行情况
* 需要程序运行之后在网页上打开页面:ip:port/druid/index.html

##### 将本地maven项目做成maven模版
* 控制台到maven项目中: mvn archetype:create-from-project
* 构建成功后会在本项目的target\generated-sources\archetype目录下生成必要的文件
* 控制台进入archetype目录下:mvn install.
* 构建成功后会在本地仓库中生成一个archetype-catalog.xml的文件,可添加到eclipse的maven->archetype->add local中

##### 多环境配置与启动程序
* 若是有多个环境,可添加多个application-不同的name.yml文件,spring会默认加载application.yml中的配置,这个里面写固定的配置,若是因为环境不同而不同的配置则写在添加的yml文件中.若是需要加载多个配置文件,则可以在spring.profiles.active属性中写多个配置文件名的后缀,即不同的name.在启动打成的jar的时候,则需要使用java -jar XXX.jar --spring.profiles.active=不同的name,需要启动那个配置就使用那个name

##### jsp支持
* 1.需要添加tomcat-embed-jasper的jar包
* 2.在src资源目录下新建webapp/WEB-INF目录
* 3.在配置文件中配置spring.mvc.view.prefix和suffix
* 4.配置完成后,在控制层的返回值,若是字符串,将直接找jsp文件,而不再返回到前端
* 5.若是要带值到jsp中,则需要添加Model对象参数,spring自带

### 主要学习的技术
* 1.数据库动态调用,见DynamicRoutingDataSource
```



# 五.整合Mybatis

## 1 单数据源配置

* mybatis.mapper-locations:xml文件所在路径,若xml文件直接放在mybatis扫描的环境中,则不需要配置.若是单独全部放在非mybatis扫描的环境中,则该参数必须配置

## 2 多数据源配置