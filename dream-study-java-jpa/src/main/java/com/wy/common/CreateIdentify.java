package com.wy.common;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

/**
 * 自定义数据库主键
 * @必须实现一个无参的构造函数,否则出错
 * @author wanyang 2018年7月16日
 */
public class CreateIdentify implements IdentifierGenerator{
	
	public CreateIdentify() {
		
	}

	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
		return System.currentTimeMillis();
	}
}