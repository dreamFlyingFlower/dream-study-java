#!/bin/bash
# Crontab    :  59 23 * * * /bin/bash $BASE_PATH/svn_backup_repos.sh >/dev/null 2>&1
# Notes      :  将脚本加入crontab中，每天定时执行
# Description:  用于 Zabbix 监控 Linux 系统用户（shell 为 /bin/bash 和 /bin/sh）密码过期，密码有效期剩余 7 天触发加自动发现用户

diskarray=(`awk -F':' '$NF ~ /\/bin\/bash/||/\/bin\/sh/{print $1}' /etc/passwd`)
length=${#diskarray[@]}

printf "{\n"
printf  '\t'"\"data\":["
for ((i=0;i<$length;i++))
do
    printf '\n\t\t{'
    printf "\"{#USER_NAME}\":\"${diskarray[$i]}\"}"
    if [ $i -lt $[$length-1] ];then
            printf ','
    fi
done
printf  "\n\t]\n"
printf "}\n"

检查用户密码过期

#!/bin/bash

export LANG=en_US.UTF-8

SEVEN_DAYS_AGO=$(date -d '-7 day' +'%s')
user="$1"

# 将Sep 09, 2018格式的时间转换成unix时间
expires_date=$(sudo chage -l $user | awk -F':' '/Password expires/{print $NF}' | sed -n 's/^ //p')
if [[ "$expires_date" != "never" ]];then
    expires_date=$(date -d "$expires_date" +'%s')

    if [ "$expires_date" -le "$SEVEN_DAYS_AGO" ];then
        echo "1"
    else
        echo "0"
    fi
else
    echo "0"
fi