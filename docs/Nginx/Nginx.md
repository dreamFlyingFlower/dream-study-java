# Nginx



# 概述

* 正向代理:用户访问服务器,中间有个代理服务,代理服务器代理的是用户
  * 用户无法直接访问真正的服务器,需要通过代理服务器进行转发才能访问真正的服务器
  * 服务器不能判断用户的真实地址以及其他信息,保护用户
  * 正向代理多用来访问无法直接访问的服务器资源,用作缓存,加速访问速度.对客户端访问授权,认证
  * 代理可以记录用户访问记录,对外隐藏用户信息
* 反向代理:用户访问服务器,中间有个代理服务,代理服务器代理的是服务器
* 用户请求通过代理服务器转发给多个服务中的一个,用户并不知道自己访问的真正服务器是那一个
  * 用户无法获得服务的真实信息,保护服务器
* 反向代理多用来保护内网安全,进行负载均衡,缓存,减少服务器压力



# 安装



## yum安装

* yum install nginx:在centos仓库中没有nginx的安装软件,需如下安装

  ```shell
  rpm -Uvh http://nginx.org/packages/centos/7/noarch/RPMS/nginx-xx版本.noarch.rpm
  ```

* service nginx start:nginx启动

* service nginx reload:nginx重启

* service nginx stop:nginx停止

* nginx -s reload:nginx重启



## 安装包安装

* 解压到指定目录/app/software/nginx

* 安装依赖:yum install -y readline-devel pcre-devel openssl-devel gcc-c++ gcc

* 进入解压后的目录,执行命令:./configure --help,查看该命令的参数,根据需要进行修改

  * --prefix=PATH:安装根目录.默认为/usr/local/nginx
  * --sbin-path=PATH:命令路径.默认为/usr/local/nginx/sbin/nginx
  * --modules-path=PATH:模块路径.默认为/usr/local/nginx/modules
  * --conf-path=PATH:nginx.conf配置文件路径.默认为/usr/local/nginx/conf/nginx.conf
  * --error-log-path=PATH:错误日志路径.默认为/usr/local/nginx/logs/error.log
  * --pid-path=PATH:运行时的pid文件路径.默认为/usr/local/nginx/logs/nginx.pid
  * --with:可以加上该参数,安装该模块
  * --without:已经安装了的模块,卸载该模块
  * --add-module=PATH:添加指定的模块,必须是已经下载到本地目录中
  * --add-dynamic-module=PATH:动态添加模块

  ```shell
  ./configure --prifix=/app/software/nginx \ # 根目录
  --with-http_ssl_module # 安装指定模块,可根据情况自定义安装
  ```

* make && make install



# 配置文件

* 每一行配置后都需要有分号
* nginx.conf包括全局块,events块,http块

* 全局块:配置影响nginx全局的指令,如用户组,日志,配置文件引入等

* events:核心配置,与内核相关.配置影响nginx服务器或与用户的网络连接,如每个进程的最大连接数等

  * worker_connection:每个工作进程的最大连接数,跟系统的最大开启文件描述符相关,可使用ulimit相关命令查看和修改

* work_processes:工作进程数,默认为1,最多最好和服务器核心数相同

* http:包含http全局快和server块.可以嵌套多个server,配置代理,缓存,日志定义等绝大多数功能和第三方模块的配置,如文件引入,mime-type定义,日志自定义,连接超时等

* http全局块:配置upstream,错误页面,连接超时等

* server:包含location,配置虚拟主机的相关参数,一个http中可以包含多个server

* location:配置请求的路由以及各种页面的处理情况

* proxy_pass:存在于location中,表示nginx代理的请求发送到另外的地址中.若有网关,最好是转发到网关中

* nginx在代理请求的时候会丢失请求的host信息,需要在location中添加真正的host信息

  ```nginx
  server{
  	location / {
  		// 设置请求头信息,$host表示当前请求的host
  		proxy_set_header Host $host
      	proxy_pass http://192.168.1.199:8080;
  	}
  }
  ```




## http

* include:可以包含其他路径中的nginx配置文件,通常多个服务都是每个服务一个配置文件.包含在include中的配置文件中server就是最上层,其中root,index等参数可以直接写在最外层,不需要写在location中
* log_format main:在ngxin.conf.default中可以看到该参数,表示日志的输出格式,可以根据默认配置文件中的说明进行配置.main是一个标识,在access_log中要用到.更多格式化参数可以参考nginx官网
* access_log foldername main:将nginx的日志以main格式输入到指定目录的文件中
* keepalive_timeout:保持连接的最大时间
* gzip:是否开启数据压缩



## server

* 若配置文件中有多个server,则相应的配置可以写在location中
* 若使用include包含了多个其他配置文件,每一个文件就是个server,则不需要location

```nginx
server{
	location /{
        listen 12345;
		server_name localhost;
        autoindex on;
        root /app;
        # add_header Content-Type "text/plain;charset=utf8;"; # 设置请求头的某些属性
        index index.html index.htm;
        rewrite ^(.*)\.vue$ /index.html; # 任何以vue结尾的都跳到index
        proxy_set_header Host 域名
        proxy_pass http://ip:port; # 或者可以写成proxy_pass name
    }
}
```

* listen:监听端口,一个server中可以有多个listen,但是端口不能重复.多个server中的listen可以相同

  ```nginx
  listen 11111;
  listen 22222;
  ```

* server_name:监听的域名,ip地址.若有域名,可写域名.多个域名,ip中间用空格隔开

* autoindex:自动索引,即自动搜索目录中的文件和目录并且展现在页面上

* root:项目根目录地址,绝对路径

* index:启动项目时打开的首页,多个用空格隔开



## location



* location存在于server中,一个server中可以有多个location

```nginx
location /{ # 请求URI
    root /app;	# 本请求对应的根目录
    index index.html;	# 本请求对应的首页
	rewrite ^(.*)\.vue$ /index.html; # 任何以vue结尾的都跳到index
    proxy_pass http://192.168.1.80:12345; # 单独代理一个ip地址
}
```

* location URI {}:对当前路径以及子路径生效
* location = URI {}:完全匹配才生效
* location ~/~* URI {}:模式匹配URI,此处的URI可使用正则表达式,~区分字符大小写,~*不区分
* location ^~URI {}:不使用正则表达式
* 多location匹配规则:先普通,再正则,匹配顺序= > ^~ > ~|~* > /|/dir/
  * 普通:除了2个正则,其他的都是普通匹配.匹配的顺序和location在文件中的顺序无关
  * 普通匹配使用最大前缀匹配,即匹配最多的才是最后使用规则
  * 有2种情况在普通匹配之后不匹配正则:使用^~或者完全匹配
  * 正则:不完全匹配普通模式时才匹配正则
  * 若同时匹配多个正则,则按照匹配规则在文件中的顺序来,先匹配,先应用
* rewrite src des:重定向,可以使用正则表达式对需要重定向的页面执行规则
  * rewrite [flag]:关键字,正则,替代内容,flag
  * 关键字:重写语法关键字
  * 正则:perl兼容正则表达式语句进行规则匹配
  * 替代内容:将正则匹配的内容替换成replacement
  * flag:rewrite支持的flag标记
    * last:本条规则匹配完成后,继续向下匹配新的location URI规则
    * break:本条规则匹配完成即终止,不再匹配后面的任何规则
    * redirect:返回302临时重定向,浏览器地址会显示跳转后的URL地址
    * permanent:返回301永久重定向,浏览器地址栏会显示跳转后的URL地址
* proxy_pass:反向代理地址,格式为http://ip:port[uri].当匹配的为正则时,不需要写uri,会被匹配掉.若是匹配普通的uri,可根据情况编写.默认情况下,页面上地址栏会跳转到被代理的地址,此时ip被改变了.若不想ip改变,可以使用https,注意该https不是https协议,只是一种特殊写法.例如反向代理到百度,此时可能会因为服务器没有路由而导致跳转失败,所以要注意跳转
* proxy_set_header Host $host:Nginx在进行网关转发时会丢失请求中的域名信息,需要设置请求头信息,$host表示当前请求的host



## upstream

* 主要用于反向代理的负载均衡,写在http内,server外

* 需要现在文件定义一个需要进行反向代理的服务器地址

  ```nginx
  # 定义需要进行反向代理的服务器地址,server可以写多个.name可自定义,会在location中使用
  # 当写多个server时表示会用到负载均衡功能,若不显示指定weight,则权重相同,即轮询访问
  upstream name{
  	server ip1:port1 weight=2; # 真实服务器的ip和端口,若是有负载均衡,可以写多个
      server ip2:port2 weight=1; # 该权重表示访问2次ip1之后再访问1次ip2
  }
  ```

* 在location中定义代理访问

  ```nginx
  location /{
      # 写上域名是防止某些服务器禁止ip访问.若不禁止ip访问,也可以写ip
  	proxy_set_header Host 域名 
      # name为upstream定义的标识
      proxy_pass http://name
  }
  ```



# Docker中使用

* 启动一个nginx实例:docker run -p 8010:8010 --name nginx -d nginx:1.10

* 若docker中没有nginx镜像,docker run会自动拉取相应版本的docker镜像

* 将容器内的配置文件复制到指定目录:docker container cp nginx:/etc/nginx /app/nginx.其中nginx是容器名,冒号后是需要容器内需要复制到外部的文件地址

* 上一步主要是为了映射nginx的配置文件,删除刚才创建的容器,重新创建一个同样端口的容器

  ```shell
  docker run -p 8010:8010 --name nginx -v /app/nginx/html:/user/share/nginx/html -v /app/nginx/logs:/var/log/nginx -v /app/nginx/conf:/etc/nginx -d nginx:1.10
  ```

  