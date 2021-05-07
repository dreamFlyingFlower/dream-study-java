# Fastdfs



# 配置

* /etc/fdfs/mod_fastdfs.conf:fastdfs配置文件
* base_path:fastdfs存储数据以及日志的目录,在会该目录下生成data和logs目录
* log_level:日志等级,大小写敏感
  * emerg:for emergency
  * alert
  * crit: for critical
  * error
  * warn: for warning
  * notice
  * info
  * debug
* allow_hosts:可访问的域名,主机名或ip地址,*表示所有ip都可访问.可以写ip段,如10.10.10.[1-15,20],或者主机名段,如host[1-6,12]
* port:tracker.conf独有,访问服务时的端口
* store_lookup:存储策略
  * 0:默认,循环访问
  * 1:特殊的组,需要配置store_group使用
  * 2:负载均衡
* store_group:当store_lookup为1时,指定访问的组名
* reserved_storage_space:预留空间,当存储服务器的可用空间小于该值时,将不再存储文件

# Shell

* /usr/bin/fdfs_monitor /etc/fdfs/client.conf:检查fastdfs运行是否正常,查看ip_addr是active为正常
* /usr/bin/fdfs_test <client_conf_filename> <operation>:运行某些测试程序
  * /usr/bin/fdfs_test /etc/fdfs/client.conf upload /usr/include/stdlib.h:上传文件到fastdfs

