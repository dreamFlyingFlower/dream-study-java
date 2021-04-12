package com.wy.wsdl.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import com.wy.wsdl.model.User;

/**
 * 一个自己写的websevice测试服务
 * 
 * @apiNote WebService注解:表明是一个webservice服务,<br>
 *          name:该服务的名称,会接在url后面<br>
 *          targetNamespace:是xml命名空间,若不定义,则是该包路径反过来的字符串
 *          serviceName:为了不暴露服务类真正的名字,真实类的一个别名
 * @apiNote WebMethod注解:表明是一个需要发布的服务方法,若不写该注解则接口中所有方法都是发布,
 *          该注解只能在有WebService注解的接中使用,静态方法和final方法不可发布<br>
 *          exclude:排除某个方法不暴露为webservice方法,true不暴露
 *          operationName:同serviceName,给服务中的方法起一个别名
 * @apiNote WebParam注解:对参数进行修饰或做特定的处理,指定名称或别名等
 *          name:指定参数的名称,从wsdl的schemaLocation标签中的url才可以看出
 * @apiNote BindingType注解:webservice服务默认发布时使用的是soap1.1协议,使用该注解可指定发布的协议类型
 * @apiNote 该接口必须有,否则无法传递对象类参数;若不需要对象类参数,可不实现该接口
 * 
 * @author ParadiseWY
 * @date 2019年2月28日 下午2:36:30
 * @git {@link https://github.com/mygodness100}
 */
@WebService(name = "wsdlService", targetNamespace = "com.wy.wsdl", serviceName = "test")
// @BindingType(SOAPBinding.SOAP12HTTP_BINDING)
public interface WsdlService {

	@WebMethod
	String getUsername(@WebParam String username);

	@WebMethod
	User getUser(User model);
}