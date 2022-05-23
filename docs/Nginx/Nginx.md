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

* nginx -c /usr/local/nginx/conf/nginx.conf:启动时加载指定位置的配置文件

* nginx -s stop:停止nginx



## 安装包安装

* 解压到指定目录/app/software/nginx

* 安装依赖:yum install -y readline-devel pcre-devel openssl-devel gcc-c++ gcc

* 进入解压后的目录,执行命令:./configure --help,查看该命令的参数,根据需要进行修改

  * --prefix=PATH:安装根目录.默认为/usr/local/nginx
  * --sbin-path=PATH:命令路径.默认为/usr/local/nginx/sbin/nginx
  * --modules-path=PATH:模块路径.默认为/usr/local/nginx/modules
  * --conf-path=PATH:nginx.conf配置文件路径.默认为/usr/local/nginx/conf/nginx.conf
  * --error-log-path=PATH:错误日志路径.默认为/usr/local/nginx/logs/error.log
  * --http-log-path=PATH:访问日志,默认为/var/log/nginx/access.log
  * --pid-path=PATH:运行时的pid文件路径.默认为/usr/local/nginx/logs/nginx.pid
  * --with:可以加上该参数,安装该模块
  * --without:已经安装了的模块,卸载该模块
  * --add-module=PATH:添加指定的模块,必须是已经下载到本地目录中
  * --add-dynamic-module=PATH:动态添加模块
  * --lock-path=PATH:锁定文件,默认为/var/lock/nginx.lock
  * --http-client-body-temp-path=/var/temp/nginx/client:客户端定义
  * --http-proxy-temp-path=/var/temp/nginx/proxy:代理临时目录位置
  * --http-fastcgi-temp-path=/var/temp/nginx/fastcgi:快速访问临时目录
  * --http-uwsgi-temp-path=/var/temp/nginx/uwsgi:sgi 访问目录
  * --http-scgi-temp-path=/var/temp/nginx/scgi:scgi访问临时目录  

  ```shell
  ./configure --prifix=/app/software/nginx \ # 根目录
  --with-http_ssl_module # 安装指定模块,可根据情况自定义安装
  ```

* make && make install



## 目录



* auto:存放 Nginx 自动安装的相关文件
* conf:存放 Nginx 服务器配置文件
* configure:命令,用于对即将安装的软件的配置,完成 makefile 编译文件的生成
* contrib:存放由其他机构贡献的文档材料
* html:存放 Nginx 欢迎页面
* man:manual,手册,存放 Nginx 帮助文档
* src:存放 Nginx 源码



# 配置文件



* 每一行配置后都需要有分号

* nginx.conf包括全局块,events块,http块

* 全局块:配置影响nginx全局的指令,如用户组,日志,配置文件引入等

* user:代表访问权限是什么,就是通过 nginx 访问 linux 服务器中文件时,使用的用户权限

* events:核心配置,与内核相关.配置影响nginx服务器或与用户的网络连接,如每个进程的最大连接数等

  * worker_connection:每个工作进程的最大连接数,跟系统的最大开启文件描述符相关,可使用ulimit相关命令查看和修改
  * use epoll:使用epoll事件驱动,因为epoll的性能相比其他事件驱动要好很多,默认使用epoll

* work_processes:工作进程数,默认为1,最好设置成和服务器核心数相同,也可设置成auto

* include:可以包含其他路径中的nginx配置文件,通常多个服务都是每个服务一个配置文件.包含在include中的配置文件中server就是最上层,其中root,index等参数可以直接写在最外层,不需要写在location中

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



## 内置变量

- `$args`:这个变量等于请求行中的参数,同`$query_string`
- `$content_length`:请求头中的Content-length字段
- `$content_type`:请求头中的Content-Type字段
- `$document_root`:当前请求在root指令中指定的值
- `$host`:请求主机头字段,否则为服务器名称
- `$http_user_agent`:客户端agent信息
- `$http_cookie`:客户端cookie信息
- `$limit_rate`:这个变量可以限制连接速率
- `$request_method`:客户端请求的动作,通常为GET或POST
- `$remote_addr`:客户端的IP地址
- `$remote_port`:客户端的端口
- `$remote_user`:已经经过Auth Basic Module验证的用户名
- `$request_filename`:当前请求的文件路径,由root或alias指令与URI请求生成
- `$scheme`:HTTP方法,如http,https
- `$server_protocol`:请求使用的协议,通常是HTTP/1.0或HTTP/1.1
- `$server_addr`:服务器地址,在完成一次系统调用后可以确定这个值
- `$server_name`:服务器名称
- `$server_port`:请求到达服务器的端口号
- `$request_uri`:包含请求参数的原始URI,不包含主机名,如:`/foo/bar.php?arg=baz`
- `$uri`:不带请求参数的当前URI,`$uri`不包含主机名,如`/foo/bar.html`
- `$document_uri`:与`$uri`相同



## http



* 定义http服务器内容
* include:加载响应类型
* default_type:默认使用 IO 流实现请求/应答
* log_format main:在ngxin.conf.default中可以看到该参数,表示日志的输出格式,可以根据默认配置文件中的说明进行配置.main是一个标识,在access_log中要用到.更多参数参考nginx官网
* access_log foldername main:将nginx的日志以main格式输入到指定目录的文件中
* sendfile on/off:是否支持文件传输
* keepalive_timeout:保持连接的最大时间
* gzip:是否开启数据压缩
* server_tokens off:隐藏nginx版本号
* limit_req_zone:限流,定义在http块中
	* zone:定义IP状态及URL访问频率的共享内存区域.zone=keyword标识区域的名字,以及冒号后面跟区域大小.16000个IP地址的状态信息约1MB,所以示例中区域可以存储160000个IP地址
	* rate:定义最大请求速率.示例中速率不能超过每秒100个请求
	```nginx
	# 定义限流.:$binary_remote_addr表示保存客户端IP地址的二进制形式
	limit_req_zone $binary_remote_addr zone=mylimit:10m rate=100r/s
	
	# 在location中设置限流.burst排队大小,nodelay不限制单个请求间的时间
	localtion / {
		limit_req zone=mylit burst=20 nodelay;
	}
	```

## server

* 若配置文件中有多个server,则相应的配置可以写在location中
* 若使用include包含了多个其他配置文件,每一个文件就是个server,则不需要location

```nginx
server{
    listen 12345;
	server_name localhost;
    # 处理ip:port/a的请求
	location /a{
        autoindex on;
        root /a;
        # add_header Content-Type "text/plain;charset=utf8;"; # 设置请求头的某些属性
        index index.html index.htm;
        rewrite ^(.*)\.vue$ /index.html; # 任何以vue结尾的都跳到index
        proxy_set_header Host 域名
        proxy_pass http://ip:port/; # 或者可以写成proxy_pass name
    }
    # 处理ip:port/b的请求
	location /b{
        autoindex on;
        root /b;
        index index.html index.htm;
        rewrite ^(.*)\.vue$ /index.html; # 任何以vue结尾的都跳到index
        proxy_set_header Host 域名
        proxy_pass http://ip:port/; # 或者可以写成proxy_pass name   
    }
}
```

* listen:监听端口,一个server中可以有多个listen,但是端口不能重复.多个server中的listen可以相同

  ```nginx
  listen 11111;
  listen 22222;
  ```

* server_name:监听的域名,ip地址.若有域名,可写域名.多个域名,ip中间用空格隔开

* location:根据请求地址访问本地不同的资源或转发请求

* autoindex:自动索引,即自动搜索目录中的文件和目录并且展现在页面上

* root:当前服务对应本地目录地址.相对地址从 nginx 安装目录开始寻址,绝对地址从根开始寻址

* alias:目录别名,当项目根目录不在nginx中时指定,绝对路径

* index:启动项目时打开的首页,多个用空格隔开

* proxy_pass:代理IP地址,可以是upstream的名称,也可以写多个IP.注意,IP结尾带不带/可能造成无法访问的问题

* error_page 500 502 503 504 /50x.html:错误页面

* if:同Java中的if,只能在server和location中用,配合内置函数使用,详细语法见Nginx官方文档



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
  
* rewrite src des [flag]:重定向,可以使用正则表达式对需要重定向的页面执行规则
  
  * src:符合正则的请求地址
  * des:匹配后的重定向地址
  * flag:rewrite支持的flag标记
    * last:本条规则匹配完成后,继续向下匹配新的location URI规则
    * break:本条规则匹配完成即终止,不再匹配后面的任何规则
    * redirect:返回302临时重定向,浏览器地址会显示跳转后的URL地址
    * permanent:返回301永久重定向,浏览器地址栏会显示跳转后的URL地址
  
* proxy_pass:反向代理地址,格式为http://ip:port[uri].当匹配的为正则时,不需要写uri,会被匹配掉.若是匹配普通的uri,可根据情况编写.默认情况下,页面上地址栏会跳转到被代理的地址,此时ip被改变了.若不想ip改变,可以使用https,注意该https不是https协议,只是一种特殊写法.例如反向代理到百度,此时可能会因为服务器没有路由而导致跳转失败,所以要注意跳转

* 代理访问:proxy_set_header Host $host:Nginx在进行网关转发时会丢失请求中的域名信息,需要设置请求头信息,$host表示当前请求的host

  ```nginx
  location /{
      # 写上域名是防止某些服务器禁止ip访问.若不禁止ip访问,也可以写ip
  	proxy_set_header Host 域名 
      # name为upstream定义的标识
      proxy_pass http://name
  }
  ```

* return:返回http状态码和可选的第二个参数可以是重定向的URL

  ```nginx
  location /test/url {
      return 301 http://www.example.com/moved/here;
  }
  ```

* deny:禁止访问某个目录

  ```nginx
  location ~* \.(txt|doc)${
    root /app/doc
    deny all;
  }
  ```
  
* allow:允许某个IP访问.如果是网段,用/隔开.如127.0.0.1/64

* expires:缓存

  ```nginx
  # 浏览器缓存,静态资源存用expires
  location ~ .*\.(?:jpg|jpeg|png|gif|ico|cur|gz|svg|svgz|mp4|ogg|ogv|webm)${
      expires 7d;
  }
  location ~ .*\.(?:js|css)${
      expires 7d;
  }
  # 代理层缓存
  proxy_cache_path /data/cache/nginx/ levels=1:2 keys_zone=cache:512m inactive=1d max_size=8g;
  location / {
      location ~ \.(htm|html)?$ {
          proxy_cache cache;
          // 以此变量值为HASH作为KEY
          proxy_cache_key $uri$is_args$args;
          add_header X-Cache $upstream_cache_status;
          proxy_cache_valid 200 10m;
          proxy_cache_valid any 1m;
          proxy_pass http://ip:port;
          proxy_redirect off;
      }
      location ~ .*\.(jpg|jpeg|png|gif|ico|txt|js|css)${
          root /data/html;
          expires 3d;
          add_header Static Nginx-Proxy;
      }
  }
  ```

  



## upstream

* 主要用于反向代理的负载均衡,写在http内,server外

* 需要现在文件定义一个需要进行反向代理的服务器地址

  ```nginx
  # 定义需要进行反向代理的服务器地址,server可以写多个.name可自定义,会在location中使用
  # 当写多个server时表示会用到负载均衡功能,若不显示指定weight,则权重相同,即轮询访问
  upstream name{
      # ip_hash;
      # hash $request_uri;
      # hash_method crc32;
  	server ip1:port1 weight=2; # 真实服务器的ip和端口,若是有负载均衡,可以写多个
      server ip2:port2 weight=1; # 该权重表示访问2次ip1之后再访问1次ip2
      server ip3:port3 backup;   # 只有上面的宕机之后才走,相当于热备服务器
  }
  ```

  * 轮询:默认不指定任何方式时,即为轮询
  * 权重:根据weight指定,数值越大,权重越大,访问次数越高
  * backup:热备,其他的服务器宕机之后才走
  * ip_hash:根据请求的ip进行hash之后转发到其他服务器
  * fair:按后端服务器的响应时间来分配请求,响应时间短的优先分配
  * hash:按访问url的hash结果来分配请求,使每个url定向到同一个后端服务器,后端服务器为缓存时比较有效.在upstream中加入hash语句,server语句中不能写入weight等其他的参数,hash_method是使用的hash算法



## 白名单



### 不限流白名单



```nginx
geo $limit {
    # IP网段
    122.16.11.0/24 0;
}

map $limit $imit_key {
    1 $binary_remote_addr;
    0 "";
}
limit_reg_zone $limit_key zone=mylimit:10m rate=1r/s;

location / {
    limit_req zone=mylimit burst=1 nodelay;
    proxy_pass http://service3Custer
}
```



### 黑名单



```nginx
location / {
	deny 10.52.119.21;
	deny 122.12.1.0/24;
	allow 10.1.1.0/16;
	aLlow 1001:0db8::/32;
	deny all
}
```



# Keepalived



* 在Linux上配合keepalived,利用Linux的LVS功能实现高可用



## 安装



* 上传或下载 keepalived.tar.gz到 /app/software/目录

* 解压安装,之后编译

  ```shell
  ./configure --prefix=/app/software/keepalived
  make && make install
  ```

* 将 keepalived 安装成 Linux 系统服务

  ```shell
  # 拷贝执行文件
  cp /usr/software/keepalived/sbin/keepalived /usr/sbin/
  
  # 将init.d文件拷贝到etc下,加入开机启动项
  cp /usr/software/keepalived/etc/rc.d/init.d/keepalived /etc/init.d/keepalived
  
  # 将keepalived文件拷贝到etc下，加入网卡配置
  cp /usr/software/keepalived/etc/sysconfig/keepalived /etc/sysconfig/ 
  
  # 创建keepalived文件夹
  mkdir -p /etc/keepalived
  
  # 将keepalived配置文件拷贝到etc下
  cp /usr/software/keepalived/etc/keepalived/keepalived.conf /etc/keepalived/keepalived.conf
  
  # 添加可执行权限
  chmod +x /etc/init.d/keepalived
  ```

* 加入开机启动

  ```shell
  # 必须保证/etc/init.d/keepalived存在
  chkconfig --add keepalived	
  chkconfig keepalived on
  
  # 添加完可查询系统服务是否存在
  chkconfig --list
  ```

* 服务启动/停止/重启

  ```shell
  service keepalived start/stop/restart
  ```

  



## 配置文件



```shell
# 执行的脚本
vrrp_script chk_nginx {
	# 运行脚本,检测nginx宕机以后,重启Nginx服务
    script "/etc/keepalived/nginx_check.sh"
    # 检测时间间隔,单位秒
    interval 2
    # 如果条件成立的话,则权重 -20
    weight -20 
}
# 定义虚拟路由,VI_1为虚拟路由的标示符,可自定义
vrrp_instance VI_1 {
	# 决定主从,MASTER主,BACKUP从.会根据网络动态变化
    state MASTER 
    # 绑定虚拟IP的网络接口,根据实际情况填写,同时外网要打开防火墙
    interface ens33
    # 虚拟路由的ID号,主从节点设置必须一样
    virtual_router_id 121
    # 本机真实IP
    #mcast_src_ip 192.168.212.140
    # 节点优先级,主节点比从节点优先级高,若相同会造成IP抢占,网络不稳,范围为0-254,master要比backup高
    priority 100
    # 优先级高的设置 nopreempt 解决异常恢复后再次抢占的问题
    nopreempt
    # 组播信息发送间隔,主从节点设置必须一样,默认1s
    advert_int 1
    # 主从节点验证信息,必须设置相同
    authentication {
        auth_type PASS
        auth_pass 1111
    }
    # 将 track_script 块加入 instance 配置块
    track_script {
    	# 执行 Nginx 监控的服务
        chk_nginx
    }
    # 虚拟ip,也就是解决写死程序的ip怎么能切换的ip,可配置多个
    virtual_ipaddress {
        192.168.1.110
        192.168.1.111
    }
}
```



```shell
#!/bin/bash
# chk_nginx.sh
A=`ps -C nginx -no-header |wc -l`
if [ $A -eq 0 ];then
	# nginx启动地址,根据实际情况修改
    /usr/local/nginx/sbin/nginx
    sleep 2
    if [ `ps -C nginx --no-header |wc -l` -eq 0 ];then
        killall keepalived
    fi
fi
```



## 发邮件



```perl
#!/usr/bin/perl -w
use Net::SMTP_auth;
use strict;
my $mailhost = 'smtp.qq.com';
my $mailfrom = 'xxx@qq.com';
my @mailto   = ('xxx@qq.com');
my $subject  = 'keepalived up on backup';
my $text = "Keepalived 1";  
my $user   = 'xxx@qq.com';
my $passwd = 'XXX';
&SendMail();

sub SendMail() {
    my $smtp = Net::SMTP_auth->new( $mailhost, Timeout => 120, Debug => 1 )
      or die "Error.\n";
    $smtp->auth( 'LOGIN', $user, $passwd );
    foreach my $mailto (@mailto) {
        $smtp->mail($mailfrom);
        $smtp->to($mailto);
        $smtp->data();
        $smtp->datasend("To: $mailto\n");
        $smtp->datasend("From:$mailfrom\n");
        $smtp->datasend("Subject: $subject\n");
        $smtp->datasend("\n");
        $smtp->datasend("$text\n\n"); 
        $smtp->dataend();
    }
    $smtp->quit;
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

  