#!/bin/bash
# 单独备份mysql数据中的每个数据库
BACKPATH=/app/back/mysql
USER=root
PASSWORD=123456
SOCKET=/software/mysql/data/3306/mysql.sock
CMD="mysql -u$USER -p$PASSWORD -S $SOCKET"
DUMP="mysqldump -u$USER -p$PASSWORD -S $SOCKET -x -B -F -R"
DBLIST=`$CMD -e "show databases;"|sed 1d|egrep -v "_schema|mysql"`
[ ! -d $BACKPATH ]&& mkdir -p $BACKPATH
for dbname in $DBLIST; do
	$DUMP $dbname |gzip > /app/back/mysql/${dbname}_${date +%F}.sql.gz
done