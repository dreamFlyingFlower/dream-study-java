#!/bin/bash

################## 1 ##################

# Crontab    :  59 23 * * * /bin/bash $BASE_PATH/svn_backup_repos.sh >/dev/null 2>&1
# Notes      :  将脚本加入crontab中，每天定时执行
# Description:  负载高时,查出占用比较高的进程脚本并存储或推送通知

# 物理cpu个数
physical_cpu_count=$(egrep 'physical id' /proc/cpuinfo | sort | uniq | wc -l)
# 单个物理cpu核数
physical_cpu_cores=$(egrep 'cpu cores' /proc/cpuinfo | uniq | awk '{print $NF}')
# 总核数
total_cpu_cores=$((physical_cpu_count*physical_cpu_cores))

# 分别是一分钟、五分钟、十五分钟负载的阈值，其中有一项超过阈值才会触发
one_min_load_threshold="$total_cpu_cores"
five_min_load_threshold=$(awk 'BEGIN {print '"$total_cpu_cores"' * "0.8"}')
fifteen_min_load_threshold=$(awk 'BEGIN {print '"$total_cpu_cores"' * "0.7"}')

# 分别是分钟、五分钟、十五分钟负载平均值
one_min_load=$(uptime | awk '{print $(NF-2)}' | tr -d ',')
five_min_load=$(uptime | awk '{print $(NF-1)}' | tr -d ',')
fifteen_min_load=$(uptime | awk '{print $NF}' | tr -d ',')

# 获取当前cpu 内存 磁盘io信息，并写入日志文件
# 如果需要发送消息或者调用其他，请自行编写函数即可
get_info(){
    log_dir="cpu_high_script_log"
    test -d "$log_dir" || mkdir "$log_dir"
    ps -eo user,pid,%cpu,stat,time,command --sort -%cpu | head -10 > "$log_dir"/cpu_top10.log
    ps -eo user,pid,%mem,rss,vsz,stat,time,command --sort -%mem | head -10 > "$log_dir"/mem_top10.log
    iostat -dx 1 10 > "$log_dir"/disk_io_10.log
}


export -f get_info

echo "$one_min_load $one_min_load_threshold $five_min_load $five_min_load_threshold $fifteen_min_load $fifteen_min_load_threshold" | \
awk '{ if ($1>=$2 || $3>=$4 || $5>=$6) system("get_info") }'


################## 2 ##################

# 查找 Linux 系统中的僵尸进程

# awk 判断 ps 命令输出的第 8 列为 Z 是,显示该进程的 PID 和进程命令
ps aux | awk '{if($8 == "Z"){print $2,$11}}'