package com.wy.cxf.client;

import java.util.Arrays;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

import com.wy.cxf.service.CxfService;
import com.wy.wsdl.model.User;

/**
 * cxf客户端调用cxf服务端代码,可以使用原生jdk的wsimport方式生成客户端调用,也可以用cxf的方式调用
 * 
 * @author ParadiseWY
 * @date 2020-10-14 15:22:14
 * @git {@link https://github.com/mygodness100}
 */
public class CxfClient {

	public static void main(String[] args) {
		handlerCxf();
	}

	/**
	 * 使用cxf方式发送webservice请求,但是该包可能会引起java底层相关包的冲突
	 */
	public static void handlerCxf() {
		JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
		Client client = dcf.createClient("http://localhost:5503/services/cxfService?wsdl");
		User user = new User();
		user.setUsername("username->cxf");
		Object[] result = new Object[0];
		try {
			// 类似反射的调用:方法名,从wsdl文件中查看;参数列表,从wsdl文件中查看
			result = client.invoke("getUser", user);
			System.out.println(Arrays.asList(result));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void handlerCxf1() {
		JaxWsProxyFactoryBean dcf = new JaxWsProxyFactoryBean();
		dcf.setServiceClass(CxfService.class);
		dcf.setAddress("http://localhost:5503/services/cxfService?wsdl");
		CxfService create = (CxfService) dcf.create();
		// CxfService create = dcf.create(CxfService.class);
		create.getUser(null);
	}
}