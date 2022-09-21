package com.wy.aop;

import java.lang.reflect.Method;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.ConstructorInterceptor;
import org.aopalliance.intercept.Interceptor;
import org.aopalliance.intercept.Invocation;
import org.aopalliance.intercept.Joinpoint;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
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
import org.springframework.aop.Advisor;
import org.springframework.aop.IntroductionAdvisor;
import org.springframework.aop.aspectj.annotation.AbstractAspectJAdvisorFactory;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.aop.aspectj.annotation.BeanFactoryAspectJAdvisorsBuilder;
import org.springframework.aop.aspectj.annotation.ReflectiveAspectJAdvisorFactory;
import org.springframework.aop.config.AopConfigUtils;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AdvisorChainFactory;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.framework.AopProxyFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.framework.ProxyConfig;
import org.springframework.aop.framework.ProxyCreatorSupport;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.autoproxy.AbstractAdvisorAutoProxyCreator;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration.EnableTransactionManagementConfiguration.CglibAutoProxyConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration.EnableTransactionManagementConfiguration.JdkDynamicAutoProxyConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.wy.annotation.Logger;
import com.wy.service.SysLogService;
import com.wy.service.impl.SysLogServiceImpl;

/**
 * Spring Aop
 * 
 * 注意:当多个不同的切面类中有相同的切面拦截表达式时,以类的首字母排序,前面的先执行,和方法名无关;若要改变这种顺序,可以使用{@link Order}
 * 当同一个类中有切面相同时,以方法的首字母进行排序,前面的先执行,后面的后执行
 * 
 * <pre>
 * {@link EnableAspectJAutoProxy}:指定代理类型以及是否暴露代理对象
 * ->{@link EnableAspectJAutoProxy#proxyTargetClass()}:指定代理使用的是JDK动态代理还是CGLIB,默认false,使用JDK动态代理
 * ->{@link EnableAspectJAutoProxy#exposeProxy()}:指定是否暴露代理对象,通过 AopContext 可以进行访问,该功能在某些时候可防止事务失效
 * {@link #JdkDynamicAopProxy}:当使用JDK动态代理时的代理处理类,非public
 * {@link #CglibAopProxy}:当使用CGLIB动态代理时的代理处理类,非public
 * {@link TransactionAutoConfiguration}:事务自动配置类,会根据配置决定是使用JDK动态代理,还是CGLIB动态代理
 * {@link JdkDynamicAutoProxyConfiguration}:当spring.aop.proxy-target-class为false,使用JDK动态代理,默认代理
 * {@link CglibAutoProxyConfiguration}:当spring.aop.proxy-target-class为true时,使用CGLIB动态代理
 * {@link Aspect}:指定需要代理的类.默认切面类应该为单例的,但是当切面类为一个多例类时,指定预处理的切入点表达式
 * ->{@link Aspect#value()}:要么使用"",即不指定,且切面类为单例模式;若指定为多例模式,会报错;
 * 		当切面类为多例时,需要指定预处理的切入点表达式:perthis(切入点表达式),它支持指定切入点表达式,或者是用@Pointcut修饰的全限定方法名.
 * 		且当切面为多例时,类中其他注解的切面表达式无效,但是必须写,但是多例并不常用
 * {@link DeclareParents}:用于给被增强的方法提供新的方法,实现新的接口,给类增强.通常用在类无法被改变的情况,如在jar包中
 * </pre>
 * 
 * AOP原理:
 * 
 * <pre>
 * {@link AbstractAutowireCapableBeanFactory#initializeBean}:主要的创建代理方法
 * ->{@link AbstractAutowireCapableBeanFactory#applyBeanPostProcessorsBeforeInitialization}:创建代理对象
 * -->{@link AbstractAutowireCapableBeanFactory#doCreateBean}:最终在该方法中进行初始化
 * {@link AopProxyFactory}:AopProxy 代理工厂类,用于生产代理对象 AopProxy
 * {@link AopProxy}:代表一个AopProxy 代理对象,可以通过该对象构造代理对象实例
 * {@link Advised}:代表被 Advice 增强的对象,包括添加advisor的方法,添加advice等的方法
 * {@link ProxyConfig}:一个代理对象的配置信息,包括代理的各种属性,如基于接口还是基于类构造代理
 * {@link ProxyCreatorSupport}:AdvisedSupport 的子类,创建代理对象的支持类,内部包含 AopProxyFacory 工厂成员,可直接使用工厂成员创建 Proxy
 * ->{@link ProxyFactory}:用于生成代理对象实例的工厂类
 * {@link Advisor}:代表一个增强器提供者的对象,内部包含getAdvice()获取增强器
 * {@link AdvisorChainFactory}:获取增强器链的工厂接口,提供方法返回所有增强器
 * {@link org.springframework.aop.Pointcut}:切入点,用于匹配类和方法,满足切入点的条件是advice
 * {@link AopConfigUtils}:AOP主要实现类,注入AOP相关切面等bean
 * ->{@link AopConfigUtils#registerAspectJAnnotationAutoProxyCreatorIfNecessary}:根据注解定义的切点来自动代理相匹配的bean
 * -->{@link AnnotationAwareAspectJAutoProxyCreator}:注册相关的自动代理类
 * 
 * ->{@link AopConfigUtils#registerOrEscalateApcAsRequired}:根据注解定义的切点来自动代理相匹配的bean
 * <code>RootBeanDefinition beanDefinition = new RootBeanDefinition(cls);</code>
 * -->动态代理的{@link BeanPostProcessor}以BeanDefinition的形式注册到BeanDefinitionMaps中
 * <code>registry.registerBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME, beanDefinition);</code>
 * -->{@link DefaultListableBeanFactory#registerBeanDefinition}:AnnotationAwareAspectJAutoProxyCreator 将会被注册到 BeanDefinitionMaps 中
 * 
 * {@link AbstractAutoProxyCreator}:BeanPostProcessor 实现,用AOP代理包装每个符合条件的bean,在调用bean本身之前委托给指定的拦截器
 * ->{@link AbstractAutoProxyCreator#postProcessAfterInitialization}:对初始化之后的bean进行AOP代理
 * -->{@link AbstractAutoProxyCreator#wrapIfNecessary}:判断Class是否需要代理,若需要,返回代理类以及相关切面
 * --->{@link AbstractAdvisorAutoProxyCreator#getAdvicesAndAdvisorsForBean}:根据beanName判断是否需要代理
 * ---->{@link AbstractAdvisorAutoProxyCreator#findEligibleAdvisors}:找到项目中所有被{@link Aspect}修饰的类,进行排序之后返回
 * ------>{@link AnnotationAwareAspectJAutoProxyCreator#findCandidateAdvisors}:获取所有的切面,获取 Aspect 的实现
 * ------->{@link AbstractAdvisorAutoProxyCreator#findCandidateAdvisors}:找到XML配置文件声明的AOP增强
 * -------->{@link BeanFactoryAspectJAdvisorsBuilder#findCandidateAdvisors}:获取所有通过 Aspect 修饰的增强
 * --------->{@link ReflectiveAspectJAdvisorFactory#getAdvisors}:找到需要切面的类,获得{@link Before}, {@link After}等注解
 * --------->{@link ReflectiveAspectJAdvisorFactory#getAdvisorMethods}:循环找被 {@link Pointcut}修饰的方法
 * --------->{@link ReflectiveAspectJAdvisorFactory#getAdvisor}:创建真正的切面类并返回
 * ---------->{@link ReflectiveAspectJAdvisorFactory#getPointcut}:获取pointcut对象进行解析,将解析出来的pointcut表达式设置到属性中
 * ----------->{@link AbstractAspectJAdvisorFactory#findAspectJAnnotationOnMethod}:找到Pointcut,Around,Before等注解
 * ---------> #InstantiationModelAwarePointcutAdvisorImpl:创建真正的切面类并返回
 * 
 * ----->{@link AbstractAdvisorAutoProxyCreator#findAdvisorsThatCanApply}:模糊匹配当前类是否作用的pointcut表示中
 * 
 * ---->{@link AbstractAutoProxyCreator#createProxy}:将通用拦截,特殊拦截以及相关参数放入 ProxyFactory 中
 * ----->{@link ProxyFactory#getProxy()}:根据Class类型判断使用JDK动态代理或CGLIB代理,返回最终的代理对象
 * ------>{@link #JdkDynamicAopProxy#getProxy}:使用JDK动态代理生成代理类,非public
 * ------->{@link AopProxyUtils#completeProxiedInterfaces}:判断剔除不需要的代理类,返回所有需要代理的接口
 * 
 * ------>{@link #JdkDynamicAopProxy#invoke}:真实方法被调用时调用的代理方法
 * ------->{@link AopUtils#invokeJoinpointUsingReflection}:Advised接口或父类接口中定义的方法,直接反射调用,不应用通知
 * <code>this.advised.getInterceptorsAndDynamicInterceptionAdvice(method,targetClass):该方法返回所有的拦截器</code>
 * ------->{@link AdvisedSupport}:对 Advised 的构建提供支持,Advised的实现类以及ProxyConfig的子类
 * -------->{@link AdvisedSupport#getInterceptorsAndDynamicInterceptionAdvice}:获得可以应用到被拦截方法上的Interceptor列表
 * --------->{@link AdvisorChainFactory#getInterceptorsAndDynamicInterceptionAdvice}:获得Interceptor调用链,并将结果缓存.
 * 		从提供的配置实例config中获取advisor列表,遍历处理这些advisor,如果是{@link IntroductionAdvisor},
 * 		则判断此Adcisor能否应用到目标targetClass.如果是 PointcutAdvisor,则判断此 Advisor 能否应用到目标方法上.
 * 		将满足条阿金的Advisor通过AdvisorAdaptor转换成Interceptor列表返回
 * 
 * ------>{@link #CglibAopProxy#getProxy}:使用CGLIB动态代理生成代理类,非public
 * ------->{@link #CglibAopProxy.DynamicAdvisedInterceptor#intercept}:AOP最终的代理对象的代理方法
 * <code>this.advised.getInterceptorsAndDynamicInterceptionAdvice(method,targetClass):该方法返回所有的拦截器</code>
 * </pre>
 * 
 * 主要接口:
 * 
 * <pre>
 * {@link Advice}:增强标记接口
 * ->{@link Interceptor}:拦截器, Advice 子接口,标记拦截器,拦截器也是增强器的一种
 * ->{@link MethodInterceptor}:方法拦截器, {@link Interceptor}子接口,拦截方法并处理
 * ->{@link ConstructorInterceptor}:构造器拦截器, {@link Interceptor}子接口,拦截构造器并处理
 * {@link Joinpoint}:连接点.在拦截器中使用,封装了原方法调用的相关信息.如参数,对象信息以及直接调用原方法的proceed()
 * {@link Invocation}:JoinPoint 子类,添加了获取调用参数方法
 * {@link MethodInvocation}:Invocation 的子类,包含了获取调用方法的方法
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
	 * execution(* com..*serviceImpl.*()):拦截所有serviceImpl结尾的类的所有无参方法
	 * execution(* com..*serviceImpl.*(..)):拦截所有serviceImpl结尾的类的所有方法
	 * execution(* com..*serviceImpl.*(*)):拦截所有serviceImpl结尾的类的任意参数方法,必须有参数
	 * execution(* save*(..)):拦截所有以save开头的方法
	 * execution(* save*(..)) && args(username)):拦截所有save开头,且参数必须有username
	 * execution(* save*(..)) || execution(* update*(..)):拦截所有的save开头的方法,或者update开头的方法,||可以换成or
	 * !execution(* save*(..)):不拦截save开头的方法,!可以换成not,注意空格
	 * bean(userService):拦截容器中userService类中的所有方法
	 * bean(*Service):拦截容器中Service结尾的类中的所有方法
	 * 
	 * args:指定被拦截方法需要传递的形参,注意是形参,非参数类型.
	 * 如果其他切面方法使用了被PointCut修饰的方法,则其他切面也要加上该形参
	 * 
	 * 拦截注解:
	 * 以@annotation关键字实现,内容为需要拦截的注解的全限定类名
	 * </pre>
	 * 
	 * {@link Pointcut#argNames()}:指定切入点表达式参数.参数可以是execution或者args中的.
	 * 该参数可以不指定,若指定则必须和args关键字中的名称一致.
	 */
	@Pointcut(value = "execution(* com.wy..*.*(..)) && !execution(* com.wy..TestCrl.Test(..))  ")
	private void aspect() {}

	@Pointcut(value = "execution(* com.wy..*.*(..)) && !execution(* com.wy..TestCrl.Test(..)) && args(token) ")
	private void aspectArg(String token) {}

	@Before(value = "aspectArg(token) && args(username)")
	public void beforeAspectArg(String token, String username) {

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
	private void aspectAnnotation() {}
	
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
	public void beforeAspect(String username, String token) {}

	/**
	 * {@link Around}:定义一个环绕通知,在Before开始执行方法之前调用一次.方法执行完,在After执行完之后再调用一次
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
	public void exception(JoinPoint joinPoint, Throwable throwable) {}
}