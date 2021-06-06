# Docker



# 概述

* 提供简单轻量的建模方式
* 职责的逻辑分离
* 快速高效的开发声明周期
* 鼓励使用面向服务的架构
* 使用Docker容器开发,测试和部署服务
* 创建隔离的运行环境
* 搭建测试环境
* 构建多用户的平台即服务(PaaS)基础设施
* 提供软件即服务应用程序(SaaS)
* 高性能,超大规模的宿主机部署
* 文件系统隔离能力:每个容器都有自己的root文件系统
* 进程隔离:每个容器都运行在自己的进程环境中
* 网络隔离:容器间的虚拟网络接口和IP地址都是分开的
* 资源隔离和分组:使用cgroups将CPU和内存之类的资源独立分配给每个Docker容器



# 组成

* Docker Client:客户端,C/S架构
* Docker Daemon:守护进程
* Docker Server:Docker Daemon的主要组成部分,接收用户通过Docker Client发送的请求
* Docker Image:镜像,容器的基石,层叠的只读文件系统
* Docker Container:容器,通过镜像启动,启动和执行阶段,写时复制
* Docker Registry:仓库



# 安装

* 修改docker远程仓库的原地址,修改/etc/default/docker,添加如下:

  ```shell
  DOCKER_OPTS="--registry-mirror=新的远程仓库地址"
  ```
  
* 查找Docker-CE的版本

  ```shell
  yum list docker-ce.x86_64 --showduplicates | sort -r
  ```

* 卸载旧版本的docker

  ```shell
  yum remove -y docker-ce
  ```

* 安装依赖

  ```shell
  yum install -y yum-utils device-mapper-persistent-data lvm2
  ```

* 添加阿里云的源

  ```shell
  yum-config-manager --add-repo https://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
  ```

* 安装docker

  ```shell
  yum -y install docker-ce-[Version] # 若不指定版本,默认安装最新版本
  ```



# Shell

* systemctl start/stop/status/restart docker:linux上启动/停止/状态/重启docker

* systemctl enable docker:linux开机自启动docker

* docker search [] iname:iname指镜像名称,从远程docker仓库中搜索镜像.一次最多显示25个镜像,只有选项中official为ok的是官方镜像

  * -s num:只显示指定星级以上的镜像
  
* docker pull [] name[:TAG]:从远程仓库中拉取镜像到本地,tag为镜像版本,默认拉取latest版本,一般拉取management版本即可,最新版本可能不够稳定

  * -a:拉取所有版本的镜像到本地

* docker images []:查看本地所有镜像,不显示中间层镜像

  * -a:查看所有镜像,显示中间层镜像,这些镜像没有名称和版本
  * -f iname:利用镜像的名称进行过滤,也可以使用linux中的grep等命令
  * --no-trunc:指定不使用截断的形式显示镜像的信息,如imageid就是被截断的
  * -q:只显示截断后镜像的imageid(iid)
  
* docker run [] IMAGE[:TAG] [COMMAND] [ARG]:启动新的容器,在新容器中执行命令

  ```shell
  # 以终端的形式运行容器并进入容器内部./bin/bash表示进入容器内部,每个IMAGE可能不一样
  docker run -it redis /bin/bash
  # 指定后台运行,不需要指定/bin/bash
  docker run -d --name=redis01 redis
  # 指定端口映射
  docker run -d -p 6181:6181 -p 6282:6282 --name=zk01 zookeeper
  ```

  * IMAGE:镜像文件的名称,该命令运行后会产生一个新的容器
  * -d:容器在创建时以后台启动的方式运行,没有交互界面.-d和-it同时使用无效
  * -i:docker为启动进程始终打开标准输入,即非后台启动
  * -t:为docker分配一个终端,或者CTRL+P/CTRL+Q退出终端,即可实现后台运行
  * -p port1:port2:为运行的容器指定端口映射
    * port1是主机端口,port2是docker容器端口
    * 若不指定port1,则将随机映射到主机端口
    * 可以同时写多个端口映射,如-p port1:port2 -p port3:port4
  * --name=cname:自定义容器的名称(cname),不能重复,若不指定,由docker自行定义
  * -v src:des[:rwo]:将容器中的文件映射到主机中,保证数据的持久化,可以给目录赋权
    * src:主机中的目录地址
    * des:容器中的目录地址
  * --volumes-from cid/cname:共享其他容器创建的数据卷
  * --volumes-from cid/cname -v path1:path2 --name cname1 iname tar cvf path2 .tar datavolumn:备份数据卷到主机中,实则是备份目录的映射
    * path1:主机中的目录地址
    * path2:容器中数据卷的备份地址
    * tar cvf...:容器中备份时执行的命令;若是还原,则将cvf改成xvf即可
    * datavolumn:容器中需要备份的数据卷目录,多个用空格隔开
  * --link=cname:alias:由于docker容器重启之后ip都会改变,若是有使用ip的操作,重启之后就会失效.该参数就是给需要通过ip操作的docker容器起一个别名,所有通过ip的操作可以通过别名来完成,类似于主机名.cname是另外一个容器的名称,alias是给该容器起的别名

* docker ps []:查看所有正在运行的容器

  * -a:查看所有正在运行和已经停止的容器
  * -l:查看最后一次运行的容器
  * -f status=exited:查看停止的容器

* docker attach cid/cname:以终端的形式进入到某个容器中

* docker exec [-d/-i/-t] cid/cname [command] [arg]:在运行中的容器内启动新进程

  ```shell
  # 以终端的形式进入容器内部
  docker exec -it cid/cname /bin/bash
  # 不进入终端启动容器中的其他应用,appname为容器中的某个应用
  docker exec cid/cname appname
  ```

* docker top cid/cname:查看运行的容器中的进程情况

* docker inspect [] iid/iname/iname:tag/cid/cname:查看镜像,容器的详细信息

  * -f:格式化形式详细信息

* docker inspect --format='{{.NetworkSettings.IPAddress}}' iid/iname/iname:tag/cid/cname:直接输出容器或镜像的ip地址,双大括号固定写法,里面的内容需要根据inspect信息指定,是一个Json对象

* docker logs [] cid/cname:查看容器日志

  * -f:一直跟踪日志变化返回结果
  * -t:在返回的结果上加上时间戳
  * --tail num:返回结尾多少行的数量,num是数量

* docker start/restart [-it] cid/cname:启动或重启已经停止的容器

* docker stop/kill cid/cname:停止某个容器,stop是优雅停止,kill是暴力停止

* docker rmi [] iid/iname/iname:tag:删除镜像.当镜像有多个版本时,可使用最后一种方式删除

  * -f:强制删除镜像,即使有容器依托在该镜像上运行
  * --no-prune:不删除镜像中没有打标签(tag)的父镜像
  * docker rmi \`docker images -q\`:删除没有在容器中运行的镜像

* docker rm [] cid/cname:删除没有运行的容器

  * -f:强行删除容易,不管是否在运行中
  * docker rm $(docker ps -aq):删除所有容器

* docker info:显示docker的运行信息

* docker -d []:docker启动时守护进程选项

  * -D:是否以debug模式运行
  * -e:
  * -g path:默认地址为/var/lib/docker
  * --icc=true:允许在同一台主机上的docker容器之间数据互联,默认是开启的
  * -l:运行时的日志级别,默认为info
  * --label key=value:定义标签
  * -p:进程id,默认地址为/var/run/docker.pid
  
* docker save mysql:版本 > /目录/mysql.tar.gz:将镜像从docker中导出到指定目录下,若是latest,版本可不写,>可以换成-o表示输出docker save mysql -o mysql.tar.gz

* docker load < mysql.tar.gz:将镜像导入到docker中,或者docker load -i < mysql.tar.gz

* docker build -t 镜像名:版本 .:将执行目录下的Dockerfile指定的项目打包成镜像

  * -t:指定镜像名的名字
  * .:表示当前目录,也可以是其他目录
  
* docker cp file1 containerid:file2:将linux中的文件拷贝到docker容器中的指定目录中

* docker cp containerid:file2 file1 :将docker容器中的文件拷贝到Linux指定目录中



# 镜像构建

## Docker Commit

* 通过容器构建
* docker commit [] CONTAINER [REPOSITORY[:TAG]]:构建镜像
  * -a author:作者名称
  * -m msg:镜像信息
  * -p:在构建镜像时是否暂停正在运行的容器,默认true暂停



## Docker Build

* 通过Dockerfile文件构建镜像

* 新建一个Dockerfile文件,无后缀,内容如下:

  * FROM:该参数表示依赖的已经存在的镜像名,格式为iname[:tag],必须是第一条非注释指令
  * MAINTAINER:维护者的信息,多个用空格隔开
  * RUN:可以有多个,有2种模式
    * shell命令:/bin/sh -c command,其中command自定义,其他固定写法
    * exec模式:["executable","arg1","arg2"...].相当于每一个空格都是一个参数
  * EXPOSE:指定镜像运行时容器使用的端口,可以有多个
  * CMD:指定容器运行的默认命令,如果在docker run时指定了默认命令,则会覆盖CMD命令.CMD有3种模式,其中2种和RUN相同,还有一种模式是exec中没有executable的,只带参数,该模式需要配合ENTRYPOINT使用
  * ENTRYPOINT:模式和CMD相同,但是在默认情况下不会被docker run中的命令覆盖,但可以使用--entrypoint参数强行覆盖.entrypoint和cmd配置使用,entrypoint中只写主要命令,cmd中只写参数,这样可以在docker run时使用-g 'args'覆盖cmd中参数
  * ADD src des:将其他文件或目录复制到使用dockerfile构建的镜像中.若是文件路径中有空格,可以使用["src","des"].
  * COPY src des:功能同ADD,不同的是add有类似解压的功能,若单纯的复制文件,推荐copy
  * VOLUME["/data1","/data2"]:向镜像中提供卷,用于持久化和数据共享.类似于docker run的-v参数,但是它不能映射到主机中
  * WORKDIR /path:在创建容器时指定默认的工作目录,即cmd的默认目录
  * ENV k v/k=v:设置环境变量
  * USER username:指定容器以那个用户运行,若不使用,默认以root用户运行
  * ONBUILD:触发器,当一个镜像被其他镜像作为基础镜像时执行时,会在构建过程中插入指令

  ```shell
  FROM JAVA1.8
  MAINTAINER username
  RUN ["java -jar","--spring.profile.actives=dev","xxx.jar"]
  EXPOSE 8080 8082
  CMD /bin/bash # 若在docker run时定义了启动命令,则CMD无效
  ENTRYPOINT /bin/bash
  ```

* docker build [] path/url:构建镜像

  * path/url:path和url是要构建镜像的Dockfile地址,一个是本地地址,一个是远程地址
  * -t iname[:tag]:指定镜像的名称和版本,若不指定版本,默认latest
  * --no-cache:不使用构建缓存,默认是使用



# 数据卷

* 数据卷是经过特殊设计的目录,可以绕过联合文件系统(UFS),为一个或多个容器提供访问
* 数据卷的设计目的在于数据持久化,完全独立于容器个生命周期,因此,docker不会在容器删除时删除其挂载的数据卷
* 数据卷在容器启动时初始化,若容器使用的镜像在挂载点包含了数据,这些数据会复制到新数据卷中
* 数据卷可以在容器之间共享和重用
* 可以对数据卷里的内容进行直接修改
* 数据卷的变化不会影响镜像的更新
* 卷会一直存在,即使挂载数据卷的容器已经被删除
* 在容器运行时的体现就是docker run中的-v参数



# 跨主机连接

* 使用网桥实现跨主机容器连接
* 使用Open vSwitch实现跨主机容器连接
* 使用weave实现跨主机容器连接



# Maven中使用

```xml
<plugin>
    <groupId>com.spotify</groupId>
    <artifactId>docker-maven-plugin</artifactId>
    <version>1.0.0</version>
    <!-- docker镜像配置 -->
    <configuration>
        <!-- 镜像名 -->
        <imageName>${project.artifactId}-${project.version}</imageName>
        <!-- dockerfile所在目录 -->
        <dockerDirectory>${project.basedir}/src/main/resources</dockerDirectory>
        <!-- 镜像版本 -->
        <imageTags>
        	<imageTag>${project.version}</imageTag>
        </imageTags>
        <!-- 构建镜像的配置信息 -->
        <resources>
            <resource>
                <targetPath>/</targetPath>
                <directory>${project.build.directory}</directory>
                <include>${project.artifactId}-${project.version}.jar</include>
            </resource>
        </resources>
    </configuration>
</plugin>
```

