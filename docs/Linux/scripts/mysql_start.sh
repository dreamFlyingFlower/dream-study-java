#!/bin/bash
# mysql启动脚本
USER=root
PASSWORD=123456
SOCKET=/app/software/mysql/data/3306/mysql.sock
CMD="mysql -u$USER -p$PASSWORD -S $SOCKET"
[ -f /etc/init.d/functions ] && /etc/init.d/functions
start(){
	# mysqld_safe若没有加入环境变量,需要添加绝对路径,使用which mysqld_safe查看绝对路径
	/usr/local/sbin/mysqld_safe --defaults-file=/app/software/mysql/data/3306/my.cnf &>/dev/null &
	# 接收上一条shell命名的结果
	RETVAL=$?
	if [ $RETVEL -eq 0 ]; then
		action "start mysql" /bin/true
	else
		action "check $1" /bin/false
	fi
	return $RETVEL
}
stop(){
	/usr/local/sbin/mysqladmin -u$USER -p$PASSWORD -S $SOCKET shutdown >/dev/null 2>&1
	# 接收上一条shell命名的结果
	RETVAL=$?
	if [ $RETVEL -eq 0 ]; then
		action "start mysql" /bin/true
	else
		action "check $1" /bin/false
	fi
	return $RETVEL
}
case "$1" in
	start )
		start
		;;
	stop )
		stop
		;;
	restart)
		stop
		start
		;;
	* )
		echo "start|stop|restart should be input"
		exit 1
		;;
esac