package com.wy.base;

import java.util.Date;

import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

/**
 * 公用参数抽象类,可继承也可不继承,若不继承,必须实现Serializable接口
 * 
 * {@link MappedSuperclass}:表明该类是一个公用类,类中字段将映射到其子类所在的表中,不可和Entity,Table注解同时存在
 * 
 * @author 飞花梦影
 * @date 2021-01-06 20:08:33
 * @git {@link https://github.com/mygodness100}
 */
@MappedSuperclass
@Getter
@Setter
public class AbstractModel extends AbstractPager {

	private static final long serialVersionUID = 1L;

	private Date createtime;

	private Date updatetime;
}