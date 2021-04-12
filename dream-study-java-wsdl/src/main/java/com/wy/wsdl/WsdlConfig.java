package com.wy.wsdl;

import javax.xml.ws.Endpoint;

import org.springframework.context.annotation.Bean;

import com.wy.wsdl.server.WsdlServiceImpl;

/**
 * 使用jdk自带的webservice发布服务和apache的cfx发布服务不一样
 * 
 * com.wy.wsdl:jdk原生的webservice服务发布<br>
 * com.wy.wsdl.service:原生jdk服务端接口类<br>
 * com.wy.wsdl.server:原生jdk服务端接口实现类<br>
 * com.wy.wsdl.client:原生jdk调用webservice服务的客户端代码
 * 
 * @apiNote 任意目录下,控制台命令{wsimport -s . 服务完整地址},可自动生成相关代码
 * @apiNote jdk自带的服务发布时端口要和项目不一样,否则端口占用报错;
 *          webservice访问的地址直接就是自定义地址ip:port/name?wsdl;
 *          cxf则是ip:port/services/name?wsdl,而且由apache自动分发到当前端口,而不需要另外定义端口
 * @apiNote cxf和原生的webservice不可同时发布,会报错,在EndPoint包会有冲突
 * 
 * @author ParadiseWY
 * @date 2020-10-11 11:12:34
 * @git {@link https://github.com/mygodness100}
 */
// @Configuration
public class WsdlConfig {

	@Bean
	public Endpoint endpoint() {
		String url = "http://localhost:5502/wsdlService";
		System.out.println("发布成功");
		return Endpoint.publish(url, new WsdlServiceImpl());
	}
}