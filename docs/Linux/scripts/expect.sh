#!/bin/bash

# 使用 expect 工具自动交互密码远程其他主机安装 httpd 软件,

# 如果没有进行密钥绑定(~/.ssh/known_hosts),ssh 远程任何主机都会询问是否确认要连接该主机
expect <<EOF
spawn ssh 192.168.1.150

expect "yes/no" {send "yes\r"}
# 根据自己的实际情况将密码修改为真实的密码字串
expect "password" {send  "密码\r"}
expect "#" {send  "yum ‐y install httpd\r"}
expect "#" {send  "exit\r"}
EOF