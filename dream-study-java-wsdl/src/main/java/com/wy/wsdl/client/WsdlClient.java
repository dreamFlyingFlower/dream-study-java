package com.wy.wsdl.client;

/**
 * webservice客户端,除了当前类之外本包其他的类都是由wsimport命令生成,本类是真实调用webservice服务的类
 * 
 * 如何使用wsimport命令,见父类docs文件夹中的WebService.md文件
 * 
 * @author ParadiseWY
 * @date 2020-10-12 23:18:16
 * @git {@link https://github.com/mygodness100}
 */
public class WsdlClient {

	public static void main(String[] args) {
		// 根据服务端的wsdl文件,查看获得服务的类名
		WsdlServiceImplService service = new WsdlServiceImplService();
		// 根据服务端的wsdl文件,查看获得端点的类名
		WsdlServiceImpl port = service.getWsdlServiceImplPort();
		// 根据服务端的wsdl文件,查看可调动的方法以及参数
		String teString = port.getUsername("test");
		System.out.println(teString);
	}
}