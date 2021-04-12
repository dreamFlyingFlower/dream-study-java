#!/bin/bash
# mysql判断主从故障
USER=root
PASSWORD=123456
SOCKET=/app/software/mysql/data/3306/mysql.sock
CMD="mysql -u$USER -p$PASSWORD -S $SOCKET"
# 可以跳过的主从同步错误码
ERRNO=(1158 1159 1007 1008 1062)
STATUS=($($CMD -e "show slave status\G"|egrep "Seconds_Behind|_Running|Last_SQL_Errno"|awk '{print $NF}'))
if [ "${STATUS[0]}" = "Yes" -a "${STATUS[1]}" = "Yes" -a "${STATUS[2]}" = "0" ]; then
	echo "mysql slave is ok"
else
	for ((i=0;i<${ERRNO[*]};i++)); do
		if [ "${ERRNO[i]}" = "${STATUS[3]}" ]; then
			$CMD -e "stop slave;"
			$CMD -e "set global sql_slave_skip_counter=1;"
			$CMD -e "start slave;"
		fi
	done

	sleep 2
	STATUS=($($CMD -e "show slave status\G"|egrep "Seconds_Behind|_Running|Last_SQL_Errno"|awk '{print $NF}'))
	if [ "${STATUS[0]}" = "Yes" -a "${STATUS[1]}" = "Yes" -a "${STATUS[2]}" = "0" ]; then
		exit 0
	fi

	echo "mysql slave is fail `date +%F\ %T`" >/tmp/mysql.log
	mail -s "mysql slave is fail `date +%F\ %T`" 123456@qq.com </tmp/mysql.log
fi