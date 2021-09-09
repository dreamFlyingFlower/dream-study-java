# Maven

# 配置文件

* 下载maven压缩文件解压之后的conf/settings.xml

* 配置本地仓库地址和远程仓库地址

  ```xml
  <!-- 本地仓库地址,默认在用户目录下 -->
  <localRepository>${user.home}/.m2/repository</localRepository>
  <!-- 远程仓库地址,默认是maven的中央仓库,在国外,可以配置成阿里或其他的 -->
  <mirrors>
  	<mirror>
          <!-- id和name可自定义,mirrorOf写成*即可 -->
      	<id>nexus-aliyun</id>
          <mirrorOf>*</mirrorOf>
          <name>Nexus aliyun</name>
          <!-- 远程仓库地址 -->
          <url>https://maven.aliyun.com/repository/public</url>
      </mirror>
  </mirrors>
  ```



# 常用命令

* mvn clean:清理缓存,下载依赖等
* mvn compile:将当前项目重新进行编译
* mvn test-compile:编译测试程序
* mvn test:执行测试程序
* mvn package:程序打包成war或jar
* mvn clean package -Dmaven.test.skip=ture:清理打包时跳过测试
* mvn install:安装到本地的maven仓库中
* mvn site:生成站点
* mvn deploy:部署



# 依赖关系

* 写在pom.xml每个dependency的scope中
* 假设A为主项目,B依赖A项目
* compile:默认,A,B都可以使用,可以向下传递.参与编译打包部署
* test:A可用,B不可用,不能向下传递,不参与打包编译部署
* provided:A可用,B不可用,不能向下传递,参与编译,不参与打包部署
* 同一个jar包被多次引用时,引用先声明的dependency中的



# 创建本地maven模版
* 控制台到maven项目中: mvn archetype:create-from-project
* 构建成功后会在本项目的target\generated-sources\archetype目录下生成必要的文件
* 控制台进入archetype目录下:mvn install.
* 构建成功后会在本地仓库中生成一个archetype-catalog.xml的文件,可添加到eclipse的maven->archetype->add local中



# 上传JAR到本地仓库

```shell
# DgroupId,DartifactId,Dversion:顾名思义是值依赖里的三项,随意填写
# Dfile:指明需要导入的jar包的本地地址
mvn install:install-file -DgroupId=com.wy -DartifactId=java-utils -Dversion=0.1 -Dfile=E:\xxxx-0.0.1.jar -Dpackaging=jar
```



# 上传JAR到公有仓库

* [教程1](https://www.sojson.com/blog/250.html),[教程2](https://www.cnblogs.com/binarylei/p/8628245.html)

* 发布issue地址,[官网](https://issues.sonatype.org/browse/OSSRH-42504?filter=-2),Gpg4win生成[密钥](https://www.gpg4win.org/download.html)
* 申请的时候需要需要选择最上方的create
* project要选择Community Support - Open Source Project Repository Hosting (OSSRH)
* issue type:new project
* groupid:如果有自己的域名服务器,可以写自己的,如果是托管在git或其他服务器的,写托管服务器的
* scmurl:项目url地址
* 创建完成之后等待工作人员审核,有时差,可能一天时间,碰到休息日,3天
* 审核通过之后需要打包发布项目到maven
* pom文件的groupid要和申请的一样,url标签必须写正确,否则后面会报错,上传不通过
* pom除了dependencies之外,其余的按照自己的写,plugins和profiles可照写,version可自己改
* 发布时候需要改写本地maven的setting.xml文件,若是用eclipse不发,需要修改eclipse的maven配置
* setting.xml的server中要写自己的ossr的申请的用户名和密码,可以配置2个,也可配置一个

```xml
<!-- Maven的settings.xml -->
<servers>
    <server>
        <id>nexus-snapshots</id>
        <username></username>
        <password></password>
    </server>
    <server>
        <id>nexus-release</id>
        <username></username>
        <password></password>
    </server>
</servers>
```

* id要和pom文件中distributionManagement标签中的id一样,url是固定的ossr的服务
* pom文件中必须配置jar-source和javadoc的插件,否则报错
* javadoc插件最好加上-Xdoclint:none这几行,因为javadoc要生成文档,若是某些方法有注释,但是
  没有param,也没有return这些系统已经定义好的标签,打包报错.自定义的标签也报错
* gpg加密插件,需要在gpg的官网上下载,并且在本地生成一个公钥,之后上传到服务器,直接用图形界面操作即可
* 最终发布命令,进入到当前文件夹:mvn clean deploy -P release -Dgpg.passphrase=密码

* 发布完成后浏览自己发布的jar包,[地址](https://oss.sonatype.org)



# 本地搭建私服仓库

* 使用nexus sonatype,官网上下载压缩包,解压后有2个文件夹:一个是nexus的运行程序,配置文件等;另外一个文件夹则是nexus从远程仓库下载到本地的jar包存放地址
* 配置nexus的环境变量,使用前需要先修改bin/jsw/conf/wrapper.confg文件,修改wrapper.java.command的值为当前系统jdk的安装目录,需要到bin这一级,并且是绝对地址,之后在控制台使用命令:nexus install安装nexus
* 安装完之后控制台:nexus start启动nexus.启动之后可在控制台打开localhost:8081,用户名密码为admin,admin123