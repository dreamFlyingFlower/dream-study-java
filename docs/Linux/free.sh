#!/bin/bash
# 内存不足,单位为M,发邮件
FREE=`free -m|awk 'NR==3{print $NF}'`
[ "$FREE" -lt 100 ]&&{
	echo "内存不足$FREE" >/opt/mail.txt
	mail -s "free is too low" 12345678@163.com </op/mail.txt
}