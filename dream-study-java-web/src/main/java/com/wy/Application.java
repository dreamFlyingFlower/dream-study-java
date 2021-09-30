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
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.JsonbHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;
import org.springframework.session.web.http.SessionRepositoryFilter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.DelegatingFilterProxy;

/**
 * 自动加载视图解析{@link WebMvcAutoConfiguration},{@link DispatcherServletAutoConfiguration}
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
 * {@link MappingJackson2HttpMessageConverter}:JSON转换器
 * {@link GsonHttpMessageConverter}:JSON转换器
 * {@link JsonbHttpMessageConverter}:JSON转换器
 * {@link MappingJackson2XmlHttpMessageConverter}:XML转换器
 * {@link Jaxb2RootElementHttpMessageConverter}:XML转换器
 * {@link ProtobufHttpMessageConverter}:给机器读的,字节码协议转换
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
 * @author ParadiseWY
 * @date 2020-11-18 13:31:27
 * @git {@link https://github.com/mygodness100}
 */
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}