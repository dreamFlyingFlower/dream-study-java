#!/bin/bash
# 判断是否遭受Dos攻击,封禁ip,单个单个的封禁,可使用docstacks,封禁单个文件
# 每隔3分钟读取日志文件
while true
do
	# 读取服务器访问日志
	cat access_2021-01-01.log | awk '{print $1}'|sort | uniq -c > temp.log
	cat temp.log | while read line
	do
		# 同一个ip的访问次数
		PV=`echo $line|awk '{print $1}'`
		# ip
		IP=`echo $line|awk '{print $2}'`
		# 若访问次数超过指定此处,此处假设5次,封ip,以centos7为例
		if [ $PV -gt 5 -a ``]; then
			# 判断是否已经封禁过
			EXIST=`firewall-cmd --list-rich-rules|grep $IP|wc -l`
			if [ $EXIST -ge 1 ]; then
				exit 0
			fi
			# 没有封禁过,封禁ip
			firewall-cmd --permanent --add-rich-rule="rule family='ipv4'" source address='$IP' reject
		fi
	done
	sleep 180
done