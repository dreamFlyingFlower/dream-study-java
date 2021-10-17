package com.wy.server;

/**
 * Tomcat服务器,本server.xml使用的版本为8.5.23
 * 
 * 部署方式,有以下3种:
 * 
 * <pre>
 * 1.不需要改动tomcat的任何配置,直接将war包放在webapps下面
 * 2.配置虚拟路径,在server.xml的Host标签里写Context标签,标签里的属性最重要的是docBase和path
 * 		docBase指定程序访问的本地路径;path指定程序访问的外部资源地址,不包括ip和端口
 * 3.在conf文件下新建Catalina文件夹,文件夹中新建一个以ip地址为名的文件,文件中放一个任意名称.xml
 * 		详细配置可见tomcat的说明文档Reference->Configuration->Containers->Context->Defining a context里的
 * 		$CATALINA_BASE/conf/[enginename]/[hostname]/context.xml.default说明,见sesrver.xml
 * 		可指定多个任意名称.xml,配置各自不同的路径
 * 4.在3中的Context说明文档中可见各种server.xml中的各种配置属性说明
 * 5.在server.xml同层目录下的web.xml中有一个servlet标签,该标签中的init-param中有一个listings的属性
 * 		该属性的值为true时,若是在部署好的前端页面访问localhost:8080,但是不加资源标识符时,可拿到该tomcat中
 * 		所有部署的资源列表,需要将这个选项改为false
 * 6.利用消息头的Referer可以得到从那一个网站跳到当前页面
 * </pre>
 * 
 * Tomcat首页-Server Status,在首页的右上角,要打开该页面,需要特定的配置,但在生产中是不能打开的
 * 
 * <pre>
 * 1.conf/tomcat-users.xml:将文件最下方的关于角色和用户的注释放开,修改密码.此处配置的就是用户和密码
 * 2.webapps/manager/META-INF/context.xml:将Context下的org.apache.catalina.valves.RemoteAddrValve的Value注释
 * </pre>
 * 
 * server.xml其他配置:
 * 
 * <pre>
 * 使用线程池:放开Executor注释,同时需要配置Connector的Executor属性,见37,46行
 * 配置NIO2协议:Connector:8.5.23默认使用的NIO协议,可以配置NIO2协议,见50行
 * 屏蔽AJP协议:因为有漏洞,见72行
 * </pre>
 * 
 * Tomcat堆栈中的常见线程
 * 
 * <pre>
 * main:主线程,通过启动包将容器组件拉起来,然后阻塞在8005端口,等待关闭.启动类为org.apache.catalina.startup.Bootstrap
 * catalina-utility:9以上有,是将8中的ContainerBackgroundProcessor线程和startStop线程合并了
 * acceptor:tomcat前端最外层线程,负责统一接收socket请求,底层就是ServerSocket.accept()
 * clientpoller:默认2个,NIO中的特有线程,reactor模式的实现者.接收acceptor交接过来的事件,对事件轮询后交给exec线程
 * exec:默认10个,tomcat的主要工作线程,接收poller线程推过来的IO事件,解析http协议,构建request和response,之后调用容器
 * NioBlockSelector.BlockPoller:默认2个,负责Servlet的输入和输出
 * 
 * Connector:在AbsrtactProtocol中初始化,初始化启动之后会启动Endpoint,接着启动poller线程和acceptor线程.
 * 		acceptor线程返回Socket之后推给NioChannel处理,之后通道和poller进行绑定
 * </pre>
 * 
 * NIO连接器原理
 * 
 * <pre>
 * 1.Acceptor线程将SocketChannel取出,传递给Poller线程,此时会产生线程阻塞,因此包装成PollEvent加入缓存队列
 * 2.Poller线程执行的就是NIO的selectKey,获得通道中的对应事件后,轮询操作,之后将selectKey和keyattachment传给工作线程池
 * 3.工作线程池调用Http11ConnectionHandler进行http协议解析,完成后将解析的内容包装成request,response对象
 * 4.CoyoteAdapter获得request,response,最终执行到业务中
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2021-10-15 20:12:57
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class S_Tomcat {

}