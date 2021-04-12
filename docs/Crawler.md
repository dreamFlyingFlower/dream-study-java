# Crawler



# 概述

* 网络爬虫(Web crawler),是一种按照一定的规则,自动地抓取万维网信息的程序或者脚本,它们被广泛用于互联网搜索引擎或其他类似网站,可以自动采集所有其能够访问到的页面内容,以获取或更新这些网站的内容和检索方式
* 从功能上来讲,爬虫一般分为数据采集,处理,储存三个部分
*  传统爬虫从一个或若干初始网页的URL开始,获得初始网页上的URL,在抓取网页的过程中,不断从当前页面上抽取新的URL放入队列,直到满足系统的一定停止条件
* 聚焦爬虫的工作流程较为复杂,需要根据一定的网页分析算法过滤与主题无关的链接,保留有用的链接并将其放入等待抓取的URL队列.然后,再根据一定的搜索策略从队列中选择下一步要抓取的网页URL,并重复上述过程,直到达到系统的某一条件时停止.所有被爬虫抓取的网页将会被系统存贮,进行一定的分析,过滤,并建立索引,以便之后的查询和检索.对于聚焦爬虫来说,这一过程所得到的分析结果还可能对以后的抓取过程给出反馈和指导
* 爬虫是一个模拟人类请求网站行为的程序.可以自动请求网页,把数据抓取下来,然后使用一定的规则提取有价值的数据
* 聚焦爬虫:通常我们自己撸的为聚焦爬虫面向主题爬虫,面向需求爬虫:会针对某种特定的能容去爬取信息,而且保证内容需求尽可能相关



# WebMagic



## 概述

* 一个Java爬虫框架,如果会python,可以使用pyspider或scrapy
* 需要搭配selenum,jsoup爬取网页,不能爬取js和动态生成的网页
* WebMagic的设计参考了业界最优秀的爬虫Scrapy,而实现则应用了HttpClient,Jsoup
* 由四个组件(Downloader,PageProcessor,Scheduler,Pipeline)构成,主要是将这些组件结合并完成多线程的任务



## 核心组件

WebMagic的结构分为Downloader,PageProcessor,Scheduler,Pipeline四大组件,并由Spider将它们彼此组织起来.这四大组件对应爬虫生命周期中的下载,处理,管理和持久化等功能.WebMagic的设计参考了Scapy,但是实现方式更Java化一些.

而Spider则将这几个组件组织起来,让它们可以互相交互,流程化的执行,可以认为Spider是一个大的容器,它也是WebMagic逻辑的核心.

![1570619519257](img\1570619519257.png)



### Downloader

Downloader负责从互联网上下载页面,以便后续处理.WebMagic默认使用了[Apache HttpClient](http://hc.apache.org/index.html)作为下载工具.



### PageProcessor

PageProcessor负责解析页面,抽取有用信息,以及发现新的链接.WebMagic使用[Jsoup](http://jsoup.org/)作为HTML解析工具,并基于其开发了解析XPath的工具[Xsoup](https://github.com/code4craft/xsoup).

在这四个组件中,PageProcessor对于每个站点每个页面都不一样,是需要使用者定制的部分.



### Scheduler

Scheduler负责管理待抓取的URL,以及一些去重的工作.WebMagic默认提供了JDK的内存队列来管理URL,并用集合来进行去重.也支持使用Redis进行分布式管理.

除非项目有一些特殊的分布式需求,否则无需自己定制Scheduler.



### Pipeline

Pipeline负责抽取结果的处理,包括计算,持久化到文件,数据库等.WebMagic默认提供了“输出到控制台”和“保存到文件”两种结果处理方案.

Pipeline定义了结果保存的方式,如果你要保存到指定数据库,则需要编写对应的Pipeline.对于一类需求一般只需编写一个Pipeline.

更多内容可以查看官网文档 http://webmagic.io/docs/zh/



## 代理IP

当我们对某些网站进行爬去的时候,我们经常会换IP来避免爬虫程序被封锁.其实也是一个比较简单的操作,目前网络上有很多IP代理商,例如西刺,芝麻,犀牛等等.这些代理商一般都会提供透明代理,匿名代理,高匿代理.



### 代理IP类型

代理IP一共可以分成4种类型.前面提到过的透明代理IP,匿名代理IP,高匿名代理IP,还有一种就是混淆代理IP.最基础的安全程度来说呢,他们的排列顺序应该是这个样子的高匿 > 混淆 > 匿名 > 透明.



# Selenium

Selenium是一个用于 Web 应用程序测试的工具.它的优点在于,浏览器能打开的页面,使用 selenium 就一定能获取到.但 selenium 也有其局限性,相对于脚本方式,selenium 获取内容的效率不高.

我们主要使用它可以调用chrome浏览器来获取必须要的Cookie,因为csdn的cookie通过js来生成的,需要浏览器才能得到Cookie



## chrome无头(headless)模式

在 Chrome 59中开始搭载Headless Chrome.这是一种在无需显示headless的环境下运行 Chrome 浏览器的方式.从本质上来说,就是不用 chrome 浏览器来运行 Chrome 的功能！它将 Chromium 和 Blink 渲染引擎提供的所有现代 Web 平台的功能都带入了命令行.

由于存在大量的网页是动态生成的,在使用浏览器查看源代码之后,发现网页dom只有一个root元根元素和一堆js引用,根本看不到网页的实际内容,因此,爬虫不仅需要把网页下载下来,还需要运行JS解析器,将网站呈现出最终的效果.

在Headless出现之前,主要流行的是PhantomJS这个库,原理是模拟成一个实际的浏览器去加载网站.Headless Chome出现之后,PhantomJS地位开始不保.毕竟Headless Chome本身是一个真正的浏览器,支持所有chrome特性,而PhantomJS只是模拟,因此Headless Chome更具优势



## webdriver 

WebDriver针对各个浏览器而开发,取代了嵌入到被测Web应用中的JavaScript.与浏览器的紧密集成支持创建更高级的测试,避免了JavaScript安全模型导致的限制.除了来自浏览器厂商的支持,

成支持创建更高级的测试,避免了JavaScript安全模型导致的限制.除了来自浏览器厂商的支持,WebDriver还利用操作系统级的调用模拟用户输入.WebDriver支持Firefox(FirefoxDriver),IE (InternetExplorerDriver),Opera (OperaDriver)和Chrome (ChromeDriver). 它还支持Android (AndroidDriver)和iPhone (IPhoneDriver)的移动应用测试.它还包括一个基于HtmlUnit的无界面实现,称为HtmlUnitDriver.WebDriver API可以通过Python,Ruby,Java和C#访问,支持开发人员使用他们偏爱的编程语言来创建测试.



## ChromeDriver下载

ChromeDriver 是 google 为网站开发人员提供的自动化测试接口,它是 **selenium2** 和 **chrome浏览器** 进行通信的桥梁.selenium 通过一套协议（JsonWireProtocol ：[https://github.com/SeleniumHQ/selenium/wiki/JsonWireProtocol](https://link.jianshu.com?t=https:/github.com/SeleniumHQ/selenium/wiki/JsonWireProtocol)）和 ChromeDriver 进行通信,selenium 实质上是对这套协议的底层封装,同时提供外部 WebDriver 的上层调用类库.

和chrome版本的对应关系

| **chromedriver**    **版本** | **chrome** **版本** |
| ---------------------------- | ------------------- |
| ChromeDriver   2.36          | Chrome v63-65       |
| ChromeDriver   2.35          | Chrome v62-64       |
| ChromeDriver   2.34          | Chrome v61-63       |
| ChromeDriver   2.33          | Chrome v60-62       |

下载地址如下

http://npm.taobao.org/mirrors/chromedriver/

详细内容可以查看 https://www.jianshu.com/p/31c8c9de8fcd

# Xpath

## 概述

xpath是一种在xml中查找信息的语言,普遍应用于xml中,在类xml的html中也可以使用,在selenium自动化中起核心作用,是写selenium自动化脚本的基础.



## Xpath的定位

xpath的定位主要由路径定位,标签定位,轴定位组合构成,外加筛选功能进行辅助,几乎可以定位到任意元素

(1)标签定位

通过标签名即可找到文档中所有满足的标签元素,如：

| **xpath** | **说明**                |
| --------- | ----------------------- |
| div       | 找到所有的div标签元素   |
| input     | 找到所有的input标签元素 |
| *         | 替代任意元素或属性      |
| @属性名   | 找到指定名称的属性      |

(2)路径定位

通过路径描述来找到需要的元素,“/”开头表示从根路径开始,其他位置表示子元素或分隔符；“//”表示后代元素；“..”表示父元素（上一级）；“.”表示当前元素；“|”表示多条路径

| **xpath**                                    | **说明**                                    |
| -------------------------------------------- | ------------------------------------------- |
| /html                                        | 找到根元素html                              |
| //div                                        | 找到所有的div元素                           |
| //div[@id='id1']/span                        | 找到id=“id1”的div元素的子元素span           |
| //div[@id='id1']//span                       | 找到id=“id1”的div元素下的所有后代元素span   |
| //div[@id='id1']/@class                      | 找到id=“id1”的div元素的class属性            |
| //div[@id='id1']/span\|//div[@id='id2']/span | 找到id=“id1”和id=“id2”的div元素的子元素span |

(3)轴定位

通过轴运算符加上“::”和“标签”,找到需要的元素,类似路径定位,如：

| **xpath**                             | **说明**                                                    |
| ------------------------------------- | ----------------------------------------------------------- |
| //div[@id='id1']/child::span          | 找到id=“id1”的div元素的子元素span，同//div[@id='id1']/span  |
| //div[@id='id1']/attribute::class     | 找到id=“id1”的div元素的class属性，同//div[@id='id1']/@class |
| //div[@id='id1']/preceding-sibling::* | 找到与id=“id1”的div元素同级别的，且在它之前的所有元素       |

下表是轴运算符的列表

| **轴名称**         | **结果**                                                 |
| ------------------ | -------------------------------------------------------- |
| ancestor           | 选取当前节点的所有先辈（父、祖父等）                     |
| ancestor-or-self   | 选取当前节点的所有先辈（父、祖父等）以及当前节点本身     |
| attribute          | 选取当前节点的所有属性                                   |
| child              | 选取当前节点的所有子元素                                 |
| descendant         | 选取当前节点的所有后代元素（子、孙等）。                 |
| descendant-or-self | 选取当前节点的所有后代元素（子、孙等）以及当前节点本身。 |
| following          | 选取文档中当前节点的结束标签之后的所有节点。             |
| namespace          | 选取当前节点的所有命名空间节点                           |
| parent             | 选取当前节点的父节点。                                   |
| preceding          | 选取文档中当前节点的开始标签之前的所有节点。             |
| preceding-sibling  | 选取当前节点之前的所有同级节点。                         |
| following-sibling  | 选取当前节点之后的所有同级节点。                         |
| Self               | 选取当前节点                                             |

一般情况下,我们使用简写后的语法.虽然完整的轴描述是一种更加贴近人类语言,利用自然语言的单词和语法来书写的描述方式,但是相比之下也更加啰嗦.

(4)筛选

通过以上方法找出来的元素会找到很多你本意不需要的元素,因此还需要通过一些筛选运算来找到对应的元素,筛选方式多种多样,下面的各种例子助你定位又快又准.

通用的筛选条件是以[xxxx]形式出现的（上面的例子中已有体现）,常见筛选如下：

- 属性筛选：

属性名前+@来表示属性,如下

| xpath                                       | 说明                                                         |
| ------------------------------------------- | ------------------------------------------------------------ |
| //div[@class='class1']                      | 筛选class属性值等于class1的div                               |
| //div[@hight>10]                            | 筛选hight属性值大于10的div(仅限数字)                         |
| //div[text()='divtext']                     | 筛选文本是divtext的div                                       |
| //div[contains(@class,'class1')]            | 筛选class属性中包含class1的div                               |
| //div[contains(text(),'text1')]             | 筛选文本包含text1的div                                       |
| //div[text()='text1'   and @class=‘class1’] | 同时满足两个条件的筛选，类似的，“或者” 的话用“or”，运算优先级高的用"()"括起来 |
| //div[text()='text1'   and not(@class)]     | 筛选文本包含 text1，且无class属性的 div                      |

- 序号筛选：

通过序号（从1开始）,或排序运算查找元素

| xpath                                                    | 说明                                                         |
| -------------------------------------------------------- | ------------------------------------------------------------ |
| //div[@id='id1']/span[1]                                 | 找到id=“id1”的div元素后代的第一个span元素，如[4]则是第4个    |
| //div[@id='id1']/span[last()]                            | 找到id=“id1”的div元素子元素的最后一个span元素，如[last()-2]则是倒数第3个 |
| //div[@id='id1']/span[position()>2   and position() < 7] | 找到id=“id1”的div元素后代的第3、4、5、6个span元素            |
| //div[@id='id1']/text()[2]                               | 找到id=“id1”的div元素的第二段文本（注：此处用于文本被子元素分割，需要选择后面文本的情况：如<div   id="id1">this is text one<strong>haha</strong>this   is text two</div>） |

特别注意：序号筛选时,指定是当前元素的同级的第n个,如果当前元素的祖先中有元素不是唯一的,那么序号筛选是无效的.

通过括号将祖先括起来,再指定序号,可以使当前元素前的祖先是指定的,且唯一的,如：

(//div[@class='class1']//span[@class='class2'])[1]/div[3]

这样就可以十分准确的定位到需要span下的第3个div,没有此括号,当//div[@class='class1']//span[@class='class2']找到多个元素时,就算用[3]也则只能定位到第1个





# 设计思路

1,配置初始化的URL,首先访问初始化的URL,先解析初始URL,并获取需要筛选的用户空间的链接

2,将用户空间的URL链接交给WebMagic进行数据抓取,并进行分页处理,获取有效的文章链接.

3,将文章交给WebMagic 进行数据抓取,如果抓取过程中出现失败,则采用selenium+Chrome 的方式抓取页面,并进行cookie重置

4,解析完成后得到Html页面交给下一级解析器进行数据解析,得到需要的数据,并将数据封装成固定的格式进行存储

5,定时任务定期对点击量比较高的数据进行重新抓取并更新数据.



# 需求分析

## 功能需求

为黑马头条提供大量的数据积累,使用爬虫对CSDN的大量博客内容进行抓取,提升黑马头条的数据量以及点击量,为以后的大数据采集提供前置数据.

### CSDN爬虫需求

- 获取CSDN文章的 标题,作者内容,发布日期,文章来源,阅读量,评论数据

- 将文章内容按照图片以及文本的方式进行存储,存储格式如下

 ```json
[
    {
        type: 'text',
        value: 'text'
    },
    {
        type: 'image',
        value: 'https://p3.pstatp.com/large/pgc-image/RVFRw8xCiUeTbd',
        style:{
            height:'810px'
        }
    }
]
 ```

- 文章可能存在多条评论,将评论数据进行存储

- 要进行代理IP的自管理,即自动进行代理IP的抓取以及定时检查无效代理IP,并进行删除,实时保证代理IP库是可用的.

### 爬虫常见问题

- CSDN使用混淆加密js设置cookie,浏览器才能解析,无法进行人工还原算法,没有办法手动获取cookie并进行注入,所以导致访问被拦截

  解决方案：使用selenium+chromedriver 先通过chrome的headless(无头)方式进行进行访问浏览器,获取cookie以及内容,更新cookie后就可以进行正常访问了

- CSDN获取首页数据比较麻烦

  解决方案,分三步,第一步获取初始化的URL,解析用户空间,然后处理分页数据,最后获取最终的文章URL



## 文档处理

* 下载完成数据后就需要进行文档处理,这里的处理是分三个步骤
* 解析初始化的URL获取列表页,将列表页的数据提交下载处理器
* 解析完列表页后获取最终的需要处理的URL交给下载处理器
* 解析最终URL数据,将解析的数据交给下一级处理器处理

