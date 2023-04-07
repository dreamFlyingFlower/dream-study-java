package com.wy;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanWrapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.DefaultErrorViewResolver;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.method.annotation.RequestHeaderMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite;
import org.springframework.web.method.support.InvocableHandlerMethod;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.FrameworkServlet;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.HttpServletBean;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping;
import org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver;
import org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.ServletCookieValueMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import com.wy.resolver.MyExceptionResolver;

/**
 * 自动加载视图解析{@link WebMvcAutoConfiguration},{@link DispatcherServletAutoConfiguration},{@link EnableWebMvc}
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
 * Web结果解析器HttpMessageConverters,如果需要自定义,可实现{@link WebMvcConfigurer}:
 * 
 * <pre>
 * {@link WebMvcAutoConfigurationAdapter#configureMessageConverters}会默认加载几种数据转换器,根据条件不同而不同.
 * {@link HttpMessageConverters}:处理数据转换列表,构造加载
 * {@link HttpMessageConverters#getCombinedConverters()}:添加解析器
 * {@link HttpMessageConverters#getDefaultConverters()}:添加默认的解析器
 * {@link WebMvcConfigurationSupport#addDefaultHttpMessageConverters}:添加默认结果解析器,默认顺序如下
 * ->{@link ByteArrayHttpMessageConverter}:字节数组消息
 * ->{@link StringHttpMessageConverter}:字符串消息
 * ->{@link ResourceHttpMessageConverter},{@link ResourceRegionHttpMessageConverter}:资源相关
 * ->{@link SourceHttpMessageConverter}:当属性 spring.xml.ignore=false 时才添加,默认添加
 * ->{@link AllEncompassingFormHttpMessageConverter}:我也不知道是什么
 * ->{@link AtomFeedHttpMessageConverter},{@link RssChannelHttpMessageConverter}:
 * 		存在{@link com.rometools.rome.feed.WireFeed}时才添加
 * ->{@link MappingJackson2XmlHttpMessageConverter}:XML转换器,
 * 		当属性 spring.xml.ignore=false,且{@link com.fasterxml.jackson.dataformat.xml.XmlMapper}存在时添加
 * ->{@link Jaxb2RootElementHttpMessageConverter}:XML转换器,
 * 		当属性 spring.xml.ignore=false,且{@link javax.xml.bind.Binder}存在时添加
 * ->{@link MappingJackson2HttpMessageConverter}:JSON转换器,
 * 		当{@link com.fasterxml.jackson.databind.ObjectMapper}和{@link com.fasterxml.jackson.core.JsonGenerator}都存在时添加
 * ->{@link GsonHttpMessageConverter}:JSON转换器,当{com.google.gson.Gson}存在时添加
 * ->{@link JsonbHttpMessageConverter}:JSON转换器,当{ avax.json.bind.Jsonb}存在时添加
 * ->{@link KotlinSerializationJsonHttpMessageConverter}:当{ otlinx.serialization.json.Json}存在时添加
 * ->{@link MappingJackson2SmileHttpMessageConverter}:当{@link com.fasterxml.jackson.dataformat.smile.SmileFactory}存在时添加
 * ->{@link MappingJackson2CborHttpMessageConverter}:当{@link com.fasterxml.jackson.dataformat.cbor.CBORFactory}存在时添加
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
 * SpringMVC执行流程
 * 
 * <pre>
 * 1.用户发送请求至前端控制器 {@link DispatcherServlet}
 * 2. {@link DispatcherServlet}收到请求调用 {@link HandlerMapping}处理器映射器
 * 3.处理器映射器找到具体的处理器(可以是XML配置,注解),生成处理器对象以及处理器拦截器一并返回给 {@link DispatcherServlet}
 * 4. {@link DispatcherServlet}调用 {@link HandlerAdapter} 处理器适配器
 * 5. {@link HandlerAdapter}经过适配调用具体的处理器(Controller)
 * 6. {@link Controller}:执行完成返回 {@link ModelAndView}
 * 7. {@link HandlerAdapter}将Controller执行结果 ModelAndView 返回给 {@link DispatcherServlet}
 * 8. {@link DispatcherServlet}将ModelAndView 传给 {@link ViewResolver} 视图解析器
 * 9. {@link ViewResolver}解析后返回具体的 View
 * 10. {@link DispatcherServlet}根据View进行渲染视图
 * 11. {@link DispatcherServlet}响应用户
 * </pre>
 * 
 * {@link DispatcherServletAutoConfiguration}:引入{@link DispatcherServlet},引入文件视图等
 * 
 * <pre>
 * 1.{@link HttpServletBean#init()}:继承{@link HttpServlet},在第一次调用Web请求时会调用init(),该初始化的作用:
 * 		1.将Servlet初始化参数设置到该组件上,通过{@link BeanWrapper}简化设置过程,方便后续使用
 * 		2.提供给子类初始化扩展点:initServletBean(),该方法由{@link FrameworkServlet}重写
 * 2.{@link FrameworkServlet#initServletBean()}:Web上下文初始化,该方法主要作用:
 * 		1.初始化Web上下文
 * 		2.提供给子类初始化扩展点
 * ->2.1->3.{@link FrameworkServlet#initWebApplicationContext}:初始化上下文,调用 {@link DispatcherServlet}
 * ->2.2.{@link FrameworkServlet#initFrameworkServlet}:空方法,主要是为了扩展,由子类自定义实现
 * 3.{@link FrameworkServlet#onRefresh}:调用子类DispatcherServlet#onRefresh
 * 4.{@link DispatcherServlet#onRefresh}:主要是调度.解析前端URL,执行业务,返回视图等
 * 5.{@link DispatcherServlet#initStrategies}:在刷新Spring上下文时初始化一系列解析器,包括URL接口,ViewResolver等等
 * 6.{@link DispatcherServlet#initMultipartResolver}:初始化多媒体文件视图解析器,主要用于文件上传
 * 7.{@link DispatcherServlet#initLocaleResolver}:初始化本地自定义视图解析器
 * 8.{@link DispatcherServlet#initThemeResolver}:初始化主题视图解析器
 * 9.{@link DispatcherServlet#initHandlerMappings}:初始化请求URL Map,从上下文获得所有{@link HandlerMapping}子类并排序.
 * 		将请求映射到处理器,返回一个{@link HandlerExecutionChain},它包括一个处理器,多个{@link HandlerInterceptor}拦截器
 * ->9.1.{@link HandlerMapping}:通过扩展处理器映射器实现不同的映射凡是,如BeanName,注解等
 * ->9.2.{@link BeanNameUrlHandlerMapping}:通过定义的 beanName 进行查找要请求的Controller
 * ->9.3.{@link RequestMappingHandlerMapping}:通过注解{@link RequestMapping}来查找对应的Controller
 * {@link DispatcherServlet#initHandlerAdapters}:初始化适配器,以便支持多种类型的处理器( HandlerExecutionChain 中的处理器)
 * ->9.4.{@link HandlerAdapter}:通过扩展处理器适配器,支持更多类型的处理器
 * 10.{@link DispatcherServlet#initHandlerExceptionResolvers}:初始化异常视图,解析执行过程中的异常
 * 11.{@link DispatcherServlet#initRequestToViewNameTranslator}:将请求到视图之间进行转换
 * 12.{@link DispatcherServlet#initViewResolvers}:初始化视图解析器,如{@link BeanNameViewResolver},{@link FreeMarkerViewResolver}
 * 13.{@link DispatcherServlet#initFlashMapManager}:用于管理FlashMap的策略接口,FlashMap用于存储一个请求的输出,
 * 		当进入请求时作为该请求的输入,通常用于重定向场景
 * 14.{@link DispatcherServlet#doDispatch}:处理从前端传过来的URL请求,判断是否为媒体文件请求,前置方法,后置方法等
 * 15.{@link AbstractHandlerMethodAdapter#handle}:真正处理请求的方法
 * ->15.1.{@link RequestMappingHandlerAdapter#handleInternal}:处理请求
 * ->15.2.{@link RequestMappingHandlerAdapter#invokeHandlerMethod}:处理请求
 * -->15.2.1.{@link ServletInvocableHandlerMethod#invokeAndHandle}:继续处理请求
 * -->15.2.2.{@link InvocableHandlerMethod#invokeForRequest}:从请求,视图等中获得请求参数,执行请求
 * -->15.2.3.{@link InvocableHandlerMethod#doInvoke}:利用代理执行真正的请求方法
 * --->15.2.3.1.{@link HandlerMethodReturnValueHandlerComposite#handleReturnValue}:处理上一步返回的结果,类似JSON序列化等
 * ---->15.2.3.1.1.{@link RequestResponseBodyMethodProcessor#handleReturnValue()}:默认跳到该类处理返回结果
 * 16.{@link DispatcherServlet#processDispatchResult}:将doDispath的结果渲染到视图
 * 		如果有视图的话,使用resolveViewName()解析View对象;没有返回视图的话,尝试RequestToViewNameTranslator
 * 17.再次调用Web请求时,入口为{@link HttpServlet#service},实际调用的是子类{@link FrameworkServlet#service}
 * </pre>
 * 
 * {@link EnableWebMvc}:自动导入DelegatingWebMvcConfiguration,该类的作用:
 * 
 * <pre>
 * 1.把所有系统中的 WebMvcConfigurer 拿过来,所有功能的定制都是这些 WebMvcConfigurer 合起来一起生效
 * 2.自动配置了一些非常底层的组件,RequestMappingHandlerMapping、这些组件依赖的组件都是从容器中获取
 * 3.只保证最基本的使用,同时会导致 {@link WebMvcAutoConfiguration}失效
 * 4.因为WebMvcAutoConfiguration生效的条件中必须不存在 WebMvcConfigurationSupport,而
 * 		DelegatingWebMvcConfiguration 继承自 WebMvcConfigurationSupport
 * </pre>
 * 
 * 文件上传流程:
 * 
 * <pre>
 * {@link MultipartAutoConfiguration}:文件上传自动配置
 * ->{@link MultipartConfigElement}:文件上传参数解析
 * ->{@link StandardServletMultipartResolver}:加载了名为multipartResolver的标准Servlet文件上传解析器
 * 
 * {@link DispatcherServlet#doDispatch}:处理请求
 * {@link DispatcherServlet#checkMultipart}:检查是否为文件上传请求,返回MultipartHttpServletRequest
 * {@link RequestMappingHandlerAdapter#invokeHandlerMethod}:解析方法调用,获得文件上传视图解析器,其他流程同一般请求
 * </pre>
 * 
 * 异常解析流程
 * 
 * <pre>
 * {@link ErrorMvcAutoConfiguration}:异常错误处理自动配置
 * ->{@link DefaultErrorAttributes}:定义错误页面中可以包含哪些数据
 * ->{@link BasicErrorController}:默认处理/error的请求,json+白页适配响应,还会存放一个key为error的view组件
 * ->{@link WhitelabelErrorViewConfiguration}:白页,注入key为error的StaticView组件,默认组件
 * {@link ControllerAdvice}+{@link ExceptionHandler}: 处理全局异常,底层是 {@link ExceptionHandlerExceptionResolver}
 * {@link ResponseStatus}+自定义异常(继承Exception或其他Exception):底层是{@link ResponseStatusExceptionResolver}
 * {@link ErrorViewResolver}:自定义异常视图需实现该接口
 * {@link HandlerExceptionResolver}:自定义异常需要实现该接口,和视图解析差不多,见{@link MyExceptionResolver}
 * {@link DefaultHandlerExceptionResolver}:Spring底层默认异常处理器
 * 
 * {@link DispatcherServlet#doDispatch}:执行Web请求
 * {@link DispatcherServlet#processDispatchResult}:处理结果与异常
 * {@link DispatcherServlet#processHandlerException}:循环异常处理器处理异常
 * {@link DefaultErrorViewResolver#resolveErrorView}:默认错误视图解析器,将响应码作为错误页的地址
 * {@link ExceptionHandlerExceptionResolver}:专门用来处理{@link ControllerAdvice}和{@link ExceptionHandler}的异常
 * {@link ResponseStatusExceptionResolver}:根据自定义异常上的注解解析调用
 * </pre>
 * 
 * {@link ServletWebServerFactoryAutoConfiguration}:自动配置Web容器解析
 * 
 * <pre>
 * {@link ServletWebServerFactoryAutoConfiguration}:导入了Tomcat,Jetty,Undertow3个容器
 * ->{@link #ServletWebServerFactoryConfiguration.EmbeddedTomcat}:注册{@link TomcatServletWebServerFactory}
 * ->{@link #ServletWebServerFactoryConfiguration.EmbeddedJetty}:注册{@link JettyServletWebServerFactory}
 * ->{@link #ServletWebServerFactoryConfiguration.EmbeddedUndertow}:注册{@link UndertowServletWebServerFactory}
 * {@link ServletWebServerApplicationContext#refresh}:{@link AnnotationConfigServletWebServerApplicationContext}继承该类,
 * 		AnnotationConfig在调用refresh()时会先调用ServletWeb的refresh(),但是默认只是调用{@link AbstractApplicationContext#refresh}
 * {@link ServletWebServerApplicationContext#onRefresh}:{@link AbstractApplicationContext#onRefresh}未做任何处理,由子类实现,
 * 		ServletWeb重写了该方法,调用了父类方法之外,同时创建了Web容器
 * ->{@link ServletWebServerApplicationContext#createWebServer}:寻找 ServletWebServerFactory 并引导创建服务器
 * {@link ServletWebServerFactory}:ServletWeb容器工厂,用来创建Web容器,根据配置不同创建不同的工厂
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
 * Servlet与Filter执行原理:
 * 
 * <pre>
 * 当 Servlet 实例被创建好后,会将该 Servlet 实例的引用存放到一个 Map 中.
 * 该 Map的 key 为 URI,而 value 则为 Servlet 实例的引用,即 Map<String, Servlet>.
 * 当 Web 容器从请求中分离出 URI 后,会首先到这个 Map 中查找是否存在其所对应的 value.
 * 若存在,则直接调用其 service()方法;若不存在,则需要创建该 Servlet 实例.
 * 在Web 容器的内存中还存在一个 Map ,该 Map 的 key 为 URI, value 为 web.xml中配置的与之对应的 Servlet 的全限定类名,即 Map<String, String>.
 * 当 Web 容器从用户请求中分离出 URI后,到第一个 Map 中又没有找到其所对应的 Servlet实例,则会马上查找这第二个 Map,
 * 从中找到其所对应的类名,再根据反射,创建这个 Servlet 实例,然后再将这个创建好的 Servlet 的引用放入到第一个 Map 中
 * 
 * 存放 Servlet 信息有两个Map,存放 Filter 信息的Map 只有一个,因为 Filter 的创建时机不同,是在服务器启动时由 web 容器自动创建的.
 * Map 的 key 是 Filter 的<url-pattern/>.若使用了<servlet-name/>,则会将指定的 Servlet 的<url-pattern/>放到 Map 中作为 key,value 为该 Filter 的引用
 * 在应用被启动时,服务器会自动将所有的 Filter 实例创建,并将它们的引用放入到相应 Map 的 value 中.
 * 在服务器中,对于每一个请求,还存在一个数组,用于存放满足当前请求的所有 Filter及最终的目标资源.
 * 当请求到达服务器后,服务器会解析出 URI,然后会先从 Filter 的 Map 中查找所有与该请求匹配的 Filter,每找到一个就将其引用存放到数组中,
 * 然后继续查找,直到将所有匹配的 Filter 全部找到并添加到数组中.这个数组就是对于当前请求所要进行处理的一个链,包含多个 Filter.
 * 服务器将按照这个链的顺序对请求进行依次过滤处理
 * Filter 的 Map 的查询过程与 Servlet 的 Map 的查询过程是不同的:
 * Servlet 的只要找到一个匹配的 key就不再查找;而 Filter则是遍历所有 key,将所有匹配的元素都查找出来
 * </pre>
 * 
 * 相关URL注解:
 * 
 * <pre>
 * {@link RequestMapping}:请求URL映射
 * ->{@link RequestMapping#params()}:请求中需要包含的参数名
 * ->{@link RequestMapping#headers():}:请求头中需要包含的属性
 * ->{@link RequestMapping#consumes()}:方法接受的请求类型
 * ->{@link RequestMapping#produces()}:方法返回的响应类型
 * {@link RequestHeader}:从请求头获得值,如charset,content-type等,由{@link RequestHeaderMethodArgumentResolver}解析
 * {@link CookieValue}:从请求中获取cookie值,由{@link ServletCookieValueMethodArgumentResolver}解析
 * {@link ModelAttribute}:修饰方法时表示在该类中的方法执行之前先执行被修饰的方法,类似于前置切面;
 * 		修饰参数时表示可以从{@link Model}中取值并赋值给被修饰的参数
 * {@link SessionAttribute}:从session中取出数据
 * {@link SessionAttributes}:存数据到session中,需要通过{@link Model}或{@link ModelMap}实现
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