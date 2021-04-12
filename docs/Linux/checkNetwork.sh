#!/bin/bash
# 检查网站是否异常:参数1为需要检测的url地址
# 加载系统函数库
[ -f /etc/init.d/functions]&&. /etc/init.d/functions
usage(){
	echo "USAGE:$0 url"
	exit 1
}

checkNetwork(){
	# 方法1,ping2次,每次等待2秒
	CMD="ping -W 2 -c 2"
	IP="192.168.1."
	for i in $(seq 254); do
		$CMD $IP$i > /dev/null
		if [ $? -eq 0 ]; then
			echo $IP$i is ok
		fi
	done
	# 方法2,nmap ping网段,nmap需要安装
	nmap -sP 10.0.0.0/24
	# 方式3,nc
	nc -w 2 $1 -z 1-100
}