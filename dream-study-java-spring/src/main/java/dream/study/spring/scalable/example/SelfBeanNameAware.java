package dream.study.spring.scalable.example;

import org.springframework.beans.factory.BeanNameAware;

/**
 * {@link BeanNameAware}:该类是Aware扩展的一种,触发点在bean的初始化之前,也就是postProcessBeforeInitialization之前
 * 
 * 用户可以扩展这个点,在初始化bean之前拿到spring容器中注册的的beanName,来自行修改这个beanName的值
 *
 * @author 飞花梦影
 * @date 2022-10-18 00:07:04
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class SelfBeanNameAware implements BeanNameAware {

	@Override
	public void setBeanName(String name) {

	}
}