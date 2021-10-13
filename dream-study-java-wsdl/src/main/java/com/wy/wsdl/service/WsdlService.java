package com.wy.wsdl.service;

import javax.jws.HandlerChain;
import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.BindingType;

import com.wy.wsdl.model.User;

/**
 * WebService测试服务.WebService服务必须实现接口,否则无法传递对象类参数;若不需要对象类参数,可不实现接口
 * 
 * <pre>
 * {@link WebService}:表明是一个WebService服务,实现类也需要添加该注解
 * 		name:该服务的名称,会接在url后面
 *     targetNamespace:是xml命名空间,若不定义,则是该包路径反过来的字符串
 *     serviceName:对外发布的服务名,默认为Java类的简单名称 + Service.可以写在实现类上,但可能不能写在接口上
 *     endpointInterface:服务接口全路径,指定做SEI(Service EndPoint Interface)服务端点接口
 *     wsdlLocation:指定用于定义WebService的WSDL文档的Web地址,Web地址可以是相对路径或绝对路径
 * {@link WebMethod}:表明是一个需要发布的方法,若不写则接口中所有方法都发布.该注解只能在标识了WebService的接中使用
 *     exclude:排除某个方法不暴露为webservice方法,true不暴露
 *     operationName:同serviceName,给服务中的方法起一个别名
 *     action:定义此操作的行为,对于SOAP绑定,此值将确定SOAPAction头的值,默认为Java方法的名称
 * {@link Oneway}:注释将一个方法表示为只有输入消息而没有输出消息的WebService单向操作.
 * 将此注释应用于客户机或服务器服务端点接口(SEI)上的方法,或者应用于JavaBeans端点的服务器端点实现类
 * {@link WebParam}:对参数进行修饰或做特定的处理,指定名称或别名等
 *		name:指定参数的名称,从wsdl的schemaLocation标签中的url才可以看出
 *		partName:定义用于表示此参数的wsdl:part属性名,仅当操作类型为RPC或操作是文档类型并且参数类型为BARE时才使用
 *		targetNamespace:指定参数的XML元素的名称空间.当属性映射至XML元素时,仅应用于文档绑定
 *		mode:此值表示此方法的参数流的方向,有效值为IN,INOUT和OUT
 *		header:指定参数是在消息头还是消息体中,默认false
 * {@link BindingType}:webservice服务默认发布时使用的是soap1.1协议,使用该注解可指定发布的协议类型
 * {@link WebResult}:注释用于定制从返回值至WSDL部件或XML元素的映射.
 * 		将此注释应用于客户机或服务器服务端点接口(SEI)上的方法,或者应用于JavaBeans 端点的服务器端点实现类
 * 		name:当返回值列示在WSDL文件中并且在连接上的消息中找到该返回值时,指定该返回值的名称.
 * 			对于RPC绑定,这是用于表示返回值的wsdl:part属性的名称.
 * 			对于文档绑定,name参数是用于表示返回值的XML元素的局部名
 * 			对于RPC和DOCUMENT/WRAPPED绑定,默认为return
 * 			对于DOCUMENT/BARE绑定,默认为方法名 + Response
 * 		targetNamespace:指定返回值的XML名称空间.仅当操作类型为RPC或者操作是文档类型并且参数类型为BARE时才使用
 * 		header:指定头中是否附带结果.默认false
 * 		partName:指定RPC或DOCUMENT/BARE操作的结果的部件名称.默认为@WebResult.name
 * {@link HandlerChain}:注释用于使WebService与外部定义的处理程序链相关联
 * 		只能通过对SEI或实现类使用HandlerChain来配置服务器端的处理程序,但是可以使用多种方法来配置客户端的处理程序.
 * 		可以通过对生成的服务类或者SEI使用HandlerChain注释来配置客户端的处理程序.
 * 		此外,可以按程序在服务上注册自己的HandlerResolver接口实现,或者按程序在绑定对象上设置处理程序链
 * 		file:指定处理程序链文件所在的位置.文件位置可以是采用外部格式的绝对java.NET.URL,也可以是类文件中的相对路径
 * 		name:指定配置文件中处理程序链的名称
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2019-02-28 14:36:30
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@WebService(name = "wsdlService", targetNamespace = "com.wy.wsdl", serviceName = "test")
// @BindingType(SOAPBinding.SOAP12HTTP_BINDING)
public interface WsdlService {

	@WebMethod
	String getUsername(@WebParam String username);

	@WebMethod
	User getUser(User model);
}