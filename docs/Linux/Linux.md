# Linux

# 目录结构
* /:根目录
* /bin:存放二进制可执行文件,普通用户使用的命令.当前为一个软链接,指向usr/bin
* /boot:存放用于系统引导时使用的各种文件
  * grub:开机设置相关文件
  * 内核文件(vmlinuz)
* /dev:用于存放设备文件,如disk,dvd,floppy,stdin等
  * /dev/zero:源源不断的产生0数据
  * /dev/null:类似于回收站,放里面的数据就消失
  * /dev/random:产生随机数
* /etc:存放系统配置文件
  * rc.d:用于存放不同运行等级的启动脚本的链接文件
* /root:超级用户root目录
* /home:存放所有普通用户目录的根目录
* /lib:软链接,指向/usr/lib,用于存放程序的动态库和模块文件
* /lib64:软链接,指向/usr/lib64,系统运行库
* /media:用于挂载本地磁盘或其他存储设备,默认挂载点
  * cdrom
  * floppy
  * U盘
* /mnt:系统管理员安装临时文件系统的安装点,手工临时挂载点
* /opt:额外安装的可选应用程序包所放置的位置
* /sbin:存放二进制可执行文件,只有root才能访问,软链接,指向usr/sbin
* /srv:存放服务进程所需的数据文件和一些服务的执行脚本
* /tmp:sbin用户存放各种临时文件,进程产生的临时文件
* /proc:虚拟文件系统,存放当前的内存,进程信息,每一个数字代表一个进程
* /usr:系统文件,用户存放系统应用程序,比较重要的目录
  * bin:普通用户使用的应用程序
  * sbin:管理员使用的应用程序
  * include:标准包含头文件
  * lib:32位库文件glibc
  * lib64:64位库文件glibc
  * share:体系结构无关(共享)数据
  * src:源代码,需要用户自行下载安装
  * local:本地管理员软件安装目录
* /var:用于存放运行时需要改变数据的文件,比如mysql,mail,日志
* ~:当前登录用户的根目录



# 用户信息

* /etc/passwd:用户信息配置文件,每行定义一个用户账户,此文件对所有用户可读,每行账户包含多个信息,用:隔开,如:

	> 用户名:口令:用户标识号:组标识号:注释:宿主目录:命令解释器
	
	* 口令:X表示用户口令被/etc/shadow文件保护
	* 用户标识号:也叫UID,系统内唯一.root用户为0,普通用户从1000开始,1-999是系统标准账户
	* 宿主目录:用户登录系统后所进入的目录,除root外都在home下
	* 命令解释器:指定该用户使用的shell,默认是/bin/bash
* /etc/shadow:用户口令信息配置文件,为了增加系统的安全性,用户口令通常用shadow passwords保护,只有root可读,多配置用:隔开,如:

	> 用户名:口令:最后一次修改时间:最小时间间隔:最大时间间隔:警告时间:不活动时间:失效时间:标志
	
	* 口令:用户密码,加密
	* 最后一次修改时间:从1970-1-1起,到用户最后一次更改口令的天数
	* 最小时间间隔:从1970-1-1起到用户可以更改口令的天数
	* 最大时间间隔:从1970-1-1起,必须更改的口令天数
	* 警告时间:在口令过期之前几天通知
	* 不活动时间:在用户口令过期后到禁用账户的天数
* /etc/group:用户组信息配置文件,将用户进行分组时linux对用户进行管理及控制访问权限的一种手段.该文件对所有用户可读,但组成员做了处理,不可见.多配置:隔开,如:

	> 组名:组口令:gid:组成员

* /etc/gshadow:定义用户组口令,组管理员信息,只有root可读



# 运行级别

## 概述

* 0:关机
* 1:单用户,多用来找回丢失密码
* 2:多用户状态,无网络服务
* 3:多用户状态,有网络服务
* 4:系统未使用保留给用户
* 5:图形界面
* 6:系统重启



## 配置文件

该配置文件在/etc/inittab中,该配置文件有效的基本只有一行



## 修改运行级别

### 修改配置文件

> id:5:initdefault->该行中的数字即代表运行级别,修改该数字,保存之后重启即可



### 命令行

init num:控制台中直接修改运行级别



## root密码找回

进入到单用户模式时,root不需要密码就可以登录系统

1. 重启linux,在提示中按任意键进入menu
2. 根据提示:'e' to edit the commands before booting,按e进入下一个界面
3. 下一个界面选择开头为kernel(内核)的,再次输入e
4. 进入之后在最下面一行quiet末尾之后添加1,中间有空格,这代表进入单用户级别,之后回车
5. 回车之后返回上一个页面,根据提示,按b,启动linux
6. 启动完成之后将直接登录到root用户,使用passwd命令即可修改root密码



## chkconfig

> 给每个服务的各个运行级别设置自启动或关闭

* chkconfig --list:查看服务
* chkconfig --level 3 服务名 on/off:给某个服务设置运行级别的自启动或关闭,服务名可以从/etc/init.d中查看



# 命令

> 以下命令中常用参数注解:
>
> []:表示其他中间命令,中间命令可有可无,多个命令可连写,如ls -l -a可写成ls -la
>
> \[filename\]:表示该参数可有可无
>
> filename,file:文件,文件名
>
> foldername,folder:目录,目录名
>
> content:内容
>
> src:目标文件,目录
>
> des:目的文件,目录
>
> username:用户名
>
> groupname:分组名
>
> pwd:用户口令
>
> command:命令



## 硬件信息

* hostname:显示主机名字
* hostname xxx:设置主机的名字,但是下次登录仍然会还原
* hostnamectl set-hostname xxx:将linux的主机名设置成xxx,该设置为永久设置
  * 或者修改/etc/sysconfig/network中的HOSTNAME也可
  * 主机名中不要有_(下划线),系统不识别,可能造成其他问题
  * 修改主机名,重启之后才能生效
* /etc/hosts:修改主机名和ip之间的映射
* uname -a:显示系统信息
* df []:显示文件系统磁盘空间的使用情况,默认以字节为单位
  * -h:显示计算单位,同时以最优方式显示数据
  * -a:显示有文件系统的磁盘使用情况
  * -m:以m为单位显示
  * -t:显示指定文件系统的磁盘空间使用情况
  * -T:显示文件类型
  * -l:计算文件大小
* fdisk -l:查看分区详情,更复杂使用情况可百度
* lsblk:查看硬盘分区和磁盘,u盘等树形挂载情况,外接U盘会显示在该信息中
  * linux磁盘分为IDE硬盘和SCSI硬盘
  * 对于IDE硬盘,驱动标识符为hdx~,其中hd表明分区所在设备的类型,此处指IDE硬盘;x为盘号,a为基本盘,b为基本盘从属盘,c为辅助主盘,d为辅助从属盘;~代表分区,前4个分区用1到4表示,它们是主分区或扩展分区,从5开始就是逻辑分区
  * 对于SCSI硬盘,驱动标识符为sdx~,sd表示为SCSI硬盘,其他和IDE硬盘一样
* mount /u/u1 /mnt/foldername:将u盘或其他外置设备分区挂载到linux系统的mnt目录的foldername下.挂载的分区名可以通过lsblk查看,每次挂载可能名字都不一样.挂载的linux文件目录可自定义
* umount /mnt/foldername:将挂载的u盘等卸载
* du [] filename/foldername:显示文件或目录已使用的空间总量
  * -a:递归显示指定目录中各文件和子目录中文件占用的数据块
  * -s:显示指定文件或目录占用的数据块
  * -b:以字节为单位显示磁盘占用情况
  * -l:计算所有文件大小,对硬链接文件计算多次
  * -h:显示文件占用数据块大小
  * -c:列出明细的同时增加汇总值
  * --max-depth=1:子目录深度



## 系统信息



* free []:显示当前内存和交换空间的使用情况,默认是k为单位
  * -h:显示计算单位,同时以最优方式显示数据
  * -b:以字节为单位显示内存和交换空间大小信息
  * -m:以m为单位展示
  * -g:以g为单位展示
  
* w:显示平均负载信息,其中load average会有3个值,这3个值分别表示最近1分钟,5分钟,15分钟的平均负载,一般这几个值在0.6和0.7之间是比较标准的,大于0.7就是比较危险的,需要进行优化

* top []:显示当前系统中耗资源使用情况,实时
  * -M:根据内存使用量来排序
  * -P:根据CPU占有率来排序
  * -T:根据进程运行时间的长短来排序
  * -U username:查看指定用户的进程
  * -K pid:根据pid结束进程
  * -q:退出
  * -h:获得帮助
  * -d num:显示用户进程状态和进程控制,每num分钟刷新一次
  * -c:显示整个命令行的信息
  * 结果第一行:
    * top:当前时间
    * up:机器运行了多少时间
    * users:当前有多少用户
    * load average:一段时间内,CPU正在处理以及等待处理的进程数之和,分别是1,5,15分钟的负载统计值
  * 结果第二行:
    * Tasks:当前有多少进程
    * running:正在运行的进程,越多表示服务器压力越大
    * sleeping:正在休眠的进程
    * stopped:停止的进程
    * zombie:僵尸进程
  * 结果第三行:
    * us:用户进程占CPU的使用率,长期过高,表明用户进程占用了大量CPU时间
    * sy:系统进程占CPU的使用率
    * ni:用户进程空间改变过优先级
    * id:空闲CPU占用率
    * wa:等待输入输出的CPU时间百分比
    * hi:硬件的中断请求
    * si:软件的中断请求
    * st:steal time
  * 结果第四行,第五行:
    * 分别是内存信息和swap(内存交换分区)信息,所有程序的运行都是在内存中进行的
    * 当内存的free变少的时候,其实我们并不需要太紧张,真正需要看的是Swap中的used信息
    * Swap分区是由硬盘提供的交换区,当物理内存不够用的时候,操作系统才会把暂时不用的数据放到Swap中.所以当这个数值变高的时候,说明内存是真的不够用了
  * 第五行以下:
    * PID:进程id
    * USER:进程所有者
    * PR:优先级.数值越大优先级越高
    * NI:nice值,负值表示高优先级,正值表示低优先级
    * VIRT:进程使用的虚拟内存总量
    * SWAP:进程使用的虚拟内存中被换出的大小
    * RES:进程使用的,未被换出的物理内存大小
    * SHR:共享内存大小
    * SHR:共享内存大小
    * S:进程状态.D表示不可中断的睡眠状态,R表示运行,S表示睡眠,T表示跟踪/停止,Z表示僵尸进程
    * %CPU:上次更新到现在的CPU占用百分比 
    * %MEM:进程使用的物理内存百分比 
    * TIME+:进程使用的CPU时间总计,单位1/100秒
    * COMMAND:命令名/命令行
  
* cat /proc/meminfo:查看内存等系统信息

  * cat /proc/meminfo|grep MemTotal:查看内存,以kb显示

* cat /proc/cpuinfo:查看cpu信息

* bg:将当前脚本转换为后台运行

* fg:挂起程序

* jobs:查看所有在后台运行的程序,使用kill %pid关闭进程

* ps []:显示瞬间的进程状态,通常使用ps -ef或pa -aux
  * -a:显示所有的用户进程,包括其他用户的
  * -l:长格式显示用户进程
  * -u:显示进程的详细状态
  * -x:显示没有控制终端的进程,即后台进程
  * -w:显示加宽,以便显示更多的信息
  * -e:显示所有进程,包括没有控制终端的进程
  * -f:全格式
  * -r:只显示正在运行的进程
  * 显示的各项的字段的含义
    * USER:进程所属用户
    * UID:用户id
    * PID:进程的PID
    * PPID:父进程,0表示无父进程
    * CPU:进程占用的CPU的百分比
    * C:cpu用于计算执行优先级的因子.数值越大,表明进程是cpu密集型运算,执行优先级会降低;数值越小,表明进程是I/O密集型运算,执行优先级会提高
    * MEM:进程占用的内存百分比
    * VSZ:进程占用的虚拟内存,单位kb
    * RSS:使用了多少物理内存,单位kb
    * TTY:使用的终端
    * STIME:进程启动的诗句
    * STAT:进程的状态
      * S:休眠
      * s:该进程是回话的先导进程
      * N:进程拥有比普通优先级更低的优先级
      * R:正在运行
      * D:短期等待
      * Z:僵死进程
      * T:被跟踪或者被停止
    * START:进程启动时间
    * TIME:占用CPU的总时间
    * COMMAND:启动命令
    * CMD:启动进程所用的命令的参数

* pstree:显示进程状态树

* strace:跟踪一个进程的系统调用情况

* ltrace:跟踪进程调用库函数的情况

* vmstat:报告虚拟内存统计情况

* ss:查看系统当前进程相关信息

* lsof []:查看系统中正在运行的程序信息

  * -i :3306:查看3306端口是否运行,注意端口前面有个冒号

* netstat -anp:查看系统当前进程相关信息,包括连接,数据包传递等

  * -tunlp:查看监听的端口

* sync:将内存的数据同步到磁盘中

* who|w:显示在线登录用户详情

* whoami:显示用户自己的角色身份

* $PATH:显示系统环境变量

* $UID:查看当前运行命令对象的uid,只有root用户的uid是0,其他都大于0

* $PWD:当前所在地址的绝对路径

* $USER:当前登录的用户名

* $HOME:当前登录用户的根目录

* $IFS:默认分割符,在awk等里面会用到,包括空格,换行,制表符

* &:该符号不仅可以用来做运算连接符,还可以作为程序后台启动的标识,用在结尾

  ```shell
  java -jar test.jar & # 表示程序将在后台运行,即使关闭终端界面,仍可正常运行
  ```

* nohup:同&在运行程序时的作用,不过nohup用在运行命令的开头,通常和&一起用.同时nohup运行时还会在所运行的程序目录中生成一个nohup.out的日志文件.注意:运行了nohup之后需要再按一次任意键,若直接断开终端,程序不会运行

  ```shell
  nohup java -jar test.jar & # 程序将在后台运行,即使关闭终端界面,仍可正常运行
  ```

* /etc/secutiry/limits.conf:修改单个文件能打开的最大句柄数

  ```shell
  * hard nofile 1000000 # *表示当前用户,nofile表示文件句柄,数字表最大句柄数
  * soft nofile 1000000
  ```

* /etc/sysctl.conf:修改全局文件句柄限制,limits.conf的数量无法超过sysctl.conf

  ```shell
  fs.file-max=1000000 # 在该文件的最后一行加上即可
  # 保存之后退出执行如下命令
  sysctl -p
  ```




### ulimit



* -a:显示目前资源限制的设定
* -u:用户最多可开启的程序数目,大致计算公式为:`内存大小(kb)/128(kb)`



### 修改线程数大小限制



* /etc/security/limits.conf:修改该文件可修改默认打开的最大文件句柄数

  ```
  # 用户,硬件/软件,文件,最大数量
  root soft nofile 65535
  root hard nofile 65535
  * soft nofile 65535
  * hard nofile 65535
  ```

* 修改完后重启生效

* /etc/security/limits.d/20-nproc.conf:修改该问价可修改可创建的最大线程数

  ```
  *          soft    nproc     4096
  root       soft    nproc     unlimited
  ```

* 修改完后重启生效



## 软件安装

### yum

* yum -y install xxx:下载安装软件,需要联网,-y表示不需要确认,直接安装
* yum install --downloadonly[=/app]:只下载文件,不安装;指定下载的地址
* yum list:列出可以安装的软件列表
* yum list installed:列出已经安装了的软件
* yum info xxx:查看软件包信息
* yum check-update xxx:检测软件是否需要更新.若有新的更新,则直接更新
* yum update xxx:更新某个软件
* yum remove xxx:卸载软件
* yum search xxx:搜索软件
* yum clean packages:清理软件缓存



### yum源

* /etc/yum.repos.d:该目录下存放所有的yum源文件

* 若官方源不好用,可以更新为国内的源

* 下载国内源文件:curl -o(欧) 自定义下载到本地的源文件名 源文件地址

  ```shell
  curl -o aliyun.repo http://mirrors.aliyun.com/repo/Centos-7.repo
  ```

* 将下载的源文件移动到/etc/yum.repo.d中

* 将原来的源文件删除或备份到其他目录中,所有以repo结尾的文件都是

* 安装新的yum源

  ```shell
  yum clean all
  yum makechahe
  ```



### 制作本地yum

* 将centos的iso镜像挂载到指定目录,此处例子为/mnt/centos

* 此处挂载为临时挂载,服务器重启之后挂载就消息,需要在/etc/fstab中添加为永久挂载

* 修改本地的仓库地址为自己

* 在仓库文件中新增baseurl和enable

  ```shell
  mount centos.iso /mnt/centos
  vi /etc/yum.repos.d/Centos.repo
  # 注释掉mirros属性,新增baseurl和enable
  # baseurl=file:///mnt/centos
  # enable=1
  ```

  

### 指向内网中的yum

* 修改/etc/yum.repos.d中的仓库文件,删除mirrors,新增baseurl,baseurl的值指向内网中的yum源

  ```shell
  baseurl=http://192.168.1.150/mnt/centos
  ```

* yum clean all && yum repolist && yum makecache



### rpm

* rpm -qa:查看已经安装的rpm包列表
* rpm [] 软件包名:查看软件包信息
  * -q:查看指定的软件包是否安装
  * -q --scripts:查询安装包中包含的脚本
  * -i:查看软件包的信息
  * -l:查看软件安装后的文件列表
  * -c:查看软件安装的配置文件
  * -d:查询软件的帮助文档
  * -f:查看某个文件是那一个软件包中的
  * -e:删除软件包
  * -e --nodeps:强制删除,可能会对其他软件造成影响
* rpm -ivh 软件包名:安装软件包
  * -i:安装软件包
  * -v:提示
  * -h:进度条
* rpm -Uvh 软件包名:升级软件



##  特殊命令

* man command|command --help:查看某个命令的详情,参数等

* which command:查看命令存在的目录

* whatis command:查看命令的描述

* command/content \> filename:重定向符,将命令的结果或其他内容重新定向写入到目的文件,覆盖写.若目的文件不存在,则新建

* command/content \>> filename:重定向符:和>一样,但是是追加写在文件末尾

* command1|command2:管道,多个命令时,后面命令的主体是前面命令的结果,如

  ```shell
  ps -ef|grep java
  ```

* alias []:查看所有别名.若带上参数,则表示给一个系统命令起别名,只是临时的别名.

  若有多个同名的命令,则别名优先使用.若想跳过别名直接使用系统命令,则可以使用\,如

  ```shell
  alias showjava="ps -ef|grep java" # 可以使用showjava显示系统中的java程序
  ls # 使用的是别名
  \ls # 跳过别名,使用的是bin中的系统命令
  ```

* unalias showjava:取消一个别名

* type [] command:查看一个命令的优先选择是普通还是别名

  * -a:查看该命令的所有类型

* -exec command {} \;:该命令可以连接多个命令,并将前一个命令的结果作为后一个命令的处理对象,类似管道命令.因为linux版本的不一致,最后一个\可能不需要,若需要,则\前面的空格也需要,command表示前一个命令的结果需要被command命令处理.如

  > find /log/ -name error.log -exec cat {} \;

* history []:显示所有执行过的linux命令

  * num:显示最近执行过的10条历史命令
  * !num:在每条历史命令前都有一个行号,可直接使用!加上行号执行该命令
  * -c:清除所有的历史命令,可能还需要清除~/.bash_history文件
  * -d num:清除指定的命令历史
  
* wget url:从网络上的某个url中下载资源到当前目录

  * -T n:超时时间,单位s,n为具体时间
  * --spider:爬虫
  * -t n:测试次数,n为具体次数

* curl [] url:从网络上的某个url中下载资源到当前目录,比wget用途更加广泛

  * -o filename:是小写的欧,给从网络上下载的资源自定义命名
  
* ntpdate:日期时间同步,需要安装ntpdate服务

* logrotate:日志切割

* supervisor:进程管理

* HISTCONTROL=ignorespace:固定写法,强制linux不记录敏感历史命令.例如输入数据库密码的时候,需要在整个命令前面加上一个空格即可,该命令将不会记录到历史命令中

* rsync,serync,inotify,union,scync2:数据同步

* xargs:

* $RANDOM:生成随机数,范围0到32767

  ```shell
  # 将random生成的值进行md5加密,生成的字符串为32位
  echo $RANDOM|md5sum
  # 截取md5加密后部分字符串
  echo $RANDOM|md5sum|cut -c 1-8
  ```

* openssl rand -base64 n:利用opsenssl生成随机数,最后的n指定生成的随机数长度

* cat /proc/sys/kernel/random/uuid:uuid随机数



## 常用命令

* ls [] [foldername]:显示当前目录或指定目录中的文件,目录名称
  * -a:显示所有文件,目录,包括隐藏文件以及目录
  
  * -l:列出所有文件以及目录的详细信息,有些版本可以直接用ll
  
    > 文件属性,硬拷贝次数,所属用户,所属用户组,大小,最后一次修改时间年月日,最后一次修改时间时分,名称
    >
    > 硬拷贝次数:没有拷贝时默认是1次,目录的硬拷贝次数是其中文件的综合,包括隐藏文件以及.和..
  
  * -h:将文件大小以k或M显示,默认是字节
  
  * -i:显示文件的唯一标识
  
* stat [] filename:显示指定文件的相关信息,比ls显示更多信息

  * -f:显示文件的大小,类型,id等信息
  * Access:文件最近一次的访问时间,在shell中就是文件的atime
  * Modify:文件最近一次的修改时间,在shell中就是文件的mtime,是内容的修改
  * Change:文件最近一次的权限修改时间,在shell中就是文件的ctime

* tree foldername:显示文件的树形结构

* pwd:显示当前目录的绝对路径

* mkdir [] foldername:新建一个目录
  
  * -p:父目录不存在的情况下,先创建父目录
  * mkdir folder/{a..z}:在folder目录下创建a,b...z多个目录
  * mkdir folder/{1/{2,3},9}:在folder目录下创建1/2,1/3目录,9目录
  
* touch filename1 filename2:在当前目录下创建2个新文件

  * touch file{1..9}:在当前目录创建file1,file2...file9这9个文件
  * touch file{1,9}:在当前目录创建file1,file9这2个文件
  * touch /app/{file1,file2}:在指定目录下创建file1和file2文件

* echo [] content:在终端输出content
  * echo content >> filename:将content追加到filename文件中

  * echo content > filename:将conent覆盖filename文件中的内容

  * -e:将双引号字符中的特殊字符进行转义,如\t,\n等

  * echo {01..10}:将01到10输出在一行,且都是两位

  * 输出颜色:有开头和结尾,结尾都是\E[0m,中间是零,可以通过man console_codes查看
  
    * 黑色:'\E[1;30m'
    * 红色:'\E[1;31m'
    * 绿色:'\E[1;32m'
    * 黄色:'\E[1;33m'
    * 蓝色:'\E[1;34m]'
  * 紫色:'\E[1;35m'
    * 闪烁:'\E[31;5m'
  
    ```shell
    echo -e \E[1;31m esserew\E[0m # 输出红色的esserew
    ```
  
* wc [] filename:统计文本文档的行数,单词数,字符数(包括空格)

  * -c:统计字节数
  * -m:统计字符数
  * -w:统计字数,一个字被定义为由空白,空格,制表符,换行符分割的字符串
  * -l:统计行数

* mv src des:将文件或目录从src移动到des,若src和des在同一个目录下,那就是将src改名为des

* cp [] src des:复制文件或目录src到des.若des中有同名文件,会提示覆盖.强制覆盖可直接使用\cp

  * cp命令默认情况下是已经覆盖了系统的cp命令的别名,可通过alias查看别名信息
  * -r:递归复制目录中的文件以及目录,若不加该参数,且src中有文件时,复制失败
  * -a:保持文件原有属性递归复制
  * -f:若存在同名文件,强制覆盖
  * src:需要进行复制的文件或目录
  * des:复制的目的目录

* cp foldername/{a,b}:将foldername下的a文件复制到该目录下并改名为b文件

* scp [] src username@ip:des:从当前服务器上拷贝文件到另外一台服务器,网络必须通畅

  * -r:循环复制目录中的文件以及目录
  * src:需要拷贝到另外服务器的文件或目录
  * username:目的服务器用户名
  * ip:目的服务器ip
  * des:目的服务器目录或文件

* rsync [] src username@ip:des:主要用于备份和镜像,速度快,避免复制相同内容和支持符号链接的优点

  * -r:递归复制
  * -v:显示复制过程
  * -l:拷贝符号链接
  * src:需要复制到另外服务器的文件或目录
  * username:目的服务器用户名
  * ip:目的服务器ip
  * des:目的服务器目录或文件

* rm [] filename:删除文件或目录,注意该命令不可以写rm /,会将整个linux系统都删除
  * -r:同时删除该目录下的所有文件
  * -f:强制删除文件或目录

* rmdir foldername:删除空目录,只能删除空目录

* tree:显示当前目录的目录树形结构

* ln -s src des:建立软件连,即快捷方式.将src的快捷方式放到des,如

  ```shell
  ln -s /root  /usr/linkRoot
  rm -rf /usr/linkRoot # 注意,删除软件连的时候,软件连后面的/不可以带,否则报错
  ```

* ifconfig:显示网络接口信息

* ping [] ip:测试网络的连通性

  * -c num:表示ping多少次之后断开

* clear:清屏

* kill [] pid:杀死进程,pid为程序的进程号,需要通过ps -ef|grep 程序名查看

  * -9:强制杀死某个进程
  * -1:重启进程

* kill all:结束所有进程

* cut [] file:切割文件内容

  * -fn:选择显示的列,n表示选择分割后的第几列,从1开始
  * -s:不显示没有分隔符的行
  * -d:自定义分隔符

* dirname path:截取一个路径的所有父路径

* basename path:截取一个路径的最后一个文件

  ```shell
  dirname /app/redis/redis.conf # /app/redis
  basename /app/redis/redis.conf # redis.conf
  ```

* seq [] n:输出从1到n的数字

  * -w:输出字符串,如1会输出01



## 文件查看

* file filename:查看文件类型,例如文本文件,目录等
* cat|tac [] filename:显示文本文件的内容
  * -n:显示行号
  * -A:查看隐藏内容,如换行符
* more|less filename:分页显示文本文件内容,less比more好用,且适合查看大文件,读一点看一点.按空格或enter查看下一页内容
* head [] filename:显示文本文件的开头内容, 默认显示10行

  * -c num:查看开头前num个字符
  * -n num:查看开头前num行
* tail/tailf [] filename:显示文件结尾内容,默认显示后10行
  * -c num:查看结尾的前num个字符
  * -n num:查看结尾的前num行
  * -f:动态显示文件中的内容,当文件中的内容增加时,持续输出结果



## 文件,内容查找

* find [src] [] filename/foldername:从指定目录以及子目录中查找指定的文件或目录,若不写src则默认从当前目录查找,filename/foldername可使用*做通配符
  * -name:指定查找的时候只需要从文件或目录的名称中查找
  * -size 2M:查找大小等于2M的文件
  * -size +2M:查找大小大于2M的文件
  * -size -2M:查找大小小于2M的文件
  * -size +2M -size -5M:查找大于2M,小于5M的文件
  * -user username:按照文件所属用户进行查找
  * -perm 777:查找权限为777的文件
  * -type:查找指定文件类型的目标,f表示文件,d表示目录,l表示软链接
  * -mtime -20:查找最近20天内更新过的文件
  * -mtime +20:查找20天以前更新过的文件
  * -a/-o:and和or的缩写,若有多个添加,可以使用-a或-o进行连接
* find ... -exec command {} \;:将find查找的结果用command命令再执行一次,{}表示find命令结果
* locate filename:快速定位查找文件,类似于数据库索引.使用该命令必须定义更新locate数据库,运行updatedb即可
* grep [] content filename:在指定文件中查找content内容
  * -i:忽略大小写
  * -r:递归查询目录中所有符合条件的文件,并显示文件名,若不需要显示文件名,则可加上参数h
  * -w:完全匹配需要查找的字符串的行
  * -c:统计文件中匹配的总数
  * -n:在输出的结果中显示匹配行的行号
  * -v:输出不匹配的行
  * -l:显示匹配的文件名
  * -color:以彩色输出结果



## 打包压缩

* zip [] customename(无扩展名,压缩完成之后默认为.zip后缀)  源文件:将源文件压缩成自定义文件

  * -r:递归压缩目录中的文件

* unzip [] filename.zip:解压文件

  * -d des:指定解压后的目录 

* gzip[] filename customname.tar.gz:压缩或解压缩文件为customname.tar.gz,源文件会被删除

  * -r:递归压缩文件夹中的文件
  * -d:将压缩文件解压,不写代表压缩,压缩后源文件将消失
  * -l:显示每个压缩文件已压缩的大小,未压缩的大小,压缩比,未压缩文件的名字
  * -v:显示每个压缩文件的文件名和压缩比
  * num:用指定数字num调整压缩的速度,-1或false表示最快压缩方法(低压缩比),-9或--best表示最慢压缩(高压缩比),默认为6

* gunzip customname.tar.gz:解压缩文件,源文件会删除

* bzip2 [] filename:压缩或解压缩文件,只能对文件执行,不可对目录执行

  * -d:解压缩
  * -z:压缩
  * num:用指定数字num调整压缩的速度,-1或fast表示最快压缩方法(低压缩比),-9或--best表示最慢压缩(高压缩比),默认为6

* tar [] file.tar[.gz] [filename/foldername]:压缩或解压缩文件或目录,将指定文件或文件夹打包成customname的名字

  * -c:打包文件,应先写打包后的文件名,再写需要打包的文件或目录名

    ```shell
    tar -cvf back.tar filename1 filename2
    ```

  * -x:解压文件

    ```shell
    tar -xvf back.tar
    ```

  * -C des:当解压文件时,将解压后的文件存放到指定目录,指定目录必须先存在,否则报错

    ```shell
    tar -xvf back.tar -C /test
    ```

  * -z:是否使用gzip压缩,如压缩:

    ```shell
    tar -zcvf back.tar.gz filename # 压缩
    tar -zxvf back.tar.gz # 解压
    ```

  * -j:是否使用bzip2压缩,使用同z参数

  * -v:压缩过程中显示详情,显示进度

  * -f:使用文件名,在f之后要立即接文件名.若是压缩文件时可自定义文件名,最好是以tar结尾.f需要放在参数的最后

  * -tf:查看压缩文件中的文件



## 时间日期

* date:显示当前格林威治标准时间
* date "+%Y-%m-%d %H:%M:%S":指定时间输出的格式,月份,分钟的显示和java相反,双引号必加
* date +%F:按yyyy-mm-dd格式输出当前日期
* date +%D:直接显示日期 (mm/dd/yy)
* date -s "yyyy-mm-dd HH:mm:ss":将系统时间改为指定的时间,双引号必须有
* hwclock -w:强制将硬件时间和系统时间同步
* date +%Y:显示当前年份(0000-9999),注意+和date之间有空格
* date +%y:年份的最后两位数字(00-99)
* date +%m:显示当前月份(01-12).注意,和java中的不一样
* date +%d:显示当前是月中的那一天(01-31)
* date +%H:小时(00-23)
* date +%I:小时(01-12)
* date +%k:小时(0-23)
* date +%l:小时(1-12)
* date +%M:分钟(00-59)
* date +%p:显示本地AM或PM
* date +%r:直接显示时间 (12 小时制,格式为 hh:mm:ss [AP]M)
* date +%s:从1970年1月1日00:00:00 UTC到目前为止的秒数
* date +%S:秒(00-60)
* date +%T:直接显示时间 (24 小时制)
* date +%X:相当于 %H:%M:%S
* date +%Z:显示时区
* date +%a:星期几 (Sun-Sat)
* date +%A:星期几 (Sunday-Saturday)
* date +%b:月份 (Jan-Dec)
* date +%B:月份 (January-December)
* date +%c:直接显示日期与时间
* date +%h:同 %b
* date +%j:一年中的第几天 (001-366)
* date +%U:一年中的第几周 (00-53) (以 Sunday 为一周的第一天的情形)
* date +%w:一周中的第几天 (0-6)
* date +%W:一年中的第几周 (00-53) (以 Monday 为一周的第一天的情形)
* date +%x:直接显示日期 (mm/dd/yy)
* cal:显示当前月的日历
* cal year:year表示一个完整的年份,显示整年的日历



## SED

* sed [] "cmd" file/input:查找或替换文件中的内容.默认情况下,并不会对源文件进行修改,只显示结果
* -n:取消默认输出,只显示匹配的行
* -e:将操作结果输出到控制台,不会改变源文件,也可以同时使用多个sed表达式
* -i:和-e相反,是直接在源文件上进行修改替换
  * -i[suffix]:添加一个后缀,此时会将文件源文件先复制,后修改
* -r:使用扩展正则表达式
* 多条sed语句可以用分号隔开,前面的结果一次给后面的表达式进行处理

```shell
sed "2,$d;s#11#22#" file # 先删除第2行后的内容,得到的结果再将11替换成22
```

* cmd:需要操作的内容,其中也可以使用命令
  * d:删除符合条件的行,条件可以是数字,也可以是正则

  ```shell
  # 数字
  sed "d" file # 删除所有内容
  sed "nd" file # 删除指定第n行,n从1开始
  sed "2,4d" file # 删除第2行到底4行,包括第2,第4行
  sed "2,+10d" file # 删除从第2行开始后面的10行,包含第2和第12行
  sed "2~3d" file # 从第2行开始删除公差为3的行,包含第2行,即删除2,5,8...
  sed "n,$d" file # 删除从第n行到末尾所有行,包含第n行
  # 正则
  sed "/abc/d" file; # 删除文件中所有带abc的行
  sed "/abc\|def/d" file # 删除含有abc或def的行
  sed "/abc/d;/def/d" file # 删除含有abc或def的行
  # 该方式可能会出现意想不到的结果.会匹配第一个含abc的行直到第一个含def的行,之后将继续匹配
  # 若再次匹配到含abc的行,则会匹配第2个含def的行,若到末尾都没有含def的行,一直输出到末尾
  # 若含有第2组含abc,def的行,则继续匹配,直到末尾
  sed "/abc/,/def/d" file # 删除从第一个包含abc行到第一个包含def的行中间所有的行
  sed "/abc/,$d" file # 删除从第一个包含abc的行到末尾的所有行
  # 混合使用
  sed "2,/abc/d" file # 删除从第2行开始到第一个包含abc的行
  # 删除符合条件的行,并将处理后的结果保存到指定文件中
  sed -e "/abc/d" file > file1
  ```

  * p:输出指定内容,需要和-n一起使用,否则会同不符合的行一起输出,造成复制的效果.语法同d

  ```shell
  sed "p" file # 重复输出所有的行
  sed "2p" file # 将第2行输出,若不配置-n参数,会将第2行输出2次
  sed -n "2p" file # 只输出符合条件的行
  ```

  * a\content:在符合条件的行后面添加新行,content为内容,\也可以换成空格,\n用于换行,语法同d

  ```shell
  # 语法和删除的基本一样,可以用数字,也可以用正则
  sed "2a qwerty" file # 在第2行后面添加qwerty
  sed "2a\qwerty" file # 在第2行后面添加qwerty
  sed "2a qwerty\nasdfgh" file # 在第2行后面添加qwerty,换行再添加asdfgh
  ```

  * i\content:同a命令,但是是在指定行之前插入新的行
  * c\content:替换符合条件的行,语法同a
  * r file:将指定的文件的内容添加到符合条件的行处
  * w file:将符合条件的行另存至指定的文件中,语法同d

  ```shell
  sed "w output.txt" file # 直接将file文件另存为output.txt
  sed "2,5w output.txt" file # 将2到5行的内容复制到output.txt中
  sed "s###g w output.txt" file # 将符合条件的行复制到output.txt中
  ```

  * e:将最终输出的内容作为bash命令执行
  * \l:将紧随其后的第一个字符转成小写处理
  * \L:将后面所有的字符全部小写处理
  * \u:将紧随其后的第一个字符转成大写处理
  * \U:将后面所有的字符全部大写处理
  * \E:和\U,\L一起使用,关闭\U,\L的功能

  ```shell
  sed -n "s#tfdsfds#ss\lAEWR#p" # 输出ssaEWR,a小写
  sed -n "s#tfdsfds#ss\LAEWR#p" # 输出ssaewr
  sed -n "s#tfdsfds#ss\Laewr#p" # 输出ssAewr
  sed -n "s#tfdsfds#ss\LAEWR#p" # 输出ssAEWR
  ```

  * N:不清除当前模式空间,然后读入下一行,以\n分隔两行,默认是第下一行就清除上一行,N相当于同时读2行

  ```shell
  # file中有2行ddddd,eeeeee
  sed "N;s#\n#=#" file # ddddd\neeeeee->ddddd=eeeeee
  ```

  * \[n\]s/pattern/content/\[m\][gi]:查找并替换,默认只替换第一个被匹配的内容,可以指定行列
    * n:从第n行开始查找,默认从第一行,最大只能为512
    * g:行内全局替换
    * m:从第m列开始查找,以空格分列,默认从第一列
    * i:忽略大小写
    * /,#,@:当被查找或替换的内容中有/时,可以将/替换成#或@
    * 在pattern中的(),{}需要用\进行转义,[]不需要,每一对()都是一个分组,可以在替换中使用\1...对应

  ```shell
  # 文件中有一行:id:100:qwerty,将数字替换成555
  # 将其他不需要替换的也要写进去,很鸡肋
  sed "s/id:[0-9]\{0,\}:qwerty/id:555:qwerty/"
  # 利用反向引用,每一对()都是一个引用,在后面的替换中可以使用\1,\2...等直接引用,最多只能9个
  sed "s#\(id:\)[0-9]\{0,\}\(:qwerty\)#\1555\2#" file
  # 若555是一个传递的值,也可以直接在表达式中使用
  num=555
  sed "s/\(id:\)[0-9]\{0,\}\(:qwerty\)/\1$num\2/"
  # 或者sed "s/\(id:\)[0-9]\{0,\}\(:qwerty\)/\1${num}\2/"
  # 一个符合网卡中ip的sed正则,将ip的最后一位换掉
  sed "s/\(IPADDR=\(\([0-9]\|[1-9][0-9]\|1[0-9][0-9]\|2[0-4][0-9]\|25[0-5]\)\.\)\{3\}\).*/\1555/" ifcfg-eth0
  sed "ns///mg" file # 替换指定字符串,同时指定是第n行第m列
  # 排序,sort -nr可以换排序方式
  sed 's/abc/d/g' file|sort
  # 去重计数uniq -c
  sed 's/abc/d/g' file|sort|uniq -c
  ```

* sed -f test.sed file:将多条符合sed语法的表达式写到test.sed文件中,sed命令会依次执行其中的命令

```shell
sed "2,$d;s#11#22#" file # 先删除第2行后的内容,得到的结果再将11替换成22
```

* -n:取消默认输出,只显示匹配的行



## AWK

* 一个文本分析工具,可以将文件中的内容逐行读入,空格和制表符为默认分隔符将每行进行切分,切分的部分再进行各种额外的处理.也可以和sed一样查找符合条件的行
* 支持自定义分隔符,支持正则,支持自定义变量,数组,a[1],a[tom],map(key),支持内置变量
* argc:命令行参数个数
* argv:命令行参数排列
* environ:支持队列中系统环境变量的使用
* FILENAME:awk浏览的文件名
* FNR:浏览文件的记录数
* FS:设置输入域分隔符,等价于命令行-F
* NF:浏览记录的域个数,即分割后的长度个数
* NR:已读的记录数,即行号
* OFS:输出域分隔符
* ORS:输出记录分隔符
* RS:控制记录分隔符
* 支持函数:print,split,substr,sub,gsub等
* 支持流程控制:if,while,do/while,for等
* awk [] '{}' file1,file2...

  * -F '':以指定分隔符分割文件中的行,默认是空格,空白,制表符,也可以自定义
  * '{}':匿名函数,可以在里面写复杂的方法,通常直接print输出
  * $n:代表分割后的列,n从1开始,可以在匿名函数中直接使用,$0表示被分割的整行

```shell
awk -F ':' '{print $1","$2}' passwd # 以冒号分割并用逗号把分割的1和2列连接起来
awk '/root/  {print $0}' passwd # 查找password中带有root的行并显示,查找规则同sed
awk '/^root/ {print $1}' passwd # 查看以root开头的行并展示分隔后的第1个字符
awk -F ':' '{print NR","NF","$0}' passwd # 输出行号和分割后列的个数
```



## UNIQ&SORT

* uniq [] file:对文件中的内容去重,但默认只去重相邻的两行内容,输出结果,但不改变文件
  * -c:对重复的行进行计数,但是只计算相邻的行
  * -d:只显示重复的行,但是只计算相邻的行
  * -u:只显示出现一次的行,但是只计算相邻的行
  * -i:忽略大小写
  * -s n:忽略前n个字符
  * -w n:只比较前n个字符
* sort [] file:排序文件中的内容,默认升序

  * -n:按数值排序
  * -r:按数字倒序
  * -t:自定义分隔符
  * +n:配合-t使用,可以对指定的列进行排序
  * -k:选择排序列,配合-t一起使用,比+n更细腻,可指定第几列的指定索引个数的字符
  * -u:去重
  * -f:忽略大小写
  * -M:排序月份
  * -d:只考虑空格,字符和数字
* sort结合uniq进行排序去重

```shell
sort file|uniq # 排序,去重
sort file|uniq -c # 排序,去重,计数
sort -t " " -k2 file # 按空格分隔文件行,用第2列进行排序
sort -t " " -k2.1,2.3 # 按空格分隔文件行,用第2列的第一个字符到第2列的第3个字符进行排序
```



## 关机命令

* sync:将内存中的数据保存到磁盘上,在关机之前最好执行一下
* shutdown []:系统关机
  * -r now:关机后立即重启
  * -h [num]:关机后不重新启动,num表示多长时间之后关机,单位分钟,计划关机
  * -h now:立即关机
* init num:当num为0时表示系统关机,num为6表示重启
* halt:关机后关闭电源
* reboot:重启
* logout:注销用户



## 用户,组

### 用户操作

* useradd/adduser [] username:添加用户相关信息
  * -u uid:指定uid,root用户的uid固定为0
  * -p passwd:设置密码
  * -g groupname:设置默认分组,若显示调用的分组不存在,则会报错.若不指定分组,默认会创建一个同名的用户组,并将用户归到新建的用户组中
  * -G groupname1 groupname2:将用户同时指定到多个其他附加组
  * -M:创建一个没有主目录的用户,类似于mysql之类,无法正常登陆linux
  * -s shellcmd:设置shell命令
    * useradd test -s /sbin/nologin:新建一个不可登陆的账户test,这种账户可以参考mysql帐号
  * -N:只创建用户,不创建组
  * -r:创建系统用户
  * -d foldername:设置用户登录时的根目录,该目录不可提前创建,否则无法登录.若不设置,默认为在home下新建同名的目录
  * -m:新增用户时直接给用户创建同名的文件夹
  * 新添加的用户无法使用su获得root权限,需要添加adm组和sudo组
  * -c comment:创建用户时带上注释
  * -e date:创建一个具有过期时间的帐号,date为一个完整的时间,可以不带时分秒
  * -f day:创建一个具有过期时间的密码,默认单位为日
* usermod [] username:修改用户相关信息
  * -u uid:修改uid
  * -p passwd:修改密码
  * -g groupname:修改分组
  * -s shellcmd:修改shell命令
  * -d foldername:修改用户登录时的根目录
  * -l newusername:修改登录名,新的登录名要放旧用户名的前面
  * -L:锁定用户账号密码
  * -U:解锁用户账号
* userdel [] username:删除用户账户
* -r:删除账号的同时删除目录
* groupadd [] groupname:添加新的用户组
* -g:指定组gid
* groupmod -g [] groupname:更改组的gid
* -n newgroupname groupname:更改组账户名
* groupdel groupname:删除用户组
* groups username:查看用户在那个用户组,多个用户组之间用空格隔开
* passwd username:更新或设置用户密码
* passwd [] username:用户操作
* -l:锁定用户账户
	* -u:解锁用户账户
	* -d:删除账户口令
* gpasswd -a username groupname:将指定用户添加到指定组
* -d username groupname:将用户从指定组中删除
	* -A username groupname:将用户指定为组的管理员
* id username:查看用户信息,若不存在,提示无此用户
* echo 'password'|passwd --stdin username:非交互式修改密码



### 用户权限

* su [- username]:从当前用户切换到另外一个用户.若是从高权限用户切换到低权限用户,不需要输入密码,反之则需要

* sudo command:当前用户没有某个命令的执行权限时,可以使用sudo来执行该命令,但是该用户需要使用visudo进行提权操作

* visudo:对某个用户在进行sudo操作时,是否有权限可以提权.需要添加如下:

  > %username ALL=(ALL) ALL:这是给username所有的权限,实际情况可根据需求修改



## 文件权限

​	ls -l查看文件时会显示文件的权限,固定10位:第一位字符表示该文件是文件或目录或其他特殊属性.剩下的9位,3位为一组,分别表示文件所属用户的读写执行权限,所属用户组的读写执行权限,其他组的读写执行权限.



### 普通权限

* 当使用ls -l的时候会显示文件详情,第一组为权限,固定10位字符,分别对应如下:

  * 文件类型:第一位是文件类型
    * d:表示目录
    * -:表示普通文件
    * l:表示软链接
    * b:块文件,硬盘
    * c:字符设备,如键盘,鼠标等
    * s:套接字文件
    * p:管道文件
  * rwx或-:2,3,4位表示当前所属用户的读写执行权限,-表示没有
  * rwx或-:5,6,7位表示当前所属用户组的读写执行权限,-表示没有
  * rwx或-:5,6,7位表示其他用户组的读写执行权限,-表示没有
* chmod [] filename|foldername:给文件或目录赋权限,可以直接用rwx,也可以用数字,r->4,w->2,x->1,用户或组或其他每一种最大是7
  * u:给所属用户设置权限,chmod u=rwx,g=rwx,o=r
  * g:给所属用户组设置权限
  * o:给其他用户设置权限
  * a:给所有用户设置权限
  * +:加上rwx中的某一个权限,chmod o+x
  * -:删除rwx中的某一个权限
  * -R:递归给目录中的文件加上相同操作
* chown [] username filename/foldername:改变文件或目录的所有者
  * -R:递归更改目录下的文件所属用户或所属用户组
  * username:更改后文件或目录所属的用户,用户改变了,但用户组不会变
  * username:groupname:更改后所属的用户以及用户组,注意中间的冒号需要加上,是分隔符
* chgrp [] groupname filename|foldername:更改文件或目录的组
  * -R:递归更改目录下的文件所属用户组
  * groupname:更改后文件或目录所属的用户组



### 特殊权限

* 一般给权限的时候只有3位,但是也有时候是4位,如chmod 755和chmod 4755
  * 多个一个权限位4表示其他用户在执行文件时,和该文件所属用户的权限相同
  * 4:只对文件设置,使该文件执行时具有和文件所有者相同的权限,同时权限位的执行权限变为s,如4777->rwsrwxrwx
  * 2:只对目录设置,使用户在该目录下创建的文件和该目录的所属用户组是同一个组,权限如下2777->rwxrwsrwt
  * 1:防删除位,删除权限由该用户所在组决定,权限如下1777->rwxrwxrwt
* chattr [] filename:给文件加上特殊权限
  * +i:防止系统中某个关键文件被修改
  * +a:只能往文件中追加,不能删除



## 换行符

> linux和windows的换行符不一样,可以用-A参数查看,从windows上的文件拿到linux上时需要将换行符进行转换,可以使用dos2linux.该依赖若没有,可以先安装

* dos2linux file:将从windows转到linux上换行符发生了改变的文件换行符转为linux



## 快捷键

* ctrl+a:光标移动到开头
* ctrl+e:光标移动到末尾
* ctrl+u:删除整行
* ctrl+w:删除整行



# 文件编辑

> 当打开或新建一个文件时,初始为命令模式,按i进入插入编辑模式,按a进入追加编辑模式,按ESC再次进入命令模式

* vi [] filename:打开文件,若该文件不存在,则新建,并将光标置于第一行开头
  * +n:打开文件,并将光标置于第n行开头
  * +:打开文件,并将光标置于最后一行开头
  * +/pattern:打开文件,并将光标置于第一个匹配pattern的字符串处
  * -r:置于上次正用vi编辑时发生故障的位置,恢复文件



## 命令模式



### 光标移动

* 上下左右箭头也可以移动光标
* h:光标左移一个字符,不换行移动
* H:光标移动至屏幕顶行
* l:光标右移一个字符,不换行移动
* L:光标移动至屏幕最后行
* M:光标移动屏幕中间
* space:光标右移一个字符
* backspace:光标左移一个字符
* k|ctrl+p:光标上移一行
* j|ctrl+n:光标下移一行
* enter:光标下移一行
* w:光标右移至下一个字符串开头,以空格和换行为标识隔开2个字符串
* b:类似w,只不过是从左往右移
* e:类似w,只不过是移动到每个字符串末尾
* ):光标移动到字符串末尾,2行之间必须有空行,否则直接移动到文件末尾
* (:光标移动到字符串开头,2行之间必须有空行,否则直接移动到文件末尾
* gg:光标移动到第一行的开头
* nG:光标移动到第n行开头,G必须大写
* n+:光标下移n行
* n-:光标上移n行
* n$:光标移动到第n行末尾
* 0:注意是零,光标移动到当前行开头
* $:光标移动到当前行末尾
* ctrl+u:向文件开头翻半屏
* ctrl+b:向文件开头翻一页
* ctrl+d:向文件末尾翻半屏
* ctrl+f:向文件末尾翻一页



### 搜索

* /pattern:从光标开始向文件末尾搜索符合表达式的字符,可以使用正则
* /^pattern:搜索以pattern开头的行
* /pattern$:搜索以pattern结尾的行
* ?pattern:从光标开始向文件开头搜索符合表达式的字符
* n:搜索的结果往下查找
* N:搜索的结果往上查找



###  替换

* r:替换当前字符,一次只能替换一个,之后仍然是命令模式
* R:替换当前字符及后面的字符,知道按ESC键退出
* :s/p1/p2/g:将当前行中所有p1都用p2替代,若不加g则只替换每行的第一个p1
* :n1,n2 s/p1/p2/g:将第n1至n2行所有p1都用p2替换
* :,n2 s/p1/p2/g:将当前行至n2行所有p1都用p2替换
* :n,$ s/p1/p2:将第n行到末尾行的每行的第一个p1换成p2
* :% s/p1/p2:将全文中每行的第一个p1换成p2
* :g/p1/s/p2/g:将文件中所有p1都用p2替换
* 若被查找或者被替换的内容中有/,可以将/替换成#,如:% s#p1#p2
* :n1,n2 s/^p1/p2:将n1到n2行开头为p1的替换成p2
* :n1,n2 s/.*/p1&:将n1到n2行整行前都添加p1,.\*表示整行,&表示将需要替换的内容,此处就是.\*



### 删除

* ndw:删除光标处开始以及后面的n-1个以空格或换行隔开的字符串
* d0:后面是零,删除当前行光标前至开头的所有字符
* d$:删除当前行光标后至末尾的所有字符
* :n1,n2 d:将n1行到n2行之间的内容删除
* ndd:删除包括当前行在内以及下面的n行,n不写默认删除当前行
* dgg:删除光标行到第一行,包括光标行
* dG:删除光标行到末尾行,包括光标行
* D:从光标位置删除到行尾
* x:删除一个字符,从光标开始,一个一个向后删除
* X:删除一个字符,从光标开始,一个一个向前删除



### 复制粘贴

* yy:复制当前行到寄存器中,多次使用只会复制最近一次的内容
* nyy:从光标所在行往下复制n行,包括光标行
* ygg:从光标所在行开始一直复制到第一行,包括光标行
* yG:从光标所在行一直复制到最后一行,包括光标行
* p:粘贴寄存器汇总复制的内容到光标下一行
* :n1,n2 co n3:将n1行到n2行之间的内容拷贝到n3行下
* :n1,n2 m n3:将n1行到n2行之间的内容移动到第n3行下
* "?nyy:将当前行以及下n行的的把内容保存到寄存器?中,?为一个字符,n为数字
* "?nyw:将当前行及其下n个字保存到寄存器?中,其中?为一个字母,n为一个数字
* "?nyl:将当前行及其下n个字符保存到寄存器?中,其中?为一个字母,n为一个数字 
* "?p:取出寄存器?中的内容并将其放到光标位置处.这里?可以是一个字母,也可以是一个数字



### 其他

* u:撤销
* :set nu:设置临时行号,关闭后就没有了,若要设置永久的,可以在/etc/vimrc中添加set nu



## 编辑模式

​	在命令模式下输入下列字符将进入指定输入模式,按ESC退出编辑模式

* i:进入编辑模式,光标所在位置前开始添加字符
* I:进入编辑模式,大写的i,光标移动到当前行第一个非空白字符前开始添加字符
* a:进入编辑模式,光标所在位置后开始添加字符
* A:进入编辑模式,光标移动到当前行末尾
* o:进入编辑模式,小写的英文字母欧,在当前行下新开一行开始编辑
* O:进入编辑模式,大写的英文字母欧,在当前行上新开一行开始编辑
* s:从光标位置开始,以输入的字符替代当前位置,知道按esc退出
* r:替换光标所在位置的字符,只替换一个
* R:从光标位置开始替换字符,直到按ESC结束
* nS:从当前光标行开始,删除包括当前行开始的n行,之后进入编辑模式



## 扩展模式

> 需要按:(冒号)进入到扩展模式,一般保存等操作需要进入该模式

* :w:保存当前文件
* :e filename:打开文件进行编辑
* :x :保存当前文件并退出
* :q:退出vi
* :q!:不保存文件并强制退出vi
* :wq:保存并退出
* :!command:执行shell命令command
* :n1,n2 w!command:将文件中n1行至n2行内容作为command的输入并执行.若不指定n1,n2,则整个文件内容都做为输入
* :r!command:将命令command的输出结果放到当前行



## 其他语法

* 获得字符创长度:#var,输出字符串长度:echo ${#var}

* xfs_repair -L /dev/dm-0:如系统崩溃,实在没有其他办法,可在安全模式下使用该命令,只对xfs文件系统有效



# 数组

* 数组定义:arr=(1 3 5),中间是空格,数组索引从0开始
* #arr[*]/#arr[@]:输出数组长度
* arr[*]/arr[@]:输出数组中所有元素
* arr[index]=x:向数组中索引为index的位置加入元素
* unset arr[index]:删除数组中索引为index的元素



# 防火墙

* systemctl start firewalld.service/service firewalld start::开启防火墙.centos7安装之后默认是开启的
* systemctl stop firewalld.service/service firewalld stop:停止防火墙,但是重启之后仍会打开
* systemctl disable firewalld.service/service firewalld disable:彻底关闭防火墙,重启之后也不会打开防火墙
* systemctl status firewalld.service/service firewalld status:查看防火墙状态



## Firewall-cmd

* firewall-cmd []:关于防火墙的操作,以下参数全部都是该命名的参数

  * --list-ports:查看已经打开的端口
  * --state:查看防火墙状态
  * --get-zones:查看有多少种区域,默认是public
  * --list-all-zone:查看所有区域中的详细信息
  * --permanent:永久的配置
  * [--zone=public] --list-services:查看已经开启的服务,此处已经默认是public域的服务
  * [--zone=public] --query-service=ssh:查询默认域中是否开启了ssh服务,ssh可以是其他服务,从上一个命令中查看
  * [--zone=public] --remove-service=ssh:从默认域中移除某个服务
  * [--zone=public] --add-service=ssh:向默认域中添加某个服务
  * --zone=public --add-port=80/tcp --permanent:centos7打开某个端口允许外部访问
  * --reload:centos7重启防火墙
  * --list-rich-rules:查看封禁ip结果
  * --permanent --add/remove-rich-rule="rule family='ipv4'" source address='xx.xx.xx.xx' reject/accept:添加/删除,封禁/解禁某个ip,需要重启防火墙
  * --permanent --add/remove-rich-rule="rule family='ipv4' source  address='xx.xx.xx.0/125' reject/accept:添加/删除,封禁/解禁ip段,需要重启防火墙
  * --permanent --add/remove-rich-rule="rule family=ipv4 source address=xx.xx.xx.xx port port=80  protocol=tcp  accept/reject":添加/删除,允许/拒绝单个ip的某个端口,需要重启防火墙



## Ipset

* 利用ipset封禁ip,更好的管理封禁的ip
* --permanent --zone=public --new-ipset=blacklist --type=hash:net:创建一个名为blacklist的库,成功后会在/etc/firewalld/ipsets下生成blacklist.xml文件
* --permanent --zone=public --ipset=blacklist --add-entry=xx.x.x.xx:添加ip
* --permanent --zone=public --ipset=blacklist --add-entry=xx.xx.xx.0/24:添加ip段
* --permanent --zone=public --ipset=blacklist --remove-entry=xx.x.x.xx:删除ip
* --permanent --zone=public --ipset=blacklist --remove-entry=xx.xx.xx.0/24:删除ip段
* --permanent --zone=public --add-rich-rule='rule source ipset=blacklist drop':封禁名为blacklist的ipset,这样就只用管理ipset即可
* --reload:重启防火墙生效
* --list-all:查看当前防火墙状态
* --list-rich-rules:查看屏蔽结果
* ipset list blacklist:查看当前ipset封禁了哪些ip
* --permanent --zone=public --new-ipset-from-file=/path/blacklist.xml:直接将已经生成好的封禁规则导入到ipset的blacklist中,需要先创建blacklist的ipset



## 管理端口

* --zone=dmz --list-ports:列出dmz 级别的被允许的进入端口
* --zone=dmz --add-port=8080/tcp:允许tcp端口8080至dmz级别
* --zone=public --add-port=5060-5059/udp --permanent:允许某范围的udp端口至public级别,并永久生效



## 网卡接口

* --zone=public --list-interfaces:列出public zone所有网卡
* --zone=public --permanent --add-interface=eth0:将eth0永久添加至public zone 
* --zone=work --permanent --change-interface=eth0:eth0存在与public zone,将该网卡添加至work zone,并将之从public zone中删除
* --zone=public --permanent --remove-interface=eth0:永久删除public zone中的eth0



## 管理服务

* --zone=work --add-service=smtp:添加smtp服务至work zone
* --zone=work --remove-service=smtp:移除work zone中的smtp服务



## Ip地址伪装

* 配置external zone中的ip地址伪装
* --zone=external --query-masquerade:查看
* --zone=external --add-masquerade:打开伪装
* --zone=external --remove-masquerade:关闭伪装



## 端口转发

* --zone=public --add-masquerade:要打开端口转发,则需要先打开伪装
* --zone=public--add-forward-port=port=22:proto=tcp:toport=3753:转发tcp 22端口至3753
* --zone=public--add-forward-port=port=22:proto=tcp:toaddr=192.168.1.100:转发22端口数据至另一个ip的相同端口上
* --zone=public --add-forward-port=port=22:proto=tcp:toport=2055:toaddr=192.168.1.100:转发22端口数据至另一 ip的2055端口上



## Icmp

* --get-icmptypes
  destination-unreachable echo-reply echo-request parameter-problem redirect router-advertisement router-solicitation source-quench time-exceeded:查看所有支持的icmp类型
* --zone=public --list-icmp-blocks:列出icmp列表
* --zone=public --add-icmp-block=echo-request [--timeout=seconds]:添加echo-request屏蔽
* --zone=public --remove-icmp-block=echo-reply:移除echo-reply屏蔽



## 双网卡

* 双网卡内网网卡不受防火墙限制
* firewall-cmd --permanent --zone=public --add-interface=eth1:公网网卡–zone=public默认区域
* firewall-cmd --permanent --zone=public --add-interface=eth2:内网网卡–zone=trusted是受信任区域 可接受所有的网络连接



# SSH

## 安装使用

1. 若linux上没有安装ssh,可安装

```shell
yum install openssh-server
```

2. 启动ssh

```shell
service sshd start
```

3. 设置开机运行

```shell
chkconfig sshd on
```



## 远程登录linux

### 直接SSH

```shell
ssh 帐号@ip ->如: ssh root@192.168.1.141
```



### SSH config

1. 进入~/.ssh目录,~是登录用户的根目录,该目录下有个config文件,若没有,则新建即可

2. config文件中可输入5个参数,如下

   1. Host:利用ssh登录时的别名,可自定义

   2. HostName:登录到当前linux的其他机器的ip地址

   3. Port:登录到当前linux的其他机器的端口,linux默认22

      > 若要修改默认的监听端口22为其他端口,可修改/etc/ssh/sshd_config的Port参数.Port参数可同时存在多个,表示同时可监听多个端口,修改之后需要重启sshd
      >
      > service sshd restart

   4. User:登录到当前linux的其他机器登录时所用的用户名

   5. IdentifyFile:登录时对应用户名的密码文件路径

```shell
# 可以写多个host,每个host都代表一个用户
Host "admin"
    HostName 192.168.1.101
    Port 22
    User root
    IdentifyFile ~/.ssh/id_rsa.pub
# 其他机器登录到当前机器
ssh admin
```



### SSH免密

1. ssh key使用非对称加密生成公钥和私钥,存放在~/.ssh目录下

2. 公钥对外开放,放在服务器的~/.ssh/authorized_keys

3. A机器生成公钥,私钥

   ```shell
   ssh-keygen -t rsa # rsa加密,ssh-keygen -t dsa,dsa加密
   # 回车后需要输入一个密钥的名称,可自定义,如admin
   # 输入完名称之后将会输入密码,完成之后就会在~/.ssh下生成admin和admin_pub文件
   ```

4. B机器若要免登陆到A机器,需要将A机器生成的admin_pub中的内容复制到B机器的~/.ssh/authorized_keys中,若该文件不存在,可新建该文件

5. 将公钥复制到B机器中之后,仍需要在B机器中加入登陆A时的私钥名称

   ```shell
   # 在B机器上执行
   ssh-add ~/.ssh/admin # 该步骤完成之后即可直接使用ssh免密登陆A机器
   ```




# 网络

## 网络配置

* /etc/sysconfig/network-scripts/ifcfg-xxxx:主要的网络配置文件,该文件的数量根据真实情况的网卡数量而定
* /etc/resolv.conf:配置可访问网络DNS的文件
  * 格式为nameserver ip地址,nameserver为固定值,可以写多个,最多生效3个
  * 该文件可自行编写,也可以通过在ifcfg-xxx文件中写DNS后,自动映射到当前文件
  * 若ifcfg-xxx中不写任何DNS,那么resolv中必须添加网关地址
* /etc/hosts:ip地址和域名之间的映射,格式为ip 域名,可以写多个,一行一个
* /etc/sysconfig/network:可修改其中hostname的信息,修改后需要重启才生效
* /sbin/route add -host 10.0.10.8 dev eth2:新增一条路由到指定网址,同时指定网卡



## 常用命令

* service network restart:重启网络
* systemctl restart network:重启网络
* service network start:启动网络
* systemctl start network:启动网络
* ifconfig eth0 up/down:启用/禁用指定网卡
* ping [] xx.xx.xx.xx:ping某个ip是否正常,ping可能被禁
  * -W n:等待超过某个时间中断,n单位为秒
  * -c n:只ping多少次,n表示次数



# 定时任务(Cron)

## 概述

* 定时任务表达式:* * * * * command
  * 5个星号分别表示:分钟(minute) 小时(hour) 日(day) 月(month) 周(week)
  * miunte:可以是0到59之间的任意整数
  * hour:取值范围为0到23之间的任意整数
  * day:取值范围为1到31之间的任意整数
  * month:取值范围为1到12之间的任意整数
  * week:取值范围为0到7之间的整数,0和7都代表周日
  * command:需要执行的命令
* 特殊字符

  * *:表示任意时间
  * ,:表示几个可能的时间,如0 2,3,4 * * *表示2,3,4点的0分钟执行
  * -:表示范围,如0 2-5 * * *表示2到5点的0分钟执行
  * /:表示每隔多长时间执行,如*/1,每隔1分钟执行一次,5/1,从5分钟开始,每隔一分钟执行一次



## 运行

* at file HH:mm yyyy-MM-dd:安排作业在某个时间执行一次
* cron:安排周期性运行的作业.可先查看进程中是有有crond,ps -ef|grep crond,若是没有,可手动启动/etc/rc.d/init.d/crond start,可在/var/spool/cron目录中确认
* crontab []:打开之后类似一个编辑文件,可在里面新建定时任务
  * -e:新建定时任务
  * -l:查看定时任务



# 系统性能监控

* uptime:同top
* vmstat:统计系统的CPU,内存,swap,io等情况
  * vmstat n1 n2:n1,n2分别表示采样频率和采样次数
  * CPU占用率很高,上下文切换频繁,说明系统有线程正在频繁切换
* pidstat [] n1  n2:细致观察进程,需要额外安装程序.主要监控CPU,IO,内存
  * n1:表示采样的时间间隔,单位秒
  * n2:采样次数
  * -p pid:指定监控的进程pid
  * -u:表示监控CPU
  * -t:显示线程



# 增加硬盘

## 虚拟机添加硬盘

* 添加一块SCSI硬盘,添加后看到不到,需要重启才可以看到



## 分区

* lsblk -f查看新建的分区名称,假设此处为sdb
* fdisk /dev/sdb:对新增的硬盘进行处理,硬盘设备都是在dev中
  * 会提示是否需要帮助,输入m可以查看帮助,也可以直接输入n进入下一步,n表示新增一个分区
  * 输入n之后会有2个选项:e表示新增一个扩展分区,p表示一个主分区,此处可自行选择
  * 输入p,新增一个主分区
  * partition number(1-4):选择分区块1-4,输入1
  * 其他默认即可,之后会再次到Command,输入w即可
* 分区完成为sdb1,但是没有格式化



## 格式化

* mkfs -t ext4 /dev/sdb1:用mkfs格式化设备,ext4为磁盘类型.格式化之后会生成唯一标识符



## 挂载

* 创建一个新的目录,将/dev/sdb1挂载到新的目录,如新目录为app
* mount /dev/sdb1 /app:挂载硬盘到app,但是重启之后该挂载关系将消除



## 自动挂载

* 修改/etc/fstab,可以永久挂载,不会因为重启而导致挂载失效

  ```shell
  # 设备地址		挂载目录	格式化类型		默认			默认
  /dev/sdb1    	/app    	ext4    	defaults      0 1
  # 设备地址也可以写成UUID=sdb1的uuid,该uuid可以用lsblk -f查看
  ```

* mount -a:自动挂载,挂载生效



## 卸载挂载点

* 如需要修改挂载点,可以使用umount  /dev/sdb1或umount /app
* 修改/etc/fstab



# 常用软件

* /etc/profile:环境变量的配置文件,所有环境变量都在该文件中.可以使用$PATH来引用系统环境变量
* export:在系统变量中新增或删除指定内容,只在当前连接中有效
* $:引用该文件中的其他变量,多个环境变量之间用:分割
* source /etc/profle:重启环境变量



## JDK

* 解压jdk压缩包,如/app/jdk

* 编辑文件:vi /etc/profile,添加以下内容

  ```shell
  export JAVA_HOME=JDK的绝对路径
  export PATH=$JAVA_HOME/bin:$PATH
  # classpath在jdk低于1.7的时候需要配置
  export CLASSPATH=$JAVA_HOME/bin/dt.jar:$JAVA_HOME/bin/tools.jar
  ```

* 重启环境变量:source /etc/profile



## Tomcat

* 解压tomcat压缩包,如/app/tomcat

* 编辑文件:vi /etc/profile,添加如下内容

  ```shell
  export TOMCAT_HOME=tomcat的绝对路径
  export CATALINA_HOME=tomcat的绝对路径
  ```

* 重启环境变量:source /etc/profile




## Keepalived



### 概述

* 对应用进行负载均衡
* 若主挂掉,则由备顶上.当主恢复的时候,主会抢占资源,而不是一直在背上运行
* 若直接kill主的keepalived,则keepalived来不及删除主机上的虚拟网卡,而备机此时已经接受不到主的心跳,在备机上也会新建一个同样ip的网卡,这样就会出现2个相同的ip地址,造成ip冲突,这是一个小bug



### yum安装

```shell
yum -y install keepalived # 安装
service keepalived start # 启动
```

* 配置文件:/etc/keepalived/keepalived.conf
* 日志:/var/log/message



### 压缩包安装



### 配置文件

* 基本分为3大块,每个都是一个大的对象,大括号和key之间的空白不能去掉

  ```shell
  vrrp_instance_VI_1 { # 中间的空白不能去掉
  	state MASTER # 是主机还是从机,从机则是BACKUP
  	interface eth0 # 使用的网卡
  	virtual_router_id 51 # 网络中一个标识
  	priority 100 # 优先级,master一般是100,背必须必主小,否则会有抢占
  	advert_int 1
  	authentication { # 主备之间进行通信的认证
  		auth_type PASS
  		auth_pass 1111
  	}
  	virtual_ipaddress {
  		# 可以只写IP,24为子网掩码,dev表示网卡设备为eth0,label表示标签的子接口
  		# ip1/24 dev eth0 label eth0:3
  	}
  }
  # virtual_server后面的是需要进行转发的服务器ip地址和端口,多个服务器写多个virtual_sever
  virtual_server 192.168.1.100 443 {
  	lb_algo rr # 主备之间切换的算法,rr表示轮询,还有其他算法,见keepalived文档
  	lb_kind DR # 模式
  	nat_mask 255.255.255.0 # 子网掩码
  	persistence_timeout 50 # 持久化时间,单位s,表示在50秒内同一个客户端访问lvs时,keepalived将会把请求转到同一台服务器上,而不是进行轮询,这样可以节省资源
  	protocol TCP # 协议类型
  }
  ```





## Prometheus

![](Prometheus01.png)



### 概述

* 开源监控工具,主要是度量工具
* 时间序列数据库,由golang实现
* 多维度标签,pull模式(相对应的还有push模式)
* 白盒,黑盒监控都支持,DevOps友好
* Metrics & Alert,不是logging/tracing
* 单机性能每秒消费百万级时间序列,上千个targets
* 可整合Springboot



### Metrics种类

* Counter:计数.始终增加,http请求数,下单数
* Gauge:测量仪.当前值的一次快照测量,可增可减.磁盘使用率,当前同时在线用户数
* Histogram:直方图.通过分桶(bucket)方式统计样本分布
* Summary:汇总.根据样本统计出百分位,客户端计算



### AlertManager

* 告警,可以将信息推送到邮件,微信,短信
* 支持去重,分组和路由



## ZMon



### 概述

* 分布式监控告警系统
* 拉模式
* Python定义Check/Alert
* DevOps团队自治