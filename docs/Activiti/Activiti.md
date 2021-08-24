# Activiti



# 概述

* 流程引擎





# REST API

* 在Activiti7版本之前,activiti-rest.war包含了内置的REST API,将该JAR部署到Tomcat即可访问

* REST API使用JSON格式,它是基于[Restlet](http://www.restlet.org)开发的

* 默认所有REST资源都需要进行登录认证后才能使用.可以在请求头中添加`Authorization:Basic ...`,或在url中包含用户名密码,如:`http://username:password@localhost...`

* 可以将Basic认证与HTTPS一起使用

* 可以认证后删除对应资源,或添加额外的授权给一个认证的用户.可以实现RestAuthenticator接口

  * requestRequiresAuthentication(Request request):在请求认证检查之前调用(通过头部传递合法的账号和密码).如果返回true,这个方法就需要认证才能访问.如果返回false,无论请求是否认证都可以访问.如果返回false,就不会为这个方法调用`isRequestAuthorized`
  * isRequestAuthorized(Request request):在用户已经通过Activiti账号管理认证后,但是在请求实际之前调用.可以用来检查认证用户是否可以访问对应请求.如果返回true,会允许请求执行.如果返回true,请求不会执行,客户端会收到对应的错误
  * 自定义的RestAuthenticator需要设置到RestletServlet的ActivitiRestServicesApplication中.最简单的方法是创建`ActivitiRestServicesApplication`的子类,并在servlet-mapping中设置自定义的类名

  ```xml
  <!-- Restlet adapter -->
  <servlet>
      <servlet-name>RestletServlet</servlet-name>
      <servlet-class>org.restlet.ext.servlet.ServerServlet</servlet-class>
      <init-param>
          <!-- Application class name -->
          <param-name>org.restlet.application</param-name>
          <param-value>com.my.company.CustomActivitiRestServicesApplication</param-value>
      </init-param>
  </servlet>
  ```

  