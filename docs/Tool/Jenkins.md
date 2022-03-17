# Jenkins



# 概述



* 自动化集成,部署工具



# 安装



* 下载压缩包,解压到/software/jenkins
* 配置环境变量:vi /etc/profile,添加如下

```shell
export JENKINS_HOME=/software/jenkins
source /etc/profile
```



# Configure



* 配置JDK,可以使用自动安装,系统自带的,也可以用自定义的安装路径.可以安装多个版本JDK

* 配置Maven,同安装JDK.注意需要设置Maven的配置,修改Maven仓库路径

* 修改Jenkins用户和端口:vi /etc/sysconfig/jenkins

  ```shell
  JENKINS_USER="root"
  JENKINS_PORT="8888"
  ```

* 启动:systemctl start jenkins

* 如果启动出现:`usr/bin/java: No such file or directory`,做一个软连接到/usr/bin/java

  * `ln -s /usr/local/java/jdk8/bin/java /usr/bin/java`

* 生成密码:`cat /var/lib/jenkins/secrets/initialAdminPassword`



# 插件管理



* 安装publish over ssh和ssh plugin,主要用来将服务部署到远程服务器上



## JDK



* JDK已经安装过了,不需要再次自动安装,但是需要手动指定别名和Java_home



## Maven



* 取消自动安装,将本机上的maven地址填入即可



## Git



* GIT plugin:git插件



# 使用



* 构建一个maven项目
* 点击源码管理,将git仓库的地址填入其中,根据实际情况添加密钥验证
* build的Root Pom需要根据实际情况选择pom.xml文件.Goals只需要写maven后的命令