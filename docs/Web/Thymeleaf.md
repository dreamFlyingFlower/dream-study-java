# Thymeleaf



# 概述



* [官方网站](https://www.thymeleaf.org/index.html)
* 通过特殊语法将数据库数据填充页面中的静态数据,该操作用JSP可以实现,但SpringBoot推荐使用Thymeleaf
* 流行的模板技术:Freemarker,Thymeleaf,Beetl,Velocity,JSP,Lua
* Freemarker适合比较复杂的网页,Thymeleaf适合简单的网页
* 在SpringBoot项目中同时使用Freemarker和Thymeleaf时,模板后缀不能相同,否则在同时使用这2个模板时,Thymeleaf模板不能加载.因为这2种模板都默认会加载templates下的文件,即使修改了资源目录,templates下的静态文件仍会加载.所以相同模板无法给Freemarker和Thymeleaf同时使用,只能写2个,且名称不能相同
* 模板技术的优缺点
  * 前端开发难度小,与后台交互次数少,服务器压力小
  * 与前端紧密耦合,页面各种元素集中同步一次渲染,页面可用性风险高
* 前端框架的优缺点
  * 动静分离,与后台耦合小,可异步加载,页面可用性高
  * 前端工作量大,与后台交互频繁,服务器压力大



# 配置文件

```yaml
spring:
  freemarker:
    # 是否开启缓存,false每次都会刷新页面,开发时设置为false,生产就是true,默认true
    cache: false
    charset: UTF-8
    allow-request-override: false
    check-template-location: true
    content-type: text/html
    expose-request-attributes: true
    expose-session-attributes: true
    # 文件后缀,默认ftl
    suffix: .ftl
    # 模板路径,默认是templates
    template-loader-path:
    - classpath:/templates/
  thymeleaf:
    cache: false
    mode: HTML5
    encoding: UTF-8
    # 文件后缀,默认是html
    suffix: .html
    servlet:
      content-type: text/html; charset=utf-8
    # 资源前缀,等同于freemarker的template-loader-path,多个前缀用逗号隔开
    prefix: classpath:/templates/
```



# 基本用法

* 准备一个controller,控制视图跳转:

```java
@Controller
public class HelloController {
    // 不能添加@ResponseBody注解,否则会被转换换json数据,无法直接跳转
    // model是视图数据,若有数据可以直接传递到视图中
    @GetMapping("hello")
    public String hello(Model model){
        model.addAttribute("msg", "Hello, Thymeleaf!");
        // 通过默认后缀,会跳转到hello.html
        return "hello";
    }
}
```

* 新建一个hello.html模板:

```html
<!DOCTYPE html>
<!-- 天剑xmlns:th="http://www.thymeleaf.org" 会有语法提示 -->
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>hello</title>
</head>
<body>
    <h1 th:text="${msg}">大家好</h1>
</body>
</html>
```

* 启动项目,访问ip:port/hello即可跳转

- 静态页面中,若th指令不被识别,浏览器就会忽略它,这样`div`的默认值就会展现在页面上
- Thymeleaf环境下,th指令会被识别和解析,而th:text的含义就是替换所在标签中的文本内容,于是msg的值就替代了div中默认的值
- th指令只能写在标签内,若想将msg的值注入到标签内容中,可已使用[[msg]]



# 语法



## th:text/utext

> Thymeleaf通过${}来获取model中的变量,这不是el表达式,而是ognl表达式



定义一个User实体类

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String name;
    private int age;
    private User friend;
}
```



然后在模型中添加数据

```java
@Controller
public class HelloController {
    @GetMapping("test")
    public String test(Model model){
        User user = new User("测试", 22, new User("柳岩", 20, null));
        model.addAttribute("msg", "hello thymeleaf!");
        model.addAttribute("user", user);
        return "hello";
    }
}
```



在hello.html中使用user数据:

```html
<!doctype html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="utf-8">
    <title>title</title>
</head>
<body>
    <h1 th:text="${msg}">大家好！</h1>
    <h1>
        <!-- 常规用法 -->
        欢迎您:<span th:text="${user.name}">请登录</span>
    </h1>
    <h1>
        <!-- 常量:有些内容可能不希望thymeleaf解析为变量 -->
        字符串常量:<span th:text="'欢迎您'"></span><br>
        数字常量:<span th:text="2020"></span><br>
        数字常量运算:<span th:text="2020 - 10"></span><br>
        bool常量:<span th:text="true"></span>
    </h1>
    <h1>
        <!-- 字符串拼接:下面两种方式等价 -->
        <span th:text="'欢迎您,' + ${user.name}"></span><br>
        <!-- 简写方式:使用‘|’围起来 -->
        <span th:text="|欢迎您,${user.name}|"></span>
    </h1>
    <h1>
        <!-- 运算:运算符放在${}外 -->
        10年后,我<span th:text="${user.age} + 10"></span>岁<br>
        <!-- 比较:gt(>),lt(<),ge(>=),le(<=),not(!),eq(==),neq/ne(!=) -->
        比较结果:<span th:text="${user.age} < ${user.friend.age}"></span>
        <!-- 三元运算 -->
        三元:<span th:text="${user.age}%2 == 0 ? '帅' : '不帅'"></span>
        <!-- 默认值:注意`?:`之间没有空格 -->
        默认值:<span th:text="${user.name} ?: '硅谷刘德华'"></span>
    </h1>
</body>
</html>
```



运行后在页面中访问ip:port/test即可查看效果



## th:object 

> 自定义变量



```html
<h2>
    <p th:text="${user.name}">Jack</p>
    <p th:text="${user.age}">21</p>
    <p th:text="${user.friend.name}">Rose</p>
</h2>
```

当数据量比较多的时候,频繁的写user就会非常麻烦,因此,Thymeleaf提供了自定义变量来解决:

```html
<h1 th:object="${user}">
    <p th:text="*{name}">Jack</p>
    <p th:text="*{age}">21</p>
    <p th:text="*{friend.name}">Rose</p>
</h1>
```

在h1上用th:object="${user}"获取user的值,并且保存,在h1内部的任意元素上,可以通过 `*{属性名}`的方式,来获取user中的属性,这样就省去了user前缀了



## th:each

> 循环



```java
List<User> users = Arrays.asList(
    new User("柳岩", 21, null),
    new User("杨紫", 23, null),
    new User("小鹿", 24, null)
);
model.addAttribute("users", users);
```

页面渲染方式如下:

```html
<table>
    <tr th:each="user: ${users}">
        <td th:text="${user.name}"></td>
        <td th:text="${user.age}"></td>
    </tr>
</table>
```

* ${users} 是要遍历的集合,可以是以下类型:
  * Iterable,实现了Iterable接口的类
  * Enumeration,枚举
  * Interator,迭代器
  * Map,遍历得到的是Map.Entry
  * Array,数组及其它一切符合数组结果的对象
* 在迭代的同时,也可以获取迭代的状态对象:

```html
<table>
    <tr th:each="user,stat: ${users}">
        <td th:text="${stat.index + 1}"></td>
        <td th:text="${user.name}"></td>
        <td th:text="${user.age}"></td>
    </tr>
</table>
```

* stat对象包含以下属性:
  * index,从0开始的角标
  * count,元素的个数,从1开始
  * size,总元素个数
  * current,当前遍历到的元素
  * even/odd,返回是否为奇偶,boolean值
  * first/last,返回是否为第一或最后,boolean值



## th:if

> 逻辑判断



Thymeleaf中使用`th:if`或者`th:unless` ,两者的意思恰好相反

```html
<table>
    <tr th:each="user,stat: ${users}" th:if="${user.age > 22}">
        <td th:text="${stat.index + 1}"></td>
        <td th:text="${user.name}"></td>
        <td th:text="${user.age}"></td>
    </tr>
</table>
```

如果表达式的值为true,则标签会渲染到页面,否则不进行渲染.

以下情况被认定为true:

- 表达式值为true
- 表达式值为非0数值或者字符串
- 表达式值为字符串,但不是`"false"`,`"no"`,`"off"`
- 表达式不是布尔、字符串、数字、字符中的任何一种

其它情况包括null都被认定为false



## th:switch

> 分支控制

这里要使用两个指令:`th:switch` 和 `th:case`,类似Java的switch case语句

```html
<div th:switch="${user.role}">
  <p th:case="'admin'">用户是管理员</p>
  <p th:case="'manager'">用户是经理</p>
  <p th:case="*">用户是别的玩意</p>
</div>
```

一旦有一个th:case成立,其它的则不再判断.与java中的switch是一样的

另外`th:case="*"`表示默认,放最后



## th:href

> 动态链接

动态链接可以通过以下两种方式生成:

```html
<!-- 直接拼接字符串 -->
<a th:href="@{'http://grain.com/brand?pageIndex=' + ${pageIndex}}">点我带你飞</a><br>
<!-- 使用()的形式定义参数 -->
<a th:href="@{http://grain.com/brand/{id}(id=${id)}">点我带你飞</a><br>
<!-- 使用(,,)的形式解析多个参数 -->
<a th:href="@{http://grain.com/brand(pageIndex=${pageIndex}, pageSize=${pageSize})}">起飞吧</a>
```

`th:src`和`th:href`用法一致



## 表单操作

```html
<form th:action="@{/login}">
    <input type="hidden" th:value="${url}" name="redirect_url">
    用户名:<input type="text" name="username"><br />
    密&emsp;码:<input type="password" name="password"><br />
    <input type="submit" value="登录"/>
</form>
```

th:action->表单提交路径

th:value->给表单元素绑定value值



## 方法及内置对象

ognl表达式本身就支持方法调用,例如:

```html
<h1 th:object="${user}">
    <!-- 这里调用了name的split方法 -->
    <p th:text="*{name.split('')[0]}"></p>
    <p th:text="*{age}"></p>
    <p th:text="*{friend.name}"></p>
</h1>
```

Thymeleaf中提供了一些内置对象和方法,获取这些对象,需要使用`#对象名`来引用



### 常用内置对象

* ctx:上下文对象
* vars:上下文变量
* locale:上下文的语言环境
* request:仅在web上下文的HttpServletRequest 对象
* response:仅在web上下文的 HttpServletResponse 对象
* session:仅在web上下文的 HttpSession 对象
* servletContext:仅在web上下文的 ServletContext 对象



### 常用内置方法

* strings:字符串格式化方法,常用的Java方法它都有.比如:equals,length,trim,substring等
* numbers:数值格式化方法,常用的方法有:formatDecimal等
* bools:布尔方法,常用的方法有:isTrue,isFalse等
* arrays:数组方法,常用的方法有:toArray,length,isEmpty,contains,containsAll等
* lists,sets:集合方法,常用的方法有:toList,size,isEmpty,contains,containsAll,sort等
* maps:对象方法,常用的方法有:size,isEmpty,containsKey,containsValue等
* dates:日期方法,常用的方法有:format,year,month,hour,createNow等



## 页面引用

th:fragment:定义一个通用的html片段

th:insert:保留自己的主标签,保留th:fragment的主标签

th:replace:不要自己的主标签,保留th:fragment的主标签

```html
<!-- 定义一个通用的fragment -->
<footer th:fragment="copy">
    <script type="text/javascript" th:src="@{/plugins/jquery/jquery-3.0.2.js}"></script>
</footer>
<!-- templatename::selector:”::”前面是模板文件名,后面是选择器
    ::selector:只写选择器,这里指fragment名称,则加载本页面对应的fragment
    templatename:只写模板文件名,则加载整个页面
-->
<div th:insert="::copy"></div>
<div th:replace="::copy"></div>
<div th:include="::copy"></div>
```

解析后:

```html
<footer>
    <script type="text/javascript" src="/plugins/jquery/jquery-3.0.2.js"></script>
</footer>
<div>
    <footer>
        <script type="text/javascript" src="/plugins/jquery/jquery-3.0.2.js"></script>
    </footer>
</div>
<footer>
    <script type="text/javascript" src="/plugins/jquery/jquery-3.0.2.js"></script>
</footer>
<div>
    <script type="text/javascript" src="/plugins/jquery/jquery-3.0.2.js"></script>
</div>
```



## 更多语法

见[官方文档](https://www.thymeleaf.org/index.html)

# 静态资源部署

web项目大部分的请求都是静态资源请求,为了提高并发能力,可以直接部署到nginx.

把课前资料\前端工程\静态资源.rar解压,上传到虚拟机/opt/static目录下:

![1588227387111](assets/1588227387111.png)

接下来,修改nginx的配置文件,添加一个server配置,使静态资源可以正常的通过nginx访问:

```nginx
server {
    listen       80;
    server_name  static.gmall.com;

    location ~ /(css|data|fronts|img|js|common)/ {
        root   /opt/static;
    }
}
```

执行:nginx -s reload

重新加载nginx配置文件,然后在浏览器中可访问静态资源



# 搜索页数据联调

把课前资料《资料\前端工程\动态页面》中的search.html及common目录copy到gmall-search工程的templates目录下,若没有该目录则手动创建

页面的body主要结构如下:

![1589069095905](assets/1589069095905.png)

包括通用的:页面顶部（页头）,商品分类导航（菜单）,页面底部（页脚）,侧面板等.这些直接引用common目录下的资源即可.接下来主要分析红框内的这部分,进行分析.



## 5.1. 最外层div

首先在最外层的div上定义了响应数据的最外层对象,方便使用里面的数据,不用反复解包响应数据:

```
th:object="${response}"
```

定义了一个thymeleaf变量location,统一获取带有请求参数的地址栏路径.因为后续所有的点击事件,都需要基于当前地址栏路径,进行修改:

```
th:with="location=${'/search?'+ #strings.replace(#httpServletRequest.queryString, '&pageNum=' + searchParam.pageNum, '')}"
```

#httpServletRequest.queryString:请求参数字符串

使用 #strings.replace 把请求参数字符串中的分页参数去掉,因为当用户修改了搜索、过滤、排序、分页之后,当前页码就不需要了.



改造SearchController的search方法,跳转到search.html页码并封装响应数据:

```java
@GetMapping
public String search(SearchParamVo paramVo, Model model){

    SearchResponseVo responseVo = this.searchService.search(paramVo);

    model.addAttribute("response", responseVo);
    model.addAttribute("searchParam", paramVo);

    return "search";
}
```



展开后主要包括:

![1589069581476](assets/1589069581476.png)



## 5.2. 面包屑

对应jd搜索页的面包屑如下:

![1589069778397](assets/1589069778397.png)



我们面包屑的结构如下:

![1589072352497](assets/1589072352497.png)



品牌的面包屑渲染:

```html
<li th:if="${not #lists.isEmpty(searchParam.brandId)}" class="with-x">
    <span>品牌:</span>
    <!-- 品牌可以多选,多选情况下品牌名以空格进行分割 -->
    <span th:each="brand : *{brands}" th:text="${brand.name + ' '}"></span>
    <!-- 点击x时,去掉地址栏中的品牌过滤条件 -->
    <a th:href="@{${#strings.replace(location, '&brandId='+ #strings.arrayJoin(searchParam.brandId, ','), '')}}">×</a>
</li>
```



分类的面包屑渲染:

```html
<li th:if="${not #lists.isEmpty(searchParam.cid)}" class="with-x">
    <span>分类:</span>
    <!-- 分类也可以多选,多选时情况下分类名称以空格分割 -->
    <span th:each="category : *{categories}" th:text="${category.name + ' '}"></span>
    <!-- 点击x时,去掉地址栏中的分类过滤条件 -->
    <a th:href="@{${#strings.replace(location, '&cid='+ #strings.arrayJoin(searchParam.cid, ','), '')}}">×</a>
</li>
```



规格参数的面包屑渲染:

```html
<li th:each="prop : ${searchParam.props}" class="with-x">
    <!-- 规格参数的格式为“8:128G-256G”,这里获取“:”号后的规格参数展示 -->
    <span th:with="(propName = ${#strings.substringAfter(prop, ':')})" th:text="${propName}"></span>
    <!--<a th:href="@{${#strings.replace(location, '&props=' + prop, '')}}" th:text="${'&props=' + prop}">×</a>-->
    <!-- 这里不能使用thymeleaf的替换语法（如上）,因为thymeleaf获取的地址:中文及特殊符号是编码后的 -->
    <a th:href="@{'javascript: cancelProp(\'' + ${prop} + '\');'}">×</a>
</li>
```

对应的js如下:

```javascript
let urlParams = decodeURI([[${#httpServletRequest.queryString}]]);

function cancelProp(prop){
    urlParams = urlParams.replace('&props=' + prop, '');
	window.location = '/search?' + urlParams;
}
```



搜索条件:

```html
<ul class="fl sui-breadcrumb" style="font-weight: bold">
    <li>
        <span th:text="${searchParam.keyword}"></span>
    </li>
</ul>
```



## 5.3. 过滤条件

对应京东的过滤条件如下:

![1589117522424](assets/1589117522424.png)



我们的过滤条件前端结构如下:

![1589197065970](assets/1589197065970.png)

包括:品牌过滤、分类过滤、规格参数过滤



品牌过滤渲染如下:

```html
<!-- 品牌过滤:只有一个品牌或者已经选择了品牌时,不显示品牌过滤 -->
<div class="type-wrap logo" th:if="${response.brands == null && response.brands.size() > 1 && searchParam.brandId == null}">
    <!-- 过滤名称写死,就是品牌 -->
    <div class="fl key brand">品牌</div>
    <div class="value logos">
        <ul class="logo-list">
            <!-- 遍历品牌集合 -->
            <li th:each="brand : *{brands}">
                <!-- 选择品牌后把品牌id拼接到地址栏 -->
                <a class="brand" style="text-decoration: none;color: red;" th:href="@{${location + '&brandId=' + brand.id}}" th:title="${brand.name}" >
                    <!-- 渲染品牌logo及品牌名称,通过js控制log和名称的切换 -->
                    <img th:src="${brand.logo}">
                    <div th:text="${brand.name}" style="display: none"></div>
                </a>
            </li>
        </ul>
    </div>
    <!-- 多选及更多,不做 -->
    <div class="ext">
        <a href="javascript:void(0);" class="sui-btn">多选</a>
        <a href="javascript:void(0);">更多</a>
    </div>
</div>
```

品牌logo及名称切换的js如下:

```html
<script >
    $(function () {
        $('.brand').hover(function(){
            /*显示品牌名称*/
            $(this).children("div").show()
            $(this).children("img").hide()
        },function(){
            // alert("come on!")
            $(this).children("div").hide()
            $(this).children("img").show()
        });
    })
</script>
```



分类过滤条件的渲染:

```html
<!-- 分类过滤:只有一个分类或者已经选择了分类时,不显示分类过滤 -->
<div class="type-wrap" th:if="${response.categories != null && response.categories.size() > 1 && searchParam.cid == null}">
    <!-- 过滤名称写死,就是分类 -->
    <div class="fl key">分类</div>
    <div class="fl value">
        <ul class="type-list">
            <!-- 遍历所有分类过滤条件 -->
            <li th:each="category : *{categories}">
                <!-- 展示分类名称,点击时把分类id拼接到地址栏 -->
                <a th:text="${category.name}" th:href="@{${location + '&cid=' + category.id}}">GSM（移动/联通2G）</a>
            </li>
        </ul>
    </div>
    <div class="fl ext"></div>
</div>
```



规格参数的过滤:

```html
<!-- 规格参数的过滤条件:由于规格过滤是多个,所以这里需要遍历.也要判断规格参数是否只有一个条件,地址栏是否包含了该规格参数的过滤 -->
<div class="type-wrap" th:each="filter : *{filters}"
     th:if="${filter.attrValues != null && filter.attrValues.size() > 1 && not (#strings.contains(location, ',' + filter.attrId + ':') || #strings.contains(location, '=' + filter.attrId + ':'))}" >
    <!-- 规格参数名 -->
    <div class="fl key" th:text="${filter.attrName}">显示屏尺寸</div>
    <div class="fl value">
        <ul class="type-list">
            <!-- 遍历渲染规格参数可选值列表 -->
            <li th:each="value : ${filter.attrValues}">
                <!-- 展示每个规格参数值.点击时把规格参数的过滤条件拼接到地址栏 -->
                <a th:text="${value}" th:href="@{${location + '&props=' + filter.attrId + ':' + value}}">3.0-3.9英寸</a>
            </li>
        </ul>
    </div>
    <div class="fl ext"></div>
</div>
```



## 5.4. 商品列表

参照京东的商品列表如下:

![1589119043416](assets/1589119043416.png)

包含3部分内容:排序、商品列表、分页等



排序渲染:

```html
<!-- 排序条件 -->
<div class="sui-navbar">
    <div class="navbar-inner filter" >
        <ul class="sui-nav">
            <!-- 排序sort=0时,该li标签处于活性状态 -->
            <li th:class="${searchParam.sort == 0 ? 'active' : ''}">
                <!-- 点击综合时,地址栏的sort值替换为0 -->
                <a th:href="@{${#strings.replace(location, '&sort=' + searchParam.sort, '&sort=0')}}">综合</a>
            </li>
            <li th:class="${searchParam.sort == 4 ? 'active' : ''}">
                <a th:href="@{${#strings.replace(location, '&sort=' + searchParam.sort, '&sort=4')}}">销量</a>
            </li>
            <li th:class="${searchParam.sort == 3 ? 'active' : ''}">
                <a th:href="@{${#strings.replace(location, '&sort=' + searchParam.sort, '&sort=3')}}">新品</a>
            </li>
            <li th:class="${searchParam.sort == 1 ? 'active' : ''}">
                <a th:href="@{${#strings.replace(location, '&sort=' + searchParam.sort, '&sort=1')}}">价格⬆</a>
            </li>
            <li th:class="${searchParam.sort == 2 ? 'active' : ''}">
                <a th:href="@{${#strings.replace(location, '&sort=' + searchParam.sort, '&sort=2')}}">价格⬇</a>
            </li>
        </ul>
    </div>
</div>
```



商品列表的渲染:

```html
<div class="goods-list">
    <ul class="yui3-g">
        <!-- 遍历goodsList,渲染商品 -->
        <li class="yui3-u-1-5" th:each="goods : *{goodsList}">
            <div class="list-wrap">
                <!-- 商品图片 -->
                <div class="p-img">
                    <!-- 点击图片跳转到商品详情页 -->
                    <a th:href="@{http://item.gmall.com/{id}.html(id=${goods.skuId})}" target="_blank"><img
                                                                                                            th:src="${goods.defaultImage}"/></a>
                </div>
                <!-- 商品价格 -->
                <div class="price">
                    <strong>
                        <em>¥</em>
                        <i th:text="${goods.price}">6088.00</i>
                    </strong>
                </div>
                <!-- 商品标题 -->
                <div class="attr">
                    <!-- 点击标题跳转到商品详情页,鼠标放在标题上展示副标题 -->
                    <a target="_blank" th:href="@{http://item.gmall.com/{id}.html(id=${goods.skuId})}" th:title="${goods.subTitle}">Apple苹果iPhone
                        6s (A1699)Apple苹果iPhone 6s (A1699)Apple苹果iPhone 6s (A1699)Apple苹果iPhone 6s
                        (A1699)</a>
                </div>
                <div class="commit">
                    <i class="command">已有<span>2000</span>人评价</i>
                </div>
                <div class="operate">
                    <a href="javascript:void(0);" target="_blank" class="sui-btn btn-bordered btn-danger">加入购物车</a>
                    <a href="javascript:void(0);" class="sui-btn btn-bordered">收藏</a>
                </div>
            </div>
        </li>
    </ul>
</div>
```



分页条件的渲染:

```html
<div class="fr page">
    <!-- 根据总记录数及pageSize计算总页数 -->
    <div class="sui-pagination pagination-large"
         th:with="totalPage = *{total % pageSize == 0 ? (total / pageSize) : (total / pageSize + 1)}">
        <ul>
            <!-- 不是第一页时,展示上一页 -->
            <li class="prev" th:if="${searchParam.pageNum != 1}">
                <!-- 点击上一页,页码减1 -->
                <a th:href="|${location}&pageNum=${searchParam.pageNum - 1}|">«上一页</a>
            </li>
            <!-- 如果是第一页,上一页按钮不可用 -->
            <li class="prev disabled" th:if="${searchParam.pageNum == 1}">
                <a href="javascript:void(0);">上一页</a>
            </li>
            <!-- 渲染页码 -->
            <li th:each="i : ${#numbers.sequence(1, totalPage)}" th:class="${i == searchParam.pageNum } ? 'active' : ''">
                <a th:href="|${location}&pageNum=${i}|"><span th:text="${i}"></span></a>
            </li>
            <!-- 渲染下一页,逻辑类似于上一页 -->
            <li class="next" th:if="${searchParam.pageNum != totalPage}">
                <a th:href="|${location}&pageNum=${searchParam.pageNum + 1}|">下一页</a>
            </li>
            <li class="next disabled" th:if="${searchParam.pageNum == totalPage}">
                <a href="javascript:void(0);">下一页</a>
            </li>
        </ul>
        <!-- 总页数 -->
        <div><span th:text="|共${totalPage}页|">共10页&nbsp;</span></div>
    </div>
</div>
```

