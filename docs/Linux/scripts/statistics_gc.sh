#!/bin/bash
# 从 test.log 中截取当天的所有 gc 信息日志,并统计 gc 时间的平均值和时长最长的时间
#!/bin/bash
awk '{print $2}' hive-server2.log | tr -d ':' | awk '{sum+=$1} END {print "avg: ", sum/NR}' >>capture_hive_log.log
awk '{print $2}' hive-server2.log | tr -d ':' | awk '{max = 0} {if ($1+0 > max+0) max=$1} END {print "Max: ", max}'>>capture_hive_log.log