#!/bin/bash
# 输出带颜色的文字
RED_COLOR='\E[1;31m'
GREEN_COLOR='\E[1;32m'
YELLOW_COLOR='\E[1;33m'
BLUE_COLOR='\E[1;34m]'
PINK_COLOR='\E[1;35m'
# 闪烁
SHAN_COLOR='\E[31;5m'
END_COLOR='\E[0m'
if [ $# -eq 0 ]; then
	echo "the param num is wrong,at least one"
	exit 1
fi
if [ $# -eq 2 ]; then
	PARAM=$( tr '[:upper:]' '[:lower:]' <<<"$2" )
	case "$PARAM" in
		red )
		echo -e "${RED_COLOR}$1${END_COLOR}"
		;;
		green )
		echo -e "${GREEN_COLOR}$1${END_COLOR}"
		;;
		yellow )
		echo -e "${YELLOW_COLOR}$1${END_COLOR}"
		;;
		blue )
		echo -e "${BLUE_COLOR}$1${END_COLOR}"
		;;
		pink )
		echo -e "${PINK_COLOR}$1${END_COLOR}"
		;;
		*)
		echo -e "${RED_COLOR}$1${END_COLOR}"
		;;
	esac
else
	echo -e "${RED_COLOR}$1${END_COLOR}"
fi
