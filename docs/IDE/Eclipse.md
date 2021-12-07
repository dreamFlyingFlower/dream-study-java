# Eclipse



# 配置(自用)



## General

* 勾选
  * Always run in background
  * Show heap status



### Editors

#### Text Editors

##### Spelling

* 不勾选
  * Enable spell checking



### WorkSpace

* 勾选
  * Text file encoding->Other->UTF-8
  * New text file line delimiter->Other->Unix



## Java

### Code Style

#### Code Templates

##### Comments

* Files

```java
/**
 * 
 *
 * @author 飞花梦影
 * @date ${currentDate:date('yyyy-MM-dd HH:mm:ss')}
 */
```

* Types

```java
/**
 * 
 * @author 飞花梦影
 * @date ${currentDate:date('yyyy-MM-dd HH:mm:ss')}
 */
```



##### Code

* New Java Files

```java
${filecomment}
${package_declaration}
/**
 * 
 *
 * @author 飞花梦影
 * @date ${currentDate:date('yyyy-MM-dd HH:mm:ss')}
 */
${typecomment}
${type_declaration}
```



#### Formatter

* eclipse_formatter.xml



### Editor

#### Content Assist

* 勾选
  * Completion inserts
  * Insert single proposals automatically
  * Disable insertion triggers except 'Enter'
  * Add import instead of qualified name
    * Use static imports
  * Fill method arguments and show guessed arguments
    * Insert parameter names
  * Show camel case matches
  * Show substring matches
  * Hide proposals not visible in the invocation context
* Auto Activation
  * 勾选Enable auto activation
  * Auto activation delay:自动提示延迟提示时间,单位毫秒,设置100
  * Auto activation triggers for Java:自动提示单词,将数字和字母,点全填进去
  * Auto activation triggers for Javadoc:@#



#### Templates

* priFinalString,Java,new a final String

```java
private final static ${String} ${NAME} = ${null};${cursor}
```

* priFinalHashmap,Java,new a final HashMap

```java
private final static Map<${String},${Object}> ${HASH_MAP} = new HashMap<>();${cursor}
${imp:import(java.util.HashMap,java.util.Map)}
```

* priFinalConmap,Java,new a final ConcurrentHashMap

```java
private final static Map<${String},${Object}> ${CONCURRENT_HASH_MAP} = new ConcurrentHashMap<>();${cursor}
${imp:import(java.util.concurrent.ConcurrentHashMap,java.util.Map)}
```

* priFinalList,Java,new a final ArrayList

```java
private final static List<${String}> ${ARRAY_LIST} = new ArrayList<>();${cursor}
${imp:import(java.util.ArrayList,java.util.List)}
```

* priFinalListMap,Java,new a final List<Map<String,Object>>

```java
private final static List<Map<${String},${Object}>> ${LIST_MAP} = new ArrayList<>();${cursor}
${imp:import(java.util.ArrayList,java.util.List,java.util.Map)}
```

* pstr,Java,new a private String

```java
private String ${NAME};${cursor}
```

* pint,Java,new a private Integer

```java
private Integer ${NAME};${cursor}
```

* plong,Java,new a private Long

```java
private Long ${NAME};${cursor}
```

* plist,Java,new a private List

```java
private List<${String}> ${NAME};${cursor}
${imp:import(java.util.List)}
```

* pmap,Java,new a private Map

```java
private Map<${String},${Object}> ${map};${cursor}
${imp:import(java.util.Map)}
```

* newHashMap,Java,new a Hashmap

```java
Map<${String},${Object}> ${map} = new HashMap<>();${cursor}
${imp:import(java.util.HashMap,java.util.Map)}
```

* newConmap,Java,new a ConcurrentHashMap

```java
Map<${String},${Object}> ${concurrentHashMap} = new ConcurrentHashMap<>();${cursor}
${imp:import(java.util.concurrent.ConcurrentHashMap,java.util.Map)}
```

* newArrayList,Java,new a ArrayList

```java
List<${String}> ${list} = new ArrayList<>()${cursor};
${imp:import(java.util.List,java.util.ArrayList)}
```

* newListMap,Java,new a List<Map>

```java
List<Map<${String},${Object}>> ${listMap} = new ArrayList<>();${cursor}
${imp:import(java.util.ArrayList,java.util.List,java.util.Map)}
```

* apim,Java,add a @ApiModel to a class entity

```java
/**
 * ${comment}
 * 
 * @auther 飞花梦影
 * @date ${currentDate:date('yyyy-MM-dd HH:mm:ss')}
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = "${comment}")${imp:import(io.swagger.annotations.ApiModel)}
```

* apimp,Java type members,add a @ApiModelProperty to a field

```java
/**
 * ${comment}
 */
@ApiModelProperty("${comment}")${cursor}${imp:import(io.swagger.annotations.ApiModelProperty)}
```

* lomAll,Java,add lombok all annotation

```java
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder${imp:import(lombok.AllArgsConstructor,lombok.Builder,lombok.Getter,lombok.NoArgsConstructor,lombok.Setter,lombok.ToString)}
```

* lomData,Java,import lombok data builder

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder${imp:import(lombok.AllArgsConstructor,lombok.Builder,lombok.NoArgsConstructor)}
```

* lomGetSet,Java,import lombok getter and setter

```java
@Getter
@Setter${imp:import(lombok.Getter,lombok.Setter)}
```

* lomSuperAll,Java,add lombok all annotation with superbuilder

```java
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder${imp:import(lombok.AllArgsConstructor,lombok.Getter,lombok.NoArgsConstructor,lombok.Setter,lombok.ToString,lombok.experimental.SuperBuilder)}
```





## Maven

* 勾选
  * Do not automatically...



### UserSettings

* 选择自己的Maven插件,使用自己的Maven配置文件,修改默认的仓库地址,不要放C盘



# 快捷键

* ctrl+shift+r:打开资源,只能打开自己写的文件,不能打开JAR包内的文件
* ctrl+shift+t:打开任何文件,包括资源文件,但不包括class
* ctrl+o:快速outline,查看当前文件的方法以及变量等
* ctrl+2,L:为变量赋值.先按ctrl+2,右下角会出现选项,选择L给变量赋值
* alt+shift+r:重命名文件或方法,需要先选中,会自动修改使用该文件的其他文件,连续按2次会弹出对话框
* alt+shift+m:方法重构
* shift+enter/ctrl+shift+enter:在当前行下/上新增一行空白行
* ctrl+.:将光标移动至当前文件中的下一个报错处或警告处
* ctrl+1:修改建议的快捷键
* ctrl+t:列出接口的实现类列表,再按一次则显示自底层向上的结构
* alt+left:在导航历史记录中后退
* alt+right:在导航历史记录中前进
* ctrl+q:回到最后一次编辑的地方
* ctrl+shift+0:自动导包



# 插件



## SpringTool4

* 生成SpringBoot和SpringCloud项目



## Lombok

* `java -jar lombok.jar`:找到Eclipse安装地址,安装即可

* 若出现错处,可在eclipse.ini中添加如下代码

  ```
  --illegal-access=warn
  --add-opens java.base/java.lang=ALL-UNNAMED
  --add-exports=java.base/sun.nio.ch=ALL-UNNAMED 
  --add-opens=java.base/java.lang.reflect=ALL-UNNAMED 
  --add-opens=java.base/java.io=ALL-UNNAMED 
  --add-exports=jdk.unsupported/sun.misc=ALL-UNNAMED
  ```

  



## MyBatipse

* 配合MyBatis,鼠标左键+CTRL可直接选择跳到相应的XML文件的相应方法中



## DBeaver

* 数据库管理工具,可以代替Navicat



## UMLet

* 画UML流程图,并且可以导出pdf,jpg等格式



## JAutoDoc

* 自动代码注释



## Eclipse Color Theme

* Eclipse多种主题切换



## Bytecode Outline

* 显示Java文件编译后的指令文件,即JVM真正运行时的指令文件



## aiXcoder

* 同codota,需要在Brower for more solutions中搜索



## Codota

* 代码提示以及最新的代码样例,需要在Brower for more solutions中搜索



## ResourceBundle Editor

* 同时修改国际化配置文件



## Jar2UML

* 将Jar文件转换为UML图



## SonarLint

* 代码检查



## Enhanced Class Decompiler

* 无需源码即可debug



## Checkstyle Plug-in

* 检查代码样式