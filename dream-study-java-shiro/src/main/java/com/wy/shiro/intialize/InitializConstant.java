package com.wy.shiro.intialize;

import java.lang.reflect.Field;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import com.wy.shiro.constant.ResourceConstant;
import com.wy.shiro.constant.RoleConstant;
import com.wy.shiro.constant.SuperConstant;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class InitializConstant implements ServletContextAware {

	private ServletContext servletContext;

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	@PostConstruct
	public void onApplicationEvent() {
		loadConstant(SuperConstant.class);
		loadConstant(ResourceConstant.class);
		loadConstant(RoleConstant.class);
	}

	/**
	 * 加载常量类
	 */
	private void loadConstant(Class<?> classTag) {
		log.info("-------------------------初始化{}开始", classTag.getName());
		Field fields[] = classTag.getFields();
		for (Field field : fields) {
			String fieldName = field.getName();
			Object object = null;
			try {
				object = field.get(classTag);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			servletContext.setAttribute(classTag.getSimpleName() + "_" + fieldName, object);
			log.info(classTag.getSimpleName() + ":---key：{}，---value：{}", classTag.getSimpleName() + "_" + fieldName,
					object);
		}
		log.info("------------------------初始化{}结束", classTag.getName());
	}
}