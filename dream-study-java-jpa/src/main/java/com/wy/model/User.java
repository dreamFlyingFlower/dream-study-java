package com.wy.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

import com.alibaba.fastjson.annotation.JSONField;
import com.wy.convert.UserTypeConverter;
import com.wy.enums.UserType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "ti_user")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long userId;

	@Column(length = 32, nullable = false)
	@Length(max = 32, message = "用户名长度不能超过32")
	private String username;

	/**
	 * 序列化默认使用jackson,若是要使用fastjson,需要配置.此处配置序列化的时候,不序列化密码
	 */
	@Column(length = 32, nullable = false)
	@JSONField(serialize = false)
	private String password;

	@Column(length = 16)
	private String realname;

	@Column
	private Integer age;

	@Column(length = 1)
	private String sex;

	@Column
	private Date birthday;

	@Column(length = 64)
	private String address;

	@Column(length = 32)
	private String email;

	@Column(length = 32)
	private String idCard;

	@Column(length = 16)
	private String telphone;

	@Column
	private Integer state;

	/**
	 * 若在数据库中像放入枚举的string类型,而不是索引值,可以在注解上添加该注解
	 */
	@Enumerated(EnumType.STRING)
	/**
	 * 作用等同于Enumerated,但是需要些一个转换器
	 */
	@Convert(converter = UserTypeConverter.class)
	private UserType userType;

	/**
	 * 用户和用户扩展信息是一对一的关系,应该使用OneToOne注解,2张表都要有对方的实体属性,且都要添加OneToOne注解
	 * 
	 * {@link OneToOne}:一对一关系<br>
	 * -> {@link OneToOne#CascadeType()):级联属性,默认无操作,若是使用PERSIST,则新增user的同时新增userinfo ->
	 * {@link OneToOne#fetch()):加载方式,默认懒加载,单表可用,但是关联表需要用EAGER,否则在级联修改时报错
	 * 
	 * {@link OneToMany},{@link ManyToOne}:一对多,多对一使用方式同OneToOne
	 * 
	 * JoinColumn维护一个外键关系,Userinfo类本身就能代表一张表,而name表示user通过userinfo中那个字段进行关联
	 * 同时Userinfo类中的User对象字段,需要给OneToOne的mappedBy方法赋值为User中Userinfo的属性名
	 */
	@OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private Userinfo userinfo;

	/**
	 * {@link ManyToMany}:多对多,Role对应的users字段上也要添加ManyToMany,指定mappedBy为User中List<Role>的字段名
	 * 
	 * {@link JoinTable}:中间关联表,name表示关联表名称,joinColumns表示本表关联中间表对应的字段名,
	 * inverseJoinColumns表示中间表关联另外一张表的字段名
	 */
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JoinTable(name = "tr_user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles;
}