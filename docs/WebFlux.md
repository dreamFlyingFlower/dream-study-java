# WebFlux



# 概述

* 响应式编程,完全异步和非阻塞,并通过Reactive Streams规范(RxJava)

 *          需要Spring5.0以上,SpringBoot2.0以上,不需要ServletAPI,故而不能作为war部署
 *          分为Mono和Flux,在简单业务上和普通对象差别不大,复杂请求业务下,可以提升性能
 *          Mono主要是针对单个对象,Flux则针对集合对象,两者之间可以相互转换
 *          SpringWebFlux有两种分隔:基于功能和基于注解的:
             *          基于注解的和SpringMVC类似,只不过返回的是Mono和Flux对象
             *          基于功能的则是路由配置与请求的实际处理分开,比较复杂
*          在WebFlux中,请求和响应不再是ServletRequest和ServletResponse,而是ServerRequest和ServerResponse
*          当spring-boot-starter-web和spring-boot-starter-webflux同时存在时,优先使用web
*          WebFlux启动方式默认是Netty,端口8080



# WebClient

* 可以直接通过该类链式调用WebFlux接口,主要用在后台调用