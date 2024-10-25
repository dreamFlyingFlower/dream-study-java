package dream.study.spring.actuator;

import org.springframework.boot.actuate.autoconfigure.metrics.export.jmx.JmxMetricsExportAutoConfiguration;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.info.InfoProperties;

/**
 * Actuator监控,可以使用使用actuator-admin的图形化界面展示监控数据
 * 
 * 需要配置文件中打开相应设置:management.endpoints.web.exposure.include: "*",打开所有监控,也可以部分打开.
 * 若使用Spring Security还需要设置Security的用户名和密码,否则无法登录,或直接设置Security放过所有请求
 * 
 * Actuator可直接由Web地址访问,2.0以后是访问地址为ip:port/server.context-path或management.endpoints.web.base-path/actuator/监控类型:
 * 
 * <pre>
 * actuator/auditevents:显示应用暴露的审计事件.比如认证进入,订单失败
 * actuator/autoconfig||/autoconfig.json:所有自动配置类信息
 * actuator/beans||/beans.json:列出当前容器所有的bean以及依赖关系
 * actuator/conditions||/conditions.json:提供一份自动配置生效的条件情况,记录哪些自动配置条件通过了,哪些没通过
 * actuator/configprops||/configprops.json:描述配置属性(包含默认值)如何注入Bean
 * actuator/dump||/dump.json:线程状态信息
 * actuator/env||/env.json:所有环境属性,包括系统的和自定义的
 * actuator/env/{name:*}:根据名称获取特定的环境属性值
 * actuator/flyway:提供一份 Flyway 数据库迁移信息
 * actuator/liquidbase:显示Liquibase 数据库迁移的纤细信息
 * actuator/health||/health.json:应用健康状况,系统相关信息,正常就是UP,由 HealthIndicator 的实现类提供
 * actuator/heapdump||heapdump.json:dump 一份应用的 JVM 堆信息
 * actuator/httptrace:显示最近100个HTTP request/repsponse
 * actuator/info||/info.json:应用信息.该信息是配置文件中所有以info开头的配置以及继承{@link InfoProperties}的信息
 * actuator/logfile:返回log file中的内容(如果 logging.file 或者 logging.path 被设置)
 * actuator/loggers:显示和修改配置的loggers
 * actuator/mappings||/mappings.json:系统所有的可调用URL,包括系统内置和自定义
 * actuator/metrics||metrics.json:应用各项指标,内存,负载,GC,内存用量和HTTP请求计数等
 * actuator/metrics/{name:.*}:某个指定的指标
 * actuator/scheduledtasks:展示应用中的定时任务信息
 * actuator/sessions:如果使用了 Spring Session,展示应用中的 HTTP sessions 信息
 * actuator/shutdown:关闭当前应用(默认关闭),需要发送post请求到客户端关闭应用,实现优雅关机.需要endpoints.shutdown.enabled设置为true
 * actuator/threaddump:获取线程活动的快照
 * actuator/trace||/trace.json:追踪信息,最新的http请求
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