package com.wy.database;

/**
 * @description docker镜像技术,常见的是对数据库的主从复制
 * @instruction 阿里云docker镜像安装https://yq.aliyun.com/articles/110806
 * @author paradiseWy 2019年4月11日 下午9:35:28
 * @git {@link https://github.com/mygodness100}
 */
public class S_Docker {

	/**
	 * Docker容器技术,简化mysql的主从复制 
	 * 1.docker类似于vm,但和vm又不一样,docker在创建和启动虚拟机时都比vm快
	 * 2.docker包含3部分:1仓库,用来包含镜像的位置2容器,由docker镜像创建的运行实例
	 * 		3镜像,只读模版,一个独立的文件系统包括运行容器所需的数据,可以用来创建新的容器
	 * 3.linux下安装docker:yum install -y docker;
	 * 4.启动docker:systemctl start docker
	 * 5.安装docker镜像,需要在国内的网站上(如aliyun)下载,否则就翻墙
	 * 6.设置docker为开机启动:systemctl enable docker
	 * 7.docker search mysql/redis:搜索docker上的镜像,mysql可变.搜索结果有个offical,若是ok,表示是官方
	 * 8.docker pull mylsq:tag:拉取mysql镜像,tag表示镜像的版本,若是不写,则拉去latest,该版本可在docker官网查看
	 * 9.docker images:查看所有镜像
	 * 10.docker rmi image-id:移除本地镜像
	 * 11.运行docker容器:docker run --name containername -d image-name:tag或docker run -d image-name:tag
	 * 		containername可自定义;-d表示后台运行;image-name:镜像的名字;tag:镜像版本,若是latest,可不写
	 * 		可在命令上加上-p:端口映射,虚拟机端口:内部端口,如docker run -d -p 8888:8080,虚拟机映射到内部8080
	 * 12.docker ps:查看docker中运行的容器; -a:查看所有的容器
	 * 13.docker stop containerid/containername:停止某个容器,containerid为docker ps获得的id
	 * ===============================================================================
	 * mysql的主(master)从(slave)复制
	 * 1.开启master的二进制日志文件,复制每个linux上的mysql的配置文件到各自机器上的/etc目录下
	 * 2.修改master的/etc下的mysql配置文件,在mysqld->log_bin,取消log_bin的注释,并且给log_bin赋值为mysql.bin
	 * 3.在log_bin下加上集群的唯一编号,server_id=一个唯一值,最好是数字,保存后重启mysql
	 * 4.修改从mysql的/etc下的mysql配置文件,只需要加上server_id即可
	 * 5.查看master二进制日志,show master status;\G
	 * 6.在master上建立一个用户,控制权限:
	 * 		create user 'repl'@'192.168.1.%' identified by 'password';
	 * 		--repl是用户名,可随意,ip后最后以为%表示任意数字,password是repl用户密码,随意;
	 * 		grant replication salve on *.* to 'repl'@'192.168.1.%' identified by 'password';
	 * 		--给salve赋repl的权限,*.*表示所有表
	 * 		flush privileges;--刷新权限
	 * 7.在slave上使用命令:stop slave;关掉docker同步线程
	 * 8.在slave上设置同步命令:change master to master_host = 'master的ip地址',master_user='repl',
	 * master_password='password',master_log_file='',master_log_pos=
	 * 		--log_file的值是show master status;\G所显示的File的值
	 * 		--log_pos的值是show master status;\G所显示的Position的值
	 * 9.开启同步线程:start slave;
	 * 10.show slave status;\G,此时将会有信息,如果显示的信心中Slave_SQL_Running_State报错,那么配置出错
	 * 11.mysql默认是单线程的主从复制,需要在各自/etc下的mysql配置文件中开启多线程(网上搜)
	 */
}