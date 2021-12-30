package com.wy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Webservice解析需要进行访问的url地址:wsimport -s 目录地址 -p 包名 完整url地址,需要jdk7以上
 * 
 * 页面WSDL中的元素解析:
 * 
 * <pre>
 * types:定义访问的类型
 * message:SOAP
 * portType:指明服务的接口,并使用operation绑定in(输入)和out(输出)消息
 * binding:指定传递消息使用的格式
 * service:指定服务发布的名称
 * </pre>
 * 
 * @apiNote 若出现解析组件's:schema' 错误,可将wsdl文件下载到本地,
 *          将<s:element ref="s:schema"/><s:any/>替换成<s:any minOccurs="2"
 *          maxOccurs="2"/>,该错误可能是因为jaxb不支持ref导致,其实可替换成任意标签
 * @apiNote 若是出现上述问题,在本地生成了相关java代码时, 需要修改以WebService结尾的文件中的地址,2或3个地方
 * @apiNote 网络可用的webservice服务:{@link https://blog.csdn.net/qq_20545159/article/details/47903513}
 * @apiNote {javax.jws.WebService},该注解将被修饰的类发布成一个WebService服务
 *          {javax.xml.ws.Endpoint},此类为端点服务类,它的publish方法可以将用WebService注解修饰的类绑定到固定的端口
 *
 * @author 飞花梦影
 * @date 2020-10-11 10:59:16
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}