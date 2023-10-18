#!/bin/bash

# 取当前目录中最近修改的tar.gz名称
TAR_NAME=`ls -lt|grep ".tar.gz"|head -n 1|awk '{print $9}'`
# 取当前目录中最近修改的启动包名
PACKAGE_NAME=
# 当前目录绝对路径
BASE_PATH=$(pwd)
# 压缩包路径
ZIP_PATH="${BASE_PATH}/${TAR_NAME}"
# 程序进程PID
PID=

# 解压
function untar(){
    # jar文件存在,直接启动
    PACKAGE_NAME=`ls -lt|grep ".jar"|head -n 1|awk '{print $9}'`
    if [ ${PACKAGE_NAME} ];then
        echo "JAR启动文件已经存在,直接启动程序"
    else
        if [ ${TAR_NAME} ];then
            echo "jar文件不存在,解压tar.gz文件---------------------------------------------"
            echo "解压磁盘路径:${BASE_PATH}"

            # 解压
            tar -zxvf ${ZIP_PATH} -C ${BASE_PATH}

            # 设置执行权限
            chmod +x ${BASE_PATH}/${PACKAGE_NAME}

            echo "解压完成---------------------------------------------"
        else
            echo "tar.gz压缩文件不存在,解压失败"
            exit
        fi
    fi
}

# 检测PID
function getPid(){
    echo "检测状态---------------------------------------------"
    PACKAGE_NAME=`ls -lt|grep ".jar"|head -n 1|awk '{print $9}'`
    PID=`ps -ef | grep ${PACKAGE_NAME} | grep -v grep | awk '{print $2}'`
    if [ ${PID} ]
    then
        echo "${PACKAGE_NAME}运行pid：${PID}"
    else
        echo "${PACKAGE_NAME}未运行"
    fi
}

# 停止
function stop(){
    echo "stop-------------------------------------------------"
    getPid
    if [ ${PID} ];then
        echo "停止程序---------------------------------------------"
        kill -9 ${PID}
    fi
}

# 启动
function run(){
    echo "启动程序---------------------------------------------"
    echo "程序名称：${PACKAGE_NAME}"

    #进入运行包目录
    cd ${BASE_PATH}

    nohup java -jar ${BASE_PATH}/${PACKAGE_NAME} &

    tail -fn 200 ${BASE_PATH}/nohup.out
}

# 启动
function start(){
    untar
    stop
    sleep 2
    run
}

# 不带参数.默认重启
if [ ${#} -eq 0 ]
then
    start
fi

# 带参数,根据参数执行
if [ ${#} -ge 1 ]
then
    case ${1} in
        "start")
            start
        ;;
        "restart")
            start
        ;;
        "stop")
            stop
        ;;
        *)
            echo "${1}无任何操作"
        ;;
    esac
else
    echo "
    command如下命令：
    无参:默认启动
    start:启动
    stop:停止进程
    restart：重启

    示例命令如：./run.sh start
    "
fi