package dream.study.spring.scalable.example;

import java.lang.reflect.Constructor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;

/**
 * {@link SmartInstantiationAwareBeanPostProcessor}:该接口继承自{@link InstantiationAwareBeanPostProcessor},多了3个扩展点:
 * 
 * <pre>
 * {@link SmartInstantiationAwareBeanPostProcessor#predictBeanType}:该触发点发生在postProcessBeforeInstantiation之前.
 * 		该方法用于预测Bean的类型,返回第一个预测成功的Class类型,如果不能预测返回null;
 * 		当调用BeanFactory.getType(name)时,若通过bean的名字无法得到bean类型信息时就调用该回调方法来决定类型信息.通常很少用该扩展点
 * {@link SmartInstantiationAwareBeanPostProcessor#determineCandidateConstructors}:该触发点发生在postProcessBeforeInstantiation之后.
 * 		用于确定该bean的构造函数之用,返回的是该bean的所有构造函数列表.用户可以扩展这个点,来自定义选择相应的构造器来实例化这个bean
 * {@link SmartInstantiationAwareBeanPostProcessor#getEarlyBeanReference}:该触发点发生在postProcessAfterInstantiation之后.
 * 		当有循环依赖的场景,bean实例化好之后,为了防止有循环依赖,会提前暴露回调方法,用于bean实例化的后置处理.
 * 		这个方法就是在提前暴露的回调方法中触发
 * </pre>
 *
 * @author 飞花梦影
 * @date 2022-10-17 23:55:29
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class SelfSmartInstantiationAwareBeanPostProcessor implements SmartInstantiationAwareBeanPostProcessor {

	@Override
	public Class<?> predictBeanType(Class<?> beanClass, String beanName) throws BeansException {
		System.out.println("[SelfSmartInstantiationAwareBeanPostProcessor] predictBeanType " + beanName);
		return beanClass;
	}

	@Override
	public Constructor<?>[] determineCandidateConstructors(Class<?> beanClass, String beanName) throws BeansException {
		System.out.println("[SelfSmartInstantiationAwareBeanPostProcessor] determineCandidateConstructors " + beanName);
		return null;
	}

	@Override
	public Object getEarlyBeanReference(Object bean, String beanName) throws BeansException {
		System.out.println("[SelfSmartInstantiationAwareBeanPostProcessor] getEarlyBeanReference " + beanName);
		return bean;
	}
}