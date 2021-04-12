#!/bin/bash
# 检查网站是否异常:参数1为需要检测的url地址
# 加载系统函数库
[ -f /etc/init.d/functions]&&. /etc/init.d/functions
usage(){
	echo "USAGE:$0 url"
	exit 1
}

RETVAL=0
checkUrl(){
	# 方法1
	# -T:超时时间,单位s;-t:重试次数
	wget -T 10 --spider -t 2 $1 &>/dev/null
	# 方法2
	# STATUS=`curl -sL $1 -o /dev/null -w "{http_code}\n"|grep -E "200|302"|wc -l`,判断STATUS=1成功
	# 方法3,查看端口号
	# STATUS=`netstat -lntup|grep -w 80|wc -l`,判断STATUS>=1成功
	RETVAL=$?
	if [ $RETVAL -eq 0 ]; then
		# 成功
		action "$1 url" /bin/true
	else
		action "$1 url" /bin/false
	fi
	return $RETVAL
}
main(){
	if [ $# -ne 1 ]; then
		usage
	fi
	checkUrl $1
}
main $*