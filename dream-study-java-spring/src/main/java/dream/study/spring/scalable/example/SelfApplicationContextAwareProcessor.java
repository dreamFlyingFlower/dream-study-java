package dream.study.spring.scalable.example;

import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.ResourceLoaderAware;

/**
 * #ApplicationContextAwareProcessor:该类没有扩展点,也不能外部继承,但内部有6个扩展点可供实现,这些点在bean实例化之后,初始化之前触发
 * 
 * 该类用于执行各种驱动接口,在bean实例化之后,属性填充之后,通过执行invokeAwareInterfaces()中的扩展接口来获取对应容器的变量
 * 
 * <pre>
 * {@link EnvironmentAware}:用于获取EnviromentAware的一个扩展类,可以获得系统内的所有参数
 * {@link EmbeddedValueResolverAware}:用于获取StringValueResolver的一个扩展类.
 * 		StringValueResolver用于获取基于String类型的properties的变量,一般用@Value的方式去获取.
 * 		如果实现了这个接口,把StringValueResolver缓存起来,通过这个类去获取String类型的变量,效果是一样的
 * {@link ResourceLoaderAware}:用于获取ResourceLoader的一个扩展类.
 * 		ResourceLoader可以用于获取classpath内所有的资源对象,可以扩展此类来拿到ResourceLoader对象
 * {@link ApplicationEventPublisherAware}:用于获取ApplicationEventPublisher的一个扩展类.
 * 		ApplicationEventPublisher可以用来发布事件,结合ApplicationListener来共同使用
 * {@link MessageSourceAware}:用于获取MessageSource的一个扩展类,MessageSource主要用来做国际化
 * {@link ApplicationContextAware}:用来获取ApplicationContext的一个扩展类.
 * 		ApplicationContext是Spring上下文管理器,可以手动的获取任何在Spring上下文注册的bean.
 * 		同时ApplicationContext也实现了BeanFactory,MessageSource,ApplicationEventPublisher等接口,也可以用来做相关接口的事情
 * </pre>
 *
 * @author 飞花梦影
 * @date 2022-10-18 00:02:52
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class SelfApplicationContextAwareProcessor {

}