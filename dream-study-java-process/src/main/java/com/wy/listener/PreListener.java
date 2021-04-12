package com.wy.listener;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * 在web程序启动的时候就执行的程序,需要配合spring注解使用
 * @author wanyang
 */
@Component
public class PreListener implements InitializingBean  {

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("spring加载完其他项目后先加载此项目");
	}
}
