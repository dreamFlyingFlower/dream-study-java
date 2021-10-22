package com.wy.wsdl;

import javax.xml.ws.Endpoint;

import org.springframework.context.annotation.Bean;

import com.wy.wsdl.server.WsdlServiceImpl;

/**
 * 使用JDK自带的webservice发布服务和apache的CFX发布服务不一样
 * 
 * com.wy.wsdl:jdk原生的webservice服务发布<br>
 * com.wy.wsdl.service:原生jdk服务端接口类<br>
 * com.wy.wsdl.server:原生jdk服务端接口实现类<br>
 * com.wy.wsdl.client:原生jdk调用webservice服务的客户端代码
 * 
 * 根据WebService的WSDL文件,可以直接生成相关业务代码:任意目录下,控制台{wsimport -s . WSDL服务完整地址}
 * 
 * JDK发布和CXF发布:
 * 
 * <pre>
 * JDK自带的服务发布时端口要和项目不一样,否则端口占用报错;WebService访问的地址直接就是自定义地址ip:port/name?wsdl;
 * CXF则是ip:port/services/name?wsdl,而且由apache自动分发到当前端口,而不需要另外定义端口
 * CXF和原生的WebService不可同时发布,会报错,在EndPoint包会有冲突
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2020-10-11 11:12:34
 * @git {@link https://github.com/dreamFlyingFlower}
 */
// @Configuration
public class WsdlConfig {

	@Bean
	public Endpoint endpoint() {
		// 注意此处若是用localhost或127.0.0.1发布会造成内网和外网ip地址无法访问的异常,最好使用ip地址进行发布
		String url = "http://localhost:5502/wsdlService";
		System.out.println("发布成功");
		return Endpoint.publish(url, new WsdlServiceImpl());
	}
}