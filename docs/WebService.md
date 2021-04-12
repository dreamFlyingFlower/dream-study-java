# WebService



# WSDL

> WebService Description Language,web服务描述语言,通过xml说明服务的地址以及调用方式

* webservice是一种跨编程语言和跨操作系统平台的远程调用技术
* 采用标准的SOAP协议传输,基于http传输协议



# SOAP

> Simple Object Access Protocol,简单对象访问协议,基于http+xml的网上传输协议



## 组成

* envelope:必须的部分,是xml的根元素
* headers:头部信息,可选的
* body:必须的,包含要执行的服务器的方法,和发送到服务器的数据



## 版本

* 主要有soap1.1和soap1.2,两者的命令空间不一样,在头信息上也存在差异
* soap1.1存在soapaction的请求头,而1.2没有该请求头
* 基于2个版本生成的wsdl也不一样,主要是命名空间不一样
* 在cxf中两种协议的请求方式也不一样
  * 1.1:content-type:text/xml:charset=utf-8
  * 1.2:content-type:application/soap+xml:charset=utf-8
* soap1.1中service为soap:address,而1.2中为soap12:address
* 在服务端的接口上可以加上@BindingType(SOAPBinding.SOAP12HTTP_BINDING)使用1.2版本



# UDDI

> 一种目录服务,通过它,企业可注册并搜索webservice服务



# Java服务端

* WebService:javax.jws.WebService,该注解将被修饰的类发布成一个WebService服务

  * 添加了该注解的类必须有非静态,非final的方法,否则报错
  * 该类中的所有非静态,非final的方法都会对外暴露
  * 若不想公开某个普通方法,可以在方法上添加@WebMethod(exclude = true)

* EndPoint:javax.xml.ws.Endpoint,该类为端点服务类,它的publish方法可以将用WebService注解修饰的类绑定到固定的端口,并通过固定url发布为一个WebService服务

  * publish:静态方法,接收2个参数
    * url:该参数为服务端发布的webservice地址,可自定义,只要端口不冲突即可
    * object:加上了WebService注解的类实例
  * 发布完成之后,将会启动另外一个线程来运行服务
  * stop:停止服务,非静态方法

  ```java
  import javax.xml.ws.Endpoint;
  import org.springframework.context.annotation.Bean;
  import org.springframework.context.annotation.Configuration;
  
  @Configuration
  public class WsdlConfig {
  
  	@Bean
  	public Endpoint endpoint() {
  		String url = "http://localhost:5502/wsdlService";
  		return Endpoint.publish(url, new WsdlServiceImpl());
  	}
  }
  ```

* web访问:利用endpoint发布成功之后,可以在浏览器中用url+wsdl方法

  ```http
  http://localhost:5502/wsdlService?wsdl
  ```

  



## XML组成

> WebService服务发布后,可以在网页上打开该地址,显示为一个xml文件,从下往上看



### service

* name:属性,表示客户端可调用的服务名,通常会在服务端定义的名称后加上service

* port:子标签,表示该服务中的端点服务
  * name:属性,端点名称,由服务端定义的名称后加上port
  * binding:属性,指向某个binding标签,标签的name属性和binding属性tns:后的值相同
  * soap:子标签,调用该webservice的真实地址.该标签表示的是用什么协议传输内容,不同的协议该标签是不一样的,可能是soap12协议,http协议等



### binding

* name:属性,该标签的名称
* type:属性,指向某个portType标签,标签name属性的值和type属性tns:后的值相同
* soap:
* operation:
  * name:属性:webservice服务中可调用的方法名
  * soap:
  * input:
    * soap:
  * output:
    * soap:



### portType

该标签是webservice服务器真正调用的类,子标签是可以调用的方法

* operation
  * name:属性,可以调用的方法名
  * input:标签
  * output:标签



### types

* xsd:schema:在xml中用到的各种类型说明
  * xsd:import:一些重要的说明
    * namespace:属性,服务端的命名空间
    * schemaLocation:属性,一个可以在网页打开的地址,里面是各种需要用到的字段说明



# Java客户端

* 创建服务:根据wsdl文件中service标签的name属性生成实体类,如service

* 创建端点:找到service标签的子标签port的name属性值,假设为WsdlPort,由service获得

  ```java
  service.getWsdlPort(); // 需要根据实际的wsdl文件得到返回值
  ```

* 找到portType标签的子标签operation中的name属性值,该值为方法名,由端点服务调用



# Wsimport

> wsimport是jdk1.6以上自带的,根据webservice的url生成客户端调用代码的工具,位于jdk/bin下

* wsimport [] url:在目录中根据webservice的url生成java代码,注意url需要带上wsdl

  * -d folder:将生成的class文件存放到指定目录,默认放在当前文件夹
  * -s folder:将生成的java文件存放到指定目录,.(点)表示当前目录
  * -p packagename:将生成的类放到指定的包下,若该参数不指定,则生成的包名同服务端包名,且只能放在同服务端包名相同路径下,否则无法调用webservice服务

* 有些url不能直接使用,需要下载到本地来,此时该url可以换成本地文件地址

  ```java
  wsimport -s . file:///webservicexml.wsdl
  ```

* 若本地使用wsimport报警提示it uses non-standard soap1.2 binding,表示该wsdl文件中使用了soap12协议,需要在命令中添加-extension命令选项,也可以直接都忽略

  ```java
  wsimport -s . -extension file:///webservicexml.wsdl
  ```

* 当生成客户端代码时,生成的service类中有2处会写入服务端的url地址