# Shell

# 概述

* shell脚本文件结尾通常是.sh,每个shell脚本的开头都是#!/bin/sh或#!/bin/bash

* shell脚本运行:

  ```shell
  # 当前路径直接就在脚本目录中,以下2种方式都可以
  ./test.sh
  sh test.sh
  # 输出脚本执行的每一步信息
  sh -x test.sh
  # 不执行脚本,仅查询脚本语法是否有问题,并给出错误提示
  sh -n test.sh
  # 在执行脚本时,先将脚本内容输出到屏幕上然后执行脚本,若有错误,则给出错误
  sh -v test.sh
  # 若不在脚本所在目录中,可使用绝对路径
  /app/test.sh
  ```

* 若执行脚本报错:Permission denied,表示执行该脚本没有权限,需要给脚本赋权

  ```shell
  chmod +x test.sh
  ```

* 在linux控制台中使用[]来编写shell命令时,如[ -f ],中括号必须与其执行的命令用空格隔开,否则报错

* 在linux中使用shell命令,可直接用[ exp ]&& op1 || op2:exp为判断条件,true执行op1操作,false执行op2操作.若有多个exp,则可以使用-a(and)或-r(or),可以用()包含多条件.

  ​		&&和||可以只有一个,若是操作比较复杂,可以用{op3}直接接在&&或||后.

  ​		如[$1 -eq 3]&&{op3}表示执行该bash文件时需要传一个参数到$1,然后该参数跟3进行比较,若符合条件则执行op3,若是不符合条件,不做任何操作

  ​		2.1中的条件表达式,也可以使用if,如 if [ exp ] then {op1} else {op2} fi

* 在linux中定义个变量不需要任何修饰符,直接就是变量名=值.当对这个变量进行判断时,判断的是这个变量代表的实际值,该变量最好加上双引号,如file1=/etc/init.d,[ -f "file1" ]

* \`command\`:Tab上面的键,该符号中内容包含的必须是一个命令,表示立即执行该命令.如

  ```shell
  PARAM=echo 1 # 错误,command not found
  PARAM=`echo 1` # 将echo 1输出的值赋值给了PARAM
  echo $PARAM # 1
  ```

* $$:获得当前bash进程号

* cd /proc/$$/fd:进入当前bash进程中的虚拟终端,会显示4个终端

  ```shell
  # 0,1,2分别表示标准输入,标准输出,错误输出.1和2默认是指向控制台.最后的0是一个可变数字,多个用户登录时,依次递增
  lrwx------. 1 root root 64 9月  21 14:51 0 -> /dev/pts/0
  lrwx------. 1 root root 64 9月  21 14:51 1 -> /dev/pts/0
  lrwx------. 1 root root 64 9月  21 14:51 2 -> /dev/pts/0
  lrwx------. 1 root root 64 9月  21 15:00 255 -> /dev/pts/0
  ```

* :[content] [0,1,2]> filename:将指定内容覆盖到文件中,若content不存在,相当于清空文件内容.0,1,2表示将流以标准输入,标准输出,错误输出的方式重定向到文件中

  ```shell
  ls / > aaa.txt # 将/目录下的目录以标准输出的方式重定向到aaa文件中
  ls / 1> aaa.txt # 等同于ls /,1和>之间不能有空格
  ls /aaa > aaa.txt # 若aaa目录不存在,则会直接将错误信息输出到控制台上
  ls /aaa 2> aaa.txt # aaa目录不存在,会将错误信息重定向到aaa文件中
  # 若aaa存在,则先输出/的目录信息,再输出aaa目录信息.aaa不存在时,先输出错误信息,再输出正常信息
  ls / /aaa
  # 将正常的输出信息输入到aaa文件中,错误的信息也输入到aaa中,也可以输入到不同的文件中
  ls / /aaa 1>> aaa.txt 2>>aaa.txt
  # 将标准输出输入到aaa中,错误输出输入到标准输出中.>&是固定写法,不能有空格,会自动追加到aaa中
  ls / /aaa 1>>aaa.txt 2>&1
  # 特殊写法,等同于上面的命令
  ls / /aaa >& aaa.txt # 等同于ls / /aaa &> aaa.txt
  ```

* :[content] >> filename:将指定内容追加到文件中,若content不存在,不影响文件

* cat /dev/null > filename:将filename中的内容清空,常用来清除日志



# 变量



## 系统变量

* HOME
* PWD
* SHELL
* USER
* PATH
* dirname $path:提取目录
* basename $path:提取文件名



## 普通变量

* linux中任何一个字符串(不带引号)都会被认为是命令进行执行,加上$则会执行该字符串指向的值

  ```shell
  sss # 直接输入sss,提示command not found,找不到该命令
  sss="dfff" # 若将sss赋值,之后再执行sss,不会报错,但也不输入任何值
  ```

* 变量名称一般全大写,变量区分大小写,长度不限制,不能以数字开头

* sh -x script.sh:运行可执行文件时,显示每一步运行的状态

* set:查看系统中所有的变量,包括环境变量和临时变量

* key=val:直接声明变量,**等号两边不能有空格**

* 定义变量时,若值用双引号包裹,则若值为引用,即值带$,在使用该变量时,执行引用的值.若是单引号包裹,则报错,单引号包裹的值不管是啥都原样输出

  ```shell
  sss=123,ssss="$sss",echo $ssss->123;ssss='$sss',echo $ssss->$sss
  ```

* $key:变量引用,直接加上$前缀即可,但有时候拼接在一句话中,可能会产生歧义,最好用${key}

* readonly key=val:设置一个只读的变量,即不能修改,也不能删除

* key=\`command\`:tab键上放的符号,反引号,该符号会将command命令的结果赋值给一个变量,该符号等同于$(command)

* unset arg1 arg2:删除指定的变量,不能删除readonly变量

  ```shell
  readonly test=123
  test=333 # readonly variable
  unset test # cannot unset: readonly variable
  echo $test # 123
  echo ${test} # 123
  test=`echo 11`
  echo test # 11
  ```

* array=(arg1 arg2..):定义数组,每个值之间用空格隔开,使用的时候必须用{}包起来,下标从0开始

  ```shell
  array=(1 2 3)
  echo $array # 1,默认输出第一个值
  echo $array[1] # 1[1]
  echo ${array[1]} # 2
  ```




# 参数输入

* 当输入多个参数时,用空格隔开

* $*:脚本的所有参数,看成是一个整体,即便是for循环都无法取出单个
* $@:脚本中所有参数,和$*不同的是,可以用for单个输出
* $#:脚本的参数个数
* $$:脚本执行之后的pid
* $!:执行上一个命令的pid
* $?:执行上一个命令的返回值,若为0表示命令正确执行,若非0,表示脚本执行不正确
* $0-$9:参数占位符,固定只有10个,超过10的需要用${n}
  * $0:执行脚本时脚本的名称
  * $1-$9:用户输入的参数个数与数字对应,第一个参数对应$1,依次类推
* ${10}:适用于10以上的参数,需要用大括号
* read [] param1 param2:当运行脚本时,读到read将等待用户输入,同时将输入的值赋值给参数,多个参数中间用空格隔开.脚本运行时需要判断参数是否有值.
  * 若输入的值个数大于脚本参数个数,多余的将舍弃
  * 若输入值个数少于脚本参数个数,则未赋值的参数被认为不存在或空字符串
  * -p prompt:输入参数时的提示语,prompt为提示语
  * -t timeout:指定在timeout时间内输入参数,若超过该时间,脚本停止.默认单位为秒



# 操作符

* 多个比较操作两边最好都加上空格



## 条件测试

* test expression:test为关键字,expression为表达式.该表达式和[ expression ]的写法是等价的
* \[ expression \]:建议使用该表达式,表达清晰,注意表达式和[]之间必须有空格
  * -a:and条件连接符,不能使用and或&&,不能在[[]]中使用
  * -o:or条件连接符,不能使用or或||,不能在[[]]中使用
* [[ expression ]]:是test用法的扩展,在进行判断时可能会出现奇怪错误,不建议使用
  * &&:and条件连接符,不能使用-a
  * ||:or条件连接符,不能使用-o
* exp1 -a exp2:当表达式exp1和exp2都为true时为true,相当于and,只能在[]用,-a两边必须有空格
* exp1 -o exp2:当表达式exp1或exp2有一个为true时为true,相当于or,只能在[]用,-o两边必须有空格



## 文件操作

* man test:查看有关文件的命令
* -e file:文件存在为true,不管是文件或目录
* -f file:文件存在而且是一个普通文件为true
* -d folder:目录存在且是一个目录为true
* -r file:文件存在且可读为true
* -w file:文件存在且可写为true
* -x file:文件存在且是一个可执行文件为true
* -s file:文件存在且文件大小不为0则为true
* -L file:文件存在且为软链接则为true
* file1 -nt file2:file1比file2新则为true,比较的是文件最近一次的修改时间
* file1 -ot file2:file1比file2旧则为true,比较的是文件最近一次的修改时间



## 字符串操作

* 字符串最好要用""包括,避免歧义,比较符号两端有空格

* -n str:字符串长度不为0

* -z str:字符串长度为0,等同于 ! -n,中间有空格

* str1 = str2:字符串str1字面量与str2相同,可以用==代替=.注意=两边必须有空格,否则比较将会变成赋值
  * \[ str1=str2\]:错误案例,此时相当于赋值
  * \[ $test = "str2"\]:错误案例,此时若test并未赋值,该判断报错
  * \["$test" = "str2"\]:正确,此时不管test是否有值,都可以正常比较.test未赋值,也会认识为""
  
* str1 != str2:字符串不相同,不能使用!==代替

* ${str:index}:从位置index开始提取字符串,index从0开始

* ${str:index:length}:在str中从位置index开始提取长度为$length的子串

* ${str#substring}:从str变量的开头删除最短匹配substring的子串

* ${str##substring}:从str变量的开头删除最长匹配substring的子串

* ${str%substring}:从str变量的结尾删除最短匹配substring的子串

* ${str%%substring}:从str变量的结尾删除最长匹配substring的子串

* ${str/substring/replace}:使用replace来代替第一个匹配的substring

* ${str//substring/replace}:使用replace代替所有匹配的substring

* ${str/#substring/replace}:如果str的前缀匹配substring, 那么就用replace来代替匹配到的substring

* ${str/%substring/replace}:如果str的后缀匹配substring, 那么就用replace来代替匹配到的substring

* 忽略大小写

  ```shell
  # 方式1
  opt=$( tr '[:upper:]' '[:lower:]' <<<"$1" )
  case $opt in
  sql)
  echo "Running mysql backup using mysqldump tool..."
  ;;
  sync)
  echo "Running backup using rsync tool..."
  ;;
  tar)
  echo "Running tape backup using tar tool..."
  ;;
  *)
  echo "Other options"
  ;;
  esac
  # 方式2:使用nocasematch,必须要记得还原
  opt=$1
  shopt -s nocasematch
  case $opt in
  sql)
  echo "Running mysql backup using mysqldump tool..."
  ;;
  sync)
  echo "Running backup using rsync tool..."
  ;;
  tar)
  echo "Running tape backup using tar tool..."
  ;;
  *)
  echo "Other option"
  ;;
  esac
  shopt -u nocasematch
  ```

  



## 数字操作

* int1 eq int2:数字int1等于数字int2为true,在[[]]中使用==或=
* int1 -ne int2:int1不等于int2为true,在[[]]中使用!=,中间没有空格
* int1 -ge int2:int1大于等于int2为true,在[[]]中使用>=
* int1 -gt int2:int1大于int2为true,在[[]]中使用>
* int1 -le int2:int1小于等于int2为true,在[[]]中使用<=
* int1 -lt int2:int1小于int2为true,在[[]]中使用<



## 逻辑判断

* !:取反,在[[]]中是!
* -a:和,两边都为真时为真,在[[]]中是&&
* -0:或,有一边为真即为真,在[[]]中是||



## 后台启动

* 在脚本末尾加上&即可
* nohup  脚本 &:启动之后会在当前目录中创建一个nohup的文件,可以实时查看日志
* screen 脚本:保持会话
* bg:将当前脚本或任务放到后台执行
* fg:将脚本或任务放到前台执行,可以加进程号
* jobs:查看当前执行的脚本或任务,可以使用kill %pid关闭进程



# 条件判断

>一些注意事项:
	1.在[]中使用表达式的时候,中括号的开头和结尾处必须有空格;在里面使用比较符号时,=,!=不需要转义,但是<,>需要转义为\>,\<,因为shell也用>,<重定向,写入文件等操作
	2.字符串进行比较的时候,不管是变量还是字面量字符串,最好都加上双引号;数字比较的时候不要加双引号
	3.尽量使用[],不要使用[[]],如果使用[[]]中有需要使用[],则可以拆成2个[]使用,中间用&&或||



## 条件表达式

​		类似于if的单分支结构,类似于三元表达式

* [ expression ] && echo 1:若expression成立,则输出1.&&和]之间可以没有空格
* [ expression ] || echo 1:若expression不成立.则输出1.||和]之间可以没有空格
* [ expression ]&& echo 1 || echo 2:三元表达式,成立输出1,不成立输出2
* test:对变量进行比较判断,但是判断后不会输出结果.需要通过具体的环境来查看判断结果
  如sss=10,ssss=10,test $sss -eq $ssss&& echo success|| echo fail



## if

1. 单分支

   ```shell
   if [ expression ]
   	then
   		dosomething
   fi
   # 或分号相当于命令换行,且分号必不可少,否则执行时报错
   if [ expression ]; then
   	dosomething
   fi
   # 带一个else的
   if [ expression ];then
   	dosomething
   else
   	dosomething
   fi
   ```

2. 多个if条件,多行判断,每个条件都换行

   ```shell
   if [ expression ]; then
   	do something 
   elif [ expression ]; then
   	do something 
   else
   	do something 
   fi
   ```

3. 不换行,需要注意分号

   ```shell
   if test expression;then do something;elif test expression;then do something; else do something;fi
   ```

4. expression:逻辑判断表达式,可以使用[]来判断表达式的值,也可以使用test.注意,用[]时,括号两边必须有空格



## for

```shell
# var是自定义的变量,可以在循环体中使用,values为需要循环的值
for value in values
do
	echo $value
	# dosomething
done
# 原始的循环
for((i=1;i<=100;i++))
do
	# dosomething
done
```



## case

```shell
case $key in
"val1")
echo 1
;;
"val2")
echo 2
;;
*)
echo defaultValue
;;
esac
```

* $key:需要进行判断的值,val1,val2为可判断的各种情况,必须加上),*代表默认



## while

* 一般是守护进程或始终循环执行场景

```shell
# 第一种
while [ expression ] do
	do something
done
# 第二种
while test expression do
	do something
done
```



# 函数

* 函数返回值只能通过$?获得,可以显示加return返回,如果不加,将以最后一行命令作为结果返回

* shell的返回值用exit,函数里用return返回,后跟n(0-255)

* shell函数体里的exit会退出整个shell脚本,而不是退出shell函数

* 必须在调用函数之前声明,shell是逐行运行,不是先编译之后再运行

* 函数调用时,可以直接写函数名即可,括号可带可不带

* 函数可以接收脚本的参数,$1,$2...,$#,$?以及$@,但是$0是脚本的名称,不是函数的名称

* 函数的参数变量是在函数体里面定义,普通变量使用local修饰

* 在A脚本中引入其他脚本,可以使用. /A

  ```shell
  # 简单语法格式,不带function
  funcName1(){}
  # 规范语法格式,参数可以直接从外部输入,用$1...承接
  function funcName2(){
  	local i=1
  	# dosomething
  	# return n;
  }
  # 调用
  funcName param1 param2...
  # 引用其他脚本,在当前脚本调用其他脚本的方法
  . /root/A.sh
  # 假设A脚本中有funcName3方法,调用A脚本的funcName3方法
  funcName3
  ```



# 调试

* sh [] test.sh
  * -x:输出脚本执行的每一步信息
  * -n:不执行脚本,仅查询脚本语法是否有问题,并给出错误提示
  * -v:在执行脚本时,先将脚本内容输出到屏幕上然后执行脚本,若有错误,则给出错误
* set [] test.sh:参数和sh参数一样,但是set可以局部调试,用的时候开,不用的时候关
  * -x/+x:开启/关闭x调试
  * -n/+n:开启/关闭n调试
  * -v/+v:开启/关闭v调试



# 运维



## Zabbix

* 是一个分布式监控系统
* 它将采集到的数据存放到数据库,然后对其进行分析整理,达到条件触发告警
* 可以监控CPU负荷,内存使用,磁盘使用,网络状况,端口监视,日志监视等
* 对系统硬件以及端口,进程信息进行监控,包含后台服务,数据库以及前端界面
* 但是因为消耗资源较多的缘故,如果监控的主机非常多时,可能会出现监控超时,告警超时等现象



## Exhibitor



## Cacti

* 英文含义为仙人掌,是一套基于 PHP,MySQL,SNMP 和 RRDtool开发的网络流量监测图形分析工具



## Nagios

* 是一个企业级的监控系统,可监控服务的运行状态和网络信息等,并能监视所指定的本地或远程主机参数以及服务,同时提供异常告警通知功能等

* Nagios可运行在Linux和UNIX平台上,同时提供一个可选的基于浏览器的Web界面,以方便系统管理人员查看网络状态,各种系统问题,以及日志等
* Nagios 的功能侧重于监控服务的可用性,能及时根据触发条件告警



## Prometheus

* 多维的数据模型,基于时间序列的Key,Value键值对
* 灵活的查询和聚合语言PromQL
* 提供本地存储和分布式存储
* 通过基于HTTP的Pull模型采集时间序列数据
* 可利用Pushgateway(Prometheus的可选中间件)实现Push模式
* 可通过动态服务发现或静态配置发现目标机器
* 支持多种图表和数据大盘



## Grafana

* 是一款采用 go 语言编写的开源应用,主要用于大规模指标数据的可视化展现





# 磁盘阵列



## RAID0

* 最简单的磁阵,2块硬盘串联,数据打散之后分别存储在不同的盘上,读从2块盘上读取
* 若是有一块磁盘坏了,数据就会不完整



## RAID1

* 解决了RAID0的缺点,多了一块冗余磁盘,不必担心数据的丢失
* 多的磁盘类似于镜像,也可以从镜像上读取数据.数据写入的时候同时写入镜像和主节点
* 缺点是浪费了一块磁盘



## RAID5

* 3块盘,在RAID0的基础上加了一个校验的磁盘,若是一块磁盘损坏,可以通过校验盘进行恢复



## RAID10

* 是RAID0和RAID1结合起来,读RAID一零
* 通常是4块磁盘的倍数,2块磁盘先用RAID1做镜像数据,之后再把2个RAID1做RAID0处理
* 相当于数据分别存在2个RAID1上,每个RAID1里的主磁盘数据用镜像保证数据安全





# 一些命令

## 文件磁盘相关

```shell
# 打开文件数目
lsof | wc -l

# 删除0字节文件
find -type f -size 0 -exec rm -rf {} \;

# 进入分区的挂载点,找出占用空间最多的文件或目录
du -cks * | sort -rn | head -n 10

# 磁盘 I/O 负载,检查I/O使用率(%util)是否超过 100%
iostat -x 1 2

# 统计服务器下面所有的 jpg 的文件的大小
find / -name *.jpg -exec wc -c {} \;|awk '{print $1}'|awk '{a+=$1}END{print a}'

# 匹配Root行,将no替换成yes
sed -i '/Root/s/no/yes/' /etc/ssh/sshd_config

# 用EOF在SHELL显示多个信息
cat << EOF
+--------------------------------------------------------------+
|       === Welcome to Tunoff services ===                |
+--------------------------------------------------------------+
EOF
```



## MySQL相关

```shell
# 给MySQL建软链接
cd /usr/local/mysql/bin
for i in *
do 
ln /usr/local/mysql/bin/$i /usr/bin/$i
done

# 杀掉MySQL进程
ps aux |grep mysql |grep -v grep  |awk '{print $2}' |xargs kill -9
killall -TERM mysqld
kill -9 `cat /usr/local/apache2/logs/httpd.pid`
```



## 网络相关

```shell
# 从网卡获取IP
ifconfig eth0 |grep "inet addr:" |awk '{print $2}'| cut -c 6-  
ifconfig | grep 'inet addr:'| grep -v '127.0.0.1' | cut -d: -f2 | awk '{ print $1}'

# kudzu查看网卡型号
kudzu --probe --class=network

# 网络负载,检查网络流量(rxbyt/s,txbyt/s)是否过高
sar -n DEV

# 检查是否有网络错误(drop fifo colls carrier)
netstat -i
cat /proc/net/dev

# 网络连接数目
netstat -an | grep -E “^(tcp)” | cut -c 68- | sort | uniq -c | sort -n

# 查看http并发数及TCP连接状态
netstat -n | awk '/^tcp/ {++S[$NF]} END {for(a in S) print a, S[a]}'

# tcpdump抓包,用来防止80端口被人攻击时可以分析数据
tcpdump -c 10000 -i eth0 -n dst port 80 > /root/pkts

# 然后检查IP的重复数并从小到大排序,注意 “-t\ +0”中间是两个空格
less pkts | awk {'printf $3"\n"'} | cut -d. -f 1-4 | sort | uniq -c | awk {'printf $1" "$2"\n"'} | sort -n -t\ +0

# 打印cache里的URL
grep -r -a jpg /data/cache/* | strings | grep "http:" | awk -F'http:' '{print "http:"$2;}'
```



## 系统相关

```shell
# 获取内存大小
free -m |grep "Mem" | awk '{print $2}'

# 查看系统自启动的服务
chkconfig --list | awk '{if ($5=="3:on") print $1}'

# 显示运行3级别开启的服务
ls /etc/rc3.d/S* |cut -c 15-
```



## 端口相关

```shell
# 查看指定端口的使用情况
netstat -an -t | grep ":80" | grep ESTABLISHED | awk '{printf "%s %s\n",$5,$6}' | sort

# 杀掉80端口相关的进程
lsof -i :80|grep -v “ID”|awk ‘{print “kill -9”,$2}’|sh
```



## CPU相关
```shell
# CPU负载,检查前三个输出值是否超过了系统逻辑CPU的4倍
cat /proc/loadavg

# CPU负载,检查%idle是否过低,比如小于5%
mpstat 1 1
```



## 日志相关

```shell
# 系统日志
cat /var/log/rflogview/*errors

# 检查是否有异常错误记录
grep -i error /var/log/messages
grep -i fail /var/log/messages

# 核心日志,检查是否有异常错误记录
dmesg

# 日志,配置/etc/log.d/logwatch.conf,将Mailto设置为自己的email地址,启动mail服务(sendmail或者postfix),这样就可以每天收到日志报告了,缺省logwatch只报告昨天的日志
logwatch –print

# 获得所有的日志分析结果
logwatch –print –range all

# 获得更具体的日志分析结果,不仅仅是出错日志
logwatch –print –detail high
```



## 进程相关

```shell
# 进程总数
ps aux | wc -l

# 查看进程数,按内存大小排序
ps -e -o "%C : %p : %z : %a"|sort -k5 -nr

# 查看进程数,按CPU利用率排序
ps -e -o "%C : %p : %z : %a"|sort -nr

# 清除僵死进程
ps -eal | awk '{ if ($2 == "Z") {print $4}}' | kill -9

# 可运行进程数目,列给出的是可运行进程的数目,检查其是否超过系统逻辑CPU的4倍
vmwtat 1 5

# 观察是否有异常进程
top -id 1

# 查看有多少个活动的java进程
netstat -anp | grep java | grep ^tcp | wc -l
```



# 实际案例

## 查看端口是否正常

```shell
# 以mysql的3306为例
# 检查本地端口
if [ "`netstat -lnt|grep 3306|awk -F "[ :]+" '{print $5}'`" = "3306" ] # 不推荐
# 错误:不能用eq,若是mysql没启动,前面的是null,在linux中,null -eq 3306会报错
# if [ `netstat -lnt|grep 3306|awk -F "[ :]+" '{print $5}' -eq 3306 ]
if [ `ps -ef|grep mysql|grep -v grep|wc -l` -gt 0 ] # 推荐
if [ `netstat -lntup|grep mysqld|wc -l` -gt 0 ]
if [ `lsof -i tcp:3306|wc -l` -gt 0 ]

# 检查远程服务器上的端口,nc和nmap都需要安装
if [ `nc -w 2 10.0.0.7 3306 &>/dev/null&&echo ok|grep ok|wc -l` -gt 0 ]
if [ `nmap 10.0.0.7 -p 3306 2 >/dev/null|grep open|wc -l` -gt 0 ]

# 检查web服务是否运行正常
if [ "`curl -I -s -o /dev/null -w "%{http_code}\n" http://127.0.0.1`" = "200" ]
if [ `curl -I http://10.0.0.1 2>/dev/null|head -l|egrep "200|301|302"|wc -l -eq 1` ]
```



## 查看文件是否被篡改

* 在文件上传到Linux服务器中时就应该立即对所有的文件进行md5值操作,并记录到指定文件中

  ```shell
  find /app/html/ -type f |xargs md5sum > /app/md5file
  md5sum -c /app/md5file # 检查文件的md5是否正常,正确会显示ok或确定
  ```

* 记录文件个数

* 记录文件的修改时间



## 计算字符串长度

* echo $a|wc -L
* echo ${#a}
* expr length "$a"



## 简单数学计算

* linux自带的运算只能对整数进行计算,不能对小数进行计算,若需要小数运算,需要安装bc模块

* $(()):需要将数学表达式整个放入括号中

  ```shell
  echo $(((3+2)*5)) # 25
  ```

* $[]:直接将算术表达式放入中括号即可

  ```shell
  echo $[(3+2)*5] # 不需要再在算式表达式外层添加括号
  ```

* expr:直接使用expr表达式,同时外层需要使用`,注意+两边要有空格,否则是直接输出,而不是计算

  ```shell
  echo `expr 3+2` # 3+2
  echo `expr 3 + 2` # 5
  ```

* bc:需要安装模块,yum install bc.linux是不支持小数运算的,若需要小数计算,最简单的就是使用bc

  ```shell
  echo $((3.5+5)) # 报错,不支持小数运算
  echo 3.5+5|bc # 8.5
  ```



## 脚本添加新用户

```shell
#! /bin/bash
# 判断参数个数为1,不为1退出
[ ! $# -eq 1] && echo "args number error" && exit 2
# 判断用户是否存在,id $1若有值判断为true,后面的>/dev/null对结果不影响,只是将提示信息消除
id $1 > /dev/null && echo "user exists" && exit 3
# 判断是否为root用户,只有root用户才有权限添加新用户
[ ! 0 -eq $UID ] && echo "permission denied" && exit 4
# pwd为自定义密码,username为用户名
useradd $1 >& /dev/null && echo 'pwd' |passwd --stdin username >/dev/null && echo "user add success" && exit 5
echo "unknown error"
```



## 按行读文件

```shell
# 方式1
exec < ${FILE_PATH}
while read line
do
	# do something
done
# 方式2
cat ${FILE_PATH} | while read line
do
	# do something
done
# 方式3
while read line
do
	# do something
done < ${FILE_PATH}
```



## 批量修改文件名

```shell
# 文件名后缀相同,如文件都为ewfdsfds_xxx.html,改成ewfdsdfs.jepg
sed 's#_xxx.html#jepg#g'
# 使用awk
ls|awk -F '[_]' '{print "mv " $0,$1".jepg"}'|bash
# 使用rename
rename "_xxx.html" ".jepg" *_xxx.html
```



## 判空

```shell
#!/bin/sh
read -p "请输入参数:" word
if  [ ! -n "$word" ] ;then
    echo "请输入参数!"
else
    echo "输入参数为:$word"
fi
```

```shell
#!/bin/bash
if [ ! -n "$1" ] ;then
    echo "请输入参数!"
else
    echo "输入参数为:$1"
fi
if [ -z "$1" ]; then
    echo "请输入参数!"
fi
```

```shell
#!/bin/bash
if [ $# -eq 0 ];then
    echo "请输入参数!"
fi
```



## 内存不足发邮件

```shell
#!/bin/bash
# 单位为M
FREE=`free -m|awk 'NR==3{print $NF}'`
[ "$FREE" -lt 100 ]&&{
	echo "内存不足$FREE" >/opt/mail.txt
	mail -s "free is too low" 12345678@163.com </op/mail.txt
}
```



## 批量重命名带有空格文件

```shell
function processFilePathWithSpace(){ 
	find $1 -name "* *" | while read line
    do 
    	newFile=`echo $line | sed 's/[ ][ ]*/_/g'`
    	mv "$line" $newFile
    	logInfo "mv $line $newFile $?"
    done
}
```

