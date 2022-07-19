#!/bin/bash
# 清除/var/log/messages日志
LOG_DIR=/var/log
ROOT_UID=0
# 要使用root权限运行
if [ "$UID" -ne "$ROOT_UID" ]; then
	echo "Must be root to run this script."
	exit 1
fi
cd $LOG_DIR||{
	echo "Can't change to necessary directory" >&2
	exit 1
}
cat /dev/null > /messages && echo "Logs cleaned up"
exit 0