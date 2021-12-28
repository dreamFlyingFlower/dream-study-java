package com.wy;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.ResourceRegionHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.cbor.MappingJackson2CborHttpMessageConverter;
import org.springframework.http.converter.feed.AtomFeedHttpMessageConverter;
import org.springframework.http.converter.feed.RssChannelHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.JsonbHttpMessageConverter;
import org.springframework.http.converter.json.KotlinSerializationJsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.smile.MappingJackson2SmileHttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;
import org.springframework.session.web.http.SessionRepositoryFilter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite;
import org.springframework.web.method.support.InvocableHandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping;
import org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

/**
 * 自动加载视图解析{@link WebMvcAutoConfiguration},{@link DispatcherServletAutoConfiguration}
 * 对Web配置做一些个性化处理,可以重写{@link WebMvcConfigurer}中的方法,如format,converter,viewer等
 * 
 * 国际化:自动配置类{@link MessageSourceAutoConfiguration},配置前缀为spring.messages<br>
 * 需要新建多个同名但后缀不同的文件,默认文件名为messages,若不配置spring.messages.basename,
 * spring只会在resources下寻找,并不会扫描整个resources.同时该配置需要加上国际化文件的名称,多个文件用逗号隔开
 * 国际化文件名称命名规则为:文件名_国家英文全小写缩写_语言英文全大写缩写.properties
 * 
 * 浏览器访问静态资源不需要加上默认目录地址,直接访问即可.加载静态资源文件的顺序,默认都在resources目录下:
 * META-INF/resources->resources,注意该目录是在resources下又新建了同名的目录->static->public->当前项目根路径
 * 配置文件中通过spring.resources.static-location:classpath:/META-INF指定加载静态文件目录,优先级高于系统默认
 * 
 * 首页,即ip:port访问的页面,默认是index.html.从默认的资源目录下开始查找,优先级高的优先使用
 * 
 * 浏览器标签图标:favicon.ico,从资源目录下查找,同样优先级高的优先使用
 * 
 * src/main/resources资源目录下的templates目录主要存放Thymeleaf模板或者其他类似框架的页面模板
 * 
 * 各种拦截的执行顺序:filter->interceptor->controlleradvice->aspect->controller
 * filter:拿不到具体的方法和参数,能拿到request和response<br>
 * interceptor:拿不到具体的参数,能拿到request,response,具体的方法和结果
 * aspect:拿不到request和reponse,可以通过{@link RequestContextHolder#getRequestAttributes()}强转为
 * {@link ServletRequestAttributes}拿到,但是要注意远程调用时可能会拿不到
 * 
 * 注册servlet三大组件:Servlet,Filter,Listener,SpringBoot有各自对应的类来代替在web.xml中注册:
 * Servlet->{@link ServletRegistrationBean}<br>
 * Filter->{@link FilterRegistrationBean}<br>
 * Listener->{@link ServletListenerRegistrationBean}
 * 
 * Thymeleaf:在html标签中加入命名空间http://www.thymeleaf.org,自动提示<br>
 * Thymeleaf语法都只能在标签内使用,且都是以th:开头,具体语法见thy/index.html
 * 
 * {@link WebMvcAutoConfiguration#getMessageConverters}会默认加载几种数据转换器,根据条件不同而不同.
 * 如果要修改加载的{@link HttpMessageConverter}顺序,可以参照该方法.默认顺序如下
 * 
 * <pre>
 * {@link ByteArrayHttpMessageConverter}:字节数组消息
 * {@link StringHttpMessageConverter}:字符串消息
 * {@link ResourceHttpMessageConverter},{@link ResourceRegionHttpMessageConverter}:资源相关
 * {@link SourceHttpMessageConverter}:当属性 spring.xml.ignore=false 时才添加,默认添加
 * {@link AllEncompassingFormHttpMessageConverter}:我也不知道是什么
 * {@link AtomFeedHttpMessageConverter},{@link RssChannelHttpMessageConverter}:
 * 		存在{@link com.rometools.rome.feed.WireFeed}时才添加
 * {@link MappingJackson2XmlHttpMessageConverter}:XML转换器,
 * 		当属性 spring.xml.ignore=false,且{@link com.fasterxml.jackson.dataformat.xml.XmlMapper}存在时添加
 * {@link Jaxb2RootElementHttpMessageConverter}:XML转换器,
 * 		当属性 spring.xml.ignore=false,且{@link javax.xml.bind.Binder}存在时添加
 * {@link MappingJackson2HttpMessageConverter}:JSON转换器,
 * 		当{@link com.fasterxml.jackson.databind.ObjectMapper}和{@link com.fasterxml.jackson.core.JsonGenerator}都存在时添加
 * {@link GsonHttpMessageConverter}:JSON转换器,当{com.google.gson.Gson}存在时添加
 * {@link JsonbHttpMessageConverter}:JSON转换器,当{ avax.json.bind.Jsonb}存在时添加
 * {@link KotlinSerializationJsonHttpMessageConverter}:当{ otlinx.serialization.json.Json}存在时添加
 * {@link MappingJackson2SmileHttpMessageConverter}:当{@link com.fasterxml.jackson.dataformat.smile.SmileFactory}存在时添加
 * {@link MappingJackson2CborHttpMessageConverter}:当{@link com.fasterxml.jackson.dataformat.cbor.CBORFactory}存在时添加
 * </pre>
 * 
 * SpringSession管理原理:通过定制的{@link HttpServletRequest}返回定制的HttpSession
 * 
 * <pre>
 * {@link SessionRepositoryFilter}
 * {@link DelegatingFilterProxy}
 * {@link SessionRepositoryFilter#SessionRepositoryRequestWrapper }
 * {@link AbstractHttpSessionApplicationInitializer}:可以自定义Session操作,但比较繁琐,可使用Spring自带的配置
 * </pre>
 * 
 * {@link DispatcherServlet},解析前端URL接口以及视图主要逻辑类,由{@link DispatcherServletAutoConfiguration}自动引入
 * 
 * <pre>
 * {@link DispatcherServlet#initStrategies}:在刷新Spring上下文时初始化一系列解析器,包括URL接口,ViewResolver等等
 * {@link DispatcherServlet#initMultipartResolver}:初始化多媒体文件视图解析器
 * {@link DispatcherServlet#initLocaleResolver}:初始化本地自定义视图解析器
 * {@link DispatcherServlet#initThemeResolver}:初始化主题视图解析器
 * {@link DispatcherServlet#initHandlerMappings}:初始化请求URL Map,从上下文获得所有{@link HandlerMapping}子类并排序.
 * ->{@link BeanNameUrlHandlerMapping}:通过定义的 beanName 进行查找要请求的Controller
 * ->{@link RequestMappingHandlerMapping}:通过注解{@link RequestMapping}来查找对应的Controller
 * {@link DispatcherServlet#initHandlerAdapters}:初始化适配器
 * {@link DispatcherServlet#initHandlerExceptionResolvers}:初始化异常视图
 * {@link DispatcherServlet#initRequestToViewNameTranslator}:
 * {@link DispatcherServlet#initViewResolvers}:初始化视图解析器,如{@link BeanNameViewResolver},{@link FreeMarkerViewResolver}
 * {@link DispatcherServlet#initFlashMapManager}:
 * {@link DispatcherServlet#doDispatch}:处理从前端传过来的URL请求,判断是否为媒体文件请求,前置方法,后置方法等
 * {@link AbstractHandlerMethodAdapter#handle}:真正处理请求的方法
 * ->{@link RequestMappingHandlerAdapter#handleInternal}:处理请求
 * ->{@link RequestMappingHandlerAdapter#invokeHandlerMethod}:处理请求
 * -->{@link ServletInvocableHandlerMethod#invokeAndHandle}:继续处理请求
 * -->{@link InvocableHandlerMethod#invokeForRequest}:从请求,视图等中获得请求参数,执行请求
 * -->{@link InvocableHandlerMethod#doInvoke}:利用代理执行真正的请求方法
 * --->{@link HandlerMethodReturnValueHandlerComposite#handleReturnValue}:处理上一步返回的结果,类似JSON序列化等
 * ---->{@link RequestResponseBodyMethodProcessor#handleReturnValue()}:默认跳到该类处理返回结果
 * {@link DispatcherServlet#processDispatchResult}:将doDispath的结果渲染到视图
 * 		如果有视图的话,使用resolveViewName()解析View对象;没有返回视图的话,尝试RequestToViewNameTranslator
 * </pre>
 * 
 * {@link AsyncHandlerInterceptor}:针对异步请求的接口,异步方法不会调用后置拦截器方法,详见{@link DispatcherServlet}1063行
 * 
 * 浏览器和服务器之间交互过程:
 * 
 * <pre>
 * 浏览器输入URL地址,查找DNS,解析URL对应的IP地址
 * 初始化网络连接,经过3次握手之后发送HTTP请求,网络请求传输到服务器
 * Web服务器接收到请求,经过处理转发到相应的Web应用
 * Web应用处理请求,并返回相应的应答
 * 网络传输应答内容给浏览器,浏览器解析从服务器返回的应答,并开始渲染和绘制页面
 * 根据HTML,CSS构建DOM,加载解析样式,此时不包括脚本,图片等资源
 * 根据DOM和CSS构建渲染树,加载需要的图片,脚本等资源
 * 构建过程中在适当的时候会把构建好的部分渲染到页面上,直到渲染完成
 * 整个页面加载完成,触发onLoad事件
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2020-11-18 13:31:27
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}