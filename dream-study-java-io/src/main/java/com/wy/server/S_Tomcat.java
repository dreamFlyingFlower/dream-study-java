package com.wy.server;

public class S_Tomcat {

	/**
	 * tomcat部署方式,3种
	 * @1.不需要改动tomcat的任何配置,直接将war包放在webapps下面
	 * @2.配置虚拟路径,在server.xml的Host标签里写Context标签,标签里的属性最重要的是docBase和path
	 * 		docBase指定程序访问的本地路径;path指定程序访问的外部资源地址,不包括ip和端口
	 * @3.在conf文件下新建Catalina文件夹,文件夹中新建一个以ip地址为名的文件,文件中放一个任意名称.xml
	 * 		详细的配置可见tomcat的说明文档Reference->Configuration->Containers->Context->Defining a context里的
	 * 		$CATALINA_BASE/conf/[enginename]/[hostname]/context.xml.default说明,见sesrver.xml
	 * 		可指定多个任意名称.xml,配置各自不同的路径
	 * @4.在3中的Context说明文档中可见各种server.xml中的各种配置属性说明
	 * @5.个呢server.xml同层目录下的web.xml中有一个servlet标签,该标签中的init-param中有一个listings的属性
	 * 		该属性的值为true时,若是在部署好的前端页面访问localhost:8080,但是不加资源标识符时,可拿到该tomcat中
	 * 		所有部署的资源列表,需要将这个选项改为false
	 * @6.利用消息头的Referer可以得到从那一个网站跳到当前页面
	 */
}
