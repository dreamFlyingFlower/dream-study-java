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
* build的Root Pom需要根据实际情况选择pom.xml文件,一般直接写pom.xml即可.Goals and Options填写需要执行的maven命令,如maven clean package
* post steps:可选择execute shell,添加一些脚本,如重新构建之后重启的脚本等
* 如需通过git进行自动构建,则需要通过git的webhook功能,填入Jenkins项目地址即可



# 自动构建



* 需要先在GitLab设置好webhook,参考官网,webhook地址设置为Jenkins执行项目地址
* 进入新建好的项目,进入配置,选择构建触发器
* 选择`Build when a change is pushed to GitLab.GitLab webhook URL xxxx`: xxxx为webhook地址
* 进入Jenkins设置允许匿名访问Jenkins