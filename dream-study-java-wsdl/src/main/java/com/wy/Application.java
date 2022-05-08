package com.wy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.wy.wsdl.WsdlConfig;
import com.wy.wsdl.service.WsdlService;

/**
 * Webservice解析需要进行访问的url地址:wsimport -s 目录地址 -p 包名 完整url地址,需要jdk7以上
 * 
 * 页面WSDL中的元素解析:
 * 
 * <pre>
 * types:定义访问的类型
 * message:SOAP.SOAP协议有1.1和1.2版本,1.1可以访问1.1和1.2,1.2只能访问1.2
 * portType:指明服务的接口,并使用operation绑定in(输入)和out(输出)消息
 * binding:指定传递消息使用的格式
 * service:指定服务发布的名称
 * </pre>
 * 
 * 使用wsimport解析错误:
 * 
 * <pre>
 * 若出现解析组件's:schema' 错误,可将wsdl文件下载到本地,
 * 将<s:element ref="s:schema"/><s:any/>替换成<s:any minOccurs="2" maxOccurs="2"/>,
 * 该错误可能是因为jaxb不支持ref导致,其实可替换成任意标签
 * 
 * 在本地生成了相关java代码时,需要修改以WebService结尾的文件中的地址,2或3个地方
 * </pre>
 * 
 * 相关注解,example见 {@link WsdlConfig}, {@link WsdlService}:
 * 
 * <pre>
 * {javax.jws.WebService},该注解将被修饰的类发布成一个WebService服务
 * {javax.xml.ws.Endpoint},此类为端点服务类,它的publish方法可以将用WebService注解修饰的类绑定到固定的端口
 * </pre>
 * 
 * 网络可用的免费webservice服务,有次数限制:
 * 
 * <pre>
 * {@link http://www.webxml.com.cn/zh_cn/index.aspx}
 * 
 * 中国股票行情分时走势预览缩略图 WEB 服务,属于webxml.com.cn网站.支持深圳和上海股市的全部基金,债券和股票,即时更新
 * 返回数据:2种大小可选择的股票GIF分时走势预览缩略图字节数组和直接输出该预览缩略图
 * Endpoint: http://ws.webxml.com.cn/webservices/ChinaStockSmallImageWS.asmx
 * Disco: http://ws.webxml.com.cn/webservices/ChinaStockSmallImageWS.asmx?disco
 * WSDL: http://ws.webxml.com.cn/webservices/ChinaStockSmallImageWS.asmx?wsdl
 * 
 * 外汇-人民币即时报价 WEB 服务,属于webxml.com.cn网站.即时服务,获取信息,但不构成投资建议
 * Endpoint: http://ws.webxml.com.cn/WebServices/ForexRmbRateWebService.asmx
 * Disco: http://ws.webxml.com.cn/WebServices/ForexRmbRateWebService.asmx?disco
 * WSDL: http://ws.webxml.com.cn/WebServices/ForexRmbRateWebService.asmx?wsdl
 * 
 * 即时外汇汇率数据 WEB 服务,支持29种以上基本汇率和交叉汇率即时外汇汇率数据
 * Endpoint: http://ws.webxml.com.cn/WebServices/ExchangeRateWebService.asmx
 * Disco: http://ws.webxml.com.cn/WebServices/ExchangeRateWebService.asmx?disco
 * WSDL: http://ws.webxml.com.cn/WebServices/ExchangeRateWebService.asmx?wsdl
 * 
 * 中国股票行情数据 WEB 服务.支持深圳和上海股市的基金、债券和股票,输出GIF分时走势图,日/周/月K线图,及时行情
 * Endpoint: http://webservice.webxml.com.cn/WebServices/ChinaStockWebService.asmx
 * Disco: http://webservice.webxml.com.cn/WebServices/ChinaStockWebService.asmx?disco
 * WSDL: http://webservice.webxml.com.cn/WebServices/ChinaStockWebService.asmx?wsdl
 * </pre>
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