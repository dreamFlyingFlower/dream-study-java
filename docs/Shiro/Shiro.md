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



# 核心



* Subject:主体,用户信息
* SecurityManager:安全管理器,管理所有的subject,负责进行认证和授权、及会话、缓存的管理
* Realms:用于进行权限信息验证,需要自己实现
* Authentication:身份认证,登录,验证用户是不是拥有相应的身份
* Authorization:授权,角色权限,验证某个已认证的用户是否拥有某个权限
* Session Manager:会话管理,用户登录后的session管理,session缓存到redis中
* Cryptography:加密
* Web Support:支持web环境
* Caching:缓存,用户信息,权限分配,角色等缓存到redis中
* CacheManager: 缓存控制器,来管理如用户,角色,权限等缓存
* Concurrency:多线程并发验证,在一个线程中开启另外一个线程,可以把权限自动传播过去
* Run As:可以允许一个用户伪装为另外一个用户的身份进行访问,有时候在管理脚本很有用
* Remember Me:记住我,登录后下次访问可不用登录
* Authenticator: 认证器,负责主体认证,可自定义实现.需要认证策略(Authentication Strategy),即什么情况下算用户认证通过
* Authrizer: 授权器,访问控制器,用来决定主体是否有权限进行相应的操作,即控制着用户能访问应用中的哪些功能
* Realm: 可以有1个或多个Realm,可以认为是安全实体数据源,即用于获取安全实体的.可以是JDBC实现,也可以是LDAP实现,或者内存实现等



# 主要注解



* RequiresGuest:调用方法时,不需要经过任何验证.该注解会被GuestAnnotationMethodInterceptor拦截
* RequiresAuthentication:调用方法时,用户必须是经过验证.该注解会被AuthenticatedAnnotationMethodInterceptor拦截
* RequiresPermissions:调用方法需要有某个权限,本系统中为菜单.value:需要的权限,logical:多权限时的判断方式,默认是and.该注解会被PermissionAnnotationMethodInterceptor拦截
* RequiresRoles:调用方法需要有某个角色.value:需要的权限,logical:多权限时的判断方式,默认是and.该注解会被RoleAnnotationMethodInterceptor拦截
* RequiresUser:当前用户必须是应用的用户才能访问方法.该注解会被UserAnnotationMethodInterceptor拦截



# 认证与授权流程



## 认证流程



```java
UsernamePasswordToken token = new UsernamePasswordToken(username, password);
Subject subject = SecurityUtils.getSubject();
// 调用AuthRealm中的方法进行登录,并存储相关信息
subject.login(token);
```

* 首先调用Subject.login(token)进行登录,其会自动委托给SecurityManager
* SecurityManager负责身份验证逻辑,它会委托给Authenticator进行身份验证
* Authenticator进行身份验证,Shiro API中核心的身份认证入口点,此处可以自定义插入自己的实现
* Authenticator可能会委托给相应的AuthenticationStrategy进行多Realm身份验证,默认ModularRealmAuthenticator会调用AuthenticationStrategy进行多Realm身份验证
* Authenticator会把相应的token传入Realm,从Realm获取身份验证信息,如果没有返回/抛出异常表示身份验证失败了



## 授权流程



* 主动调用授权代码同上
* 首先调用Subject.isPermitted*/hasRole*接口,其会委托给SecurityManager,而SecurityManager接着会委托给Authorizer
* Authorizer是真正的授权者,如果调用如isPermitted(“user:view”),其首先会通过PermissionResolver把字符串转换成相应的Permission实例
* 在进行授权之前,会调用相应的Realm获取Subject相应的角色/权限用于匹配传入的角色/权限
* Authorizer会判断Realm的角色/权限是否和传入的匹配,如果有多个Realm,会委托给ModularRealmAuthorizer进行循环判断,如果匹配如isPermitted*/hasRole*会返回true,否则返回false表示授权失败
* 若是根据注解进行授权验证,则会使用相应的拦截器,如RoleAnnotationMethodInterceptor等