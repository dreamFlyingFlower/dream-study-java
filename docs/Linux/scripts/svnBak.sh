#!/bin/bash
# Crontab    :  59 23 * * * /bin/bash $BASE_PATH/svn_backup_repos.sh >/dev/null 2>&1
# Notes      :  将脚本加入crontab中，每天定时执行
# Description:  SVN完全备份

set -e

SRC_PATH="/opt/svndata"
DST_PATH="/data/svnbackup"
LOG_FILE="$DST_PATH/logs/svn_backup.log"
SVN_BACKUP_C="/bin/svnadmin hotcopy"
SVN_LOOK_C="/bin/svnlook youngest"
TODAY=$(date +'%F')
cd $SRC_PATH
ALL_REPOS=$(find ./ -maxdepth 1 -type d ! -name 'httpd' -a ! -name 'bak' | tr -d './')

# 创建备份目录，备份脚本日志目录
test -d $DST_PATH || mkdir -p $DST_PATH
test -d $DST_PATH/logs || mkdir $DST_PATH/logs
test -d $DST_PATH/$TODAY || mkdir $DST_PATH/$TODAY

# 备份repos文件
for repo in $ALL_REPOS
do
    $SVN_BACKUP_C $SRC_PATH/$repo $DST_PATH/$TODAY/$repo

    # 判断备份是否完成
    if $SVN_LOOK_C $DST_PATH/$TODAY/$repo;then
        echo "$TODAY: $repo Backup Success" >> $LOG_FILE 
    else
        echo "$TODAY: $repo Backup Fail" >> $LOG_FILE
    fi
done

# # 备份用户密码文件和权限文件
cp -p authz access.conf $DST_PATH/$TODAY

# 日志文件转储
mv $LOG_FILE $LOG_FILE-$TODAY

# 删除七天前的备份
seven_days_ago=$(date -d "7 days ago" +'%F')
rm -rf $DST_PATH/$seven_days_ago