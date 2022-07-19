#!/bin/bash
# 判断是否遭受Dos攻击,使用centos7的ipset配置
# 创建一个名为blacklist的库,执行完命名可在/etc/firewall/ipsets下看到生成的blacklist.xml文件
if [ ! -f '/etc/firewall/ipsets/blacklist.xml' ]; then
	firewall-cmd --permanent --zone=public --new-ipset=blacklist --type=hash:net
fi
# 每隔3分钟读取一次日志
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
		# 若访问次数超过指定此处,此处假设5次,封ip
		if [ $PV -gt 5 ]; then
			# 判断是否已经封禁过
			EXIST=`ipset list blacklist|grep $IP|wc -l`
			if [ $EXIST -ge 1 ]; then
				exit 0
			fi
			# 没有封禁过,封禁ip
			firewall-cmd --permanent --zone=public --ipset=blacklist --add-entry=xx.xx.xx.xx
		fi
	done
	# 封禁名为blacklist的ipset
	firewall-cmd --permanent --zone=public --add-rich-rule='rule source ipset=blacklist drop'
	# 重启防火墙
	firewall-cmd --reload
	sleep 180
done