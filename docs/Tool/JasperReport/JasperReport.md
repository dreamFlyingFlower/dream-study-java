# JasperReport



# 概述



* 一个强大、灵活的报表生成工具,能够展示丰富的页面内容,并将之转换成PDF
* 通过JasperReports生成PDF报表一共要经过三个阶段:
  * 设计阶段(Design): 创建一些模板,模板包含了报表的布局与设计,包括执行计算的复杂公式、可选的从数据源获取数据的查询语句、以及其它的一些信息.模板设计完成之后,将模板保存为JRXML文件,其实就是一个XML文件
  * 执行阶段(Execution): 使用以JRXML文件编译为可执行的二进制文件(即.Jasper文件)结合数据进行执行,填充报表数据
  * 输出阶段(Export): 数据填充结束,可以指定输出为多种形式的报表



# 原理



* JRXML->Jasper->Jrprint->Exporter->Jasperreport

* JRXML: 报表填充模板,本质是一个XML.JasperReport已经封装了一个dtd,只要按照规定的格式写这个xml文件,jasperReport就可以将其解析最终生成报表
* Jasper: 由JRXML模板编译生成的二进制文件,用于代码填充数据.解析完成后JasperReport就开始将.jrxml文件编译成.jasper文件,因为JasperReport只可以对.jasper文件进行填充数据和转换
* Jrprint: 当用数据填充完Jasper后生成的文件,用于输出报表,是JasperReport的核心所在.它会根据xml里面写好的查询语句来查询指定数据库,也可以控制在后台编写查询语句,参数,数据库.在报表填充完后,会再生成一个.jrprint格式的文件
* Exporter: 决定要输出的报表为何种格式,报表输出的管理类
* Jasperreport可以输出多种格式的报表文件,常见的有Html,PDF,xls等



# Jaspersoft Studio



* [官网](https://community.jaspersoft.com/community-download)
* 一个可视化的报表设计工具,使用该软件可以方便地对报表进行可视化的设计,设计结果为格式.jrxml 的 XML 文件,并且可以把.jrxml 文件编译成.jasper 格式文件方便 JasperReport 报表引擎解析、显示



# 面板介绍



* Report editing area: 主编辑区域,该区域中,可以直观地通过拖动,定位,对齐和通过 Designer palette(设计器调色板)对报表元素调整大小.JasperSoft Studio 有一个多标签编辑器,Design,Source 和 Preview:
  * Design tab: 当你打开一个报告文件,它允许您以图形方式创建报表选中
  * Source tab: 包含用于报表的 JRXML 源代码
  * Preview tab: 允许在选择数据源和输出格式后,运行报表预览
* Repository Explorer view: 包含 JasperServer 生成的连接和可用的数据适配器列表
* Project Explorer view: 包含 JasperReports 的工程项目清单
* Outline view: 在大纲视图中显示了一个树的形式的方式报告的完整结构
* Properties view: 通常被填充与实际所选元素的属性的信息
* Problems view: 显示的问题和错误,例如可以阻断报告的正确的编译
* Report state: 提供了有关在报表编译/填充/执行统计用户有用的信息,错误会显示在这里



# 基本使用



### 模板制作



* 打开Jaspersoft Studio ,新建一个project, 步骤: File -> New -> Project-> JasperReports Project
* 新建一个Jasper Report模板,在左下方Project Explorer 找到刚才新建的Project,步骤:项目右键 -> New -> Jasper Report
* 选择 Blank A4 (A4纸大小的模板),然后 Next 命名为DemoReport1.jrxml.
* 报表模板被垂直的分层,每一个部分都是一个Band,每一个Band的特点不同:
  * Title(标题): 只在整个报表的第一页的最上端显示,只在第一页显示,其他页面均不显示
  * Page Header(页头): 在整个报表中每一页都会显示,在第一页中,出现的位置在 Title Band的下面.在除了第一页的其他页面中Page Header 的内容均在页面的最上端显示
  * Page Footer(页脚): 在整个报表中每一页都会显示,显示在页面的最下端,一般用来显示页码
  * Detail 1(详细):报表内容,每一页都会显示
  * Column Header(列头):Detail中打印的是一张表的话,这Column Header就是表中列的列头
  * Column Footer(列脚):Detail中打印的是一张表的话,这Column Footer就是表中列的列脚
  * Summary(统计):表格的合计段,出现在整个报表的最后一页中,在Detail 1 Band后面,主要是用来做报表的合计显示



### 编译模板



* 右键单机模板文件 -> compile Report 对模板进行编译,生成.jasper文件



### 中文处理



* 设计阶段需要指定中文样式,通过手动指定中文字体的形式解决中文不现实,添加properties文件:

```properties
net.sf.jasperreports.extension.registry.factory.simple.font.families=net.sf.jasperreports.engine.fonts.SimpleFontExtensionsRegistryFactory
net.sf.jasperreports.extension.simple.font.families.lobstertwo=stsong/fonts.xml
```

* 指定中文配置文件fonts.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<fontFamilies>
    <!--<fontFamily name="Lobster Two">-->
    <!--<normal>lobstertwo/LobsterTwo-Regular.otf</normal>-->
    <!--<bold>lobstertwo/LobsterTwo-Bold.otf</bold>-->
    <!--<italic>lobstertwo/LobsterTwo-Italic.otf</italic>-->
    <!--<boldItalic>lobstertwo/LobsterTwo-BoldItalic.otf</boldItalic>-->
    <!--<pdfEncoding>Identity-H</pdfEncoding>-->
    <!--<pdfEmbedded>true</pdfEmbedded>-->
    <!--<!–-->
    <!--<exportFonts>-->
    <!--<export key="net.sf.jasperreports.html">'Lobster Two', 'Times New Roman',Times, serif</export>-->
    <!--</exportFonts>-->
    <!--–>-->
    <!--</fontFamily>-->
    <fontFamily name="华文宋体">
        <normal>stsong/stsong.TTF</normal>
        <bold>stsong/stsong.TTF</bold>
        <italic>stsong/stsong.TTF</italic>
        <boldItalic>stsong/stsong.TTF</boldItalic>
        <pdfEncoding>Identity-H</pdfEncoding>
        <pdfEmbedded>true</pdfEmbedded>
        <exportFonts>
            <export key="net.sf.jasperreports.html">'华文宋体', Arial, Helvetica, sansserif</export>
            <export key="net.sf.jasperreports.xhtml">'华文宋体', Arial, Helvetica, sansserif</export>
        </exportFonts>
    <!--
    <locales>
    <locale>en_US</locale>
    <locale>de_DE</locale>
    </locales>
    -->
    </fontFamily>
</fontFamilies>
```

* 引入字体库stsong.TTF



# Java中使用



* 添加依赖到pom.xml中

```java
@RestController
public class JasperController {

    @GetMapping("/testJasper")
    public void createHtml(HttpServletResponse response, HttpServletRequest request)throws Exception{
        // 引入jasper文件.由JRXML模板编译生成的二进制文件,用于代码填充数据
        Resource resource = new ClassPathResource("templates/test01.jasper");
        // 加载jasper文件创建inputStream
        FileInputStream isRef = new FileInputStream(resource.getFile());
        ServletOutputStream sosRef = response.getOutputStream();
        try {
            // 创建JasperPrint对象
            JasperPrint jasperPrint = JasperFillManager.fillReport(isRef, new HashMap<>(),new JREmptyDataSource());
            // 写入pdf数据
            JasperExportManager.exportReportToPdfStream(jasperPrint,sosRef);
        } finally {
            sosRef.flush();
            sosRef.close();
        }
    }
}
```



# 数据填充



* JasperReport对报表模板中的数据填充有很多中方式,最典型的有以下两种:
  * Parameters:参数填充.通常是从程序里传值到报表里,起参数传递的作用,需要在创建的时候定义它的数据类型
  * DataSource:数据源填充



## 参数Map填充数据



* 创建新模板

* 创建Parameter:

  * 在outline面板中找到Parameters,右键 -> Create Parameter,新建一个Parameter(生成一个Paramerter1)
  * 右键 Paramete1 -> Show Properties.
  * 设置Name为title、Class为java.lang.String
  * Name的值不能重复,因为传入的参数的key就是这个参数名

  ![](JASPERREPORT01.JPG)

* 模板参数设置:将设置好的参数直接拖入表格中对应的位置,并设置好大小与对齐方式

  ![](JASPERREPORT02.jpg)

* PDF输出

  ```java
  @GetMapping("/testJasper02")
  public void createPdf(HttpServletRequest request, HttpServletResponse response) throws IOException {
      // 引入jasper文件
      Resource resource = new ClassPathResource("templates/parametersTest.jasper");
      FileInputStream fis = new FileInputStream(resource.getFile());
      // 创建JasperPrint,向jasper文件中填充数据
      ServletOutputStream os = response.getOutputStream();
      try {
          // parameters集合中传递的key需要和设计模板中使用的name一致
          HashMap parameters = new HashMap();
          parameters.put("title","用户详情");
          parameters.put("username","李四");
          parameters.put("mobile","120");
          JasperPrint print = JasperFillManager.fillReport(fis, parameters,new JREmptyDataSource());
          // 将JasperPrint已PDF的形式输出
          JasperExportManager.exportReportToPdfStream(print,os);
          response.setContentType("application/pdf");
      } catch (JRException e) {
          e.printStackTrace();
      }finally {
          os.flush();
      }
  }
  ```

  

## JDBC数据源



* 配置数据连接

  ![](JASPERREPORT03.JPG)

* 制作空白模板:创建空白模板,并将不需要的Band删除

* 将数据库用户字段配置到模块中.为了方便的进行模板制作,可以将需要数据库表中的字段添加到Studio中.在outline中右键模板,选择dataset and query

  ![](JASPERREPORT04.JPG)

* 用户可以在 SQL 查询语句输入窗口中,输入需要查询数据的查询语句,点击右上角的“Read Fields”按钮,界面下方的字段列表中,就会显示此查询语句中所涵盖的所有字段的列表

  ![](JASPERREPORT05.JPG)

* 在Fields列表中,只保留报表中使用的字段,其他用不到的字段最好用删掉,防止由于数据表变化,导致报表模板中的字段设置与数据表对应不上,导致报表报错.输入完毕确定后,系统即会把查询语句保存在报表模板中

  ![](JASPERREPORT06.JPG)

* 填充Filed.将id,mobile,username等拖入到 Detail Band中设计模板

  ![](JASPERREPORT07.jpg)

* PDF输出

  ```java
  // 测试JDBC连接数据源
  @GetMapping("/testJasper03")
  public void createPdf(HttpServletRequest request, HttpServletResponse response) throws Exception {
      Resource resource = new ClassPathResource("templates/testConn.jasper");
      FileInputStream fis = new FileInputStream(resource.getFile());
      // 创建JasperPrint,向jasper文件中填充数据
      ServletOutputStream os = response.getOutputStream();
      try {
          HashMap parameters = new HashMap();
          // jasper流,参数列表,数据库连接
          JasperPrint print = JasperFillManager.fillReport(fis,parameters,getConnection());
          // 将JasperPrint已PDF的形式输出
          JasperExportManager.exportReportToPdfStream(print,os);
          response.setContentType("application/pdf");
      } catch (JRException e) {
          e.printStackTrace();
      }finally {
          os.flush();
      }
  }
  
  // 创建数据库Connection
  public Connection getConnection() throws Exception {
      String url = "jdbc:mysql://localhost/test";
      Class.forName("com.mysql.jdbc.Driver");
      Connection conn = DriverManager.getConnection(url, "root", "root");
      return conn;
  }
  ```



## JavaBean数据源



* 创建Filed

  ![](JASPERREPORT08.jpg)

* 构造模板

  ![](JASPERREPORT09.JPG)

* PDF输出

  ```java
  // 测试javaBean数据源
  @GetMapping("/testJasper04")
  public void createPdf(HttpServletRequest request, HttpServletResponse response) throws Exception {
      Resource resource = new ClassPathResource("templates/testJavaBean.jasper");
      FileInputStream fis = new FileInputStream(resource.getFile());
      // 创建JasperPrint,向jasper文件中填充数据
      ServletOutputStream os = response.getOutputStream();
      try {
          Map<String,Object> parameters = new HashMap<>();
          JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(getUserList());
          JasperPrint print = JasperFillManager.fillReport(fis, parameters,ds);
          JasperExportManager.exportReportToPdfStream(print,os);
          response.setContentType("application/pdf");
      } catch (JRException e) {
          e.printStackTrace();
      }finally {
          os.flush();
      }
  }
  
  // 创建数据库Connection
  public List<User> getUserList() throws Exception {
      List<User> list = new ArrayList<>();
      for (int i=1;i<=5;i++) {
          User user = new User(i+"", "testName"+i, "10"+i, "企业"+i, "部门"+i);
          list.add(user);
      }
      return list;
  }
  ```



# 分组报表



* 有两种情况会使用分组报表:美观和好看的显示
* 当数据分为两层表时,经常需要批量打印子表的数据.打印时,常常需要按照父表的外键或关联值进行自动分组,即每一条父表记录所属的子表记录打印到一组报表中,每组报表都单独计数及计算页数
* 在应用中,可以通过选择需要打印的父表记录,将父表记录的 ID 传入,由报表自动进行分组
* 新建模板

* 新建报表群组.选中报表名称点击右键,选择菜单中的Create Group

  ![](JASPERREPORT10.JPG)

* 需要设置分组的名称,分组字段,也可以设置按照指定的函数,方法处理后进行分组

  ![](JASPERREPORT11.JPG)

* 按照字段companyName进行分组,设置完毕,点击Next.系统显示细节设置界面,此处可以设置是否加入group header和group footer区.保持默认选中,加入这两个区域,这样可以控制在每组报表的结尾,打印相应的信息,例如统计信息等

  ![](JASPERREPORT12.JPG)

* 放置报表数据.将companyName拖入 Group Header中,会跳出 TextField Wizard框,选中 NoCalculation Function

* 双击 $F{deptId} 会弹出Expression editor框

  ![](JASPERREPORT13.jpg)

* 添加分组Band.将需要作为表头打印的内容拖入 CompanyGroup Header1 栏,将字段拖入 detail 栏,将每个分组结尾需要打印的内容放入 Companygroup footer 栏,将页脚需要打印的内容放入 Page Footer栏

  ![](JASPERREPORT14.jpg)

* PDF输出

  ```java
  // 测试分组
  @GetMapping("/testJasper05")
  public void createPdf(HttpServletRequest request, HttpServletResponse response) throws Exception {
      Resource resource = new ClassPathResource("templates/testGroup.jasper");
      FileInputStream fis = new FileInputStream(resource.getFile());
      ServletOutputStream os = response.getOutputStream();
      try {
          HashMap parameters = new HashMap();
          JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(getUserList());
          JasperPrint print = JasperFillManager.fillReport(fis, parameters,ds);
          JasperExportManager.exportReportToPdfStream(print,os);
          response.setContentType("application/pdf");
      } catch (JRException e) {
          e.printStackTrace();
      }finally {
          os.flush();
      }
  }
  
  // 构造假数据
  public List<User> getUserList() throws Exception {
      List<User> list = new ArrayList<>();
      for(int i=1;i<=3;i++) {
          User user = new User("it00"+i, "itcast"+i, "1380000000"+i, "Test", "TTT");
          list.add(user);
      }
      for(int i=1;i<=3;i++) {
          User user = new User("hm00"+i, "itheima"+i, "1880000000"+i, "Test", "TTT");
          list.add(user);
      }
      return list;
  }
  ```



# Chart图表