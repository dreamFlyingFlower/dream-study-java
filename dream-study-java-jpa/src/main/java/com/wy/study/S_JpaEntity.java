package com.wy.study;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.id.Assigned;
import org.hibernate.id.ForeignGenerator;
import org.hibernate.id.GUIDGenerator;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.IdentityGenerator;
import org.hibernate.id.IncrementGenerator;
import org.hibernate.id.SelectGenerator;
import org.hibernate.id.UUIDHexGenerator;

import com.wy.model.Role;

import lombok.Getter;
import lombok.Setter;

/**
 * 使用jpa的实体类必须实现Serializable,否则报错,事例见{@link TestJpa}
 * 
 * {@link Entity}:声明当前类为实体类<br>
 * {@link Table}:声明实体类与表的关系,name属性可指定数据库表的名称,若不指定,则为实体类名<br>
 * {@link DynamicInsert}:动态生成sql语句,当需要插入的值为null时,该字段将不会插入,默认false
 * {@link DynamicUpdate}:动态生成sql语句,当需要更新的字段值为null时,默认不更新
 * {@link MappedSuperclass}:表明该类是一个公用基类,类中字段将映射到其子类所在的表中,不可和Entity,Table注解同时存在<br>
 * {@link Id}:表明该字段是主键 <br>
 * {@link Column}:表明是一个字段,name属性可设置数据库字段<br>
 * {@link Temporal}:只能在util.date和util.calendar字段上使用,将字段值转为sql.date,{@link #createtime}<br>
 * {@link Basic}:指定非约束明确的各个字段<br>
 * {@link Embedded}:指定类或它的值是一个可嵌入的类的实例的实体的属性<br>
 * {@link Transient}:指定的属性不持久化,表明该字段的值不会存储在数据库中<br>
 * {@link Access}:类属性的访问权限.若设置{@link AccessType#FIELD},则属性必须为public;
 * 若设置{@link AccessType#PROPERTY},通过getter和setter访问<br>
 * {@link UniqueConstraint}:指定的字段和用于主要或辅助表的唯一约束<br>
 * {@link ColumnResult}:参考使用select子句的SQL查询中的列名<br>
 * {@link ManyToMany}:多对多的表关系,2张表都需要添加该注解,一张表中定义了JoinTable,则另外一张表定义mappedBy<br>
 * ->{@link ManyToMany#mappedBy()}:另外一张表对应本表的属性名,只需要一张表写该属性即可<br>
 * {@link ManyToOne}:多对一的表关系,一个定义ManyToOne,另外一个表就要定义OneToMany<br>
 * {@link OneToMany}:一对多的个表关系,一个定义OneToMany,另外一个表就要定义ManyToOne<br>
 * {@link OneToOne}:定义了连接表之间有一个一对一的关系<br>
 * {@link JoinColumn}:指定一个实体组织或实体的集合,这是用在多对一和一对多关联<br>
 * {@link JoinTable}:多对多情况下指定一个中间关联表以及关联字段:<br>
 * ->{@link JoinTable#name()}:指定中间表的表名<br>
 * ->{@link JoinTable#joinColumns()}:指定本类中关联中间表的数据库字段名<br>
 * ->{@link JoinTable#inverseJoinColumns()}:指定中间表关联另外一张表的数据库字段名<br>
 * {@link NamedQueries}:指定命名查询的列表<br>
 * {@link NamedQuery}:指定使用静态名称的查询<br>
 * 
 * {@link GeneratedValue#strategy()}:主键生成策略,只有4种:<br>
 * {@link GenerationType#AUTO}:默认类型,主键由程序自己控制选择<br>
 * {@link GenerationType#TABLE}:使用一个特定的数据库表来保存主键<br>
 * {@link GenerationType#SEQUENCE}:使用序列,并非所有所有数据库都支持,mysql不支持,oracle支持<br>
 * {@link GenerationType#IDENTITY}:主键有数据库自动生成,也就是自增主键,oracle不支持<br>
 * 
 * {@link GeneratedValue#generator()}:主键生成策略,当不使用strategy()的策略时,需要特殊指定
 * {@link SequenceGenerator}:当strategy()的值为SEQUENCE时,指定序列的名称,见{@link #id2}
 * {@link TableGenerator}:当strategy()的值为TABLE时,指定序列的名称
 * {@link GenericGenerator}:自定义主键生成策略,配合GeneratedValue生成各种不同主键策略,见{@link #id4}:
 * {@link IdentifierGenerator}:自定义主键策略,需要实现该接口,见{@link #id3},spring中已经实现的有:
 * "uuid"->{@link UUIDHexGenerator}<br>
 * "assigned"->{@link Assigned}<br>
 * "identity"->{@link IdentityGenerator}<br>
 * "select"->{@link SelectGenerator}<br>
 * "sequence"->{@link SequenceGenerator}<br>
 * "increment"->{@link IncrementGenerator}<br>
 * "foreign"->{@link ForeignGenerator}<br>
 * "guid"->{@link GUIDGenerator}<br>
 * 
 * @author ParadiseWY
 * @date 2020-12-02 17:25:40
 * @git {@link https://github.com/mygodness100}
 */
@Entity
@Table(name = "tb_user")
@DynamicInsert
@DynamicUpdate
// @MappedSuperclass
@Getter
@Setter
public class S_JpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	/**
	 * 默认主键策略,适用于所有数据库
	 */
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	/**
	 * 主键自增,不适用于oracle
	 */
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id1;

	/**
	 * 主键以序列为准增加,不适用于mysql
	 */
	@SequenceGenerator(name = "sequenceUser", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceUser")
	private Long id2;

	/**
	 * 自定义主键,自定义主键的类需要写完整路径,名称可自定义
	 */
	@GenericGenerator(name = "createIdentify", strategy = "com.wy.common.CreateIdentify")
	@GeneratedValue(generator = "createIdentify")
	private Integer id3;

	/**
	 * 以uuid字符串为主键
	 */
	@GenericGenerator(name = "createUUID", strategy = "uuid")
	@GeneratedValue(generator = "createUUID")
	@Column(length = 50, nullable = false)
	private Integer id4;

	@Column
	private String username;

	@Column
	private String realname;

	@Column
	private String password;

	@Column
	private String sex;

	@Column
	private Integer age;

	@Column
	private Date birthday;

	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date createtime;

	/**
	 * 假设单个用户对应单个角色,单个角色对应多个用户,
	 * 
	 * ManyToOne:即多个用户对应单个角色,JpaRole对应的users字段上要添加OneToMany,指定mappedBy为字段名
	 * 
	 * JoinColumn:本表关联JpaRole表的字段名
	 */
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "role_id")
	private Role jpaRole;
}