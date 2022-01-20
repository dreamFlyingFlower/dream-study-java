package com.wy.actuator;

import org.springframework.boot.actuate.autoconfigure.metrics.export.jmx.JmxMetricsExportAutoConfiguration;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.info.InfoProperties;

/**
 * Actuator监控,可以使用使用actuator-admin的图形化界面展示监控数据
 * 
 * 需要配置文件中打开相应设置:management.endpoints.web.exposure.include: "*",打开所有监控,也可以部分打开.
 * 若使用Spring Security还需要设置Security的用户名和密码,否则无法登录,或直接设置Security放过所有请求
 * 
 * Actuator可以在网页上用GET访问程序的监控信息,2.0以后是访问地址为ip:port/actuator加上不同的监控类型,actuator可配置
 * 
 * <pre>
 * ip:port/actuator/info||/info.json:应用信息.该信息是配置文件中所有以info开头的配置以及继承{@link InfoProperties}的信息
 * ip:port/actuator/configprops||/configprops.json:所有配置属性
 * ip:port/actuator/autoconfig||/autoconfig.json:所有自动配置类信息
 * ip:port/actuator/beans||/beans.json:列出当前容器所有的bean
 * ip:port/actuator/health||/health.json:应用健康状况,系统相关信息,正常就是UP
 * ip:port/actuator/mappings||/mappings.json:系统所有的可调用URL,包括系统内置和自定义
 * ip:port/actuator/trace||/trace.json:追踪信息,最新的http请求
 * ip:port/actuator/dump||/dump.json:线程状态信息
 * ip:port/actuator/env||/env.json:所有的配置,包括系统的和自定义的
 * ip:port/actuator/env/{name:*}:某个指定的配置文件
 * ip:port/actuator/heapdump||heapdump.json:
 * ip:port/actuator/metrics||metrics.json:应用各项指标,内存,负载,GC等
 * ip:port/actuator/metrics/{name:.*}:某个指定的指标
 * ip:port/actuator/auditevents:审计事件
 * ip:port/actuator/shutdown:关闭当前应用(默认关闭),需要发送post请求到客户端关闭应用,实现优雅关机.需要在配置文件开启权限
 * </pre>
 * 
 * 自定义一个健康状态检测器:需实现{@link HealthIndicator},指示器名只能是xxxHealthIndicator,将指示器加入到Spring中
 * 
 * {@link CounterService}:程序计数,可能后期的版本没有该类了,需要查看文档.计数后的值会在actuator/metrics中显示
 * {@link GaugeService}:同CounterService,不过作用是给自定义的参数设置值
 * {@link JmxMetricsExportAutoConfiguration}:将actuator/metrics的监控值输出到JMX中,自定义百度
 * 
 * @auther 飞花梦影
 * @date 2021-05-17 23:01:18
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class ActuatorConfig {

}