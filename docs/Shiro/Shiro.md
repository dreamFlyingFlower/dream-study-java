# Shiro



# 概述



* 安全框架,执行身份验证、授权、密码和会话管理
* 验证用户来核实他们的身份
* 对用户执行访问控制,如判断用户是否被分配了一个确定的安全角色,判断用户是否被允许做某事
* 在任何环境下使用 Session API,即使没有 Web 或 EJB 容器
* 在身份验证，访问控制期间或在会话的生命周期,对事件作出反应
* 聚集一个或多个用户安全数据的数据源,并作为一个单一的复合用户视图
* 启用单点登录(SSO)功能
* 为没有关联到登录的用户启用Remember Me服务



# 核心组件



![](001.png)



## Subject



* 主体,用户信息.外部应用与subject进行交互,subject将用户作为当前操作的主体,这个主体可以是一个通过浏览器请求的用户,也可能是一个运行的程序
* Subject在shiro中是一个接口,接口中定义了很多认证授相关的方法,外部程序通过subject进行认证授,而subject是通过SecurityManager安全管理器进行认证授权



## SecurityManager



* 安全管理器,管理所有的subject,负责进行认证和授权、及会话、缓存的管理
* 通过SecurityManager可以完成subject的认证、授权等,SecurityManager是通过Authenticator进行认证,通过Authorizer进行授权,通过SessionManager进行会话管理等
* SecurityManager是一个接口,继承了Authenticator, Authorizer, SessionManager这三个接口



## Authenticator



* 认证器,负责主体认证,可自定义实现.需要认证策略(Authentication Strategy),即什么情况下算用户认证通过



## Authorizer



* 授权器,用户通过认证器认证通过,在访问功能时需要通过授权器判断用户是否有此功能的操作权限



## Realm



* 数据库读取+认证功能+授权功能实现
* 用于进行权限信息验证,相当于datasource,securityManager进行安全认证需要通过Realm获取用户权限数据



## Authentication



* 身份认证,登录,验证用户是不是拥有相应的身份



## Authorization



* 授权,角色权限,验证某个已认证的用户是否拥有某个权限



## Session Manager



* 会话管理,用户登录后的session管理,但不需要依赖web容器的session



## SessionDAO



* 会话dao,是对session会话操作的一套接口.可以通过jdbc将会话存储到数据库,也可以把session存储到缓存服务器



## CacheManager



* 缓存管理,将用户权限数据存储在缓存,这样可以提高性能



## Caching



* 缓存,用户信息,权限分配,角色等缓存到Redis中或Session中



## Cryptography



* 密码管理,shiro提供了一套加密/解密的组件,方便开发



## Web Support



* 支持web环境



## Concurrency



* 多线程并发验证,在一个线程中开启另外一个线程,可以把权限自动传播过去



## Run As



* 可以允许一个用户伪装为另外一个用户的身份进行访问,有时候在管理脚本很有用



## Remember Me



* 记住我,登录后下次访问可不用登录





# 主要注解



* RequiresGuest:调用方法时,不需要经过任何验证.该注解会被GuestAnnotationMethodInterceptor拦截
* RequiresAuthentication:调用方法时,用户必须是经过验证.该注解会被AuthenticatedAnnotationMethodInterceptor拦截
* RequiresPermissions:调用方法需要有某个权限,本系统中为菜单.value:需要的权限,logical:多权限时的判断方式,默认是and.该注解会被PermissionAnnotationMethodInterceptor拦截
* RequiresRoles:调用方法需要有某个角色.value:需要的权限,logical:多权限时的判断方式,默认是and.该注解会被RoleAnnotationMethodInterceptor拦截
* RequiresUser:当前用户必须是应用的用户才能访问方法.该注解会被UserAnnotationMethodInterceptor拦截



# 认证与授权流程



## Realm



![](003.PNG)



* 一般不会直接实现Realm接口,而是继承AuthorizingRealm,同时需要重写认证与授权方法



## 认证流程



![002](002.PNG)



```java
UsernamePasswordToken token = new UsernamePasswordToken(username, password);
Subject subject = SecurityUtils.getSubject();
// 调用AuthRealm中的方法进行登录,并存储相关信息
subject.login(token);
```

* 首先调用Subject.login(token)进行登录,其会自动委托给SecurityManager
* Shiro把用户的数据封装成标识token,token一般封装着用户名,密码等信息
* SecurityManager负责身份验证逻辑,它会委托给Authenticator进行身份验证
* Authenticator进行身份验证,Shiro API中核心的身份认证入口点,此处可以自定义插入自己的实现
* Authenticator可能会委托给相应的AuthenticationStrategy进行多Realm身份验证,默认ModularRealmAuthenticator会调用AuthenticationStrategy进行多Realm身份验证
* Authenticator会把相应的token传入Realm,从Realm获取身份验证信息,如果没有返回/抛出异常表示身份验证失败了



## 授权流程



![](004.PNG)



* 主动调用授权代码同上
* 首先调用Subject.isPermitted*/hasRole*接口,其会委托给SecurityManager,而SecurityManager接着会委托给Authorizer,Authorizer将其委托给了自定义的Realm
* Authorizer如果调用如isPermitted(“user:view”),其首先会通过PermissionResolver把字符串转换成相应的Permission实例
* Realm将用户请求的参数封装成权限对象,再从doGetAuthorizationInfo()获取从数据库中查询的权限
* Authorizer会判断Realm的角色/权限是否和传入的匹配,如果有多个Realm,会委托给ModularRealmAuthorizer进行循环判断,如果匹配如isPermitted*/hasRole*会返回true,否则返回false表示授权失败
* 若是根据注解进行授权验证,则会使用相应的拦截器,如RoleAnnotationMethodInterceptor等



# Shiro过滤器



* Shiro内置了很多默认的过滤器,比如身份验证、授权等.可参考mgt.DefaultFilter中的枚举过滤器



## 认证相关



| 过滤器 | 过滤器类                 | 说明                                                         |
| ------ | ------------------------ | ------------------------------------------------------------ |
| authc  | FormAuthenticationFilter | 基于表单的过滤器:如/**=authc,如果没有登录会跳到相应的登录页面登录 |
| logout | LogoutFilter             | 退出过滤器,主要属性:redirectUrl,退出成功后重定向的地址,如/logout=logout |
| anon   | AnonymousFilter          | 匿名过滤器,即不需要登录即可访问;一般用于静态资源过滤;示例/static/**=anon |



## 授权相关



| 过滤器 | 过滤器类                       | 说明                                                         |
| ------ | ------------------------------ | ------------------------------------------------------------ |
| roles  | RolesAuthorizationFilter       | 角色授权拦截器,验证用户是否拥有所有角色;<br />主要属性: loginUrl:登录页面地址(/login.jsp);<br />unauthorizedUrl:未授权后重定向的地址;<br />例:/admin/**=roles[admin] |
| perms  | PermissionsAuthorizationFilter | 权限授权拦截器,验证用户是否拥有所有权限;<br />属性和roles一样;<br />例:/user/**=perms["user:create"] |
| port   | PortFilter                     | 端口拦截器,主要属性:port[可通过的端口];<br />例:/test= port[80],如果用户访问该页面是非80,将自动将请求端口改为80并重定向到80,其他路径/参数等都一样 |
| rest   | HttpMethodPermissionFilter     | rest风格拦截器,自动根据请求方法构建权限字符串(GET=read, POST=create, PUT=update, DELETE=delete, HEAD=read, TRACE=read, OPTIONS=read, MKCOL=create);<br />例:/users=rest[user],会自动拼出user:read,user:create,user:update,user:delete权限字符串进行权限匹配.所有都匹配:isPermittedAll |
| ssl    | SslFilter                      | SSL拦截器,只有请求协议是https才能通过;否则自动跳转会https端口(443);其他和port拦截器一样 |



## 项目授权



| Subject 登录相关方法 | 描述                                |
| -------------------- | ----------------------------------- |
| isAuthenticated()    | 返回true 表示已经登录,否则返回false |



| Subject 角色相关方法                     | 描述                                                         |
| ---------------------------------------- | ------------------------------------------------------------ |
| hasRole(String roleName)                 | 返回true 如果Subject 被分配了指定的角色,否则返回false        |
| hasRoles(List<String> roleNames)         | 返回true 如果Subject 被分配了所有指定的角色,否则返回false    |
| hasAllRoles(Collection<String>roleNames) | 返回一个与方法参数中目录一致的hasRole 结果的集合.有性能的提高如果许多角色需要执行检查 |
| checkRole(String roleName)               | 安静地返回,如果Subject 被分配了指定的角色,否则就抛出AuthorizationException |
| checkRoles(Collection<String>roleNames)  | 安静地返回,如果Subject 被分配了所有的指定的角色,否则就抛出AuthorizationException |
| checkRoles(String… roleNames)            | 与上面的checkRoles 方法的效果相同,但允许Java5 的var-args 类型的参数 |



| Subject 资源相关方法                           | 描述                                                         |
| ---------------------------------------------- | ------------------------------------------------------------ |
| isPermitted(Permission p)                      | 返回true 如果该Subject 被允许执行某动作或访问被权限实例指定的资源,否则返回false |
| isPermitted(List<Permission> perms)            | 返回一个与方法参数中目录一致的isPermitted 结果的集合         |
| isPermittedAll(Collection<Permission>perms)    | 返回true 如果该Subject 被允许所有指定的权限,否则返回false.有性能的提高如果需要执行许多检查 |
| isPermitted(String perm)                       | 返回true 如果该Subject 被允许执行某动作或访问被字符串权限指定的资源,否则返回false |
| isPermitted(String…perms)                      | 返回一个与方法参数中目录一致的isPermitted 结果的数组.有性能的提高如果许多字符串权限检查需要被执行 |
| isPermittedAll(String…perms)                   | 返回true 如果该Subject 被允许所有指定的字符串权限,否则返回false |
| checkPermission(Permission p)                  | 安静地返回,如果Subject 被允许执行某动作或访问被特定的权限实例指定的资源,否则抛出AuthorizationException |
| checkPermission(String perm)                   | 安静地返回,如果Subject 被允许执行某动作或访问被特定的字符串权限指定的资源,否则抛出AuthorizationException |
| checkPermissions(Collection<Permission> perms) | 安静地返回,如果Subject 被允许所有的权限,否则抛出AuthorizationException .有性能的提高如果需要执行许多检查 |
| checkPermissions(String… perms)                | 和上面的checkPermissions 方法效果相同,但是使用的是基于字符串的权限 |



# Springboot集成Shiro



* ts_user:用户表,一个用户可以有多个角色
* ts_role:角色表,一个角色可以有多个资源
* ts_resource:资源表
* ts_user_role:用户角色中间表
* ts_role_resource:角色资源中间表



```sql
CREATE TABLE `ts_user` (
  `ID` varchar(36) NOT NULL COMMENT '主键',
  `LOGIN_NAME` varchar(36) DEFAULT NULL COMMENT '登录名称',
  `REAL_NAME` varchar(36) DEFAULT NULL COMMENT '真实姓名',
  `NICK_NAME` varchar(36) DEFAULT NULL COMMENT '昵称',
  `PASS_WORD` varchar(150) DEFAULT NULL COMMENT '密码',
  `SALT` varchar(36) DEFAULT NULL COMMENT '加密因子',
  `SEX` int(11) DEFAULT NULL COMMENT '性别',
  `ZIPCODE` varchar(36) DEFAULT NULL COMMENT '邮箱',
  `ADDRESS` varchar(36) DEFAULT NULL COMMENT '地址',
  `TEL` varchar(36) DEFAULT NULL COMMENT '固定电话',
  `MOBIL` varchar(36) DEFAULT NULL COMMENT '电话',
  `EMAIL` varchar(36) DEFAULT NULL COMMENT '邮箱',
  `DUTIES` varchar(36) DEFAULT NULL COMMENT '职务',
  `SORT_NO` int(11) DEFAULT NULL COMMENT '排序',
  `ENABLE_FLAG` varchar(18) DEFAULT NULL COMMENT '是否有效',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='用户表';

```



```sql
CREATE TABLE `ts_role` (
  `ID` varchar(36) NOT NULL COMMENT '主键',
  `ROLE_NAME` varchar(36) DEFAULT NULL COMMENT '角色名称',
  `LABEL` varchar(36) DEFAULT NULL COMMENT '角色标识',
  `DESCRIPTION` varchar(200) DEFAULT NULL COMMENT '角色描述',
  `SORT_NO` int(36) DEFAULT NULL COMMENT '排序',
  `ENABLE_FLAG` varchar(18) DEFAULT NULL COMMENT '是否有效',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='用户角色表';
```



```sql
CREATE TABLE `ts_resource` (
  `ID` varchar(36) NOT NULL COMMENT '主键',
  `PARENT_ID` varchar(36) DEFAULT NULL COMMENT '父资源',
  `RESOURCE_NAME` varchar(36) DEFAULT NULL COMMENT '资源名称',
  `REQUEST_PATH` varchar(200) DEFAULT NULL COMMENT '资源路径',
  `LABEL` varchar(200) DEFAULT NULL COMMENT '资源标签',
  `ICON` varchar(20) DEFAULT NULL COMMENT '图标',
  `IS_LEAF` varchar(18) DEFAULT NULL COMMENT '是否叶子节点',
  `RESOURCE_TYPE` varchar(36) DEFAULT NULL COMMENT '资源类型',
  `SORT_NO` int(11) DEFAULT NULL COMMENT '排序',
  `DESCRIPTION` varchar(200) DEFAULT NULL COMMENT '描述',
  `SYSTEM_CODE` varchar(36) DEFAULT NULL COMMENT '系统code',
  `IS_SYSTEM_ROOT` varchar(18) DEFAULT NULL COMMENT '是否根节点',
  `ENABLE_FLAG` varchar(18) DEFAULT NULL COMMENT '是否有效',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='资源表';

```



```sql
CREATE TABLE `ts_role_resource` (
  `ID` varchar(36) NOT NULL,
  `ENABLE_FLAG` varchar(18) DEFAULT NULL,
  `ROLE_ID` varchar(36) DEFAULT NULL,
  `RESOURCE_ID` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='角色资源表';
```



```sql
CREATE TABLE `ts_user_role` (
  `ID` varchar(36) NOT NULL,
  `ENABLE_FLAG` varchar(18) DEFAULT NULL,
  `USER_ID` varchar(36) DEFAULT NULL,
  `ROLE_ID` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='用户角色表';
```

