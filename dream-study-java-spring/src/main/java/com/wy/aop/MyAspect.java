package com.wy.aop;

import java.lang.reflect.Method;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.ConstructorInterceptor;
import org.aopalliance.intercept.Interceptor;
import org.aopalliance.intercept.Invocation;
import org.aopalliance.intercept.Joinpoint;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.internal.lang.reflect.PointcutImpl;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.DeclareParents;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.aspectj.weaver.patterns.KindedPointcut;
import org.springframework.aop.Advisor;
import org.springframework.aop.IntroductionAdvisor;
import org.springframework.aop.aspectj.AbstractAspectJAdvice;
import org.springframework.aop.aspectj.AspectJMethodBeforeAdvice;
import org.springframework.aop.aspectj.annotation.AbstractAspectJAdvisorFactory;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.aop.aspectj.annotation.BeanFactoryAspectJAdvisorsBuilder;
import org.springframework.aop.aspectj.annotation.ReflectiveAspectJAdvisorFactory;
import org.springframework.aop.aspectj.autoproxy.AspectJAwareAdvisorAutoProxyCreator;
import org.springframework.aop.config.AopConfigUtils;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AdvisorChainFactory;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.framework.AopProxyFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.framework.DefaultAdvisorChainFactory;
import org.springframework.aop.framework.DefaultAopProxyFactory;
import org.springframework.aop.framework.ProxyConfig;
import org.springframework.aop.framework.ProxyCreatorSupport;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.aop.framework.adapter.AfterReturningAdviceInterceptor;
import org.springframework.aop.framework.adapter.MethodBeforeAdviceInterceptor;
import org.springframework.aop.framework.autoproxy.AbstractAdvisorAutoProxyCreator;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.aop.framework.autoproxy.BeanFactoryAdvisorRetrievalHelper;
import org.springframework.aop.interceptor.ExposeInvocationInterceptor;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration.EnableTransactionManagementConfiguration.CglibAutoProxyConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration.EnableTransactionManagementConfiguration.JdkDynamicAutoProxyConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.wy.annotation.Logger;
import com.wy.service.SysLogService;
import com.wy.service.impl.SysLogServiceImpl;

/**
 * Spring Aop
 * 
 * 注意:当多个不同的切面类中有相同的切面拦截表达式时,以类的首字母排序,前面的先执行,和方法名无关;可以使用{@link Order}改变顺序
 * 当同一个类中有切面相同时,以方法的首字母进行排序,前面的先执行,后面的后执行
 * 
 * 核心接口,类:
 * 
 * <pre>
 * {@link Advice}:增强标记接口
 * ->{@link Interceptor}:拦截器, Advice 子接口,标记拦截器,拦截器也是增强器的一种
 * ->{@link MethodInterceptor}:方法拦截器, {@link Interceptor}子接口,拦截方法并处理
 * ->{@link ConstructorInterceptor}:构造器拦截器, {@link Interceptor}子接口,拦截构造器并处理
 * {@link Joinpoint}:连接点.在拦截器中使用,封装了原方法调用的相关信息.如参数,对象信息以及直接调用原方法的proceed()
 * {@link Invocation}:JoinPoint 子类,添加了获取调用参数方法
 * {@link MethodInvocation}:Invocation 的子类,包含了获取调用方法的方法
 * {@link JoinPoint}:所有的切面方法都可以使用,当做参数传入
 * {@link ProceedingJoinPoint}:只有环绕通知可使用为参数
 * {@link AspectJAwareAdvisorAutoProxyCreator}:XML方式生成代理的主要类
 * {@link AbstractAutoProxyCreator}:AOP代理核心类,主要生成AOP代理对象
 * {@link AnnotationAwareAspectJAutoProxyCreator}:AbstractAutoProxyCreator子类,大部分功能在该类完成
 * {@link AopProxyFactory}:AopProxy 代理工厂类,主要构建AOP代理bean
 * ->{@link DefaultAopProxyFactory#createAopProxy()}:默认实现,根据被代理类的情况创建JDK代理或CGLIB代理
 * {@link AopProxy}:代表一个AopProxy 代理对象,可以通过该对象构造代理对象实例
 * ->{@link #JdkDynamicAopProxy}:当使用JDK动态代理时的代理处理类,非public
 * ->{@link #CglibAopProxy}:当使用CGLIB动态代理时的代理处理类,非public
 * {@link Advised}:代表被 Advice 增强的对象,包括添加advisor的方法,添加advice等的方法
 * {@link ProxyConfig}:一个代理对象的配置信息,包括代理的各种属性,如基于接口还是基于类构造代理
 * {@link ProxyCreatorSupport}:AdvisedSupport 的子类,创建代理对象的支持类,内部包含 AopProxyFacory 工厂成员,
 * 		可直接使用工厂成员创建 Proxy
 * ->{@link ProxyFactory}:用于生成代理对象实例的工厂类
 * {@link Advisor}:代表一个增强器提供者的对象,内部包含getAdvice()获取增强器
 * {@link AdvisorChainFactory}:获取增强器链的工厂接口,提供方法返回所有增强器
 * {@link org.springframework.aop.Pointcut}:切入点,用于匹配类和方法,满足切入点的条件是advice
 * 
 * {@link Aspect}:指定需要代理的类.默认切面类应该为单例的,但是当切面类为一个多例类时,指定预处理的切入点表达式
 * ->{@link Aspect#value()}:要么使用"",即不指定,且切面类为单例模式;若指定为多例模式,会报错;
 * 		当切面类为多例时,需要指定预处理的切入点表达式:perthis(切入点表达式),它支持指定切入点表达式,
 * 		或者是用@Pointcut修饰的全限定方法名.
 * 		且当切面为多例时,类中其他注解的切面表达式无效,但是必须写,但是多例并不常用
 * {@link DeclareParents}:用于给被增强的方法提供新的方法,实现新的接口,给类增强.通常用在类无法被改变的情况,如在jar包中
 * {@link PointcutImpl}:当方法上有{@link Pointcut}时,该注解会被PointcutImpl解析
 * {@link KindedPointcut}:当方法上没有 Pointcut时,方法的切入点表达式会被KindedPointcut解析
 * </pre>
 * 
 * AOP相关自动配置类,在{@link AbstractApplicationContext#invokeBeanFactoryPostProcessors}中被调用:
 * 
 * <pre>
 * {@link EnableAspectJAutoProxy}:指定代理类型以及是否暴露代理对象,引入AnnotationAwareAspectJAutoProxyCreator定义
 * ->{@link EnableAspectJAutoProxy#proxyTargetClass()}:指定代理使用的是JDK动态代理还是CGLIB,默认false,使用JDK动态代理
 * ->{@link EnableAspectJAutoProxy#exposeProxy()}:指定是否暴露代理对象,通过 AopContext 可以进行访问,某些情况下可防止事务失效
 * {@link AspectJAutoProxyRegistrar#registerBeanDefinitions}:由 EnableAspectJAutoProxy 引入,
 * 		往容器中注入beanName为internalAutoProxyCreator的{@link AnnotationAwareAspectJAutoProxyCreator}beanDefinition.
 * ->{@link AopConfigUtils#registerAspectJAnnotationAutoProxyCreatorIfNecessary}:根据注解定义的切点来自动代理相匹配的bean
 * ->{@link AopConfigUtils#registerOrEscalateApcAsRequired}:注册到beanDefinitionMap中
 * {@link JdkDynamicAutoProxyConfiguration}:当spring.aop.proxy-target-class为false,使用JDK动态代理,默认代理
 * {@link CglibAutoProxyConfiguration}:当spring.aop.proxy-target-class为true时,使用CGLIB动态代理
 * </pre>
 * 
 * AOP原理--注册代理切面AnnotationAwareAspectJAutoProxyCreator
 * 
 * <pre>
 * 1.{@link AbstractApplicationContext#registerBeanPostProcessors}:后置处理拦截bean的创建,
 * 		AOP会在此时创建内部beanName为internalAutoProxyCreator的实例{@link AnnotationAwareAspectJAutoProxyCreator}
 * 2.{@link AbstractBeanFactory#getBean(String, Class)}:获取bean实例,如果获取不到就创建
 * 3.{@link DefaultSingletonBeanRegistry#getSingleton(String, ObjectFactory)}:获取单例bean
 * 4.{@link AbstractAutowireCapableBeanFactory#createBean}:创建bean
 * 5.{@link AbstractAutowireCapableBeanFactory#doCreateBean}:创建bean
 * 6.{@link AbstractAutowireCapableBeanFactory#createBeanInstance}:创建bean实例
 * 7.{@link AbstractAutowireCapableBeanFactory#initializeBean}:初始化bean
 * 8.{@link AbstractAutowireCapableBeanFactory#invokeAwareMethods}:给AnnotationAwareAspectJAutoProxyCreator设置beanFactory,
 * 		此处实际是调用的父类{@link AbstractAdvisorAutoProxyCreator#setBeanFactory()}
 * 9.{@link AbstractAdvisorAutoProxyCreator#setBeanFactory}:设置beanFactory,同时调用父类的setBeanFactory
 * 10.{@link AbstractAdvisorAutoProxyCreator#initBeanFactory}:AnnotationAwareAspectJAutoProxyCreator重写了该方法
 * 11.{@link AnnotationAwareAspectJAutoProxyCreator#initBeanFactory}:构建{@link ReflectiveAspectJAdvisorFactory}和
 * 		BeanFactoryAspectJAdvisorsBuilderAdapter对象,此时AnnotationAwareAspectJAutoProxyCreator已经创建完成
 * </pre>
 * 
 * AOP原理--创建代理bean:
 * 
 * <pre>
 * 1.{@link AbstractApplicationContext#finishBeanFactoryInitialization}:创建剩余的单例bean
 * 2.{@link DefaultListableBeanFactory#preInstantiateSingletons}:创建剩余的单例bean
 * 3.{@link AbstractBeanFactory#getBean(String)}:创建剩余的单例bean,以下步骤同创建AnnotationAwareAspectJAutoProxyCreator
 * 4.{@link AbstractAutowireCapableBeanFactory#resolveBeforeInstantiation}:后置处理器返回一个代理对象,有则返回,没有则创建
 * 5.{@link AbstractAutowireCapableBeanFactory#applyBeanPostProcessorsBeforeInstantiation}:bean前置调用
 * ->5.1.{@link AbstractAutoProxyCreator#postProcessBeforeInstantiation()}:实际调用
 * 6.{@link AbstractAutowireCapableBeanFactory#applyBeanPostProcessorsAfterInitialization}:bean前置调用
 * ->6.1.{@link AbstractAutoProxyCreator#postProcessAfterInitialization()}:实际调用,生成代理类的入口,返回代理后的bean
 * 7.{@link AbstractAutoProxyCreator#wrapIfNecessary()}:判断Class是否需要代理,若需要,返回代理类以及相关切面
 * ->7.1.{@link AbstractAutoProxyCreator#isInfrastructureClass()}:判断是否是基础接口
 * ->7.2.{@link AbstractAutoProxyCreator#shouldSkip()}:判断是否需要跳过的类
 * ->7.3.{@link AbstractAutoProxyCreator#getAdvicesAndAdvisorsForBean()}:调用{@link AbstractAdvisorAutoProxyCreator}
 * 8.{@link AbstractAdvisorAutoProxyCreator#getAdvicesAndAdvisorsForBean}:获取所有的增强器,找到能当前bean能用的,
 * 		有则保存到advisedBeans中
 * 9.{@link AbstractAdvisorAutoProxyCreator#findEligibleAdvisors}:找到所有的候选增强器,{@link Advisor}实现类
 * ->9.1{@link AnnotationAwareAspectJAutoProxyCreator#findCandidateAdvisors}:找到所有被{@link Aspect}修饰的bean增强器
 * ->9.1.1.{@link BeanFactoryAspectJAdvisorsBuilder#buildAspectJAdvisors}:获取所有通过 Aspect 修饰的增强
 * ->9.1.1.1.{@link AnnotationAwareAspectJAutoProxyCreator.BeanFactoryAspectJAdvisorsBuilderAdapter#isEligibleBean}:判断是否为切面
 * ->9.1.1.2.{@link ReflectiveAspectJAdvisorFactory#getAdvisors}:找到需要切面的类,获得{@link Before}, {@link After}等注解
 * ->9.1.1.3.{@link ReflectiveAspectJAdvisorFactory#getAdvisorMethods}:循环找被 {@link Pointcut}修饰的方法
 * ->9.1.1.4.{@link ReflectiveAspectJAdvisorFactory#getAdvisor}:创建真正的切面类并返回
 * ->9.1.1.4.1.{@link ReflectiveAspectJAdvisorFactory#getPointcut}:获取pointcut对象进行解析,将解析出来的pointcut表达式设置到属性中
 * ->9.1.1.4.1.1.{@link AbstractAspectJAdvisorFactory#findAspectJAnnotationOnMethod}:找到Pointcut,Around,Before等注解
 * ->9.1.1.5.{@link #InstantiationModelAwarePointcutAdvisorImpl}:创建真正的切面类并返回
 * 
 * ->9.2.{@link AbstractAdvisorAutoProxyCreator#findCandidateAdvisors}:由子类调用,获取所有的切面,获取 Advisor 的实现类bean
 * ->9.2.1.{@link BeanFactoryAdvisorRetrievalHelper#findAdvisorBeans()}:查找所有 Advisor 实现类bean
 * 
 * ->9.3.{@link AbstractAdvisorAutoProxyCreator#findAdvisorsThatCanApply}:获取当前bean能使用的增强器,模糊匹配当前类是否作用的pointcut表示中
 * 10.{@link AbstractAdvisorAutoProxyCreator#createProxy}:获取所有的增强方法,将通用拦截,特殊拦截以及相关参数保存到ProxyFactory中
 * 11.{@link ProxyFactory#getProxy()}:根据Class类型判断使用JDK动态代理或CGLIB代理,返回最终的代理对象
 * 12.{@link ProxyFactory#createAopProxy}:创建代理工厂,默认为DefaultAopProxyFactory
 * 13.{@link AopProxyFactory#createAopProxy}:AopProxy 代理工厂类,调用DefaultAopProxyFactory
 * 14.{@link DefaultAopProxyFactory#createAopProxy}:根据目标类的情况自动创建JDK代理或CGLIB代理,返回之后完成代理创建
 * ->14.1.{@link #JdkDynamicAopProxy#getProxy}:使用JDK动态代理生成代理类,非public
 * ->14.1.1.{@link AopProxyUtils#completeProxiedInterfaces}:剔除不需要的代理类,返回所有需要代理的接口,实例化JdkDynamicAopProxy时创建
 * ->14.1.2.{@link #JdkDynamicAopProxy#invoke}:真实方法调用时代理方法才会被调用,如果使用JDK代理,则该方法为切面入口
 * ->14.1.2.1.{@link AopUtils#invokeJoinpointUsingReflection}:Advised接口或父类接口中定义的方法,直接反射调用,不应用通知
 * ->14.1.2.2.this.advised.getInterceptorsAndDynamicInterceptionAdvice():该方法返回所有的拦截器链,调用AdvisedSupport
 * ->14.1.2.3.{@link AdvisedSupport#getInterceptorsAndDynamicInterceptionAdvice}:对Advised的实现类以及ProxyConfig的子类,
 * 		获得可以应用到被拦截方法上的Interceptor列表
 * ->14.1.2.3.1.{@link AdvisorChainFactory#getInterceptorsAndDynamicInterceptionAdvice}:获得Interceptor调用链,并将结果缓存.
 * 		从提供的配置实例config中获取advisor列表,遍历处理这些advisor,如果是{@link IntroductionAdvisor},
 * 		则判断此Adcisor能否应用到目标targetClass.如果是 PointcutAdvisor,则判断此 Advisor 能否应用到目标方法上.
 * 		将满足条件的Advisor通过AdvisorAdaptor转换成Interceptor列表返回
 * ->14.1.2.3.1.1.{@link DefaultAdvisorChainFactory#getInterceptorsAndDynamicInterceptionAdvice}:AdvisorChainFactory的默认实现类,
 * 		最终会执行切面方法.该方法根据情况会返回包含{@link ExposeInvocationInterceptor},{@link MethodBeforeAdviceInterceptor},
 * 		{@link AfterReturningAdviceInterceptor}等,和切面相关的类的数组,之后循环执行
 * 
 * ->14.1.2.4.{@link ReflectiveMethodInvocation#proceed}:调用切面点通过拦截器链,获得返回值
 * ->14.1.2.4.1.{@link MethodBeforeAdviceInterceptor#invoke}:调用{@link AspectJMethodBeforeAdvice}
 * ->14.1.2.4.2.{@link AspectJMethodBeforeAdvice#before}:调用{@link AbstractAspectJAdvice}
 * ->14.1.2.4.2.1.{@link AbstractAspectJAdvice#invokeAdviceMethodWithGivenArgs}:执行前置方法,即执行被@Before修饰的方法
 * 
 * ->14.2.{@link #CglibAopProxy#getProxy}:真实方法被调用时代理方法才会被调用,使用CGLIB动态代理生成代理类,	非public,该方法为切面入口
 * ->14.2.1.{@link #CglibAopProxy.DynamicAdvisedInterceptor#intercept}:AOP最终的代理对象的代理方法
 * 				<code>this.advised.getInterceptorsAndDynamicInterceptionAdvice(method,targetClass):该方法返回所有的拦截器</code>
 * ->14.2.2.{@link AdvisedSupport#getInterceptorsAndDynamicInterceptionAdvice()}:根据目标方法获取代理的拦截器链
 * ->14.2.3.{@link AdvisorChainFactory#getInterceptorsAndDynamicInterceptionAdvice}:调用DefaultAdvisorChainFactory
 * ->14.2.3.1.{@link DefaultAdvisorChainFactory#getInterceptorsAndDynamicInterceptionAdvice}:利用工厂获取拦截器链
 * ->14.2.4.{@link #CglibMethodInvocation#proceed}:创建一个方法拦截对象,依次调用拦截器链中的方法,其他流程和JDK流程相似
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2021-10-21 13:35:36
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Component
@Aspect
public class MyAspect {

	/**
	 * 让@DeclareParents中vlaue指代的类具有被标识的接口中方法,defaultImpl为接口的默认实现类,可在各个阶段的增强中使用
	 * 
	 * value可以指定单个接口,+表示该接口的所有实现类都有被标识的接口中的方法
	 */
	@DeclareParents(value = "com.wy.service+", defaultImpl = SysLogServiceImpl.class)
	private SysLogService sysLogService;

	/**
	 * {@link Pointcut}:声明一个通用切入点,用来配置需要被拦截的类,方法.若该方法为public,其他类中也可以使用全限定类名来使用
	 * 
	 * 切入点的解析并不是在创建解析Bean对象时执行,而是在refresh()的invokeBeanFactoryPostProcessors()中执行
	 * 
	 * {@link Pointcut#value()}:声明切入点的表达式
	 * 
	 * <pre>
	 * 表达式参数拦截:
	 * 固定语法,使用execution和args等关键字,execution参数分为6段:
	 * 第一段:访问修饰符,可省略
	 * 第二段:返回值类型,必须.通配符*表示可返回任意类型.若返回指定类型,需要写全路径名,如返回String,需要写java.lang.String.
	 * 		也可以直接写void,表示无返回值.或者!void,表示只要不是void的就可以
	 * 第三段:需要拦截的包名.2个点,表示对包下的子包同样拦截,不写表示只对当前包下的类,方法进行拦截
	 * 第四段:需要拦截的类,*表示包下所有类
	 * 第五段:需要拦截的类中的方法名,*表示所有方法.若需要指定参数,也需要写类全路径名,多个用逗号隔开
	 * 第六段:括号里表示的是参数个数,..表示参数可有可无,可以有多个
	 * 
	 * eg:在表达式中也可以使用逻辑表达式,如||,or,!,not,&&,and
	 * execution(* com.wy..*.*(..)):拦截com.wy包以及子包下所有类的所有方法
	 * execution(* com..*ServiceImpl.*()):拦截所有serviceImpl结尾的类的所有无参方法
	 * execution(* com..*ServiceImpl.*(..)):拦截所有serviceImpl结尾的类的所有方法
	 * execution(* com..*ServiceImpl.*(*)):拦截所有serviceImpl结尾的类的任意参数方法,必须有参数
	 * execution(* save*(..)):拦截所有以save开头的方法
	 * execution(* save*(..)) && args(username)):拦截所有save开头,且参数必须有username
	 * execution(* save*(..)) || execution(* update*(..)):拦截所有的save开头的方法,或者update开头的方法,||可以换成or
	 * !execution(* save*(..)):不拦截save开头的方法,!可以换成not,注意空格
	 * bean(userService):拦截容器中userService类中的所有方法
	 * bean(*Service):拦截容器中Service结尾的类中的所有方法
	 * reference pointcut:表示引用其他命名切入点,只有@AspectJ风格支持
	 * this:用于匹配当前AOP代理对象类型的执行方法.可能包括引入接口也类型匹配
	 * within:用于匹配指定类型内的方法执行.
	 * target:用于匹配当前目标对象类型的执行方法,不包括引入接口也类型匹配
	 * args:指定被拦截方法需要传递的形参,注意是形参,非参数类型.如果其他切面方法使用了被PointCut修饰的方法,则其他切面也要加上该形参
	 * 
	 * `@annotation`:注解拦截,内容为需要拦截的注解的全限定类名
	 * `@within`:用于匹配所有持有指定注解类型内的方法
	 * `@target`:用于匹配当前目标对象类型的执行方法,其中目标对象持有指定的注解
	 * `@args`:用于匹配当前执行的方法传入的参数持有指定注解的执行
	 * </pre>
	 * 
	 * {@link Pointcut#argNames()}:指定切入点表达式参数.参数可以是execution或者args中的.
	 * 
	 * 该参数可以不指定,若指定则必须和args关键字中的名称一致.
	 */
	@Pointcut(value = "execution(* com.wy..*.*(..)) && !execution(* com.wy..TestCrl.Test(..))  ")
	private void aspect() {
	}

	@Pointcut(value = "execution(* com.wy..*.*(..)) && !execution(* com.wy..TestCrl.Test(..)) && args(token) ")
	private void aspectArg(String token) {
	}

	@Before(value = "aspectArg(token) && args(username)")
	public void beforeAspectArg(String token, String username) {

	}

	/**
	 * ..表示拦截任意参数开头的方法,适用于拦截只含有某些特定参数的方法
	 * 
	 * @param username
	 */
	@Before(value = "args(..,username)")
	public void beforeAspectArgMulti(String username) {

	}

	@Before("aspect() && this(sysLogService)")
	public void beforeAspectDeclareParents(SysLogService sysLogService) {
		// 在前置中使用
		sysLogService.create(null);
	}

	/**
	 * 注解拦截
	 */
	@Pointcut("@annotation(com.wy.annotation.Logger)")
	private void aspectAnnotation() {
	}

	/**
	 * 第一种拦截注解:直接使用拦截表达式,通过反射获取方法,从方法上获取被拦截的注解
	 */
	@Before("aspectAnnotation()")
	public void beforeAspectAnnotation() {

	}

	/**
	 * 第二种拦截注解,拦截注解里的表达式中的参数必须和形参对应
	 * 
	 * @param logger
	 */
	@Before("aspectAnnotation() && @annotation(logger)")
	public void beforeAspectAnnotation(Logger logger) {
		System.out.println(logger.description());
	}

	/**
	 * {@link Before}:定义一个前置通知,在调用方法之前调用.需要设置一个拦截的切入点,即被PointCut修改的方法名
	 * 
	 * args:指定被拦截的方法参数个数以及形参名,即只会拦截参数名为username的方法
	 */
	@Before("aspect() && args(username,token)")
	public void beforeAspect(String username, String token) {
	}

	/**
	 * {@link Around}:定义一个环绕通知,在Before开始执行方法之前调用一次.方法执行完,在After执行完之后再调用一次.
	 * 
	 * 如果方法执行有异常,则环绕后通知不执行
	 * 
	 * @param joinPoint 包含了执行方法的相关信息,方法名,参数等
	 * @throws Throwable 会被AfterThrowing接收
	 */
	@Around("aspect()")
	public Object aroundAspect(ProceedingJoinPoint joinPoint) throws Throwable {
		// 获得方法参数
		joinPoint.getArgs();
		// 如果定义了环绕拦截,该方法必须执行.该方法实际上就是执行真正的方法,且最好有返回值
		Object object = joinPoint.proceed();
		return object;
	}

	@Around("aspect()")
	public Object aroundAspectLog(ProceedingJoinPoint joinPoint) throws Throwable {
		// 获得方法参数
		joinPoint.getArgs();
		// 获得签名方法
		Signature signature = joinPoint.getSignature();
		// 判断当前签名方法是否为方法签名
		if (signature instanceof MethodSignature) {
			MethodSignature methodSignature = (MethodSignature) signature;
			// 获得当前执行的方法
			Method method = methodSignature.getMethod();
			System.out.println(method.getName());
			System.out.println(method.getModifiers());
		}
		// 如果定义了环绕拦截,该方法必须执行.该方法实际上就是执行真正的方法,且最好有返回值
		return joinPoint.proceed();
	}

	/**
	 * {@link AfterReturning}:定义一个后置通知,即方法正常执行完之后,结果未返回之前执行.当方法抛出异常时,后置通知将不会被执行
	 * 在这里不能使用ProceedingJoinPoint,只能使用JoinPoint,否则报异常;该注解既可拿到参数,也可以拿到结果
	 * 
	 * returning:接收返回结果的参数,即afterAspect的形参,类型可自定义
	 */
	@AfterReturning(pointcut = "aspect()", returning = "result")
	public void afterAspect(Object result) {
		System.out.println("方法执行结果为:" + result);
	}

	/**
	 * {@link After}:定义一个最终通知,在AfterReturning后执行,相当于finally中的代码
	 * 
	 * 当方法抛出异常时,该方法在AfterThrowing修饰的方法之后执行
	 */
	@After("aspect()")
	public void after() {

	}

	/**
	 * {@link AfterThrowing}:定义一个异常通知.throwable表示接收异常的参数名,即exception的形参
	 * 
	 * 在这里不能使用ProceedingJoinPoint,只能使用JoinPoint,否则报异常
	 */
	@AfterThrowing(pointcut = "aspect()", throwing = "throwable")
	public void exception(JoinPoint joinPoint, Throwable throwable) {
	}
}