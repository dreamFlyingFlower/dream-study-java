package com.wy.extension;

import org.springframework.beans.factory.FactoryBean;

/**
 * Spring通过反射机制利用bean的class属性指定子类去实例化bean.某些情况下,实例化Bean过程比较复杂,需要在bean中提供大量的配置信息.
 * 此时采用编码的方式可能会更简单,Spring为此提供了一个{@link FactoryBean}的工厂类,用户可以通过实现该接口定制实例化Bean的逻辑.
 * 
 * 用户可以扩展这个类,来为要实例化的bean作一个代理,比如为该对象的所有的方法作一个拦截,在调用前后输出一行log,模仿ProxyFactoryBean的功能
 *
 * @author 飞花梦影
 * @date 2022-10-18 00:12:46
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class SelfFactoryBean implements FactoryBean<SelfFactoryBean.SelfFactoryInnerBean> {

	@Override
	public SelfFactoryBean.SelfFactoryInnerBean getObject() throws Exception {
		System.out.println("[FactoryBean] getObject");
		return new SelfFactoryBean.SelfFactoryInnerBean();
	}

	@Override
	public Class<?> getObjectType() {
		return SelfFactoryBean.SelfFactoryInnerBean.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public static class SelfFactoryInnerBean {

	}
}