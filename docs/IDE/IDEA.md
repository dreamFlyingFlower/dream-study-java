# IDEA



# 安装

* 安装完成后不要第一时间打开,配置idea.properties,将源文件地址和缓存地址修改成其他盘





# Editor

* File->Settings->Editor:编辑配置



## General



### 鼠标滚轮控制字体大小

* Change font size(Zoom) with Ctrl+Mouse Wheel



删除文件末尾空白

* Remove trailing blank lines at the end of saved files



### 自动提示忽略大小写

* Code Completion:取消勾选Match case



### 自动导包

* AutoImport:勾选Java中的Add unambigous...和Optimize imports...
* 快捷键:ALT+ENTER



### 系统默认自动补全

* 需自行修改,用在变量后面



## Code Editing



### 鼠标滑过显示文档

* 勾选Show quick documentation on mouse move
* Tooltip delay:设置提示出现的延迟时间



### 设置编码

* File Encodings:Global Encoding,Project Encoding,Default encoding for properties files都设置为UTF-8



## File and Code templates



### 新建文件自定义时间

* Includes:

```java
#set($str = "")
#set($stringClass=$str.getClass())
#set($localeClass=$stringClass.forName("java.util.Locale")) 
#set($date=$stringClass.forName("java.util.Date").newInstance())
#set($locale=$localeClass.getConstructor($stringClass).newInstance("en_US"))
#set($dateFormat=$stringClass.forName("java.text.SimpleDateFormat").getConstructor($stringClass, $localeClass).newInstance("yyyy-MM-dd HH:mm:ss", $locale))
#set($fdate=$dateFormat.format($date)) 

/**
 *
 * @date : ${fdate}
 */
```



## Live Templates 



### 自定义注释时间

* 新建一个变量名,不能为系统已经设置好的重名,如dateFormat,abbreviation设置为dateFormat,template text输入{$dateFormat$}
* 点击右边的Edit variables,在弹出框中Name设置为dateFormat,Expression设置为date("自定义的格式,与Java时间格式相同")
* default value和不填,skip if defined勾选,在File and code template中可以使用${dateFormat}连接当前定义的表达式



## File Types



### 隐藏文件

* 在ignore files and folders中添加



### 格式化设置

* Code Style->Java
* Wrapping and Braces:
  * Keep when reformatting:取消勾选Line breaks,Commetn at first column,Control statement in one line
  * Method declaration parameters:设置为:Wrap if long
  * Enum constants:wrap always
* Blank Lines:
  * Minimum Blank Lines:
    * Before package statement:0
    * After package statement:1
    * Before imports:1
    * After imports:1
    * Around class:1
    * After class header:1
    * Before class end:0
    * After anonymous class header:1
    * Around field in interface:1
    * Around field:1
    * Around method in interface:1
    * Around method:1
    * Before method body:1
    * Around initializer:1

* JavaDoc:
  * Alignment:
    * Align parameter description:取消勾选
    * Align thrown exception descriptions:取消勾选
  * Other:
    * Generate "<p>" on empty lines:取消勾选



## Code Style



### 设置导包不显示`*`

* Java->Imports:class count to use...和Names count to use...设置更大的值



## Inspections



### 自动生成序列号

* 搜索Serializable,选择Serialization issues,勾选Serializable class without serialVersionUID
* 在Java类中右键可看到serialVersionUID选项



# Build,Execution,Deployment

* File->Settings->Build,Execution,Deployment:构建,执行配置



## Complier



### 自动编译

* 勾选Build project automatically,Compile independent modules in parallel
* 不能对SpringBoot项目进行热部署开发,需要其他配置,且自动编译极慢极耗资源





# File->Project Structure



## Project Settings



### Libraries

* 添加额外的包到项目中



# 双击SHIFT设置



## 显示内存使用

* show Memory indicator:设置为on



# 插件



## Easy_Javadoc

* 快速给Java中的类,属性,方法生成注释,会根据英文命名自动生成中文注释,快捷键`CTRL+\`.也可以批量生成注释,快捷键`CTRL+SHIFT+\`
* 在Other Settings中自定义注释模板



## Easy Code

* 快速生成entity,dao,service,controller

![](easy_code01.png)

![](easy_code02.png)



## Lombok

* Lombok插件



## RestfulTool

* 维护项目中所有请求以及对应的类,主要是Controller中的值,快捷键`CTRL+ALT+N`
* IDEA右侧会出现一栏RestServices,这里有整个项目的http请求,可以简单的进行测试



## String Manipulation

* 字符串转换工具,可以进行驼峰,蛇形相互转换,快捷键`ALT+M`



## Codota

* 代码提示以及最新的代码样例,需要再Brower for more solutions中搜索,消耗性能



## aiXcoder

* 代码提示,支持相似代码搜索功能



## Java Stream Debugger

* 对Java8的Stream进行Debug