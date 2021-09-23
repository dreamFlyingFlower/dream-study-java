# Maven

# 配置文件

* 下载maven压缩文件解压之后的conf/settings.xml

* maven默认远程仓库地址在MAVEN_HOME/lib/maven-model-builder-xxx.jar的pom.xml中

* 配置本地仓库地址和远程仓库地址

  ```xml
  <!-- 本地仓库地址,默认在用户目录下 -->
  <localRepository>${user.home}/.m2/repository</localRepository>
  <!-- 远程仓库镜像地址,默认是maven的中央仓库,服务器在国外,国内可以配置成阿里或其他的 -->
  <mirrors>
      <mirror>
          <!-- id和name可自定义,mirrorOf写成*即可 -->
          <id>nexus-aliyun</id>
          <name>Nexus aliyun</name>
          <!-- 仓库镜像:表示指定仓库使用镜像仓库地址下载jar包 -->
          <!-- 可以写多repository标签中的id值,逗号隔开.*表示所有的仓库都使用镜像仓库地址 -->
          <mirrorOf>*</mirrorOf>
          <!-- 远程公有或私有仓库地址 -->
          <url>https://maven.aliyun.com/repository/public</url>
      </mirror>
  </mirrors>
  <!-- 修改JDK版本 -->
  <profile>
      <!-- id可自定义 -->
      <id>jdk1.8</id>
      <activation>
          <!-- 是否启用:true启用,false不启用 -->
          <activeByDefault>true</activeByDefault>
          <!-- JDK版本 -->
          <jdk>1.8</jdk>
      </activation>
      <properties>
          <maven.compiler.source>1.8</maven.compiler.source>
          <maven.compiler.target>1.8</maven.compiler.target>
          <maven.compiler.compilerVersion>1.8</maven.compiler.compilerVersion>
      </properties>
  </profile>
  ```



# 常用命令

* mvn clean:清理缓存,下载依赖,移除上一次构建生成的文件
* mvn compile:将当前项目重新进行编译
* mvn test-compile:编译测试程序
* mvn test:执行测试程序
* mvn package:接受编译好的代码,将代码打包成war或jar
* mvn clean package -Dmaven.test.skip=ture:清理打包时跳过测试
* mvn install:将项目安装到本地的maven仓库中,可以让其他项目进行依赖
* mvn deploy:将最终的包复制到远程仓库,让其他开发人员与项目共享
* mvn site:生成项目的站点文档
* mvn versions:set -DnewVersion=0.0.2:设置父子模块的新版本号,修改后父子模块都会改版本号
* mvn versions:update-child-modules:根据父模块版本号更新子模块版本号



# 依赖关系

* 写在pom.xml每个dependency的scope中
* 假设A为主项目,B依赖A项目
* compile:默认,A,B都可以使用,可以向下传递.参与编译打包部署
* test:A可用,B不可用,不能向下传递,不参与打包编译部署
* provided:A可用,B不可用,不能向下传递,参与编译,不参与打包部署
* runtime:编译时不依赖,运行打包时需要依赖
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



# Nexus私服

* 使用nexus sonatype搭建私服,官网上下载压缩包,解压后有2个文件夹:一个是nexus的运行程序,配置文件等;另外一个是nexus从远程仓库下载到本地的jar包存放地址
* 配置nexus的环境变量,需要先修改bin/jsw/conf/wrapper.confg文件的wrapper.java.command的值为JDK的绝对路径,需要到bin这一级,之后在控制台使用命令:nexus install安装nexus
* 安装完之后控制台:nexus start/nexus stop启动/停止nexus
* 网页打开nexus控制台:localhost:8081,默认用户名密码为admin,admin123,默认密码都是用户名加123
* Repository:
  * 3rd party:上传公有仓库中没有的jar包
  * Apache Snapshots:apache的测试jar包
  * Central:私有仓库的中央仓库.当jar包在私有仓库没有的时候,从公有仓库下载到该仓库下
  * Releases:内部上传的项目打包后的jar包,版本后缀必须是Release,使用mvn:deploy上传
  * Snapshots:内部上传的项目打包后的jar包,版本后缀必须是Snapshot,使用mvn:deploy上传
* Type:
  * hosted:内部使用.将内部的项目上传到Maven私服做其他内部项目的依赖
  * proxy:代理.私有仓库没有的时候,从公有仓库下载,如Maven或阿里的公有仓库
  * group:组仓库.当jar需要从多个私有仓库中获取时,在pom.xml中配置多个仓库就很繁琐,可以使用group类型,将其他仓库添加到该组中,pom.xml中只需要写该组仓库的地址即可



## POM

* 在pom.xml中指定jar包下载地址,而不使用maven配置文件中的仓库地址

```xml
<!-- 从指定仓库地址中下载JAR -->
<repositories>
    <repository>
        <!-- 仓库编号,唯一,自定义 -->
        <id>repository-personal</id>
        <!-- 仓库名称,唯一,自定义 -->
        <name>Repository Personal</name>
        <!-- 仓库地址,此处的配置主要写私有仓库地址 -->
        <url>http://maven.aliyun.com/repository/public</url>
        <releases>
            <!-- 是否可以使用release版本的jar包,默认false -->
            <enabled>true</enabled>
        </releases>
        <snapshots>
            <!-- 是否可以使用snapshot版本的jar包,默认false -->
            <enabled>true</enabled>
        </snapshots>
    </repository>
</repositories>
<!-- 项目发布到私服 -->
<distributionManagement>
    <repository>
        <id>releases</id>
        <url>http://localhost:8081/nexus/content/repositories/releases</url>
    </repository>
    <snapshotRepository>
        <id>snapshots</id>
        <url>http://localhost:8081/nexus/content/repositories/snapshots</url>
    </snapshotRepository>
</distributionManagement>
```

* 如果使用私有仓库,则每个项目中都需要配置该仓库,太过繁琐.可以修改maven的配置文件,使每个项目都使用私有仓库,配置好之后,可以注释掉pom中的仓库地址

```xml
<servers>
    <!-- 当需要向私服本地项目版本时,若匿名用户就可以发布jar包,则无需设置用户名和密码 -->
    <!-- 若需要验证用户,则需要本文件中配置登录私服的用户名和密码 -->
    <!-- id需要和pom文件中各种distributionManagement的id标签对应 -->
	<server>
    	<id>releases</id>
        <username>username</username>
        <password>username123</password>
    </server>
    <server>
    	<id>snapshots</id>
        <username>username</username>
        <password>username123</password>
    </server>
</servers>
<profiles>
    <profile>
        <!-- 配置仓库地址的id,唯一,自定义 -->
        <id>profile-personal</id>
        <!-- 私有仓库地址,和mirrors中不同的是,此处的配置需要开启之后才生效 -->
        <repositories>
            <repository>
                <!-- 仓库编号,唯一,自定义 -->
                <id>repository-personal</id>
                <!-- 仓库名称,唯一,自定义 -->
                <name>Repository Personal</name>
                <!-- 仓库地址,此处的配置主要写私有仓库地址 -->
                <url>http://maven.aliyun.com/repository/public</url>
                <releases>
                    <!-- 是否可以使用release版本的jar包,默认false -->
                    <enabled>true</enabled>
                </releases>
                <snapshots>
                    <!-- 是否可以使用snapshot版本的jar包,默认false -->
                    <enabled>true</enabled>
                </snapshots>
            </repository>
        </repositories>
    </profile>
</profiles>
<activeProfiles>
    <!-- 激活私有仓库地址 -->
    <activeProfile>profile-personal</activeProfile>
</activeProfiles>
```

* mvn deploy:将项目发布到私服