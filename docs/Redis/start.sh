#!/bin/sh
#chkconfig: 2345 80 90
# 以下参数根据实际情况修改
REDISPORT=6379
EXEC=/usr/local/bin/redis-server
CLIEXEC=/usr/local/bin/redis-cli
PIDFILE=/var/run/redis_${REDISPORT}.pid
CONF="/etc/redis/${REDISPORT}.conf"

function start(){
	if [ -f $PIDFILE ];then
		echo "$PIDFILE exists, process is already running or crashed"
	else
		echo "Starting Redis server..."
		$EXEC $CONF &
	fi
}

function stop(){
	if [ ! -f $PIDFILE ];then
		echo "$PIDFILE does not exist, process is not running"
	else
		PID=$(cat $PIDFILE)
		echo "Stopping ..."
		$CLIEXEC -p $REDISPORT shutdown
		while [ -x /proc/${PID} ]
		do
			echo "Waiting for Redis to shutdown ..."
			sleep 1
		done
		echo "Redis stopped"
	fi	
}

case "$1" in
	start)
		start
		;;
	stop)
		stop
		;;
	restart)
		stop
		sleep 2
		start
		;;
	*)
		echo "Please use start or stop or restart as first argument"
		;;
esac