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