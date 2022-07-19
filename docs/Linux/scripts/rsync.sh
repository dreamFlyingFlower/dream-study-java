#!/bin/bash
# 启动,停止rsync,并加入到自启动中.rsync启动时会在指定目录下新建一个pid文件,判断该文件是否存在判断rsync是否启动或停止
[ -f /etc/init.d/functions ] && . /etc/init.d/functions
PIDFILE=/var/run/rsyncd.pid
judge(){
	RETVAL=$?
	if [ $RETVAL -eq 0 ]; then
		action "rsync is $1" /bin/true
	else
		action "rsync is $1" /bin/false
	fi
	return $RETVAL
}
start(){
	if [ -f $PIDFILE ]; then
		echo "rsync server is running ..."
		RETVAL=$?
	else
		rsync --daemon
		judge started
	fi
	return $RETVAL
}
stop(){
	if [ ! -f $PIDFILE ]; then
		echo "rsync server is already stopped ..."
		RETVAL=$?
	else
		kill -USR2 $(cat $PIDFILE)
		rm -f $PIDFILE
		judge stopped
	fi
	return $RETVAL
}
case "$1" in
	start )
		start
		RETVAL=$?
		;;
	stop )
		stop
		RETVAL=$?
		;;
	restart )
		stop
		sleep 2
		start
		RETVAL=$?
		;;
	*)
		"USAGE:$0 {start|stop|restart}"
		exit 1
esac
exit $RETVAL