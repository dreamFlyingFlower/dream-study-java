package com.wy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动类
 * 
 * <pre>
 * 修改Eclipse运存:eclipse目录下的eclipse.ini,添加两行:-Xms1024m(最小运存),-Xmx2048m(最大运存)
 * 修改JDK运存:preferences->Java->Installed JRE->Edit->Default VM Arguments:-Xms512m -Xmx2014m
 * 修改单个项目运存:Run AS->Run Configuration->Arguments->VM arguments:-Xms1024m -Xmx2048m
 * Tomcat:修改start.sh或catalina.bat,添加:JAVA_OPTS='-Xms256m -Xmx512m -XX:MaxNewSize=256m -XX:MaxPermSize=256m'
 * </pre>
 * 
 * 一些工具以及问题
 * 
 * <pre>
 *	Jenkins:代码持续集成自动化部署
 * Gitlab-Cli:代码测试集成自动部署
 * jmeter,loadrunner:压力测试工具
 * sonarqube:代码检测工具,findbugs检查代码中的bug
 * Kryo:更好的序列化框架
 * apache-james:搭建私人的邮件服务器,Foxmail进行邮件测试
 * </pre>
 * 
 * jacoco:maven插件,见pom.xml.自动化生成测试文档,帮助完善代码
 * 
 * <pre>
 * mvn help:describe -Dplugin=org.jacoco:jacoc-maven-plugin -Ddetail:查看jacoco的配置详情,在pom.xml中配置完后进行打包即可
 * 打包通过之后,可以在项目的target中找到site目录,将里面的index.html在网页中打开即可
 * 如果有显示为红色则表示单测不通过,需要修改
 * </pre>
 * 
 * CAP:一致性,可用性,分区容忍性,三个要素最多满足2个,不可能同时满足3个
 * 
 * <pre>
 *	C:Consistency,在分布式系统中的所有数据备份,在同一时刻是否同样的值,如事务的强一致性
 *	A:Availability,在集群中部分节点故障后,集群整体是否还能响应客户端的请求
 *	P:Partition tolerance,多节点必须在限定的时间内完成数据的一致性,若无法完成,就表示发生了分区,必须做出选择
 * </pre>
 * 
 * 工具类起名:
 * 
 * <pre>
 * Bootstrap,Starter:一般作为程序启动器使用,或者作为启动器的基类
 * Processor:某一类功能的处理器,用来表示某个处理过程,是一系列代码片段的集合
 * Manager:对有生命状态的对象进行管理,通常作为某一类资源的管理入口
 * Holder:表示持有某个或者某类对象的引用,并可以对其进行统一管理.多见于不好回收的内存统一处理,或者一些全局集合容器的缓存
 * Factory:工厂模式的命名
 * Provider:Provider = Strategy + Factory.Provider一般是接口或者抽象类,以便能够完成子实现
 * Registrar:注册并管理一系列资源
 * Engine:一般是核心模块,用来处理一类功能
 * Service:某个服务
 * Task:某个任务
 * Context:如果程序执行有一些变量,需要从函数执行的入口开始,一直传到大量子函数执行完毕之后
 * Propagator:传播,繁殖.用来将context中传递的值进行复制,添加,清除,重置,检索,恢复等动作.通常,它会提供一个叫做propagate的方法,实现真正的变量管理
 * Handler,Callback,Trigger,Listener:回调处理方法.
 * 		Callback通常是一个接口,用于响应某类消息,进行后续处理;
 * 		Handler通常表示持有真正消息处理逻辑的对象,它是有状态的;
 * 		Tigger触发器代表某类事件的处理,属于Handler,通常不会出现在类的命名中;
 * 		Listener的应用更加局限,通常在观察者模式中用来表示特定的含义
 * Aware:它有点回调的意思
 * Metric,Monitor:监控数据
 * Estimator:估计,统计,用于计算某一类统计数值的计算器
 * Accumulator:累加器,用来缓存累加的中间计算结果,并提供读取通道
 * Tracker:一般用于记录日志或者监控值,通常用于apm中
 * Allocator:与存储相关,通常表示内存分配器或者管理器
 * Chunk:表示一块内存,如果想要对一类存储资源进行抽象,并统一管理,可以采用它
 * Arena:舞台,竞技场,它普遍用于各种存储资源的申请、释放与管理,为不同规格的存储chunk提供舞台,好像也是非常形象的表示
 * Pool:池子
 * Pipeline,Chain:一般用在责任链模式中
 * Filter:过滤器,用来筛选某些满足条件的数据集,或者在满足某些条件的时候执行一部分逻辑
 * Interceptor:拦截器,和Filter差不多
 * Evaluator:评估器.可用于判断某些条件是否成立,一般内部方法evaluate会返回bool
 * Detector:探测器.用来管理一系列探测性事件,并在发生的时候能够进行捕获和响应
 * Cache:缓存
 * Buffer:缓冲,它一般用在数据写入阶段
 * Composite:将相似的组件进行组合,并以相同的接口或者功能进行暴露,使用者不知道这到底是一个组合体还是其他个体
 * Wrapper:用来包装某个对象,做一些额外的处理,以便增加或者去掉某些功能
 * Option, Param,Attribute:配置信息.和Properties的区别并不大
 * Tuple:元组.由于Java中缺乏元组结构,我们通常会自定义这样的类
 * Aggregator:聚合器,可以做一些聚合计算
 * Iterator:迭代器.在数据集很大的时候,需要进行深度遍历,迭代器可以说是必备的,使用迭代器还可以在迭代过程中安全的删除某些元素
 * Batch:某些可以批量执行的请求或者对象
 * Limiter:限流器,使用漏桶算法或者令牌桶来完成平滑的限流
 * Strategy:策略模式使用
 * Adapter:适配器使用
 * Action,Command:将一个请求封装为一个对象,从而使你可用不同的请求对客户进行参数化,对请求排队或记录请求日志,以及支持可撤销的操作
 * 		用来表示一系列动作指令,用来实现命令模式,封装一系列动作或者功能.Action一般用在UI操作上,后端框架可以无差别的使用
 * Event:一系列事件.在语义上,Action,Command等,来自于主动触发,Event来自于被动触发
 * Delegate:代理或者委托模式.委托模式是将一件属于委托者做的事情,交给另外一个被委托者来处理
 * Builder:建造者模式
 * Template:模板方法类的命名
 * Proxy:代理模式
 * Converter,Resolver:转换和解析.用于不同对象之间的转换,一般特别复杂的转换或者有加载过程的需求,可以使用Resolver
 * Parser:表示非常复杂的解析器,比如解析DSL
 * Customizer:表示对某个对象进行特别的配置
 * Formatter:格式化类
 * Packet:通常用于网络编程中的数据包
 * Protocol:用户网络编程,用来表示某个协议
 * Encoder、Decoder、Codec:编码解码器
 * Request,Response:用于网络请求的进和出
 * Util,Helper:都表示工具类
 * Mode,Type:枚举
 * Invoker,Invocation:invoker是一类接口,通常会以反射或者触发的方式,执行一些具体的业务逻辑
 * Initializer:初始化操作
 * Feture,Promise:用在多线程之间,进行数据传递.Feture代表一个操作将来的结果;Promise是为了让代码变得优美而存在的
 * Selector:根据一系列条件,获得相应的同类资源
 * Reporter:用来汇报某些执行结果
 * Constants:一般用于常量列表
 * Accessor:封装了一系列get和set方法的类.Accessor类一般是要通过计算来完成get和set,而不是直接操作变量
 * Generator:生成器,一般用于生成代码,生成id等
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2021-03-30 11:00:45
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@EnableScheduling
@EnableAsync
@SpringBootApplication
public class Application {

	/**
	 * 启动类
	 * 
	 * run():扫描springboot的上下文环境,装载自动配置类
	 * 
	 * @param args 当多环境启动时,可以加载不同的参数
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	public static void free() {
		Runtime runtime = Runtime.getRuntime();
		// 可用内存,单位字节,换算成m需要除以1024的平方
		System.out.println(runtime.freeMemory());
		// 总内存字节,单位字节,换算成m需要除以1024的平方
		System.out.println(runtime.totalMemory());
	}
}