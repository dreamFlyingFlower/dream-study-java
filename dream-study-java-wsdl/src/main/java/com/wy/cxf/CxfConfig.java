package com.wy.cxf;

import javax.xml.ws.Endpoint;

import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.wy.cxf.service.CxfService;

/**
 * 利用CXF发送WebService请求,发布服务
 * 
 * ServerFactoryBean和JaxWsServerFactoryBean发布webservice方式虽然一样,但是生成的wsdl不一样
 * JaxWsServerFactoryBean生成的wsdl和原生的jdk发布webservice的一样,
 * ServerFactoryBean在wsdl中的service标签内容和jdk不一样,name属性不会添加service
 * JaxWsServerFactoryBean比ServerFactoryBean更严格,需要在发布的类上添加WebService注解,否则方法不生效
 * 
 * CXF的wsdl文件访问地址ip:port/{context-path}/services/name?wsdl,由apache自动分发到当前端口,而不需要另外定义端口
 * 
 * 当服务发布成功之后,在wsdl的页面中wsdl:import标签中的location属性指向的url对webservice的服务描述更具体
 * 
 * @author 飞花梦影
 * @date 2020-10-14 14:38:20
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
public class CxfConfig {

	@Autowired
	private Bus bus;

	@Autowired
	private CxfService cxfService;

	@Bean
	public Endpoint endpoint() {
		EndpointImpl endpointImpl = new EndpointImpl(bus, cxfService);
		// 发布访问地址中的name
		endpointImpl.publish("/cxfService");
		return endpointImpl;
	}
}