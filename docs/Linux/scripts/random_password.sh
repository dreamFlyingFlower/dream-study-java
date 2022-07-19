#!/bin/bash


################## 1 ##################


# 生成随机密码(urandom 版本) 

# /dev/urandom 文件是 Linux 内置的随机设备文件
# cat /dev/urandom 可以看看里面的内容,ctrl+c 退出查看
# 查看该文件内容后,发现内容有些太随机,包括很多特殊符号,我们需要的密码不希望使用这些符号
# tr ‐dc '_A‐Za‐z0‐9' < /dev/urandom
# 该命令可以将随机文件中其他的字符删除,仅保留大小写字母,数字,下划线,但是内容还是太多
# 我们可以继续将优化好的内容通过管道传递给 head 命令,在大量数据中仅显示头 10 个字节
# 注意 A 前面有个下划线
tr -dc '_A‐Za‐z0‐9' </dev/urandom | head -c 10


################## 2 ##################


# 生成随机密码(字串截取版本) 

# 设置变量 key,存储密码的所有可能性(密码库),如果还需要其他字符请自行添加其他密码字符
# 使用$#统计密码库的长度
key="0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
num=${#key}
# 设置初始密码为空
pass=''
# 循环 8 次,生成随机密码
# 每次都是随机数对密码库的长度取余,确保提取的密码字符不超过密码库的长度
# 每次循环提取一位随机密码,并将该随机密码追加到 pass 变量的最后
for i in {1..8}
do  
  index=$[RANDOM%num]
  pass=$pass${key:$index:1}
done
echo $pass


################## 3 ##################


# 生成随机密码(UUID 版本,16 进制密码) 
uuidgen


################## 4 ##################


# 生成随机密码(进程 ID 版本,数字密码)
echo $$